package app.com.bookdb.controller;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import app.com.bookdb.R;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AppConfig extends Application{

    private final String TAG = AppConfig.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static AppConfig instance;

    public static int LOGIN_URL  =      0;
    public static int LOGOUT_URL  =      1;
    public static int INSERT_BOOK_URL  = 2;
    public static int UPDATE_BOOK_URL  = 3;
    public static int DETAIL_BOOK_URL  = 4;
    public static int BOOK_LIST_URL    = 5;
    public static int PROFILE_USER_URL = 6;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        Realm.init(this);
        RealmConfiguration realmConfig= new RealmConfiguration.Builder()
                .name(getString(R.string.app_db_pref))
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        TypefaceUtil.overrideFont(getApplicationContext(),"Light","roboto.ttf");
    }

    public static synchronized AppConfig getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setShouldCache(false);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setShouldCache(false);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueueWith30ms(Request<T> req) {
        req.setTag(TAG);
        req.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,
                3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setShouldCache(false);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
