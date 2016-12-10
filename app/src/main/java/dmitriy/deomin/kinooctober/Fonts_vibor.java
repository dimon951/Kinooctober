package dmitriy.deomin.kinooctober;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class Fonts_vibor extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fonts_vibor);

        //во весь экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ((LinearLayout)findViewById(R.id.fonts_vibor)).setBackgroundColor(Main.COLOR_FON);

        ((Button)findViewById(R.id.button_font_tweed)).setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Tweed.ttf"));
        ((Button)findViewById(R.id.button_font_kramola)).setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Kramola.ttf"));
        ((Button)findViewById(R.id.button_font_badaboom)).setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Badaboom.ttf"));
        ((Button)findViewById(R.id.button_font_capture)).setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Capture.ttf"));
        ((Button)findViewById(R.id.button_font_bemount)).setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Bemount.ttf"));
        ((Button)findViewById(R.id.button_font_Smeshariki)).setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Smeshariki.ttf"));
        ((Button)findViewById(R.id.button_font_Snowstorm)).setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Snowstorm.ttf"));
        ((Button)findViewById(R.id.button_font_Izhitsa)).setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Izhitsa.ttf"));
        ((Button)findViewById(R.id.button_font_Sensei)).setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Sensei.ttf"));
        ((Button)findViewById(R.id.button_font_PF_DinText)).setTypeface(Typeface.createFromAsset(getAssets(),"fonts/PF_DinText.ttf"));
        ((Button)findViewById(R.id.button_font_Yarin)).setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Yarin.ttf"));
        ((Button)findViewById(R.id.button_font_Frezer)).setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Frezer.ttf"));
        ((Button)findViewById(R.id.button_font_Futured)).setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Futured.ttf"));
        ((Button)findViewById(R.id.button_font_Rotonda)).setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Rotonda.ttf"));

        ((Button)findViewById(R.id.button_font_tweed)).setTextColor(Main.COLOR_TEXT);
        ((Button)findViewById(R.id.button_font_kramola)).setTextColor(Main.COLOR_TEXT);
        ((Button)findViewById(R.id.button_font_badaboom)).setTextColor(Main.COLOR_TEXT);
        ((Button)findViewById(R.id.button_font_capture)).setTextColor(Main.COLOR_TEXT);
        ((Button)findViewById(R.id.button_font_bemount)).setTextColor(Main.COLOR_TEXT);
        ((Button)findViewById(R.id.button_font_Smeshariki)).setTextColor(Main.COLOR_TEXT);
        ((Button)findViewById(R.id.button_font_Snowstorm)).setTextColor(Main.COLOR_TEXT);
        ((Button)findViewById(R.id.button_font_Izhitsa)).setTextColor(Main.COLOR_TEXT);
        ((Button)findViewById(R.id.button_font_Sensei)).setTextColor(Main.COLOR_TEXT);
        ((Button)findViewById(R.id.button_font_PF_DinText)).setTextColor(Main.COLOR_TEXT);
        ((Button)findViewById(R.id.button_font_Yarin)).setTextColor(Main.COLOR_TEXT);
        ((Button)findViewById(R.id.button_font_Frezer)).setTextColor(Main.COLOR_TEXT);
        ((Button)findViewById(R.id.button_font_Futured)).setTextColor(Main.COLOR_TEXT);
        ((Button)findViewById(R.id.button_font_Rotonda)).setTextColor(Main.COLOR_TEXT);
    }



    public void save_Tweed(View v){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        v.startAnimation(anim);
        Main.save_value("fonts","fonts/Tweed.ttf");
        Main.Toast("Хорошо,теперь нужно перезапустить программу");
    }
    public void save_Kramola(View v){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        v.startAnimation(anim);
        Main.save_value("fonts","fonts/Kramola.ttf");
        Main.Toast("Хорошо,теперь нужно перезапустить программу");
    }
    public void save_Badaboom(View v){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        v.startAnimation(anim);
        Main.save_value("fonts","fonts/Badaboom.ttf");
        Main.Toast("Хорошо,теперь нужно перезапустить программу");
    }
    public void save_Capture(View v){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        v.startAnimation(anim);
        Main.save_value("fonts","fonts/Capture.ttf");
        Main.Toast("Хорошо,теперь нужно перезапустить программу");
    }
    public void save_Bemount(View v){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        v.startAnimation(anim);
        Main.save_value("fonts","fonts/Bemount.ttf");
        Main.Toast("Хорошо,теперь нужно перезапустить программу");
    }
    public void save_Smeshariki(View v){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        v.startAnimation(anim);
        Main.save_value("fonts","fonts/Smeshariki.ttf");
        Main.Toast("Хорошо,теперь нужно перезапустить программу");
    }
    public void save_Snowstorm(View v){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        v.startAnimation(anim);
        Main.save_value("fonts","fonts/Snowstorm.ttf");
        Main.Toast("Хорошо,теперь нужно перезапустить программу");
    }
    public void save_Izhitsa(View v){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        v.startAnimation(anim);
        Main.save_value("fonts","fonts/Izhitsa.ttf");
        Main.Toast("Хорошо,теперь нужно перезапустить программу");
    }

    public void save_Rotonda(View v){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        v.startAnimation(anim);
        Main.save_value("fonts","fonts/Rotonda.ttf");
        Main.Toast("Хорошо,теперь нужно перезапустить программу");
    }
    public void save_Frezer(View v){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        v.startAnimation(anim);
        Main.save_value("fonts","fonts/Frezer.ttf");
        Main.Toast("Хорошо,теперь нужно перезапустить программу");
    }
    public void save_Futured(View v){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        v.startAnimation(anim);
        Main.save_value("fonts","fonts/Futured.ttf");
        Main.Toast("Хорошо,теперь нужно перезапустить программу");
    }
    public void save_Yarin(View v){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        v.startAnimation(anim);
        Main.save_value("fonts","fonts/Yarin.ttf");
        Main.Toast("Хорошо,теперь нужно перезапустить программу");
    }
    public void save_Sensei(View v){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        v.startAnimation(anim);
        Main.save_value("fonts","fonts/Sensei.ttf");
        Main.Toast("Хорошо,теперь нужно перезапустить программу");
    }
    public void save_PF_DinText(View v){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        v.startAnimation(anim);
        Main.save_value("fonts","fonts/PF_DinText.ttf");
        Main.Toast("Хорошо,теперь нужно перезапустить программу");
    }

}
