import org.jspecify.annotations.NullMarked;

/**
 * Composer as an external system: importing {@code composer.json}, auto-reload, Composer tasks and the "Composer" new
 * project wizard.
 *
 * @author VISTALL
 * @since 30-Aug-22
 */
@NullMarked
module consulo.php.composer.impl {
    requires consulo.module.creation.api;
    requires consulo.module.ui.api;
    requires consulo.project.ui.api;
    requires consulo.file.editor.api;
    requires transitive consulo.external.system.api;

    requires consulo.php.api;

    requires consulo.json.api;
    requires consulo.json.jom.api;
    requires consulo.json.scheme.api;

    exports consulo.php.composer;
    exports consulo.php.composer.execution;
    exports consulo.php.composer.icon;
    exports consulo.php.composer.importProvider;
    exports consulo.php.composer.jom;
    exports consulo.php.composer.localize;
    exports consulo.php.composer.module.extension;
    exports consulo.php.composer.project;
    exports consulo.php.composer.setting;
    exports consulo.php.composer.task;
    exports consulo.php.composer.ui;
    exports consulo.php.composer.wizard;

    opens consulo.php.composer.setting to consulo.util.xml.serializer;
}
