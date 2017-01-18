package com.ailk.eaap.o2p.util.file.match.impl;

import com.ailk.eaap.o2p.util.file.match.IFilenameMatcher;

public class RegexpFilenameMatcher implements IFilenameMatcher {

	public boolean match(String filename, String exp) {
		if(filename == null || exp == null) return false;
		return filename.matches(exp);
	}
}
