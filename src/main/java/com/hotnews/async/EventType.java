package com.hotnews.async;

/**
 * Author: XianDaLi
 * Date: 2020/8/9 20:23
 * Remark:枚举类,分别发生了哪些事件
 */
public enum EventType {
	LIKE(0),
	COMMENT(1),
	LOGIN(2),
	MAIL(3);

	private int value;

	EventType(int value){this.value = value;}
	public int getValue(){return value;}
}
