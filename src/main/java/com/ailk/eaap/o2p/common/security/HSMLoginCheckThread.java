package com.ailk.eaap.o2p.common.security;

import com.asiainfo.foundation.log.Logger;

public class HSMLoginCheckThread implements Runnable{
	private final static Logger logger = Logger.getLog(HSMLoginCheckThread.class);
	private String password;
	public HSMLoginCheckThread(String password) {
		this.password = password;
	}

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(90000);
				boolean isLogin = false;
				try {
					isLogin = HSM_Manager.isLoginToHSM();
				}catch(Exception e) {
					isLogin = false;
				}
				if(!isLogin) {
					boolean successConnect = HSM_Manager.reconnect(password);
					if(!successConnect) continue;
				} else {
					logger.debug("connect alife...");
				}
			} catch (Exception e) {
				logger.error("reconnect error..., message is:{0}", e.getMessage());
			}
		}
	}
}
