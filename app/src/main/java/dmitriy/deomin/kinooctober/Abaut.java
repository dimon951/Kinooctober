package dmitriy.deomin.kinooctober;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.Timer;
import java.util.TimerTask;

import dmitriy.deomin.kinooctober.Info.Politika;
import dmitriy.deomin.kinooctober.Info.Pravila_kinoteatra;

public class Abaut extends Activity {

    ShimmerTextView hTextView;
    TextView textView;
    int live;
    Timer mTimer;
    MyTimerTask mMyTimerTask;
    Shimmer shimmer;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abaut);


        //во весь экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ((RelativeLayout)findViewById(R.id.fon_abaut)).setBackgroundColor(Main.COLOR_FON);
        imageButton = ((ImageButton)findViewById(R.id.imageButton_vk));

        hTextView = (ShimmerTextView) findViewById(R.id.text_abaut);
        hTextView.setTypeface(Main.face);
        hTextView.setText(getVersion());
        shimmer = new Shimmer();
        shimmer.start(hTextView);



        textView = (TextView)findViewById(R.id.textView_abaut);
        String v = getVersion();
        String t= getApplication().getResources().getString(R.string.Abaut_text);
        t= t.replace("++++++++++","Кинотеарт Октябрь "+v);
        textView.setText(t);
        textView.setVisibility(View.GONE);


        live= 0; // 2
        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, 1000, 1000);

    }

    public  void pizdez(){
        if (mTimer != null) {
            mTimer.cancel();
        }

        shimmer.cancel();
        hTextView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        imageButton.setVisibility(View.VISIBLE);
    }

    public void finish(View view) {
        this.finish();
    }

    public void koni(View v) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        v.startAnimation(anim);
        Intent p = new Intent(this, Politika.class);
        startActivity(p);
    }


    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    live++;
                    if(live>=2){
                      pizdez();
                    }
                }
            });
        }
    }

    private String getVersion(){
        try {
            PackageManager packageManager=getPackageManager();
            PackageInfo packageInfo=packageManager.getPackageInfo(getPackageName(),0);
            return packageInfo.versionName;
        }
        catch (  PackageManager.NameNotFoundException e) {
            return "?";
        }
    }

    public void clik_mail(View view) {
        Uri uri = Uri.parse("mailto:58627@bk.ru");
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(it);
    }
    public  void opengruppa(View view) {
        Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://vk.com/kinooctober"));
        startActivity(browseIntent);
    }

}
