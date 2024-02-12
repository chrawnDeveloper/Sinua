package com.conde.cc_contadordecalorias.ui.home;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
public class Util {
    public static boolean validate(AppCompatActivity activity, int requestCode, String[] permissions) {
        List<String> list = new ArrayList<String>();
        for (String permission : permissions) {
            boolean ok = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
            if (!ok) { // se for false
                list.add(permission);
            }
        }
        if (list.isEmpty()) {
            return true;
        }
        String[] newPermissions = new String[list.size()];
        list.toArray(newPermissions);
//solicita a permissao
        ActivityCompat.requestPermissions(activity, newPermissions, requestCode);
        return false;
    }
}
