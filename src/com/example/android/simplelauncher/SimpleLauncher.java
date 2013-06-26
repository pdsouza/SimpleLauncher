package com.example.android.simplelauncher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * SimpleLauncher: A super simple app launcher.
 */

public class SimpleLauncher extends Activity {

    private static final String LOG_TAG = "SimpleLauncher";

    private static final HashSet<String> DEFAULT_DOCK_APPS = new HashSet<String>(Arrays.asList(
                                                        "Phone", "Browser", "People", "Messaging" ));

    private ArrayList<LaunchableAppInfo> mApplications;
    private ArrayList<LaunchableAppInfo> mDockedApplications;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.simple_launcher);

        loadApps();

        bindApps();

        bindDockedApps();

    }

    /**
     * Populate a list of currently launchable applications on this device.
     */
    private void loadApps() {

        PackageManager packManager = getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final List<ResolveInfo> apps = packManager.queryIntentActivities(mainIntent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(packManager));

        if(mApplications == null)
            mApplications = new ArrayList<LaunchableAppInfo>(apps.size());
        else
            mApplications.clear();

        if(mDockedApplications == null)
            mDockedApplications = new ArrayList<LaunchableAppInfo>();

        if(apps != null) for (ResolveInfo qinfo : apps) {
            LaunchableAppInfo info = new LaunchableAppInfo(new ComponentName(
                                            qinfo.activityInfo.applicationInfo.packageName,
                                            qinfo.activityInfo.name));
            info.label = qinfo.loadLabel(packManager);
            info.icon  = qinfo.loadIcon(packManager);

            if(!mDockedApplications.contains(info) && DEFAULT_DOCK_APPS.contains(info.label))
                mDockedApplications.add(info);
            else
                mApplications.add(info);

            Log.v(LOG_TAG, info.toString());
        }

    }

    /**
     * Bind all launchable apps to the screen.
     */
    private void bindApps() {

        GridView gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(new ImageArrayAdapter(this, mApplications));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                LaunchableAppInfo info = (LaunchableAppInfo) parent.getItemAtPosition(position);
                startActivity(info.intent);
            }
        });

    }

    /**
     * Bind all docked apps to the dock.
     */
    private void bindDockedApps() {

        GridView dockView = (GridView) findViewById(R.id.dock_view);
        dockView.setAdapter(new ImageArrayAdapter(this, mDockedApplications));

        dockView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                LaunchableAppInfo info = (LaunchableAppInfo) parent.getItemAtPosition(position);
                startActivity(info.intent);
            }
        });

    }

    /**
     * Adapter to populate the launcher with launchable apps.
     */
    private class ImageArrayAdapter extends ArrayAdapter<LaunchableAppInfo> {

        public ImageArrayAdapter(Context context, ArrayList<LaunchableAppInfo> apps) {
            super(context, 0, apps);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LaunchableAppInfo app = getItem(position);
            Log.d(LOG_TAG, "Position " + position + ": " + app);
            ImageView imageView;

            if(convertView == null)
                imageView = new ImageView(this.getContext());
            else
                imageView = (ImageView) convertView;

            imageView.setImageDrawable(app.icon);
            return imageView;
        }

    }

}