package com.example.guans.arrivied.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LocateIntentService extends IntentService implements AMapLocationListener{
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_LOCATE_ONE_TIME = "com.example.guans.arrivied.service.action.ONE_TIME";
    private static final String ACTION_LOCATE_ALLWAYS = "com.example.guans.arrivied.service.action.ALLWAYS";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.guans.arrivied.service.extra.LOCATE_ONE_TIME";
    private static final String EXTRA_PARAM2 = "com.example.guans.arrivied.service.extra.LOCATE_ALLWAYS";
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption = null;
    private Intent locationResultIntent;
    public static final String ACTION_LOCATION_RESULT="com.example.guan.arrived.loctionservice.RESULT_FIND";

    public LocateIntentService() {
        super("LocateIntentService");
        Log.e(getClass().getSimpleName(),"constructor");
        initLocationClient();
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startLocateOneTime(Context context, String param1, String param2) {
        Intent intent = new Intent(context, LocateIntentService.class);
        intent.setAction(ACTION_LOCATE_ONE_TIME);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startLocateAllways(Context context, String param1, String param2) {
        Intent intent = new Intent(context, LocateIntentService.class);
        intent.setAction(ACTION_LOCATE_ALLWAYS);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOCATE_ONE_TIME.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleLocateOneTime(param1, param2);
            } else if (ACTION_LOCATE_ALLWAYS.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleLocateAlways(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleLocateOneTime(String param1, String param2) {
        mLocationOption.setOnceLocation(true);
        mLocationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(mLocationOption);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleLocateAlways(String param1, String param2) {
        mLocationOption.setOnceLocation(false);
        mLocationOption.setNeedAddress(false);
        mLocationClient.setLocationOption(mLocationOption);
    }
    private void initLocationClient(){
        mLocationClient=new AMapLocationClient(super.getApplicationContext());
        mLocationOption=new AMapLocationClientOption();
        mLocationClient.setLocationListener(this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
//设置定位参数
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if(locationResultIntent==null){
            locationResultIntent=new Intent(ACTION_LOCATION_RESULT);
        }
//            if (amapLocation.getErrorCode() == 0) {
//                //定位成功回调信息，设置相关消息
//                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                amapLocation.getLatitude();//获取纬度
//                amapLocation.getLongitude();//获取经度
//                amapLocation.getAccuracy();//获取精度信息
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(amapLocation.getTime());
//                df.format(date);//定位时间
//            } else {
//                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                Log.e("AmapError","location Error, ErrCode:"
//                        + amapLocation.getErrorCode() + ", errInfo:"
//                        + amapLocation.getErrorInfo());
//            }
            locationResultIntent.putExtra("LocationResult",amapLocation);
            sendBroadcast(locationResultIntent);
        }
    }

    @Override
    public void onDestroy() {
        if(mLocationClient!=null){
            mLocationClient.unRegisterLocationListener(this);
            mLocationClient.onDestroy();
        }
        Log.e(getClass().getSimpleName(),"Destory");
        super.onDestroy();
    }
}
