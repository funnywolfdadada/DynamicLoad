package com.funnywolf.host;

import android.content.Context;
import android.widget.Toast;

public class HostFunc {
    public static void invoke(Context context) {
        Toast.makeText(context, "Invoke From HostFunc", Toast.LENGTH_SHORT).show();
    }
}
