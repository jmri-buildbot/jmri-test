/**
 * 
 */
package jmri.jmrit.operations.setup;

import java.io.File;
import java.util.Date;

/**
 * Represents the set of Operations files that is considered a "Backup" of the
 * current Operations files.
 * 
 * It can facilitate the display and selection of backup sets using a GUI.
 * 
 * This class needs tests.......
 * 
 * @author Gregory Madsen Copyright (C) 2012
 * 
 */
public class BackupSet {

	private String _setName;

	public String getSetName() {
		return _setName;
	}

	private Date _lastModifiedDate;
	private File _dir;

	public BackupSet(File dir) {
		_dir = dir;
		_setName = dir.getName();
		_lastModifiedDate = new Date(dir.lastModified());
	}

	public void delete() {
		deleteDirectoryAndFiles(_dir);
	}

	private void deleteDirectoryAndFiles(File dir) {
		// Deletes all of the files in a directory, and then the directory
		// itself.
		// This is NOT a general purpose method, as it only handles directories
		// with only files and no sub directories.
		// This probably needs to handle failures. delete() returns false if it fails.
		for (File f : dir.listFiles()) {
			// Delete files first
			if (f.isFile()) {
				f.delete();
			}
		}

		dir.delete();
	}


	@Override
	public String toString() {
		return String.format("%s (%s)", _setName, _lastModifiedDate);
	}
}
