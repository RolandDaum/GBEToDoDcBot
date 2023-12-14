package com.gbetododc.DiscordBot.Commands.admin;

import com.gbetododc.DiscordBot.roles;
import com.gbetododc.DiscordBot.Commands.register.S_Register;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class S_Admin {
    
    static Dotenv dotenv = Dotenv.configure().load();
    static String GUILDID = dotenv.get("GUILDID");

    public static void main(SlashCommandInteractionEvent event) {

        if (event.getSubcommandGroup().equals("roles")) {
            if (event.getSubcommandName().equals("deleteallcourseroles")) {

            }
        }

        switch (event.getSubcommandGroup()) {

            case "roles":

                switch (event.getSubcommandName()) {

                    case "add":
                        roles.createRole(
                            event.getJDA().getGuildById(GUILDID), 
                            event.getOption("rolename", null, OptionMapping::getAsString), 
                            event.getOption("coursetype", null, OptionMapping::getAsString), 
                            event.getOption("rolecolor", null, OptionMapping::getAsInt)         // Was machen, wenn null aka nicht eingegeben wurde?
                        );
                        event.reply("Started rolecreation process").queue();
                    case "idk":
                        break;
                }

        
            case "idk":
                break;
        }


    }
}
