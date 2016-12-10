package dmitriy.deomin.kinooctober.kino_sourse;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.R;

public class Sessions extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


     int green = Color.rgb(25, 164, 21); // цвет зала
     int red = Color.RED;
     int blue = Color.BLUE;

    //сеансы
     ArrayList title_list_sessions = new ArrayList();
     ArrayList item_list_sessions = new ArrayList();
     ArrayList ava_list_sessions = new ArrayList();
     ArrayList data_list_sessions = new ArrayList();
     ArrayList description_list_sessions = new ArrayList();
     ArrayList time_vse_list_sessions = new ArrayList(); // все расписания



    //первый час
     ArrayList time1_list_sessions= new ArrayList(); //время
     ArrayList time1_color_sessions = new ArrayList(); // цвет зала
     ArrayList time1_format_sessions = new ArrayList(); //качество видео
    //второй час
     ArrayList time2_list_sessions= new ArrayList(); //время
     ArrayList time2_color_sessions = new ArrayList(); // цвет зала
     ArrayList time2_format_sessions = new ArrayList(); //качество видео
    //третий час
     ArrayList time3_list_sessions= new ArrayList(); //время
     ArrayList time3_color_sessions = new ArrayList(); // цвет зала
     ArrayList time3_format_sessions = new ArrayList(); //качество видео
    //4
     ArrayList time4_list_sessions= new ArrayList(); //время
     ArrayList time4_color_sessions = new ArrayList(); // цвет зала
     ArrayList time4_format_sessions = new ArrayList(); //качество видео
    //5
     ArrayList time5_list_sessions= new ArrayList(); //время
     ArrayList time5_color_sessions = new ArrayList(); // цвет зала
     ArrayList time5_format_sessions = new ArrayList(); //качество видео
    //6
     ArrayList time6_list_sessions= new ArrayList(); //время
     ArrayList time6_color_sessions = new ArrayList(); // цвет зала
     ArrayList time6_format_sessions = new ArrayList(); //качество видео
    //7
     ArrayList time7_list_sessions= new ArrayList(); //время
     ArrayList time7_color_sessions = new ArrayList(); // цвет зала
     ArrayList time7_format_sessions = new ArrayList(); //качество видео
    //8
     ArrayList time8_list_sessions= new ArrayList(); //время
     ArrayList time8_color_sessions = new ArrayList(); // цвет зала
     ArrayList time8_format_sessions = new ArrayList(); //качество видео

    Dowload_i_Parsing_text d_i_p_t; // поток будет брать инфу с сайта
    Context context;

    int page_dowloads_sessions; // какую загружать страницу расписаний

    final String AVA = "image";
    final String TITLE = "title";
    final String ITEM_PODROBNO = "item_podrobno";

    final String NACHALO ="nachalo";
    final String OPISANIE = "description";
    final String RASPISANIE_VSE = "vsetime";
    final String UPDATE_DATE = "update_date";

    final String TIME1 = "time1";
    final String TIME1COLOR = "time1color";
    final String TIME1FORMAT = "time1format";

    final String TIME2 = "time2";
    final String TIME2COLOR = "time2color";
    final String TIME2FORMAT = "time2format";

    final String TIME3 = "time3";
    final String TIME3COLOR = "time3color";
    final String TIME3FORMAT = "time3format";

    final String TIME4 = "time4";
    final String TIME4COLOR = "time4color";
    final String TIME4FORMAT = "time4format";

    final String TIME5 = "time5";
    final String TIME5COLOR = "time5color";
    final String TIME5FORMAT = "time5format";

    final String TIME6 = "time6";
    final String TIME6COLOR = "time6color";
    final String TIME6FORMAT = "time6format";

    final String TIME7 = "time7";
    final String TIME7COLOR = "time7color";
    final String TIME7FORMAT = "time7format";

    final String TIME8 = "time8";
    final String TIME8COLOR = "time8color";
    final String TIME8FORMAT = "time8format";

    ListView listView;
    public SwipeRefreshLayout swipeLayout;

    String [] title = {};
    String [] item = {};
    String [] ava = {};
    String [] nachalo = {}; // data
    String [] description = {};  //opisanie";
    String [] ras_vse = {};

    //первый час
    String[] text_time1_text = {};
    String[] text_time1_color_text ={};
    String[] text_time1_format_text ={};

    String[] text_time2_text = {};
    String[] text_time2_color_text ={};
    String[] text_time2_format_text ={};

    String[] text_time3_text = {};
    String[] text_time3_color_text ={};
    String[] text_time3_format_text ={};

    String[] text_time4_text = {};
    String[] text_time4_color_text ={};
    String[] text_time4_format_text ={};

    String[] text_time5_text = {};
    String[] text_time5_color_text ={};
    String[] text_time5_format_text ={};

    String[] text_time6_text = {};
    String[] text_time6_color_text ={};
    String[] text_time6_format_text ={};

    String[] text_time7_text = {};
    String[] text_time7_color_text ={};
    String[] text_time7_format_text ={};

    String[] text_time8_text = {};
    String[] text_time8_color_text ={};
    String[] text_time8_format_text ={};

    String data_update;

    boolean fl;
    boolean run_potok;

    TextView no_content;

    BroadcastReceiver broadcastReceiver;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.sessions,null);

        context = v.getContext();

        //обновление тянуть вниз-------------------------------------------------------------
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.conteiner_swipe);
        swipeLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        swipeLayout.setColorSchemeColors(
                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        swipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //-------------------------------------------------------------------------------------------


        page_dowloads_sessions = 1;
        run_potok = false;

            //title
            title = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_title_sessions").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_title_sessions").size(); i++) {
                Main.save_value("ras_update_old",Main.getStringArrayPref(getActivity().getApplicationContext(), "element_title_sessions").get(0));
                title[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_title_sessions").get(i);
            }

            //item_list_sessions
            item = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"item_list_sessions").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "item_list_sessions").size(); i++) {
                item[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "item_list_sessions").get(i);
            }

            //ava
            ava = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_ava_sessions").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_ava_sessions").size(); i++) {
                ava[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_ava_sessions").get(i);
            }

            //data
            nachalo = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_data_sessions").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_data_sessions").size(); i++) {
                nachalo[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_data_sessions").get(i);
            }
            //description
            description = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_description_sessions").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_description_sessions").size(); i++) {
                description[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_description_sessions").get(i);
            }

            //time
            ras_vse = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time_vse_sessions").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time_vse_sessions").size(); i++) {
                ras_vse[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time_vse_sessions").get(i);
            }
//1
            text_time1_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_time1_sessions").size()]; // время
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time1_sessions").size(); i++) {
                text_time1_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time1_sessions").get(i);
            }
            text_time1_color_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_time1_color_sessions").size()]; // цвет зала
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time1_color_sessions").size(); i++) {
                text_time1_color_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time1_color_sessions").get(i);
            }
            text_time1_format_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time1_format_sessions").size()]; // цвет зала
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time1_format_sessions").size(); i++) {
                text_time1_format_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time1_format_sessions").get(i);
            }
//2
            text_time2_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_time2_sessions").size()]; // время
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time2_sessions").size(); i++) {
                text_time2_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time2_sessions").get(i);
            }
            text_time2_color_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_time2_color_sessions").size()]; // цвет зала
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time2_color_sessions").size(); i++) {
                text_time2_color_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time2_color_sessions").get(i);
            }
            text_time2_format_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_time2_format_sessions").size()]; // цвет зала
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time2_format_sessions").size(); i++) {
                text_time2_format_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time2_format_sessions").get(i);
            }
//3
            text_time3_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_time3_sessions").size()]; // время
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(),"element_time3_sessions").size(); i++) {
                text_time3_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(),"element_time3_sessions").get(i);
            }
            text_time3_color_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_time3_color_sessions").size()]; // цвет зала
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(),"element_time3_color_sessions").size(); i++) {
                text_time3_color_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(),"element_time3_color_sessions").get(i);
            }
            text_time3_format_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_time3_format_sessions").size()]; // цвет зала
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(),"element_time3_format_sessions").size(); i++) {
                text_time3_format_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(),"element_time3_format_sessions").get(i);
            }
//4
            text_time4_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time4_sessions").size()]; // время
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time4_sessions").size(); i++) {
                text_time4_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time4_sessions").get(i);
            }
            text_time4_color_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time4_color_sessions").size()]; // цвет зала
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time4_color_sessions").size(); i++) {
                text_time4_color_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time4_color_sessions").get(i);
            }
            text_time4_format_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time4_format_sessions").size()]; // цвет зала
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time4_format_sessions").size(); i++) {
                text_time4_format_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time4_format_sessions").get(i);
            }
//5
            text_time5_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time5_sessions").size()]; // время
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time5_sessions").size(); i++) {
                text_time5_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time5_sessions").get(i);
            }
            text_time5_color_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time5_color_sessions").size()]; // цвет зала
            for (int i = 0; i !=Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time5_color_sessions").size(); i++) {
                text_time5_color_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time5_color_sessions").get(i);
            }
            text_time5_format_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time5_format_sessions").size()]; // цвет зала
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time5_format_sessions").size(); i++) {
                text_time5_format_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time5_format_sessions").get(i);
            }
//6
            text_time6_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time6_sessions").size()]; // время
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time6_sessions").size(); i++) {
                text_time6_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time6_sessions").get(i);
            }
            text_time6_color_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time6_color_sessions").size()]; // цвет зала
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time6_color_sessions").size(); i++) {
                text_time6_color_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time6_color_sessions").get(i);
            }
            text_time6_format_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time6_format_sessions").size()]; // цвет зала
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time6_format_sessions").size(); i++) {
                text_time6_format_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time6_format_sessions").get(i);
            }

//7
            text_time7_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time7_sessions").size()]; // время
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time7_sessions").size(); i++) {
                text_time7_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time7_sessions").get(i);
            }
            text_time7_color_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time7_color_sessions").size()]; // цвет зала
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time7_color_sessions").size(); i++) {
                text_time7_color_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time7_color_sessions").get(i);
            }
            text_time7_format_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time7_format_sessions").size()]; // цвет зала
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time7_format_sessions").size(); i++) {
                text_time7_format_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time7_format_sessions").get(i);
            }

//8
            text_time8_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time8_sessions").size()]; // время
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time8_sessions").size(); i++) {
                text_time8_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time8_sessions").get(i);
            }
            text_time8_color_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time8_color_sessions").size()]; // цвет зала
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time8_color_sessions").size(); i++) {
                text_time8_color_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time8_color_sessions").get(i);
            }
            text_time8_format_text = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time8_format_sessions").size()]; // цвет зала
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time8_format_sessions").size(); i++) {
                text_time8_format_text[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_time8_format_sessions").get(i);
            }

            //дата обновления инфы
            data_update = Main.save_read("data_good");

            fl = Main.save_read_bool("fl");

           no_content = ((TextView)v.findViewById(R.id.textView_tab_refresh_sessions));
           no_content.setVisibility((title.length>0)?View.GONE:View.VISIBLE);




        ArrayList<Map<String,Object>> data = new ArrayList<Map<String,Object>>(title.length);

        Map<String,Object> m ;

        for(int i = 0;i<title.length;i++){
            m= new HashMap<String,Object>();

            m.put(AVA,(ava.length>i)?ava[i]:"http://cs14113.vk.me/c623228/v623228725/277ce/lhi2VNZbS_g.jpg");
            m.put(TITLE,(title.length>i)?title[i]:"названия нет");
            m.put(ITEM_PODROBNO,(item.length>i)?item[i]:"");
            m.put(NACHALO,(nachalo.length>i)?nachalo[i]:"неизвестно");
            m.put(OPISANIE,(description.length>i)? description[i] : "Без описания");
            m.put(RASPISANIE_VSE,(ras_vse.length>i)?ras_vse[i]:"Нет расписания");

            m.put(TIME1,(text_time1_text.length>i)?text_time1_text[i]:"0");
            m.put(TIME1COLOR,(text_time1_color_text.length>i)?text_time1_color_text[i]:"0");
            m.put(TIME1FORMAT,(text_time1_format_text.length>i)?text_time1_format_text[i]:"0");

            m.put(TIME2,(text_time2_text.length>i)?text_time2_text[i]:"0");
            m.put(TIME2COLOR,(text_time2_color_text.length>i)?text_time2_color_text[i]:"0");
            m.put(TIME2FORMAT,(text_time2_format_text.length>i)?text_time2_format_text[i]:"0");

            m.put(TIME3,(text_time3_text.length>i)?text_time3_text[i]:"0");
            m.put(TIME3COLOR,(text_time3_color_text.length>i)?text_time3_color_text[i]:"0");
            m.put(TIME3FORMAT,(text_time3_format_text.length>i)?text_time3_format_text[i]:"0");

            m.put(TIME4,(text_time4_text.length>i)?text_time4_text[i]:"0");
            m.put(TIME4COLOR,(text_time4_color_text.length>i)?text_time4_color_text[i]:"0");
            m.put(TIME4FORMAT,(text_time4_format_text.length>i)?text_time4_format_text[i]:"0");

            m.put(TIME5,(text_time5_text.length>i)?text_time5_text[i]:"0");
            m.put(TIME5COLOR,(text_time5_color_text.length>i)?text_time5_color_text[i]:"0");
            m.put(TIME5FORMAT,(text_time5_format_text.length>i)?text_time5_format_text[i]:"0");

            m.put(TIME6,(text_time6_text.length>i)?text_time6_text[i]:"0");
            m.put(TIME6COLOR,(text_time6_color_text.length>i)?text_time6_color_text[i]:"0");
            m.put(TIME6FORMAT,(text_time6_format_text.length>i)?text_time6_format_text[i]:"0");

            m.put(TIME7,(text_time7_text.length>i)?text_time7_text[i]:"0");
            m.put(TIME7COLOR,(text_time7_color_text.length>i)?text_time7_color_text[i]:"0");
            m.put(TIME7FORMAT,(text_time7_format_text.length>i)?text_time7_format_text[i]:"0");

            m.put(TIME8,(text_time8_text.length>i)?text_time8_text[i]:"0");
            m.put(TIME8COLOR,(text_time8_color_text.length>i)?text_time8_color_text[i]:"0");
            m.put(TIME8FORMAT,(text_time8_format_text.length>i)?text_time8_format_text[i]:"0");

            //дата обновления
            m.put(UPDATE_DATE,data_update);


            data.add(m);
        }

        listView = (ListView) v.findViewById(R.id.listView_ras);
        Adapter_Raspisan adapterRaspisan = new Adapter_Raspisan(v.getContext(),data,R.layout.delegat_sessions,null,null);
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapterRaspisan);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);


        //слушаем main вот такой костыль
        //***************************************************************************
        //фильтр для нашего сигнала из сервиса
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Key_signala_main_her");

        //приёмник  сигналов
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("Key_signala_main_her")) {
                    onRefresh();
                }
            }
        };

        //регистрируем приёмник
        context.registerReceiver(broadcastReceiver,intentFilter);
        //*******************************************************************************************



        return v;
    }//onCreateView

    @Override
    public void onRefresh() {
        if(isNetworkConnected()) {

            if (!run_potok) {
                context.unregisterReceiver(broadcastReceiver);
                d_i_p_t = new Dowload_i_Parsing_text();// создаём поток

                title_list_sessions.clear();
                item_list_sessions.clear();
                ava_list_sessions.clear();
                data_list_sessions.clear();
                description_list_sessions.clear();
                time_vse_list_sessions.clear(); // все расписания

                time1_list_sessions.clear(); // первый час
                time1_color_sessions.clear();
                time1_format_sessions.clear();

                time2_list_sessions.clear(); // второй час
                time2_color_sessions.clear();
                time2_format_sessions.clear();

                time3_list_sessions.clear(); // 3 час
                time3_color_sessions.clear();
                time3_format_sessions.clear();

                time4_list_sessions.clear(); // 4 час
                time4_color_sessions.clear();
                time4_format_sessions.clear();

                time5_list_sessions.clear(); // 5 час
                time5_color_sessions.clear();
                time5_format_sessions.clear();

                time6_list_sessions.clear(); // 6 час
                time6_color_sessions.clear();
                time6_format_sessions.clear();

                time7_list_sessions.clear(); // 7 час
                time7_color_sessions.clear();
                time7_format_sessions.clear();

                time8_list_sessions.clear(); // 8 час
                time8_color_sessions.clear();
                time8_format_sessions.clear();

                page_dowloads_sessions = 1; // первая страница

                run_potok = true;
                d_i_p_t.execute("http://michurinsk-film.ru/film/sessions"); // запускаем поток

            }else{ //!run_potok
                Main.Toast_error("Уже обновляется");
            }
        }else{ //isNetworkConnected()
            swipeLayout.setRefreshing(false);
            Main.Toast_error(getString(R.string.netu_internetu));
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }


    public void save_sesions_data(){
        //сохраняем массивы строк в память
        Main.setStringArrayPref(context, "element_time_vse_sessions", time_vse_list_sessions);
        //первый час
        Main.setStringArrayPref(context, "element_time1_sessions", time1_list_sessions);
        Main.setStringArrayPref(context, "element_time1_color_sessions", time1_color_sessions);
        Main.setStringArrayPref(context, "element_time1_format_sessions", time1_format_sessions);
        //2
        Main.setStringArrayPref(context, "element_time2_sessions", time2_list_sessions);
        Main.setStringArrayPref(context, "element_time2_color_sessions", time2_color_sessions);
        Main.setStringArrayPref(context, "element_time2_format_sessions", time2_format_sessions);
        //3
        Main.setStringArrayPref(context, "element_time3_sessions", time3_list_sessions);
        Main.setStringArrayPref(context, "element_time3_color_sessions", time3_color_sessions);
        Main.setStringArrayPref(context, "element_time3_format_sessions", time3_format_sessions);
        //4
        Main.setStringArrayPref(context, "element_time4_sessions", time4_list_sessions);
        Main.setStringArrayPref(context, "element_time4_color_sessions", time4_color_sessions);
        Main.setStringArrayPref(context, "element_time4_format_sessions", time4_format_sessions);
        //5
        Main.setStringArrayPref(context, "element_time5_sessions", time5_list_sessions);
        Main.setStringArrayPref(context, "element_time5_color_sessions", time5_color_sessions);
        Main.setStringArrayPref(context, "element_time5_format_sessions", time5_format_sessions);
        //6
        Main.setStringArrayPref(context, "element_time6_sessions", time6_list_sessions);
        Main.setStringArrayPref(context, "element_time6_color_sessions", time6_color_sessions);
        Main.setStringArrayPref(context, "element_time6_format_sessions", time6_format_sessions);
        //7
        Main.setStringArrayPref(context, "element_time7_sessions", time7_list_sessions);
        Main.setStringArrayPref(context, "element_time7_color_sessions", time7_color_sessions);
        Main.setStringArrayPref(context, "element_time7_format_sessions", time7_format_sessions);
        //8
        Main.setStringArrayPref(context, "element_time8_sessions", time8_list_sessions);
        Main.setStringArrayPref(context, "element_time8_color_sessions", time8_color_sessions);
        Main.setStringArrayPref(context, "element_time8_format_sessions", time8_format_sessions);

        //если все заебись сохраняем дату обновления
        String d = (String) DateFormat.format("dd", new Date()); //yyyy-MM-dd kk:mm:ss   день
        String m = return_mesac((String) DateFormat.format("M", new Date()));
        Main.save_value("data_good", d + m);

    }

    public String return_mesac(String nomer_mecaca){
        switch (nomer_mecaca){
            case ("1"):
                return " Января";
            case ("2"):
                return " Февраля";
            case ("3"):
                return " Марта";
            case ("4"):
                return " Апреля";
            case ("5"):
                return " Мая";
            case ("6"):
                return " Июня";
            case ("7"):
                return " Июля";
            case ("8"):
                return " Августа";
            case ("9"):
                return " Сентября";
            case ("10"):
                return " Октября";
            case ("11"):
                return " Ноября";
            case ("12"):
                return " Декабря";
        }
        return nomer_mecaca;
    }


     class Dowload_i_Parsing_text extends AsyncTask<String, Void, String> {
         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             // Toast.makeText(getApplicationContext(), "Подключение и обновление...",Toast.LENGTH_SHORT).show();
         }

         @Override
         protected String doInBackground(String... params) {
             //проверяем што страничка работает
             try {
                 switch (Proverochka(params[0].toString())){
                     case 200: //все норм
                         return P_i_SAVE_sessions(params[0].toString());
                     case 0: // полный пинздец
                         return "Ресурс не отвечает";
                     default: return "Ресурс не отвечает";
                 }
             } catch (IOException e) {
                 e.printStackTrace();
                 return "Ресурс не отвечает";
             }
         }

         public void pars_i_save_sessions(Element value){
             Element element_page = value.select(".paginationControl").first();
             if(element_page != null) {
                 Elements page_count = element_page.select(".nopage");

                 if(page_count != null) {
                     ArrayList page_sessions = new ArrayList();
                     for (int i = 0; i != page_count.size(); i++) {
                         page_sessions.add(page_count.get(i).text());
                     }
                     Main.setStringArrayPref(context, "element_page_sessions", page_sessions);
                 }
             }else{
                 ArrayList page_sessions = new ArrayList();
                 Main.setStringArrayPref(context, "element_page_sessions", page_sessions);
             }




             Elements elements = value.select(".event");
             for(int i=0;i!=elements.size();i++){
                 title_list_sessions.add(elements.get(i).select(".title").text());//title
                 item_list_sessions.add("http://michurinsk-film.ru" + elements.get(i).select(".title").select("a").attr("href")); // подробная страница

                 ava_list_sessions.add("http://michurinsk-film.ru" + elements.get(i).select("img[src$=.jpg]").attr("src"));//ava
                 data_list_sessions.add(elements.get(i).select(".date").text());//data
                 description_list_sessions.add(elements.get(i).select(".description").text());//opisanie
             }
             Main.setStringArrayPref(context, "element_title_sessions", title_list_sessions);
             Main.setStringArrayPref(context, "item_list_sessions", item_list_sessions);
             Main.setStringArrayPref(context, "element_ava_sessions", ava_list_sessions);
             Main.setStringArrayPref(context, "element_data_sessions", data_list_sessions);
             Main.setStringArrayPref(context, "element_description_sessions", description_list_sessions);

//время сеансов сегодня
             Elements date_time_vse = value.select(".sessions-list"); // куча дат надо вытаскивать

             for(int i=0;i!=date_time_vse.size();i++) {
                 if(date_time_vse.get(i).text().contains("Сеансы сегодня:")){
//1
                     if(date_time_vse.get(i).select("span").size()>0) {
                         if (date_time_vse.get(i).select("span").get(0).select(".price-info") != null |
                                 date_time_vse.get(i).select("span").get(0).attr("alt") != null |
                                 date_time_vse.get(i).select("span").get(0).attr("title") != null) {

                             time1_list_sessions.add(date_time_vse.get(i).select("span").get(0).select(".price-info").text());

                             if (date_time_vse.get(i).select("span").get(0).attr("alt").toString().contains("Синий зал")) {
                                 time1_color_sessions.add(blue);  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(0).attr("alt").toString().contains("Зелёный зал")) {
                                 time1_color_sessions.add(green);  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(0).attr("alt").toString().contains("Красный зал")) {
                                 time1_color_sessions.add(red);  // цвет зала для первого часы
                             }

                             if (date_time_vse.get(i).select("span").get(0).attr("title").toString().contains("3D")) {
                                 time1_format_sessions.add("3D");  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(0).attr("title").toString().contains("2D")) {
                                 time1_format_sessions.add("2D");  // цвет зала для первого часы
                             }
                         }
                     }else {
                         time1_list_sessions.add("0");
                         time1_color_sessions.add("0");
                         time1_format_sessions.add("0");
                     }

                     //2
                     if(date_time_vse.get(i).select("span").size()>1) {
                         if (date_time_vse.get(i).select("span").get(1).select(".price-info") != null |
                                 date_time_vse.get(i).select("span").get(1).attr("alt") != null |
                                 date_time_vse.get(i).select("span").get(1).attr("title") != null) {

                             time2_list_sessions.add(date_time_vse.get(i).select("span").get(1).select(".price-info").text()); // только первый час , время

                             if (date_time_vse.get(i).select("span").get(1).attr("alt").toString().contains("Синий зал")) {
                                 time2_color_sessions.add(blue);  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(1).attr("alt").toString().contains("Зелёный зал")) {
                                 time2_color_sessions.add(green);  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(1).attr("alt").toString().contains("Красный зал")) {
                                 time2_color_sessions.add(red);  // цвет зала для первого часы
                             }

                             if (date_time_vse.get(i).select("span").get(1).attr("title").toString().contains("3D")) {
                                 time2_format_sessions.add("3D");  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(1).attr("title").toString().contains("2D")) {
                                 time2_format_sessions.add("2D");  // цвет зала для первого часы
                             }
                         }
                     }else {
                         time2_list_sessions.add("0");
                         time2_color_sessions.add("0");
                         time2_format_sessions.add("0");
                     }

//3
                     if(date_time_vse.get(i).select("span").size()>2) {
                         if (date_time_vse.get(i).select("span").get(2).select(".price-info") != null |
                                 date_time_vse.get(i).select("span").get(2).attr("alt") != null |
                                 date_time_vse.get(i).select("span").get(2).attr("title") != null) {

                             time3_list_sessions.add(date_time_vse.get(i).select("span").get(2).select(".price-info").text()); // только первый час , время

                             if (date_time_vse.get(i).select("span").get(2).attr("alt").toString().contains("Синий зал")) {
                                 time3_color_sessions.add(blue);  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(2).attr("alt").toString().contains("Зелёный зал")) {
                                 time3_color_sessions.add(green);  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(2).attr("alt").toString().contains("Красный зал")) {
                                 time3_color_sessions.add(red);  // цвет зала для первого часы
                             }

                             if (date_time_vse.get(i).select("span").get(2).attr("title").toString().contains("3D")) {
                                 time3_format_sessions.add("3D");  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(2).attr("title").toString().contains("2D")) {
                                 time3_format_sessions.add("2D");  // цвет зала для первого часы
                             }
                         }
                     }else {
                         time3_list_sessions.add("0");
                         time3_color_sessions.add("0");
                         time3_format_sessions.add("0");
                     }

//4
                     if(date_time_vse.get(i).select("span").size()>3) {
                         if (date_time_vse.get(i).select("span").get(3).select(".price-info") != null |
                                 date_time_vse.get(i).select("span").get(3).attr("alt") != null |
                                 date_time_vse.get(i).select("span").get(3).attr("title") != null) {

                             time4_list_sessions.add(date_time_vse.get(i).select("span").get(3).select(".price-info").text()); // только первый час , время

                             if (date_time_vse.get(i).select("span").get(3).attr("alt").toString().contains("Синий зал")) {
                                 time4_color_sessions.add(blue);  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(3).attr("alt").toString().contains("Зелёный зал")) {
                                 time4_color_sessions.add(green);  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(3).attr("alt").toString().contains("Красный зал")) {
                                 time4_color_sessions.add(red);  // цвет зала для первого часы
                             }

                             if (date_time_vse.get(i).select("span").get(3).attr("title").toString().contains("3D")) {
                                 time4_format_sessions.add("3D");  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(3).attr("title").toString().contains("2D")) {
                                 time4_format_sessions.add("2D");  // цвет зала для первого часы
                             }
                         }
                     }else {
                         time4_list_sessions.add("0");
                         time4_color_sessions.add("0");
                         time4_format_sessions.add("0");
                     }
//5
                     if(date_time_vse.get(i).select("span").size()>4) {
                         if (date_time_vse.get(i).select("span").get(4).select(".price-info") != null |
                                 date_time_vse.get(i).select("span").get(4).attr("alt") != null |
                                 date_time_vse.get(i).select("span").get(4).attr("title") != null) {

                             time5_list_sessions.add(date_time_vse.get(i).select("span").get(4).select(".price-info").text()); // только первый час , время

                             if (date_time_vse.get(i).select("span").get(4).attr("alt").toString().contains("Синий зал")) {
                                 time5_color_sessions.add(blue);  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(4).attr("alt").toString().contains("Зелёный зал")) {
                                 time5_color_sessions.add(green);  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(4).attr("alt").toString().contains("Красный зал")) {
                                 time5_color_sessions.add(red);  // цвет зала для первого часы
                             }

                             if (date_time_vse.get(i).select("span").get(4).attr("title").toString().contains("3D")) {
                                 time5_format_sessions.add("3D");  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(4).attr("title").toString().contains("2D")) {
                                 time5_format_sessions.add("2D");  // цвет зала для первого часы
                             }
                         }
                     }else {
                         time5_list_sessions.add("0");
                         time5_color_sessions.add("0");
                         time5_format_sessions.add("0");
                     }
//6
                     if(date_time_vse.get(i).select("span").size()>5) {
                         if (date_time_vse.get(i).select("span").get(5).select(".price-info") != null |
                                 date_time_vse.get(i).select("span").get(5).attr("alt") != null |
                                 date_time_vse.get(i).select("span").get(5).attr("title") != null) {

                             time6_list_sessions.add(date_time_vse.get(i).select("span").get(5).select(".price-info").text()); // только первый час , время

                             if (date_time_vse.get(i).select("span").get(5).attr("alt").toString().contains("Синий зал")) {
                                 time6_color_sessions.add(blue);  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(5).attr("alt").toString().contains("Зелёный зал")) {
                                 time6_color_sessions.add(green);  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(5).attr("alt").toString().contains("Красный зал")) {
                                 time6_color_sessions.add(red);  // цвет зала для первого часы
                             }

                             if (date_time_vse.get(i).select("span").get(5).attr("title").toString().contains("3D")) {
                                 time6_format_sessions.add("3D");  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(5).attr("title").toString().contains("2D")) {
                                 time6_format_sessions.add("2D");  // цвет зала для первого часы
                             }
                         }
                     }else {
                         time6_list_sessions.add("0");
                         time6_color_sessions.add("0");
                         time6_format_sessions.add("0");
                     }

//7
                     if(date_time_vse.get(i).select("span").size()>6) {
                         if (date_time_vse.get(i).select("span").get(6).select(".price-info") != null |
                                 date_time_vse.get(i).select("span").get(6).attr("alt") != null |
                                 date_time_vse.get(i).select("span").get(6).attr("title") != null) {

                             time7_list_sessions.add(date_time_vse.get(i).select("span").get(6).select(".price-info").text()); // только первый час , время

                             if (date_time_vse.get(i).select("span").get(6).attr("alt").toString().contains("Синий зал")) {
                                 time7_color_sessions.add(blue);  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(6).attr("alt").toString().contains("Зелёный зал")) {
                                 time7_color_sessions.add(green);  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(6).attr("alt").toString().contains("Красный зал")) {
                                 time7_color_sessions.add(red);  // цвет зала для первого часы
                             }

                             if (date_time_vse.get(i).select("span").get(6).attr("title").toString().contains("3D")) {
                                 time7_format_sessions.add("3D");  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(6).attr("title").toString().contains("2D")) {
                                 time7_format_sessions.add("2D");  // цвет зала для первого часы
                             }
                         }
                     }else {
                         time7_list_sessions.add("0");
                         time7_color_sessions.add("0");
                         time7_format_sessions.add("0");
                     }

//8
                     if(date_time_vse.get(i).select("span").size()>7) {
                         if (date_time_vse.get(i).select("span").get(7).select(".price-info") != null |
                                 date_time_vse.get(i).select("span").get(7).attr("alt") != null |
                                 date_time_vse.get(i).select("span").get(7).attr("title") != null) {

                             time8_list_sessions.add(date_time_vse.get(i).select("span").get(7).select(".price-info").text()); // только первый час , время

                             if (date_time_vse.get(i).select("span").get(7).attr("alt").toString().contains("Синий зал")) {
                                 time8_color_sessions.add(blue);  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(7).attr("alt").toString().contains("Зелёный зал")) {
                                 time8_color_sessions.add(green);  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(7).attr("alt").toString().contains("Красный зал")) {
                                 time8_color_sessions.add(red);  // цвет зала для первого часы
                             }

                             if (date_time_vse.get(i).select("span").get(7).attr("title").toString().contains("3D")) {
                                 time8_format_sessions.add("3D");  // цвет зала для первого часы
                             }
                             if (date_time_vse.get(i).select("span").get(7).attr("title").toString().contains("2D")) {
                                 time8_format_sessions.add("2D");  // цвет зала для первого часы
                             }
                         }
                     }else {
                         time8_list_sessions.add("0");
                         time8_color_sessions.add("0");
                         time8_format_sessions.add("0");
                     }
                 }
                 if(date_time_vse.get(i).text().toString().contains("Дата Сеансы")){
                    //тут надо поппарится
                     String tablica = "";
                     Elements elements_tabl = date_time_vse.get(i).select("tbody > tr");
                     for (Element e : elements_tabl) {
                         if(!e.text().equals("Дата Сеансы")) {
                             tablica += e.text() + "\n";
                         }
                     }

                     time_vse_list_sessions.add(tablica);
                 }
             }
         }


         @Override
         protected void onPostExecute(String s) {
             super.onPostExecute(s);
             posle_potoka(s); //обработка после потока
         }

         private String P_i_SAVE_sessions(String s){
             Document doc = null;
             try {
                 doc = Jsoup
                         .connect(s)
                         .timeout(3000)
                         .get();

             } catch (IOException e) {
                 e.printStackTrace();
                return  "Ресурс не отвечает";
             }

             if(doc != null){
                 // отправляем дальше на переработку )
                 pars_i_save_sessions(doc.select(".block").first());
             }else {
                 return "Ресурс не отвечает";
             }


             return s;
         }
     }


    public void posle_potoka(String s){

         run_potok = false;
        if(s.contains("sessions")) {

            ArrayList page = new ArrayList();//при первом запуске в нег записывается общее количество страниц
            page = Main.getStringArrayPref(context, "element_page_sessions");

            if(page.size() == page_dowloads_sessions ||page.size()< page_dowloads_sessions){ // если все страницы загрузились
                save_sesions_data(); // сохраняем данные
                Main.open_session(); // загружаем даанные во фрагмент
                if(title.length==0){
                    no_content.setText("Возможно на сайте идут работы,попробуйте попоже");
                }
                return;
            }

            if(page.size()>1| page_dowloads_sessions <page.size()){
                if(page_dowloads_sessions ==1){//если это первая страница показываем общее количество
                    d_i_p_t = new Dowload_i_Parsing_text();
                    page_dowloads_sessions = page_dowloads_sessions +1;
                    d_i_p_t.execute("http://michurinsk-film.ru/film/sessions/"+String.valueOf(page_dowloads_sessions));
                }else{
                    d_i_p_t = new Dowload_i_Parsing_text();
                    page_dowloads_sessions = page_dowloads_sessions +1;
                    d_i_p_t.execute("http://michurinsk-film.ru/film/sessions/"+String.valueOf(page_dowloads_sessions));
                }

            }else{
                save_sesions_data(); // сохраняем данные
                Main.open_session(); // загружаем дааные во фрагмент
                if(title.length==0){
                    no_content.setText("Возможно на сайте идут работы,попробуйте попоже");
                }
            }
        }else{
            run_potok = false;
            swipeLayout.setRefreshing(false);
            Main.Toast_error("Ошибка");
        }
    } //posle_potoka

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
}
