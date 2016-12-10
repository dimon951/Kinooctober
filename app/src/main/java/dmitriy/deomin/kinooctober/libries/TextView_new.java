package dmitriy.deomin.kinooctober.libries;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import dmitriy.deomin.kinooctober.Main;


/**
 * Created by Admin on 23.07.2016.
 */
public class TextView_new extends TextView {


    public TextView_new(Context context) {
        super(context);
        this.setTypeface(Main.face);
        this.setTextColor(Main.COLOR_TEXT);
    }

    public TextView_new(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Main.face);
        this.setTextColor(Main.COLOR_TEXT);
    }

    public TextView_new(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTypeface(Main.face);
        this.setTextColor(Main.COLOR_TEXT);
    }

}
