package com.example.testios;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testios.Services.MyService;
import com.example.testios.Services.RegisterAsSystemService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
Button on,off;
ImageView img;
Location location;
MyService myService = new MyService();
RegisterAsSystemService registerAsSystemService = new RegisterAsSystemService();
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        on = findViewById(R.id.Bon);
        off = findViewById(R.id.Boff);
        img = findViewById(R.id.img);
        // returns true if mock location enabled, false if not enabled.
       on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"benar on",Toast.LENGTH_LONG).show();
                Intent intent=new Intent("android.settings.NOTIFICATION_LISTENER_SETTINGS");

                img.setImageResource(R.drawable.ic_launcher_background);
                on.setVisibility(View.INVISIBLE);
                off.setVisibility(View.VISIBLE);
            }
        });
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setImageResource(R.drawable.ic_launcher_foreground);
                off.setVisibility(View.INVISIBLE);
                on.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"salah off",Toast.LENGTH_LONG).show();
            }
        });
        if (Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION).equals("0")){
            Toast.makeText(getApplicationContext(),"false",Toast.LENGTH_SHORT).show();

        }
           else {
            Toast.makeText(getApplicationContext(),"true",Toast.LENGTH_SHORT).show();

        }

    }
    public void isMockLocationEnabled() {
        try {
            //if marshmallow
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AppOpsManager opsManager = (AppOpsManager) getApplicationContext().getSystemService(Context.APP_OPS_SERVICE);
                Toast.makeText(getApplicationContext(), (CharSequence) opsManager,Toast.LENGTH_LONG).show();
            } else {
                // in marshmallow this will always return true
                Toast.makeText(getApplicationContext(), "no",Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyService ns = new MyService();
        RegisterAsSystemService.registererService(ns, this);

        if (!NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName())) {        //ask for permission
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivityForResult(intent, 101);


        }
    }
    private void Verifypermissions()
    {
        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0])== PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1])==PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2])==PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        else
        {
            ActivityCompat.requestPermissions(MainActivity.this,permissions,400);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Verifypermissions();
    }

}
