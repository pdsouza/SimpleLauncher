package com.example.android.simplelauncher;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by chaoticryld on 6/17/13.
 */
public class LaunchableAppInfo {

    Intent intent;

    CharSequence label;

    Drawable icon;

    public LaunchableAppInfo(ComponentName appName) {
        intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(appName);
    }

    @Override
    public String toString() {
        return "LaunchableAppInfo{" +
                "intent=" + intent +
                ", label=" + label +
                ", icon=" + icon +
                '}';
    }
}
