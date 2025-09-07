package com.rakesh.sms.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisConnection {

	public static int expiryTimer;
	private static JedisPool jedisPool;

	public void init() {
		String redisIp = CoreUtils.getProperty("redisIp");
		String redisPort = CoreUtils.getProperty("redisPort");
		if ((redisIp != null) && (redisPort != null)) {
			jedisPool = new JedisPool(redisIp, Integer.parseInt(redisPort));
			expiryTimer = Integer.parseInt(CoreUtils.getProperty("redisExpiryTimer"));
		}
	}

	public static Jedis getJedis() {
		if (jedisPool != null) {
			return jedisPool.getResource();
		} else {
			return null;
		}
	}

	

	@SuppressWarnings("deprecation")
	public static void disconnect(Jedis jedis) {
		try {
			if (jedis != null)
				jedisPool.returnResourceObject(jedis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void destroy() {
		jedisPool.destroy();
	}

}