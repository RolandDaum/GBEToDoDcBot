package com.gbetododc.MSAuthGraph;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import com.gbetododc.MSAuthGraph.JsonMSenv.MSenv;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import kong.unirest.Unirest;

public class MsGraph {
    /**
     * Refreshing the todoHW.json file
     * @param Boolean callback
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
            MsAuth.tokenRT(successTRF -> {
                if (successTRF) {
                    Logger.log("MsGraph - refreshToDoList", "restarting ToDo refresh after successfully refreshing old token", LogLvl.normale);
                    MsGraph.refreshToDoList(empty -> {
                        callback.accept(empty);
                    });
                } else if (!successTRF) {
                    callback.accept(false);   
                }
            });

        } else {
            kong.unirest.HttpResponse<String> response = Unirest.get(msenvJson.getReqCredentials().getUrl_graph() + "todo/lists/" + msenvJson.getReqCredentials().getToDoListID() + "/tasks?$filter=status ne 'completed'") // https://learn.microsoft.com/en-us/graph/query-parameters
                    .header("Authorization", (msenvJson.getValues().getTokenType() + " " + msenvJson.getValues().getToken()).toString())
                    .asString();

            String responseBody = response.getBody().toString();
            Integer responseCode = response.getStatus();
            
            switch (responseCode) {
                case 200:
                    ToDoHWTasks todohwtasks = new Gson().fromJson(responseBody, ToDoHWTasks.class);
                    Boolean savedToDoHWtasks = JsonTodoHW.saveToDoHW(todohwtasks);
                    callback.accept(savedToDoHWtasks);
                    break;
                case 400:
                    GraphErrorResp errorResp = new Gson().fromJson(responseBody, GraphErrorResp.class);
                    Logger.log(
                        "MsAuth - refreshToDoList", 
                        errorResp.getError().getInnerError().getDate() + " " + errorResp.getError().getCode() + " " + errorResp.getError().getMessage(),
                        LogLvl.critical
                    );
                    callback.accept(false);
                    break;
                case 401:
                    Logger.log("MsGraph - refreshToDoList", "refreshing Token set because of an invalid token", LogLvl.normale);
                    MsAuth.tokenRT(success -> {
                        if (success) {
                            refreshToDoList(successToDoRF -> {
                                callback.accept(successToDoRF);
                            });
                        } else if (!success) {
                            callback.accept(false);
                        }
                    });
                    break;
                default:
                    callback.accept(false);
                    Logger.log("MsAuth - TokenReq with RF Token", "Error " + responseCode + "\n" + responseBody, LogLvl.critical);
                    break;
            }
        }
    }

    // Class Structure for todo/lists/tasks response
    public class ToDoHWTasks {
        private List<ToDoHW_Task> value;

        public List<ToDoHW_Task> getValue() {return value;}
    }
    public class ToDoHW_Task {
        // private String importance;
        // private String status;
        private String title;
        // private String createdDateTime;
        // private String lastModifiedDateTime;
        // private Boolean hasAttachments;
        // private String id;
        private ToDoHW_dueDateTime dueDateTime;

        public String getTitle() {return title;}
        public ToDoHW_dueDateTime getDueDateTime() {return dueDateTime;}
    }
    public class ToDoHW_dueDateTime {
        private String dateTime;
        private String timeZone;

        public String getDateTime() {return dateTime;}
        public String getTimeZone() {return timeZone;}
    }

    // Class Structure for todo/lists/tasks error response
    public class GraphErrorResp {
        private Error error;

        public Error getError() {return this.error;}
    }
    private class Error {
        private String code;
        private String message;
        private InnerError innerError;

        public String getCode() {return this.code;}
        public String getMessage() {return this.message;}
        public InnerError getInnerError() {return this.innerError;}
    }
    private class InnerError {
        private String code;
        private String date;
        @SerializedName("request-id")
        private String requestid;
        @SerializedName("client-request-id")
        private String clientrequestid;

        public String getCode() {return this.code;}
        public String getDate() {return this.date;}
        public String getRequestId() {return this.requestid;}
        public String getClientRequestId() {return this.clientrequestid;}
    }

}
