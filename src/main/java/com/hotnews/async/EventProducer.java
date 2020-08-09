package com.hotnews.async;

import com.alibaba.fastjson.JSONObject;
import com.hotnews.controller.IndexController;
import com.hotnews.util.JedisAdapter;
import com.hotnews.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: XianDaLi
 * Date: 2020/8/9 20:45
 * Remark:将该事件放入异步队列
 */
@Service
public class EventProducer {
	private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);
	@Autowired
	JedisAdapter jedisAdapter;

	public boolean fireEvent(EventModel model){
		try {
			String json = JSONObject.toJSONString(model);
			String key = RedisKeyUtil.getEventQueueKey();
			jedisAdapter.lpush(key, json);
			return true;
		}catch (Exception e){
			logger.error("事件放入队列异常"+e.getMessage());
			return false;
		}
	}
}
