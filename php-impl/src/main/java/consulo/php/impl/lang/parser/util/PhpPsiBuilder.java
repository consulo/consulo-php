package consulo.php.impl.lang.parser.util;

import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.language.parser.PsiBuilder;
import consulo.language.parser.PsiBuilderAdapter;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;

/**
 * @author markov
 * @since 13.10.2007
 */
public class PhpPsiBuilder extends PsiBuilderAdapter {
    // > 0 inside function, method and closure bodies
    private int myFunctionDepth;

    public PhpPsiBuilder(@Nonnull PsiBuilder builder) {
        super(builder);
    }

    public void enterFunction() {
        myFunctionDepth++;
    }

    public void exitFunction() {
        myFunctionDepth--;
    }

    public boolean isInFunction() {
        return myFunctionDepth > 0;
    }

    public boolean compare(final IElementType type) {
        return getTokenType() == type;
    }

    public boolean compare(final TokenSet types) {
        return types.contains(getTokenType());
    }

    public boolean compareAndEat(final IElementType type) {
        boolean found = compare(type);
        if (found) {
            advanceLexer();
        }
        return found;
    }

    public boolean compareAndEat(final TokenSet types) {
        boolean found = compare(types);
        if (found) {
            advanceLexer();
        }
        return found;
    }

    public void match(final IElementType token) {
        match(token, PhpParserErrors.expected(token));
    }

    public void match(final IElementType token, @Nonnull LocalizeValue errorMessage) {
        if (!compareAndEat(token)) {
            error(errorMessage);
        }
    }

    public void match(final TokenSet tokens) {
        match(tokens, PhpParserErrors.expected(tokens));
    }

    public void match(final TokenSet tokens, @Nonnull LocalizeValue errorMessage) {
        if (!compareAndEat(tokens)) {
            error(errorMessage);
        }
    }
}
