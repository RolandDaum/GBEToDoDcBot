package com.gbetododc.DiscordBot.Notification;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreadHWNotification extends Thread {
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
    public static void main(String[] args) {new ThreadHWNotification().start();}
    @Override
    public void run() {     
        while (true) {

            LocalTime currentTime = LocalTime.now();
            LocalTime wakeUpTime = getNextNotificationTime(currentTime);

            System.out.println("Thread woke up and will sleep until: " + wakeUpTime);
            Duration timetosleep = Duration.between(currentTime, wakeUpTime);
            try {Thread.sleep(timetosleep);}
            catch (InterruptedException e) {e.printStackTrace();}

            HomeworkNotification.Notifie(wakeUpTime);
        }
    }
    private static LocalTime getNextNotificationTime(LocalTime currentTime) {
        Boolean isAfter;
        for (LocalTime localTime : PeriodStartTimes) {
            isAfter = currentTime.isAfter(localTime);
            if (!isAfter) {
                return localTime;
            }
        }
        return null;
    }
}
