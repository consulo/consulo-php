package net.jay.plugins.php.utils;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLock;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.SourceTreeToPsiMap;
import com.intellij.psi.impl.source.parsing.ChameleonTransforming;
import com.intellij.psi.impl.source.tree.*;
import com.intellij.util.ArrayUtil;
import com.intellij.util.CharTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 27.02.2007
 *
 * @author jay
 */
public class DebugUtil {

    private static final Logger LOG = Logger.getInstance(DebugUtil.class.getName());

    final public static /*final*/ boolean CHECK = false;
    public static final boolean CHECK_INSIDE_ATOMIC_ACTION_ENABLED = true;

    public static String psiTreeToString(PsiElement element, boolean
            skipWhitespaces) {
        return treeToString(SourceTreeToPsiMap.psiElementToTree(element),
                skipWhitespaces);
    }

    public static String treeToString(ASTNode root, boolean skipWhitespaces) {
        StringBuffer buffer = new StringBuffer();
        treeToBuffer(buffer, root, 0, skipWhitespaces, false, false, false);
        return buffer.toString();
    }

    public static String treeToString(ASTNode root, boolean
            skipWhitespaces, boolean showRanges) {
        StringBuffer buffer = new StringBuffer();
        treeToBuffer(buffer, root, 0, skipWhitespaces, showRanges, false, false);
        return buffer.toString();
    }


    public static String treeToStringWithUserData(TreeElement root,
                                                  boolean skipWhitespaces) {
        StringBuffer buffer = new StringBuffer();
        treeToBufferWithUserData(buffer, root, 0, skipWhitespaces);
        return buffer.toString();
    }

    public static String treeToStringWithUserData(PsiElement root,
                                                  boolean skipWhitespaces) {
        StringBuffer buffer = new StringBuffer();
        treeToBufferWithUserData(buffer, root, 0, skipWhitespaces);
        return buffer.toString();
    }

    public static void treeToBuffer(StringBuffer buffer,
                                    ASTNode root,
                                    int indent,
                                    boolean skipWhiteSpaces,
                                    boolean showRanges,
                                    final boolean showChildrenRanges,
                                    boolean tokenTypesOnly) {
        if (skipWhiteSpaces && root.getElementType() ==
                ElementType.WHITE_SPACE) return;

        for (int i = 0; i < indent; i++) {
            buffer.append('\t');
        }
        if (root instanceof CompositeElement) {
            final PsiElement psiElement = root.getPsi();
            if (!tokenTypesOnly) {
                if (psiElement != null) {
                    buffer.append(psiElement.toString());
                } else {
                    buffer.append(root.getElementType().toString());
                }
            }
        } else {
            String text = root.getText();
            text = StringUtil.replace(text, "\n", "\\n");
            text = StringUtil.replace(text, "\r", "\\r");
            text = StringUtil.replace(text, "\t", "\\t");
            if (tokenTypesOnly) {
                buffer.append(root.getElementType().toString());
            } else {
                buffer.append(root.toString());
            }
            buffer.append("('");
            buffer.append(text);
            buffer.append("')");
        }
        if (showRanges) buffer.append(root.getTextRange());
        buffer.append("\n");
        if (root instanceof CompositeElement) {
            ChameleonTransforming.transformChildren(root);
            ASTNode child = root.getFirstChildNode();

            if (child == null) {
                for (int i = 0; i < indent + 1; i++) {
                    buffer.append('\t');
                }
                buffer.append("<empty list>\n");
            } else {
                while (child != null) {
                    int newIndent = indent + 1;
                    if (tokenTypesOnly) {
                        newIndent = 0;
                    }
                    treeToBuffer(buffer, child, newIndent, skipWhiteSpaces,
                            showChildrenRanges, showChildrenRanges, tokenTypesOnly);
                    child = child.getTreeNext();
                }
            }
        }
    }

    private static void treeToBufferWithUserData(StringBuffer buffer,
                                                 TreeElement root, int indent, boolean skipWhiteSpaces) {
        if (skipWhiteSpaces && root.getElementType() ==
                ElementType.WHITE_SPACE) return;

        for (int i = 0; i < indent; i++) {
            buffer.append(' ');
        }
        if (root instanceof CompositeElement) {
            buffer.append(SourceTreeToPsiMap.treeElementToPsi(root).toString());
        } else {
            String text = root.getText();
            text = StringUtil.replace(text, "\n", "\\n");
            text = StringUtil.replace(text, "\r", "\\r");
            text = StringUtil.replace(text, "\t", "\\t");
            buffer.append(root.toString());
            buffer.append("('");
            buffer.append(text);
            buffer.append("')");
        }
        buffer.append(root.getUserDataString());
        buffer.append("\n");
        if (root instanceof CompositeElement || ((LeafElement) root).isChameleon()) {
            PsiElement[] children =
                    SourceTreeToPsiMap.treeElementToPsi(root).getChildren();

            for (PsiElement child : children) {
                treeToBufferWithUserData(buffer,
                        (TreeElement) SourceTreeToPsiMap.psiElementToTree(child), indent + 2,
                        skipWhiteSpaces);
            }

            if (children.length == 0) {
                for (int i = 0; i < indent + 2; i++) {
                    buffer.append(' ');
                }
                buffer.append("<empty list>\n");
            }
        }
    }

    private static void treeToBufferWithUserData(StringBuffer buffer,
                                                 PsiElement root, int indent, boolean skipWhiteSpaces) {
        if (skipWhiteSpaces && root instanceof PsiWhiteSpace) return;

        for (int i = 0; i < indent; i++) {
            buffer.append(' ');
        }
        if (root instanceof CompositeElement) {
            buffer.append(root);
        } else {
            String text = root.getText();
            text = StringUtil.replace(text, "\n", "\\n");
            text = StringUtil.replace(text, "\r", "\\r");
            text = StringUtil.replace(text, "\t", "\\t");
            buffer.append(root.toString());
            buffer.append("('");
            buffer.append(text);
            buffer.append("')");
        }
        buffer.append(((UserDataHolderBase) root).getUserDataString());
        buffer.append("\n");

        PsiElement[] children = root.getChildren();

        for (PsiElement child : children) {
            treeToBufferWithUserData(buffer, child, indent + 2, skipWhiteSpaces);
        }

        if (children.length == 0) {
            for (int i = 0; i < indent + 2; i++) {
                buffer.append(' ');
            }
            buffer.append("<empty list>\n");
        }

    }

    public static void checkTreeStructure(ASTNode anyElement) {
        ASTNode root = anyElement;
        while (root.getTreeParent() != null) {
            root = root.getTreeParent();
        }
        if (root instanceof CompositeElement) {
            synchronized (PsiLock.LOCK) {
                checkSubtree(root);
            }
        }
    }

    private static void checkSubtree(ASTNode root) {
        if (root.getFirstChildNode() == null) {
            if (root.getLastChildNode() != null) {
                throw new IncorrectTreeStructureException(root, "firstChild == null, but lastChild != null");
            }
        } else {
            for (ASTNode child = root.getFirstChildNode(); child != null;
                 child = child.getTreeNext()) {
                if (child instanceof CompositeElement) {
                    checkSubtree(child);
                }
                if (child.getTreeParent() != root) {
                    throw new IncorrectTreeStructureException(child, "child has wrong parent value");
                }
                if (child == root.getFirstChildNode()) {
                    if (child.getTreePrev() != null) {
                        throw new IncorrectTreeStructureException(root,
                                "firstChild.prev != null");
                    }
                } else {
                    if (child.getTreePrev() == null) {
                        throw new IncorrectTreeStructureException(child, "not first child has prev == null");
                    }
                    if (child.getTreePrev().getTreeNext() != child) {
                        throw new IncorrectTreeStructureException(child,
                                "element.prev.next != element");
                    }
                }
                if (child.getTreeNext() == null) {
                    if (root.getLastChildNode() != child) {
                        throw new IncorrectTreeStructureException(child, "not last child has next == null");
                    }
                }
            }
        }
    }

    public static void checkParentChildConsistent(ASTNode element) {
        ASTNode treeParent = element.getTreeParent();
        if (treeParent == null) return;
        ASTNode[] elements = treeParent.getChildren(null);
        if (ArrayUtil.find(elements, element) == -1) {
            throw new IncorrectTreeStructureException(element, "child cannot be found among parents children");
        }
        //LOG.debug("checked consistence: "+System.identityHashCode(element));
    }

    public static void checkSameCharTabs(ASTNode element1, ASTNode element2) {
        final CharTable fromCharTab = SharedImplUtil.findCharTableByTree(element1);
        final CharTable toCharTab = SharedImplUtil.findCharTableByTree(element2);
        LOG.assertTrue(fromCharTab == toCharTab);
    }

    public static String psiToString(final PsiElement file, final boolean skipWhitespaces) {
        return psiToString(file, skipWhitespaces, false);
    }

    public static String psiToString(final PsiElement file, final boolean skipWhitespaces, final boolean tokenTypesOnly) {
        final StringBuffer stringBuffer = new
                StringBuffer(file.getTextLength() * 5);
        if (file.getNode() == null) {
            psiToBuffer(stringBuffer, file, 0, skipWhitespaces, false, false);
        } else {
            treeToBuffer(stringBuffer, file.getNode(), 0, skipWhitespaces,
                    false, false, tokenTypesOnly);
        }
        return stringBuffer.toString();
    }

    public static void psiToBuffer(StringBuffer buffer,
                                   PsiElement root,
                                   int indent,
                                   boolean skipWhiteSpaces,
                                   boolean showRanges,
                                   final boolean showChildrenRanges) {
        if (skipWhiteSpaces && root instanceof PsiWhiteSpace) return;

        for (int i = 0; i < indent; i++) {
            buffer.append(' ');
        }
        final String rootStr = root.toString();
        buffer.append(rootStr);
        PsiElement child = root.getFirstChild();
        if (child == null) {
            String text = root.getText();
            text = StringUtil.replace(text, "\n", "\\n");
            text = StringUtil.replace(text, "\r", "\\r");
            text = StringUtil.replace(text, "\t", "\\t");
            buffer.append("('");
            buffer.append(text);
            buffer.append("')");
        }

        if (showRanges) buffer.append(root.getTextRange());
        buffer.append("\n");
        while (child != null) {
            psiToBuffer(buffer, child, indent + 2, skipWhiteSpaces,
                    showChildrenRanges, showChildrenRanges);
            child = child.getNextSibling();
        }
    }

    public static String tokensToString(PsiFile file, boolean b) {
        List<String> tokenList = new ArrayList<String>();
        treeToTokenList(tokenList, file.getNode());
        StringBuffer buf = new StringBuffer();
        for (String s : tokenList) {
            buf.append(s).append("\n");
        }
        return buf.toString();
    }

    private static void treeToTokenList(List<String> tokenList, ASTNode node) {
        if (node instanceof CompositeElement) {
            ASTNode child = node.getFirstChildNode();
            while (child != null) {
                if (!(child instanceof PsiWhiteSpace)) {
                    treeToTokenList(tokenList, child);
                }
                child = child.getTreeNext();
            }
        } else {
            StringBuffer buffer = new StringBuffer();
            String text = node.getText();
            text = StringUtil.replace(text, "\n", "\\n");
            text = StringUtil.replace(text, "\r", "\\r");
            text = StringUtil.replace(text, "\t", "\\t");
            buffer.append(node.getElementType().toString());
            buffer.append("('");
            buffer.append(text);
            buffer.append("')");
            tokenList.add(buffer.toString());
        }
    }


    public static class IncorrectTreeStructureException extends RuntimeException {

        private final ASTNode myElement;

        public IncorrectTreeStructureException(ASTNode element, String message) {
            super(message);
            myElement = element;
        }

        public ASTNode getElement() {
            return myElement;
        }
    }
}
