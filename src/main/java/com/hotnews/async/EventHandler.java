package com.hotnews.async;

import java.util.List;

/**
 * Author: XianDaLi
 * Date: 2020/8/9 20:40
 * Remark:
 */
public interface EventHandler {
	void doHandle(EventModel model);
	List<EventType> getSupportEventTypes();
}
