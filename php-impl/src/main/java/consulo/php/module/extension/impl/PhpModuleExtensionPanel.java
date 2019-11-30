package consulo.php.module.extension.impl;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.ColoredListCellRendererWrapper;
import com.intellij.ui.SimpleTextAttributes;
import consulo.extension.ui.ModuleExtensionSdkBoxBuilder;
import consulo.module.extension.ModuleExtension;
import consulo.module.extension.ModuleExtensionWithSdk;
import consulo.module.extension.MutableModuleInheritableNamedPointer;
import consulo.php.PhpLanguageLevel;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.ui.annotation.RequiredUIAccess;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class PhpModuleExtensionPanel extends JPanel
{
	@RequiredUIAccess
	public PhpModuleExtensionPanel(PhpMutableModuleExtensionImpl mutableModuleExtension, Runnable runnable)
	{
		super(new VerticalFlowLayout());
		add(ModuleExtensionSdkBoxBuilder.createAndDefine(mutableModuleExtension, runnable).build());

		final JComboBox versionComboBox = new ComboBox();
		add(LabeledComponent.left(versionComboBox, "Version"));
		versionComboBox.setRenderer(new ColoredListCellRendererWrapper<Object>()
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
			versionComboBox.addItem(languageLevel);
		}

		for(Module module : ModuleManager.getInstance(mutableModuleExtension.getModule().getProject()).getModules())
		{
			// dont add self module
			if(module == mutableModuleExtension.getModule())
			{
				continue;
			}

			final ModuleExtension extension = ModuleUtilCore.getExtension(module, mutableModuleExtension.getId());
			if(extension instanceof ModuleExtensionWithSdk)
			{
				final ModuleExtensionWithSdk sdkExtension = (ModuleExtensionWithSdk) extension;
				// recursive depend
				if(sdkExtension.getInheritableSdk().getModule() == mutableModuleExtension.getModule())
				{
					continue;
				}
				versionComboBox.addItem(sdkExtension.getModule());
			}
		}

		final MutableModuleInheritableNamedPointer<PhpLanguageLevel> inheritableLanguageLevel = mutableModuleExtension.getInheritableLanguageLevel();

		final String moduleName = inheritableLanguageLevel.getModuleName();
		if(moduleName != null)
		{
			final Module module = inheritableLanguageLevel.getModule();
			if(module != null)
			{
				versionComboBox.setSelectedItem(module);
			}
			else
			{
				versionComboBox.addItem(moduleName);
			}
		}
		else
		{
			versionComboBox.setSelectedItem(inheritableLanguageLevel.get());
		}

		versionComboBox.addItemListener(e ->
		{
			final Object selectedItem = versionComboBox.getSelectedItem();
			if(selectedItem instanceof Module)
			{
				inheritableLanguageLevel.set(((Module) selectedItem).getName(), null);
			}
			else if(selectedItem instanceof PhpLanguageLevel)
			{
				inheritableLanguageLevel.set(null, ((PhpLanguageLevel) selectedItem).getShortName());
			}
			else
			{
				inheritableLanguageLevel.set(selectedItem.toString(), null);
			}
		});
	}
}
