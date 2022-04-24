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
        PluginApkUtil.init(this);
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
        Resources resources = PluginApkUtil.getInstance().getResource();
        return resources == null ? super.getResources() : resources;
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = PluginApkUtil.getInstance().getTheme();
        return theme == null ? super.getTheme() : theme;
    }

    @Override
    public AssetManager getAssets() {
        AssetManager assetManager = PluginApkUtil.getInstance().getAssetManager();
        return assetManager == null ? super.getAssets() : assetManager;
    }

    private void loadApkPluginResource() {
        File apkPluginFile = new File(Environment.getExternalStorageDirectory() + "/app_02_debug.apk");
        if (apkPluginFile.exists()) {
            String resourcePath = apkPluginFile.getAbsolutePath();
            PluginApkUtil.getInstance().loadAppPluginResource(resourcePath);
        }
    }

}
