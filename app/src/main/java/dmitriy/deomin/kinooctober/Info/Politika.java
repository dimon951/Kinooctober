package dmitriy.deomin.kinooctober.Info;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.R;

/**
 * Created by dimon on 08.02.17.
 */

public class Politika extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.politika_confedi);
        //во весь экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((LinearLayout)findViewById(R.id.fon_politiki)).setBackgroundColor(Main.COLOR_FON);
        ((TextView)findViewById(R.id.textView_politiki)).setTextColor(Main.COLOR_TEXT);
    }
}
