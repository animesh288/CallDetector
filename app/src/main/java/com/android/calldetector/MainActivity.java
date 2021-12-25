package com.android.calldetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.os.PowerManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private static MainActivity instance;
    Button start;
    EditText editText,spreadSheet;
    TextView current;
    public static String name="";
    public static String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance=this;
        current=findViewById(R.id.current);
        start=findViewById(R.id.launch);
        editText=findViewById(R.id.sheetname);
        spreadSheet=findViewById(R.id.spreadsheetUrl);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=editText.getText().toString();
                url=spreadSheet.getText().toString();
                editText.setText("");
                spreadSheet.setText("");
                current.setText("Current Url : "+url+"\n\nCurrent sheet : "+name);
            }
        });
        current.setText("Current sheet : "+name);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS

        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

    }

    public static MainActivity getInstance(){
        return instance;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}