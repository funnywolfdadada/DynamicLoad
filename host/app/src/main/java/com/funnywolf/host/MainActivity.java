package com.funnywolf.host;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView text;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PluginManager.loadPlugin(this, "foo");
        PluginManager.loadPlugin(this, "bar");
        HostFunc.invoke(this);
        initViews();
    }

    private void initViews() {
        text = findViewById(R.id.text);
        image = findViewById(R.id.image);
        findViewById(R.id.load_self_res).setOnClickListener(v -> loadSelfRes());
        findViewById(R.id.load_foo_res).setOnClickListener(v -> loadFooRes());
        findViewById(R.id.load_bar_res).setOnClickListener(v -> loadBarRes());
        findViewById(R.id.access_foo_func).setOnClickListener(v -> accessFooFunc());
        findViewById(R.id.access_bar_func).setOnClickListener(v -> accessBarFunc());
        findViewById(R.id.start_foo).setOnClickListener(v -> startFoo());
        findViewById(R.id.start_bar).setOnClickListener(v -> startBar());
    }

    private void loadSelfRes() {
        text.setText(R.string.host_text);
        image.setImageResource(R.drawable.host_image);
    }

    private void loadFooRes() {
        text.setText(PluginUtils.getString("foo", "plugin_foo_text"));
        image.setImageDrawable(PluginUtils.getDrawable("foo", "plugin_foo_image"));
    }

    private void loadBarRes() {
        text.setText(PluginUtils.getString("bar", "plugin_bar_text"));
        image.setImageDrawable(PluginUtils.getDrawable("bar", "plugin_bar_image"));
    }

    private void accessFooFunc() {
        PluginManager.PluginInfo pluginInfo = PluginManager.getPlugin("foo");
        if (pluginInfo == null) { return; }
        try {
            Class clazz = pluginInfo.classLoader.loadClass("com.funnywolf.plugin.foo.FooFunc");
            clazz.getMethod("invoke", Context.class).invoke(clazz.newInstance(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void accessBarFunc() {
        PluginManager.PluginInfo pluginInfo = PluginManager.getPlugin("bar");
        if (pluginInfo == null) { return; }
        try {
            Class clazz = pluginInfo.classLoader.loadClass("com.funnywolf.plugin.bar.BarFunc");
            clazz.getMethod("invoke", Context.class).invoke(null, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startFoo() {
        HostActivity.start(this, "foo", "com.funnywolf.plugin.foo.FooFragment");
    }

    private void startBar() {
        HostActivity.start(this, "bar", "com.funnywolf.plugin.bar.BarFragment");
    }

}
