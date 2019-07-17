package com.funnywolf.host;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        textView = findViewById(R.id.text_view);
        imageView = findViewById(R.id.image_view);
        findViewById(R.id.load_plugin).setOnClickListener(v -> loadPlugin());
        findViewById(R.id.start_plugin).setOnClickListener(v -> startPlugin());
    }

    private void loadPlugin() {
        String pluginPath = getPluginPath();
        if (pluginPath == null) {
            Toast.makeText(this, "找不到插件", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            PackageInfo packageInfo = getPackageManager().getPackageArchiveInfo(pluginPath, PackageManager.GET_ACTIVITIES);
            Resources resources = getPackageManager().getResourcesForApplication(packageInfo.applicationInfo);
            AssetManager assetManager = resources.getAssets();
            AssetManager.class.getDeclaredMethod("addAssetPath", String.class)
                    .invoke(assetManager, pluginPath);
            int textId = resources.getIdentifier("plugin_show_text", "string", packageInfo.packageName);
            int imageId = resources.getIdentifier("plugin_image", "drawable", packageInfo.packageName);
            textView.setText(resources.getText(textId));
            imageView.setImageDrawable(resources.getDrawable(imageId, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startPlugin() {
        String pluginPath = getPluginPath();
        if (pluginPath == null) {
            Toast.makeText(this, "找不到插件", Toast.LENGTH_SHORT).show();
            return;
        }
        HostActivity.start(this, pluginPath, "com.funnywolf.plugin.PluginFragment");
    }

    private String getPluginPath() {
        String pluginPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "plugin.apk";
        File plugin = new File(pluginPath);
        return plugin.exists() ? pluginPath : null;
    }
}
