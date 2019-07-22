package com.funnywolf.plugin.bar;

import android.content.Context;
import android.widget.Toast;

public class BarFunc {
    public static void invoke(Context context) {
        Toast.makeText(context, "Invoke From BarFunc", Toast.LENGTH_SHORT).show();
    }
}
