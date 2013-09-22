package org.consulo.php.lang.documentation.phpdoc.psi;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.consulo.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import org.consulo.php.lang.documentation.phpdoc.psi.impl.PhpDocTypeImpl;
import org.consulo.php.lang.documentation.phpdoc.psi.impl.tags.PhpDocReturnTagImpl;
import org.consulo.php.lang.documentation.phpdoc.psi.impl.tags.PhpDocVarTagImpl;
import org.consulo.php.lang.psi.impl.PhpElementImpl;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.HashMap;

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
			public void accept(@NotNull PhpElementVisitor visitor)
			{
				visitor.visitPhpElement(this);
			}
		};
	}

}
