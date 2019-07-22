package com.funnywolf.host;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;

import com.funnywolf.host.utils.DexUtil;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dalvik.system.DexClassLoader;

public class PluginManager {
    private static final String TAG = "PluginManager";

    public static final boolean COMBINE_CLASSLOADER = true;

    private static Map<String, PluginInfo> sPluginMap = new ConcurrentHashMap<>();

    public static void loadPlugin(Context context, String name) {
        if (sPluginMap.containsKey(name)) {
            return;
        }
        context = context.getApplicationContext();
        String pluginPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "plugin" + File.separator + name + ".apk";
        File pluginFile = new File(pluginPath);
        if (!pluginFile.exists()) {
            return;
        }
        try {
            PluginInfo pluginInfo = new PluginInfo();
            pluginInfo.name = name;
            pluginInfo.classLoader = createClassLoader(context, pluginPath);
            pluginInfo.packageInfo = context.getPackageManager().getPackageArchiveInfo(pluginPath, PackageManager.GET_ACTIVITIES);
            pluginInfo.resources = createResources(context, pluginPath);
            sPluginMap.put(name, pluginInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ClassLoader createClassLoader(Context context, String pluginPath) throws Exception {
        DexClassLoader classLoader = new DexClassLoader(pluginPath, context.getDir("dex",
                Context.MODE_PRIVATE).getAbsolutePath(), null, context.getClassLoader());
        if (COMBINE_CLASSLOADER) {
            DexUtil.insertDex(classLoader, context.getClassLoader());
            return context.getClassLoader();
        }
        return classLoader;
    }

    private static Resources createResources(Context context, String pluginPath) throws Exception {
        AssetManager assetManager = createAssetManager(pluginPath);
        Resources hostResource = context.getResources();
        return new Resources(assetManager, hostResource.getDisplayMetrics(), hostResource.getConfiguration());
    }

    private static AssetManager createAssetManager(String pluginPath) throws Exception {
        AssetManager assetManager = AssetManager.class.newInstance();
        AssetManager.class.getDeclaredMethod("addAssetPath", String.class).invoke(assetManager, pluginPath);
        return assetManager;
    }

    public static PluginInfo getPlugin(String name) {
        return sPluginMap.get(name);
    }

    public static class PluginInfo {
        public String name;
        public ClassLoader classLoader;
        public Resources resources;
        public PackageInfo packageInfo;
    }
}
