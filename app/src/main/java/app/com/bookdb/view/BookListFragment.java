package app.com.bookdb.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.bookdb.R;
import app.com.bookdb.controller.AppConfig;
import app.com.bookdb.controller.SysLog;
import app.com.bookdb.model.Book;
import butterknife.BindView;
import butterknife.ButterKnife;


public class BookListFragment extends Fragment implements BookAdapter.OnBookAdapterListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BookListFragment() {
        // Required empty public constructor
    }


    public static BookListFragment newInstance(String param1, String param2) {
        BookListFragment fragment = new BookListFragment();
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

    @BindView(R.id.rv_book_list)
    RecyclerView rvBookList;
    @BindView(R.id.progress_bar)AVLoadingIndicatorView progressBar;
    @BindView(R.id.progress_title)AppCompatTextView progressTitle;

    private View view;
    private BookAdapter bookAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_book_list, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    private final String TAG = BookListFragment.class.getSimpleName();
    private String token;
    private String url;

    public void setAttributes(String token, String url) {
        this.token = token;
        this.url = url;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            populateData();
        }

    }

    @Override
    public void onItemClick(Book book) {
        mListener.onBookListItemClick(book);
    }

    private List<Book> bookList;


    private void populateData() {
        bookList = null;
        bookList = new ArrayList<>();

        progressBar.setVisibility(View.VISIBLE);
        progressTitle.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, url + "?token=" + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SysLog.getInstance().sendLog(TAG, "response : " + response);
                try {

                    JSONObject resp = new JSONObject(response);
                    if (resp.getInt("status_code") == 200) {
                        JSONObject data = resp.getJSONObject("data");
                        JSONArray books = data.getJSONArray("books");
                        for (int i = 0; i < books.length(); i++) {
                            JSONObject bookItem = books.getJSONObject(i);
                            JSONObject createdBy = bookItem.getJSONObject("createdBy");
                            JSONObject modifiedBy = bookItem.getJSONObject("modifiedBy");
                            bookList.add(
                                    new Book(bookItem.getString("createdAt"),
                                            createdBy.getString("fullname"),
                                            createdBy.getString("id"),
                                            createdBy.getString("username"),
                                            bookItem.getString("deletedAt"),
                                            bookItem.getString("description"),
                                            bookItem.getString("id"),
                                            bookItem.getString("name"),
                                            bookItem.getString("modifiedAt"),
                                            modifiedBy.getString("fullname"),
                                            modifiedBy.getString("id"),
                                            modifiedBy.getString("username")));
                        }
                        synchronized (this) {
                            progressBar.setVisibility(View.INVISIBLE);
                            progressTitle.setVisibility(View.INVISIBLE);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bookAdapter = new BookAdapter(getContext());
                                bookAdapter.setListener(BookListFragment.this);
                                bookAdapter.setData(bookList);
                                rvBookList.setAdapter(bookAdapter);
                                rvBookList.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
                                rvBookList.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                                bookAdapter.notifyDataSetChanged();
                            }
                        },1000);

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

        void onBookListShow();
        void onBookListItemClick(Book book);
    }
}
