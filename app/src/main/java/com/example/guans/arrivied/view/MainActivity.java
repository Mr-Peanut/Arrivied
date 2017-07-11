package com.example.guans.arrivied.view;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.example.guans.arrivied.R;
import com.example.guans.arrivied.service.LocateIntentService;
import com.example.guans.arrivied.service.LocationReceiver;

public class MainActivity extends AppCompatActivity implements LocationReceiver.LocationReceiveListener {
    private LocationReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initReceiver();
        LocateIntentService.startLocateOneTime(this,null,null);
    }

    private void initReceiver() {
        if (receiver==null)
            receiver=new LocationReceiver();
        IntentFilter intentFilter=new IntentFilter(LocateIntentService.ACTION_LOCATION_RESULT);
        registerReceiver(receiver,intentFilter);
    }


    @Override
    public void onBroadcastReceive(Intent intent) {
        AMapLocation result=intent.getParcelableExtra("LocationResult");
        TextView textView=(TextView) findViewById(R.id.text);
        textView.setText(result.getAddress());
    }
    @Override
    protected void onDestroy() {
        if(receiver!=null)
            unregisterReceiver(receiver);
        super.onDestroy();
    }
}
