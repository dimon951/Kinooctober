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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.SuperToast;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.R;

public class Ava_skoro extends Activity {

    ImageView imageView;
    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    Spannable text;
    Dowload_i_Parsing_text d_i_p_t;


    String nazvanie_value;
    String originalnoe_nazvanie_value;
    String opisanie_value;
    String prodolgitelnost_value;
    String kategoria_value;
    String ganr_value;
    String acteri_value;
    String regiser_value;

    Context context;


    //для обводки картинок рамкой
    Transformation transformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ava_skoro);

        context = getApplicationContext();

        imageView  = (ImageView)findViewById(R.id.imageView_ava);

        //во весь экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        transformation = new RoundedTransformationBuilder()
                .borderColor(Main.COLOR_TEXT)
                .borderWidthDp(Main.TOLSHINA_RAMKI)
                .cornerRadiusDp(Main.RADIUS_CKRUGLENIA/2)
                .oval(false)
                .build();
        ((LinearLayout)findViewById(R.id.fon_ava_skoro)).setBackgroundColor(Main.COLOR_FON);

        ((LinearLayout)findViewById(R.id.liner_skoro1)).setBackgroundColor(Main.COLOR_ITEM);
        ((LinearLayout)findViewById(R.id.liner_skoro2)).setBackgroundColor(Main.COLOR_ITEM2);
        ((LinearLayout)findViewById(R.id.liner_skoro3)).setBackgroundColor(Main.COLOR_ITEM);
        ((LinearLayout)findViewById(R.id.liner_skoro4)).setBackgroundColor(Main.COLOR_ITEM2);
        ((LinearLayout)findViewById(R.id.liner_skoro5)).setBackgroundColor(Main.COLOR_ITEM);
        ((LinearLayout)findViewById(R.id.liner_skoro6)).setBackgroundColor(Main.COLOR_ITEM2);
        ((LinearLayout)findViewById(R.id.liner_skoro7)).setBackgroundColor(Main.COLOR_ITEM);
        ((LinearLayout)findViewById(R.id.liner_skoro8)).setBackgroundColor(Main.COLOR_ITEM2);




        if(isNetworkConnected()) {

            smena_texta(true);
            if(getIntent().getStringExtra("ava").length()>1) {
                Picasso.get()
                        .load(getIntent().getStringExtra("ava"))
                        .transform(transformation)
                        .into(imageView);
            }

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
                    ((TextView)findViewById(R.id.textView_nazvanie_value)).setText(updete_text(proces));
                    ((TextView)findViewById(R.id.textView_originalnoe_nazvanie_value)).setText(updete_text(proces));
                    ((TextView)findViewById(R.id.textView_opisanie_value)).setText(updete_text(proces));
                    ((TextView)findViewById(R.id.textView_prodolgitelnost_value)).setText(updete_text(proces));
                    ((TextView)findViewById(R.id.textView_kategoria_value)).setText(updete_text(proces));
                    ((TextView)findViewById(R.id.textView_ganr_value)).setText(updete_text(proces));
                    ((TextView)findViewById(R.id.textView_acteri_value)).setText(updete_text(proces));
                    ((TextView)findViewById(R.id.textView_regiser_value)).setText(updete_text(proces));
                    ((Button)findViewById(R.id.button_ava_skoro)).setText(updete_text(proces));
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
        return Color.rgb(r,g,b);
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

            Elements table = doc.select(".film-info");
            Elements rows = table.select("tr");
            Elements otziv = doc.select(".block.film-reviews");

            if(rows.size()>7) {
                nazvanie_value = rows.get(0).select("td").get(1).text();
                originalnoe_nazvanie_value = rows.get(1).select("td").get(1).text();
                opisanie_value = rows.get(2).select("td").get(1).text();
                prodolgitelnost_value = rows.get(3).select("td").get(1).text();
                kategoria_value = rows.get(4).select("td").get(1).text();
                ganr_value = rows.get(5).select("td").get(1).text();
                acteri_value = rows.get(6).select("td").get(1).text();
                regiser_value = rows.get(7).select("td").get(1).text();
            }

            return params[0].toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            smena_texta(false);
            ((TextView)findViewById(R.id.textView_nazvanie_value)).setText(nazvanie_value);
            ((TextView)findViewById(R.id.textView_originalnoe_nazvanie_value)).setText(originalnoe_nazvanie_value);
            ((TextView)findViewById(R.id.textView_opisanie_value)).setText(opisanie_value);
            ((TextView)findViewById(R.id.textView_prodolgitelnost_value)).setText(prodolgitelnost_value);
            ((TextView)findViewById(R.id.textView_kategoria_value)).setText(kategoria_value);
            ((TextView)findViewById(R.id.textView_ganr_value)).setText(ganr_value);
            ((TextView)findViewById(R.id.textView_acteri_value)).setText(acteri_value);
            ((TextView)findViewById(R.id.textView_regiser_value)).setText(regiser_value);
            ((Button)findViewById(R.id.button_ava_skoro)).setText(updete_text(nazvanie_value));
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
