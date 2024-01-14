package com.gbetododc.MSAuthGraph;

import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

import com.gbetododc.MSAuthGraph.MSenvJson.MSenv;

import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class MsGraph {
    public static void refreshToDoList() {
        MSenv msenvJson = MSenvJson.getMSenv();
        if (msenvJson.equals(null)) {return;}

        String tkexpString = msenvJson.getValues().getExpiryDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date tkexpDate;
        try {
            tkexpDate = dateFormat.parse(tkexpString);
        } catch (ParseException e) {return;}

        Date currentDate = new Date();
        if ((tkexpDate.getTime() - currentDate.getTime()) <= 30000) { // min 30 sec until expirering
            System.out.println("token will/is expire/d, requesting new one");
            MsAuth.tokenRT(success -> {
                if (success) {
                    System.out.println("successfully refresht token");
                    refreshToDoList();
                } else {
                    System.out.println("could not refresh the token");
                }
            });

        } else {
            kong.unirest.HttpResponse<String> response = 
                Unirest.get(msenvJson.getReqCredentials().getUrl_graph() + "todo/lists/" + msenvJson.getReqCredentials().getToDoListID() + "/tasks?$filter=status ne 'completed'") // https://learn.microsoft.com/en-us/graph/query-parameters
                    .header("Authorization", (msenvJson.getValues().getTokenType() + " " + msenvJson.getValues().getToken()).toString())
                    .asString();
        
            System.out.println(response.getBody());        
        }
    }

}