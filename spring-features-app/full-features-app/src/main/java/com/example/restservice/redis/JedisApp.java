package com.example.restservice.redis;

/**
 * @className: JedisApp
 * @author: geeker
 * @date: 8/27/25 4:38 PM
 * @Version: 1.0
 * @description:
 */
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

public class JedisApp {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);

        // NX 示例
        String result1 = jedis.set("key1", "value1", SetParams.setParams().nx());
        System.out.println("NX result: " + result1); // OK

        // XX 示例
        String result2 = jedis.set("key1", "value2", SetParams.setParams().xx());
        System.out.println("XX result: " + result2); // OK

        // 带过期时间
        String result3 = jedis.set("temp_key", "temp_value",
                SetParams.setParams().nx().ex(Long.valueOf(60)));
        System.out.println("NX with EX result: " + result3);

        jedis.close();
    }
}
