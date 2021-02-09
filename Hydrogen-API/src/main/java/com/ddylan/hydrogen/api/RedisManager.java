package com.ddylan.hydrogen.api;

import lombok.Getter;
import redis.clients.jedis.Jedis;

public class RedisManager {

    @Getter
    private Jedis jedis;

    public boolean init() {
        this.jedis = new Jedis(HydrogenAPI.getSettingsManager().getSettings().get("redis-host"));
        this.jedis.connect();
        System.out.println("Connected to redis.");
        return true;
    }

}
