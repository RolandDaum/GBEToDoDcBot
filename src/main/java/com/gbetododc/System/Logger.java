package com.gbetododc.System;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.management.relation.Role;

import com.gbetododc.DiscordBot.DiscordBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;

public class Logger {
    private static void test() {
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

        Long consolelogChannelID = 1186320451492925510L;

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE dd MMM yyyy / HH:mm:ss");
        String dateTime = currentDateTime.format(dateTimeFormatter);

        switch (level) {

            case normale:
                System.out.println(ConsoleColors.BLACK + "[" + dateTime + "] " + ConsoleColors.GREEN + "[" + logtitle + "] " + ConsoleColors.WHITE + logmessage);
                try {
                    DiscordBot.JDA.getGuildById(DiscordBot.GUILDID)
                        .getTextChannelById(consolelogChannelID)
                        .sendMessage("```ansi\n[0;2m[0;2m[0;2m[0;2m[0;2m[" + dateTime + "] [0;32m[" + logtitle + "][0m " + logmessage + "\n```")
                        .queue();
                    } catch (Throwable e) {}     
                break;

            case moderate:
                System.out.println(ConsoleColors.BLACK + "[" + dateTime + "] " + ConsoleColors.YELLOW + "[" + logtitle + "] " + ConsoleColors.WHITE + logmessage);
                try {
                    DiscordBot.JDA.getGuildById(DiscordBot.GUILDID)
                        .getTextChannelById(consolelogChannelID)
                        .sendMessage("```ansi\n[0;2m[0;2m[" + dateTime + "] [0;33m[" + logtitle + "][0m " + logmessage + "\n```")
                        .queue();
                    } catch (Throwable e) {}      
                break;

            case critical:
                System.out.println(ConsoleColors.BLACK + "[" + dateTime + "] " + ConsoleColors.RED_BOLD + "[" + logtitle + "] " + ConsoleColors.WHITE + logmessage);
                try {
                    DiscordBot.JDA.getGuildById(DiscordBot.GUILDID)
                        .getTextChannelById(consolelogChannelID)
                        .sendMessage("```ansi\n[" + dateTime + "] [2;32m[1;32m[1;33m[1;31m[0;31m[0;31m[" + logtitle + "][0m[0;31m[0m[1;31m[0m[1;33m[0m[1;32m[0m[2;32m[0m "+ logmessage + "\n```")
                        .queue();
                    } catch (Throwable e) {}    
                try {
                    net.dv8tion.jda.api.entities.Role developerRole = DiscordBot.JDA.getGuildById(DiscordBot.GUILDID).getRolesByName("Developer", true).getFirst();
                    List<Member> developerList = DiscordBot.JDA.getGuildById(DiscordBot.GUILDID).getMembersWithRoles(developerRole);

                    for (Member member : developerList) {
                        member.getUser().openPrivateChannel().queue(
                            channel -> {
                                channel
                                    .sendMessage("```ansi\n[" + dateTime + "] [2;32m[1;32m[1;33m[1;31m[0;31m[0;31m[" + logtitle + "][0m[0;31m[0m[1;31m[0m[1;33m[0m[1;32m[0m[2;32m[0m "+ logmessage + "\n```")
                                    .queue();
                            }
                        );
                    }
                } catch (Throwable e) {}
                break;

            case Title:
                System.out.println(ConsoleColors.BLACK + "[" + dateTime + "] " + ConsoleColors.PURPLE_BOLD + "[" + logtitle + "] " + ConsoleColors.WHITE + logmessage);
                try {
                    DiscordBot.JDA.getGuildById(DiscordBot.GUILDID)
                        .getTextChannelById(consolelogChannelID)
                        .sendMessage("```ansi\n[0;2m[0;2m[0;2m[0;2m[" + dateTime + "] [0;34m[" + logtitle + "][0m " + logmessage + "\n```")
                        .queue();
                    } catch (Throwable e) {}
        }

        // TODO: Add log save to .txt

    }
}
