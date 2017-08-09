package com.lab.fx.messenger.onesignal;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.LinearLayout;

import com.lab.fx.messenger.R;

/**
 * Created by febri on 08/08/17.
 */

public class Composer extends AlertDialog.Builder {


    public Composer(Context context) {
        super(context);
        setView(new LinearLayout(context));
    }

}
