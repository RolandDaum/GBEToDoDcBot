package com.gbetododc;

import java.util.concurrent.TimeUnit;

import com.gbetododc.DiscordBot.DiscordBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class App {
    public static void main( String[] args ) throws InterruptedException {
        DiscordBot.main(args);
    }
}