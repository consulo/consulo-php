package consulo.php.completion;

import java.util.ArrayList;
import java.util.List;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.impl.PhpConstantReferenceImpl;
import consulo.php.lang.psi.impl.PhpVariableReferenceImpl;
import consulo.php.lang.psi.resolve.PhpResolveProcessor;
import consulo.php.lang.psi.resolve.PhpVariantsProcessor;
import consulo.php.lang.psi.resolve.ResolveUtil;

/**
 * @author VISTALL
 * @since 2019-04-14
 */
public class PhpCompletionContributor extends CompletionContributor
{
	public PhpCompletionContributor()
	{
		extend(CompletionType.BASIC, StandardPatterns.psiElement(PhpTokenTypes.VARIABLE), (parameters, context, result) -> {
			PsiElement position = parameters.getPosition();

			PsiElement parent = position.getParent();
			if(parent instanceof PhpVariableReferenceImpl)
			{
				PhpVariableReferenceImpl reference = (PhpVariableReferenceImpl) parent;

				UsageContext usageContext = reference.createUsageContext();

				reference.processForCompletion(phpNamedElement -> {
					LookupElement element = PhpVariantsUtil.getLookupItem(phpNamedElement, usageContext);
					result.addElement(element);

					return true;
				});
			}
		});

		extend(CompletionType.BASIC, StandardPatterns.psiElement(PhpTokenTypes.IDENTIFIER), (parameters, context, result) -> {
			PsiElement position = parameters.getPosition();

			PsiElement parent = position.getParent();

			if(parent instanceof PhpConstantReferenceImpl)
			{
				PhpConstantReferenceImpl reference = (PhpConstantReferenceImpl) parent;

				UsageContext usageContext = reference.createUsageContext();
				ClassUsageContext classUsageContext = reference.createClassUsageContext();

				PhpVariantsProcessor variantsProcessor = new PhpVariantsProcessor(reference)
				{
					@Override
					protected boolean isAppropriateDeclarationType(PsiElement possibleDeclaration)
					{
						PhpResolveProcessor.ElementKind kind = PhpResolveProcessor.getKind(possibleDeclaration);
						return kind == PhpResolveProcessor.ElementKind.PARAMETER || kind == PhpResolveProcessor.ElementKind.VARIABLE;
					}
				};

				ResolveUtil.treeWalkUp(reference, variantsProcessor);
				for(PhpNamedElement phpNamedElement : variantsProcessor.getVariants())
				{
					LookupElement element = PhpVariantsUtil.getLookupItem(phpNamedElement, usageContext);
					result.addElement(element);
				}

				List<PhpClass> classes = new ArrayList<>();
				reference.processForCompletion(phpNamedElement -> {
					classes.add((PhpClass) phpNamedElement);
					return true;
				});

				List<LookupElement> lookupItemsForClasses = PhpVariantsUtil.getLookupItemsForClasses(classes, classUsageContext);

				result.addAllElements(lookupItemsForClasses);
			}
		});
	}
}
