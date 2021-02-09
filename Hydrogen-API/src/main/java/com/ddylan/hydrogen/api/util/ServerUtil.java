package com.ddylan.hydrogen.api.util;

import com.ddylan.hydrogen.api.HydrogenAPI;
import com.nimbusds.jose.shaded.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerUtil {

    private static Jedis jedis = HydrogenAPI.getRedisManager().getJedis();

    public static void update(String name, Map<String, Object> body) {
        String key = "servers:" + name;
        jedis.hset(key, "id", name);
        jedis.hset(key, "displayName", name);
        jedis.hset(key, "serverGroup", name);
        jedis.hset(key, "serverIp", "127.0.0.1");
        jedis.hset(key, "lastTps", body.get("lastTps").toString());
        jedis.hset(key, "lastUpdatedAt", (System.currentTimeMillis() / 1000L) + "");

        jedis.sadd("servers", name);
    }

    public static Set<JSONObject> getServersAsJSON() {
        Set<JSONObject> servers = new HashSet<>();

        jedis.keys("servers:*").forEach(key -> {
            JSONObject json = new JSONObject();

            json.putAll(jedis.hgetAll(key));
            servers.add(json);
        });
        return servers;
    }
}
