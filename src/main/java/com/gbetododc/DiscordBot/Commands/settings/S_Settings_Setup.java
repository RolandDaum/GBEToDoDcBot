package com.gbetododc.DiscordBot.Commands.settings;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class S_Settings_Setup {

    static Dotenv dotenv = Dotenv.configure().load();


    public static void setup(JDA jda) {
        
        jda.getGuildById(dotenv.get("GUILDID"))
            .upsertCommand("settings", "adjust the some settings")
                .addOption(OptionType.STRING, "rolename", "add role")
                    .addChoice("Choice1", "Wert1")
                    .addChoice("Choice2", "Wert2")
                .addOption(OptionType.INTEGER, "test_int", "test int desc", "Test Integer")
                    .addChoice("Choice3", 42)
                    .addChoice("Choice4", 99)
            .queue();
    }
}
