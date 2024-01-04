package com.gbetododc.DiscordBot.Commands;

import org.jetbrains.annotations.NotNull;
import com.gbetododc.DiscordBot.Commands.admin.S_Admin;
import com.gbetododc.DiscordBot.Commands.register.S_Register;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "admin":
                S_Admin.main(event);
                break;
            case "register":
                S_Register.main(event);
                break;
        }
    }
}