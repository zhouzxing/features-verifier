package com.example.restservice.bean.datetime;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @className: DateTest
 * @author: geeker
 * @date: 12/2/25 7:08 PM
 * @Version: 1.0
 * @description:
 */

public class DateTest {

    @Test
    public void calendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        calendar.set(2024,0,1);
        System.out.println(calendar.getTime());

        LocalDateTime localDateTime = LocalDateTime.of(2024, 1, 1,1,1);
        System.out.println(localDateTime);
    }

    @Test
    public void timeZone() {
        // 有效的时区标识符
        String[] timeZones = {
                "UTC", "GMT", "GMT+0", "GMT-0", "GMT0",
                "Z", "UTC+0", "UTC-0", "UTC0", "Etc/UTC", "Etc/GMT"
        };

        for (String zoneId : timeZones) {
            try {
                ZoneId zone = ZoneId.of(zoneId);
                ZonedDateTime time = ZonedDateTime.now(zone);
                System.out.println(zoneId + " : " + time);
            } catch (Exception e) {
                System.out.println(zoneId + " : 无效时区");
            }
        }
    }

    @Test
    public void http() {
        // HTTP 头部使用 GMT 格式（历史原因）
        Instant now = Instant.now();

        // RFC 1123 格式（HTTP 标准）
        DateTimeFormatter httpFormatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        ZonedDateTime gmtTime = now.atZone(ZoneId.of("GMT"));
        String httpDate = gmtTime.format(httpFormatter);

        System.out.println("HTTP 日期头: " + httpDate);

        // ISO 8601 格式（现代API推荐）
        String isoDate = now.toString();
        System.out.println("ISO 8601 格式: " + isoDate);

        // 解析 HTTP 日期
        String receivedDate = "Mon, 15 Jan 2024 10:30:00 GMT";
        ZonedDateTime parsed = ZonedDateTime.parse(receivedDate, httpFormatter);
        System.out.println("解析后的时间: " + parsed);
    }

}
