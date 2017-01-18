package com.ailk.eaap.o2p.util.file.remote.session.sftp;

import java.util.concurrent.atomic.AtomicInteger;

import com.jcraft.jsch.Session;

public class JSchSessionWrapper {
	private final Session session;

	private final AtomicInteger channels = new AtomicInteger();

	public JSchSessionWrapper(Session session) {
		this.session = session;
	}

	public void addChannel() {
		this.channels.incrementAndGet();
	}

	public void close() {
		if (channels.decrementAndGet() <= 0) {
			this.session.disconnect();
		}
	}

	public final Session getSession() {
		return session;
	}

	public boolean isConnected() {
		return session.isConnected();
	}
}
