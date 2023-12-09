package com.gbetododc.DiscordBot.Commands.settings;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class S_Settings {
    public static void main(SlashCommandInteractionEvent event) {
        event.reply("setting command").queue();
    }
}
