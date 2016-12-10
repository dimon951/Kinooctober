package dmitriy.deomin.kinooctober.Info;

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
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

import java.io.IOException;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.R;

public class Add_vopr extends Activity {


    AddVopr add_vopr;

    String login;
    String parol;

    String text_vopr;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vopr);

        context = getApplicationContext();

        //во весь экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        login = Main.save_read("login_user");
        parol = Main.save_read("parol_user");


        String name;
        if(Main.save_read("name_add_vopr").length()<1){
            if (Main.save_read("name_user").length() > 1) {
                name = "";
            } else {
                name = Main.save_read("name_user") + " " + Main.save_read("fam_user");
            }
        }else {
             name = Main.save_read("name_add_vopr");
        }


        String mil;
        if(Main.save_read("mil_add_vopr").length()<1) {
            if (Main.save_read("mail_user").length() < 1) {
                mil = "";
            } else {
                mil = Main.save_read("mail_user");
            }
        }else{
            mil = Main.save_read("mil_add_vopr");
        }


        ((EditText)findViewById(R.id.vopr_editText_name)).setText(name);
        ((EditText)findViewById(R.id.vopr_editText_email)).setText(mil);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Main.save_value("name_add_vopr", ((EditText) findViewById(R.id.vopr_editText_name)).getText().toString());
        Main.save_value("mil_add_vopr",((EditText)findViewById(R.id.vopr_editText_email)).getText().toString());
    }

    public void addOtziv(View view) {

        text_vopr = ((EditText)findViewById(R.id.vopr_editText_vopr)).getText().toString();

        if(text_vopr.length()<3){
            Toast.makeText(getApplicationContext(), "Введите текст", Toast.LENGTH_SHORT).show();
            return;
        }

        if(((EditText)findViewById(R.id.vopr_editText_name)).getText().length()<2){
            Toast.makeText(getApplicationContext(),"Введите имя",Toast.LENGTH_SHORT).show();
            return;
        }else {
            Main.save_value("name_add_vopr",((EditText)findViewById(R.id.vopr_editText_name)).getText().toString());
        }
        if(((EditText)findViewById(R.id.vopr_editText_email)).getText().length()<2){
            Toast.makeText(getApplicationContext(),"Введите email",Toast.LENGTH_SHORT).show();
            return;
        }else {
            Main.save_value("mil_add_vopr",((EditText)findViewById(R.id.vopr_editText_email)).getText().toString());
        }


        if (isNetworkConnected()){ // если есть интернет проверим правельность

            //вклячаем анимацию
            ((ProgressBar)findViewById(R.id.progressBar4)).setVisibility(View.VISIBLE);

            add_vopr = new AddVopr();
            add_vopr.execute("gogogo");

        }else {
            Toast.makeText(getApplicationContext(),"нужно интернет соединение",Toast.LENGTH_SHORT).show();
        }

    }


    class AddVopr extends AsyncTask<String, Void, String> {
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
                Connection connection3 = connection1.url("http://michurinsk-film.ru/question/ask")
                        // .timeout(7000)
                        .cookies(response1.cookies())
                        .ignoreHttpErrors(true)
                        .data("id", "0")
                        .data("owner", Main.save_read("id_user"))
                        .data("name", ((EditText) findViewById(R.id.vopr_editText_name)).getText().toString())
                        .data("email", ((EditText)findViewById(R.id.vopr_editText_email)).getText().toString())
                        .data("question", text_vopr)
                        .method(Connection.Method.POST)
                        .followRedirects(true);
                Connection.Response response3 = connection3.execute();

                return "Вопрос отправлен, будет добавлен после одобрения";


            } catch (IOException e) {
                e.printStackTrace();
            }

            return params[0].toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //выключаем анимацию показываем сообщение
            ((ProgressBar)findViewById(R.id.progressBar4)).setVisibility(View.GONE);
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
