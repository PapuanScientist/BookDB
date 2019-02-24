package app.com.bookdb.controller;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class BaseActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    protected native String ipin(String piss);
    protected native String upin(String pass);
    protected native String hola(int paraondeir);


    protected SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager();

        doRequestPermissions();
    }

    protected Snackbar displaySnackbar(View view, String message, int duration, String titleAction, View.OnClickListener listener){
        Snackbar snackbar = Snackbar.make(view,message,duration);
        snackbar.setAction(titleAction, listener);
        snackbar.show();
        return snackbar;
    }

    protected static int REQUEST_ALL_PERMISSIONS = 1;
    protected String[] PERMISSIONS = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
    };

    private boolean hasPermission(Context context,String... permissions){
        if (context != null && permissions != null) {
            for (String permission : permissions){
                if (ActivityCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void doRequestPermissions(){
        boolean valHasPermission = hasPermission(this,PERMISSIONS);
        SysLog.getInstance().sendLog(TAG,"has permission : "+valHasPermission);
        if (!valHasPermission) {
            SysLog.getInstance().sendLog(TAG,"dorequest permission");
            ActivityCompat.requestPermissions(this,PERMISSIONS,REQUEST_ALL_PERMISSIONS);
        }
    }

    private final String TAG = BaseActivity.class.getSimpleName();

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        SysLog.getInstance().sendLog(TAG,"onActivityResult requestCode : "+requestCode);
        if (requestCode == REQUEST_ALL_PERMISSIONS ){
            SysLog.getInstance().sendLog(TAG,"all permissions granted ");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    protected boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                SysLog.getInstance().sendLog(TAG,"isMyServiceRunning? "+true);
                return true;
            }
        }
        SysLog.getInstance().sendLog(TAG,"isMyServiceRunning? "+false);
        return false;
    }

}
