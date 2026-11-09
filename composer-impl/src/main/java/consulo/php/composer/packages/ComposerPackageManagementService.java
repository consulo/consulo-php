/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package consulo.php.composer.packages;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import consulo.application.Application;
import consulo.application.progress.ProgressIndicator;
import consulo.application.progress.Task;
import consulo.http.HttpRequests;
import consulo.localize.LocalizeValue;
import consulo.module.Module;
import consulo.php.composer.ComposerConstants;
import consulo.php.composer.ComposerUtil;
import consulo.php.composer.setting.ComposerExecutionSettings;
import consulo.process.ExecutionException;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.util.CapturingProcessUtil;
import consulo.process.util.ProcessOutput;
import consulo.repository.ui.InstalledPackage;
import consulo.repository.ui.PackageInstallScope;
import consulo.repository.ui.PackageManagementServiceEx;
import consulo.repository.ui.RepoPackage;
import consulo.repository.ui.SearchablePackageManagementService;
import consulo.util.concurrent.AsyncResult;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The requirements of the composer.json of a module, from Packagist, changed by composer.
 */
public class ComposerPackageManagementService extends PackageManagementServiceEx implements SearchablePackageManagementService {
    private static final String PACKAGIST = "https://packagist.org/";
    // the metadata of composer 2: the tagged releases of a package, the newest first
    private static final String METADATA = "https://repo.packagist.org/p2/";

    static final PackageInstallScope REQUIRE = new PackageInstallScope("require", LocalizeValue.localizeTODO("require"));
    static final PackageInstallScope REQUIRE_DEV = new PackageInstallScope("require-dev", LocalizeValue.localizeTODO("require-dev"));

    private static final class ComposerInstalledPackage extends InstalledPackage {
        private final PackageInstallScope myScope;

        private ComposerInstalledPackage(String name, @Nullable String version, PackageInstallScope scope) {
            super(name, version);
            myScope = scope;
        }

        @Override
        public @Nullable String getTooltipText() {
            return myScope.name().get();
        }
    }

    private final Module myModule;
    private final VirtualFile myComposerJson;
    private final Path myPackageDirectory;
    private final Map<String, String> myDescriptions = new ConcurrentHashMap<>();

    public ComposerPackageManagementService(Module module, VirtualFile composerJson) {
        myModule = module;
        myComposerJson = composerJson;
        myPackageDirectory = Objects.requireNonNull(composerJson.getParent(), "a composer.json is in a directory").toNioPath();
    }

    private Path getPackageDirectory() {
        return myPackageDirectory;
    }

    /**
     * {@code php}, {@code ext-json}: the platform a package requires, which is no package of a repository
     */
    private static boolean isPlatformRequirement(String name) {
        return !name.contains("/");
    }

    @Override
    public List<PackageInstallScope> getInstallScopes() {
        return List.of(REQUIRE, REQUIRE_DEV);
    }

    @Override
    public @Nullable PackageInstallScope getDefaultInstallScope() {
        return REQUIRE;
    }

    @Override
    public boolean canUseExtraOptions() {
        return true;
    }

    @Override
    public boolean canUpdatePackage(InstalledPackage installedPackage) {
        return !isPlatformRequirement(installedPackage.getName());
    }

    @Override
    public List<RepoPackage> getAllPackages() throws IOException {
        return List.of();
    }

    @Override
    public List<RepoPackage> getPackages(String searchQuery, int from, int to) {
        if (StringUtil.isEmptyOrSpaces(searchQuery)) {
            return List.of();
        }
        int size = Math.max(1, to - from);
        try {
            String url = PACKAGIST + "search.json?q=" + URLEncoder.encode(searchQuery, StandardCharsets.UTF_8)
                + "&per_page=" + size + "&page=" + (from / size + 1);
            JsonObject root = JsonParser.parseString(HttpRequests.request(url).productNameAsUserAgent().readString(null)).getAsJsonObject();
            List<RepoPackage> packages = new ArrayList<>();
            Set<String> names = new HashSet<>();
            if (root.get("results") instanceof JsonArray results) {
                for (JsonElement element : results) {
                    JsonObject result = element.getAsJsonObject();
                    String name = string(result, "name");
                    // a virtual package provided by others comes with the name of a real one too, after it
                    if (name == null || !names.add(name)) {
                        continue;
                    }
                    String description = string(result, "description");
                    if (!StringUtil.isEmptyOrSpaces(description)) {
                        myDescriptions.put(name, description);
                    }
                    packages.add(new RepoPackage(name, PACKAGIST));
                }
            }
            return packages;
        }
        catch (IOException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getPageSize() {
        return 50;
    }

    @Override
    public List<? extends InstalledPackage> getInstalledPackagesList() throws IOException {
        // the file is read from the disk: composer has just rewritten it, before its psi follows
        Path composerJson = getPackageDirectory().resolve(ComposerConstants.COMPOSER_JSON);
        if (!Files.isRegularFile(composerJson)) {
            return List.of();
        }
        JsonObject root;
        try {
            root = JsonParser.parseString(Files.readString(composerJson)).getAsJsonObject();
        }
        catch (RuntimeException e) {
            // a composer.json being edited
            return List.of();
        }
        Map<String, String> installedVersions = installedVersions(getPackageDirectory());
        List<InstalledPackage> packages = new ArrayList<>();
        addInstalled(packages, requirements(root, REQUIRE.id()), REQUIRE, installedVersions);
        addInstalled(packages, requirements(root, REQUIRE_DEV.id()), REQUIRE_DEV, installedVersions);
        return packages;
    }

    private static Map<String, String> requirements(JsonObject root, String section) {
        Map<String, String> requirements = new LinkedHashMap<>();
        if (root.get(section) instanceof JsonObject object) {
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                requirements.put(entry.getKey(), entry.getValue().isJsonPrimitive() ? entry.getValue().getAsString() : "");
            }
        }
        return requirements;
    }

    private static void addInstalled(List<InstalledPackage> packages, Map<String, String> requirements, PackageInstallScope scope, Map<String, String> installedVersions) {
        for (Map.Entry<String, String> entry : requirements.entrySet()) {
            // the version the vendor directory has, else the constraint composer.json asks for
            String version = installedVersions.get(entry.getKey());
            packages.add(new ComposerInstalledPackage(entry.getKey(), version != null ? version : entry.getValue(), scope));
        }
    }

    /**
     * The versions of vendor/composer/installed.json: {@code {"packages": [...]}} of composer 2, a plain array of composer 1
     */
    private static Map<String, String> installedVersions(Path packageDirectory) {
        Path installed = packageDirectory.resolve(ComposerConstants.VENDOR_DIRECTORY).resolve("composer").resolve("installed.json");
        Map<String, String> versions = new HashMap<>();
        if (!Files.isRegularFile(installed)) {
            return versions;
        }
        try {
            JsonElement root = JsonParser.parseString(Files.readString(installed));
            JsonElement packages = root.isJsonObject() ? root.getAsJsonObject().get("packages") : root;
            if (packages instanceof JsonArray array) {
                for (JsonElement element : array) {
                    if (element instanceof JsonObject pkg) {
                        String name = string(pkg, "name");
                        String version = string(pkg, "version");
                        if (name != null && version != null) {
                            versions.put(name, version);
                        }
                    }
                }
            }
        }
        catch (IOException | RuntimeException ignored) {
            // an unreadable lock of the vendor directory - the constraints are shown
        }
        return versions;
    }

    @Override
    public void installPackage(RepoPackage repoPackage, @Nullable String version, @Nullable PackageInstallScope scope, @Nullable String extraOptions, Listener listener) {
        String name = repoPackage.getName();
        run(name, require(version == null ? name : name + ":" + version, scope, extraOptions), listener);
    }

    @Override
    public void installPackage(RepoPackage repoPackage, @Nullable String version, boolean forceUpgrade, @Nullable String extraOptions, Listener listener, boolean installToUser) {
        installPackage(repoPackage, version, (PackageInstallScope) null, extraOptions, listener);
    }

    @Override
    public void updatePackage(InstalledPackage installedPackage, @Nullable String version, Listener listener) {
        String name = installedPackage.getName();
        // the requirement stays in the section it is in
        PackageInstallScope scope = installedPackage instanceof ComposerInstalledPackage composerPackage ? composerPackage.myScope : null;
        run(name, require(version == null ? name : name + ":" + version, scope, null), listener);
    }

    @Override
    public void uninstallPackages(List<InstalledPackage> installedPackages, Listener listener) {
        // composer removes a requirement of require-dev only with --dev, so each section is removed by a command of its own
        List<String> require = new ArrayList<>();
        List<String> requireDev = new ArrayList<>();
        for (InstalledPackage installedPackage : installedPackages) {
            boolean dev = installedPackage instanceof ComposerInstalledPackage composerPackage && REQUIRE_DEV.equals(composerPackage.myScope);
            (dev ? requireDev : require).add(installedPackage.getName());
        }
        remove(require, false, listener);
        remove(requireDev, true, listener);
    }

    private void remove(List<String> names, boolean dev, Listener listener) {
        if (names.isEmpty()) {
            return;
        }
        List<String> arguments = new ArrayList<>();
        arguments.add("remove");
        if (dev) {
            arguments.add("--dev");
        }
        arguments.addAll(names);
        run(names.size() == 1 ? names.get(0) : null, arguments, listener);
    }

    private static List<String> require(String packageSpec, @Nullable PackageInstallScope scope, @Nullable String extraOptions) {
        List<String> arguments = new ArrayList<>();
        arguments.add("require");
        arguments.add(packageSpec);
        if (scope != null && REQUIRE_DEV.id().equals(scope.id())) {
            arguments.add("--dev");
        }
        if (extraOptions != null) {
            for (String option : extraOptions.trim().split("\\s+")) {
                if (!option.isEmpty()) {
                    arguments.add(option);
                }
            }
        }
        return arguments;
    }

    private void run(@Nullable String packageName, List<String> arguments, Listener listener) {
        new Task.Backgroundable(myModule.getProject(), LocalizeValue.of(ComposerUtil.toPresentation(arguments)), true) {
            @Override
            public void run(ProgressIndicator indicator) {
                listener.operationStarted(packageName);
                ErrorDescription error = null;
                try {
                    ComposerExecutionSettings settings = ComposerUtil.createExecutionSettings(myModule.getProject(), getPackageDirectory().toString());
                    GeneralCommandLine commandLine = ComposerUtil.createCommandLine(settings, getPackageDirectory(), arguments);
                    ProcessOutput output = CapturingProcessUtil.execAndGetOutput(commandLine);
                    if (output.getExitCode() != 0) {
                        String message = StringUtil.notNullize(StringUtil.nullize(output.getStderr().trim()), "Exit code " + output.getExitCode());
                        error = new ErrorDescription(lastLine(message), commandLine.getCommandLineString(), output.getStdout() + output.getStderr(), null);
                    }
                }
                catch (ExecutionException e) {
                    error = ErrorDescription.fromMessage(StringUtil.notNullize(e.getMessage(), e.getClass().getName()));
                }
                // composer.json is changed by composer: its import follows the change of the file
                myComposerJson.refresh(false, false);
                listener.operationFinished(packageName, error);
            }
        }.queue();
    }

    private static String lastLine(String text) {
        List<String> lines = StringUtil.split(text, "\n");
        return lines.isEmpty() ? text : lines.get(lines.size() - 1);
    }

    @Override
    public AsyncResult<List<String>> fetchPackageVersions(String packageName) {
        if (isPlatformRequirement(packageName)) {
            return AsyncResult.resolved(List.of());
        }
        return async(() -> {
            List<String> versions = new ArrayList<>();
            for (JsonObject release : releases(packageName)) {
                String version = string(release, "version");
                if (version != null) {
                    versions.add(version);
                }
            }
            return versions;
        });
    }

    @Override
    public AsyncResult<String> fetchPackageDetails(String packageName) {
        String description = myDescriptions.get(packageName);
        if (description != null || isPlatformRequirement(packageName)) {
            return AsyncResult.resolved(StringUtil.escapeXmlEntities(StringUtil.notNullize(description)));
        }
        return async(() -> {
            List<JsonObject> releases = releases(packageName);
            if (releases.isEmpty()) {
                return "";
            }
            // the minified metadata has the whole data in its first release only
            JsonObject newest = releases.get(0);
            StringBuilder html = new StringBuilder();
            String text = string(newest, "description");
            if (text != null) {
                myDescriptions.put(packageName, text);
                html.append(StringUtil.escapeXmlEntities(text));
            }
            String homepage = string(newest, "homepage");
            if (homepage != null) {
                html.append("<br/><a href=\"").append(StringUtil.escapeXmlEntities(homepage)).append("\">")
                    .append(StringUtil.escapeXmlEntities(homepage)).append("</a>");
            }
            return html.toString();
        });
    }

    @Override
    public boolean shouldFetchLatestVersionsForOnlyInstalledPackages() {
        return true;
    }

    @Override
    public AsyncResult<String> fetchLatestVersion(InstalledPackage pkg) {
        if (isPlatformRequirement(pkg.getName())) {
            return AsyncResult.resolved(null);
        }
        return async(() -> {
            for (JsonObject release : releases(pkg.getName())) {
                String version = string(release, "version");
                // the newest stable one - the releases are the newest first
                if (version != null && !version.contains("-")) {
                    return version;
                }
            }
            return null;
        });
    }

    private static List<JsonObject> releases(String packageName) throws IOException {
        String text = HttpRequests.request(METADATA + packageName + ".json").productNameAsUserAgent().readString(null);
        JsonObject root = JsonParser.parseString(text).getAsJsonObject();
        List<JsonObject> releases = new ArrayList<>();
        if (root.get("packages") instanceof JsonObject packages && packages.get(packageName) instanceof JsonArray array) {
            for (JsonElement element : array) {
                if (element instanceof JsonObject release) {
                    releases.add(release);
                }
            }
        }
        return releases;
    }

    private static @Nullable String string(JsonObject object, String key) {
        JsonElement element = object.get(key);
        return element != null && element.isJsonPrimitive() ? element.getAsString() : null;
    }

    private static <T> AsyncResult<T> async(Callable<T> callable) {
        AsyncResult<T> result = AsyncResult.undefined();
        Application.get().executeOnPooledThread(() -> {
            try {
                result.setDone(callable.call());
            }
            catch (Exception e) {
                result.rejectWithThrowable(e);
            }
        });
        return result;
    }
}
