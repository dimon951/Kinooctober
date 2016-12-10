package dmitriy.deomin.kinooctober.kino_sourse;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperToast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.R;

import static dmitriy.deomin.kinooctober.R.layout.ava_otzivi;


public class Ava_otzivi extends Activity{
   // ImageView imageView;
    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    Spannable text;
    Dowload_i_Parsing_text d_i_p_t;

    String title_com;
    String avtor;
    String film;
    String data_com; // data
    String coment;  //текст коментария
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ava_otzivi);
        context = getApplicationContext();


        //во весь экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ((LinearLayout)findViewById(R.id.fon_ava_otzivi)).setBackgroundColor(Main.COLOR_FON);

        if(isNetworkConnected()) {

            smena_texta(true);
//            Picasso.with(this)
//                    .load(getIntent().getStringExtra("ava"))
//                    .into(imageView);

            d_i_p_t = new Dowload_i_Parsing_text();
            d_i_p_t.execute(getIntent().getStringExtra("item"));
        }else {
            Main.Toast_error(getString(R.string.netu_internetu));
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

        String url_img = "/Pictures/Screenshots_MichKino/" + "Screenshots" + System.currentTimeMillis() + ".png";

        View v = getWindow().getDecorView().getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);

        //создадим папки если нет
        File sddir = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures/Screenshots_MichKino/");
        if (!sddir.exists()) {
            sddir.mkdirs();
        }

        try {
            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory().toString(), url_img));
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
//tost---------------------------------------------------------------
            SuperToast superToast = new SuperToast(this);
            superToast.setDuration(SuperToast.Duration.LONG);
            superToast.setText("Скриншот сохранён в:"+url_img);
            superToast.setIcon(SuperToast.Icon.Dark.SAVE, SuperToast.IconPosition.LEFT);
            superToast.show();
//---------------------------------------------------------------------
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
                    ((TextView)findViewById(R.id.ozivi_textView_zag_com)).setText(updete_text(proces));
                    ((TextView)findViewById(R.id.ozivi_textView_avtor)).setText(updete_text(proces));
                    ((TextView)findViewById(R.id.ozivi_textView_film)).setText(updete_text(proces));
                    ((TextView)findViewById(R.id.ozivi_textView_data)).setText(updete_text(proces));
                    ((TextView)findViewById(R.id.ozivi_textView_comentariy)).setText(updete_text(proces));
                    ((Button)findViewById(R.id.button_logo_ava_otzivi)).setText(updete_text(proces));
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
                        .timeout(3000)
                        .get();

            } catch (IOException e) {
                e.printStackTrace();
            }


                title_com = doc.select(".title").text();
                avtor = doc.select(".events-item.author").text().replace("Автор:", "");
                film = doc.select(".events-item.film-title").text().replace("Фильм:","");
                data_com = doc.select(".date").text();
                coment = doc.select(".description").text();


            return params[0].toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            smena_texta(false);
            ((TextView)findViewById(R.id.ozivi_textView_zag_com)).setText(title_com);
            ((TextView)findViewById(R.id.ozivi_textView_avtor)).setText(avtor);
            ((TextView)findViewById(R.id.ozivi_textView_film)).setText(film);
            ((TextView)findViewById(R.id.ozivi_textView_data)).setText(data_com);
            ((TextView)findViewById(R.id.ozivi_textView_comentariy)).setText(coment);
            ((Button)findViewById(R.id.button_logo_ava_otzivi)).setText(updete_text(title_com));
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
