package com.ailk.eaap.o2p.util.file.match.impl;

import org.apache.commons.io.FilenameUtils;

import com.ailk.eaap.o2p.util.file.match.IFilenameMatcher;

public class WildcardFilenameMatcher implements IFilenameMatcher {

	public boolean match(String filename, String exp) {
		return FilenameUtils.wildcardMatch(filename, exp);
	}
}
