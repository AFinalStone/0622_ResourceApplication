package com.afs.resource;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;

import java.io.File;

public class ResApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        PluginApkUtilV2.init(this);
        loadApkPluginResource();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String app_name = getResources().getString(R.string.app_name);
        System.out.println("ResApplication==" + app_name);
    }

    @Override
    public Resources getResources() {
        Resources resources = PluginApkUtilV2.getInstance().getResource();
        return resources == null ? super.getResources() : resources;
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = PluginApkUtilV2.getInstance().getTheme();
        return theme == null ? super.getTheme() : theme;
    }

    @Override
    public AssetManager getAssets() {
        AssetManager assetManager = PluginApkUtilV2.getInstance().getAssetManager();
        return assetManager == null ? super.getAssets() : assetManager;
    }

    private void loadApkPluginResource() {
        File apkPluginFile = new File(Environment.getExternalStorageDirectory() + "/app_resource.zip");
        if (apkPluginFile.exists()) {
            String resourcePath = apkPluginFile.getAbsolutePath();
            PluginApkUtilV2.getInstance().loadAppPluginResource(resourcePath);
        }
    }

}
