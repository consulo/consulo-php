package org.consulo.php.vfs;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.vfs.ArchiveFileSystem;
import com.intellij.openapi.vfs.impl.archive.ArchiveHandler;
import com.intellij.util.messages.MessageBus;
import org.consulo.vfs.ArchiveFileSystemBase;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 13.07.13.
 */
public class PharFileSystem extends ArchiveFileSystemBase implements ApplicationComponent {
	public static final String PROTOCOL = "phar";

	public PharFileSystem(MessageBus bus) {
		super(bus);
	}

	@Override
	public ArchiveHandler createHandler(ArchiveFileSystem archiveFileSystem, String s) {
		return new PharHandler(archiveFileSystem, s);
	}

	@Override
	public void setNoCopyJarForPath(String s) {

	}

	@NotNull
	@Override
	public String getProtocol() {
		return PROTOCOL;
	}

	@Override
	public void initComponent() {

	}

	@Override
	public void disposeComponent() {

	}

	@NotNull
	@Override
	public String getComponentName() {
		return getClass().getSimpleName();
	}
}