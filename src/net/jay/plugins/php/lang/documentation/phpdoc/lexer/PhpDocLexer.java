package net.jay.plugins.php.lang.documentation.phpdoc.lexer;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.MergingLexerAdapter;
import com.intellij.psi.tree.TokenSet;

/**
 * @author jay
 * @date Jun 26, 2008 10:47:50 PM
 */
public class PhpDocLexer extends MergingLexerAdapter {

  public PhpDocLexer() {
    super(
      new FlexAdapter(new PhpDocFlexLexer()),
      TokenSet.create(
        PhpDocTokenTypes.DOC_TEXT,
        PhpDocTokenTypes.DOC_WHITESPACE,
        PhpDocTokenTypes.DOC_IGNORED
      )
    );
  }

}
