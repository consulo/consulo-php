package consulo.php.lang.psi;

/**
 * @author VISTALL
 * @since 2019-03-11
 */
public interface PhpUseListStatement extends PhpElement
{
	PhpUse[] getUses();
}
