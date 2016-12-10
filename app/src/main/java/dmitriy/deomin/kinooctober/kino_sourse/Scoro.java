package dmitriy.deomin.kinooctober.kino_sourse;


import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.R;

public class Scoro extends Fragment implements SwipeRefreshLayout.OnRefreshListener{




    //скоро
    ArrayList title_list_premiere = new ArrayList();
    ArrayList item_list_premiera = new ArrayList();
    ArrayList ava_list_premiere = new ArrayList();
    ArrayList data_list_premiere = new ArrayList();
    ArrayList description_list_premiere = new ArrayList();

    Dowload_i_Parsing_text d_i_p_t; // поток будет брать инфу с сайта
    int page_dowloads_skoro; //скоро
    boolean run_potok;

    Context context;

    final String AVA = "image";
    final String TITLE = "title";
    final String NACHALO ="nachalo";
    final String OPISANIE = "description";

    String [] title = {};
    String [] ava = {};
    String [] nachalo = {}; // data
    String [] description = {};  //opisanie";
    String [] itemg = {}; //страница с подробностями

    ListView listView;


    TextView no_content;

    public  SwipeRefreshLayout swipeLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.scoro,null);

        context = v.getContext();
        page_dowloads_skoro = 1;
        run_potok = false;



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
            title = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_title_premiere").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_title_premiere").size(); i++) {
                title[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_title_premiere").get(i);
            }
        //item_list_premiere
            itemg = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"item_list_premiere").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "item_list_premiere").size(); i++) {
                itemg[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "item_list_premiere").get(i);
             }
            //ava
            ava = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_ava_premiere").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_ava_premiere").size(); i++) {
                ava[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_ava_premiere").get(i);
            }
            //data
            nachalo = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_data_premiere").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_data_premiere").size(); i++) {
                nachalo[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_data_premiere").get(i);
            }
            //description
            description = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_description_premiere").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_description_premiere").size(); i++) {
                description[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_description_premiere").get(i);
            }

        ArrayList<Map<String,Object>> data = new ArrayList<Map<String,Object>>(title.length);

        Map<String,Object> m ;

        for(int i = 0;i<title.length;i++){
            m= new HashMap<String,Object>();
            m.put(AVA,(ava.length>i)?ava[i]:"http://cs14113.vk.me/c623228/v623228725/277ce/lhi2VNZbS_g.jpg");
            m.put(TITLE,(title.length>i)?title[i]:"названия нет");
            m.put(NACHALO,(nachalo.length>i)?nachalo[i]:"неизвестно");
            m.put(OPISANIE,(description.length>i)?description[i]:"Без описания");
            data.add(m);
        }

        listView = (ListView) v.findViewById(R.id.listView_premiera);
        Adapter_Premiera adapterPremiera = new Adapter_Premiera(v.getContext(),data,R.layout.delegat_premiere,null,null);
        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(adapterPremiera);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);


        //Обрабатываем щелчки на элементах ListView:
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //реализация
                String url = ((ava.length>position)?ava[position].toString():"");
                String it = ((itemg.length>position)?itemg[position].toString():"");
                Intent i = new Intent(v.getContext(), Ava_skoro.class);
                i.putExtra("ava", url);
                i.putExtra("item",it);
                i.putExtra("fl",Main.save_read_bool("fl"));
                startActivity(i);
            }
        });



        no_content =  ((TextView)v.findViewById(R.id.textView_tab_refresh_skoro));
        no_content.setVisibility((title.length > 0) ? View.GONE : View.VISIBLE);

        return v;
    }


    class Dowload_i_Parsing_text extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            //проверяем што страничка работает
            try {
                switch (Proverochka(params[0].toString())){
                    case 200: //все норм
                        return Scoro(params[0].toString());
                    case 0: // полный пинздец
                        return "Ресурс не отвечает";
                    default: return "Ресурс не отвечает";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Ресурс не отвечает";
            }

        }

        private String Scoro(String s){
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

            if(doc!= null){
                pars_i_save_premiere(doc.select(".block").first());
            }else{
                return "Ресурс не отвечает";
            }

            return s;
        }


        public void  pars_i_save_premiere(Element value){// парсим скоро

            Element element_page = value.select(".paginationControl").first();
            if(element_page != null) {
                Elements page_count = element_page.select(".nopage");

                if(page_count != null) {
                    ArrayList page_skoro = new ArrayList();
                    for (int i = 0; i != page_count.size(); i++) {
                        page_skoro.add(page_count.get(i).text());
                    }
                    Main.setStringArrayPref(context, "element_page_skoro", page_skoro);
                }
            }else{
                ArrayList page_skoro = new ArrayList();
                Main.setStringArrayPref(context, "element_page_skoro", page_skoro);
            }

            Elements elements = value.select(".event");
            for(int i=0;i!=elements.size();i++){
                title_list_premiere.add(elements.get(i).select(".title").text());//title
                item_list_premiera.add("http://michurinsk-film.ru" + elements.get(i).select(".title").select("a").attr("href"));// подробная страница

                ava_list_premiere.add("http://michurinsk-film.ru" + elements.get(i).select(".event-image").select("img").attr("src"));//ava
                data_list_premiere.add(elements.get(i).select(".date").text());
                description_list_premiere.add(elements.get(i).select(".description").text());//opisanie
            }



        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            posle_potoka(s); //обработка после потока
        }
    }


    public  void posle_potoka(String s){

        run_potok = false;
        if(s.contains("premiere")) {

            ArrayList page = new ArrayList();//при первом запуске в нег записывается общее количество страниц
            page = Main.getStringArrayPref(context, "element_page_skoro");

            if(page.size() == page_dowloads_skoro ||page.size()< page_dowloads_skoro){ // если все страницы загрузились
                save_skoro_data();// сохраняем данные
                Main.open_skoro();
               if(title.length == 0){
                   no_content.setText("Возможно на сайте идут работы,попробуйте попоже");
               }
                return;
            }

            if(page.size()>1| page_dowloads_skoro <page.size()){
                if(page_dowloads_skoro ==1){//если это первая страница показываем общее количество
                    d_i_p_t = new Dowload_i_Parsing_text();
                    page_dowloads_skoro = page_dowloads_skoro +1;
                    d_i_p_t.execute("http://michurinsk-film.ru/film/premiere/"+String.valueOf(page_dowloads_skoro));
                }else{
                    d_i_p_t = new Dowload_i_Parsing_text();
                    page_dowloads_skoro = page_dowloads_skoro +1;
                    d_i_p_t.execute("http://michurinsk-film.ru/film/premiere/"+String.valueOf(page_dowloads_skoro));
                }


            }else{
                save_skoro_data();// сохраняем данные
                Main.open_skoro();
                if(title.length == 0){
                    no_content.setText("Возможно на сайте идут работы,попробуйте попоже");
                }
            }

        }else{
            run_potok = false;
            swipeLayout.setRefreshing(false);
             Main.Toast_error("Ошибка");
        }
//================================================================================================
    }

    public  void save_skoro_data(){
        Main.setStringArrayPref(context, "element_title_premiere", title_list_premiere);
        Main.setStringArrayPref(context, "item_list_premiere", item_list_premiera);
        Main.setStringArrayPref(context, "element_ava_premiere", ava_list_premiere);
        Main.setStringArrayPref(context, "element_data_premiere", data_list_premiere);
        Main.setStringArrayPref(context, "element_description_premiere", description_list_premiere);
    }


        @Override
    public void onRefresh() {

        if(isNetworkConnected()) {

            if (!run_potok) {

                     d_i_p_t = new Dowload_i_Parsing_text();// создаём поток
                    //очишщаем списки
                    title_list_premiere.clear();
                    ava_list_premiere.clear();
                    data_list_premiere.clear();
                    description_list_premiere.clear();

                    page_dowloads_skoro = 1;

                    run_potok = true;
                    d_i_p_t.execute("http://michurinsk-film.ru/film/premiere");
            }else { //run_potok
                Main.Toast("Уже обновляется");
            }
        }else {//isNetworkConnected()
            swipeLayout.setRefreshing(false);
            Main.Toast(getString(R.string.netu_internetu));
        }
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
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }
}
