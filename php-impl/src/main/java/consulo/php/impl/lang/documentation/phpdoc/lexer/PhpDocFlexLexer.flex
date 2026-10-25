package consulo.php.impl.lang.documentation.phpdoc.lexer;

import consulo.language.lexer.LexerBase;
import consulo.language.ast.IElementType;

%%

%class PhpDocFlexLexer
%extends LexerBase
%implements PhpDocTokenTypes
%unicode
%public

%function advanceImpl
%type IElementType

%eof{ return;
%eof}

%{
  /**
   * '@' starts a tag unless it continues a word (e-mail addresses)
   */
  private boolean isTagStart() {
    if (zzStartRead == 0) {
      return true;
    }
    char previous = zzBuffer.charAt(zzStartRead - 1);
    return !Character.isLetterOrDigit(previous) && previous != '_' && previous != '.' && previous != '-';
  }
%}

%x YYINITIAL
%x IN_COMMENT
%x AFTER_LINEBREAK

LABEL      =  [a-zA-Z_\u0080-￿][a-zA-Z0-9_\u0080-￿]*
TAG_LABEL  =  [a-zA-Z_\u0080-￿][a-zA-Z0-9_\-\u0080-￿]*
TAG_NAME   =  "@" "\\"? {TAG_LABEL} ("\\" {TAG_LABEL})*
VARIABLE   =  "$" {LABEL}
WHITESPACE =  [ \t]+
NEWLINE    =  \r\n|\r|\n
INTEGER    =  "-"? [0-9]+ ("." [0-9]+)?
DQ_STRING  =  \" ([^\"\r\n] | \"\")* \"
SQ_STRING  =  \' [^\'\r\n]* \'
HTML_TAG   =  "<" "/"? ("b"|"br"|"code"|"i"|"ul"|"ol"|"li"|"kbd"|"var"|"pre"|"p"|"samp") "/"? ">"

%%

<YYINITIAL> "/**"                       { yybegin(IN_COMMENT); return DOC_COMMENT_START; }
<YYINITIAL> [^]                         { return DOC_IGNORED; }

<IN_COMMENT, AFTER_LINEBREAK> "*/"      { yybegin(YYINITIAL); return DOC_COMMENT_END; }
<IN_COMMENT, AFTER_LINEBREAK> {NEWLINE} { yybegin(AFTER_LINEBREAK); return DOC_WHITESPACE; }
<IN_COMMENT, AFTER_LINEBREAK> {WHITESPACE} { return DOC_WHITESPACE; }

<AFTER_LINEBREAK> "*"                   { yybegin(IN_COMMENT); return DOC_LEADING_ASTERISK; }
<AFTER_LINEBREAK> [^]                   { yybegin(IN_COMMENT); yypushback(1); }

<IN_COMMENT> {TAG_NAME}                 { if (isTagStart()) { return DOC_TAG_NAME; } yypushback(yylength() - 1); return DOC_TEXT; }
<IN_COMMENT> {VARIABLE}                 { return DOC_VARIABLE; }
<IN_COMMENT> {LABEL}                    { return DOC_IDENTIFIER; }
<IN_COMMENT> {INTEGER}                  { return DOC_INTEGER; }
<IN_COMMENT> {DQ_STRING} | {SQ_STRING}  { return DOC_STRING; }
<IN_COMMENT> {HTML_TAG}                 { return DOC_HTML_TAG; }
<IN_COMMENT> "::"                       { return DOC_STATIC; }
<IN_COMMENT> "\\"                       { return DOC_NAMESPACE; }
<IN_COMMENT> "("                        { return DOC_LPAREN; }
<IN_COMMENT> ")"                        { return DOC_RPAREN; }
<IN_COMMENT> "{"                        { return DOC_LBRACE; }
<IN_COMMENT> "}"                        { return DOC_RBRACE; }
<IN_COMMENT> "["                        { return DOC_LBRACKET; }
<IN_COMMENT> "]"                        { return DOC_RBRACKET; }
<IN_COMMENT> "<"                        { return DOC_LAB; }
<IN_COMMENT> ">"                        { return DOC_RAB; }
<IN_COMMENT> ","                        { return DOC_COMMA; }
<IN_COMMENT> "|"                        { return DOC_PIPE; }
<IN_COMMENT> "&"                        { return DOC_AMPERSAND; }
<IN_COMMENT> "..."                      { return DOC_DOT; }
<IN_COMMENT> [^]                        { return DOC_TEXT; }
