package com.gbetododc.DiscordBot.Commands.register;

import java.util.ArrayList;
import java.util.List;

import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class S_Register_Setup {
    
    static Dotenv dotenv = Dotenv.configure().load();

    public static void setup(JDA jda) {
        String[] LKurse = {
            "MA1", "MA2", "PH1", "CH1", "BI1", "BI2",
            "DE1", "DE2", "DE3","ENG1", "ENG2", "ENG3","LA1","FR1",
            "EK1", "PW1", "PW2","GE1", "GE2",
            "KU1", "KU2"
        };
        String[] GKNaturwissenschaften = {"ma1", "ma2", "ma3", "ma4", "ph1", "bi1", "bi2", "if1", "if2", "ch1"};
        String[] GKSprache = {"de1", "de2", "eng1", "eng2", "snn1", "la1", "fr1"};
        String[] GKGesellschaft = {"pw1", "pw2", "ge1", "ge2", "re1", "re2", "rk1", "wn1"};
        String[] GKKünstlerisch = {"mu1", "ku1", "ds1"};
        String[] GKSport = {"sp1", "sp2", "sp3", "sp4", "spp1"};
        String[] SF = {"sf1", "sf2", "sf3", "sf4", "sf5", "sf6"};

        List<OptionData> optionDataList = new ArrayList<>();

        for (int i=1; i<4; i++) {   optionDataList.add(createOptionData("p"+i, "Wähle dein P"+i, LKurse, true));                                                   }
        for (int i=1; i<6; i++) {   optionDataList.add(createOptionData("gk-naturwissenschaften-"+i, "Wähle einen Kurs aus dem naturwissenschaftlichen Bereich", GKNaturwissenschaften, false));  }
        for (int i=1; i<6; i++) {   optionDataList.add(createOptionData("gk-sprache-"+i, "Wähle einen Kurs aus dem sprachlichen Bereich", GKSprache, false));                          }
        for (int i=1; i<5; i++) {   optionDataList.add(createOptionData("gk-gesellschaft-"+i, "Wähle einen Kurs aus dem gesellschaftlichen Bereich", GKGesellschaft, false));                }
        for (int i=1; i<4; i++) {   optionDataList.add(createOptionData("gk-künstlerisch-"+i, "Wähle einen Kurs aus dem künstlerischen Bereich", GKKünstlerisch, false));                }
        optionDataList.add(createOptionData("gk-sport", "Wähle dein Sportkurs", GKSport, false));
        optionDataList.add(createOptionData("seminarfach", "Wähle dein Seminarfach", SF, false));

        jda.getGuildById(dotenv.get("GUILDID"))
            .upsertCommand("register", "regsiter your courses")
            .addOptions(optionDataList)
            .queue(
                sucess -> {
                    Logger.log("S_Register_Setup - Setup", "Successfully added /register command", LogLvl.normale);
                }
            );
    }

    private static OptionData createOptionData(String name, String description, String[] choicelist, Boolean required) {
        OptionData optionData = new OptionData(OptionType.STRING, name, description, required);
        
        for (String course : choicelist) {
            optionData.addChoice(course, course);
        }

        return optionData;
    }
}
