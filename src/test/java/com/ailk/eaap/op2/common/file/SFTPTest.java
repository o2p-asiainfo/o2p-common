package com.ailk.eaap.op2.common.file;

import static org.mockito.Mockito.*;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.ailk.eaap.o2p.util.file.remote.session.Session;
import com.ailk.eaap.o2p.util.file.remote.session.sftp.DefaultSftpSessionFactory;
import com.ailk.eaap.o2p.util.file.remote.session.sftp.JSchSessionWrapper;
import com.ailk.eaap.o2p.util.file.remote.session.sftp.SftpSession;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;

/**
 * 
 * @author 颖勤
 *
 */
public class SFTPTest {

	@Test
	public void getSessionTest() throws IOException{
		DefaultSftpSessionFactory sftpFac = mock(DefaultSftpSessionFactory.class);
		JSchSessionWrapper wrapper = mock(JSchSessionWrapper.class);
		when(sftpFac.getSession()).thenReturn(new SftpSession(wrapper ));
		Session session = sftpFac.getSession();
		Assert.assertNotNull(session);
	}
	@Test(expected = IllegalStateException.class)
	public void list() throws IOException{
		DefaultSftpSessionFactory sftpFac = mock(DefaultSftpSessionFactory.class);
		sftpFac.setHost("192.168.1.52");
		Resource privateKey = new FileSystemResource("d:/id_dsa");
		sftpFac.setPrivateKey(privateKey);
		sftpFac.setPrivateKeyPassphrase("");
		sftpFac.setPort(22);
		sftpFac.setUser("hadoop");
		doThrow(new IllegalStateException()).when(sftpFac).getSession();
		Session<LsEntry,ChannelSftp> session = sftpFac.getSession();
		
		String[] fileNames = session.listNames("/home/hadoop/testdir/tmp");
		System.out.println(fileNames.length);
	}
	@Test(expected = IOException.class)
	public void list1() throws IOException{
		DefaultSftpSessionFactory sftpFac = mock(DefaultSftpSessionFactory.class);
		sftpFac.setHost("192.168.1.52");
		Resource privateKey = new FileSystemResource("d:/id_dsa");
		sftpFac.setPrivateKey(privateKey);
		sftpFac.setPrivateKeyPassphrase("");
		sftpFac.setPort(22);
		sftpFac.setUser("hadoop");
		when(sftpFac.getSession()).thenAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Session<LsEntry,ChannelSftp> ss = mock(Session.class);
				return ss;
			}
		});
		Session<LsEntry,ChannelSftp> session = sftpFac.getSession();
		Assert.assertNotNull(session);
		when(session.list("/home")).thenThrow(new IOException("can't find the dirtory /home"));
		session.list("/home");
	}	
}
