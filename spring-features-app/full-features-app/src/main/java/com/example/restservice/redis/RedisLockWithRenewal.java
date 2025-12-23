package com.example.restservice.redis;

/**
 * @className: RedissonLockWithRenew
 * @author: geeker
 * @date: 8/27/25 3:37 PM
 * @Version: 1.0
 * @description:
 */

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

public class RedisLockWithRenewal {
    private Jedis jedis;
    private String lockKey;
    private String lockValue;
    private long expireTimeMs;
    private volatile boolean isLocked = false;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> renewalTask;

    public RedisLockWithRenewal(Jedis jedis, String lockKey, long expireTimeMs) {
        this.jedis = jedis;
        this.lockKey = lockKey;
        this.expireTimeMs = expireTimeMs;
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.lockValue = UUID.randomUUID().toString(); // 唯一值，用于安全释放
    }

    public boolean tryLock() {
        // 1. 尝试获取锁
        SetParams setParams = SetParams.setParams().nx().px(expireTimeMs);
        String result = jedis.set(lockKey, lockValue, setParams);
        if ("OK".equals(result)) {
            isLocked = true;
            // 2. 启动续约任务
            startRenewalTask();
            return true;
        }
        return false;
    }

    private void startRenewalTask() {
        long renewalPeriodMs = expireTimeMs / 3; // 续约周期为过期时间的 1/3
        renewalTask = scheduler.scheduleAtFixedRate(() -> {
            if (isLocked) {
                try {
                    // 3. 定期续约：重置过期时间
                    jedis.expire(lockKey, Long.valueOf (expireTimeMs / 1000)); // expire 命令参数是秒
                    System.out.println("锁续约成功: " + lockKey);
                } catch (Exception e) {
                    // 续约失败，可能是 Redis 连接问题或锁已被其他线程释放
                    System.err.println("锁续约失败: " + e.getMessage());
                    // 可以考虑取消任务
                    renewalTask.cancel(true);
                }
            }
        }, renewalPeriodMs, renewalPeriodMs, TimeUnit.MILLISECONDS); // 初始延迟后，周期执行
    }

    public void unlock() {
        if (!isLocked) {
            return;
        }
        // 4. 停止续约任务
        if (renewalTask != null) {
            renewalTask.cancel(true); // 中断续约线程
        }
        scheduler.shutdown();

        // 5. 使用 Lua 脚本保证原子性地释放锁（判断值再删除）
        String luaScript = """
                if redis.call('get', KEYS[1]) == ARGV[1] then
                    return redis.call('del', KEYS[1])
                else
                    return 0
                end
                """;
        try {
            jedis.eval(luaScript, Collections.singletonList(lockKey), Collections.singletonList(lockValue));
            isLocked = false;
            System.out.println("锁释放成功: " + lockKey);
        } catch (Exception e) {
            System.err.println("锁释放异常: " + e.getMessage());
        }
    }
}