package com.funnywolf.host;

import android.graphics.drawable.Drawable;

public class PluginUtils {
    public static CharSequence getString(String pluginName, String resName) {
        PluginManager.PluginInfo pluginInfo = PluginManager.getPlugin(pluginName);
        if (pluginInfo == null) {
            return null;
        }
        try {
            return pluginInfo.resources.getText(getId(pluginInfo, "string", resName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Drawable getDrawable(String pluginName, String resName) {
        PluginManager.PluginInfo pluginInfo = PluginManager.getPlugin(pluginName);
        if (pluginInfo == null) {
            return null;
        }
        try {
            return pluginInfo.resources.getDrawable(getId(pluginInfo, "drawable", resName), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getId(PluginManager.PluginInfo pluginInfo, String resType, String resName) {
        if (pluginInfo == null) {
            return 0;
        }
        return pluginInfo.resources.getIdentifier(resName, resType, pluginInfo.packageInfo.packageName);
    }

    public static int getId(String pluginName, String resType, String resName) {
        return getId(PluginManager.getPlugin(pluginName), resType, resName);
    }


}
