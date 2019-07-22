package com.funnywolf.plugin.foo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnywolf.plugin.R;

public class FooFragment extends Fragment {
    private static final String TAG = "FooFragment";

    private TextView text;
    private ImageView image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_foo, container, false);
        Log.d(TAG, "onCreateView: " + v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        text = view.findViewById(R.id.text);
        image = view.findViewById(R.id.image);
        view.findViewById(R.id.load_self_res).setOnClickListener(v -> loadSelfRes());
        view.findViewById(R.id.access_host_func).setOnClickListener(v -> accessHostFunc());
        view.findViewById(R.id.access_bar_func).setOnClickListener(v -> accessBarFunc());
    }

    private void loadSelfRes() {
        text.setText(R.string.plugin_foo_text);
        image.setImageResource(R.drawable.plugin_foo_image);
    }

    private void accessHostFunc() {
        Log.d(TAG, "accessHostFunc: ");
        try {
            Class clazz = getActivity().getClassLoader().loadClass("com.funnywolf.host.HostFunc");
            clazz.getMethod("invoke", Context.class).invoke(clazz.newInstance(), getContext());
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void accessBarFunc() {
        Log.d(TAG, "accessBarFunc: ");
        try {
            Class clazz = getActivity().getClassLoader().loadClass("com.funnywolf.plugin.bar.BarFunc");
            clazz.getMethod("invoke", Context.class).invoke(null, getContext());
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

}
