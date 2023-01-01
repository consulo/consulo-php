package consulo.php.impl.lang.highlighter.hierarchy;

import consulo.codeEditor.markup.GutterIconRenderer;
import consulo.codeEditor.markup.RangeHighlighter;
import consulo.codeEditor.markup.SeparatorPlacement;
import consulo.colorScheme.TextAttributes;

import java.awt.*;

/**
 * @author jay
 * @date Jun 25, 2008 12:53:18 AM
 */
abstract public class PhpLineMarkerInfo
{

	public static final String SPACE = "&nbsp;";
	public static final String BREAK = "<br>";

	public final int startOffset;
	public TextAttributes attributes;
	public Color separatorColor;
	public SeparatorPlacement separatorPlacement;
	public RangeHighlighter highlighter;


	public PhpLineMarkerInfo(final int startOffset)
	{
		this.startOffset = startOffset;
	}

	abstract public GutterIconRenderer createGutterRenderer();

}
