package com.ailk.eaap.o2p.util.file.remote.session;

public interface DownloadCallBack<T> {
	public void doFile(T client,String fileName, String path);
}
