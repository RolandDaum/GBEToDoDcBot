package com.gbetododc.DiscordBot.Commands.settings;

import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class S_Settings_Setup {

    static Dotenv dotenv = Dotenv.configure().load();


    public static void setup(JDA jda) {
        
        jda.getGuildById(dotenv.get("GUILDID"))
            .upsertCommand("settings", "adjust the some settings")
                .addOption(OptionType.STRING, "null", "null")
            .queue(
                sucess -> {
                    Logger.log("S_Settings_Setup - Setup", "Successfully added /" + sucess.getFullCommandName() + " command", LogLvl.normale);
                }
            );
    }
}
