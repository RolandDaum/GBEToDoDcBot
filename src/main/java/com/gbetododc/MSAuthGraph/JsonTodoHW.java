package com.gbetododc.MSAuthGraph;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

import com.gbetododc.DiscordBot.DiscordBot;
import com.gbetododc.MSAuthGraph.MsGraph.ToDoHWTasks;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;
import com.google.gson.Gson;

public class JsonTodoHW {
    static String jsonFilePathString = DiscordBot.PROJPATH + "\\src\\main\\java\\com\\gbetododc\\MSAuthGraph\\todoHW.json";
    
    public static ToDoHWTasks getToDoHW() {
        try {
            String jsonDataString = new String(Files.readAllBytes(Paths.get(jsonFilePathString)));

            ToDoHWTasks todohwtasks = new Gson().fromJson(jsonDataString, ToDoHWTasks.class);

            return todohwtasks;
        } catch (Throwable e) {
            Logger.log("MSenvJson - getToDoHW", "Failed to read todoHW.json. It may be missing or currepted", LogLvl.critical);
            return null;
        }
    }

    public static Boolean saveToDoHW(ToDoHWTasks todohwtasks) {
        try (FileWriter filewriter = new FileWriter(jsonFilePathString)) {
            filewriter.write(new Gson().toJson(todohwtasks, ToDoHWTasks.class));
            filewriter.close();
        
            return true;
        }
        catch (Throwable e) {
            return false;
        }
    }
}
