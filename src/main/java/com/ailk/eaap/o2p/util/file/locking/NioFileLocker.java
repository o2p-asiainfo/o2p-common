package com.ailk.eaap.o2p.util.file.locking;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import com.ailk.eaap.o2p.util.file.FileHandleException;
import com.ailk.eaap.o2p.util.file.filters.AbstractFileLockerFilter;

/**
 * 
 * @author 颖勤
 *
 */
public class NioFileLocker extends AbstractFileLockerFilter{
    private final ConcurrentMap<File, FileLock> lockCache = new ConcurrentHashMap<File, FileLock>();

    /**
     * {@inheritDoc}
     */
    public boolean lock(File fileToLock) {
        FileLock lock = lockCache.get(fileToLock);
        if (lock == null) {
            FileLock newLock = null;
            try {
                newLock = FileChannelCache.tryLockFor(fileToLock);
            } catch (IOException e) {
                throw new FileHandleException("Failed to lock file: "
                        + fileToLock, e);
            }
            if (newLock != null) {
                FileLock original = lockCache.putIfAbsent(fileToLock, newLock);
                lock = original != null ? original : newLock;
            }
        }
        return lock != null;
    }

    public boolean isLockable(File file) {
        return lockCache.containsKey(file) || !FileChannelCache.isLocked(file);
    }

    public void unlock(File fileToUnlock) {
        FileLock fileLock = lockCache.get(fileToUnlock);
        try {
            if (fileLock != null) {
                fileLock.release();
            }
            FileChannelCache.closeChannelFor(fileToUnlock);
        } catch (IOException e) {
            throw new FileHandleException("Failed to unlock file: "
                    + fileToUnlock, e);
        }
	}
}
