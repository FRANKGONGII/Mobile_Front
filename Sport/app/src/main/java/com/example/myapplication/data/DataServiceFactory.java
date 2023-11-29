package com.example.myapplication.data;

import android.os.Bundle;

public class DataServiceFactory {
    private static DataService dataService = null;

    public static DataService getInstance() {
        if(dataService == null){
            dataService = new RemoteData();
        }
        return dataService;
    }
}
