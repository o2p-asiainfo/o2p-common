package com.ailk.eaap.o2p.util.file.filters;

import java.io.File;

import com.ailk.eaap.o2p.util.file.locking.FileLocker;



public abstract class AbstractFileLockerFilter extends AbstractFileListFilter<File> implements FileLocker{
	
    @Override
    public boolean accept(File file) {
        return this.isLockable(file);
    }
}
