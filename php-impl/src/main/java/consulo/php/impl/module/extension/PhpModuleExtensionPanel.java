package consulo.php.impl.module.extension;

import consulo.application.AllIcons;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.module.extension.ModuleExtension;
import consulo.module.extension.ModuleExtensionWithSdk;
import consulo.module.extension.MutableModuleInheritableNamedPointer;
import consulo.module.ui.extension.ModuleExtensionSdkBoxBuilder;
import consulo.php.PhpLanguageLevel;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.SimpleTextAttributes;
import consulo.ui.ex.awt.ColoredListCellRenderer;
import consulo.ui.ex.awt.ComboBox;
import consulo.ui.ex.awt.LabeledComponent;
import consulo.ui.ex.awt.VerticalFlowLayout;

import jakarta.annotation.Nonnull;
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
		versionComboBox.setRenderer(new ColoredListCellRenderer<Object>()
		{
			@Override
			protected void customizeCellRenderer(@Nonnull JList<?> list, Object value, int index, boolean selected, boolean hasFocus)
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
