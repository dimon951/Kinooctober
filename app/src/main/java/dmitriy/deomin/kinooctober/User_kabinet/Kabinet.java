package dmitriy.deomin.kinooctober.User_kabinet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.R;

public class Kabinet extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kabinet);
        //во весь экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((LinearLayout)findViewById(R.id.fon_lichniy_kabinet)).setBackgroundColor(Main.COLOR_FON);

    }

    public void user_setting(View view) {
    }

    public void user_otzivi(View view) {
    }
}
