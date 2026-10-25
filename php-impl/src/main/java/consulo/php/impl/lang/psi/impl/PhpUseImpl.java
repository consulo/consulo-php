package consulo.php.impl.lang.psi.impl;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.language.ast.TokenSet;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.php.impl.lang.psi.PhpNameResolver;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.resolve.PsiScopeProcessor;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpUseImpl extends PhpElementImpl implements PhpUse
{
	public PhpUseImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitUse(this);
	}

	@Nonnull
	@Override
	public ClassReference[] getClassReferences()
	{
		return findChildrenByClass(ClassReference.class);
	}

	@Nullable
	@Override
	@RequiredReadAction
	public String getAliasName()
	{
		PsiElement as = findChildByType(PhpTokenTypes.kwAS);
		if(as == null)
		{
			return null;
		}
		PsiElement alias = findChildByType(PhpTokenTypes.IDENTIFIER);
		return alias == null ? null : alias.getText();
	}

	@Nullable
	@Override
	@RequiredReadAction
	public String getFQN()
	{
		ClassReference reference = findChildByClass(ClassReference.class);
		if(reference == null)
		{
			return null;
		}
		String name = PhpNameResolver.nameText(reference);
		// group use: 'use A\\{B, C}' - the prefix is a sibling of the items
		PsiElement parent = getParent();
		ClassReference prefix = parent == null ? null : PsiTreeUtil.getChildOfType(parent, ClassReference.class);
		if(prefix != null && parent.getNode().findChildByType(PhpTokenTypes.LBRACE) != null)
		{
			name = PhpNameResolver.nameText(prefix) + "\\" + name;
		}
		return name.startsWith("\\") ? name : "\\" + name;
	}

	@Override
	public boolean isClassImport()
	{
		TokenSet kinds = TokenSet.create(PhpTokenTypes.kwFUNCTION, PhpTokenTypes.kwCONST);
		if(getNode().findChildByType(kinds) != null)
		{
			return false;
		}
		PsiElement parent = getParent();
		return parent == null || parent.getNode().findChildByType(kinds) == null;
	}

	@Override
	@RequiredReadAction
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
	{
		ClassReference[] classReferences = getClassReferences();
		for(ClassReference classReference : classReferences)
		{
			PsiElement element = classReference.resolve();
			if(element == null)
			{
				continue;
			}

			if(!processor.execute(element, state))
			{
				return false;
			}
		}
		return true;
	}
}
