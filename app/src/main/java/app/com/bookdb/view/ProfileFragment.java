package app.com.bookdb.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import app.com.bookdb.controller.SysLog;
import app.com.bookdb.model.Book;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @BindView(R.id.profile_display_name)AppCompatTextView displayNameField;

    @BindView(R.id.profile_fullname_field)AppCompatTextView fullnameField;
    @BindView(R.id.profile_username_field)AppCompatTextView usernameField;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            doRequest();
        }
        super.onHiddenChanged(hidden);
    }

    private final String TAG = ProfileFragment.class.getSimpleName();
    private String token;
    private String url;

    public void setAttributes(String token, String url){
        this.token = token;
        this.url = url;
    }

    private void doRequest() {
        boolean fieldEmpty = false;

        if (!fieldEmpty) {
            SysLog.getInstance().sendLog(TAG,"url : "+url);
            StringRequest request = new StringRequest(Request.Method.GET,url+"?token="+token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    SysLog.getInstance().sendLog(TAG, "response : " + response);
                    try {

                        JSONObject resp = new JSONObject(response);
                        if (resp.getInt("status_code") == 200) {
                            JSONObject data = resp.getJSONObject("data");
                            displayNameField.setText(data.getString("username"));
                            fullnameField.setText(data.getString("fullname"));
                            usernameField.setText(data.getString("username"));
                        }
                    } catch (JSONException ex) {
                        SysLog.getInstance().sendLog(TAG, "ex : " + ex.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    SysLog.getInstance().sendLog(TAG, "error volley : " + error.getMessage());


                }
            });

            AppConfig.getInstance().addToRequestQueue(request);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onProfileFragmentShow();
    }
}
