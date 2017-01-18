package com.ailk.eaap.o2p.util.file.remote.session;

/**
 * creat by zhuangyq
 * @author 颖勤
 *
 */
public interface SessionFactory<T,F> {
	Session<T,F> getSession();
}
