package app.com.bookdb.view;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.com.bookdb.R;
import app.com.bookdb.controller.AppConfig;
import app.com.bookdb.controller.BaseActivity;
import app.com.bookdb.controller.SysLog;
import app.com.bookdb.model.Credential;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_username_field)AppCompatEditText usernameField;
    @BindView(R.id.login_password_field)EditTextPassword passwordField;
    @BindView(R.id.login_btn)AppCompatButton loginBtn;
    @BindView(R.id.login_progress)ProgressBar loginProgress;
    @BindView(R.id.display_message_error)AppCompatTextView displayMessageError;

    private final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (sessionManager.hasLogin()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @OnClick(R.id.login_btn)
    void onClick(View view){
        doLogin();
    }


    private void doLogin() {
        boolean fieldEmpty = false;
        if (usernameField.getText().toString().length() == 0 || passwordField.getText().toString().length() == 0) {
            usernameField.setError("Harus diisi");
            usernameField.requestFocus();
            passwordField.setError("Harus diisi");
            fieldEmpty =true;
        } else if (usernameField.getText().toString().length() == 0) {
            usernameField.setError("Harus diisi");
            usernameField.requestFocus();
            fieldEmpty = true;
        } else if (passwordField.getText().toString().length() == 0) {
            passwordField.setError("Harus diisi");
            passwordField.requestFocus();
            fieldEmpty = true;
        }
        if (!fieldEmpty) {
            loginBtn.setVisibility(View.INVISIBLE);
            loginProgress.setVisibility(View.VISIBLE);

            StringRequest request = new StringRequest(Request.Method.POST,upin(hola(AppConfig.LOGIN_URL)), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    SysLog.getInstance().sendLog(TAG, "response : " + response);
                    try {
                        loginProgress.setVisibility(View.INVISIBLE);
                        loginBtn.setVisibility(View.VISIBLE);
                        JSONObject resp = new JSONObject(response);
                        if (resp.getInt("status_code") == 200) {
                            JSONObject data = resp.getJSONObject("data");
                            Credential credential = new Credential();



                            credential.setToken(data.getString("token"));

                            JSONObject user = data.getJSONObject("user");
                            credential.setUserActiveStatus(user.getString("active"));
                            credential.setUserFullname(user.getString("fullname"));
                            credential.setUserId(user.getString("id"));
                            credential.setUserFullname(user.getString("username"));

                            SysLog.getInstance().sendLog(TAG,"token : "+data.getString("token"));
                            SysLog.getInstance().sendLog(TAG,"active : "+user.getString("active"));
                            SysLog.getInstance().sendLog(TAG,"fullname : "+user.getString("fullname"));
                            SysLog.getInstance().sendLog(TAG,"id : "+user.getString("id"));
                            SysLog.getInstance().sendLog(TAG,"username : "+user.getString("username"));

                            sessionManager.setCredentials(
                                    credential,
                                    true,
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            SysLog.getInstance().sendLog(TAG, "onRunnable method run, doLogin Action ");

                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    });
                        } else if (resp.getInt("status") == 400) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loginBtn.setVisibility(View.VISIBLE);

                                    final Snackbar snackbar = Snackbar.make(loginBtn, getString(R.string.app_error_username_password_not_match), Snackbar.LENGTH_INDEFINITE);
                                    snackbar.setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            snackbar.dismiss();
                                        }
                                    });
                                    snackbar.show();
                                }
                            }, 0);
                        }
                    } catch (JSONException ex) {
                        SysLog.getInstance().sendLog(TAG, "ex : " + ex.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    SysLog.getInstance().sendLog(TAG, "error volley : " + error.getMessage());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loginBtn.setVisibility(View.VISIBLE);
                            try {
                                if (error.getMessage().contains("java.net.ConnectException:")) {
                                    final Snackbar snackbar = Snackbar.make(loginBtn, getString(R.string.app_error_no_network), Snackbar.LENGTH_INDEFINITE);
                                    snackbar.setAction(getString(R.string.app_ok_command), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            snackbar.dismiss();
                                        }
                                    });
                                    snackbar.show();
                                }
                            }catch (NullPointerException ex){
                                SysLog.getInstance().sendLog(TAG,"");
                            }finally {
                                    loginProgress.setVisibility(View.INVISIBLE);
                                   displayMessageError.setText(getString(R.string.app_double_check_your_input));

                            }
                        }
                    }, 2000);

                }
            });
            Map<String, String> params = new HashMap<>();
            params.put("username", usernameField.getText().toString());
            params.put("password", passwordField.getText().toString());
            request.setParams(params);


            AppConfig.getInstance().addToRequestQueue(request);
        }
    }

}
