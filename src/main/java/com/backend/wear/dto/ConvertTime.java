package com.backend.wear.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

// CreatedAt 포맷 클래스 (몇분전 등)
public class ConvertTime {
    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    public static String convertLocalDatetimeToTime(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();

        long diffTime = localDateTime.until(now, ChronoUnit.SECONDS); // now보다 이후면 +, 전이면 -

        if (diffTime < SEC)
            return diffTime + "초 전";

        diffTime = diffTime / SEC;
        if (diffTime < MIN)
            return diffTime + "분 전";

        diffTime = diffTime / MIN;
        if (diffTime < HOUR)
            return diffTime + "시간 전";

        diffTime = diffTime / HOUR;
        if (diffTime < DAY)
            return diffTime + "일 전";

        diffTime = diffTime / DAY;
        if (diffTime < MONTH)
            return diffTime + "개월 전";

        diffTime=diffTime/MONTH;
        return diffTime+"년 전";
    }

    public static LocalDateTime convertChatTimeStampToLocalDateTime(String timestamp){
        LocalDateTime now = LocalDateTime.now();

        // "오후 3:26" 형식의 문자열을 LocalTime으로 파싱
        LocalTime time = LocalTime.parse(timestamp, DateTimeFormatter.ofPattern("a h:mm"));

        // 현재 날짜와 시간의 연도, 월, 일, 초, 밀리초를 가져오기
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int second = now.getSecond();
        int ms = now.getNano();

        // LocalDateTime으로 합치기
        LocalDateTime dateTime = LocalDateTime.of(year, month, day, time.getHour(), time.getMinute(), second, ms);

        return dateTime;
    }
}