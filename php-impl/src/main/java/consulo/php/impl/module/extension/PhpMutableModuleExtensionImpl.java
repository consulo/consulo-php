package consulo.php.impl.module.extension;

import consulo.content.bundle.Sdk;
import consulo.disposer.Disposable;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.extension.ModuleExtension;
import consulo.module.extension.ModuleExtensionWithSdk;
import consulo.module.extension.MutableModuleInheritableNamedPointer;
import consulo.module.ui.extension.ModuleExtensionBundleBoxBuilder;
import consulo.php.PhpLanguageLevel;
import consulo.php.localize.PhpLocalize;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.php.module.extension.PhpMutableModuleExtension;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.ui.ComboBox;
import consulo.ui.Component;
import consulo.ui.TextAttribute;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.layout.VerticalLayout;
import consulo.ui.util.LabeledBuilder;
import consulo.util.lang.StringUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.*;

/**
 * @author VISTALL
 * @since 04.07.13.
 */
public class PhpMutableModuleExtensionImpl extends PhpModuleExtensionImpl implements PhpMutableModuleExtension<PhpModuleExtensionImpl> {
    public PhpMutableModuleExtensionImpl(@Nonnull String id, @Nonnull ModuleRootLayer module) {
        super(id, module);
    }

    @Nonnull
    @Override
    public MutableModuleInheritableNamedPointer<Sdk> getInheritableSdk() {
        return (MutableModuleInheritableNamedPointer<Sdk>) super.getInheritableSdk();
    }

    @Override
    @Nonnull
    public MutableModuleInheritableNamedPointer<PhpLanguageLevel> getInheritableLanguageLevel() {
        return myLanguageLevel;
    }

    @RequiredUIAccess
    @Nullable
    @Override
    public Component createConfigurationComponent(@Nonnull Disposable disposable, @Nonnull Runnable runnable) {
        MutableModuleInheritableNamedPointer<PhpLanguageLevel> languageLevel = getInheritableLanguageLevel();

        List<Object> items = new ArrayList<>(Arrays.asList(PhpLanguageLevel.VALUES));
        Map<Module, String> inheritedLevels = new HashMap<>();
        for (Module module : ModuleManager.getInstance(getModule().getProject()).getModules()) {
            if (module == getModule()) {
                continue;
            }

            ModuleExtension<?> extension = ModuleUtilCore.getExtension(module, getId());
            if (extension instanceof ModuleExtensionWithSdk<?> sdkExtension && sdkExtension.getInheritableSdk().getModule() != getModule()) {
                items.add(module);

                PhpModuleExtension<?> phpExtension = ModuleUtilCore.getExtension(module, PhpModuleExtension.class);
                if (phpExtension != null) {
                    inheritedLevels.put(module, phpExtension.getLanguageLevel().getShortName());
                }
            }
        }

        Object selected = languageLevel.get();
        String moduleName = languageLevel.getModuleName();
        if (moduleName != null) {
            Module module = languageLevel.getModule();
            if (module != null) {
                selected = module;
            }
            else {
                items.add(moduleName);
                selected = moduleName;
            }
        }

        ComboBox<Object> versionBox = ComboBox.create(items);
        versionBox.setRender((presentation, item) ->
        {
            Object value = item.getValue();
            if (value instanceof PhpLanguageLevel level) {
                presentation.append(level.getShortName());
                if (!StringUtil.isEmpty(level.getDescription())) {
                    presentation.append(" " + level.getDescription(), TextAttribute.GRAYED);
                }
            }
            else if (value instanceof Module module) {
                presentation.withIcon(PlatformIconGroup.nodesModule());
                presentation.append(module.getName(), TextAttribute.REGULAR_BOLD);

                String inheritedLevel = inheritedLevels.get(module);
                if (inheritedLevel != null) {
                    presentation.append(" (" + inheritedLevel + ")", TextAttribute.GRAYED);
                }
            }
            else if (value instanceof String name) {
                presentation.withIcon(PlatformIconGroup.nodesModule());
                presentation.append(name, TextAttribute.ERROR_BOLD);
            }
        });
        versionBox.setValue(selected);
        versionBox.addValueListener(event -> {
            Object value = event.getValue();
            if (value instanceof Module module) {
                languageLevel.set(module.getName(), null);
            }
            else if (value instanceof PhpLanguageLevel level) {
                languageLevel.set(null, level.getShortName());
            }
            else if (value != null) {
                languageLevel.set(value.toString(), null);
            }
        });

        VerticalLayout layout = VerticalLayout.create();
        layout.add(ModuleExtensionBundleBoxBuilder.createAndDefine(this, disposable, runnable).build());
        layout.add(LabeledBuilder.filled(PhpLocalize.phpModuleExtensionVersionLabel(), versionBox));
        return layout;
    }

    @Override
    public void setEnabled(boolean b) {
        myIsEnabled = b;
    }

    @Override
    public boolean isModified(@Nonnull PhpModuleExtensionImpl extension) {
        return isModifiedImpl(extension) || !extension.getInheritableLanguageLevel().equals(getInheritableLanguageLevel());
    }
}
