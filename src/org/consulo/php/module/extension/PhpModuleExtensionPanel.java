package org.consulo.php.module.extension;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;

import org.consulo.module.extension.ModuleExtension;
import org.consulo.module.extension.ModuleExtensionProvider;
import org.consulo.module.extension.ModuleExtensionProviderEP;
import org.consulo.module.extension.ModuleExtensionWithSdk;
import org.consulo.module.extension.MutableModuleInheritableNamedPointer;
import org.consulo.module.extension.ui.ModuleExtensionWithSdkPanel;
import org.consulo.php.PhpLanguageLevel;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ColoredListCellRendererWrapper;
import com.intellij.ui.SimpleTextAttributes;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class PhpModuleExtensionPanel extends JPanel
{
	private JPanel myRoot;

	private ModuleExtensionWithSdkPanel myModuleExtensionWithSdkPanel;
	private JComboBox myVersionBox;
	private PhpMutableModuleExtension myMutableModuleExtension;
	private Runnable myRunnable;

	public PhpModuleExtensionPanel(PhpMutableModuleExtension mutableModuleExtension, Runnable runnable)
	{

		myMutableModuleExtension = mutableModuleExtension;
		myRunnable = runnable;

		final MutableModuleInheritableNamedPointer<PhpLanguageLevel> inheritableLanguageLevel = myMutableModuleExtension.getInheritableLanguageLevel();

		final String moduleName = inheritableLanguageLevel.getModuleName();
		if(moduleName != null)
		{
			final Module module = inheritableLanguageLevel.getModule();
			if(module != null)
			{
				myVersionBox.setSelectedItem(module);
			}
			else
			{
				myVersionBox.addItem(moduleName);
			}
		}
		else
		{
			myVersionBox.setSelectedItem(inheritableLanguageLevel.get());
		}

		myVersionBox.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				final Object selectedItem = myVersionBox.getSelectedItem();
				if(selectedItem instanceof Module)
				{
					inheritableLanguageLevel.set(((Module) selectedItem).getName(), null);
				}
				else if(selectedItem instanceof PhpLanguageLevel)
				{
					inheritableLanguageLevel.set(null, ((PhpLanguageLevel) selectedItem).getName());
				}
				else
				{
					inheritableLanguageLevel.set(selectedItem.toString(), null);
				}
			}
		});
	}

	private void createUIComponents()
	{
		myRoot = this;
		myModuleExtensionWithSdkPanel = new ModuleExtensionWithSdkPanel(myMutableModuleExtension, myRunnable);
		myVersionBox = new ComboBox();
		myVersionBox.setRenderer(new ColoredListCellRendererWrapper<Object>()
		{
			@Override
			protected void doCustomize(JList list, Object value, int index, boolean selected, boolean hasFocus)
			{
				if(value instanceof PhpLanguageLevel)
				{
					final PhpLanguageLevel languageLevel = (PhpLanguageLevel) value;
					append(languageLevel.getShortName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
					append(" ");
					append(languageLevel.getDescription(), SimpleTextAttributes.GRAY_ATTRIBUTES);
				}
				else if(value instanceof Module)
				{
					setIcon(AllIcons.Nodes.Module);
					append(((Module) value).getName(), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);

					final PhpModuleExtension extension = ModuleUtilCore.getExtension((Module) value, PhpModuleExtension.class);
					if(extension != null)
					{
						final PhpLanguageLevel languageLevel = extension.getLanguageLevel();
						append("(" + languageLevel.getShortName() + ")", SimpleTextAttributes.GRAY_ATTRIBUTES);
					}
				}
				else if(value instanceof String)
				{
					setIcon(AllIcons.Nodes.Module);
					append((String) value, SimpleTextAttributes.ERROR_BOLD_ATTRIBUTES);
				}
			}
		});

		for(PhpLanguageLevel languageLevel : PhpLanguageLevel.VALUES)
		{
			myVersionBox.addItem(languageLevel);
		}

		insertModuleItems();
	}

	public void insertModuleItems()
	{
		final ModuleExtensionProvider provider = ModuleExtensionProviderEP.findProvider(myMutableModuleExtension.getId());
		if(provider == null)
		{
			return;
		}

		for(Module module : ModuleManager.getInstance(myMutableModuleExtension.getModule().getProject()).getModules())
		{
			// dont add self module
			if(module == myMutableModuleExtension.getModule())
			{
				continue;
			}

			final ModuleExtension extension = ModuleUtilCore.getExtension(module, provider.getImmutableClass());
			if(extension instanceof ModuleExtensionWithSdk)
			{
				final ModuleExtensionWithSdk sdkExtension = (ModuleExtensionWithSdk) extension;
				// recursive depend
				if(sdkExtension.getInheritableSdk().getModule() == myMutableModuleExtension.getModule())
				{
					continue;
				}
				myVersionBox.addItem(sdkExtension.getModule());
			}
		}
	}
}
