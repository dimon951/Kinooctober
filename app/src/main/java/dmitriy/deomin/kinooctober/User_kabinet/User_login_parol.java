package dmitriy.deomin.kinooctober.User_kabinet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.Mamori;
import dmitriy.deomin.kinooctober.R;


public class User_login_parol extends Activity {

    Autefikacia autefikacia;

    String login;
    String parol;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_parol);
        context = getApplicationContext();


        //во весь экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((LinearLayout)findViewById(R.id.fon_user_logi_i_parol)).setBackgroundColor(Main.COLOR_FON);


        login = Main.save_read("login_user");
        parol = Main.save_read("parol_user");


        ((EditText)findViewById(R.id.editText_login)).setText(login);
        ((EditText)findViewById(R.id.editText_parol)).setText(parol);
       // ((EditText) findViewById(R.id.editText_parol)).setRawInputType(InputType.TYPE_CLASS_TEXT);
    }

    public void clik_login_parol_ok(View view) {
        login = ((EditText)findViewById(R.id.editText_login)).getText().toString();
        parol = ((EditText)findViewById(R.id.editText_parol)).getText().toString();
        // сохраним если че
        Main.save_value("login_user", login);
        Main.save_value("parol_user", parol);
        if (isNetworkConnected()){ // если есть интернет проверим правельность

            //вклячаем анимацию
            ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);

            autefikacia = new Autefikacia();
            autefikacia.execute("gogogo");

        }else {
            Toast.makeText(getApplicationContext(), " нужно интернет соединение", Toast.LENGTH_SHORT).show();
        }

    }

    public void mamori(View view) {
        Intent i = new Intent(getApplicationContext(),Mamori.class);
        startActivity(i);
    }

    public void registracia(View view) {
        Intent i = new Intent(getApplicationContext(),Registracia.class);
        startActivity(i);
    }

    public void Pokazat_porol(View view) {

        ((EditText)findViewById(R.id.editText_parol)).setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);

//        if(((EditText)findViewById(R.id.editText_parol)).getInputType()== InputType.TYPE_TEXT_VARIATION_PASSWORD){
//            ((EditText)findViewById(R.id.editText_parol)).setRawInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
//        }else{
//           // Toast.makeText(this,"метод срабатывает",Toast.LENGTH_LONG).show();
//            ((EditText) findViewById(R.id.editText_parol)).setRawInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        }
   }


    class Autefikacia extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            //проверяем што страничка работает
            try {
                switch (Proverochka(params[0].toString())){
                    case 200: //все норм
                        return Userloginparol();
                    case 0: // полный пинздец
                        return "Ресурс не отвечает или умер";
                    default: return "Ресурс не отвечает или умер";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //выключаем анимацию показываем сообщение
            ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.GONE);
            Toast.makeText(context,s,Toast.LENGTH_LONG).show();
            if(s.length()==56){
                open_kabinet();
            }
        }

        private String Userloginparol(){
            String useragent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 YaBrowser/15.4.2272.3429 Safari/537.36";
            String loginURL = "http://michurinsk-film.ru/auth/authenticate";
            try {
                //получаем страницу входа
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

                //узнаем данные свои(уже должны быть авторизованы)
                //http://michurinsk-film.ru/id
                Document doc2 = Jsoup.connect("http://michurinsk-film.ru/id")
                        .cookies(response1.cookies())
                        .get();



                Elements elements = doc2.select(".login-links");
                String id_user = elements.first().select("a").attr("href").toString().replace("/id",""); //   /id541

                ///Log.d("TTT","\n\n\n\n===================\n\n\n"+id_user);

                //сохраняем ид пользователя
                Main.save_value("id_user", id_user);

                if(id_user.length()>7){
                    //поставвим в памяти
                    Main.save_value_bool("authenticate",false);
                    return "Ошибка, проверте логин или пароль";
                }else
                {
                    //если норм то поставвим в памяти
                    Main.save_value_bool("authenticate",true);
                    return "Успешно,теперь можно добавлять отзывы и задавать вопросы";
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            }
        }

    }

    public void open_kabinet(){
//        Intent i = new Intent(User_login_parol.this,Kabinet.class);
//        i.putExtra("fl",Main.save_read_bool("fl"));
//        startActivity(i);
    }

    private int Proverochka(String u) throws IOException {
        URL url = new URL(u);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            connection.connect();
            int statusCode = connection.getResponseCode();
            return statusCode;
        } catch (UnknownHostException e) {
            return 0;
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
