package com.gbetododc.DiscordBot.Commands;

import org.jetbrains.annotations.NotNull;
import com.gbetododc.DiscordBot.DiscordBot;
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
            case "admin":
                S_Admin.main(event);
                break;
            case "register":
                S_Register.main(event);
                break;            

        }
    }


    public static void commandSetup() {
        JDA jda = DiscordBot.JDA;
        Logger.log("BotCommand - Setup", "Started the BodCommand slash setupt process", LogLvl.normale);
        
        jda.getGuildById(dotenv.get("GUILDID")).updateCommands().queue(
            success -> {
                Logger.log("BotCommand - commandSetup", "Successfully reset all commands", LogLvl.normale);
            }
        );

        S_Register_Setup.setup(jda);
        S_Settings_Setup.setup(jda);
        S_Admin_Setup.setup(jda);
    }


}