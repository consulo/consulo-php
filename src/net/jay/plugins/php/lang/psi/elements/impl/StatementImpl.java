package net.jay.plugins.php.lang.psi.elements.impl;

import net.jay.plugins.php.lang.psi.elements.Statement;
import com.intellij.lang.ASTNode;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 30.03.2007
 *
 * @author jay
 */
public class StatementImpl extends PHPPsiElementImpl implements Statement {

	public StatementImpl(ASTNode node) {
		super(node);
	}
}
