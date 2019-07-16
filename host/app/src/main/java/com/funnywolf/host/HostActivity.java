package com.funnywolf.host;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.FrameLayout;

import dalvik.system.DexClassLoader;

public class HostActivity extends AppCompatActivity {
    private static final String TAG = "HostActivity";

    public static final String EXTRA_PLUGIN_PATH = "EXTRA_PLUGIN_PATH";
    public static final String EXTRA_FRAGMENT_NAME = "EXTRA_FRAGMENT_NAME";
    public static final int LAYOUT_ID = HostActivity.class.hashCode();

    private DexClassLoader dexClassLoader;
    private AssetManager assetManager;
    private Resources resources;

    public static void start(Activity activity, String pluginPath, String fragmentName) {
        if (activity == null
                || TextUtils.isEmpty(pluginPath)
                || TextUtils.isEmpty(fragmentName)) {
            return;
        }
        Intent intent = new Intent(activity, HostActivity.class);
        intent.putExtra(EXTRA_PLUGIN_PATH, pluginPath);
        intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentName);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String pluginPath = intent.getStringExtra(EXTRA_PLUGIN_PATH);
        String fragmentName = intent.getStringExtra(EXTRA_FRAGMENT_NAME);
        if (TextUtils.isEmpty(pluginPath) || TextUtils.isEmpty(fragmentName)) {
            finish();
            return;
        }

        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(LAYOUT_ID);
        setContentView(frameLayout);

        try {
            dexClassLoader = new DexClassLoader(pluginPath, getDir("dex", Context.MODE_PRIVATE).getAbsolutePath(),
                    null, getClassLoader());
            assetManager = AssetManager.class.newInstance();
            AssetManager.class.getDeclaredMethod("addAssetPath", String.class)
                    .invoke(assetManager, pluginPath);
            resources = new Resources(assetManager, getResources().getDisplayMetrics(), getResources().getConfiguration());

            Fragment fragment = (Fragment) dexClassLoader.loadClass(fragmentName).newInstance();
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
        return dexClassLoader != null ? dexClassLoader : super.getClassLoader();
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
