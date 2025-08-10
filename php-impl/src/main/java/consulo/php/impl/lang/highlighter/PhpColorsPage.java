package consulo.php.impl.lang.highlighter;

import consulo.annotation.component.ExtensionImpl;
import consulo.colorScheme.setting.AttributesDescriptor;
import consulo.colorScheme.setting.ColorDescriptor;
import consulo.language.editor.colorScheme.setting.ColorSettingsPage;
import consulo.language.editor.highlight.SyntaxHighlighter;
import consulo.localize.LocalizeValue;
import consulo.php.PhpBundle;
import consulo.php.PhpLanguageLevel;
import consulo.php.localize.PhpLocalize;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 13.03.2007
 *
 * @author jay
 */
@ExtensionImpl
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
			new AttributesDescriptor(PhpLocalize.colorSettingsKeyword(), PhpHighlightingData.KEYWORD),
			new AttributesDescriptor(PhpLocalize.colorSettingsComment(), PhpHighlightingData.COMMENT),
			new AttributesDescriptor(PhpLocalize.colorSettingsNumber(), PhpHighlightingData.NUMBER),
			new AttributesDescriptor(PhpLocalize.colorSettingsString(), PhpHighlightingData.STRING),
			new AttributesDescriptor(PhpLocalize.colorSettingsExec_command(), PhpHighlightingData.EXEC_COMMAND),
			new AttributesDescriptor(PhpLocalize.colorSettingsEscape_sequence(), PhpHighlightingData.ESCAPE_SEQUENCE),
			new AttributesDescriptor(PhpLocalize.colorSettingsOperation(), PhpHighlightingData.OPERATION_SIGN),
			new AttributesDescriptor(PhpLocalize.colorSettingsBrackets(), PhpHighlightingData.BRACKETS),
			new AttributesDescriptor(PhpLocalize.colorSettingsPredefinedSymbols(), PhpHighlightingData.PREDEFINED_SYMBOL),
			//new AttributesDescriptor(PhpBundle.message("color.settings.expression_subtitution_marks"), PhpHighlightingData.EXPR_SUBST_MARKS),
			new AttributesDescriptor(PhpLocalize.colorSettingsBad_character(), PhpHighlightingData.BAD_CHARACTER),
			new AttributesDescriptor(PhpLocalize.colorSettingsComma(), PhpHighlightingData.COMMA),
			new AttributesDescriptor(PhpLocalize.colorSettingsSemicolon(), PhpHighlightingData.SEMICOLON),
			new AttributesDescriptor(PhpLocalize.colorSettingsHeredoc_id(), PhpHighlightingData.HEREDOC_ID),
			new AttributesDescriptor(PhpLocalize.colorSettingsHeredoc_content(), PhpHighlightingData.HEREDOC_CONTENT),
			new AttributesDescriptor(PhpLocalize.colorSettingsVar(), PhpHighlightingData.VAR),
			new AttributesDescriptor(PhpLocalize.colorSettingsIdentifier(), PhpHighlightingData.IDENTIFIER),
			new AttributesDescriptor(PhpLocalize.colorSettingsConstant(), PhpHighlightingData.CONSTANT),
			new AttributesDescriptor(PhpLocalize.colorSettingsDoccomment(), PhpHighlightingData.DOC_COMMENT),
			new AttributesDescriptor(PhpLocalize.colorSettingsDoctag(), PhpHighlightingData.DOC_TAG),
			new AttributesDescriptor(PhpLocalize.colorSettingsDocmarkup(), PhpHighlightingData.DOC_MARKUP)
	};

	@Override
	@Nonnull
	public LocalizeValue getDisplayName()
	{
		return PhpLocalize.colorSettingsName();
	}

	@Override
	@Nonnull
	public AttributesDescriptor[] getAttributeDescriptors()
	{
		return ATTRS;
	}

	@Override
	@Nonnull
	public ColorDescriptor[] getColorDescriptors()
	{
		return new ColorDescriptor[0];
	}

	@Override
	@Nonnull
	public SyntaxHighlighter getHighlighter()
	{
		return new PhpFileSyntaxHighlighter(PhpLanguageLevel.HIGHEST);
	}

	@Override
	@NonNls
	@Nonnull
	public String getDemoText()
	{
		return DEMO_TEXT;
	}
}
