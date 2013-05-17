package net.jay.plugins.php.lang.psi.resolve.types;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import net.jay.plugins.php.cache.DeclarationsIndex;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.PhpDocComment;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.tags.PhpDocVarTag;
import net.jay.plugins.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import net.jay.plugins.php.lang.psi.elements.*;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;

/**
 * @author jay
 * @date Jun 18, 2008 12:03:21 PM
 */
public class PhpTypeAnnotatorVisitor extends PHPElementVisitor {

  public static final Key<PhpType> TYPE_KEY = new Key<PhpType>("PhpTypeKey");

  @SuppressWarnings({"ConstantConditions"})
  public void visitPhpVariable(Variable variable) {
    PhpType type = new PhpType();
    if (variable.getName() != null && variable.getName().equals("this")) {
      final PhpClass klass = PsiTreeUtil.getParentOfType(variable, PhpClass.class);
      if (klass != null && klass.getName() != null) {
        DeclarationsIndex index = DeclarationsIndex.getInstance(variable);
        if (index != null) {
          type.addClasses(index.getClassesByName(klass.getName()));
        }
      }
    } else if (!variable.isDeclaration()) {
      final ResolveResult[] results = variable.multiResolve(false);
      if (results.length > 0) {
        ResolveResult result = results[results.length - 1];
        final PsiElement element = result.getElement();
        if (element instanceof PhpTypedElement) {
          type.addClasses(((PhpTypedElement) element).getType().getTypes());
        }
      }
    } else {
      final PsiElement parent = variable.getParent();
      if (parent instanceof AssignmentExpression) {
        final PsiElement value = ((AssignmentExpression) parent).getValue();
        if (value instanceof PhpTypedElement) {
          type.addClasses(((PhpTypedElement) value).getType().getTypes());
        }
      } else if (parent instanceof Catch) {
        final ClassReference classReference = ((Catch) parent).getExceptionType();
        DeclarationsIndex index = DeclarationsIndex.getInstance(variable);
        if (index != null) {
          type.addClasses(index.getClassesByName(classReference.getReferenceName()));
        }
      }
    }
    variable.putUserData(TYPE_KEY, type);
  }

  public void visitPhpAssignmentExpression(AssignmentExpression expr) {
    PhpType type = new PhpType();
    final PsiElement value = expr.getValue();
        if (value instanceof PhpTypedElement) {
          type.addClasses(((PhpTypedElement) value).getType().getTypes());
        }
    expr.putUserData(TYPE_KEY, type);
  }

  public void visitPhpMethodReference(MethodReference reference) {
    PhpType type = new PhpType();
    final PsiElement element = reference.resolve();
    if (element instanceof Method) {
      type.addClasses(((Method) element).getType().getTypes());
    }
    reference.putUserData(TYPE_KEY, type);
  }

  public void visitPhpMethod(Method method) {
    PhpType type = new PhpType();
    final PhpDocComment docComment = method.getDocComment();
    if (docComment != null) {
      final PhpDocReturnTag returnTag = docComment.getReturnTag();
      if (returnTag != null) {
        DeclarationsIndex index = DeclarationsIndex.getInstance(method);
        for (String className : returnTag.getTypes()) {
          type.addClasses(index.getClassesByName(className));
        }
      }
    }
    method.putUserData(TYPE_KEY, type);
  }

  public void visitPhpFieldReference(FieldReference fieldReference) {
    PhpType type = new PhpType();
    final PsiElement element = fieldReference.resolve();
    if (element instanceof Field) {
      type.addClasses(((Field) element).getType().getTypes());
    }
    fieldReference.putUserData(TYPE_KEY, type);
  }

  public void visitPhpField(Field field) {
    PhpType type = new PhpType();
    final PhpDocComment docComment = field.getDocComment();
    if (docComment != null) {
      final PhpDocVarTag varTag = docComment.getVarTag();
      if (varTag != null && varTag.getType() != null && varTag.getType().length() > 0) {
        DeclarationsIndex index = DeclarationsIndex.getInstance(field);
        if (index != null) {
          type.addClasses(index.getClassesByName(varTag.getType()));
        }
      }
    }
    field.putUserData(TYPE_KEY, type);
  }

  @SuppressWarnings({"ConstantConditions"})
  public void visitPhpNewExpression(NewExpression expression) {
    PhpType type = new PhpType();
    final PHPPsiElement classReference = expression.getFirstPsiChild();
    if (classReference instanceof ClassReference) {
      PsiElement klass = ((ClassReference) classReference).resolve();
      if (!(klass instanceof PhpClass)) {
        klass = PsiTreeUtil.getParentOfType(klass, PhpClass.class);
      }
      if (klass != null && ((PhpClass) klass).getName() != null) {
        DeclarationsIndex index = DeclarationsIndex.getInstance(expression);
        if (index != null) {
          type.addClasses(index.getClassesByName(((PhpClass) klass).getName()));
        }
      }
    }
    expression.putUserData(TYPE_KEY, type);
  }

  public void visitPhpParameter(Parameter parameter) {
    PhpType type = new PhpType();
    final PHPPsiElement classReference = parameter.getFirstPsiChild();
    if (classReference instanceof ClassReference) {
      DeclarationsIndex index = DeclarationsIndex.getInstance(parameter);
      if (index != null) {
        type.addClasses(index.getClassesByName(((ClassReference) classReference).getReferenceName()));
      }
    }
    parameter.putUserData(TYPE_KEY, type);
  }

  private static PhpTypeAnnotatorVisitor instance = null;

  public static PhpTypeAnnotatorVisitor getInstance() {
    if (instance == null) {
      instance = new PhpTypeAnnotatorVisitor();
    }
    return instance;
  }

  public static void process(PsiElement element) {
    element.accept(getInstance());
  }
}
