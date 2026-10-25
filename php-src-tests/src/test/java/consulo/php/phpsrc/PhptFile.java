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

package consulo.php.phpsrc;

import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * One php-src {@code .phpt} test: named sections like {@code --TEST--}, {@code --FILE--}, {@code --EXPECT--}.
 *
 * @see <a href="https://qa.php.net/phpt_details.php">PHPT format</a>
 */
public record PhptFile(String relativePath, Map<String, String> sections) {
    private static final Pattern SECTION = Pattern.compile("^--([A-Z_]+)--$", Pattern.MULTILINE);

    public static PhptFile load(Path root, Path file) throws IOException {
        String text = new String(Files.readAllBytes(file), StandardCharsets.UTF_8).replace("\r\n", "\n");
        String relativePath = root.relativize(file).toString().replace('\\', '/');
        return new PhptFile(relativePath, parseSections(text));
    }

    static Map<String, String> parseSections(String text) {
        Map<String, String> sections = new LinkedHashMap<>();
        Matcher matcher = SECTION.matcher(text);
        String name = null;
        int start = 0;
        while (matcher.find()) {
            if (name != null) {
                sections.put(name, text.substring(start, matcher.start()));
            }
            name = matcher.group(1);
            start = Math.min(matcher.end() + 1, text.length());
        }
        if (name != null) {
            sections.put(name, text.substring(start));
        }
        return sections;
    }

    /**
     * PHP source under test, or null for tests without inline source (e.g. {@code --FILE_EXTERNAL--}).
     */
    public @Nullable String source() {
        String file = sections.get("FILE");
        if (file != null) {
            return file;
        }
        String fileEof = sections.get("FILEEOF");
        if (fileEof != null) {
            // FILEEOF: same as FILE, but without the trailing newline
            return fileEof.endsWith("\n") ? fileEof.substring(0, fileEof.length() - 1) : fileEof;
        }
        return null;
    }

    /**
     * True when PHP itself rejects the source at parse time, so our parser must report an error too.
     * Parse errors of {@code eval()}'d or stream-included code are not about this file.
     */
    public boolean expectsParseError() {
        for (String key : new String[]{"EXPECT", "EXPECTF", "EXPECTREGEX"}) {
            String expected = sections.get(key);
            if (expected == null) {
                continue;
            }
            for (String line : expected.split("\n")) {
                if (line.contains("Parse error:") && !line.contains("eval()'d code") && !line.contains("://")) {
                    return true;
                }
            }
        }
        return false;
    }
}
