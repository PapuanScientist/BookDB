package app.com.bookdb.controller;

import android.os.Handler;
import android.view.View;

import app.com.bookdb.model.Credential;


public class SessionManager {

    private DBEngine engine;
    private String TAG = DBEngine.class.getSimpleName();

    public SessionManager(){
        this.engine = new DBEngine();
        SysLog.getInstance().sendLog(TAG,"session initial on costructor");
    }

    public boolean resetCrendetials(Credential userCredentials){
        return (engine.delete(userCredentials) == DBEngine.SUCCESS) ;
    }

    public synchronized void updateCrendetials(Credential userCredentials, View.OnClickListener onSuccess, View.OnClickListener onError){
        engine.update(userCredentials,onSuccess,onError) ;
    }

    public void setCredentials(Credential userCredentials,boolean autoLogin,Runnable runAction){
        if (runAction == null && autoLogin) {
            throw new NullPointerException("runEvent is null, please correct the code");
        }
        int insertVal = this.engine.insert(userCredentials);
        SysLog.getInstance().sendLog(TAG,"insert value : "+insertVal);
        if (insertVal == DBEngine.SUCCESS ) {
            SysLog.getInstance().sendLog(TAG," doLogin");
            new Handler().postDelayed(runAction,0);
        }
    }

    public Credential getCurrentUser(){
        return engine.getCurrentUser();
    }

    public boolean hasLogin(){
        return this.engine.countRows() > 0;
    }






}
