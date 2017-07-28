package com.lab.fx.library.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lab.fx.library.R;

/**
 * Created by febri on 28/07/17.
 */

public class TextLabel extends LinearLayout {

    public TextLabel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.text_label, this, true);
        String label = attrs.getAttributeValue("app", "label");
        if (label != null) {
            ((TextView) findViewById(R.id.label)).setText(label);
        }
        String value = attrs.getAttributeValue("app", "value");
        if (value != null) {
            ((TextView) findViewById(R.id.value)).setText(value);
        }
    }
}
