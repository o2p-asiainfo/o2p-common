package com.ailk.eaap.o2p.util.file.remote.session;

public interface SharedSessionCapable {
	/**
	 * @return true if this factory uses a shared session.
	 */
	public abstract boolean isSharedSession();

	/**
	 * Resets the shared session so the next {@code #getSession()} will return a session
	 * using a new connection.
	 */
	public abstract void resetSharedSession();
}
