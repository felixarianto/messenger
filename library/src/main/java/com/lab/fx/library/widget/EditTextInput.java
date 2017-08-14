package com.lab.fx.library.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lab.fx.library.R;

/**
 * Created by febri on 12/08/17.
 */

public class EditTextInput extends RelativeLayout {
    EditText  edtx;
    ImageView btn_ok;
    ImageView btn_cancel;
    int       r_icon;

    public EditTextInput(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.widget_edtx_input, this);
        edtx       = (EditText) findViewById(R.id.edtx_value);
        btn_ok     = (ImageView)findViewById(R.id.btn_ok);
        btn_cancel = (ImageView)findViewById(R.id.btn_cancel);
        r_icon     = attrs.getAttributeIntValue("app", "thumb", R.drawable.icv_notifications_blue);
        edtx.setHint(attrs.getAttributeValue("android", "hint"));
        edtx.setTextColor(getResources().getColor(R.color.text_content));
        viewMode();
    }

    private void viewMode() {
        btn_cancel.setImageResource(r_icon);
        btn_ok.setImageResource(R.drawable.icv_mode_edit_black);
        btn_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode();
            }
        });
        edtx.setEnabled(false);
    }

    private String mLastText;
    private void editMode() {
        mLastText= getText();
        btn_cancel.setImageResource(R.drawable.icv_close_black);
        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setText(mLastText);
                viewMode();
            }
        });
        btn_ok.setImageResource(R.drawable.icv_save_black);
        btn_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edtx.setOnFocusChangeListener(null);
                callOnClick();
                viewMode();
            }
        });
        edtx.setEnabled(true);
        edtx.requestFocus();
        edtx.setSelection(getText().length());
        edtx.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    setText(mLastText);
                    viewMode();
                }
            }
        });
    }

    public String getText() {
       return edtx.getText().toString();
    }

    public void setText(String p_value) {
        edtx.setText(p_value);
    }

    public void setHint(String p_value) {
        edtx.setHint(p_value);
    }

    public void setIcon(int p_icon) {
        r_icon = p_icon;
        viewMode();
    }


}
