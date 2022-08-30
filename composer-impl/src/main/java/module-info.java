/**
 * @author VISTALL
 * @since 30-Aug-22
 */
module consulo.php.composer.impl
{
	requires consulo.ide.api;

	requires consulo.php.api;

	requires consulo.javascript.json.javascript.impl;

	exports consulo.php.composer;
	exports consulo.php.composer.icon;
	exports consulo.php.composer.importProvider;
}