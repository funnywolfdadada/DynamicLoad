package com.funnywolf.host;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.FrameLayout;

public class HostActivity extends AppCompatActivity {
    private static final String TAG = "HostActivity";

    public static final String EXTRA_PLUGIN_NAME = "EXTRA_PLUGIN_NAME";
    public static final String EXTRA_FRAGMENT_NAME = "EXTRA_FRAGMENT_NAME";
    public static final int LAYOUT_ID = HostActivity.class.hashCode();

    private ClassLoader classLoader;
    private AssetManager assetManager;
    private Resources resources;

    public static void start(Activity activity, String pluginName, String fragmentName) {
        if (activity == null
                || TextUtils.isEmpty(pluginName)
                || TextUtils.isEmpty(fragmentName)) {
            return;
        }
        Intent intent = new Intent(activity, HostActivity.class);
        intent.putExtra(EXTRA_PLUGIN_NAME, pluginName);
        intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentName);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String pluginName = intent.getStringExtra(EXTRA_PLUGIN_NAME);
        String fragmentName = intent.getStringExtra(EXTRA_FRAGMENT_NAME);
        PluginManager.PluginInfo pluginInfo = PluginManager.getPlugin(pluginName);
        if (TextUtils.isEmpty(pluginName) || TextUtils.isEmpty(fragmentName) || pluginInfo == null) {
            finish();
            return;
        }
        classLoader = pluginInfo.classLoader;
        assetManager = pluginInfo.resources.getAssets();
        resources = pluginInfo.resources;

        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(LAYOUT_ID);
        setContentView(frameLayout);

        try {
            Fragment fragment = (Fragment) classLoader.loadClass(fragmentName).newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(LAYOUT_ID, fragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader != null ? classLoader : super.getClassLoader();
    }

    @Override
    public AssetManager getAssets() {
        return assetManager != null ? assetManager : super.getAssets();
    }

    @Override
    public Resources getResources() {
        return resources != null ? resources : super.getResources();
    }
}
