package com.gbetododc.DiscordBot;


import com.gbetododc.DiscordBot.Commands.BotCommandEventListener;
import com.gbetododc.DiscordBot.Commands.admin.S_Admin_Setup;
import com.gbetododc.DiscordBot.Commands.register.S_Register_Setup;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordBot extends ListenerAdapter {
    static Dotenv dotenv = Dotenv.configure().load();

    public static void main(String[] args) throws InterruptedException {
        String TOKEN = dotenv.get("DISCORDBOTTOKEN");

        JDA jda = JDABuilder.createDefault(TOKEN)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT)
            .addEventListeners(new BotCommandEventListener())
            .build()
            .awaitReady();

        // Setup of the /Commands
        jda.getGuildById(dotenv.get("GUILDID")).updateCommands().queue(); // Deleting all Guild Commands on startup
        // TODO - Die ganzen Setup dinger in eine Neue public functin in Bot Command Eventlistener machen und dann diese Date zu Commands.setup einfach umbenenen
        S_Register_Setup.setup(jda);
        // S_Settings_Setup.setup(jda);
        S_Admin_Setup.setup(jda);

    }
}