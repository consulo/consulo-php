package org.consulo.php.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

%%

%{
	public PHPStructuringFlexLexer() {
		this((java.io.Reader)null);
	}
%}

%class PHPStructuringFlexLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}


PHP_COMMON_OPENING_TAG =            "<?php"
PHP_CLOSING_TAG =                   "?>"
PHP_BRIEF_OPENING_TAG =             "<?"
PHP_ECHO_OPENING_TAG =              "<?="
PHP_OPENING_TAG =                   {PHP_COMMON_OPENING_TAG} | {PHP_BRIEF_OPENING_TAG}

EOL =                               "\r" | "\n" | "\r\n"
ANY_CHAR =                          .|{EOL}
SINGLE_QUOTE =                      "'"
DOUBLE_QUOTE =                      "\""

%xstate IN_PHP_BLOCK
%xstate IN_PHP_ECHO_BLOCK
%xstate IN_PHP_SQ_STRING
%xstate IN_PHP_DQ_STRING
%xstate IN_PHP_BLOCK_COMMENT
%xstate IN_PHP_LINE_COMMENT


%%
<IN_PHP_SQ_STRING>{
 "\\'"                              { return PHPStructuralTokenTypes.PHP_CODE; }
 {SINGLE_QUOTE}                     { yybegin(IN_PHP_BLOCK); return PHPStructuralTokenTypes.PHP_CODE; }
 {ANY_CHAR}                         { return PHPStructuralTokenTypes.PHP_CODE; }
}
<IN_PHP_DQ_STRING>{
 "\\\""                             { return PHPStructuralTokenTypes.PHP_CODE; }
 {DOUBLE_QUOTE}                     { yybegin(IN_PHP_BLOCK); return PHPStructuralTokenTypes.PHP_CODE; }
 {ANY_CHAR}                         { return PHPStructuralTokenTypes.PHP_CODE; }
}
<IN_PHP_LINE_COMMENT>{
 {PHP_CLOSING_TAG}                  { yybegin(YYINITIAL); return PHPStructuralTokenTypes.PHP_CODE; }
 {EOL}                              { yybegin(IN_PHP_BLOCK); return PHPStructuralTokenTypes.PHP_CODE; }
 {ANY_CHAR}                         { return PHPStructuralTokenTypes.PHP_CODE; }
}
<IN_PHP_BLOCK_COMMENT>{
 {PHP_CLOSING_TAG}                  { yybegin(YYINITIAL); return PHPStructuralTokenTypes.PHP_CODE; }
 "*/"                               { yybegin(IN_PHP_BLOCK); return PHPStructuralTokenTypes.PHP_CODE; }
 {ANY_CHAR}                         { return PHPStructuralTokenTypes.PHP_CODE; }
}
<IN_PHP_ECHO_BLOCK>{
 {PHP_CLOSING_TAG}                  { yybegin(YYINITIAL); return PHPStructuralTokenTypes.PHP_ECHO_CODE; }
 {ANY_CHAR}                         { return PHPStructuralTokenTypes.PHP_ECHO_CODE; }
}
// everything up to the php closing tag is the php code
// the exceptions are: php closing tag in the string is treated as a string and
// the php closing tag in the commented string is treated as a closing tag
<IN_PHP_BLOCK>{
 {PHP_CLOSING_TAG}                  { yybegin(YYINITIAL); return PHPStructuralTokenTypes.PHP_CODE; }
 "//" | "#"                         { yybegin(IN_PHP_LINE_COMMENT); return PHPStructuralTokenTypes.PHP_CODE; }
 "/*"                               { yybegin(IN_PHP_BLOCK_COMMENT); return PHPStructuralTokenTypes.PHP_CODE; }
 {SINGLE_QUOTE}                     { yybegin(IN_PHP_SQ_STRING); return PHPStructuralTokenTypes.PHP_CODE; }
 {DOUBLE_QUOTE}                     { yybegin(IN_PHP_DQ_STRING); return PHPStructuralTokenTypes.PHP_CODE; }
 {ANY_CHAR}                         { return PHPStructuralTokenTypes.PHP_CODE; }
}
// if we meet an opening tag than we get into the php block state
{PHP_ECHO_OPENING_TAG}              { yybegin(IN_PHP_ECHO_BLOCK); return PHPStructuralTokenTypes.PHP_ECHO_CODE; }
{PHP_OPENING_TAG}                   { yybegin(IN_PHP_BLOCK); return PHPStructuralTokenTypes.PHP_CODE; }
// every char we meet is outside of php (presumably html)
{ANY_CHAR}                          { return PHPStructuralTokenTypes.HTML_CODE; }