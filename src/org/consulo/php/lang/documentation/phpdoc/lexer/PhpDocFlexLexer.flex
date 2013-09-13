
package org.consulo.php.lang.documentation.phpdoc.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

%%

%class PhpDocFlexLexer
%implements FlexLexer, PhpDocTokenTypes
%unicode
%public

%function advance
%type IElementType

%eof{ return;
%eof}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////// User code //////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

%{ // User code

  public PhpDocFlexLexer() {
    this((java.io.Reader) null);
  }

%}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////// GroovyDoc lexems ////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

%x YYINITIAL
%x IN_COMMENT
%x AFTER_LINEBREAK
%x AFTER_ASTERISK
%x AFTER_LBRACE
%x IGNORED_LINE

LABEL      =  [a-zA-Z_\x7f-\xff][a-zA-Z0-9_\x7f-\xff]*
VARIABLE   =  "$" {LABEL}
WHITESPACE =  [ \t]+
NEWLINE    =  ("\r"|"\n"|"\r\n")
ANY_CHAR   =  .|{NEWLINE}
HTML_TAG   =  "<" "/"? ("b"|"br"|"code"|"i"|"ul"|"ol"|"li"|"kbd"|"var"|"pre"|"p"|"samp") ">"

%%

<YYINITIAL> "/**" {
  yybegin(IN_COMMENT);
  return DOC_COMMENT_START;
}
<YYINITIAL> {ANY_CHAR} {
  return DOC_IGNORED;
}
<IN_COMMENT> "*/" {
  yybegin(YYINITIAL);
  return DOC_COMMENT_END;
}

<IN_COMMENT> {LABEL} {
  return DOC_IDENTIFIER;
}

<IN_COMMENT> {VARIABLE} {
  return DOC_VARIABLE;
}

<IN_COMMENT> "." {WHITESPACE} {
  yypushback(yytext().length() - 1);
  return DOC_DOT;
}

<IN_COMMENT> "|" {
  return DOC_PIPE;
}

<IN_COMMENT> "{" {
  yybegin(AFTER_LBRACE);
  return DOC_LBRACE;
}

<IN_COMMENT> {HTML_TAG} {
  return DOC_HTML_TAG;
}

<AFTER_LBRACE> "@" {LABEL} {
  return DOC_TAG_NAME;
}

<AFTER_LBRACE> {LABEL} {
  return DOC_IDENTIFIER;
}

<AFTER_LBRACE> "}" {
  yybegin(IN_COMMENT);
  return DOC_RBRACE;
}

<AFTER_LBRACE> . {
  return DOC_TEXT;
}

<AFTER_LBRACE> {WHITESPACE} {
  return DOC_WHITESPACE;
}

<AFTER_LBRACE> {NEWLINE} {
  yybegin(AFTER_LINEBREAK);
  return DOC_WHITESPACE;
}

<IN_COMMENT> {NEWLINE} {
  yybegin(AFTER_LINEBREAK);
  return DOC_WHITESPACE;
}

<AFTER_LINEBREAK> {WHITESPACE} {
  return DOC_WHITESPACE;
}

<AFTER_LINEBREAK> "*/" {
  yybegin(YYINITIAL);
  return DOC_COMMENT_END;
}

<AFTER_LINEBREAK> "*" {
  yybegin(AFTER_ASTERISK);
  return DOC_LEADING_ASTERISK;
}

<AFTER_LINEBREAK> . {
  yybegin(IGNORED_LINE);
  yypushback(1);
}

<IGNORED_LINE> "*/" {
  yybegin(YYINITIAL);
  return DOC_COMMENT_END;
}

<IGNORED_LINE> . {
  return DOC_IGNORED;
}

<IGNORED_LINE> {NEWLINE} {
  yybegin(AFTER_LINEBREAK);
  return DOC_WHITESPACE;
}

<AFTER_LINEBREAK> {NEWLINE} {
  return DOC_WHITESPACE;
}

<AFTER_ASTERISK> "@" {LABEL} {
  yybegin(IN_COMMENT);
  return DOC_TAG_NAME;
}

<AFTER_ASTERISK> {WHITESPACE} {
  return DOC_WHITESPACE;
}

<AFTER_ASTERISK> . {
  yybegin(IN_COMMENT);
  yypushback(1);
}

<AFTER_ASTERISK> {NEWLINE} {
  yybegin(AFTER_LINEBREAK);
  return DOC_WHITESPACE;
}

<IN_COMMENT> {WHITESPACE} {
  return DOC_WHITESPACE;
}

<IN_COMMENT> . {
  return DOC_TEXT;
}