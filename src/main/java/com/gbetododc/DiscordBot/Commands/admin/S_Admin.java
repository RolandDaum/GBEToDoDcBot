package com.gbetododc.DiscordBot.Commands.admin;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class S_Admin {
    public static void main(SlashCommandInteractionEvent event) {

        if (event.getSubcommandGroup().equals("roles")) {
            if (event.getSubcommandName().equals("deleteallcourseroles")) {

            }
        }


        // event.reply("admin command").queue();
    }

    private void removeAllCourseroles() {
        
    }

    private void removeRolle() {

    }
}
