package consulo.php.impl.xdebug;

import consulo.execution.debug.frame.*;
import consulo.logging.Logger;
import consulo.php.impl.xdebug.connection.DbgpCommandSender;
import consulo.php.impl.xdebug.connection.DbgpResponse;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class PhpValue extends XNamedValue {
    private static final Logger LOG = Logger.getInstance(PhpValue.class);

    private final DbgpCommandSender myCommandSender;
    private final Element myPropertyElement;
    private final int myStackLevel;
    private final int myContextId;

    public PhpValue(@Nonnull DbgpCommandSender commandSender,
                    @Nonnull Element propertyElement,
                    int stackLevel,
                    int contextId) {
        super(getDisplayName(propertyElement));
        myCommandSender = commandSender;
        myPropertyElement = propertyElement;
        myStackLevel = stackLevel;
        myContextId = contextId;
    }

    @Nonnull
    private static String getDisplayName(@Nonnull Element element) {
        String name = element.getAttribute("name");
        return name != null && !name.isEmpty() ? name : "?";
    }

    @Override
    public void computePresentation(@Nonnull XValueNode node, @Nonnull XValuePlace place) {
        String type = myPropertyElement.getAttribute("type");
        String value = computeValueString();
        boolean hasChildren = "1".equals(myPropertyElement.getAttribute("children"));

        node.setPresentation(null, type, value, hasChildren);
    }

    @Nonnull
    private String computeValueString() {
        String type = myPropertyElement.getAttribute("type");

        if ("array".equals(type)) {
            String numChildren = myPropertyElement.getAttribute("numchildren");
            return "array(" + (numChildren != null ? numChildren : "?") + ")";
        }

        if ("object".equals(type)) {
            String className = myPropertyElement.getAttribute("classname");
            return className != null && !className.isEmpty() ? className : "object";
        }

        if ("null".equals(type)) {
            return "null";
        }

        if ("uninitialized".equals(type)) {
            return "uninitialized";
        }

        String encoding = myPropertyElement.getAttribute("encoding");
        String textContent = getDirectTextContent(myPropertyElement);

        if ("base64".equals(encoding) && textContent != null) {
            String decoded = PhpDebugUtil.decodeBase64(textContent);
            if ("string".equals(type) && decoded != null) {
                return "\"" + decoded + "\"";
            }
            return decoded != null ? decoded : "";
        }

        if (textContent != null) {
            if ("string".equals(type)) {
                return "\"" + textContent + "\"";
            }
            return textContent;
        }

        return "";
    }

    @Nullable
    private static String getDirectTextContent(@Nonnull Element element) {
        NodeList children = element.getChildNodes();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.TEXT_NODE ||
                children.item(i).getNodeType() == Node.CDATA_SECTION_NODE) {
                text.append(children.item(i).getNodeValue());
            }
        }
        String result = text.toString().trim();
        return result.isEmpty() ? null : result;
    }

    @Override
    public void computeChildren(@Nonnull XCompositeNode node) {
        boolean hasChildren = "1".equals(myPropertyElement.getAttribute("children"));
        if (!hasChildren) {
            super.computeChildren(node);
            return;
        }

        // check if children are already inline in the element
        List<Element> inlineChildren = getChildPropertyElements(myPropertyElement);
        if (!inlineChildren.isEmpty()) {
            addChildProperties(node, inlineChildren);
            return;
        }

        // fetch children via property_get
        String fullname = myPropertyElement.getAttribute("fullname");
        if (fullname == null || fullname.isEmpty()) {
            node.setErrorMessage("Cannot expand: no fullname attribute");
            return;
        }

        try {
            DbgpResponse response = myCommandSender.propertyGet(fullname, myStackLevel, myContextId).get();
            if (response.hasError()) {
                node.setErrorMessage("Error: " + response.getErrorMessage());
                return;
            }

            Element prop = response.getFirstChildElement("property");
            if (prop != null) {
                List<Element> children = getChildPropertyElements(prop);
                addChildProperties(node, children);
            }
            else {
                node.addChildren(XValueChildrenList.EMPTY, true);
            }
        }
        catch (Exception e) {
            LOG.warn("Failed to get property children", e);
            node.setErrorMessage("Failed to load: " + e.getMessage());
        }
    }

    private void addChildProperties(@Nonnull XCompositeNode node, @Nonnull List<Element> elements) {
        XValueChildrenList children = new XValueChildrenList();
        for (Element child : elements) {
            String name = child.getAttribute("name");
            children.add(name != null ? name : "?", new PhpValue(myCommandSender, child, myStackLevel, myContextId));
        }
        node.addChildren(children, true);
    }

    @Nonnull
    private static List<Element> getChildPropertyElements(@Nonnull Element parent) {
        List<Element> result = new ArrayList<>();
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element child) {
                if ("property".equals(child.getLocalName()) || "property".equals(child.getTagName())) {
                    result.add(child);
                }
            }
        }
        return result;
    }

    @Nullable
    @Override
    public XValueModifier getModifier() {
        String fullname = myPropertyElement.getAttribute("fullname");
        if (fullname == null || fullname.isEmpty()) {
            return null;
        }
        return new PhpValueModifier(myCommandSender, fullname, myStackLevel);
    }
}
