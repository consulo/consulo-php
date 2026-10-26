package consulo.php.impl.lang.documentation.phpdoc.psi.impl;

import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElementResolveResult;
import consulo.language.psi.ResolveResult;
import consulo.php.impl.lang.psi.resolve.types.PhpTypeInference;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date Jun 29, 2008 2:20:02 AM
 */
public class PhpDocTypeImpl extends PhpDocPsiElementImpl implements PhpDocType
{

	public PhpDocTypeImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}

	@Override
	public String getName()
	{
		return getText();
	}

	@Nonnull
	@Override
	public PhpType getType()
	{
		return PhpTypeInference.getDocType(this);
	}

	@Override
	@RequiredReadAction
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		List<ResolveResult> result = new ArrayList<>();
		PhpIndex index = PhpIndex.getInstance(getProject());
		for(String type : getType().getTypes())
		{
			String fqn = type.endsWith("[]") ? type.substring(0, type.indexOf("[]")) : type;
			if(!fqn.startsWith("\\") || PhpType.isPrimitiveType(fqn))
			{
				continue;
			}
			for(PhpClass phpClass : index.getAnyByFQN(fqn))
			{
				result.add(new PsiElementResolveResult(phpClass));
			}
		}
		return result.toArray(ResolveResult.EMPTY_ARRAY);
	}
}
