package com.hotnews.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;
import sun.dc.pr.PRError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Author: XianDaLi
 * Date: 2020/8/9 0:33
 * Remark:
 */
@Service
public class JedisAdapter implements InitializingBean {
	private JedisPool pool = null;

	private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

	public static void print(int index, Object obj){
		System.out.println(String.format("%d,%s",index,obj.toString()));
	}
	public static void main(String[] argv){
		Jedis jedis = new Jedis();
		jedis.flushAll();

		// get,set 键值对，类似单个k v
		jedis.set("hello","world");
		print(1,jedis.get("hello"));
		jedis.rename("hello","newhello");
		print(1,jedis.get("newhello"));
		jedis.setex("hello2",10,"world");

		// 数值操作
		jedis.set("pv","100");
		jedis.incr("pv");           //+1
		print(2,jedis.get("pv"));
		jedis.incrBy("pv",5); //+n
		print(2,jedis.get("pv"));

		// 数据结构 双向列表， 最近来访， 粉丝列表，消息队列
		String listName = "listA";
		for (int i = 0; i < 10; i++) {
			jedis.lpush(listName,"a" + String.valueOf(i));
		}
		print(3,jedis.lrange(listName,0,12)); // 最近来访10个id
		print(4,jedis.llen(listName));
		print(5,jedis.lpop(listName));
		print(6,jedis.llen(listName));
		print(7,jedis.lindex(listName,3));
		print(8,jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER,"a4","bb"));
		print(9,jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE,"a4","cc"));
		print(10,jedis.lrange(listName,0,12));

		//hash操作,类似于map
		//Map<String,String> map = new HashMap<>();
		String userKey = "user12";
		jedis.hset(userKey,"name","jim");
		jedis.hset(userKey,"age","12");
		jedis.hset(userKey,"phone","18611111111");

		print(12,jedis.hget(userKey,"name"));
		print(13,jedis.hgetAll(userKey));
		jedis.hdel(userKey,"phone");
		print(14,jedis.hgetAll(userKey));
		print(15,jedis.hkeys(userKey)); // 取所有keys
		print(16,jedis.hvals(userKey)); // 取所有values
		print(17,jedis.hexists(userKey,"email")); // 是否存在该key
		print(18,jedis.hexists(userKey,"age"));
		jedis.hsetnx(userKey,"school","mju");  // 若不存在才添加
		jedis.hsetnx(userKey,"name","lxd");
		print(19,jedis.hgetAll(userKey));

		//set集合 点赞用户群, 共同好友
		String likeKeys1 = "newsLike1";
		String likeKeys2 = "newsLike2";
		for (int i = 0; i < 10; i++) {
			jedis.sadd(likeKeys1,String.valueOf(i));
			jedis.sadd(likeKeys2,String.valueOf(i*2));
		}
		print(20,jedis.smembers(likeKeys1));
		print(21,jedis.smembers(likeKeys2));
		print(22,jedis.sinter(likeKeys1,likeKeys2)); // 交集
		print(23,jedis.sunion(likeKeys1,likeKeys2)); // 并集
		print(24,jedis.sdiff(likeKeys1,likeKeys2)); // 差异
		print(25,jedis.sismember(likeKeys1,"5")); // 是否存在
		jedis.srem(likeKeys1,"5"); // 删除
		print(26,jedis.smembers(likeKeys1));
		print(27,jedis.scard(likeKeys1));
		jedis.smove(likeKeys2,likeKeys1,"14"); //把k2的14移动到k1
		print(28,jedis.scard(likeKeys1));
		print(29,jedis.smembers(likeKeys1));
//		print(30,jedis.smembers(likeKeys2));

		// 排序集合，优先队列，排行榜
		String rankKey = "rankKey";
		jedis.zadd(rankKey,15,"A");
		jedis.zadd(rankKey,60,"B");
		jedis.zadd(rankKey,90,"C");
		jedis.zadd(rankKey,80,"D");
		jedis.zadd(rankKey,75,"E");
		print(30,jedis.zcard(rankKey)); // 总共多少值
		print(31,jedis.zcount(rankKey,61,100));
		print(32,jedis.zscore(rankKey,"E"));
		jedis.zincrby(rankKey,2,"E");
		print(33,jedis.zscore(rankKey,"E"));
		// 改错卷了
		jedis.zincrby(rankKey,2,"Z");
		print(34,jedis.zcount(rankKey,0,100));
		// 2-4 名 Luc
		print(35,jedis.zrange(rankKey,1,3));
		print(36,jedis.zrevrange(rankKey,1,3));

		for(Tuple tuple : jedis.zrangeByScoreWithScores(rankKey,"0","100")){
			print(37,tuple.getElement() + ":" +String.valueOf(tuple.getScore()));
		}

		print(38,jedis.zrank(rankKey,"B")); // 正序排名
		print(39,jedis.zrevrank(rankKey,"B")); // 倒叙排名

		// 线程池，默认八条线程
//		JedisPool pool = new JedisPool();
//		for (int i = 0; i < 100; i++) {
//			Jedis j = pool.getResource();
//			j.get("a");
//			System.out.println("POOL" + i);
//			j.close();
//		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		pool = new JedisPool("localhost",6379);
	}

	private Jedis getJedis(){
		return pool.getResource();
	}

	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return getJedis().get(key);
		} catch (Exception e) {
			logger.error("发生异常" + e.getMessage());
			return null;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public void set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.set(key, value);
		} catch (Exception e) {
			logger.error("发生异常" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public long sadd(String key,String value){
		Jedis jedis = null;
		try{
			jedis = pool.getResource();
			return jedis.sadd(key,value);
		}catch (Exception e){
			logger.error("发生异常",e.getMessage());
			return 0;
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}

	public long srem(String key,String value){
		Jedis jedis = null;
		try{
			jedis = pool.getResource();
			return jedis.srem(key,value);
		}catch (Exception e){
			logger.error("发生异常",e.getMessage());
			return 0;
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}

	public boolean sismember(String key,String value){
		Jedis jedis = null;
		try{
			jedis = pool.getResource();
			return jedis.sismember(key,value);
		}catch (Exception e){
			logger.error("发生异常",e.getMessage());
			return false;
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}

	public long scard(String key){
		Jedis jedis = null;
		try{
			jedis = pool.getResource();
			return jedis.scard(key);
		}catch (Exception e){
			logger.error("发生异常",e.getMessage());
			return 0;
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}

	public void setex(String key, String value) {
		// 验证码, 防机器注册，记录上次注册时间，有效期3天
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.setex(key, 10, value);
		} catch (Exception e) {
			logger.error("发生异常" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public long lpush(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.lpush(key, value);
		} catch (Exception e) {
			logger.error("发生异常" + e.getMessage());
			return 0;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public List<String>  brpop(int timeout, String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.brpop(timeout, key);
		} catch (Exception e) {
			logger.error("发生异常" + e.getMessage());
			return null;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public void setObject(String key, Object obj){
		set(key, JSON.toJSONString(obj));
	}

	public <T> T getObject(String key,Class<T> clazz){
		String value = get(key);
		if(value != null){
			return JSON.parseObject(value,clazz);
		}
		return null;
	}

}
