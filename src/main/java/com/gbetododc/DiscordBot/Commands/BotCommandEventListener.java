package com.gbetododc.DiscordBot.Commands;

import org.jetbrains.annotations.NotNull;

import com.gbetododc.DiscordBot.Commands.admin.S_Admin;
import com.gbetododc.DiscordBot.Commands.register.S_Register;
import com.gbetododc.DiscordBot.Commands.settings.S_Settings;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotCommandEventListener extends ListenerAdapter {
    
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(event.getName().equals("register")){
            S_Register.main(event);
        }
        // else if(event.getName().equals("settings")){
        //     S_Settings.main(event);
        // }
        else if (event.getName().equals("admin")){
            S_Admin.main(event);
        }
    }


}