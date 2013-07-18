/* The following code was generated by JFlex 1.4.3 on 18.07.13 19:01 */


package net.jay.plugins.php.lang.documentation.phpdoc.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 18.07.13 19:01 from the specification file
 * <tt>F:/github.com/consulo/consulo-php/src/net/jay/plugins/php/lang/documentation/phpdoc/lexer/PhpDocFlexLexer.flex</tt>
 */
public class PhpDocFlexLexer implements FlexLexer, PhpDocTokenTypes {
  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int AFTER_LBRACE = 8;
  public static final int AFTER_ASTERISK = 6;
  public static final int YYINITIAL = 0;
  public static final int AFTER_LINEBREAK = 4;
  public static final int IN_COMMENT = 2;
  public static final int IGNORED_LINE = 10;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  2,  2,  3,  3,  4,  4,  5, 5
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\4\1\6\2\0\1\5\22\0\1\4\3\0\1\3\5\0"+
    "\1\31\3\0\1\32\1\10\12\2\2\0\1\7\1\0\1\30\1\0"+
    "\1\35\32\1\4\0\1\1\1\0\1\24\1\11\1\13\1\15\1\16"+
    "\3\1\1\17\1\1\1\22\1\21\1\27\1\1\1\14\1\25\1\1"+
    "\1\12\1\26\1\1\1\20\1\23\4\1\1\34\1\33\1\36\1\0"+
    "\201\1\uff00\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\6\0\3\1\1\2\1\3\1\2\1\4\2\5\3\2"+
    "\1\6\1\7\2\10\1\4\1\11\3\12\3\2\1\13"+
    "\2\1\1\0\1\14\12\0\1\15\1\16\1\17\1\20"+
    "\1\21\1\22\6\0";

  private static int [] zzUnpackAction() {
    int [] result = new int[57];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\37\0\76\0\135\0\174\0\233\0\272\0\331"+
    "\0\370\0\272\0\u0117\0\u0136\0\u0155\0\u0174\0\272\0\u0193"+
    "\0\u01b2\0\u01d1\0\272\0\272\0\272\0\u01f0\0\272\0\u01b2"+
    "\0\272\0\u0174\0\u020f\0\u0155\0\u0174\0\u022e\0\272\0\u0174"+
    "\0\u01b2\0\u024d\0\u026c\0\u028b\0\u02aa\0\u02c9\0\u02e8\0\u0307"+
    "\0\u0326\0\u0345\0\u0364\0\u0383\0\u03a2\0\272\0\u01d1\0\u03c1"+
    "\0\u03e0\0\272\0\272\0\u03ff\0\u041e\0\u043d\0\u045c\0\u047b"+
    "\0\u049a";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[57];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\5\7\1\10\2\7\1\11\26\7\1\12\1\13\1\12"+
    "\1\14\1\15\1\16\1\17\1\20\1\12\17\13\1\12"+
    "\1\21\1\22\1\23\1\24\2\12\4\25\1\15\1\26"+
    "\1\27\22\25\1\30\5\25\4\31\1\15\1\32\1\17"+
    "\26\31\1\33\1\31\1\12\1\13\2\12\1\34\1\35"+
    "\1\17\2\12\17\13\5\12\1\36\1\37\5\7\1\40"+
    "\1\17\22\7\1\41\5\7\45\0\1\7\61\0\1\42"+
    "\6\0\2\13\6\0\17\13\10\0\1\43\7\0\17\43"+
    "\13\0\1\15\40\0\1\17\40\0\1\44\1\45\1\0"+
    "\1\46\1\47\2\0\1\50\1\47\1\51\1\52\1\53"+
    "\1\0\1\54\1\55\20\0\1\56\32\0\1\57\40\0"+
    "\1\27\31\0\1\60\7\0\17\60\10\0\1\61\7\0"+
    "\17\61\40\0\1\62\6\0\2\43\6\0\17\43\20\0"+
    "\1\45\1\0\1\46\1\47\2\0\1\50\1\47\1\51"+
    "\1\52\1\53\1\0\1\54\1\55\22\0\1\50\15\0"+
    "\1\63\22\0\1\64\43\0\1\50\45\0\1\63\25\0"+
    "\1\50\30\0\1\65\51\0\1\66\24\0\1\67\15\0"+
    "\1\63\32\0\1\70\13\0\2\60\6\0\17\60\10\0"+
    "\2\61\6\0\17\61\24\0\1\67\36\0\1\50\33\0"+
    "\1\50\42\0\1\50\47\0\1\71\34\0\1\50\11\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[1209];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;
  private static final char[] EMPTY_BUFFER = new char[0];
  private static final int YYEOF = -1;
  private static java.io.Reader zzReader = null; // Fake

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\6\0\1\11\2\1\1\11\4\1\1\11\3\1\3\11"+
    "\1\1\1\11\1\1\1\11\5\1\1\11\2\1\1\0"+
    "\1\1\12\0\1\11\3\1\2\11\6\0";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[57];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** this buffer may contains the current text array to be matched when it is cheap to acquire it */
  private char[] zzBufferArray;

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the textposition at the last state to be included in yytext */
  private int zzPushbackPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */

  public PhpDocFlexLexer() {
    this((java.io.Reader) null);
  }



  public PhpDocFlexLexer(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public PhpDocFlexLexer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 104) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  public final int getTokenStart(){
    return zzStartRead;
  }

  public final int getTokenEnd(){
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end,int initialState){
    zzBuffer = buffer;
    zzBufferArray = com.intellij.util.text.CharArrayUtil.fromSequenceWithoutCopying(buffer);
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzPushbackPos = 0;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position <tt>pos</tt> from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBufferArray != null ? zzBufferArray[zzStartRead+pos]:zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() {
    if (!zzEOFDone) {
      zzEOFDone = true;
    
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;
    char[] zzBufferArrayL = zzBufferArray;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL.charAt(zzCurrentPosL++);
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL.charAt(zzCurrentPosL++);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 13: 
          { yybegin(YYINITIAL);
  return DOC_COMMENT_END;
          }
        case 19: break;
        case 17: 
          { yybegin(IN_COMMENT);
  return DOC_COMMENT_START;
          }
        case 20: break;
        case 6: 
          { return DOC_PIPE;
          }
        case 21: break;
        case 9: 
          { yybegin(AFTER_ASTERISK);
  return DOC_LEADING_ASTERISK;
          }
        case 22: break;
        case 8: 
          { yybegin(IGNORED_LINE);
  yypushback(1);
          }
        case 23: break;
        case 10: 
          { yybegin(IN_COMMENT);
  yypushback(1);
          }
        case 24: break;
        case 15: 
          { yybegin(IN_COMMENT);
  return DOC_TAG_NAME;
          }
        case 25: break;
        case 18: 
          { return DOC_HTML_TAG;
          }
        case 26: break;
        case 2: 
          { return DOC_TEXT;
          }
        case 27: break;
        case 1: 
          { return DOC_IGNORED;
          }
        case 28: break;
        case 4: 
          { return DOC_WHITESPACE;
          }
        case 29: break;
        case 5: 
          { yybegin(AFTER_LINEBREAK);
  return DOC_WHITESPACE;
          }
        case 30: break;
        case 16: 
          { return DOC_TAG_NAME;
          }
        case 31: break;
        case 14: 
          { yypushback(yytext().length() - 1);
  return DOC_DOT;
          }
        case 32: break;
        case 12: 
          { return DOC_VARIABLE;
          }
        case 33: break;
        case 3: 
          { return DOC_IDENTIFIER;
          }
        case 34: break;
        case 11: 
          { yybegin(IN_COMMENT);
  return DOC_RBRACE;
          }
        case 35: break;
        case 7: 
          { yybegin(AFTER_LBRACE);
  return DOC_LBRACE;
          }
        case 36: break;
        default:
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
            return null;
          }
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
