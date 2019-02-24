package app.com.bookdb.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
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
import app.com.bookdb.model.Credential;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InsertBookFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public InsertBookFragment() {
        // Required empty public constructor
    }

    public static InsertBookFragment newInstance(String param1, String param2) {
        InsertBookFragment fragment = new InsertBookFragment();
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

    @BindView(R.id.book_name_field)AppCompatEditText bookNameField;
    @BindView(R.id.book_description_field)AppCompatEditText bookDescriptionField;
    @BindView(R.id.insert_book_btn)AppCompatButton insertBookBtn;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_insert_book, container, false);
       ButterKnife.bind(this,view);
       return view;
    }

    @OnClick(R.id.insert_book_btn)
    void onClik(View view){
        doInsert();
    }

    private final String TAG = InsertBookFragment.class.getSimpleName();
    private String token;
    private String url;
    private boolean edit;
    private Book book;

    public void setAttributes(String token, String url, boolean edit, Book book){
        this.token = token;
        this.url = url;
        this.edit = edit;
        this.book= book;


        if (edit) {
            bookNameField.setText(book.getName());
            bookDescriptionField.setText(book.getDescription());
            insertBookBtn.setText("Update");
        }
    }

    private void doInsert() {
        boolean fieldEmpty = false;
        if (bookNameField.getText().toString().length() == 0 || bookDescriptionField.getText().toString().length() == 0) {
            bookNameField.setError("Harus diisi");
            bookNameField.requestFocus();
            bookDescriptionField.setError("Harus diisi");
            fieldEmpty =true;
        } else if (bookNameField.getText().toString().length() == 0) {
            bookNameField.setError("Harus diisi");
            bookNameField.requestFocus();
            fieldEmpty = true;
        } else if (bookDescriptionField.getText().toString().length() == 0) {
            bookDescriptionField.setError("Harus diisi");
            bookDescriptionField.requestFocus();
            fieldEmpty = true;
        }
        if (!fieldEmpty) {

            SysLog.getInstance().sendLog(TAG,"url : "+url);
            StringRequest request = new StringRequest(Request.Method.POST,url+"?token="+token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    SysLog.getInstance().sendLog(TAG, "response : " + response);
                    try {

                        JSONObject resp = new JSONObject(response);
                        if (resp.getInt("status_code") == 200) {
                            if (edit) {
                                mListener.onInsertBookDataSuccess(true);
                                mListener.onInsertBookDismiss();
                            }else {
                                mListener.onInsertBookDataSuccess(false);
                                bookNameField.setText("");
                                bookDescriptionField.setText("");
                            }

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
            SysLog.getInstance().sendLog(TAG,"token : "+ token);
            SysLog.getInstance().sendLog(TAG,"name : "+bookNameField.getText().toString());
            SysLog.getInstance().sendLog(TAG,"description : "+ bookDescriptionField.getText().toString());

            Map<String, String> params = new HashMap<>();
            params.put("name", bookNameField.getText().toString());
            params.put("description", bookDescriptionField.getText().toString());
            if (edit) {
                params.put("id", book.getId());
            }
            request.setParams(params);


            AppConfig.getInstance().addToRequestQueue(request);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            mListener.onInsertBookShow();
        }
        super.onHiddenChanged(hidden);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onInsertBookShow();
        void onInsertBookDataSuccess(boolean edit);
        void onInsertBookDismiss();
    }
}
