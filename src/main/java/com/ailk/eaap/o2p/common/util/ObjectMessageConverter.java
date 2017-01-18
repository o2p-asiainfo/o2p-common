package com.ailk.eaap.o2p.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import com.asiainfo.foundation.common.ExceptionCommon;
import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.LogModel;
import com.asiainfo.foundation.log.Logger;

/**
 * @ClassName: ObjectMessageConverter
 * @Description:
 * @author zhengpeng
 * @date 2015-3-18 上午11:16:39
 * 
 */
public class ObjectMessageConverter implements MessageConverter {
	
	private Logger log = Logger.getLog(this.getClass());
	
	// 从消息中取出对象
	@Override
	public Object fromMessage(Message message) throws JMSException,
			MessageConversionException {
		Object object = null;
		if (message instanceof ObjectMessage) {
			byte[] obj = (byte[]) ((ObjectMessage) message).getObject();
			ByteArrayInputStream bis = new ByteArrayInputStream(obj);
			try {
				ObjectInputStream ois = new ObjectInputStream(bis);
				object = ois.readObject();
			} catch (Exception e) {
				log.error(LogModel.EVENT_APP_EXCPT, new BusinessException(ExceptionCommon.WebExceptionCode,"ObjectMessageConverter fromMessage exception:" + e.getCause(),null));
			}
		}
		return object;
	}

	// 将对象转换成消息
	@Override
	public Message toMessage(Object object, Session session)
			throws JMSException, MessageConversionException {
		ObjectMessage objectMessage = session.createObjectMessage();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
			byte[] objMessage = bos.toByteArray();
			objectMessage.setObject(objMessage);
		} catch (IOException e) {
			log.error(LogModel.EVENT_APP_EXCPT, new BusinessException(ExceptionCommon.WebExceptionCode,"ObjectMessageConverter toMessage exception:" + e.getCause(),null));
		}
		return objectMessage;
	}

}
