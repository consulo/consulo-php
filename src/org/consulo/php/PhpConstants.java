package org.consulo.php;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public interface PhpConstants
{
	String THIS = "this";
	String SELF = "self";
	String PARENT = "parent";

	String[] SUPER_GLOBALS = new String[]{
			"_GET",
			"_POST",
			"_SERVER",
			"_FILES",
			"_COOKIE",
			"_SESSION",
			"_REQUEST",
			"_ENV",
			"GLOBALS"
	};
}
