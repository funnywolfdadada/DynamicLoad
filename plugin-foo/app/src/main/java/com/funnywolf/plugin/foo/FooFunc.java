package com.funnywolf.plugin.foo;

import android.content.Context;
import android.widget.Toast;

public class FooFunc {
    public static void invoke(Context context) {
        Toast.makeText(context, "Invoke From FooFunc", Toast.LENGTH_SHORT).show();
    }
}
