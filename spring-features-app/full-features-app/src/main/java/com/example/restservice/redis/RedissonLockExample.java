package com.example.restservice.redis;

/**
 * @className: RedissonExample
 * @author: geeker
 * @date: 8/27/25 3:34 PM
 * @Version: 1.0
 * @description:
 */
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import java.util.concurrent.TimeUnit;

public class RedissonLockExample {
    public static void main(String[] args) {
        // 1. 配置并创建 RedissonClient
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);

        // 2. 获取分布式锁对象
        RLock lock = redisson.getLock("myLock");

        try {
            // 3. 获取锁（最重要的一步）
            // 不传leaseTime参数，看门狗才会生效
            //lock.lock(); // 或者使用 lock.lock(10, TimeUnit.SECONDS); 但这样看门狗不生效
            lock.lock(10, TimeUnit.SECONDS);

            // 4. 执行你的业务逻辑
            System.out.println("锁获取成功，执行业务...");
            Thread.sleep(60000); // 模拟一个长达60秒的业务

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 5. 无论如何，最后要释放锁
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                System.out.println("锁已释放");
            }
        }

        redisson.shutdown();
    }
}