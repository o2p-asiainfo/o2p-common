package com.ailk.eaap.o2p.common.util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;

/**
 * 
 * @author zhuangyq
 * @version v0.1
 */
public final class JsonPathUtils {
	public static <T> T evaluate(Object json, String jsonPath, Filter<?>... filters) throws Exception {
		if (json instanceof String) {
			return JsonPath.read((String) json, jsonPath, filters);
		}
		else if (json instanceof File) {
			return JsonPath.read((File) json, jsonPath, filters);
		}
		else if (json instanceof URL) {
			return JsonPath.read((URL) json, jsonPath, filters);
		}
		else if (json instanceof InputStream) {
			return JsonPath.read((InputStream) json, jsonPath, filters);
		}
		else {
			return JsonPath.read(json, jsonPath, filters);
		}

	}

	private JsonPathUtils() {
	}
}
