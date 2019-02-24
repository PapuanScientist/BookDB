package app.com.bookdb.controller;

import android.view.View;
import app.com.bookdb.model.Credential;
import io.realm.Realm;

public class DBEngine {
    public final static int SUCCESS = 0;
    public final static int FAIL = -1;

    private Realm realm;

    public DBEngine() {
        realm = Realm.getDefaultInstance();
    }

    private String TAG = DBEngine.class.getSimpleName();

    public int insert(Credential user) {
        realm.beginTransaction();
        Number maxId = realm.where(Credential.class).max("id");
        int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
        user.setId(nextId);

        realm.insert(user);
        realm.commitTransaction();

        realm.beginTransaction();
        int val = (realm.where(Credential.class).equalTo("id", nextId).findFirst() != null) ? SUCCESS : FAIL;
        SysLog.getInstance().sendLog(TAG, "value insert : " + val);
        realm.commitTransaction();

        return val;
    }

    private Credential user;

    public synchronized Credential getCurrentUser() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user = realm.where(Credential.class).findFirst();
            }
        });

        return user;
    }

    public int delete(Credential user) {
        int idDelete = user.getId();
        realm.beginTransaction();

        realm.delete(Credential.class);
        realm.commitTransaction();

        realm.beginTransaction();
        int val = (realm.where(Credential.class).equalTo("id",idDelete).findFirst() == null) ? SUCCESS : FAIL;
        SysLog.getInstance().sendLog(TAG, "val delete : " + val + " = " + SUCCESS);
        realm.commitTransaction();

        return val;
    }

    public synchronized void update(final Credential userUpdate,final View.OnClickListener onSuccess,final View.OnClickListener onError) {
        final int idUpdate = userUpdate.getId();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(userUpdate);
            }
        },new Realm.Transaction.OnSuccess(){
            @Override
            public void onSuccess() {
                onSuccess.onClick(null);
            }
        },new Realm.Transaction.OnError(){
            @Override
            public void onError(Throwable error) {
                onError.onClick(null);
            }
        });
    }


    public int countRows() {
        realm.beginTransaction();
        Number maxId = realm.where(Credential.class).max("id");

        int countRows = (maxId == null) ? 0 : maxId.intValue();
        realm.commitTransaction();

        return countRows;
    }

}
