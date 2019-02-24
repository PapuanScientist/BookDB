package app.com.bookdb.view;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import app.com.bookdb.R;
import app.com.bookdb.controller.SysLog;

/**
 * Created by Ori Syun on 1/25/2018.
 */

public class EditTextPassword extends AppCompatEditText implements View.OnTouchListener {
    String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    private Context context;

    public EditTextPassword(Context context) {
        super(context);
        this.context = context;
    }

    public EditTextPassword(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public EditTextPassword(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textstyle", Typeface.NORMAL);
        Typeface font_type = Typeface.createFromAsset(getContext().getAssets(), "roboto.ttf");
        setTypeface(font_type, textStyle);

        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getRawX() >= ((float) (v.getRight() - getCompoundDrawables()[2].getBounds().width()))) {

                if (getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    SysLog.getInstance().sendLog("EdiTextPassword","if condition ");

                    setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_lock),null, ContextCompat.getDrawable(context,R.drawable.ic_red_eye_visibility_remove_off),null);

                    setTransformationMethod(PasswordTransformationMethod.getInstance());
                    setSelection(getText().length());
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.ic_lock),null, ContextCompat.getDrawable(context,R.drawable.ic_eye),null);

                    SysLog.getInstance().sendLog("EdiTextPassword","else condition ");
                    setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    setSelection(getText().length());
                }

                return true;
            }
        }
        return false;
    }


}
