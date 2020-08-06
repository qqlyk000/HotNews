package com.hotnews.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: XianDaLi
 * Date: 2020/8/6 1:16
 * Remark:
 */
public class ViewObject {
	private Map<String,Object> objs = new HashMap<>();
	public void set(String key, Object value){
		objs.put(key,value);
	}

	public Object get(String key){
		return objs.get(key);
	}
}
