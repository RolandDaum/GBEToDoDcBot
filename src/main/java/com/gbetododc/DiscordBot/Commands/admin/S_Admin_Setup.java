package com.gbetododc.DiscordBot.Commands.admin;

import java.time.LocalTime;
import java.util.List;
import com.gbetododc.DiscordBot.DiscordBot;
import com.gbetododc.DiscordBot.Notification.HomeworkNotification;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

public class S_Admin_Setup {
    static Dotenv dotenv = Dotenv.configure().load();

    public static void setup() {
        // Var to be used in the command setup
        List<LocalTime> pst = HomeworkNotification.PeriodStartTimes;

        DiscordBot.MAIINSERVERGUILD.upsertCommand("admin", "admin settings")
            .addSubcommandGroups(
                new SubcommandGroupData("roles", "manage roles")
                    .addSubcommands(
                        new SubcommandData("list", "list all roles")
                            .addOptions(
                                new OptionData(OptionType.STRING, "roletypes", "types of roles to be listed", true)
                                    .addChoice("all", "all")
                                    .addChoice("courses", "courses")
                            ),
                            
                        new SubcommandData("add", "add a role")
                            .addOption(OptionType.STRING, "rolename", "enter role name", true)
                            .addOptions(new OptionData(OptionType.STRING, "coursetype", "optional - enter a coursetype if the role is a course")
                                                .addChoice("LK", "LK")
                                                .addChoice("GKNaturwissenschaften", "GKNaturwissenschaften")
                                                .addChoice("GKSprache", "GKSprache")
                                                .addChoice("GKGesellschaft", "GKGesellschaft")  
                                                .addChoice("GKKünstlerisch", "GKKünstlerisch")
                                                .addChoice("GKSport", "GKSport")
                                                .addChoice("SF", "SF")
                            )
                            .addOption(OptionType.INTEGER, "rolecolor", "optional - enter role color as int like 0x255255255"),

                        new SubcommandData("remove", "a delete a role (courses will automaticlly be removed from .json file)")
                            .addOption(OptionType.ROLE, "deleterole", "comfirm", true),
    
                        new SubcommandData("removeall", "delete all roles")
                            .addOption(OptionType.BOOLEAN, "confirme", "confirme to delete all roles", true),
    
                        new SubcommandData("removeallcourses", "delete all courses")
                            .addOption(OptionType.BOOLEAN, "confirme", "confirme to delete all courseroles", true)
                    ),

                new SubcommandGroupData("bot", "manage bot")
                    .addSubcommands(
                        new SubcommandData("updatecommands", "update all the commands")
                            .addOption(OptionType.BOOLEAN, "confirme", "confirme", true),
                        new SubcommandData("token", "manage the token")
                            .addOption(OptionType.STRING, "change", "change the bot token", true),
                        new SubcommandData("changeseverid", "change the server ID")
                            .addOption(OptionType.INTEGER, "change", "change the server ID used by the bot", true),
                        new SubcommandData("shutdown", "shutdown the bot")
                            .addOption(OptionType.BOOLEAN, "confirme", "shutdown the bot", true)
                    ),  

                new SubcommandGroupData("msapi","manage msapi")
                    .addSubcommands(
                        new SubcommandData("reauthorize", "ommand for reauthentication")
                            .addOption(OptionType.STRING, "authcode", "enter authcode"),
                        new SubcommandData("refreshtoken", "refresh auth cred via the RFToken"),
                        new SubcommandData("refreshtodo", "refresh the todo list"),
                        new SubcommandData("ctimehwnot", "notifie a course of their homework at a specified time")
                            // .addOption(OptionType.ROLE, "courserole", "the course role to be notified", true)
                            .addOptions(
                                new OptionData(OptionType.STRING, "notificationtime", "Choose at which period time should be notified", true)
                                    .addChoice(pst.get(0).toString(), pst.get(0).toString())
                                    .addChoice(pst.get(1).toString(), pst.get(1).toString())
                                    .addChoice(pst.get(2).toString(), pst.get(2).toString())
                                    .addChoice(pst.get(3).toString(), pst.get(3).toString())
                                    .addChoice(pst.get(4).toString(), pst.get(4).toString())
                                    .addChoice(pst.get(5).toString(), pst.get(5).toString())
                                    .addChoice(pst.get(6).toString(), pst.get(6).toString())
                                    .addChoice(pst.get(7).toString(), pst.get(7).toString())
                                    .addChoice(pst.get(8).toString(), pst.get(8).toString())
                                    .addChoice(pst.get(9).toString(), pst.get(9).toString())
                                    .addChoice(pst.get(10).toString(), pst.get(10).toString())
                            )     
                    )
            )
            .queue(
                sucess -> {
                    Logger.log("S_Admin_Setup - Setup", "Successfully added /admin command", LogLvl.normale);
                },
                failure -> {
                    Logger.log("S_Admin_Setup - Setup", "failed to update '/admin' command", LogLvl.moderate);
                }
            );
        
    }

}
