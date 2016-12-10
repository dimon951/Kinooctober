package dmitriy.deomin.kinooctober.kino_sourse;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

import java.io.IOException;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.R;

public class Add_otziv extends Activity {


    AddOtziv add_otziv;

    String url_otziva;

    String login;
    String parol;

    String zagolovok;
    String text_otziva;


    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_otziv);

        context = getApplicationContext();

        //во весь экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        login = Main.save_read("login_user");
        parol = Main.save_read("parol_user");

        ((TextView)findViewById(R.id.textView_name_film_otziv)).setText(getIntent().getExtras().getString("nazvanie_value"));

        //http://michurinsk-film.ru/review/edit?film=880
        url_otziva = getIntent().getExtras().getString("url_otz");
    }

    public void addOtziv(View view) {

        zagolovok = ((EditText)findViewById(R.id.editText_add_zagolovok)).getText().toString();
        text_otziva = ((EditText)findViewById(R.id.editText_add_text_otziva)).getText().toString();

        if(zagolovok.length()<1){
            Toast.makeText(getApplicationContext(),"Введите заголовок",Toast.LENGTH_SHORT).show();
            return;
        }
        if(text_otziva.length()<3){
            Toast.makeText(getApplicationContext(),"Введите текст отзыва",Toast.LENGTH_SHORT).show();
            return;
        }


        if (isNetworkConnected()){ // если есть интернет проверим правельность

            //вклячаем анимацию
            ((ProgressBar)findViewById(R.id.progressBar2)).setVisibility(View.VISIBLE);

            add_otziv = new AddOtziv();
            add_otziv.execute("gogogo");

        }else {
            Toast.makeText(getApplicationContext(),"Для добавления отзыва нужно интернет соединение",Toast.LENGTH_SHORT).show();
        }

    }


    class AddOtziv extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String useragent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 YaBrowser/15.4.2272.3429 Safari/537.36";
            String loginURL = "http://michurinsk-film.ru/auth/authenticate";
                try {
                    //получаем страницу входа аутефикации
                    Connection connection1 = HttpConnection.connect(loginURL)
                            .ignoreHttpErrors(true)
                            .userAgent(useragent);
                    Connection.Response response1 = connection1
                            .execute();

                    //делаем пост запрос
                    Connection connection2 = connection1.url(loginURL)
                            // .timeout(7000)
                            .cookies(response1.cookies())
                            .ignoreHttpErrors(true)
                            .data("redirect","/auth")
                            .data("login", login)
                            .data("password", parol)
                            .data("rememberme", "0")
                            .method(Connection.Method.POST)
                            .followRedirects(true);
                    Connection.Response response2 = connection2.execute();

                //делаем пост запрос
                Connection connection3 = connection1.url(url_otziva)
                        // .timeout(7000)
                        .cookies(response1.cookies())
                        .ignoreHttpErrors(true)
                        .data("id", "0")
                        .data("film_id", url_otziva.replace("http://michurinsk-film.ru/review/edit?film=", "")) ////http://michurinsk-film.ru/review/edit?film=880 = 880
                        .data("owner", Main.save_read("id_user"))
                        .data("title",zagolovok)
                        .data("description", text_otziva)
                        .data("status","0")
                        .data("submit","Сохранить")
                        .method(Connection.Method.POST)
                        .followRedirects(true);
                Connection.Response response3 = connection3.execute();


                    return "Отзыв отправлен, будет добавлен после одобрения";


            } catch (IOException e) {
                e.printStackTrace();
            }
           // Log.d("TTT","\nid film "+url_otziva.replace("http://michurinsk-film.ru/review/edit?film=", ""));
            //Log.d("TTT","\nid user "+Main.save_read("id_user"));

            return params[0].toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //выключаем анимацию показываем сообщение
            ((ProgressBar)findViewById(R.id.progressBar2)).setVisibility(View.GONE);
            Toast.makeText(context,s,Toast.LENGTH_LONG).show();
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
