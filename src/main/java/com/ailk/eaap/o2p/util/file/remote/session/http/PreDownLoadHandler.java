package com.ailk.eaap.o2p.util.file.remote.session.http;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.StaticScriptSource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import bsh.Interpreter;

public class PreDownLoadHandler {
	private final static Log logger = LogFactory.getLog(PreDownLoadHandler.class);
	private String url;
	private String downloadPath;
	private String dynScript;
	public PreDownLoadHandler(String url,String downloadPath,String dynScript){
		this.url= url;
		this.downloadPath = downloadPath;
		this.dynScript = dynScript;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDownloadPath() {
		return downloadPath;
	}
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
	public String getDynScript() {
		return dynScript;
	}
	public void setDynScript(String dynScript) {
		this.dynScript = dynScript;
	}
	public String downloadRuleExecute(){
		if(!StringUtils.hasText(dynScript)){
				dynScript ="import java.util.Date;" +
						"import java.text.SimpleDateFormat;" +
						" SimpleDateFormat sdf = new SimpleDateFormat(\"yyyy-MM-dd\");" +
						" String dateStr = sdf.format(new Date());" +
						" StringBuffer fullUrl =  new StringBuffer();" +
						" fullUrl.append(url)." +
						" append(\"?SelectDate=\").append(dateStr).append(\"&reportType=CVSDR&tsvflag=Y\");" +
						" return fullUrl.toString();";
		}
		ScriptSource scriptSource = new StaticScriptSource(dynScript);
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("url", url);
		variables.put("downloadPath", downloadPath);
		Object ret = executeScript(scriptSource,variables );
		return ret!=null?ret.toString():"";
	}
	private Object executeScript(ScriptSource scriptSource,
			Map<String, Object> variables) {
		Object result = null;
		try {
			String script = scriptSource.getScriptAsString();
			Assert.hasText(script, "Script source must not be empty");
			Interpreter interpreter = new Interpreter();
			Date start = new Date();
			if (logger.isDebugEnabled()) {
				logger.debug("executing script: " + script);
			}
			if (!CollectionUtils.isEmpty(variables)) {
				for (Map.Entry<String, Object> entry : variables.entrySet()) {
					interpreter.set(entry.getKey(), entry.getValue());
				}
				result = interpreter.eval(script);
			}
			else {
				result = interpreter.eval(script);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("script executed in " + (new Date().getTime() - start.getTime()) + " ms");
			}
		}
		catch (Exception e) {
			throw new RuntimeException("bean shell execute exception,"+e.getMessage(), e);
		}
		if(result!=null&& result instanceof Map){
			return result;
		}
		return result;
	}
}
