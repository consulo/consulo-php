package org.consulo.php.lang.documentation.phpdoc.psi.impl.tags;

import java.util.ArrayList;
import java.util.List;

import net.jay.plugins.php.lang.documentation.phpdoc.psi.PhpDocType;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.impl.PhpDocPsiElementImpl;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import org.consulo.php.lang.psi.elements.PHPPsiElement;

import com.intellij.lang.ASTNode;

/**
 * @author jay
 * @date Jun 29, 2008 2:32:57 AM
 */
public class PhpDocReturnTagImpl extends PhpDocPsiElementImpl implements PhpDocReturnTag
{

	public PhpDocReturnTagImpl(ASTNode node)
	{
		super(node);
	}

	public String[] getTypes()
	{
		List<String> types = new ArrayList<String>();
		PHPPsiElement child = getFirstPsiChild();
		while(child != null)
		{
			if(child instanceof PhpDocType)
			{
				types.add(((PhpDocType) child).getType());
			}
			child = child.getNextPsiSibling();
		}

		return types.toArray(new String[types.size()]);
	}

	public String getTypeString()
	{
		StringBuffer types = new StringBuffer();
		PHPPsiElement child = getFirstPsiChild();
		while(child != null)
		{
			if(child instanceof PhpDocType)
			{
				types.append(((PhpDocType) child).getType());
			}
			child = child.getNextPsiSibling();
			if(child != null && child instanceof PhpDocType)
			{
				types.append("|");
			}
		}

		return types.toString();
	}
}
