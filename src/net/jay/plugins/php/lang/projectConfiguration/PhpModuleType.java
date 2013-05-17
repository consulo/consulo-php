package net.jay.plugins.php.lang.projectConfiguration;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.ProjectWizardStepFactory;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ComboboxWithBrowseButton;
import net.jay.plugins.php.PHPBundle;
import net.jay.plugins.php.PHPIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Maxim
 */
public class PhpModuleType extends ModuleType<PhpModuleBuilder> implements ApplicationComponent {
  public PhpModuleType() {
    super("PHP");
  }

  public PhpModuleBuilder createModuleBuilder() {
    return new PhpModuleBuilder();
  }

  public String getName() {
    return PHPBundle.message("module.type.php.name");
  }

  public String getDescription() {
    return PHPBundle.message("module.type.php.description");
  }

  public Icon getBigIcon() {
    return PHPIcons.PHP_LARGE_ICON;
  }

  public Icon getNodeIcon(boolean isOpened) {
    return PHPIcons.PHP_ICON;
  }

  @Override
  public ModuleWizardStep[] createWizardSteps(WizardContext wizardContext, PhpModuleBuilder moduleBuilder, ModulesProvider modulesProvider) {
    Sdk[] sdks = PhpSdkType.getInstance().getSdks();
    final ModuleWizardStep chooseSourceFolder = ProjectWizardStepFactory.getInstance().createSourcePathsStep(wizardContext, moduleBuilder, null, null);

    if (sdks.length == 0) {
      return new ModuleWizardStep[] {
        chooseSourceFolder,
        new ChoosePHPSdkStep(moduleBuilder, wizardContext)
      };
    }

    moduleBuilder.setSdk(sdks[0]);
    return new ModuleWizardStep[] {
      chooseSourceFolder
    };
  }

  @NonNls
  @NotNull
  public String getComponentName() {
    return "PhpSupport.ModuleType";
  }

  public void initComponent() {
    ModuleTypeManager.getInstance().registerModuleType(this);
  }

  public void disposeComponent() {
  }

  public static ModuleType getInstance() {
    return ApplicationManager.getApplication().getComponent(PhpModuleType.class);
  }

  private class ChoosePHPSdkStep extends ModuleWizardStep {
    private final PhpModuleBuilder myModuleBuilder;
    @SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
    private final WizardContext myWizardContext;
    private ComboboxWithBrowseButton myChoosePhpSdkCombo;
    private JPanel myPanel;

    public ChoosePHPSdkStep(PhpModuleBuilder moduleBuilder, WizardContext wizardContext) {
      myModuleBuilder = moduleBuilder;
      myWizardContext = wizardContext;
      myChoosePhpSdkCombo.getComboBox().setEditable(true);
      myChoosePhpSdkCombo.addBrowseFolderListener(
        PHPBundle.message("choose.php.sdk.path"),
        null,
        null,
        new FileChooserDescriptor(false, true, false, false,false, false) {
          @Override
          public boolean isFileSelectable(VirtualFile file) {
            if(super.isFileSelectable(file)) {
              return isValidPhpSdkHomeDirectory(file);
            }
            return false;
          }
        },
        TextComponentAccessor.STRING_COMBOBOX_WHOLE_TEXT
      );
    }

    public JComponent getComponent() {
      return myPanel;
    }

    @Override
    public boolean validate() throws ConfigurationException {
      final String s = getSdkPath();
      if (!s.equals("")) {
        VirtualFile file = VfsUtil.findRelativeFile(s, null);

        if (file != null) {

          if (!isValidPhpSdkHomeDirectory(file)) throw new ConfigurationException(PHPBundle.message("invalid.php.sdk.directory.specified"));
          return super.validate();
        }
      }

      int result = Messages.showYesNoDialog(
        PHPBundle.message("continue.to.work.without.sdk.defined"),
        PHPBundle.message("confirm.sdk.version"),
        Messages.getWarningIcon()
      );
      return result == DialogWrapper.OK_EXIT_CODE;
    }

    private String getSdkPath() {
      return (String) myChoosePhpSdkCombo.getComboBox().getEditor().getItem();
    }

    public void updateDataModel() {
      myModuleBuilder.setSdkPath(getSdkPath());
    }

    @SuppressWarnings({"BoundFieldAssignment"})
    @Override
    public void disposeUIResources() {
      myPanel = null;
      myChoosePhpSdkCombo = null;
      super.disposeUIResources();
    }
  }

  public static boolean isValidPhpSdkHomeDirectory(VirtualFile file) {
    boolean correctHome = false;

    for(VirtualFile child : file.getChildren()) {
      if (child.getNameWithoutExtension().equals("php")) {
        if ((SystemInfo.isWindows && "exe".equals(child.getExtension()))
            || (!SystemInfo.isWindows && child.getExtension() == null)
           ) {
          correctHome = true;
          break;
        }
      }
    }
    return correctHome;
  }
}
