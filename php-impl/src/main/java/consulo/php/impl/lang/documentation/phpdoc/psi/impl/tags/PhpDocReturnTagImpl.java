package consulo.php.impl.lang.documentation.phpdoc.psi.impl.tags;

import java.util.ArrayList;
import java.util.List;

import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.documentation.phpdoc.psi.PhpDocType;
import consulo.php.impl.lang.documentation.phpdoc.psi.impl.PhpDocPsiElementImpl;
import consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import jakarta.annotation.Nonnull;

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

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}

	@Override
	public String[] getTypes()
	{
		List<String> types = new ArrayList<String>();
		PhpPsiElement child = getFirstPsiChild();
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

	@Override
	public String getTypeString()
	{
		StringBuffer types = new StringBuffer();
		PhpPsiElement child = getFirstPsiChild();
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
