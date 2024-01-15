package com.gbetododc.MSAuthGraph;

import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import com.gbetododc.MSAuthGraph.JsonMSenv.MSenv;
import com.gbetododc.MSAuthGraph.MsAuth.TKerrorResp;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

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
            kong.unirest.HttpResponse<String>  response;
            try {
                response = 
                Unirest.get(msenvJson.getReqCredentials().getUrl_graph() + "todo/lists/" + msenvJson.getReqCredentials().getToDoListID() + "/tasks?$filter=status ne 'completed'") // https://learn.microsoft.com/en-us/graph/query-parameters
                    .header("Authorization", (msenvJson.getValues().getTokenType() + " " + msenvJson.getValues().getToken()).toString())
                    .asString();
            } catch (Throwable e) {
                System.out.println(e);
                Boolean success = false;
                callback.accept(success);
                return;
            }


            Integer responseCode = response.getStatus();
            String responseBody = response.getBody().toString();
            System.out.println(responseCode);
            switch (responseCode) {
                case 200:
                    ToDoHWTasks todohwtasks = new Gson().fromJson(responseBody, ToDoHWTasks.class);
                    Boolean savedToDoHWtasks = JsonTodoHW.saveToDoHW(todohwtasks);
                    if (savedToDoHWtasks) {
                        Boolean success = true;
                        callback.accept(success);
                    } else if (!savedToDoHWtasks) {
                        Boolean success = false;
                        callback.accept(success);   
                    }
                    break;
                case 400:
                // TODO hier ist ein erro oder so
                System.out.pritnln()
                    Gson gson = new Gson();
                    GraphErrorResp errorResp = gson.fromJson(tkexpString, GraphErrorResp.class);
                    // Logger.log(
                    //     "MsAuth - RFTokenRq", 
                    //     errorResp.getError().getInnerError().getDate() + " " + errorResp.getError().getCode() + "\n" + 
                    //     errorResp.getError().getMessage(),
                    //     LogLvl.critical
                    // );
                    Boolean success = false;
                    callback.accept(success);
                    break;
                default:
                    success = false;
                    callback.accept(success);
                    Logger.log("MsAuth - TokenReq with RF Token", "Error " + responseCode + "\n" + responseBody, LogLvl.critical);
                    break;
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
