package com.gbetododc.DiscordBot.Commands.admin;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.Command.Subcommand;
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
                            .addOption(OptionType.STRING, "rolename", "enter role name"),
    
                        new SubcommandData("remove", "a delete a role")
                            .addOption(OptionType.ROLE, "delete", "comfirm"),
    
                        new SubcommandData("removeall", "delete all roles")
                            .addOption(OptionType.BOOLEAN, "delete", "comfirm"),
    
                        new SubcommandData("removeallcourses", "delete all courses")
                            .addOption(OptionType.BOOLEAN, "delete", "comfirm")
                    ),

                new SubcommandGroupData("bot", "manage bot")
                    .addSubcommands(
                        new SubcommandData("token", "manage the token")
                            .addOption(OptionType.STRING, "change", "change the bot token"),
                        new SubcommandData("severid", "manage the server ID")
                            .addOption(OptionType.INTEGER, "change", "change the server ID used by the bot")
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
            .queue();
        
    }

}
