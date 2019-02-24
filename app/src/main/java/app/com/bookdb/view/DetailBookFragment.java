package app.com.bookdb.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.com.bookdb.R;
import app.com.bookdb.controller.AppConfig;
import app.com.bookdb.controller.SysLog;
import app.com.bookdb.model.Book;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailBookFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DetailBookFragment() {
        // Required empty public constructor
    }

    public static DetailBookFragment newInstance(String param1, String param2) {
        DetailBookFragment fragment = new DetailBookFragment();
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

    private View view;

    @BindView(R.id.book_name_field)
    AppCompatEditText bookNameField;
    @BindView(R.id.book_description_field)
    AppCompatEditText bookDescriptionField;

    @BindView(R.id.book_creator_field)
    AppCompatEditText creatorNameField;
    @BindView(R.id.book_date_created_field)
    AppCompatEditText bookDateCreatedField;

    @BindView(R.id.book_changePerson_field)
    AppCompatEditText changePersonField;
    @BindView(R.id.book_date_modified_field)
    AppCompatEditText bookDateDateModifiedField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private final String TAG = DetailBookFragment.class.getSimpleName();

    private String token;
    private String id;
    private String url;

    public void setAttributes(String token,String id,String url) {
        this.token = token;
        this.id = id;
        this.url = url;
    }

    private void setField(String name,String desc,String dateCreated,String creator,String dateModified,String changePerson){
        bookNameField.setText(name);
        bookDescriptionField.setText(desc);

        long dCreated = Long.parseLong(dateCreated) * 1000;

        creatorNameField.setText(creator);
        bookDateCreatedField.setText(DateFormat.format("MM/dd/yyyy HH:mm", new Date(dCreated)).toString());

        try {
            changePersonField.setText((changePerson == null) ? "-" : changePerson);
            long dModified = Long.parseLong(dateModified) * 1000;
            bookDateDateModifiedField.setText(DateFormat.format("MM/dd/yyyy HH:mm", new Date(dModified)).toString());
        }catch (NumberFormatException ex){
            SysLog.getInstance().sendLog(TAG,"ex : "+ex.getMessage());
            changePersonField.setText("-" );
            bookDateDateModifiedField.setText("-");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            doRequest();
        }
        super.onHiddenChanged(hidden);
    }

    private void doRequest() {
            StringRequest request = new StringRequest(Request.Method.GET,url+"?token="+token+"&id="+id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    SysLog.getInstance().sendLog(TAG, "response : " + response);
                    try {

                        JSONObject resp = new JSONObject(response);
                        if (resp.getInt("status_code") == 200) {
                            JSONObject data = resp.getJSONObject("data");
                            JSONObject createdBy =data.getJSONObject("createdBy");
                            JSONObject modifiedBy = data.getJSONObject("modifiedBy");

                            setField(data.getString("name"),data.getString("description"),
                                    data.getString("createdAt"),createdBy.getString("username"),
                                    data.getString("modifiedAt"),modifiedBy.getString("username"));
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
        void onDetailBookFragmentInteraction();
    }
}
