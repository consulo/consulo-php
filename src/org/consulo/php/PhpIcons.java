package org.consulo.php;

import javax.swing.Icon;

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.LayeredIcon;
import com.intellij.util.Icons;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 22.02.2007
 *
 * @author jay
 */
@Deprecated
public class PhpIcons
{
	public static final Icon FINAL_MARK = IconLoader.findIcon("/nodes/finalMark.png");
	public static final Icon STATIC_MARK = IconLoader.findIcon("/nodes/staticMark.png");

	public static final Icon CLASS = Icons.CLASS_ICON;
	public static final Icon ABSTRACT_CLASS = Icons.ABSTRACT_CLASS_ICON;
	public static final Icon FINAL_CLASS = new LayeredIcon(2);

	static
	{
		((LayeredIcon) FINAL_CLASS).setIcon(CLASS, 0);
		((LayeredIcon) FINAL_CLASS).setIcon(FINAL_MARK, 1);
	}

	public static final Icon INTERFACE = Icons.INTERFACE_ICON;
	public static final Icon VARIABLE = Icons.VARIABLE_ICON;
	public static final Icon PARAMETER = Icons.PARAMETER_ICON;
	public static final Icon METHOD = Icons.METHOD_ICON;
	public static final Icon STATIC_METHOD = new LayeredIcon(2);

	static
	{
		((LayeredIcon) STATIC_METHOD).setIcon(METHOD, 0);
		((LayeredIcon) STATIC_METHOD).setIcon(STATIC_MARK, 1);
	}

	public static final Icon FIELD = Icons.FIELD_ICON;
	public static final Icon STATIC_FIELD = new LayeredIcon(2);

	static
	{
		((LayeredIcon) STATIC_FIELD).setIcon(FIELD, 0);
		((LayeredIcon) STATIC_FIELD).setIcon(STATIC_MARK, 1);
	}

	public static final Icon PUBLIC = Icons.PUBLIC_ICON;
	public static final Icon PROTECTED = Icons.PROTECTED_ICON;
	public static final Icon PRIVATE = Icons.PRIVATE_ICON;

	public static final Icon OVERRIDES = IconLoader.findIcon("/gutter/overridingMethod.png");
	public static final Icon IMPLEMENTS = IconLoader.findIcon("/gutter/implementingMethod.png");
	public static final Icon OVERRIDEN = IconLoader.findIcon("/gutter/overridenMethod.png");
	public static final Icon IMPLEMENTED = IconLoader.findIcon("/gutter/implementedMethod.png");
}
