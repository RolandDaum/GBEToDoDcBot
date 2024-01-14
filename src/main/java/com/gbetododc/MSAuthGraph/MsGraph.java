package com.gbetododc.MSAuthGraph;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import com.gbetododc.MSAuthGraph.JsonMSenv.MSenv;
import com.google.gson.Gson;

import kong.unirest.Unirest;

public class MsGraph {
    /**
     * Refreshing the todoHW.json file
     * @param callback as Boolean success
     */
    public static void refreshToDoList(Consumer<Boolean> callback) {
        MSenv msenvJson = JsonMSenv.getMSenv();
        if (msenvJson.equals(null)) {return;}

        String tkexpString = msenvJson.getValues().getExpiryDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date tkexpDate;
        try {
            tkexpDate = dateFormat.parse(tkexpString);
        } catch (ParseException e) {return;}

        Date currentDate = new Date();
        if ((tkexpDate.getTime() - currentDate.getTime()) <= 30000) { // min 30 sec until expirering
            MsAuth.tokenRT(success -> {
                if (success) {
                    refreshToDoList(empty -> {});
                } else if (!success) {
                    callback.accept(success);   
                }
            });

        } else {
            kong.unirest.HttpResponse<String> response = 
                Unirest.get(msenvJson.getReqCredentials().getUrl_graph() + "todo/lists/" + msenvJson.getReqCredentials().getToDoListID() + "/tasks?$filter=status ne 'completed'") // https://learn.microsoft.com/en-us/graph/query-parameters
                    .header("Authorization", (msenvJson.getValues().getTokenType() + " " + msenvJson.getValues().getToken()).toString())
                    .asString();

            ToDoHWTasks todohwtasks = new Gson().fromJson(response.getBody(), ToDoHWTasks.class);
            Boolean savedToDoHWtasks = JsonTodoHW.saveToDoHW(todohwtasks);
            if (savedToDoHWtasks) {
                Boolean success = true;
                callback.accept(success);
            } else if (!savedToDoHWtasks) {
                Boolean success = false;
                callback.accept(success);   
            }
        }
    }

    // Class Structure for todo/lists/tasks response
    public class ToDoHWTasks {
        private List<Task> value;

        public List<Task> getValue() {return value;}
    }
    private class Task {
        // private String importance;
        // private String status;
        private String title;
        // private String createdDateTime;
        // private String lastModifiedDateTime;
        // private Boolean hasAttachments;
        // private String id;
        private dueDateTime dueDateTime;

        public String getTitle() {return title;}
        public dueDateTime getDueDateTime() {return dueDateTime;}
    }
    private class dueDateTime {
        private String dateTime;
        private String timeZone;

        public String getDateTime() {return dateTime;}
        public String getTimeZone() {return timeZone;}
    }
}