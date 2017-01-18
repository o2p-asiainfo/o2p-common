// ****************************************************************************
// Copyright (c) 2010 SafeNet, Inc. All rights reserved.
//
// All rights reserved.  This file contains information that is
// proprietary to SafeNet, Inc. and may not be distributed
// or copied without written consent from SafeNet, Inc.
// ****************************************************************************
package com.ailk.eaap.o2p.common.security;

import java.security.Provider;
import java.security.Security;

import com.asiainfo.foundation.log.Logger;

public class ProviderList {
	private final static Logger logger = Logger.getLog(ProviderList.class);

	public static void listProviders() {
		Provider[] providers = Security.getProviders();
		logger.info("Provider list");
		for (int i = 0; i < providers.length; i++) {
			logger.info((i + 1) + ":" + providers[i].toString());
		}
	}
}
