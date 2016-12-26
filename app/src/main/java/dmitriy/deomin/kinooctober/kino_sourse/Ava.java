package dmitriy.deomin.kinooctober.kino_sourse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import dmitriy.deomin.kinooctober.User_kabinet.User_login_parol;

public class Ava extends Activity {

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

    String otzivi_k_vilmu;

    TextView textView_nazvanie_value;
    TextView textView_originalnoe_nazvanie_value;
    TextView textView_opisanie_value;
    TextView textView_prodolgitelnost_value;
    TextView textView_kategoria_value;
    TextView textView_ganr_value;
    TextView textView_acteri_value;
    TextView textView_regiser_value;
    TextView textView_otzizi_k_filmu;

    Button button_logo_ava;


    String urlotz;
    Context context;

    //для обводки картинок рамкой
    Transformation transformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ava);

        context= getApplicationContext();

        //во весь экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        transformation = new RoundedTransformationBuilder()
                .borderColor(Main.COLOR_TEXT)
                .borderWidthDp(Main.TOLSHINA_RAMKI)
                .cornerRadiusDp(Main.RADIUS_CKRUGLENIA/2)
                .oval(false)
                .build();

        imageView  = (ImageView) findViewById(R.id.imageView_ava);

        ((LinearLayout)findViewById(R.id.fon_ava)).setBackgroundColor(Main.COLOR_FON);


        textView_nazvanie_value =((TextView)findViewById(R.id.textView_nazvanie_value));
        textView_originalnoe_nazvanie_value =((TextView)findViewById(R.id.textView_originalnoe_nazvanie_value));
        textView_opisanie_value =((TextView)findViewById(R.id.textView_opisanie_value));
        textView_prodolgitelnost_value=((TextView)findViewById(R.id.textView_prodolgitelnost_value));
        textView_kategoria_value=((TextView)findViewById(R.id.textView_kategoria_value));
        textView_ganr_value=((TextView)findViewById(R.id.textView_ganr_value));
        textView_acteri_value=((TextView)findViewById(R.id.textView_acteri_value));
        textView_regiser_value=((TextView)findViewById(R.id.textView_regiser_value));
        textView_otzizi_k_filmu=((TextView)findViewById(R.id.textView_otzizi_k_filmu));

        button_logo_ava=((Button)findViewById(R.id.button_logo_ava));

        //ставим шрифт, цвет
        textView_nazvanie_value.setTypeface(Main.face);
        textView_originalnoe_nazvanie_value.setTypeface(Main.face);
        textView_opisanie_value.setTypeface(Main.face);
        textView_prodolgitelnost_value.setTypeface(Main.face);
        textView_kategoria_value.setTypeface(Main.face);
        textView_ganr_value.setTypeface(Main.face);
        textView_acteri_value.setTypeface(Main.face);
        textView_regiser_value.setTypeface(Main.face);
        textView_otzizi_k_filmu.setTypeface(Main.face);
        button_logo_ava.setTypeface(Main.face);

        textView_nazvanie_value.setTextColor(Main.COLOR_TEXT);
        textView_originalnoe_nazvanie_value.setTextColor(Main.COLOR_TEXT);
        textView_opisanie_value.setTextColor(Main.COLOR_TEXT);
        textView_prodolgitelnost_value.setTextColor(Main.COLOR_TEXT);
        textView_kategoria_value.setTextColor(Main.COLOR_TEXT);
        textView_ganr_value.setTextColor(Main.COLOR_TEXT);
        textView_acteri_value.setTextColor(Main.COLOR_TEXT);
        textView_regiser_value.setTextColor(Main.COLOR_TEXT);
        textView_otzizi_k_filmu.setTextColor(Main.COLOR_TEXT);
        button_logo_ava.setTextColor(Main.COLOR_TEXT);

        ///ставим разделение по цветам у лояутов
        ((LinearLayout)findViewById(R.id.ava1_liner)).setBackgroundColor(Main.COLOR_ITEM);
        ((LinearLayout)findViewById(R.id.ava2_liner)).setBackgroundColor(Main.COLOR_ITEM2);
        ((LinearLayout)findViewById(R.id.ava3_liner)).setBackgroundColor(Main.COLOR_ITEM);
        ((LinearLayout)findViewById(R.id.ava4_liner)).setBackgroundColor(Main.COLOR_ITEM2);
        ((LinearLayout)findViewById(R.id.ava5_liner)).setBackgroundColor(Main.COLOR_ITEM);
        ((LinearLayout)findViewById(R.id.ava6_liner)).setBackgroundColor(Main.COLOR_ITEM2);
        ((LinearLayout)findViewById(R.id.ava7_liner)).setBackgroundColor(Main.COLOR_ITEM);
        ((LinearLayout)findViewById(R.id.ava8_liner)).setBackgroundColor(Main.COLOR_ITEM2);
        ((LinearLayout)findViewById(R.id.ava9_liner)).setBackgroundColor(Main.COLOR_ITEM);
        ((LinearLayout)findViewById(R.id.ava10_liner)).setBackgroundColor(Main.COLOR_ITEM2);


        if(isNetworkConnected()) {

            smena_texta(true);
            Picasso.with(this)
                    .load(getIntent().getStringExtra("ava"))
                    .transform(transformation)
                    .into(imageView);

            d_i_p_t = new Dowload_i_Parsing_text();
            d_i_p_t.execute(getIntent().getStringExtra("item"));
        }else {
            Toast.makeText(getApplicationContext(), "нужно интернет соединение", Toast.LENGTH_SHORT).show();
        }

        //http://michurinsk-film.ru/film/item/881
        urlotz = getIntent().getStringExtra("item");
        //меняем film/item/ на review/edit?film=
        urlotz = urlotz.replace("film/item/","review/edit?film=");
        //Log.d("TTT",urlotz);

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

    public void Add_otziv(View view) {

        //http://michurinsk-film.ru/review/edit?film=880
//на адрес лолжен быть подан POST запрос с логином и паролем(если зарегистрирован)

        if(Main.save_read("login_user").length()<1){ // предложим ввести логин с паролем или зарегится
            //поставим в памяти што авторизации нет
            Main.save_value_bool("authenticate",false);
            //запустим активити с вводом
            Intent i = new Intent(getApplicationContext(), User_login_parol.class);
            i.putExtra("fl",Main.save_read_bool("fl"));
            startActivity(i);
        }else if(Main.save_read_bool("authenticate")){ //если авторизация есть ,запустим отзывы
            Intent i = new Intent(getApplicationContext(), Add_otziv.class);
            i.putExtra("fl",Main.save_read_bool("fl"));
            i.putExtra("nazvanie_value",nazvanie_value);
            i.putExtra("url_otz",urlotz);
            startActivity(i);
        }else {
            //запустим активити с вводом
            Intent i = new Intent(getApplicationContext(), User_login_parol.class);
            i.putExtra("fl",Main.save_read_bool("fl"));
            startActivity(i);
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
                    textView_nazvanie_value.setText(updete_text(proces));
                    textView_originalnoe_nazvanie_value.setText(updete_text(proces));
                    textView_opisanie_value.setText(updete_text(proces));
                    textView_prodolgitelnost_value.setText(updete_text(proces));
                    textView_kategoria_value.setText(updete_text(proces));
                    textView_ganr_value.setText(updete_text(proces));
                    textView_acteri_value.setText(updete_text(proces));
                    textView_regiser_value.setText(updete_text(proces));
                    textView_otzizi_k_filmu.setText(updete_text(proces));
                    button_logo_ava.setText(updete_text(proces));

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
                        .timeout(3000)
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

            otzivi_k_vilmu = otziv.text();

            return params[0].toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            smena_texta(false);
            textView_nazvanie_value.setText(nazvanie_value);
            textView_originalnoe_nazvanie_value.setText(originalnoe_nazvanie_value);
            textView_opisanie_value.setText(opisanie_value);
            textView_prodolgitelnost_value.setText(prodolgitelnost_value);
            textView_kategoria_value.setText(kategoria_value);
            textView_ganr_value.setText(ganr_value);
            textView_acteri_value.setText(acteri_value);
            textView_regiser_value.setText(regiser_value);
            textView_otzizi_k_filmu.setText(otzivi_k_vilmu);
            button_logo_ava.setText(updete_text(nazvanie_value));

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
