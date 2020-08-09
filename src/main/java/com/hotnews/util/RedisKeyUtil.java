package com.hotnews.util;

/**
 * Author: XianDaLi
 * Date: 2020/8/9 12:47
 * Remark:设置生成的集合类名称
 */
public class RedisKeyUtil {
	private static String SPLIT = ":";
	private static String BITZ_LIKE = "LIKE";
	private static String BITZ_DISLIKE = "DISLIKE";
	private static String BIZ_EVENT = "EVENT";

	public static String getEventQueueKey() {
		return BIZ_EVENT;
	}

	public static String getLikeKey(int entityId,int entityType){
		// "LIKE:xxx:xxx"
		return BITZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
	}

	public static String getDisLikeKey(int entityId,int entityType){
		// "DISLIKE:xxx:xxx"
		return BITZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
	}

}
