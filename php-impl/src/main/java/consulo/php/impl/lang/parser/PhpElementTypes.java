package consulo.php.impl.lang.parser;

import consulo.language.ast.IElementType;
import consulo.php.impl.lang.psi.PhpStubElements;


/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public interface PhpElementTypes extends com.jetbrains.php.lang.parser.PhpElementTypes
{
	IElementType PARAMETER = PhpStubElements.PARAMETER;
	IElementType NAMESPACE = PhpStubElements.NAMESPACE;
	IElementType DEFINE = PhpStubElements.DEFINE;
	IElementType CONSTANT_ITEM = PhpStubElements.CONSTANT_ITEM;
	IElementType CLASS_CONSTANT = PhpStubElements.CLASS_FIELD;
	IElementType CLASS = PhpStubElements.CLASS;
	IElementType FUNCTION = PhpStubElements.FUNCTION;
	IElementType CLASS_FIELD = PhpStubElements.CLASS_FIELD;
}
