package net.jay.plugins.php.lang.templates;

import java.io.IOException;
import java.io.InputStream;

import net.jay.plugins.php.lang.PHPFileType;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.template.impl.TemplateSettings;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.impl.FileTemplateImpl;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;

/**
 * @author jay
 * @date Jun 14, 2008 7:02:08 PM
 */
public class PhpTemplatesLoader implements ApplicationComponent
{

	private static final Logger LOG = Logger.getInstance(PhpTemplatesLoader.class.getName());
	private static final String TEMPLATES_FILE = "templates.xml";

	private FileTemplateManager templateManager;
	private TemplateSettings settings;

	public PhpTemplatesLoader(final FileTemplateManager fileTemplateManager, final TemplateSettings templateSettings)
	{
		templateManager = fileTemplateManager;
		settings = templateSettings;
	}

	public void initComponent()
	{
		loadFileTemplates();
	}

	public void disposeComponent()
	{
	}

	@NotNull
	public String getComponentName()
	{
		return "PhpSupport.PhpTemplatesLoader";
	}

	private static final String TYPE_PATTERN = "pattern";
	private static final String TYPE_TEMPLATE = "template";

	private void registerPattern(final String name, final String text)
	{
		FileTemplate pattern = templateManager.getPattern(name);
		if(pattern != null)
		{
			return;
		}

		pattern = templateManager.addPattern(name, PHPFileType.DEFAULT_EXTENSION);
		((FileTemplateImpl) pattern).setInternal(true);
		pattern.setText(text);
	}

	private void registerTemplate(final String name, final String text)
	{
		FileTemplate pattern = templateManager.getTemplate(name);
		if(pattern != null)
		{
			return;
		}

		pattern = templateManager.addTemplate(name, PHPFileType.DEFAULT_EXTENSION);
		((FileTemplateImpl) pattern).setInternal(true);
		pattern.setText(text);
	}

	private void loadFileTemplates()
	{
		final InputStream inputStream = PhpTemplatesLoader.class.getResourceAsStream(TEMPLATES_FILE);
		if(inputStream != null)
		{
			final SAXBuilder parser = new SAXBuilder();
			try
			{
				final Document doc = parser.build(inputStream);
				final Element root = doc.getRootElement();
				for(Object o : root.getChildren())
				{
					if(o instanceof Element)
					{
						Element e = (Element) o;
						final String name = e.getAttributeValue("name");
						final String type = e.getAttributeValue("type");
						final String text = e.getText();
						if(type.equals(TYPE_PATTERN))
						{
							registerPattern(name, text);
						}
						if(type.equals(TYPE_TEMPLATE))
						{
							registerTemplate(name, text);
						}
					}
				}
				inputStream.close();
			}
			catch(JDOMException e)
			{
				LOG.warn(e);
			}
			catch(IOException e)
			{
				LOG.warn(e);
			}
		}
		else
		{
			LOG.warn("File " + TEMPLATES_FILE + " wasn't found");
		}
	}
}
