package com.gbetododc.DiscordBot.Commands.admin;

import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

public class S_Admin_Setup {
    static Dotenv dotenv = Dotenv.configure().load();

    public static void setup(JDA jda) {


        jda.getGuildById(dotenv.get("GUILDID"))
            .upsertCommand("admin", "admin settings")

            .addSubcommandGroups(

                new SubcommandGroupData("roles", "manage roles")
                    .addSubcommands(
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

                        new SubcommandData("remove", "a delete a role")
                            .addOption(OptionType.ROLE, "delete", "comfirm", true)
                            .addOptions(new OptionData(OptionType.STRING, "coursetype", "optional but necessary for courses - enter coursetype to remove it from courses.json")
                                .addChoice("LK", "LK")
                                .addChoice("GKNaturwissenschaften", "GKNaturwissenschaften")
                                .addChoice("GKSprache", "GKSprache")
                                .addChoice("GKGesellschaft", "GKGesellschaft")  
                                .addChoice("GKKünstlerisch", "GKKünstlerisch")
                                .addChoice("GKSport", "GKSport")
                                .addChoice("SF", "SF")
                            ),
    
                        new SubcommandData("removeall", "delete all roles")
                            .addOption(OptionType.BOOLEAN, "confirme", "confirme to delete all roles", true),
    
                        new SubcommandData("removeallcourses", "delete all courses")
                            .addOption(OptionType.BOOLEAN, "confirme", "confirme to delete all courseroles", true)
                    ),

                new SubcommandGroupData("bot", "manage bot")
                    .addSubcommands(
                        new SubcommandData("token", "manage the token")
                            .addOption(OptionType.STRING, "change", "change the bot token", true),
                        new SubcommandData("changeseverid", "change the server ID")
                            .addOption(OptionType.INTEGER, "change", "change the server ID used by the bot", true)
                    ),  

                new SubcommandGroupData("msapi","manage msapi")
                    .addSubcommands(

                        new SubcommandData("token", "manage the token")
                            .addOption(OptionType.BOOLEAN, "get", "output the current token")
                            .addOption(OptionType.BOOLEAN, "delete", "remove the current token")
                            .addOption(OptionType.BOOLEAN, "refresh", "refresh the current token")
                            .addOption(OptionType.BOOLEAN, "expire", "output the tokens expiration date"),

                        new SubcommandData("refreshtoken", "manage the refresh token")
                            .addOption(OptionType.BOOLEAN, "get", "output the current refreshtoken")
                            .addOption(OptionType.BOOLEAN, "delete", "remove the current refreshtoken")
                            .addOption(OptionType.BOOLEAN, "refresh", "refresh the current refreshtoken")
                            .addOption(OptionType.BOOLEAN, "expire", "output the tokens expiration date"),

                        new SubcommandData("clientsecret", "manage the client secret")
                            .addOption(OptionType.STRING, "change", "change the clientsecret")
                            .addOption(OptionType.BOOLEAN, "get", "output the current clientsecret")
                    )
            )

                // .addOptions(
                //     new OptionData(OptionType.STRING, "remove rolles", "remove a specific group of roles")
                //         .addChoice("all - (Dangerous never do this)", "remall")
                //         .addChoice("subjects - (For development purpose only)", "remsubj")
                //         .addChoice("specific - ()", "remsubj")
                // )
            .queue(
                sucess -> {
                    Logger.log("S_Admin_Setup - Setup", "Successfully added /admin command", LogLvl.normale);
                }
            );
        
    }

}
