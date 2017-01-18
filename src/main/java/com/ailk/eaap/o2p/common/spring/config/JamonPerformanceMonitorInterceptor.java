package com.ailk.eaap.o2p.common.spring.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

/**
 * 
 * @author 颖勤
 *
 */
public class JamonPerformanceMonitorInterceptor implements MethodInterceptor{

	//默认为true,表示jamon的monitor默认可用
	private boolean monitorPerformance = true;

	public void setMonitorPerformance(boolean monitorPerformance) {
		this.monitorPerformance = monitorPerformance;
	}

	public boolean isMonitorPerformance() {
		return monitorPerformance;
	}

	public Object invoke(MethodInvocation mi) throws Throwable {
		Monitor monitor = isMonitorPerformance() ? MonitorFactory.start(methodSignature(mi)) : null;
		try {
			return mi.proceed();
		} finally {
			if (isMonitorPerformance())
				monitor.stop();
		}
	}

	/**
	 * 拦截下方法名，原来的spring提供的JamonPerformanceMonitorInterceptor没有方法参数
	 * 这里给补上
	 * @param mi
	 * @return
	 */
	private String methodSignature(MethodInvocation mi) {
		StringBuffer methodSignature = new StringBuffer(ClassUtils.getQualifiedMethodName(mi.getMethod()));
		methodSignature.append("(");
		Class[] paramTypes = mi.getMethod().getParameterTypes();
		for (int i = 0; i < paramTypes.length; i++) {
			if (i > 0)
				methodSignature.append(",");
			methodSignature.append(getShortName(paramTypes[i]));
		}
		methodSignature.append(")");
		return methodSignature.toString();
	}

	private String getShortName(Class clazz) {
		String name = clazz.getName();
		Package p = clazz.getPackage();
		if (p != null)
			name = StringUtils.delete(name, p.getName() + ".");
		return name;
	}

}
