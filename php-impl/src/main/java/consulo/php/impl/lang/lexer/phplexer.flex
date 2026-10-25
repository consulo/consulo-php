package consulo.php.impl.lang.lexer;

import consulo.language.ast.IElementType;
import consulo.language.lexer.FlexLexer;
import consulo.php.PhpLanguageLevel;
import consulo.php.impl.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import consulo.php.impl.lang.lexer.managers.*;
import consulo.php.lang.lexer.PhpTokenTypes;

%%

%{
  private boolean myHighlightingMode;
  private PhpLanguageLevel myLanguageLevel;
  // __halt_compiler seen: everything after the next ';' or '?>' is raw data
  private boolean myHaltCompiler;
  // '{' nesting: __halt_compiler() stops lexing only at the outermost scope
  private int myBraceDepth;

	public _PhpFlexLexer(boolean highlightingMode, PhpLanguageLevel languageLevel) {
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


	public final CharSequence getBuffer(){
		return zzBuffer;
	}

	private void reset(int initialState){
		opManager.reset();
		lcManager.reset();
		sManager.reset();
		hdManager.reset();
		brcManager = null;
		myHaltCompiler = false;
		myBraceDepth = 0;

		sManager.toState(initialState);
	}

%}

%class _PhpFlexLexer
%implements FlexLexer
%public
%unicode
%function advance
%type IElementType

LNUM =					[0-9]+
DNUM =					([0-9]*[\.][0-9]+)|([0-9]+[\.][0-9]*)
EXPONENT_DNUM = 			(({LNUM}|{DNUM})[eE][+-]?{LNUM})
HNUM = 					"0x"[0-9a-fA-F]+
LABEL= 					[a-zA-Z_\x7f-\xff][a-zA-Z0-9_\x7f-\xff]*
WHITESPACE = 				[ \n\r\t]+
TABS_AND_SPACES = 			[ \t]*
//TOKENS = 				[;:,.\[\]()|^&+-/*=%!~$<>?@]
ANY_CHAR = 				[^]
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

DIGITS =                           [0-9]("_"?[0-9])*
DECIMAL_INTEGER =                  [1-9]("_"?[0-9])* | 0
HEX_INTEGER =                      0[xX][0-9a-fA-F]("_"?[0-9a-fA-F])*
OCTAL_INTEGER =                    0[oO]?("_"?[0-7])+
INTEGER_LITERAL =                  ({DECIMAL_INTEGER} | {HEX_INTEGER} | {OCTAL_INTEGER})
BIN_INTEGER_LITERAL =              0[bB][01]("_"?[01])*

FLOATING_POINT_LITERAL1 =          {DIGITS}?"."{DIGITS}{EXPONENT_PART}? | {DIGITS}"."{EXPONENT_PART}?
FLOATING_POINT_LITERAL2 =          {DIGITS}{EXPONENT_PART}
EXPONENT_PART =                    [Ee][+-]?{DIGITS}
FLOAT_LITERAL =                    {FLOATING_POINT_LITERAL1} | {FLOATING_POINT_LITERAL2}

LINE_COMMENT_WITH_CLOSING_TAG =    ("//" | "#") .* "?>"
LINE_COMMENT_NORMAL =              ("//" | "#") .*
LINE_COMMENT =                     {LINE_COMMENT_NORMAL} | {LINE_COMMENT_WITH_CLOSING_TAG}

C_STYLE_COMMENT =                  ("/*"[^"*"]{COMMENT_TAIL})|"/*"
DOC_COMMENT =                      "/*""*"+("/"|([^"/""*"]{COMMENT_TAIL}))?
COMMENT_TAIL =                     ([^"*"]*("*"+[^"*""/"])?)*("*"+"/")?

HEREDOC_START =                    [bB]? "<<<" {TABS_AND_SPACES} ({LABEL} | "\"" {LABEL} "\"" | "'" {LABEL} "'") {NEWLINE}

//===================================================================
//==========================CASTS====================================
//===================================================================

CAST_BEGIN =                       "(" {TABS_AND_SPACES}
CAST_END =                         {TABS_AND_SPACES} ")"

INTEGER_CAST =                     {CAST_BEGIN} ([iI][nN][tT] | [iI][nN][tT][eE][gG][eE][rR]) {CAST_END}
FLOAT_CAST =                       {CAST_BEGIN} ([fF][lL][oO][aA][tT] | [dD][oO][uU][bB][lL][eE]) {CAST_END}
BOOLEAN_CAST =                     {CAST_BEGIN} ([bB][oO][oO][lL] | [bB][oO][oO][lL][eE][aA][nN]) {CAST_END}
STRING_CAST =                      {CAST_BEGIN} ([sS][tT][rR][iI][nN][gG] | [bB][iI][nN][aA][rR][yY]) {CAST_END}
ARRAY_CAST  =                      {CAST_BEGIN} [aA][rR][rR][aA][yY] {CAST_END}
OBJECT_CAST  =                     {CAST_BEGIN} [oO][bB][jJ][eE][cC][tT] {CAST_END}
UNSET_CAST =                       {CAST_BEGIN} [uU][nN][sS][eE][tT] {CAST_END}
VOID_CAST =                        {CAST_BEGIN} [vV][oO][iI][dD] {CAST_END}


%x ST_IN_SCRIPTING
%x ST_DOUBLE_QUOTES
%x ST_BACKQUOTE
%x ST_HEREDOC
%x ST_NOWDOC
%x ST_HALTED
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
	{INTEGER_LITERAL}                  { return opManager.process(PhpTokenTypes.INTEGER_LITERAL); }
	{BIN_INTEGER_LITERAL}              { return opManager.process(PhpTokenTypes.BINARY_LITERAL); }
	{FLOAT_LITERAL}                    { return opManager.process(PhpTokenTypes.FLOAT_LITERAL); }
}

<ST_VAR_OFFSET>{
	{INTEGER_LITERAL}                  { return PhpTokenTypes.VARIABLE_OFFSET_NUMBER; }
}


//========================OPERATORS===================================

<ST_IN_SCRIPTING>{
	{INTEGER_CAST}                     { return opManager.process(PhpTokenTypes.opINTEGER_CAST); }
	{FLOAT_CAST}                       { return opManager.process(PhpTokenTypes.opFLOAT_CAST); }
	{BOOLEAN_CAST}                     { return opManager.process(PhpTokenTypes.opBOOLEAN_CAST); }
	{STRING_CAST}                      { return opManager.process(PhpTokenTypes.opSTRING_CAST); }
	{ARRAY_CAST}                       { return opManager.process(PhpTokenTypes.opARRAY_CAST); }
	{OBJECT_CAST}                      { return opManager.process(PhpTokenTypes.opOBJECT_CAST); }
	{UNSET_CAST}                       { return opManager.process(PhpTokenTypes.opUNSET_CAST); }
	{VOID_CAST}                        { return opManager.process(PhpTokenTypes.opVOID_CAST); }

	"++"                               { return opManager.process(PhpTokenTypes.opINCREMENT); }
	"--"                               { return opManager.process(PhpTokenTypes.opDECREMENT); }
	"~"                                { return opManager.process(PhpTokenTypes.opBIT_NOT); }
	"-"                                { return opManager.process(PhpTokenTypes.opMINUS); }
	"+"                                { return opManager.process(PhpTokenTypes.opPLUS); }
	"==="                              { return opManager.process(PhpTokenTypes.opIDENTICAL); }
	"!=="                              { return opManager.process(PhpTokenTypes.opNOT_IDENTICAL); }
	"=="                               { return opManager.process(PhpTokenTypes.opEQUAL); }
	"!="                               { return opManager.process(PhpTokenTypes.opNOT_EQUAL); }
	"<>"                               { return opManager.process(PhpTokenTypes.opNOT_EQUAL); }
	"<=>"                              { return opManager.process(PhpTokenTypes.opSPACESHIP); }
	"??="                              { return opManager.process(PhpTokenTypes.opCOALESCE_ASGN); }
	"??"                               { return opManager.process(PhpTokenTypes.opCOALESCE); }
	"**="                              { return opManager.process(PhpTokenTypes.opPOW_ASGN); }
	"**"                               { return opManager.process(PhpTokenTypes.opPOW); }
	"|>"                               { return opManager.process(PhpTokenTypes.opPIPE); }
	"<<"                               { return opManager.process(PhpTokenTypes.opSHIFT_LEFT); }
	">>"                               { return opManager.process(PhpTokenTypes.opSHIFT_RIGHT); }
	"<="                               { return opManager.process(PhpTokenTypes.opLESS_OR_EQUAL); }
	">="                               { return opManager.process(PhpTokenTypes.opGREATER_OR_EQUAL); }
	">"                                { return opManager.process(PhpTokenTypes.opGREATER); }
	"<"                                { return opManager.process(PhpTokenTypes.opLESS); }
	">>="                              { return opManager.process(PhpTokenTypes.opSHIFT_RIGHT_ASGN); }
	"<<="                              { return opManager.process(PhpTokenTypes.opSHIFT_LEFT_ASGN); }
	"+="                               { return opManager.process(PhpTokenTypes.opPLUS_ASGN); }
	"-="                               { return opManager.process(PhpTokenTypes.opMINUS_ASGN); }
	"*="                               { return opManager.process(PhpTokenTypes.opMUL_ASGN); }
	"/="                               { return opManager.process(PhpTokenTypes.opDIV_ASGN); }
	"%="                               { return opManager.process(PhpTokenTypes.opREM_ASGN); }
	".="                               { return opManager.process(PhpTokenTypes.opCONCAT_ASGN); }
	"&="                               { return opManager.process(PhpTokenTypes.opBIT_AND_ASGN); }
	"|="                               { return opManager.process(PhpTokenTypes.opBIT_OR_ASGN); }
	"^="                               { return opManager.process(PhpTokenTypes.opBIT_XOR_ASGN); }
	"="                                { return opManager.process(PhpTokenTypes.opASGN); }
	"@"                                { return opManager.process(PhpTokenTypes.opSILENCE); }
	"&"                                { return opManager.process(PhpTokenTypes.opBIT_AND); }
	"|"                                { return opManager.process(PhpTokenTypes.opBIT_OR); }
	"^"                                { return opManager.process(PhpTokenTypes.opBIT_XOR); }
	"."                                { return opManager.process(PhpTokenTypes.opCONCAT); }
	"&&"                               { return opManager.process(PhpTokenTypes.opAND); }
	"||"                               { return opManager.process(PhpTokenTypes.opOR); }
	","                                { return opManager.process(PhpTokenTypes.opCOMMA); }
	"?"                                { return opManager.process(PhpTokenTypes.opQUEST); }
	"?:"                               { return opManager.process(PhpTokenTypes.ELVIS); }
	"%"                                { return opManager.process(PhpTokenTypes.opREM); }
	"!"                                { return opManager.process(PhpTokenTypes.opNOT); }
	":"                                { return opManager.process(PhpTokenTypes.opCOLON); }
	";"                                { if (myHaltCompiler && myBraceDepth == 0) { yybegin(ST_HALTED); } return opManager.process(PhpTokenTypes.opSEMICOLON); }
	"*"                                { return opManager.process(PhpTokenTypes.opMUL); }
	"/"                                { return opManager.process(PhpTokenTypes.opDIV); }
	"\\"                               { return myLanguageLevel.isAtLeast(PhpLanguageLevel.PHP_5_3) ? opManager.process(PhpTokenTypes.SLASH) : PhpTokenTypes.UNKNOWN_SYMBOL; }
	"=>"                               { return opManager.process(PhpTokenTypes.HASH_ARRAY); }
	"..."                              { return opManager.process(PhpTokenTypes.ELLIPSIS); }

	"["                                { return opManager.process(PhpTokenTypes.LBRACKET); }
	"]"                                { return opManager.process(PhpTokenTypes.RBRACKET); }
	"("                                { return opManager.process(PhpTokenTypes.LPAREN); }
	")"                                { return opManager.process(PhpTokenTypes.RPAREN); }

	"{"                                { myBraceDepth++; sManager.toState(ST_IN_SCRIPTING); return opManager.process(PhpTokenTypes.LBRACE); }
	"}"                                { if (myBraceDepth > 0) {myBraceDepth--;} if (sManager.getStackSize() > 1) {sManager.toPreviousState();} return opManager.process(PhpTokenTypes.RBRACE); }
	"$"                                { return PhpTokenTypes.DOLLAR; }
	"::"                               { return PhpTokenTypes.SCOPE_RESOLUTION; }
}

<ST_DOUBLE_QUOTES,ST_BACKQUOTE,ST_HEREDOC>"${" {
	myBraceDepth++;
	sManager.toState(ST_LOOKING_FOR_VARNAME);
	return PhpTokenTypes.DOLLAR_LBRACE;
}

<ST_LOOKING_FOR_VARNAME>{LABEL}/[\[}] {
	sManager.toPreviousState();
	sManager.toState(ST_IN_SCRIPTING);
	return PhpTokenTypes.VARIABLE_NAME;
}

<ST_LOOKING_FOR_VARNAME>{ANY_CHAR} {
	yypushback(1);
	sManager.toPreviousState();
	sManager.toState(ST_IN_SCRIPTING);
}

<ST_IN_SCRIPTING>"->" {
	sManager.toState(ST_LOOKING_FOR_PROPERTY);
	return PhpTokenTypes.ARROW;
}

<ST_IN_SCRIPTING>"?->" {
	sManager.toState(ST_LOOKING_FOR_PROPERTY);
	return PhpTokenTypes.NULLSAFE_ARROW;
}

<ST_IN_SCRIPTING>"#[" {
	if (myLanguageLevel.isAtLeast(PhpLanguageLevel.PHP_8_0)) {
		return PhpTokenTypes.ATTRIBUTE_START;
	}
	yypushback(1);
	int eatResult = lcManager.eat();
	if (eatResult > 0) {
		zzMarkedPos += eatResult;
	}
	return PhpTokenTypes.LINE_COMMENT;
}

<ST_LOOKING_FOR_PROPERTY>"->" {
	return PhpTokenTypes.ARROW;
}

<ST_LOOKING_FOR_PROPERTY>"?->" {
	return PhpTokenTypes.NULLSAFE_ARROW;
}

<ST_LOOKING_FOR_PROPERTY>{LABEL} {
	sManager.toPreviousState();
	return PhpTokenTypes.IDENTIFIER;
}

<ST_LOOKING_FOR_PROPERTY>{ANY_CHAR} {
	yypushback(1);
	sManager.toPreviousState();
}

// structure
<YYINITIAL>(([^<]|"<"[^?%s<]){1,400})|"<s"|"<" {
	return PhpTokenTypes.HTML;
}
<YYINITIAL>"<?"[pP][hH][pP] | "<?" {
	sManager.toState(ST_IN_SCRIPTING);
	return PhpTokenTypes.PHP_OPENING_TAG;
}

<YYINITIAL>"<%=" |"<?="  {
	sManager.toState(ST_IN_SCRIPTING);
	return PhpTokenTypes.PHP_ECHO_OPENING_TAG;
}

<ST_IN_SCRIPTING,ST_DOUBLE_QUOTES,ST_HEREDOC,ST_BACKQUOTE,ST_VAR_OFFSET>"$"{LABEL} {
	return PhpTokenTypes.VARIABLE;
}

<ST_DOUBLE_QUOTES,ST_HEREDOC,ST_BACKQUOTE>"$"{LABEL}"->"[a-zA-Z_\x7f-\xff] {
	yypushback(3);
	sManager.toState(ST_LOOKING_FOR_PROPERTY);
	return PhpTokenTypes.VARIABLE;
}

<ST_DOUBLE_QUOTES,ST_HEREDOC,ST_BACKQUOTE>"$"{LABEL}"?->"[a-zA-Z_\x7f-\xff] {
	yypushback(4);
	sManager.toState(ST_LOOKING_FOR_PROPERTY);
	return PhpTokenTypes.VARIABLE;
}

<ST_DOUBLE_QUOTES,ST_HEREDOC,ST_BACKQUOTE>"$"{LABEL}"[" {
	yypushback(1);
	sManager.toState(ST_VAR_OFFSET);
	return PhpTokenTypes.VARIABLE;
}

<ST_VAR_OFFSET>"[" {
	return PhpTokenTypes.LBRACKET;
}

<ST_VAR_OFFSET>"-" {
	return PhpTokenTypes.opMINUS;
}

<ST_VAR_OFFSET>"]" {
	sManager.toPreviousState();
	return PhpTokenTypes.RBRACKET;
}

<ST_VAR_OFFSET>{LABEL} {
	return PhpTokenTypes.IDENTIFIER;
}

<ST_IN_SCRIPTING>{LABEL} {
	IElementType type = PhpKeywords.lookup(yytext(), myLanguageLevel);
	if (type == PhpTokenTypes.HALT_COMPILER_KEYWORD) {
		myHaltCompiler = true;
	}
	return opManager.process(type);
}

<ST_IN_SCRIPTING>{WHITESPACE} {
	return PhpTokenTypes.WHITE_SPACE;
}

<ST_IN_SCRIPTING>{
	"/**/"                             { return PhpTokenTypes.C_STYLE_COMMENT; }
	{C_STYLE_COMMENT}                  { return PhpTokenTypes.C_STYLE_COMMENT; }
	{DOC_COMMENT}                      { return PhpDocElementTypes.DOC_COMMENT; }
}

<ST_IN_SCRIPTING>"#" {
	int eatResult = lcManager.eat();
	if (eatResult == LineCommentManager.END_SEEN) {
		return PhpTokenTypes.LINE_COMMENT;
	}
	if (eatResult == LineCommentManager.CLOSING_TAG_SEEN) {
		return PhpTokenTypes.LINE_COMMENT;
	}
	assert(eatResult > 0);
	zzMarkedPos += eatResult;
	return PhpTokenTypes.LINE_COMMENT;
}

<ST_IN_SCRIPTING>"//" {
	int eatResult = lcManager.eat();
	if (eatResult == LineCommentManager.END_SEEN) {
		return PhpTokenTypes.LINE_COMMENT;
	}
	if (eatResult == LineCommentManager.CLOSING_TAG_SEEN) {
		return PhpTokenTypes.LINE_COMMENT;
	}
	assert(eatResult > 0);
	zzMarkedPos += eatResult - 1;
	return PhpTokenTypes.LINE_COMMENT;
}

<ST_IN_SCRIPTING>"%>" | "?>"  {
	yybegin(myHaltCompiler && myBraceDepth == 0 ? ST_HALTED : YYINITIAL);
	return PhpTokenTypes.PHP_CLOSING_TAG;
}

<ST_HALTED>{ANY_CHAR}+ {
	return PhpTokenTypes.INLINE_DATA;
}

<ST_IN_SCRIPTING>([bB]? "\"" {DOUBLE_QUOTES_CHARS}* ("{"* | "$"*) "\"") {
	return PhpTokenTypes.STRING_LITERAL;
}

<ST_IN_SCRIPTING>([bB]? "'" ([^'\\]|("\\"{ANY_CHAR}))* "'") {
	return PhpTokenTypes.STRING_LITERAL_SINGLE_QUOTE;
}

<ST_IN_SCRIPTING>[bB]? "\"" {
	sManager.toState(ST_DOUBLE_QUOTES);
	return PhpTokenTypes.chDOUBLE_QUOTE;
}

<ST_IN_SCRIPTING>"`" {
	sManager.toState(ST_BACKQUOTE);
	return PhpTokenTypes.chBACKTRICK;
}

<ST_IN_SCRIPTING>{HEREDOC_START} {
	hdManager.startHeredoc();
	return opManager.process(PhpTokenTypes.HEREDOC_START);
}

<ST_START_HEREDOC>{ANY_CHAR} {
	yypushback(1);
	yybegin(hdManager.isNowdoc() ? ST_NOWDOC : ST_HEREDOC);
}

<ST_START_HEREDOC>{TABS_AND_SPACES}{LABEL} {
	String label = yytext().toString().trim();
	if (label.equals(hdManager.getHeredocID()) && (zzMarkedPos >= zzEndRead || !HeredocManager.isLabelChar(zzBuffer.charAt(zzMarkedPos)))) {
		hdManager.endHeredoc();
		sManager.toPreviousState();
		return PhpTokenTypes.HEREDOC_END;
	}
	yypushback(yylength());
	yybegin(hdManager.isNowdoc() ? ST_NOWDOC : ST_HEREDOC);
}

<ST_HEREDOC,ST_NOWDOC>{ANY_CHAR} {
	int eatResult = hdManager.eat();
	if (eatResult == HeredocManager.END_SEEN) {
		sManager.toPreviousState();
		zzMarkedPos += hdManager.getHeredocEndLength();
		hdManager.endHeredoc();
		return PhpTokenTypes.HEREDOC_END;
	}
	if (eatResult == HeredocManager.SIMPLE_ESCAPE_SEEN) {
		zzMarkedPos += 1;
		return PhpTokenTypes.ESCAPE_SEQUENCE;
	}
	assert(eatResult > 0);
	zzMarkedPos += eatResult - 1;
	return PhpTokenTypes.HEREDOC_CONTENTS;
}

<ST_DOUBLE_QUOTES,ST_BACKQUOTE,ST_HEREDOC>"{$" {
	myBraceDepth++;
	yypushback(1);
	sManager.toState(ST_IN_SCRIPTING);
	return PhpTokenTypes.LBRACE;
}

<ST_DOUBLE_QUOTES>{DOUBLE_QUOTES_CHARS}+ {
	return PhpTokenTypes.STRING_LITERAL;
}

<ST_DOUBLE_QUOTES>{DOUBLE_QUOTES_CHARS}*("{"{2,200}|"$"{2,200}|(("{"+|"$"+)"\"")) {
	yypushback(1);
	return PhpTokenTypes.STRING_LITERAL;
}

<ST_BACKQUOTE>{BACKQUOTE_CHARS}+ {
	return PhpTokenTypes.EXEC_COMMAND;
}

<ST_BACKQUOTE>{BACKQUOTE_CHARS}*("{"{2,5}|"$"{2,5}|(("{"+|"$"+)[`])) {
	yypushback(1);
	return PhpTokenTypes.EXEC_COMMAND;
}

<ST_DOUBLE_QUOTES>"\"" {
	sManager.toPreviousState();
	return PhpTokenTypes.chDOUBLE_QUOTE;
}

<ST_BACKQUOTE>"`" {
	sManager.toPreviousState();
	return PhpTokenTypes.chBACKTRICK;
}

<ST_IN_SCRIPTING,ST_DOUBLE_QUOTES,ST_BACKQUOTE,ST_HEREDOC,ST_NOWDOC,ST_START_HEREDOC,ST_END_HEREDOC,ST_LOOKING_FOR_PROPERTY,ST_LOOKING_FOR_VARNAME,ST_VAR_OFFSET,ST_COMMENT,ST_DOC_COMMENT,ST_ONE_LINE_COMMENT>{ANY_CHAR} {
	return PhpTokenTypes.UNKNOWN_SYMBOL;
}