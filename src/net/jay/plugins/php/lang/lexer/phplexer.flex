package net.jay.plugins.php.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import net.jay.plugins.php.lang.lexer.managers.*;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import org.jetbrains.annotations.NotNull;
import gnu.trove.THashSet;
import java.util.Arrays;
import java.util.Set;
import net.jay.plugins.php.completion.PhpCompletionData;
import org.consulo.php.PhpLanguageLevel;
%%

%{
  private boolean myHighlightingMode;
  private PhpLanguageLevel myLanguageLevel;

	public PHPFlexLexer(boolean highlightingMode, PhpLanguageLevel languageLevel) {
		this((java.io.Reader)null);
		myHighlightingMode = highlightingMode;
		myLanguageLevel = languageLevel;
	}

	//private StringManager sqsManager = new SingleQuotedStringManager(this);
	//private StringManager dqsManager = new DoubleQuotedStringManager(this);
	//private StringManager btsManager = new BacktrickedStringManager(this);
	private BraceManager brcManager;
	private OperatorManager opManager = new OperatorManager(this);
	private StatesManager sManager = new StatesManager(this);
	private HeredocManager hdManager = new HeredocManager(this, sManager);
	private LineCommentManager lcManager = new LineCommentManager(this);


	@NotNull
	public final CharSequence getBuffer(){
		return zzBuffer;
	}

	private void reset(int initialState){
		opManager.reset();
		lcManager.reset();
		sManager.reset();
		hdManager.reset();
		brcManager = null;

		sManager.toState(initialState);
	}

%}

%class PHPFlexLexer
%implements FlexLexer
%public
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}


LNUM =					[0-9]+
DNUM =					([0-9]*[\.][0-9]+)|([0-9]+[\.][0-9]*)
EXPONENT_DNUM = 			(({LNUM}|{DNUM})[eE][+-]?{LNUM})
HNUM = 					"0x"[0-9a-fA-F]+
LABEL= 					[a-zA-Z_\x7f-\xff][a-zA-Z0-9_\x7f-\xff]*
WHITESPACE = 				[ \n\r\t]+
TABS_AND_SPACES = 			[ \t]*
//TOKENS = 				[;:,.\[\]()|^&+-/*=%!~$<>?@]
ANY_CHAR = 				(.|[\n])
NEWLINE = 				("\r"|"\n"|"\r\n")

/*
 * LITERAL_DOLLAR matches unescaped $ that aren't followed by a label character
 * or a { and therefore will be taken literally. The case of literal $ before
 * a variable or "${" is handled in a rule for each string type
 */
DOUBLE_QUOTES_LITERAL_DOLLAR = 		("$"+([^a-zA-Z_\x7f-\xff$\"\\{]|("\\"{ANY_CHAR})))
BACKQUOTE_LITERAL_DOLLAR = 		("$"+([^a-zA-Z_\x7f-\xff$`\\{]|("\\"{ANY_CHAR})))
HEREDOC_LITERAL_DOLLAR = 		("$"+([^a-zA-Z_\x7f-\xff$\n\r\\{]|("\\"[^\n\r])))

/*
 * Usually, HEREDOC_NEWLINE will just function like a simple NEWLINE, but some
 * special cases need to be handled. HEREDOC_CHARS doesn't allow a line to
 * match when { or $, and/or \ is at the end. (("{"*|"$"*)"\\"?) handles that,
 * along with cases where { or $, and/or \ is the ONLY thing on a line
 *
 * The other case is when a line contains a label, followed by ONLY
 * { or $, and/or \  Handled by ({LABEL}";"?((("{"+|"$"+)"\\"?)|"\\"))
 */
HEREDOC_NEWLINE = 			((( {LABEL} ";"? ( ( ( "{"+ | "$"+ ) "\\"? ) | "\\" ) ) | ( ( "{"* | "$"* ) "\\"? ) ){NEWLINE})

/*
 * This pattern is just used in the next 2 for matching { or literal $, and/or
 * \ escape sequence immediately at the beginning of a line or after a label
 */
HEREDOC_CURLY_OR_ESCAPE_OR_DOLLAR = 	(("{"+[^$\n\r\\{])|("{"*"\\"[^\n\r])|{HEREDOC_LITERAL_DOLLAR})

/*
 * These 2 label-related patterns allow HEREDOC_CHARS to continue "regular"
 * matching after a newline that starts with either a non-label character or a
 * label that isn't followed by a newline. Like HEREDOC_CHARS, they won't match
 * a variable or "{$"  Matching a newline, and possibly label, up TO a variable
 * or "{$", is handled in the heredoc rules
 *
 * The HEREDOC_LABEL_NO_NEWLINE pattern (";"[^$\n\r\\{]) handles cases where ;
 * follows a label. [^a-zA-Z0-9_\x7f-\xff;$\n\r\\{] is needed to prevent a label
 * character or ; from matching on a possible (real) ending label
 */
HEREDOC_NON_LABEL = 			([^a-zA-Z_\x7f-\xff$\n\r\\{]|{HEREDOC_CURLY_OR_ESCAPE_OR_DOLLAR})
HEREDOC_LABEL_NO_NEWLINE = 		({LABEL}([^a-zA-Z0-9_\x7f-\xff;$\n\r\\{]|(";"[^$\n\r\\{])|(";"?{HEREDOC_CURLY_OR_ESCAPE_OR_DOLLAR})))

/*
 * CHARS matches everything up to a variable or "{$"
 * {'s are matched as long as they aren't followed by a $
 * The case of { before "{$" is handled in a rule for each string type
 *
 * For heredocs, matching continues across/after newlines if/when it's known
 * that the next line doesn't contain a possible ending label
 */
DOUBLE_QUOTES_CHARS = 			("{"*([^$\"\\{]|("\\"{ANY_CHAR}))|{DOUBLE_QUOTES_LITERAL_DOLLAR})
BACKQUOTE_CHARS =			("{"*([^$`\\{]|("\\"{ANY_CHAR}))|{BACKQUOTE_LITERAL_DOLLAR})
HEREDOC_CHARS =				("{"*([^$\n\r\\{]|("\\"[^\n\r]))|{HEREDOC_LITERAL_DOLLAR}|({HEREDOC_NEWLINE}+({HEREDOC_NON_LABEL}|{HEREDOC_LABEL_NO_NEWLINE})))

DECIMAL_INTEGER =                  [1-9][0-9]* | 0
HEX_INTEGER =                      0[xX][0-9a-fA-F]+
OCTAL_INTEGER =                    0[0-7]+
INTEGER_LITERAL =                  ({DECIMAL_INTEGER} | {HEX_INTEGER} | {OCTAL_INTEGER})

FLOATING_POINT_LITERAL1 =          [0-9]*"."[0-9]+{EXPONENT_PART}?
FLOATING_POINT_LITERAL2 =          [0-9]+{EXPONENT_PART}
EXPONENT_PART =                    [Ee][+-]?[0-9]+

FLOAT_LITERAL =                    {FLOATING_POINT_LITERAL1} | {FLOATING_POINT_LITERAL2}

LINE_COMMENT_WITH_CLOSING_TAG =    ("//" | "#") .* "?>"
LINE_COMMENT_NORMAL =              ("//" | "#") .*
LINE_COMMENT =                     {LINE_COMMENT_NORMAL} | {LINE_COMMENT_WITH_CLOSING_TAG}

C_STYLE_COMMENT =                  ("/*"[^"*"]{COMMENT_TAIL})|"/*"
DOC_COMMENT =                      "/*""*"+("/"|([^"/""*"]{COMMENT_TAIL}))?
COMMENT_TAIL =                     ([^"*"]*("*"+[^"*""/"])?)*("*"+"/")?

HEREDOC_START =                    "<<<" {TABS_AND_SPACES} {LABEL} {NEWLINE}

//===================================================================
//==========================CASTS====================================
//===================================================================

CAST_BEGIN =                       "(" {TABS_AND_SPACES}
CAST_END =                         {TABS_AND_SPACES} ")"

INTEGER_CAST =                     {CAST_BEGIN} ("int"|"integer") {CAST_END}
FLOAT_CAST =                       {CAST_BEGIN} ("float"|"double"|"real") {CAST_END}
BOOLEAN_CAST =                     {CAST_BEGIN} ("bool"|"boolean") {CAST_END}
STRING_CAST =                      {CAST_BEGIN} ("string"|"binary") {CAST_END}
ARRAY_CAST  =                      {CAST_BEGIN} "array" {CAST_END}
OBJECT_CAST  =                     {CAST_BEGIN} "object" {CAST_END}
UNSET_CAST =                       {CAST_BEGIN} "unset" {CAST_END}


%x ST_IN_SCRIPTING
%x ST_DOUBLE_QUOTES
%x ST_BACKQUOTE
%x ST_HEREDOC
%x ST_START_HEREDOC
%x ST_END_HEREDOC
%x ST_LOOKING_FOR_PROPERTY
%x ST_LOOKING_FOR_VARNAME
%x ST_VAR_OFFSET
%x ST_COMMENT
%x ST_DOC_COMMENT
%x ST_ONE_LINE_COMMENT

%%

<ST_IN_SCRIPTING>{
//=========================KEYWORDS==================================
//   oop
	"class"                            { return PHPTokenTypes.kwCLASS; }
	"interface"                        { return PHPTokenTypes.kwINTERFACE; }
	"extends"                          { return PHPTokenTypes.kwEXTENDS; }
	"implements"                       { return PHPTokenTypes.kwIMPLEMENTS; }
	"var"                              { return PHPTokenTypes.kwVAR; }
	"const"                            { return PHPTokenTypes.kwCONST; }
	"public"                           { return PHPTokenTypes.kwPUBLIC; }
	"protected"                        { return PHPTokenTypes.kwPROTECTED; }
	"private"                          { return PHPTokenTypes.kwPRIVATE; }
	"static"                           { return PHPTokenTypes.kwSTATIC; }
	"final"                            { return PHPTokenTypes.kwFINAL; }
	"abstract"                         { return PHPTokenTypes.kwABSTACT; }
	"clone"                            { return PHPTokenTypes.kwCLONE; }
	"new"                              { return PHPTokenTypes.kwNEW; }
	"instanceof"                       { return PHPTokenTypes.kwINSTANCEOF; }

	"namespace"                        { return myLanguageLevel.isAtLeast(PhpLanguageLevel.PHP_5_3) ? PHPTokenTypes.NAMESPACE_KEYWORD : PHPTokenTypes.IDENTIFIER;}
	"use"                              { return myLanguageLevel.isAtLeast(PhpLanguageLevel.PHP_5_3) ? PHPTokenTypes.USE_KEYWORD : PHPTokenTypes.IDENTIFIER;}

	//   flow control
	"if"                               { return opManager.process(PHPTokenTypes.kwIF); }
	"elseif"                           { return opManager.process(PHPTokenTypes.kwELSEIF); }
	"else"                             { return opManager.process(PHPTokenTypes.kwELSE); }
	"do"                               { return opManager.process(PHPTokenTypes.kwDO); }
	"while"                            { return opManager.process(PHPTokenTypes.kwWHILE); }
	"break"                            { return opManager.process(PHPTokenTypes.kwBREAK); }
	"switch"                           { return opManager.process(PHPTokenTypes.kwSWITCH); }
	"case"                             { return opManager.process(PHPTokenTypes.kwCASE); }
	"default"                          { return PHPTokenTypes.kwDEFAULT; }
	"for"                              { return opManager.process(PHPTokenTypes.kwFOR); }
	"foreach"                          { return PHPTokenTypes.kwFOREACH; }
	"as"                               { return opManager.process(PHPTokenTypes.kwAS); }
	"continue"                         { return opManager.process(PHPTokenTypes.kwCONTINUE); }
	"declare"                          { return PHPTokenTypes.kwDECLARE; }

	//   terminating flow
	"endif"                            { return PHPTokenTypes.kwENDIF; }
	"endwhile"                         { return PHPTokenTypes.kwENDWHILE; }
	"endfor"                           { return PHPTokenTypes.kwENDFOR; }
	"endforeach"                       { return PHPTokenTypes.kwENDFOREACH; }
	"endswitch"                        { return PHPTokenTypes.kwENDSWITCH; }
	"enddeclare"                       { return PHPTokenTypes.kwENDDECLARE; }

	//   functions
	"function"                         { return PHPTokenTypes.kwFUNCTION; }
	"global"                           { return PHPTokenTypes.kwGLOBAL; }
	"return"                           { return opManager.process(PHPTokenTypes.kwRETURN); }
	"array"                            { return opManager.process(PHPTokenTypes.kwARRAY); }
	"list"                             { return opManager.process(PHPTokenTypes.kwLIST); }
	"print"                            { return opManager.process(PHPTokenTypes.kwPRINT); }
	"echo"                             { return opManager.process(PHPTokenTypes.kwECHO); }
	"empty"                            { return opManager.process(PHPTokenTypes.kwEMPTY); }
	"eval"                             { return opManager.process(PHPTokenTypes.kwEVAL); }
	"die"                              { return opManager.process(PHPTokenTypes.kwEXIT); }
	"exit"                             { return opManager.process(PHPTokenTypes.kwEXIT); }
	"isset"                            { return opManager.process(PHPTokenTypes.kwISSET); }
	"unset"                            { return opManager.process(PHPTokenTypes.kwUNSET); }

	//   includes
	"include"                          { return opManager.process(PHPTokenTypes.kwINCLUDE); }
	"include_once"                     { return opManager.process(PHPTokenTypes.kwINCLUDE_ONCE); }
	"require"                          { return opManager.process(PHPTokenTypes.kwREQUIRE); }
	"require_once"                     { return opManager.process(PHPTokenTypes.kwREQUIRE_ONCE); }

	//   exception handling
	"try"                              { return PHPTokenTypes.kwTRY; }
	"catch"                            { return PHPTokenTypes.kwCATCH; }
	"throw"                            { return PHPTokenTypes.kwTHROW; }

	//   magic constants
	"__LINE__"                         { return opManager.process(PHPTokenTypes.CONST_LINE); }
	"__FILE__"                         { return opManager.process(PHPTokenTypes.CONST_FILE); }
	"__FUNCTION__"                     { return opManager.process(PHPTokenTypes.CONST_FUNCTION); }
	"__CLASS__"                        { return opManager.process(PHPTokenTypes.CONST_CLASS); }
	"__METHOD__"                       { return opManager.process(PHPTokenTypes.CONST_METHOD); }
}

<ST_IN_SCRIPTING>{
//   operators
	[aA][nN][dD]                       { return opManager.process(PHPTokenTypes.opLIT_AND); }
	[oO][rR]                           { return opManager.process(PHPTokenTypes.opLIT_OR); }
	[xX][oO][rR]                       { return opManager.process(PHPTokenTypes.opLIT_XOR); }
}

<ST_IN_SCRIPTING>{
	{INTEGER_LITERAL}                  { return opManager.process(PHPTokenTypes.INTEGER_LITERAL); }
	{FLOAT_LITERAL}                    { return opManager.process(PHPTokenTypes.FLOAT_LITERAL); }
}

<ST_VAR_OFFSET>{
	{INTEGER_LITERAL}                  { return PHPTokenTypes.VARIABLE_OFFSET_NUMBER; }
}


//========================OPERATORS===================================

<ST_IN_SCRIPTING>{
	{INTEGER_CAST}                     { return opManager.process(PHPTokenTypes.opINTEGER_CAST); }
	{FLOAT_CAST}                       { return opManager.process(PHPTokenTypes.opFLOAT_CAST); }
	{BOOLEAN_CAST}                     { return opManager.process(PHPTokenTypes.opBOOLEAN_CAST); }
	{STRING_CAST}                      { return opManager.process(PHPTokenTypes.opSTRING_CAST); }
	{ARRAY_CAST}                       { return opManager.process(PHPTokenTypes.opARRAY_CAST); }
	{OBJECT_CAST}                      { return opManager.process(PHPTokenTypes.opOBJECT_CAST); }
	{UNSET_CAST}                       { return opManager.process(PHPTokenTypes.opUNSET_CAST); }

	"++"                               { return opManager.process(PHPTokenTypes.opINCREMENT); }
	"--"                               { return opManager.process(PHPTokenTypes.opDECREMENT); }
	"~"                                { return PHPTokenTypes.UNKNOWN_SYMBOL; }
	"-"                                { return opManager.process(PHPTokenTypes.opMINUS); }
	"+"                                { return opManager.process(PHPTokenTypes.opPLUS); }
	"==="                              { return opManager.process(PHPTokenTypes.opIDENTICAL); }
	"!=="                              { return opManager.process(PHPTokenTypes.opNOT_IDENTICAL); }
	"=="                               { return opManager.process(PHPTokenTypes.opEQUAL); }
	"!="                               { return opManager.process(PHPTokenTypes.opNOT_EQUAL); }
	"<>"                               { return opManager.process(PHPTokenTypes.opNOT_EQUAL); }
	"<<"                               { return opManager.process(PHPTokenTypes.opSHIFT_LEFT); }
	">>"                               { return opManager.process(PHPTokenTypes.opSHIFT_RIGHT); }
	"<="                               { return opManager.process(PHPTokenTypes.opLESS_OR_EQUAL); }
	">="                               { return opManager.process(PHPTokenTypes.opGREATER_OR_EQUAL); }
	">"                                { return opManager.process(PHPTokenTypes.opGREATER); }
	"<"                                { return opManager.process(PHPTokenTypes.opLESS); }
	">>="                              { return opManager.process(PHPTokenTypes.opSHIFT_RIGHT_ASGN); }
	"<<="                              { return opManager.process(PHPTokenTypes.opSHIFT_LEFT_ASGN); }
	"+="                               { return opManager.process(PHPTokenTypes.opPLUS_ASGN); }
	"-="                               { return opManager.process(PHPTokenTypes.opMINUS_ASGN); }
	"*="                               { return opManager.process(PHPTokenTypes.opMUL_ASGN); }
	"/="                               { return opManager.process(PHPTokenTypes.opDIV_ASGN); }
	"%="                               { return opManager.process(PHPTokenTypes.opREM_ASGN); }
	".="                               { return opManager.process(PHPTokenTypes.opCONCAT_ASGN); }
	"&="                               { return opManager.process(PHPTokenTypes.opBIT_AND_ASGN); }
	"|="                               { return opManager.process(PHPTokenTypes.opBIT_OR_ASGN); }
	"^="                               { return opManager.process(PHPTokenTypes.opBIT_XOR_ASGN); }
	"="                                { return opManager.process(PHPTokenTypes.opASGN); }
	"@"                                { return opManager.process(PHPTokenTypes.opSILENCE); }
	"&"                                { return opManager.process(PHPTokenTypes.opBIT_AND); }
	"|"                                { return opManager.process(PHPTokenTypes.opBIT_OR); }
	"^"                                { return opManager.process(PHPTokenTypes.opBIT_XOR); }
	"."                                { return opManager.process(PHPTokenTypes.opCONCAT); }
	"&&"                               { return opManager.process(PHPTokenTypes.opAND); }
	"||"                               { return opManager.process(PHPTokenTypes.opOR); }
	","                                { return opManager.process(PHPTokenTypes.opCOMMA); }
	"?"                                { return opManager.process(PHPTokenTypes.opQUEST); }
	"%"                                { return opManager.process(PHPTokenTypes.opREM); }
	"!"                                { return opManager.process(PHPTokenTypes.opNOT); }
	":"                                { return opManager.process(PHPTokenTypes.opCOLON); }
	";"                                { return opManager.process(PHPTokenTypes.opSEMICOLON); }
	"*"                                { return opManager.process(PHPTokenTypes.opMUL); }
	"/"                                { return opManager.process(PHPTokenTypes.opDIV); }
	"=>"                               { return opManager.process(PHPTokenTypes.opHASH_ARRAY); }


	"["                                { return opManager.process(PHPTokenTypes.chLBRACKET); }
	"]"                                { return opManager.process(PHPTokenTypes.chRBRACKET); }
	"("                                { return opManager.process(PHPTokenTypes.chLPAREN); }
	")"                                { return opManager.process(PHPTokenTypes.chRPAREN); }

	"{"                                { sManager.toState(ST_IN_SCRIPTING); return opManager.process(PHPTokenTypes.chLBRACE); }
	"}"                                { if (sManager.getStackSize() > 1) {sManager.toPreviousState();} return opManager.process(PHPTokenTypes.chRBRACE); }
	"$"                                { return PHPTokenTypes.DOLLAR; }
	"::"                               { return PHPTokenTypes.SCOPE_RESOLUTION; }
}

<ST_DOUBLE_QUOTES,ST_BACKQUOTE,ST_HEREDOC>"${" {
	sManager.toState(ST_LOOKING_FOR_VARNAME);
	return PHPTokenTypes.DOLLAR_LBRACE;
}

<ST_LOOKING_FOR_VARNAME>{LABEL} {
	sManager.toPreviousState();
	sManager.toState(ST_IN_SCRIPTING);
	return PHPTokenTypes.VARIABLE_NAME;
}

<ST_LOOKING_FOR_VARNAME>{ANY_CHAR} {
	yypushback(1);
	sManager.toPreviousState();
	sManager.toState(ST_IN_SCRIPTING);
}

<ST_IN_SCRIPTING>"->" {
	sManager.toState(ST_LOOKING_FOR_PROPERTY);
	return PHPTokenTypes.ARROW;
}

<ST_LOOKING_FOR_PROPERTY>"->" {
	return PHPTokenTypes.ARROW;
}

<ST_LOOKING_FOR_PROPERTY>{LABEL} {
	sManager.toPreviousState();
	return PHPTokenTypes.IDENTIFIER;
}

<ST_LOOKING_FOR_PROPERTY>{ANY_CHAR} {
	yypushback(1);
	sManager.toPreviousState();
}

// structure
<YYINITIAL>(([^<]|"<"[^?%s<]){1,400})|"<s"|"<" {
	return PHPTokenTypes.HTML;
}

<YYINITIAL>"<%=" |"<?=" | "<%" |"<?php" | "<?" {
	sManager.toState(ST_IN_SCRIPTING);
	return PHPTokenTypes.PHP_ECHO_OPENING_TAG;
}

<ST_IN_SCRIPTING,ST_DOUBLE_QUOTES,ST_HEREDOC,ST_BACKQUOTE,ST_VAR_OFFSET>"$"{LABEL} {
	return PHPTokenTypes.VARIABLE;
}

<ST_DOUBLE_QUOTES,ST_HEREDOC,ST_BACKQUOTE>"$"{LABEL}"->"[a-zA-Z_\x7f-\xff] {
	yypushback(3);
	sManager.toState(ST_LOOKING_FOR_PROPERTY);
	return PHPTokenTypes.VARIABLE;
}

<ST_DOUBLE_QUOTES,ST_HEREDOC,ST_BACKQUOTE>"$"{LABEL}"[" {
	yypushback(1);
	sManager.toState(ST_VAR_OFFSET);
	return PHPTokenTypes.VARIABLE;
}

<ST_VAR_OFFSET>"[" {
	return PHPTokenTypes.chLBRACKET;
}

<ST_VAR_OFFSET>"]" {
	sManager.toPreviousState();
	return PHPTokenTypes.chRBRACKET;
}

<ST_IN_SCRIPTING,ST_VAR_OFFSET>{LABEL} {
	return PHPTokenTypes.IDENTIFIER;
}

<ST_IN_SCRIPTING>{WHITESPACE} {
	return PHPTokenTypes.WHITE_SPACE;
}

<ST_IN_SCRIPTING>{
	{C_STYLE_COMMENT}                  { return PHPTokenTypes.C_STYLE_COMMENT; }
	{DOC_COMMENT}                      { return PHPTokenTypes.DOC_COMMENT; }
}

<ST_IN_SCRIPTING>"#" {
	int eatResult = lcManager.eat();
	if (eatResult == LineCommentManager.END_SEEN) {
		return PHPTokenTypes.LINE_COMMENT;
	}
	if (eatResult == LineCommentManager.CLOSING_TAG_SEEN) {
		return PHPTokenTypes.LINE_COMMENT;
	}
	assert(eatResult > 0);
	zzMarkedPos += eatResult;
	return PHPTokenTypes.LINE_COMMENT;
}

<ST_IN_SCRIPTING>"//" {
	int eatResult = lcManager.eat();
	if (eatResult == LineCommentManager.END_SEEN) {
		return PHPTokenTypes.LINE_COMMENT;
	}
	if (eatResult == LineCommentManager.CLOSING_TAG_SEEN) {
		return PHPTokenTypes.LINE_COMMENT;
	}
	assert(eatResult > 0);
	zzMarkedPos += eatResult - 1;
	return PHPTokenTypes.LINE_COMMENT;
}

<ST_IN_SCRIPTING>"?>" {
	yybegin(YYINITIAL);
	return PHPTokenTypes.PHP_CLOSING_TAG;
}

<ST_IN_SCRIPTING>"%>" {
	yybegin(YYINITIAL);
	return PHPTokenTypes.PHP_CLOSING_TAG;
}

<ST_IN_SCRIPTING>("\"" {DOUBLE_QUOTES_CHARS}* ("{"* | "$"*) "\"") {
	return PHPTokenTypes.STRING_LITERAL;
}

<ST_IN_SCRIPTING>("'" ([^'\\]|("\\"{ANY_CHAR}))* "'") {
	return PHPTokenTypes.STRING_LITERAL_SINGLE_QUOTE;
}

<ST_IN_SCRIPTING>"\"" {
	sManager.toState(ST_DOUBLE_QUOTES);
	return PHPTokenTypes.chDOUBLE_QUOTE;
}

<ST_IN_SCRIPTING>"`" {
	sManager.toState(ST_BACKQUOTE);
	return PHPTokenTypes.chBACKTRICK;
}

<ST_IN_SCRIPTING>{HEREDOC_START} {
	hdManager.startHeredoc();
	return opManager.process(PHPTokenTypes.HEREDOC_START);
}

<ST_START_HEREDOC>{ANY_CHAR} {
	yypushback(1);
	yybegin(ST_HEREDOC);
}

<ST_START_HEREDOC>{LABEL}";"?[\n\r] {
	String text = yytext().toString();
	int labelEnd = text.length() - 1;
	char semicolon = text.charAt(text.length() - 2);
	if (semicolon == ';') {
		labelEnd--;
		yypushback(2);
	} else {
		yypushback(1);
	}
	String label = text.substring(0, labelEnd);
	if (label.equals(hdManager.getHeredocID())) {
		sManager.toPreviousState();
		return PHPTokenTypes.HEREDOC_END;
	}
	return advance();

}

<ST_HEREDOC>{ANY_CHAR} {
	int eatResult = hdManager.eat();
	if (eatResult == HeredocManager.END_SEEN) {
		sManager.toPreviousState();
		zzMarkedPos += hdManager.getHeredocEndLength();
		return PHPTokenTypes.HEREDOC_END;
	}
	if (eatResult == HeredocManager.SIMPLE_ESCAPE_SEEN) {
		zzMarkedPos += 1;
		return PHPTokenTypes.ESCAPE_SEQUENCE;
	}
	assert(eatResult > 0);
	zzMarkedPos += eatResult - 1;
	return PHPTokenTypes.HEREDOC_CONTENTS;
}

<ST_DOUBLE_QUOTES,ST_BACKQUOTE,ST_HEREDOC>"{$" {
	yypushback(1);
	sManager.toState(ST_IN_SCRIPTING);
	return PHPTokenTypes.chLBRACE;
}

<ST_DOUBLE_QUOTES>{DOUBLE_QUOTES_CHARS}+ {
	return PHPTokenTypes.STRING_LITERAL;
}

<ST_DOUBLE_QUOTES>{DOUBLE_QUOTES_CHARS}*("{"{2,200}|"$"{2,200}|(("{"+|"$"+)"\"")) {
	yypushback(1);
	return PHPTokenTypes.STRING_LITERAL;
}

<ST_BACKQUOTE>{BACKQUOTE_CHARS}+ {
	return PHPTokenTypes.EXEC_COMMAND;
}

<ST_BACKQUOTE>{BACKQUOTE_CHARS}*("{"{2,5}|"$"{2,5}|(("{"+|"$"+)[`])) {
	yypushback(1);
	return PHPTokenTypes.EXEC_COMMAND;
}

<ST_DOUBLE_QUOTES>"\"" {
	sManager.toPreviousState();
	return PHPTokenTypes.chDOUBLE_QUOTE;
}

<ST_BACKQUOTE>"`" {
	sManager.toPreviousState();
	return PHPTokenTypes.chBACKTRICK;
}

<ST_IN_SCRIPTING,ST_DOUBLE_QUOTES,ST_BACKQUOTE,ST_HEREDOC,ST_START_HEREDOC,ST_END_HEREDOC,ST_LOOKING_FOR_PROPERTY,ST_LOOKING_FOR_VARNAME,ST_VAR_OFFSET,ST_COMMENT,ST_DOC_COMMENT,ST_ONE_LINE_COMMENT>{ANY_CHAR} {
	return PHPTokenTypes.UNKNOWN_SYMBOL;
}