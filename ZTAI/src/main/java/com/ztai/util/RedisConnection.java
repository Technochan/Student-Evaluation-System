package com.ztai.util;


import redis.clients.jedis.Jedis;

public class RedisConnection {
	
	private static Jedis jedis;

    public static void connect() {
        jedis = new Jedis("localhost", 6379);
    }

    public static Jedis getConnection() {
        if (jedis == null) {
            connect();
        }
        return jedis;
    }

    public static void closeConnection() {
        if (jedis != null) {
            jedis.close();
        }
    }
}
