package consulo.php.lang.documentation.phpdoc.psi.impl.tags;

import java.util.ArrayList;
import java.util.List;

import consulo.php.lang.documentation.phpdoc.psi.PhpDocType;
import consulo.php.lang.documentation.phpdoc.psi.impl.PhpDocPsiElementImpl;
import consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import consulo.php.lang.psi.PhpElement;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
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

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}

	@Override
	public String[] getTypes()
	{
		List<String> types = new ArrayList<String>();
		PhpElement child = getFirstPsiChild();
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
		PhpElement child = getFirstPsiChild();
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
