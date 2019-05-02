package net.jay.plugins.php.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

public class FileScanner {

	/**
	 * @param path    Path to base file (often it`s a directory to scan)
	 * @param pattern Pattern to match files
	 * @return List of files
	 *
	 * @throws java.io.FileNotFoundException if file not found
	 */
	public static List<File> scan(final String path, final String pattern) throws FileNotFoundException {
		List<File> myFiles = new ArrayList<File>();
		File baseFile = new File(path);
		if (!baseFile.exists()) {
			throw new FileNotFoundException("File " + path + " doesn`t exist!");
		}
		scanForFiles(myFiles, baseFile, pattern);
		return myFiles;
	}

	private static void scanForFiles(final List<File> files, final File f, final String pattern) {
// recursively scan for all subdirectories
		if (f.isDirectory()) {
			for (File file : f.listFiles()) {
				if (!file.isDirectory()) {
					String path = file.getAbsolutePath();
					if (!path.contains(".svn") &&
						!path.contains(".cvs") &&
						path.matches(pattern)) {
						files.add(file);
					}
				} else {
					scanForFiles(files, file, pattern);
				}
			}
		}
	}

}