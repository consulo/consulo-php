package net.jay.plugins.php.lang.highlighter.hierarchy;

import com.intellij.codeHighlighting.TextEditorHighlightingPass;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiTreeUtil;
import net.jay.plugins.php.cache.DeclarationsIndex;
import net.jay.plugins.php.cache.psi.LightElementUtil;
import net.jay.plugins.php.cache.psi.LightPhpClass;
import net.jay.plugins.php.cache.psi.LightPhpInterface;
import net.jay.plugins.php.cache.psi.LightPhpMethod;
import net.jay.plugins.php.lang.psi.PHPFile;
import net.jay.plugins.php.lang.psi.elements.Method;
import net.jay.plugins.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date Jun 25, 2008 12:05:40 AM
 */
public class PhpHierarchyHighlightingPass extends TextEditorHighlightingPass {

    private PHPFile file;
    private List<PhpLineMarkerInfo> markers = new ArrayList<PhpLineMarkerInfo>();

    protected PhpHierarchyHighlightingPass(@NotNull Project project,
                                           @NotNull Editor editor,
                                           @NotNull PHPFile file) {
        super(project, editor.getDocument());
        this.file = file;
    }

    private PsiElement[] getElements() {
        return PsiTreeUtil.collectElements(file, new PsiElementFilter() {
            public boolean isAccepted(PsiElement element) {
                return element instanceof PhpClass;
            }
        });
    }

    public void doCollectInformation(ProgressIndicator progress) {
        for (PsiElement element : getElements()) {
            addOverridesGutterIfNeeded(element);
            addOverridenGutterIfNeeded(element);
        }
    }

    private void addOverridenGutterIfNeeded(PsiElement element) {
        if (element instanceof PhpClass) {
            DeclarationsIndex index = DeclarationsIndex.getInstance(element);
            if (index != null) {
                final LightPhpClass phpClass = LightElementUtil.findLightClassByPsi((PhpClass) element);
                final List<LightPhpClass> list = index.getSubclasses(phpClass);
                if (list.size() > 0) {
                    markers.add(new PhpOverridenGutterInfo(element.getTextOffset(),
                            element.getProject(),
                            list,
                            element));
                }
            }
        }
    }

    private void addOverridesGutterIfNeeded(PsiElement element) {
        if (element instanceof Method && element.getParent() instanceof PhpClass) {
            final LightPhpClass klass = LightElementUtil.findLightClassByPsi((PhpClass) element.getParent());
            final String methodName = ((Method) element).getName();
            if (klass != null) {
                LightPhpClass superClass = klass.getSuperClass();
                List<LightPhpMethod> methods = new ArrayList<LightPhpMethod>();
                while (superClass != null) {
                    for (LightPhpMethod method : superClass.getChildrenOfType(LightPhpMethod.class)) {
                        if (method.getName().equals(methodName)) {
                            methods.add(method);
                        }
                    }
                    superClass = superClass.getSuperClass();
                }

                final List<LightPhpInterface> superInterfaces = klass.getSuperInterfaces();
                for (LightPhpInterface superInterface : superInterfaces) {
                    for (LightPhpMethod method : superInterface.getChildrenOfType(LightPhpMethod.class)) {
                        if (method.getName().equals(methodName)) {
                            methods.add(method);
                        }
                    }
                }

                if (methods.size() > 0) {
                    markers.add(new PhpOverridesGutterInfo(element.getTextOffset(),
                            element.getProject(),
                            methods));
                }
            }
        }
    }

    public void doApplyInformationToEditor() {
        PhpGutterUtil.setLineMarkersToEditor(myProject, myDocument, markers);
    }
}
