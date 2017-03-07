package com.ggi.uparty.util;

import java.util.Comparator;

import com.badlogic.gdx.files.FileHandle;

public class ByDateComparator implements Comparator<FileHandle>{

	@Override
	public int compare(FileHandle o1, FileHandle o2) {
		return (int) (o2.lastModified()-o1.lastModified());
	}

}
