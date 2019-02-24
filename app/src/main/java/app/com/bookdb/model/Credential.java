package app.com.bookdb.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Credential extends RealmObject {

    @PrimaryKey
    private int id;

    @Required
    private String user_active_status;
    @Required
    private String user_fullname;
    @Required
    private String user_id;
    @Required
    private String user_username;

    @Required
    private String token;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setUserActiveStatus(String user_active_status) {
        this.user_active_status = user_active_status;
    }

    public String getUserActiveStatus() {
        return user_active_status;
    }

    public void setUserFullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }

    public String getUserFullname() {
        return user_fullname;
    }

    public void setUserUsername(String user_username) {
        this.user_username = user_username;
    }

    public String getUserUsername() {
        return user_username;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getUserId() {
        return user_id;
    }
}