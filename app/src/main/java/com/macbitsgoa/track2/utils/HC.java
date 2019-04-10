package com.macbitsgoa.track2.utils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HC {
    public static final String ID = "com.macbitsgoa.track2";

    public static final String DEFAULT_SP = "sp";

    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.BODY_SENSORS,
    };

    public static SharedPreferences getSharedPreferences(@NonNull final Context context) {
        return context.getSharedPreferences(DEFAULT_SP, Context.MODE_PRIVATE);
    }

    public static boolean deleteDirectory(@NonNull final String directoryPath) {
        final File directory = new File(directoryPath);
        if (!directory.exists()) {
            return true;
        }
        final String[] children = directory.list();
        boolean delete = true;
        for (String childFile : children) {
            delete &= new File(directory, childFile).delete();
        }
        return delete;
    }
}
