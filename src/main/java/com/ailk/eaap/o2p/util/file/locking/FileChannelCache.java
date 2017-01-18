package com.ailk.eaap.o2p.util.file.locking;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Static cache of FileLocks that can be used to ensure that only a single lock is used inside this ClassLoader.
 *
 * @author Iwein Fuld
 * @since 2.0
 */
public class FileChannelCache {
	private final static Log LOG = LogFactory.getLog(FileChannelCache.class);
	 private static ConcurrentMap<File, FileChannel> channelCache = new ConcurrentHashMap<File, FileChannel>();


	    /**
	     * Try to get a lock for this file while guaranteeing that the same channel will be used for all file locks in this
	     * VM. If the lock could not be acquired this method will return <code>null</code>.
	     * <p>
	     * Locks acquired through this method should be passed back to #closeChannelFor to prevent memory leaks.
	     * <p>
	     * Thread safe.
	     */
	 	public FileChannelCache(){
	 		
	 	}
	    public static FileLock tryLockFor(File fileToLock) throws IOException {
	        FileChannel channel = channelCache.get(fileToLock);
	        if (channel == null) {
	            FileChannel newChannel = new RandomAccessFile(fileToLock, "rw").getChannel();
	            FileChannel original = channelCache.putIfAbsent(fileToLock, newChannel);
	            channel = (original != null) ? original : newChannel;
	        }
	        FileLock lock = null;
	        if (channel != null) {
	            try {
	                lock = channel.tryLock();
	            }
	            catch (OverlappingFileLockException e) {
	                // File is already locked in this thread or virtual machine
	            	LOG.warn("File is already locked in this thread or virtual machine",e);
	            }
	        }
	        return lock;
	    }

	    /**
	     * Close the channel for the file passed in.
	     * <p>
	     * Thread safe.
	     */
	    public static void closeChannelFor(File fileToUnlock) {
	        FileChannel fileChannel = channelCache.remove(fileToUnlock);
	        if (fileChannel != null) {
	        	try {
					fileChannel.close();
				}
	        	catch (IOException e) {
					LOG.warn("closeChannelFor occur exception",e);
				}
	        }
	    }

	    public static boolean isLocked(File file) {
	        return channelCache.containsKey(file);
	    }
}
