package com.lab.fx.library.contact;

import com.lab.fx.library.model.Holder;

/**
 * Created by febri on 30/07/17.
 */

public class Mine {
    private static PersonHolder HOLDER;
    public static void set(PersonHolder p_holder) {
        HOLDER = p_holder;
    }
    public static PersonHolder get() {
        if (HOLDER == null) {
            HOLDER = new PersonHolder();
            HOLDER.pin = "mine";
            HOLDER.first_name = "Mine";
        }
        return HOLDER;
    }

}
