package dmitriy.deomin.kinooctober.servis;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.R;

public class Run_update_site extends Service {

    SharedPreferences mSettings;
    Document doc;
    Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        mSettings = getSharedPreferences("mysettings", Context.MODE_PRIVATE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            Log.i("TTT", "сервис запустился ");

            //*****************читаем настройку***************************
            String set;
            if (mSettings.contains("Run_serviv_mich_kino")) {
                set = mSettings.getString("Run_serviv_mich_kino", "");
            } else {
                set = "";
            }
            //********************************************

            //если сервис включен будем шпарить**********
            if (set.equals("on")) {

                handler = new Handler();
                handler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void run() {

                        Log.i("TTT", "поток запустился ");

                        //если есть интеренет и он норм работает
                        if (isNetworkConnected()) {

                            String data_news = "";
                            String data_ras = "";
                            String signal = "";


                            Her_nevs her_nevs = new Her_nevs();
                            try {
                                data_news = her_nevs.execute().get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                            //если все норм проверилось (пустота не вернулась)
                            if (!data_news.equals("")) {

                                String old_news;
                                if (mSettings.contains("news_update_old")) {
                                    old_news = mSettings.getString("news_update_old", "");
                                } else {
                                    old_news = "";
                                }

                                if (!data_news.equals(old_news)) {
                                    signal = "0";//есть новость
                                    Log.d("TTT", "есть новость");
                                } else {
                                    Log.d("TTT", "новостей нет");
                                }

                            }


                            Her_raspisanie her_raspis = new Her_raspisanie();
                            try {
                                data_ras = her_raspis.execute().get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                            //если все норм проверилось (пустота не вернулась)
                            if (!data_ras.equals("")) {
                                String old_raspis;
                                if (mSettings.contains("ras_update_old")) {
                                    old_raspis = mSettings.getString("ras_update_old", "");
                                } else {
                                    old_raspis = "";
                                }


                                if (!data_ras.equals(old_raspis)) {
                                    Log.d("TTT", "расписания есть");
                                    if (signal.equals("0")) {
                                        signal = "2";
                                    }
                                    if (signal.equals("")) {
                                        signal = "1";
                                    }
                                } else {
                                    Log.d("TTT", "расписаний нет");
                                }

                            }


                            //если есть чето то шлём панику
                            if (!signal.equals("")) {

                                String maseg = "";

                                if (signal.equals("0")) {
                                    maseg = "Есть новая новость";
                                }

                                if (signal.equals("1")) {
                                    maseg = "Есть новое расписание";
                                }
                                if (signal.equals("2")) {
                                    maseg = "Есть новое расписание и новость";
                                }


                                //******************************************************************
                                if (!Main.visi) {
                                    //покажем уведомление
                                    Intent intent1 = new Intent(getApplicationContext(), Main.class);
                                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());
                                    taskStackBuilder.addParentStack(Main.class);
                                    taskStackBuilder.addNextIntent(intent1);
                                    PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                                    Notification notification = new Notification.Builder(getApplicationContext())
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setLargeIcon(
                                                    BitmapFactory.decodeResource(getApplicationContext()
                                                            .getResources(), R.drawable.ic_launcher))
                                            .setContentTitle("Есть обновление в кт <Октябрь>")
                                            .setAutoCancel(true)
                                            .setPriority(Notification.PRIORITY_MAX)
                                            .setDefaults(Notification.DEFAULT_VIBRATE)
                                            .setContentIntent(pendingIntent)
                                            .setContentText(maseg)
                                            .build();

                                    //запишем в память что есть новая хрень и код сохраним херни
                                    mSettings.edit().putString("run_update_start",signal).apply();

                                    NotificationManager notificationManager = (NotificationManager) getSystemService(getApplication().NOTIFICATION_SERVICE);
                                    notificationManager.notify(7, notification);

                                } else {

                                    //запишем в память что есть новая хрень и код сохраним херни
                                    mSettings.edit().putString("run_update_start",signal).apply();

                                    //послать сигнал проге если она активна
                                    Intent i = new Intent("Key_signala");
                                    i.putExtra("key_data", signal);
                                    //кому новость 0 новости, 1 расписание 2
                                    sendBroadcast(i);
                                }

                                maseg = null;
                                //***************************************************************
                            }



                            //незнаю нужно или нет но переменые обнулим
                            //-------------------------------------------
                            data_news = null;
                            data_ras = null;
                            signal = null;
                            her_nevs = null;
                            her_raspis = null;
                            doc = null;
                            //--------------------------------------------
                        }
                        //запускает поток опять
                        handler.postDelayed(this, 1000 * 3600); // 3600  вроде как час
                    }

                });
            } else {
                //иначе вырубим его
                stopSelf();
            }
            //*********************************************************************************
        return Service.START_STICKY_COMPATIBILITY;
    }



   private class  Her_nevs extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... params) {


            try {
                doc = Jsoup.connect("http://michurinsk-film.ru/news/").timeout(3000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(doc!=null){

                Element element_blok = doc.select(".block").first();
                if(element_blok!=null){

                    Element element_news = element_blok.select(".news-list").get(0);
                    if(element_news!=null){

                        Elements element_date = element_news.select(".date");
                        if(element_date.isEmpty()){

                            return  element_date.text();

                        }else return "";

                    }else return "";

                }else return "";

            }else {
                return "";
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            doc = null;
        }
    }

  private class  Her_raspisanie extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... params) {
            try {
                doc = Jsoup.connect("http://michurinsk-film.ru/film/sessions").timeout(3000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(doc!=null){

                //сделаем проверки и если все пучком вернём

                Element element_blok = doc.select(".block").first();
                if(element_blok!=null){

                    Element element_event = element_blok.select(".event").get(0);
                    if(element_event!=null){

                        Elements element_title = element_event.select(".title");

                        if(element_title.isEmpty()){

                            return element_title.text();

                        }else {return "";}

                    }else {return "";}

                }else {return "";}

            }else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            doc = null;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        // проверка подключения
        if (activeNetwork != null && activeNetwork.isConnected()) {
            Proverka_speed ps = new Proverka_speed();
            try {
                return  ps.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return false;
            }

        }

        return false;
    }

  private class Proverka_speed extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // тест доступности внешнего ресурса
                URL url = new URL("http://michurinsk-film.ru/film/sessions");
                HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
                urlc.setConnectTimeout(700); // Timeout в секундах
                urlc.connect();
                // статус ресурса OK
                if (urlc.getResponseCode() == 200) {
                    return true;
                }
                // иначе проверка провалилась
                return false;

            } catch (IOException e) {
                Log.d("my_tag", "Ошибка проверки подключения к интернету", e);
                return false;
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler=null;
        Log.i("TTT","сервису пиздец");
        Toast.makeText(getApplicationContext(),"Проверка обновлений остановлена",Toast.LENGTH_SHORT).show();
    }
}
