package com.ztai.dao;

import java.util.List;

import com.ztai.model.ProgressCard;
import com.ztai.util.RedisConnection;

import redis.clients.jedis.Jedis;

public class RedisManager {
	

    private static RedisManager redisManager;

    private RedisManager() {
        RedisConnection.connect();
    }

    public static RedisManager getInstance() {
        if (redisManager == null) {
            redisManager = new RedisManager();
        }
        return redisManager;
    }
	
	public boolean checkKey(String key) {
		boolean keyExists = false;
        try (Jedis jedis = RedisConnection.getConnection()) {

            keyExists = jedis.exists(key);
        	System.out.println("Checking key "+ keyExists);
        }
        return keyExists;
	}

	public List<ProgressCard> getProgressCardInfo() {
		return null;
	}

	public void setProfressCardInfo(List<ProgressCard> report) {
		System.out.println("Coming to update");
	}

}
