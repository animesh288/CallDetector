package com.android.calldetector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
    
public class CallDetector extends BroadcastReceiver
    {

        MainActivity mainActivity;
        String log="";
        String sheetname=MainActivity.name;
        String url=MainActivity.url;
        @Override
        public void onReceive(Context context, Intent intent) {
        TelephonyManager telephonyManager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)){
            new CountDownTimer(1000,1000){

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    log=getCallDetails(context.getApplicationContext());
                    Log.i("lag",log);
                    String a[]=log.split("break");
                    String ph=a[0];
                    String date=a[1];
                    Log.i("phone number",ph);
                    Log.i("Date",date);
                    addItemToSheet(ph,date,sheetname,url);

                }
            }.start();

        }
    }


        private String getCallDetails(Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(number);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String dir = null;

            stringBuffer.append(phNumber + "break" +callDayTime);
            cursor.close();
            return stringBuffer.toString();
        }
        return stringBuffer.toString();
    }
        private void addItemToSheet(String ph,String d,String sheetname,String url){
        String phone=ph;
        String date=d;

        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbxHYzwE_Tg90Woy3IOyOxnElN5JY-gTZDZjYba026ToM66WFHZT4Zls3h9daoyhp6DL/exec", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("action","addCaller");
                params.put("vPhone",phone);
                params.put("vDate",date);
                params.put("sheet",sheetname);
                params.put("url",url);

                return params;


            }
        };
        int socketTimeOut=5000;
        RetryPolicy retryPolicy=new DefaultRetryPolicy(socketTimeOut,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.getInstance());
        requestQueue.add(stringRequest);
    }
    }

