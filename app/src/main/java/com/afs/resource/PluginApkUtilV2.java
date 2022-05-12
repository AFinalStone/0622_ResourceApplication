package com.afs.resource;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.afs.resource.res.TinkerResourcePatcher;

public class PluginApkUtilV2 {


    private static PluginApkUtilV2 INSTANCE;
    private Application mApplication;
    private AssetManager mAssetManager;
    private Resources mResource;
    private Resources.Theme mTheme;

    private PluginApkUtilV2(Application application) {
        mApplication = application;
    }

    public static void init(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new PluginApkUtilV2(application);
        }
    }

    public static PluginApkUtilV2 getInstance() {
        return INSTANCE;
    }

    /**
     * @param apkPluginPath
     */
    public void loadAppPluginResource(String apkPluginPath) {
        try {
            TinkerResourcePatcher.isResourceCanPatch(mApplication);
            TinkerResourcePatcher.monkeyPatchExistingResources(mApplication, apkPluginPath);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public AssetManager getAssetManager() {
        return mAssetManager;
    }

    public Resources getResource() {
        return mResource;
    }

    public Resources.Theme getTheme() {
        return mTheme;
    }

}
