package net.jay.plugins.php.lang.highlighter;

import java.util.Map;

import javax.swing.Icon;

import net.jay.plugins.php.PHPBundle;
import net.jay.plugins.php.PHPIcons2;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 13.03.2007
 *
 * @author jay
 */
public class PHPColorsPage implements ColorSettingsPage
{

	private static final String DEMO_TEXT = "<?php\n" +
			"$heredoc = <<< HEREDOC_ID\n" +
			"some $contents\n" +
			"HEREDOC_ID;\n" +
			"\n" +
			"// Sample comment\n\n" +
			"class SomeClass {\n" +
			"   /**\n" +
			"    * Description by <a href=\"mailto:\">alex</a>\n" +
			"    * @return SomeType\n" +
			"    */\n" +
			"   function doSmth($abc, $def) {\n" +
			"      $v = Helper::convert($abc . \"\\n {$def}\");\n" +
			"      $q = new Query( $this->invent(abs(0x80)) );\n" +
			"      return array($v => $q->result);\n" +
			"   }\n" +
			"}\n\n" +
			"include (dirname(__FILE__) . \"inc.php\");\n" +
			"`rm -r`;\n" +
			"\\";


	private static final AttributesDescriptor[] ATTRS = new AttributesDescriptor[]{
			new AttributesDescriptor(PHPBundle.message("color.settings.keyword"), PHPHighlightingData.KEYWORD),
			new AttributesDescriptor(PHPBundle.message("color.settings.comment"), PHPHighlightingData.COMMENT),
			new AttributesDescriptor(PHPBundle.message("color.settings.number"), PHPHighlightingData.NUMBER),
			new AttributesDescriptor(PHPBundle.message("color.settings.string"), PHPHighlightingData.STRING),
			new AttributesDescriptor(PHPBundle.message("color.settings.exec_command"), PHPHighlightingData.EXEC_COMMAND),
			new AttributesDescriptor(PHPBundle.message("color.settings.escape_sequence"), PHPHighlightingData.ESCAPE_SEQUENCE),
			new AttributesDescriptor(PHPBundle.message("color.settings.operation"), PHPHighlightingData.OPERATION_SIGN),
			new AttributesDescriptor(PHPBundle.message("color.settings.brackets"), PHPHighlightingData.BRACKETS),
			new AttributesDescriptor(PHPBundle.message("color.settings.predefined.symbols"), PHPHighlightingData.PREDEFINED_SYMBOL),
			//new AttributesDescriptor(PHPBundle.message("color.settings.expression_subtitution_marks"), PHPHighlightingData.EXPR_SUBST_MARKS),
			new AttributesDescriptor(PHPBundle.message("color.settings.bad_character"), PHPHighlightingData.BAD_CHARACTER),
			new AttributesDescriptor(PHPBundle.message("color.settings.comma"), PHPHighlightingData.COMMA),
			new AttributesDescriptor(PHPBundle.message("color.settings.semicolon"), PHPHighlightingData.SEMICOLON),
			new AttributesDescriptor(PHPBundle.message("color.settings.heredoc_id"), PHPHighlightingData.HEREDOC_ID),
			new AttributesDescriptor(PHPBundle.message("color.settings.heredoc_content"), PHPHighlightingData.HEREDOC_CONTENT),
			new AttributesDescriptor(PHPBundle.message("color.settings.var"), PHPHighlightingData.VAR),
			new AttributesDescriptor(PHPBundle.message("color.settings.identifier"), PHPHighlightingData.IDENTIFIER),
			new AttributesDescriptor(PHPBundle.message("color.settings.constant"), PHPHighlightingData.CONSTANT),
			new AttributesDescriptor(PHPBundle.message("color.settings.doccomment"), PHPHighlightingData.DOC_COMMENT),
			new AttributesDescriptor(PHPBundle.message("color.settings.doctag"), PHPHighlightingData.DOC_TAG),
			new AttributesDescriptor(PHPBundle.message("color.settings.docmarkup"), PHPHighlightingData.DOC_MARKUP)
	};

	@NotNull
	public String getDisplayName()
	{
		return PHPBundle.message("color.settings.name");
	}

	@Nullable
	public Icon getIcon()
	{
		return PHPIcons2.Php;
	}

	@NotNull
	public AttributesDescriptor[] getAttributeDescriptors()
	{
		return ATTRS;
	}

	@NotNull
	public ColorDescriptor[] getColorDescriptors()
	{
		return new ColorDescriptor[0];
	}

	@NotNull
	public SyntaxHighlighter getHighlighter()
	{
		return new PhpFileSyntaxHighlighter();
	}

	@NonNls
	@NotNull
	public String getDemoText()
	{
		return DEMO_TEXT;
	}

	@Nullable
	public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap()
	{
		return null;
	}
}
