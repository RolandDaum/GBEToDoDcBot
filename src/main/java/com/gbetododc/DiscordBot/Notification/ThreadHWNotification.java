package com.gbetododc.DiscordBot.Notification;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;

import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;

public class ThreadHWNotification extends Thread {

    public static void main(String[] args) {
        new ThreadHWNotification().start();
    }
    @Override
    public void run() {   
        Logger.log("ThreadHWNotification - Thread", "Notification Thread is up and running", LogLvl.Title);
  
        while (true) {

            LocalTime currentTime = LocalTime.now(Clock.system(ZoneId.of("Europe/Berlin")));
            LocalTime wakeUpTime = getNextNotificationTime(currentTime);

            Logger.log("ThreadHWNotification - Thread", "Notification Thread will sleep until: " + wakeUpTime, LogLvl.normale);

            Duration timetosleep = Duration.ofDays(0);
            if (currentTime.isBefore(wakeUpTime)) {
                timetosleep = Duration.between(currentTime, wakeUpTime);
            } else if (currentTime.isAfter(wakeUpTime)) {
                timetosleep = Duration.between(
                    currentTime, LocalTime.of(23, 59, 59)
                    ).plus(
                        Duration.between(LocalTime.of(0,0,0), wakeUpTime)
                    );
            }

            try {Thread.sleep(timetosleep.toMillis());}
            catch (InterruptedException e) {e.printStackTrace();}

            HomeworkNotification.Notifie(wakeUpTime);
        }
    }
    private static LocalTime getNextNotificationTime(LocalTime currentTime) {
        Boolean isAfter;
        for (LocalTime localTime : HomeworkNotification.PeriodStartTimes) {
            isAfter = currentTime.isAfter(localTime);
            if (!isAfter) {
                return localTime;
            }
        }
        return HomeworkNotification.PeriodStartTimes.get(0);
    }
}
