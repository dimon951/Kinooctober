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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.R;

public class News extends Fragment implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {

    final String TITLE_NEWS = "title_news";
    final String DATA_NEWS ="data_news";
    final String AVA_NEWS = "ava_news";
    final String COMENTT = "description";//text
    final String COMENTT_AVA = "ava_dis";//image v discription
    String format_v_normul;

    String [] titl_news = {};
    String [] data_news = {}; // data
    String [] ava_news =  {};
    String [] comentl = {};  //текст
    String [] coment2 = {};  //картинка в самой новости

    String [] item = {}; //подробно
    boolean fl;
    boolean run_potok;
    int page_news; // страница новостей
    int count_page_news;//всего страниц новостей
    Dowload_i_Parsing_text d_i_p_t; // поток будет брать инфу с сайта
    Context context;

    Button pag_go;
    Button pag_back;
    TextView count_page;

    TextView no_content;

    ListView listView;
    public SwipeRefreshLayout swipeLayout;

   BroadcastReceiver broadcastReceiver;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.news, null);
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

        //title
        titl_news = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_title_news").size()];
        for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_title_news").size(); i++) {
            titl_news[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_title_news").get(i);
        }

        //ava
        ava_news = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"ava_list_news").size()];
        for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "ava_list_news").size(); i++) {
            ava_news[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "ava_list_news").get(i);
        }

        //item_list_news
        item = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"item_list_news").size()];
        for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "item_list_news").size(); i++) {
            item[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "item_list_news").get(i);
        }

        //data
        data_news = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_data_news").size()];
        for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_data_news").size(); i++) {
            Main.save_value("news_update_old",Main.getStringArrayPref(getActivity().getApplicationContext(), "element_data_news").get(0));
            data_news[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_data_news").get(i);
        }
        //coment
        comentl = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_description_news").size()];
        for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_description_news").size(); i++) {
            comentl[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_description_news").get(i);
        }
        //coment2
        coment2 = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_description_news_ava").size()];
        for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_description_news_ava").size(); i++) {
            coment2[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_description_news_ava").get(i);
        }


        fl= Main.save_read_bool("fl");

        no_content = (TextView)v.findViewById(R.id.textView_tab_refresh_news);
        no_content.setVisibility((titl_news.length > 0) ? View.GONE : View.VISIBLE);

        ArrayList<Map<String,Object>> data = new ArrayList<Map<String,Object>>(titl_news.length);

        Map<String,Object> m ;


        for(int i = 0;i< titl_news.length;i++){
            m= new HashMap<String,Object>();
            m.put(TITLE_NEWS,(titl_news.length>i)?titl_news[i]:"названия нет");
            m.put(AVA_NEWS,(ava_news.length>i)?ava_news[i]:"названия нет");
            m.put(DATA_NEWS,(data_news.length>i)?data_news[i]:"названия нет");
            m.put(COMENTT,(comentl.length>i)?comentl[i]:"названия нет");
            m.put(COMENTT_AVA,(coment2.length>i)?coment2[i]:"названия нет"); //
            data.add(m);
        }

        String[] from = {
                TITLE_NEWS,
                AVA_NEWS,
                DATA_NEWS,
                COMENTT,
                COMENTT_AVA
        };
        int[] to = {
                R.id.news_textView_title,
                R.id.news_imageView_ava,
                R.id.news_textView_data,
                R.id.news_textView_text,
                R.id.ava_discriptions
        };



        listView = (ListView) v.findViewById(R.id.listView_news);
        Adapter_News adapter_news = new Adapter_News(v.getContext(),data, R.layout.delegat_news,from,to);
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter_news);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);



        run_potok = false;
        page_news = Integer.valueOf((Main.save_read("page_news").length()<1)?"1":Main.save_read("page_news"));
        count_page_news = Integer.valueOf((Main.save_read("count_page_news").length()<1)?"40":Main.save_read("count_page_news"));

        pag_go = ((Button)v.findViewById(R.id.page3));
        pag_back = ((Button)v.findViewById(R.id.pag1));
        pag_go.setOnClickListener(this);
        pag_back.setOnClickListener(this);
        count_page = ((TextView)v.findViewById(R.id.textView_nomer_tekusey_stranici));

        count_page.setText(String.valueOf(page_news) + "(" + String.valueOf(count_page_news) + ")");

//Обрабатываем щелчки на элементах ListView:
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                  String it = ((item.length>position)?item[position].toString():"");
                  String av = ((ava_news.length>position)?ava_news[position].toString():"");
                  String av_d = ((coment2.length>position)?coment2[position].toString():"");

                Intent i = new Intent(v.getContext(), Ava_news.class);
                i.putExtra("ava", av);
                i.putExtra("ava_dis", av_d);
                i.putExtra("fl",fl);
                i.putExtra("item",it);
                startActivity(i);

            }
        });




        //слушаем main вот такой костыль
        //***************************************************************************
        //фильтр для нашего сигнала из сервиса
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Key_signala_main_news");

        //приёмник  сигналов
         broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("Key_signala_main_news")) {
                   onRefresh();
                }
            }
        };

        //регистрируем приёмник
        context.registerReceiver(broadcastReceiver,intentFilter);
        //*******************************************************************************************

        return v;
    }

    @Override
    public void onRefresh() {
        if(isNetworkConnected()) {
            if (!run_potok) {
                context.unregisterReceiver(broadcastReceiver);
                d_i_p_t = new Dowload_i_Parsing_text();// создаём поток
                d_i_p_t.execute("http://michurinsk-film.ru/news/"+String.valueOf(page_news));
                run_potok = true;
            }else{ //!run_potok
               Main.Toast("Уже обновляется");
            }
        }else{ //isNetworkConnected()
            swipeLayout.setRefreshing(false);
            Main.Toast_error(getString(R.string.netu_internetu));
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==pag_go.getId()) {
            if (page_news < count_page_news) {
                page_news++;
                Main.save_value("page_news", String.valueOf(page_news)); //сохраняем в память чтоб при следующей загрузке
                count_page.setText(String.valueOf(page_news) + "(" + String.valueOf(count_page_news) + ")");
                swipeLayout.setRefreshing(true);
                onRefresh();
            } else {
                Main.Toast("Последняя страница");
            }
        }
        if(view.getId()==pag_back.getId()) {
            if (page_news > 1) {
                page_news--;
                Main.save_value("page_news", String.valueOf(page_news)); //сохраняем в память чтоб при следующей загрузке
                count_page.setText(String.valueOf(page_news) + "(" + String.valueOf(count_page_news) + ")");
                swipeLayout.setRefreshing(true);
                onRefresh();
            } else {
                Main.Toast("Первая страница");
            }
        }
    }


    class Dowload_i_Parsing_text extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //проверяем што страничка работает
            try {
                switch (Proverochka(params[0].toString())){
                    case 200: //все норм
                        return News(params[0].toString());
                    case 0: // полный пинздец
                        return "Ресурс не отвечает";
                    default: return "Ресурс не отвечает";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        private String News(String s){
            Document doc = null;
            try {
                doc = Jsoup
                        .connect(s)
                        .timeout(3000)
                        .get();

            } catch (IOException e) {
                e.printStackTrace();
                return "Ресурс не отвечает";
            }
            // и парсим тело
            if(doc!=null){
                pars_i_save_news(doc.select(".block").first());
            }else {
                return "Ресурс не отвечает";
            }

            return s;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            posle_potoka(s); //обработка после потока
        }
    }


    public  void posle_potoka(String s){
        if(!s.contains("Ресурс не отвечает")) {
            run_potok = false;
            Main.open_news();
            if(titl_news.length == 0){
                no_content.setText("Возможно на сайте идут работы,попробуйте попоже");
            }

        }else{
            run_potok = false;
            swipeLayout.setRefreshing(false);
            Main.Toast_error("Ошибка");
        }
    }

    public void pars_i_save_news(Element value){
        ArrayList titll_news = new ArrayList(); // название новости
        ArrayList item_news = new ArrayList(); // страница с подробностями
        ArrayList data_news = new ArrayList(); // дата
        ArrayList ava_news = new ArrayList(); // авa
        ArrayList text_news = new ArrayList();// текс
        ArrayList text_news_ava = new ArrayList();// картинкка из новостей

        count_page_news = Integer.valueOf(value.select(".nopage").last().text());
        Main.save_value("count_page_news", String.valueOf(count_page_news));


        Elements elements = value.select(".news-list");
        for(int i = 0;i!=elements.size();i++){
            titll_news.add(elements.get(i).select(".title").text());//title
            item_news.add("http://michurinsk-film.ru" + elements.get(i).select(".title").select("a").attr("href")); //item

            data_news.add(elements.get(i).select(".date").text());//data


            ava_news.add("http://michurinsk-film.ru"+elements.get(i).select(".news-image").select("img").attr("src"));//ava
            // ava_news.add("http://michurinsk-film.ru"+elements.get(i).select(".news-image").get(i).select("img").attr("src"));//ava если нужно будет все картинки выташит
/*
            if(elements.get(i).select(".news-image").select("img").attr("src").length() < 5){
                ava_news.add("http://michurinsk-film.ru"+elements.get(i).select(".description").select("img").attr("src").toString());
            } else {
                ava_news.add("http://michurinsk-film.ru" + elements.get(i).select(".news-image").select("img").attr("src").toString());
            }

            //Log.w("TTT",ava_news.toString());
*/





            text_news.add(elements.get(i).select(".description").text());//text
//            format_v_normul = elements.get(i).select(".description").select("img").attr("src");
//            try {
//                encode(format_v_normul, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }

//            try {
//                format_v_normul = URLEncoder.encode(elements.get(i).select(".description").select("img").attr("src").toString(), "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }

            //            String encodedUrl = URLEncoder.encode(url, "UTF-8");
//            System.out.println("Encoded URL " + encodedUrl);
//            String decodedUrl = URLDecoder.decode(url, "UTF-8");
//            System.out.println("Dncoded URL " + decodedUrl);

            //криворучим велосипед
            String str =  elements.get(i).select(".description").select("img").attr("src").toString();

            if(str.lastIndexOf("/")>10){
                String f;
                f = str.substring(str.lastIndexOf("/")+1);
              //  Log.d("TTT",str + "---"+str.lastIndexOf("/")+"ttttttttttt");
                try {
                f= URLEncoder.encode(f,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            format_v_normul = str.substring(0, str.lastIndexOf("/")+1)+f.replace("+", "%20");


            text_news_ava.add("http://michurinsk-film.ru" + format_v_normul);
          //  Log.d("TTT", "http://michurinsk-film.ru" + format_v_normul + " dolgen bit adres kartinki");
            }else{
                text_news_ava.add("http://michurinsk-film.ru");//забиваем пустотооой
                // Log.d("TTT", "забиваем пустотой");
            }

        }



        Main.setStringArrayPref(context, "element_title_news", titll_news);
        Main.setStringArrayPref(context, "item_list_news", item_news);
        Main.setStringArrayPref(context, "element_data_news", data_news);
        Main.setStringArrayPref(context, "ava_list_news", ava_news);
        Main.setStringArrayPref(context, "element_description_news", text_news);
        Main.setStringArrayPref(context, "element_description_news_ava", text_news_ava);

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