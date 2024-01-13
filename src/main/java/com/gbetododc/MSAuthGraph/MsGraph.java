package com.gbetododc.MSAuthGraph;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gbetododc.MSAuthGraph.MSenvJson.MSenv;

public class MsGraph {
    public static void refreshToDoList() {
        MSenv msenvJson = MSenvJson.getMSenv();
        if (msenvJson.equals(null)) {return;}

        String tkexpString = msenvJson.getValues().getExpiryDate();
        System.out.println(tkexpString);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date tkexpDate;
        try {
            tkexpDate = dateFormat.parse(tkexpString);
        } catch (ParseException e) {return;}

        Date currentDate = new Date();
        System.out.println(tkexpDate.getTime() - currentDate.getTime());
        if ((tkexpDate.getTime() - currentDate.getTime()) <= 30000) {
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
            System.out.println("token will last some more time");
            // TODO: MSGraph API Unirest
        }
    }
}