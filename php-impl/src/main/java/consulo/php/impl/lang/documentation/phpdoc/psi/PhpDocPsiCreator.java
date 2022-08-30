package consulo.php.impl.lang.documentation.phpdoc.psi;

import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.psi.PsiElement;
import consulo.php.impl.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import consulo.php.impl.lang.documentation.phpdoc.psi.impl.PhpDocTypeImpl;
import consulo.php.impl.lang.documentation.phpdoc.psi.impl.tags.PhpDocReturnTagImpl;
import consulo.php.impl.lang.documentation.phpdoc.psi.impl.tags.PhpDocVarTagImpl;
import consulo.php.impl.lang.psi.impl.PhpElementImpl;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.php.lang.documentation.phpdoc.psi.PhpDocPsiElement;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jay
 * @date Jun 29, 2008 12:03:19 AM
 */
public class PhpDocPsiCreator implements PhpDocElementTypes
{
	public static final Map<String, Class<? extends PhpDocPsiElement>> tagMap = new HashMap<String, Class<? extends PhpDocPsiElement>>();

	static
	{
		tagMap.put("@var", PhpDocVarTagImpl.class);
		tagMap.put("@return", PhpDocReturnTagImpl.class);
	}

	public static PsiElement createElement(ASTNode node)
	{
		IElementType type = node.getElementType();

		if(type == phpDocTag)
		{
			final String tagName = node.getText().split("\\s", 2)[0];
			final Class<? extends PhpDocPsiElement> klass = tagMap.get(tagName);
			if(klass != null)
			{
				try
				{
					final Constructor<? extends PhpDocPsiElement> constructor = klass.getDeclaredConstructor(ASTNode.class);
					return constructor.newInstance(node);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		if(type == phpDocType)
		{
			return new PhpDocTypeImpl(node);
		}

		return new PhpElementImpl(node)
		{
			@Override
			public void accept(@Nonnull PhpElementVisitor visitor)
			{
				visitor.visitPhpElement(this);
			}
		};
	}

}
