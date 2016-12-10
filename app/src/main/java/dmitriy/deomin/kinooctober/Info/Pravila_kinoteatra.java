package dmitriy.deomin.kinooctober.Info;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.R;

public class Pravila_kinoteatra extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pravila_kinoteatra);
        //во весь экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((LinearLayout)findViewById(R.id.fon_pravil_kinoteatra)).setBackgroundColor(Main.COLOR_FON);
        ((TextView)findViewById(R.id.textView_pravila_kinoteatra)).setTextColor(Main.COLOR_TEXT);
        ((TextView)findViewById(R.id.textView_pravila_kinoteatra)).setTypeface(Main.face);
    }

}
