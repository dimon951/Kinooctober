package dmitriy.deomin.kinooctober.Info;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import dmitriy.deomin.kinooctober.R;

import static dmitriy.deomin.kinooctober.R.layout.ava_vopr;


public class Ava_vopr extends Activity {
    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    Spannable text;
    Dowload_i_Parsing_text d_i_p_t;

    String vopros;
    String otvet;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ava_vopr);
        context = getApplicationContext();

        //во весь экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(isNetworkConnected()) {
            smena_texta(true);
            d_i_p_t = new Dowload_i_Parsing_text();
            d_i_p_t.execute(getIntent().getStringExtra("item"));
        }else {
            Toast.makeText(getApplicationContext(), "нужно интернет соединение", Toast.LENGTH_SHORT).show();
        }



    }


    public void Clik(View view) {
        this.finish();
    }

    public void smena_texta(boolean on_of){
        if(on_of){
            if (mTimer != null) {
                mTimer.cancel();
            }
            mTimer = new Timer();
            mMyTimerTask = new MyTimerTask();
            mTimer.schedule(mMyTimerTask, 100, 500);
        }else{
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
        }
    }

    public void Clik_logo_ava(View view) {
        captureScreen();
    }
    private void captureScreen() {

        String url_img = "/Pictures/Screenshot/"+"Screenshots" + System.currentTimeMillis() + ".png";

        View v = getWindow().getDecorView().getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);

        //создадим папки если нет
        File sddir = new File(Environment.getExternalStorageDirectory().toString()+"/Pictures/Screenshot/");
        if (!sddir.exists()) {
            sddir.mkdirs();
        }

        try {
            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory().toString(),url_img));
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(context,"Скриншот сохранён",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MyTimerTask extends TimerTask {
        String proces= "обновляется";
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView)findViewById(R.id.ava_textView_vopr)).setText(updete_text(proces));
                    ((TextView)findViewById(R.id.ava_textView_otv)).setText(updete_text(proces));
                    proces= proces+".";
                    if(proces.length()>15){proces = "обновляется";}
                }
            });
        }
    }

    Spannable updete_text(String t){
        text = new SpannableString(t); // / обновляется...

        if(t.length()>12){
            text.setSpan(new ForegroundColorSpan(random_color()), 11, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if(t.length()>13){
            text.setSpan(new ForegroundColorSpan(random_color()), 12, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if(t.length()==14){
            text.setSpan(new ForegroundColorSpan(random_color()), 13, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return  text;
    }

    public int random_color(){
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        return Color.rgb(r, g, b);
    }


    class Dowload_i_Parsing_text extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Document doc = null;
            try {
                doc = Jsoup
                        .connect(params[0].toString())
                        .timeout(0)
                        .maxBodySize(0)
                        .get();

            } catch (IOException e) {
                e.printStackTrace();
            }

            vopros = doc.select(".question").first().text();
            otvet = doc.select(".answer").first().text();


            return params[0].toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            smena_texta(false);
            ((TextView)findViewById(R.id.ava_textView_vopr)).setText(vopros);
            ((TextView)findViewById(R.id.ava_textView_otv)).setText(otvet);
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

}
