package com.ailk.eaap.o2p.util.file.remote.session.ftp;

import org.apache.commons.net.ftp.FTPClient;

public class DefaultFtpSessionFactory extends AbstractFtpSessionFactory<FTPClient>{

	@Override
	protected FTPClient createClientInstance() {
		return new FTPClient();
	}

}
