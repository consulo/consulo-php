package org.consulo.php.lang.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.consulo.php.PhpBundle;
import org.consulo.php.PhpIcons2;
import org.consulo.php.PhpLanguageLevel;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 13.03.2007
 *
 * @author jay
 */
public class PhpColorsPage implements ColorSettingsPage
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
			new AttributesDescriptor(PhpBundle.message("color.settings.keyword"), PHPHighlightingData.KEYWORD),
			new AttributesDescriptor(PhpBundle.message("color.settings.comment"), PHPHighlightingData.COMMENT),
			new AttributesDescriptor(PhpBundle.message("color.settings.number"), PHPHighlightingData.NUMBER),
			new AttributesDescriptor(PhpBundle.message("color.settings.string"), PHPHighlightingData.STRING),
			new AttributesDescriptor(PhpBundle.message("color.settings.exec_command"), PHPHighlightingData.EXEC_COMMAND),
			new AttributesDescriptor(PhpBundle.message("color.settings.escape_sequence"), PHPHighlightingData.ESCAPE_SEQUENCE),
			new AttributesDescriptor(PhpBundle.message("color.settings.operation"), PHPHighlightingData.OPERATION_SIGN),
			new AttributesDescriptor(PhpBundle.message("color.settings.brackets"), PHPHighlightingData.BRACKETS),
			new AttributesDescriptor(PhpBundle.message("color.settings.predefined.symbols"), PHPHighlightingData.PREDEFINED_SYMBOL),
			//new AttributesDescriptor(PhpBundle.message("color.settings.expression_subtitution_marks"), PHPHighlightingData.EXPR_SUBST_MARKS),
			new AttributesDescriptor(PhpBundle.message("color.settings.bad_character"), PHPHighlightingData.BAD_CHARACTER),
			new AttributesDescriptor(PhpBundle.message("color.settings.comma"), PHPHighlightingData.COMMA),
			new AttributesDescriptor(PhpBundle.message("color.settings.semicolon"), PHPHighlightingData.SEMICOLON),
			new AttributesDescriptor(PhpBundle.message("color.settings.heredoc_id"), PHPHighlightingData.HEREDOC_ID),
			new AttributesDescriptor(PhpBundle.message("color.settings.heredoc_content"), PHPHighlightingData.HEREDOC_CONTENT),
			new AttributesDescriptor(PhpBundle.message("color.settings.var"), PHPHighlightingData.VAR),
			new AttributesDescriptor(PhpBundle.message("color.settings.identifier"), PHPHighlightingData.IDENTIFIER),
			new AttributesDescriptor(PhpBundle.message("color.settings.constant"), PHPHighlightingData.CONSTANT),
			new AttributesDescriptor(PhpBundle.message("color.settings.doccomment"), PHPHighlightingData.DOC_COMMENT),
			new AttributesDescriptor(PhpBundle.message("color.settings.doctag"), PHPHighlightingData.DOC_TAG),
			new AttributesDescriptor(PhpBundle.message("color.settings.docmarkup"), PHPHighlightingData.DOC_MARKUP)
	};

	@NotNull
	public String getDisplayName()
	{
		return PhpBundle.message("color.settings.name");
	}

	@Nullable
	public Icon getIcon()
	{
		return PhpIcons2.Php;
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
		return new PhpFileSyntaxHighlighter(PhpLanguageLevel.HIGHEST);
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
