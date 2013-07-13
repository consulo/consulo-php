/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.php.internal.core.phar;

import com.intellij.openapi.vfs.ArchiveEntry;
import com.intellij.openapi.vfs.ArchiveFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Holds the Phar Archive
 */
public class PharArchiveFile implements ArchiveFile {

	private PharFile pharFile;
	private File file;
	/**
	 * Cache of phar files, so we don't create phar files representation every
	 * call
	 */
	private static final Map<String, WeakReference<PharFile>> pharFiles = new HashMap<String, WeakReference<PharFile>>();

	public PharArchiveFile(String fileName) throws IOException, PharException {
		this(new File(fileName));
	}

	public PharArchiveFile(File file) throws IOException, PharException {
		this.file = file;
		String key = getFileKey(file);
		if (!pharFiles.containsKey(key)) {
			pharFiles.put(key, new WeakReference<PharFile>(new PharFile(file)));
		}
		final WeakReference<PharFile> weakReference = pharFiles.get(key);
		pharFile = weakReference.get();
		// maybe the phar is collected by gc
		makeSureInit(file);
	}

	private void makeSureInit(File file) throws IOException, PharException {
		if (pharFile == null) {
			String key = getFileKey(file);
			pharFiles.put(key, new WeakReference<PharFile>(new PharFile(file)));
			final WeakReference<PharFile> weakReference = pharFiles.get(key);
			pharFile = weakReference.get();
		}
	}

	private String getFileKey(File file) {
		String key = file.getAbsolutePath() + file.lastModified();
		return key;
	}

	public void close() throws IOException {
		if (pharFile != null)
			pharFile.close();
	}

	@NotNull
	public Iterator<? extends PharEntry> entries() {
		init();
		List<PharEntry> pharEntryList = pharFile.getPharEntryList();

		return pharEntryList.iterator();
	}

	@Override
	public int getSize() {
		init();
		return pharFile.getPharEntryList().size();
	}

	@Nullable
	public PharEntry getEntry(String name) {
		init();
		return pharFile.getEntry(name);
	}

	public InputStream getInputStream(ArchiveEntry entry) throws IOException {
		init();
		if (entry instanceof PharEntry) {
			return pharFile.getInputStream((PharEntry) entry);
		}
		return null;
	}

	private void init() {
		try {
			makeSureInit(file);
		} catch (IOException e) {
		} catch (PharException e) {
		}
	}

	public String getName() {
		return pharFile.getName();
	}

	public int fileSize() {
		return pharFile.getFileNumber();
	}

}