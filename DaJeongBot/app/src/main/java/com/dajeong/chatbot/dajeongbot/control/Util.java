package com.dajeong.chatbot.dajeongbot.control;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Util {
    public static String getProperty(String key,Context context){
        Properties properties = new Properties();;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;

        try {
            inputStream = assetManager.open("config.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties.getProperty(key);

    }
}
