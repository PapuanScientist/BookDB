package app.com.bookdb.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import app.com.bookdb.model.Book;
import app.com.bookdb.model.Credential;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements BookListFragment.OnFragmentInteractionListener,
        InsertBookFragment.OnFragmentInteractionListener, DetailBookFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener{

    private final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.content_main)
    View view;

    private BookListFragment bookListFragment;
    private InsertBookFragment insertBookFragment;
    private DetailBookFragment detailBookFragment;
    private ProfileFragment profileFragment;

    private Fragment lastFragmentShow;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        bookListFragment = new BookListFragment();
        insertBookFragment = new InsertBookFragment();
        detailBookFragment = new DetailBookFragment();
        profileFragment = new ProfileFragment();

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.content_main, bookListFragment).commit();
        fragmentManager.beginTransaction().add(R.id.content_main, insertBookFragment).commit();
        fragmentManager.beginTransaction().add(R.id.content_main, detailBookFragment).commit();
        fragmentManager.beginTransaction().add(R.id.content_main, profileFragment).commit();
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (lastFragmentShow != null) {
            setActiveFragment(lastFragmentShow);
        } else {
            toolbar.setTitle(getResources().getString(R.string.app_book_list));
            bookListFragment.setAttributes(sessionManager.getCurrentUser().getToken(), upin(hola(AppConfig.BOOK_LIST_URL)));
            setActiveFragment(bookListFragment);
        }
    }

    private void setActiveFragment(Fragment fragment) {
        for (Fragment item : fragmentManager.getFragments()) {
            fragmentManager.beginTransaction().hide(item).commitAllowingStateLoss();
        }
        for (Fragment item : fragmentManager.getFragments()) {
            if (fragment.getId() == item.getId()) {
                if (fragment.getId() == insertBookFragment.getId()) {
                    fragmentManager.beginTransaction().show(fragment).setCustomAnimations(R.anim.down_from_top, R.anim.down_from_top).commitAllowingStateLoss();
                } else {
                    fragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
                }
                lastFragmentShow = fragment;
            }
        }
    }

    @OnClick({R.id.fab, R.id.edit_btn, R.id.detail_btn})
    void onClick(View view) {
        if (view.getId() == R.id.fab) {
            toolbar.setTitle(getResources().getString(R.string.app_add_book));
            insertBookFragment.setAttributes(sessionManager.getCurrentUser().getToken(), upin(hola(AppConfig.INSERT_BOOK_URL)), false, null);
            setActiveFragment(insertBookFragment);
        }

    }

    private Book book;

    private Menu menu;

    private boolean closeState=false;
    @Override
    public void onBackPressed() {
        if (closeState) {
            super.onBackPressed();
            return;
        }
        closeState = true;
        Toast.makeText(this, R.string.app_exit_message, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeState = false;
            }
        }, 2000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            doLogOut();
        } else if (id == R.id.action_profile) {
            SysLog.getInstance().sendLog("ProfileFragment","profile fragment : "+appBarLayout.getElevation());
            appBarLayout.setElevation(0);
            findViewById(R.id.fab).setVisibility(View.GONE);
            menu.findItem(R.id.action_profile).setVisible(false);
            menu.findItem(R.id.action_logout).setVisible(false);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setContentInsetStartWithNavigation(0);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayBookList();
                }
            });
            toolbar.setTitle(getResources().getString(R.string.app_profile));
            profileFragment.setAttributes(sessionManager.getCurrentUser().getToken(),upin(hola(AppConfig.PROFILE_USER_URL)));
            setActiveFragment(profileFragment);
        }

        return true;
    }

    private void doLogOut() {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        final AlertDialog alertDialog = builder.create();
        alertDialog.setMessage(getString(R.string.app_exit_prompt));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, getString(R.string.app_ok_command), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doLogOutServer();
            }
        });
        alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE, getString(R.string.app_cancel_command), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void doLogOutServer() {
        StringRequest request = new StringRequest(Request.Method.GET, upin(hola(AppConfig.LOGOUT_URL)), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    SysLog.getInstance().sendLog(TAG, "response : " + response);
                    JSONObject resp = new JSONObject(response);
                    if (resp.getInt("status_code") == 200) {
                        if (sessionManager.resetCrendetials(sessionManager.getCurrentUser())) {
                            finish();
                        }
                    }
                } catch (JSONException ex) {
                    SysLog.getInstance().sendLog(TAG, " ex :" + ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                SysLog.getInstance().sendLog(TAG, "error volley : " + error.getMessage());


            }
        });
        Map<String, String> params = new HashMap<>();
        params.put("token", sessionManager.getCurrentUser().getToken());
        request.setParams(params);


        AppConfig.getInstance().addToRequestQueue(request);
    }

    private void editBook() {
        toolbar.setTitle(getResources().getString(R.string.app_edit_book));
        insertBookFragment.setAttributes(sessionManager.getCurrentUser().getToken(), upin(hola(AppConfig.UPDATE_BOOK_URL)), true, book);
        setActiveFragment(insertBookFragment);
    }

    private void detailBook(final Book book) {
        appBarLayout.setElevation(10.5f);
        findViewById(R.id.fab).setVisibility(View.GONE);
        menu.findItem(R.id.action_profile).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayBookList();
            }
        });
        toolbar.setTitle(getResources().getString(R.string.app_detail_book));
        detailBookFragment.setAttributes(sessionManager.getCurrentUser().getToken(),book.getId(),upin(hola(AppConfig.DETAIL_BOOK_URL)));
        setActiveFragment(detailBookFragment);
    }


    @Override
    public void onBookListItemClick(final Book book) {
        this.book = book;
        //toggleBottomSheet();
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        view.findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                editBook();
            }
        });
        view.findViewById(R.id.detail_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                detailBook(book);
            }
        });

        ;
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    @Override
    public void onBookListShow() {

        findViewById(R.id.fab).setVisibility(View.VISIBLE);
    }

    private void displayBookList() {
        appBarLayout.setElevation(10.5f);
        toolbar.setNavigationIcon(null);
        toolbar.setTitle(getResources().getString(R.string.app_book_list));
        bookListFragment.setAttributes(sessionManager.getCurrentUser().getToken(),upin(hola(AppConfig.BOOK_LIST_URL)));
        setActiveFragment(bookListFragment);
        findViewById(R.id.fab).setVisibility(View.VISIBLE);
        menu.findItem(R.id.action_profile).setVisible(true);
        menu.findItem(R.id.action_logout).setVisible(true);
    }

    @Override
    public void onInsertBookShow() {
        findViewById(R.id.fab).setVisibility(View.GONE);
        menu.findItem(R.id.action_profile).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayBookList();
            }
        });
    }

    @Override
    public void onInsertBookDataSuccess(boolean edit) {
        final Snackbar snackbar = Snackbar.make(view, (edit) ? "Data berhasil diupdate" : "Data berhasil disimpan", Snackbar.LENGTH_SHORT);
        snackbar.setAction(getString(R.string.app_ok_command), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public void onInsertBookDismiss() {
        displayBookList();
    }

    @Override
    public void onDetailBookFragmentInteraction() {

    }

    @Override
    public void onProfileFragmentShow() {

    }
}
