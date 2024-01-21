package com.gbetododc.Test;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gbetododc.DiscordBot.Notification.JsonTTble;
import com.gbetododc.DiscordBot.Notification.ThreadHWNotification;
import com.gbetododc.DiscordBot.Notification.JsonTTble.TTble;
import com.google.gson.JsonObject;

public class Test {
    // private static List<Time> PeriodStartTimes = new ArrayList<>(
    //     Arrays.asList(
    //         Time.valueOf("07:40:00"),
    //         Time.valueOf("08:30:00"),
    //         Time.valueOf("09:35:00"),
    //         Time.valueOf("10:25:00"),
    //         Time.valueOf("11:25:00"),
    //         Time.valueOf("12:15:00"),
    //         Time.valueOf("13:05:00"),
    //         Time.valueOf("13:50:00"),
    //         Time.valueOf("14:35:00"),
    //         Time.valueOf("15:25:00"),
    //         Time.valueOf("16:20:00")
    //     )
    // );
    private static List<LocalTime> PeriodStartTimes = new ArrayList<>(
        Arrays.asList(
            LocalTime.of(7, 40),
            LocalTime.of(8,30),
            LocalTime.of(9,35),
            LocalTime.of(10,25),
            LocalTime.of(11,25),
            LocalTime.of(12,15),
            LocalTime.of(13,5),
            LocalTime.of(13,50),
            LocalTime.of(14,35),
            LocalTime.of(15,25),
            LocalTime.of(16,20)
            // Time.valueOf("07:40:00"),
            // Time.valueOf("08:30:00"),
            // Time.valueOf("09:35:00"),
            // Time.valueOf("10:25:00"),
            // Time.valueOf("11:25:00"),
            // Time.valueOf("12:15:00"),
            // Time.valueOf("13:05:00"),
            // Time.valueOf("13:50:00"),
            // Time.valueOf("14:35:00"),
            // Time.valueOf("15:25:00"),
            // Time.valueOf("16:20:00")
        )
    );
    public static void main(String[] args) {
        System.out.println(PeriodStartTimes.get(0));
        // LocalTime currentTime = LocalTime.now();
        
        // // Einordnung beim Start, wo er sich selber gerade befindet
        // Boolean isAfter;
        // LocalTime NextNotificationTime = null;
        // for (LocalTime localTime : PeriodStartTimes) {
        //     isAfter = currentTime.isAfter(localTime);
        //     if (!isAfter) {
        //         NextNotificationTime = localTime;
        //         break;
        //     }
        // }

        // System.out.println(NextNotificationTime);


        // Thread HWThread = new ThreadHWNotification();
        // HWThread.start();

        // while (HWThread.isAlive()) {
        //     System.out.println("Thread is running");
        //     try {
        //         Thread.currentThread().sleep(2);
        //     } catch (InterruptedException e) {
        //         // TODO Auto-generated catch block
        //         e.printStackTrace();
        //     }
        // }
        
        


        // TTble ttble = JsonTTble.getTTble();
        // System.out.println(
        //     "Dienstag 2 Stunde: " + 
        //     ttble.getCourses(5, 6)

        // );
    }
}