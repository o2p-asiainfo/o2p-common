package com.ailk.eaap.o2p.util.file.locking;

import java.io.File;

/**
 * 
 * @author 颖勤
 *
 */
public interface FileLocker {

	/**
	 * Tries to lock the given file and returns <code>true</code> if it was
	 * successful, <code>false</code> otherwise.
     *
     * @param fileToLock   the file that should be locked according to this locker
     */
	boolean lock(File fileToLock);

    /**
     * Checks whether the file passed in can be locked by this locker. This method never changes the locked state.
     *
     * @return true if the file was locked by another locker than this locker
     */
    boolean isLockable(File file);

	/**
	 * Unlocks the given file.
     *
     * @param fileToUnlock  the file that should be unlocked according to this locker
     */
	void unlock(File fileToUnlock);

}