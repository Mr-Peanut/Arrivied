package com.example.guans.arrivied.view;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.example.guans.arrivied.R;
import com.example.guans.arrivied.bean.LocationClient;
import com.example.guans.arrivied.service.LocateService;
import com.example.guans.arrivied.receiver.LocationReceiver;
import com.example.guans.arrivied.util.LOGUtil;

public class MainActivity extends AppCompatActivity implements LocationReceiver.LocationReceiveListener {
    private LocationReceiver receiver;
    private LocationClient locationClient;
    private ServiceConnection locationServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initReceiver();
        bindLocationService();
        findViewById(R.id.city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationClient.startLocateOneTime();
            }
        });

//        LocateIntentService.startLocateOneTime(this,null,null);
    }

    private void bindLocationService() {
        LOGUtil.logE(this, "bindLocationService");
        Intent locationIntent = new Intent(this, LocateService.class);
        locationIntent.setAction(LocateService.ACTION_LOCATION_BIND);
        locationServiceConnection = new LocationServiceConnection();
        bindService(locationIntent, locationServiceConnection, Service.BIND_AUTO_CREATE);
    }

    private void initReceiver() {
        if (receiver==null)
            receiver=new LocationReceiver();
        receiver.setLocationListener(this);
        IntentFilter intentFilter = new IntentFilter(LocateService.ACTION_LOCATION_RESULT);
        registerReceiver(receiver,intentFilter);
    }


    @Override
    public void onBroadcastReceive(Intent intent) {
        LOGUtil.logE(this, "onLocationResultReceive");
        AMapLocation result=intent.getParcelableExtra("LocationResult");
        TextView textView = findViewById(R.id.city);
        textView.setText(result.getAddress());
    }
    @Override
    protected void onDestroy() {
        if (receiver != null) {
            receiver.setLocationListener(null);
            unregisterReceiver(receiver);
        }
        unbindService(locationServiceConnection);
        super.onDestroy();
    }

    class LocationServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            locationClient = (LocationClient) iBinder;
            LOGUtil.logE(this, "onServiceConnection");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            locationClient = null;
        }
    }
}
