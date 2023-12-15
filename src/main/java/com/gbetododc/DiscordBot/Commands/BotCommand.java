package com.gbetododc.DiscordBot.Commands;

import org.jetbrains.annotations.NotNull;
import com.gbetododc.DiscordBot.Commands.admin.S_Admin;
import com.gbetododc.DiscordBot.Commands.admin.S_Admin_Setup;
import com.gbetododc.DiscordBot.Commands.register.S_Register;
import com.gbetododc.DiscordBot.Commands.register.S_Register_Setup;
import com.gbetododc.DiscordBot.Commands.settings.S_Settings_Setup;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotCommand extends ListenerAdapter {
    static Dotenv dotenv = Dotenv.configure().load();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "register":
                S_Register.main(event);

            // case "settings":
            //     S_Settings.main(event);
            
            case "admin":
                S_Admin.main(event);
        }
    }


    public static void commandSetup(JDA jda) {
        Logger.log("BotCommand Setup", "Started the BodCommand slash setupt process", LogLvl.normale);
        // Setup of the /Commands
        jda.getGuildById(dotenv.get("GUILDID")).updateCommands().queue(
            success -> {
                Logger.log("BotCommand - Setup", "Successfully reset all commands", LogLvl.normale);
            }
        ); // Deleting all Guild Commands on startup

        // TODO - Die ganzen Setup dinger in eine Neue public functin in Bot Command Eventlistener machen und dann diese Date zu Commands.setup einfach umbenenen
        S_Register_Setup.setup(jda);
        S_Settings_Setup.setup(jda);
        S_Admin_Setup.setup(jda);
    }


}