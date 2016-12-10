package dmitriy.deomin.kinooctober.User_kabinet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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
import dmitriy.deomin.kinooctober.R;


public class Registracia extends Activity{

    Regestr regestr;

    String login;
    String parol;
    String parol2;
    String mail;
    String ima;
    String fam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registracia);

        //во весь экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((LinearLayout)findViewById(R.id.fon_registracia)).setBackgroundColor(Main.COLOR_FON);

        //дастаём из памяти раннее введеные данные
        login = Main.save_read("login_user");
        parol= Main.save_read("parol_user");
        parol2= Main.save_read("parol2_user");
        mail= Main.save_read("mail_user");
        ima= Main.save_read("ima_user");
        fam= Main.save_read("fam_user");

        //запролняем ранее введые данные
        ((EditText)findViewById(R.id.reg_editText_login)).setText(login);
        ((EditText)findViewById(R.id.reg_editText_parol)).setText(parol);
        ((EditText)findViewById(R.id.reg_editText_parol_podt)).setText(parol2);
        ((EditText)findViewById(R.id.reg_editText_mail)).setText(mail);
        ((EditText)findViewById(R.id.reg_editText_name)).setText(ima);
        ((EditText)findViewById(R.id.reg_editText_famali)).setText(fam);


        //если нет заполним гугл аккаунтом
        if (((EditText) findViewById(R.id.reg_editText_login)).getText().toString().length() < 1) {
            ((EditText) findViewById(R.id.reg_editText_login)).setText(getUsername(this)); // поставим имя аккаунта
        }
        if (((EditText) findViewById(R.id.reg_editText_mail)).getText().toString().length() < 1) {
            ((EditText) findViewById(R.id.reg_editText_mail)).setText(getEmail(this)); // поставим почту аккаунта
        }


    }


    public String getUsername(Context context) {
        return  "Имя";
    }

    public String getEmail(Context context) {
      return "@email";
    }

    public void reg(View view) {

        //считывем введеные данные
        login = ((EditText)findViewById(R.id.reg_editText_login)).getText().toString();
        parol = ((EditText)findViewById(R.id.reg_editText_parol)).getText().toString();
        parol2 = ((EditText)findViewById(R.id.reg_editText_parol_podt)).getText().toString();
        mail = ((EditText)findViewById(R.id.reg_editText_mail)).getText().toString();
        ima = ((EditText)findViewById(R.id.reg_editText_name)).getText().toString();
        fam = ((EditText)findViewById(R.id.reg_editText_famali)).getText().toString();

        //сохраняем их в память
        Main.save_value("login_user",login);
        Main.save_value("parol_user",parol);
        Main.save_value("parol2_user",parol2);
        Main.save_value("mail_user",mail);
        Main.save_value("ima_user",ima);
        Main.save_value("fam_user",fam);


        if(ima.length()>1){
            Toast.makeText(getApplicationContext(), "Имя сохранено", Toast.LENGTH_SHORT).show();
        }



        //проверка введеных данных
        if(login.length()<1){
            Toast.makeText(getApplicationContext(), "Введите логин", Toast.LENGTH_SHORT).show();
            return;
        }else {
            ((EditText)findViewById(R.id.reg_editText_login)).setBackgroundColor(Color.GREEN);
        }
        if(parol.length()<1){
            Toast.makeText(getApplicationContext(), "Введите пароль", Toast.LENGTH_SHORT).show();
            return;
        }else {
            ((EditText)findViewById(R.id.reg_editText_parol)).setBackgroundColor(Color.GREEN);
        }
        if(parol2.length()<1){
            Toast.makeText(getApplicationContext(), "Введите подтверждение пароля", Toast.LENGTH_SHORT).show();
            return;
        }else {
            ((EditText)findViewById(R.id.reg_editText_parol_podt)).setBackgroundColor(Color.GREEN);
        }
        if(mail.length()<1){
            Toast.makeText(getApplicationContext(), "Введите email", Toast.LENGTH_SHORT).show();
            return;
        }else {
            ((EditText)findViewById(R.id.reg_editText_mail)).setBackgroundColor(Color.GREEN);
            Main.save_value("mail_user", ((EditText) findViewById(R.id.reg_editText_mail)).getText().toString());
        }
        if(ima.length()<1){
            Toast.makeText(getApplicationContext(), "Введите имя", Toast.LENGTH_SHORT).show();
            return;
        }else{
            ((EditText)findViewById(R.id.reg_editText_name)).setBackgroundColor(Color.GREEN);
            Main.save_value("name_user",((EditText)findViewById(R.id.reg_editText_name)).getText().toString());
        }
        if(fam.length()<1){
            Toast.makeText(getApplicationContext(), "Введите фамелию", Toast.LENGTH_SHORT).show();
            return;
        }else {
            ((EditText)findViewById(R.id.reg_editText_famali)).setBackgroundColor(Color.GREEN);
            Main.save_value("fam_user",((EditText)findViewById(R.id.reg_editText_famali)).getText().toString());
        }
        //проверка подтверждения пароля
        if(parol.equals(parol2)){
            ((EditText)findViewById(R.id.reg_editText_parol_podt)).setBackgroundColor(Color.GREEN);
        }else {
            Toast.makeText(getApplicationContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            ((EditText)findViewById(R.id.reg_editText_parol_podt)).setBackgroundColor(Color.RED);
            return;

        }


        if (isNetworkConnected()){ // если есть интернет проверим правельность

            //вклячаем анимацию
            ((ProgressBar)findViewById(R.id.progressBar3)).setVisibility(View.VISIBLE);

            regestr = new Regestr();
            regestr.execute("gogogo");

        }else {
            Toast.makeText(getApplicationContext(), "нужно интернет соединение", Toast.LENGTH_SHORT).show();
        }
    }




    class Regestr extends AsyncTask<String, Void, String> {
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
                        return Registracia();
                    case 0: // полный пинздец
                        return "Ресурс не отвечает или умер";
                    default:
                        return "Ресурс не отвечает или умер";
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
            ((ProgressBar)findViewById(R.id.progressBar3)).setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            if(s.length()==32){
                open_kabinet();
            }
        }


        private String Registracia(){
            String useragent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 YaBrowser/15.4.2272.3429 Safari/537.36";
            String loginURL = "http://michurinsk-film.ru/user-register";
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
                        .data("id","0")
                        .data("login",login)
                        .data("password", parol)
                        .data("confirm",parol2)
                        .data("email", mail)
                        .data("fname",ima)
                        .data("lname",fam)
                        .method(Connection.Method.POST)
                        .followRedirects(true);
                Connection.Response response2 = connection2.execute();

                // Log.d("TTT",response2.toString());


                //узнаем данные свои(уже должны быть авторизованы)
                //http://michurinsk-film.ru/id
                Document doc2 = Jsoup.connect("http://michurinsk-film.ru/id")
                        .cookies(response1.cookies())
                        .get();

                Elements elements = doc2.select(".login-links");
                String id_user = elements.first().select("a").attr("href").toString();

                if(id_user.length()>7){
                    //поставвим в памяти
                    Main.save_value_bool("authenticate",false);
                    return "Ошибка регистрации";
                }else
                {

                    Main.save_value_bool("authenticate",true);//если норм то поставвим в памяти
                    Main.save_value("id_user", id_user);//сохраняем ид пользователя
                    Main.save_value("login_user",login);
                    Main.save_value("parol_user",parol);
                    return "Успешно,логин и пароль сохранены";
                }


            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            }
        }

    }

    public void open_kabinet(){
//        Intent i = new Intent(Registracia.this,Kabinet.class);
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
