package com.appindia.butterDictionary;

import android.app.Application;
import android.content.res.Resources;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class App extends Application {

    private static App mInstance;
    private static Resources resources;
    private static FileOutputStream fileOutputStream;
    private static String fileName;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        resources = getResources();
        try {
            fileOutputStream = openFileOutput(fileName, MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static App getInstance() {
        return mInstance;
    }

    public static Resources getRes() {
        return resources;
    }

    public static FileOutputStream openFileOut(String name) {
        fileName = name;
        return fileOutputStream;
    }

}
