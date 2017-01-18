package com.ailk.eaap.o2p.common.security;

import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.Logger;
import com.safenetinc.luna.LunaSlotManager;

public class HSM_Manager {
    public static LunaSlotManager slotManager = null;
    private final static Logger logger = Logger.getLog(HSM_Manager.class);
    
    public static void hsmLogin(String password) throws Exception{
        slotManager = LunaSlotManager.getInstance();
        try {
        	slotManager.login(password);
        } catch (Exception e) {
        	logger.error("login error and try to reconnect...,message is:{0}", e.getMessage());
        	boolean reconnect = reconnect(password);
        	if(!reconnect) throw new BusinessException(9083, "9283.SECURITY.LOGIN.ERROR", e);
        }
    }
    
    public static boolean isLoginToHSM() {
    	return slotManager.isLoggedIn();
    }
    
    public static void hsmLogout() {
		slotManager.logout();
    }
    
    public static boolean reconnect(String password) {
        try {
        	reconnectHsmServer(password);
        } catch (Exception e) {
            logger.error("reconnect error..., message is:{0}", e.getMessage());
            return false;
        }
        
        
        return true;
    }

    private static void reconnectHsmServer(String password) {
        synchronized (LunaSlotManager.class) {
        	slotManager = LunaSlotManager.getInstance();
        	try {
        		slotManager.reinitialize();
        	} catch(Exception e) {
        		slotManager.login(password);
        		return;
        	}
        	slotManager.login(password);
        }
    }
}
