package com.gbetododc.test;

import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;

public class Test {
    public static void main(String[] args) {
        System.out.println(
            LocalTime.now(Clock.system(ZoneId.of("Europe/Berlin")))
        );
    }
}