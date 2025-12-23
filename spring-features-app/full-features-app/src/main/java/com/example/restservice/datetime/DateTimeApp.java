package com.example.restservice.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @className: App
 * @author: geeker
 * @date: 12/2/25 6:38 PM
 * @Version: 1.0
 * @description:
 */

public class DateTimeApp {
    public static void main(String[] args) throws ParseException {
        // 日历+时区： 格林 -> 日期
        Date date = new Date();
        System.out.println(date.getYear());
        System.out.println(date.getMonth());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(date));
        Date date1 = simpleDateFormat.parse("2023-09-01 12:00:00");

        // java.time.*

        long currentTimeMillis = System.currentTimeMillis();
        Clock clockDefault = Clock.systemDefaultZone();
        Clock clockUTC = Clock.systemUTC();
        Clock clock = Clock.system(ZoneId.of("Asia/Shanghai"));
        // 时钟偏移
        Clock offset = Clock.offset(clock, Duration.ofHours(8));
        Clock tick = Clock.tick(clock, Duration.ofHours(8));
        LocalDateTime localDateTime = LocalDateTime.now(clockUTC);


    }
}
