package com.smallbil.service;

import android.content.Context;
import android.content.SharedPreferences;

public final class LocalStorageService {
    public static SharedPreferences getPreferences(Context context, String settings) {
        return context.getSharedPreferences(settings, context.MODE_PRIVATE);
    }
}
