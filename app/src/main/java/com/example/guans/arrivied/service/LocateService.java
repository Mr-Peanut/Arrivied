package com.example.guans.arrivied.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class LocateService extends Service {
    public LocateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LocationClient();

    }
    private class LocationClient extends Binder{

    }
}
