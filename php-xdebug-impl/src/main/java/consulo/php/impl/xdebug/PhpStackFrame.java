package consulo.php.impl.xdebug;

import consulo.execution.debug.XDebuggerUtil;
import consulo.execution.debug.XSourcePosition;
import consulo.execution.debug.evaluation.XDebuggerEvaluator;
import consulo.execution.debug.frame.XCompositeNode;
import consulo.execution.debug.frame.XStackFrame;
import consulo.execution.debug.frame.XValueChildrenList;
import consulo.localize.LocalizeValue;
import consulo.logging.Logger;
import consulo.php.impl.xdebug.connection.DbgpCommandSender;
import consulo.php.impl.xdebug.connection.DbgpResponse;
import consulo.ui.ex.ColoredTextContainer;
import consulo.ui.ex.SimpleTextAttributes;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.w3c.dom.Element;

import java.util.List;

public class PhpStackFrame extends XStackFrame {
    private static final Logger LOG = Logger.getInstance(PhpStackFrame.class);

    private final DbgpCommandSender myCommandSender;
    private final String myFileUri;
    private final int myLine;
    private final String myWhere;
    private final int myLevel;

    public PhpStackFrame(@Nonnull DbgpCommandSender commandSender,
                         @Nonnull String fileUri,
                         int line,
                         @Nullable String where,
                         int level) {
        myCommandSender = commandSender;
        myFileUri = fileUri;
        myLine = line;
        myWhere = where;
        myLevel = level;
    }

    @Nullable
    @Override
    public XSourcePosition getSourcePosition() {
        VirtualFile file = PhpDebugUtil.toVirtualFile(myFileUri, myCommandSender.getPlatform());
        if (file == null) {
            return null;
        }
        // DBGp lines are 1-based, XSourcePosition lines are 0-based
        return XDebuggerUtil.getInstance().createPosition(file, myLine - 1);
    }

    @Override
    public void customizePresentation(@Nonnull ColoredTextContainer component) {
        String display = myWhere != null && !myWhere.isEmpty() ? myWhere : "{main}";
        component.append(display, SimpleTextAttributes.REGULAR_ATTRIBUTES);

        VirtualFile file = PhpDebugUtil.toVirtualFile(myFileUri, myCommandSender.getPlatform());
        if (file != null) {
            component.append(" (" + file.getName() + ":" + myLine + ")", SimpleTextAttributes.GRAYED_ATTRIBUTES);
        }
    }

    @Override
    public void computeChildren(@Nonnull XCompositeNode node) {
        try {
            // get context names for this stack level
            DbgpResponse contextNamesResponse = myCommandSender.contextNames(myLevel).get();
            if (contextNamesResponse.hasError()) {
                node.setErrorMessage("Failed to get contexts: " + contextNamesResponse.getErrorMessage());
                return;
            }

            XValueChildrenList children = new XValueChildrenList();
            List<Element> contexts = contextNamesResponse.getContextElements();

            for (Element contextElement : contexts) {
                String contextIdStr = contextElement.getAttribute("id");
                String contextName = contextElement.getAttribute("name");
                int contextId = Integer.parseInt(contextIdStr);

                DbgpResponse contextResponse = myCommandSender.contextGet(myLevel, contextId).get();
                if (contextResponse.hasError()) {
                    continue;
                }

                List<Element> properties = contextResponse.getPropertyElements();
                // For "Locals" (context 0), add properties directly
                // For other contexts, add as a group
                if (contextId == 0) {
                    for (Element prop : properties) {
                        String name = prop.getAttribute("name");
                        children.add(name, new PhpValue(myCommandSender, prop, myLevel, contextId));
                    }
                }
                else if (!properties.isEmpty()) {
                    children.addTopGroup(new PhpValueGroup(
                        LocalizeValue.of(contextName),
                        myCommandSender,
                        properties,
                        myLevel,
                        contextId
                    ));
                }
            }

            node.addChildren(children, true);
        }
        catch (Exception e) {
            LOG.warn("Failed to compute children", e);
            node.setErrorMessage("Failed to load variables: " + e.getMessage());
        }
    }

    @Nullable
    @Override
    public XDebuggerEvaluator getEvaluator() {
        return new PhpDebuggerEvaluator(myCommandSender);
    }

    @Nullable
    @Override
    public Object getEqualityObject() {
        return myFileUri + ":" + myLine + ":" + myLevel;
    }
}
