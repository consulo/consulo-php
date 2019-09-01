package consulo.php.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpDefine;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import consulo.php.completion.keywords.PhpKeywordsCompletionProvider;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.impl.*;
import consulo.php.lang.psi.resolve.PhpResolveProcessor;
import consulo.php.lang.psi.resolve.PhpVariantsProcessor;
import consulo.php.lang.psi.resolve.ResolveUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

		extend(CompletionType.BASIC, StandardPatterns.psiElement(PhpTokenTypes.IDENTIFIER), new PhpKeywordsCompletionProvider());
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
				List<PhpDefine> defines = new ArrayList<>();
				reference.processForCompletion(namedElement -> {
					if(namedElement instanceof PhpClass)
					{
						classes.add((PhpClass) namedElement);
					}
					else if(namedElement instanceof PhpDefine)
					{
						defines.add((PhpDefine) namedElement);
					}
					return true;
				});

				List<LookupElement> lookupItemsForClasses = PhpVariantsUtil.getLookupItemsForClasses(classes, classUsageContext);

				result.addAllElements(lookupItemsForClasses);

				result.addAllElements(Arrays.asList(PhpVariantsUtil.getLookupItems(defines, usageContext)));
			}
			else if(parent instanceof PhpFieldReferenceImpl)
			{
				UsageContext usageContext = new UsageContext();
				final PhpClass contextClass = PsiTreeUtil.getParentOfType(parent, PhpClass.class);
				if(contextClass != null)
				{
					usageContext.setClassForAccessFilter(contextClass);
				}

				usageContext.setCallingObjectClass(contextClass);

				PsiElement objectReference = ((PhpFieldReferenceImpl) parent).getObjectReference();

				((PhpFieldReferenceImpl) parent).processForCompletion(e -> {
					LookupElement lookupElement = PhpVariantsUtil.getLookupItem(e, usageContext, objectReference == null ? PhpNamingPolicy.NOTHING : PhpNamingPolicy.WITHOUT_DOLLAR);
					result.addElement(lookupElement);
					return true;
				});

			}
			else if(parent instanceof PhpFunctionReferenceImpl)
			{
				UsageContext usageContext = ((PhpFunctionReferenceImpl) parent).createUsageContext();

				((PhpFunctionReferenceImpl) parent).processForCompletion(phpNamedElement ->
				{
					LookupElement lookupItem = PhpVariantsUtil.getLookupItem(phpNamedElement, usageContext);
					result.addElement(lookupItem);
					return true;
				});
			}
		});
	}
}
