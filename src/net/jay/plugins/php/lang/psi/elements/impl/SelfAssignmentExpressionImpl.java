package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import net.jay.plugins.php.lang.psi.elements.SelfAssignmentExpression;

/**
 * @author jay
 * @date Apr 4, 2008 2:03:58 PM
 */
public class SelfAssignmentExpressionImpl extends AssignmentExpressionImpl implements SelfAssignmentExpression {
  public SelfAssignmentExpressionImpl(ASTNode node) {
    super(node);
  }
}
