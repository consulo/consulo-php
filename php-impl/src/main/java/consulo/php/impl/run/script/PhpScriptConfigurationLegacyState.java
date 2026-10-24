package consulo.php.impl.run.script;

/**
 * Fields of the PHP script configuration before the fragment model, read once by
 * {@link PhpScriptConfiguration#convertLegacyState}.
 *
 * @author VISTALL
 * @since 2026-10-23
 */
public class PhpScriptConfigurationLegacyState {
    public String SCRIPT_PATH;
    public String PROGRAM_ARGUMENTS;
    public String WORKING_DIRECTORY;
    public boolean PASS_PARENT_ENVS = true;
}
