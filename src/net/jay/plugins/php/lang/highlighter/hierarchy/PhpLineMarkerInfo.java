package net.jay.plugins.php.lang.highlighter.hierarchy;

import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.editor.markup.SeparatorPlacement;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.GutterIconRenderer;

import java.awt.*;

/**
 * @author jay
 * @date Jun 25, 2008 12:53:18 AM
 */
abstract public class PhpLineMarkerInfo {

  public static final String SPACE = "&nbsp;";
  public static final String BREAK = "<br>";

  public final int startOffset;
  public TextAttributes attributes;
  public Color separatorColor;
  public SeparatorPlacement separatorPlacement;
  public RangeHighlighter highlighter;


  public PhpLineMarkerInfo(final int startOffset) {
    this.startOffset = startOffset;
  }

  abstract public GutterIconRenderer createGutterRenderer();

}
