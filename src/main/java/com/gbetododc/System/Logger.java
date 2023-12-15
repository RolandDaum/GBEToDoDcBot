package com.gbetododc.System;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    public static void main(String[] args) {
        log("Title", "Titleest message", LogLvl.Title);
        log("normale", "test message", LogLvl.normale);
        log("moderate", "test message", LogLvl.moderate);
        log("critical", "test message", LogLvl.critical);
    }

    public enum LogLvl {
        normale,
        moderate,
        critical,
        Title
    }

    public static void log(String logtitle, String logmessage, LogLvl level) {

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE dd MMM yyyy / HH:mm:ss");
        String dateTime = currentDateTime.format(dateTimeFormatter);

        switch (level) {
            case normale:
                System.out.println(ConsoleColors.BLACK + "[" + dateTime + "] " + ConsoleColors.GREEN + "[" + logtitle + "] " + ConsoleColors.WHITE + logmessage);
                break;
            case moderate:
                System.out.println(ConsoleColors.BLACK + "[" + dateTime + "] " + ConsoleColors.YELLOW + "[" + logtitle + "] " + ConsoleColors.WHITE + logmessage);
                break;
            case critical:
                System.out.println(ConsoleColors.BLACK + "[" + dateTime + "] " + ConsoleColors.RED_BOLD + "[" + logtitle + "] " + ConsoleColors.WHITE + logmessage);
                break;
            case Title:
                System.out.println(ConsoleColors.BLACK + "[" + dateTime + "] " + ConsoleColors.PURPLE_BOLD + "[" + logtitle + "] " + ConsoleColors.WHITE + logmessage);
                break;
        }
        // TODO: Add Discord Chanel log
        // TODO: Add log save to .txt

    }
}
