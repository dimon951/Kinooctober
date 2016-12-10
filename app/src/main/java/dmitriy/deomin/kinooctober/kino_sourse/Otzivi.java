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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Otzivi extends Fragment implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{



    static int page_otzivi; // страница отзовов
    static int count_page_otzivi; //всего страниц отзовов
    Context context;
    Dowload_i_Parsing_text d_i_p_t; // поток будет брать инфу с сайта


    final String TITLE_COM = "title_com";
    final String AVTOR = "avtor";
    final String FILM = "film";
    final String DATA_COM ="data_com";
    final String COMENT = "coment";

    String [] title_com = {};
    String [] avtorl = {};
    String [] film = {};
    String [] data_com = {}; // data
    String [] comentl = {};  //текст коментария

    String [] item = {}; //подробно
    boolean fl;
    boolean run_potok;

    Button pag_go;
    Button pag_back;
    TextView count_page;


    ListView listView;

    TextView no_content;

    public SwipeRefreshLayout swipeLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.otzivi, null);

        context = v.getContext();
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
            title_com = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_title_otzivi").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_title_otzivi").size(); i++) {
                title_com[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_title_otzivi").get(i);
            }

            //avtor
            avtorl = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"avtor_list_otzivi").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "avtor_list_otzivi").size(); i++) {
                avtorl[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "avtor_list_otzivi").get(i);
            }

            //film
            film = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"film_list_otzivi").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "film_list_otzivi").size(); i++) {
                film[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "film_list_otzivi").get(i);
            }

            //item_list_otzivi
            item = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"item_list_otzivi").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "item_list_otzivi").size(); i++) {
                item[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "item_list_otzivi").get(i);
            }

            //data
            data_com = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_data_otzivi").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_data_otzivi").size(); i++) {
                data_com[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_data_otzivi").get(i);
            }
            //coment
            comentl = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_description_otzivi").size()];
            for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_description_otzivi").size(); i++) {
                comentl[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_description_otzivi").get(i);
            }
            fl= Main.save_read_bool("fl");



        ArrayList<Map<String,Object>> data = new ArrayList<Map<String,Object>>(title_com.length);

        Map<String,Object> m ;

        for(int i = 0;i< title_com.length;i++){
            m= new HashMap<String,Object>();
            m.put(TITLE_COM,(title_com.length>i)? title_com[i]:"названия нет");
            m.put(AVTOR,(avtorl.length>i)? avtorl[i]:"Без автора");
            m.put(FILM,(film.length>i)?film[i]:"Без названия");
            m.put(DATA_COM,(data_com.length>i)?data_com[i]:"неизвестно");
            m.put(COMENT,(comentl.length>i)? comentl[i]:"Без описания");
            data.add(m);
        }

        Adapter_otzivi adapter_otzivi = new Adapter_otzivi(v.getContext(),data,R.layout.delegat_otzivi,null,null);

        listView = (ListView) v.findViewById(R.id.listView_otzivi);
        listView.setAdapter(adapter_otzivi);

//Обрабатываем щелчки на элементах ListView:
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //String url = ava[position].toString();
                String it = item[position].toString();
                Intent i = new Intent(v.getContext(), Ava_otzivi.class);
                //i.putExtra("ava", url);
                i.putExtra("fl",fl);
                i.putExtra("item",it);
                startActivity(i);
            }
        });

        pag_go = ((Button)v.findViewById(R.id.page3));
        pag_back = ((Button)v.findViewById(R.id.pag1));
        pag_go.setOnClickListener(this);
        pag_back.setOnClickListener(this);
        count_page = ((TextView)v.findViewById(R.id.textView_nomer_tekusey_stranici));



        page_otzivi = Integer.valueOf((Main.save_read("page_otzivi").length()<1)?"1":Main.save_read("page_otzivi"));
        count_page_otzivi = Integer.valueOf((Main.save_read("count_page_otzivi").length()<1) ? "4" : Main.save_read("count_page_otzivi"));

        count_page.setText(String.valueOf(page_otzivi) + "(" + String.valueOf(count_page_otzivi) + ")");


        no_content =  ((TextView)v.findViewById(R.id.textView_tab_refresh_otzivi));
        no_content.setVisibility((title_com.length>0)?View.GONE:View.VISIBLE);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        listView.smoothScrollToPosition(0); // при обновлении проматываем список к началу
    }

    @Override
    public void onRefresh() {
        if(isNetworkConnected()) {

            if (!run_potok) {

                d_i_p_t = new Dowload_i_Parsing_text();// создаём поток
                d_i_p_t.execute("http://michurinsk-film.ru/reviews/" + String.valueOf(page_otzivi));
                run_potok = true;
            }else{ //!run_potok
                Main.Toast_error("Уже обновляется");
            }
        }else{ //isNetworkConnected()
            swipeLayout.setRefreshing(false);
            Main.Toast_error(getString(R.string.netu_internetu));
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==pag_go.getId()) {
            if (page_otzivi < count_page_otzivi) {
                page_otzivi++;
                Main.save_value("page_otzivi", String.valueOf(page_otzivi)); //сохраняем в память чтоб при следующей загрузке
                count_page.setText(String.valueOf(page_otzivi) + "(" + String.valueOf(count_page_otzivi) + ")");
                swipeLayout.setRefreshing(true);
                onRefresh();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Последняя страница", Toast.LENGTH_SHORT).show();
            }
        }
        if(view.getId()==pag_back.getId()) {
            if (page_otzivi > 1) {
                page_otzivi--;
                Main.save_value("page_otzivi", String.valueOf(page_otzivi)); //сохраняем в память чтоб при следующей загрузке
                count_page.setText(String.valueOf(page_otzivi) + "(" + String.valueOf(count_page_otzivi) + ")");
                swipeLayout.setRefreshing(true);
                onRefresh();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Первая страница", Toast.LENGTH_SHORT).show();
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
                        return Otzivi(params[0].toString());
                    case 0: // полный пинздец
                        return "Ресурс не отвечает";
                    default: return "Ресурс не отвечает";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Ресурс не отвечает";
            }

        }


        private String Otzivi(String s){
            Document doc = null;
            try {
                doc = Jsoup
                        .connect(s)
                        .timeout(3000)
                        .get();

            } catch (IOException e) {
                return "Ресурс не отвечает";
            }

            if(doc!=null){
                pars_i_save_reviews(doc.select(".block").first());
            }else{
                return "Ресурс не отвечает";
            }

            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            posle_potoka(s); //обработка после потока
        }




        public void pars_i_save_reviews(Element value){
            ArrayList titl_otzivi = new ArrayList(); // название коментария
            ArrayList item_otzivi = new ArrayList(); // страница с подробностями
            ArrayList data_otzovi = new ArrayList(); // дата отзова
            ArrayList avtor = new ArrayList(); // автор
            ArrayList film =  new ArrayList(); // название фильма
            ArrayList comentariy = new ArrayList();// сам коментраий

             count_page_otzivi = Integer.valueOf(value.select(".nopage").last().text());
            Main.save_value("count_page_otzivi", String.valueOf(count_page_otzivi));


            Elements elements = value.select(".event");
            for(int i =0;i!=elements.size();i++){
                titl_otzivi.add(elements.get(i).select(".title").text());//title + item
                item_otzivi.add("http://michurinsk-film.ru" + elements.get(i).select(".title").select("a").attr("href")); // подробная страница

                data_otzovi.add(elements.get(i).select(".date").text());
                avtor.add(elements.get(i).select(".events-item.author").text().replace("Автор:", ""));//avtor
                film.add(elements.get(i).select(".events-item.film-title").text().replace("Фильм:",""));//film
                comentariy.add(elements.get(i).select(".description").text());// описание
            }
            Main.setStringArrayPref(context, "element_title_otzivi", titl_otzivi);
            Main.setStringArrayPref(context, "item_list_otzivi", item_otzivi);
            Main.setStringArrayPref(context, "element_data_otzivi", data_otzovi);
            Main.setStringArrayPref(context, "avtor_list_otzivi", avtor);
            Main.setStringArrayPref(context, "film_list_otzivi", film);
            Main.setStringArrayPref(context, "element_description_otzivi", comentariy);
        }


    }

    public  void posle_potoka(String s){
        if(!s.equals("Ресурс не отвечает")){
            run_potok = false;
            Main.open_otzivi();
            if(title_com.length==0){
                no_content.setText("Возможно на сайте идут работы,попробуйте попоже");
            }

        }else {
            run_potok = false;
            swipeLayout.setRefreshing(false);
            Main.Toast_error("Ресурс не отвечает");
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
