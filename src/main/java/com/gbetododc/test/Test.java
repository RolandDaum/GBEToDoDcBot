package com.gbetododc.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

import com.gbetododc.DiscordBot.Notification.JsonTTble;
import com.gbetododc.DiscordBot.Notification.JsonTTble.Day;
import com.gbetododc.DiscordBot.Notification.JsonTTble.TTble;
import com.google.gson.JsonObject;

public class Test {
    public static void main(String[] args) {
        TTble ttble = JsonTTble.getTTble();
        System.out.println(
            "Dienstag 2 Stunde: " + 
            ttble.getCourses(5, 6)

        );
    }
}