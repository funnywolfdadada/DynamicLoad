package com.funnywolf.host;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        findViewById(R.id.load_plugin).setOnClickListener(v -> loadPlugin());
    }

    private void loadPlugin() {
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
