package dmitriy.deomin.kinooctober.Info;


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

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
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

public class Vopr extends Fragment implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{



    static int page_vopr; // страница отзовов
    static int count_page_vopr; //всего страниц отзовов
    Context context;
    Dowload_i_Parsing_text d_i_p_t; // поток будет брать инфу с сайта


    final String VOPROS = "vopros";
    final String OTVET = "otvet";

    String [] vopr = {};
    String [] otv = {};
    String [] item = {}; //подробно

    boolean fl;
    boolean run_potok;

    Button pag_go;
    Button pag_back;
    TextView count_page;

    Button add_vopr;

    String login;
    String parol;


    ListView listView;
    public SwipeRefreshLayout swipeLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.vopr, null);

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


        login = Main.save_read("login_user");
        parol = Main.save_read("parol_user");

        //vopros
        vopr = new String[Main.getStringArrayPref(getActivity().getApplicationContext(), "element_vopr").size()];
        for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_vopr").size(); i++) {
            vopr[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_vopr").get(i);
        }

        //otvet
       otv = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_otv").size()];
        for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_otv").size(); i++) {
            otv[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_otv").get(i);
        }

        //item
        item = new String[Main.getStringArrayPref(getActivity().getApplicationContext(),"element_otv_item").size()];
        for (int i = 0; i != Main.getStringArrayPref(getActivity().getApplicationContext(), "element_otv_item").size(); i++) {
            item[i] = Main.getStringArrayPref(getActivity().getApplicationContext(), "element_otv_item").get(i);
        }

        fl= Main.save_read_bool("fl");


        ((TextView)v.findViewById(R.id.textView_tab_refresh_vopr)).setVisibility((vopr.length>0)?View.GONE:View.VISIBLE);

        ArrayList<Map<String,Object>> data = new ArrayList<Map<String,Object>>(vopr.length);

        Map<String,Object> m ;

        for(int i = 0;i< vopr.length;i++){
            m= new HashMap<String,Object>();
            m.put(VOPROS,(vopr.length>i)? vopr[i]:"...");
            m.put(OTVET,(otv.length>i)? otv[i]:"....");
            data.add(m);
        }

        String[] from = {
                VOPROS,
                OTVET,
        };
        int[] to = {
                R.id.textView_vopr,
                R.id.textView_otv,
        };


        listView = (ListView) v.findViewById(R.id.listView_vopr);
        Adapter_vopros adapter_vopros = new Adapter_vopros(v.getContext(),data,R.layout.delegat_vopr,from,to);
        SwingRightInAnimationAdapter animationAdapter = new SwingRightInAnimationAdapter(adapter_vopros);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);



//Обрабатываем щелчки на элементах ListView:
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                String it = item[position].toString();
                if(it.length()>26) {//http://michurinsk-film.ru   =  26
                    Intent i = new Intent(v.getContext(), Ava_vopr.class);
                    i.putExtra("fl", fl);
                    i.putExtra("item", it);
                    startActivity(i);
                }
            }
        });

        pag_go = ((Button)v.findViewById(R.id.page3_vopr));
        pag_back = ((Button)v.findViewById(R.id.pag1_vopr));
        add_vopr =  ((Button)v.findViewById(R.id.button_add_vopr));
        pag_go.setOnClickListener(this);
        pag_back.setOnClickListener(this);
        add_vopr.setOnClickListener(this);
        count_page = ((TextView)v.findViewById(R.id.textView_nomer_tekusey_stranici_vopr));






        page_vopr = Integer.valueOf((Main.save_read("page_vopr").length()<1)?"1":Main.save_read("page_vopr"));
        count_page_vopr = Integer.valueOf((Main.save_read("count_page_vopr").length() < 1) ? "10" : Main.save_read("count_page_vopr"));

        count_page.setText(String.valueOf(page_vopr) + "(" + String.valueOf(count_page_vopr) + ")");



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
                d_i_p_t.execute("http://michurinsk-film.ru/question/" + String.valueOf(page_vopr));
                run_potok = true;
            }else{ //!run_potok
                Toast.makeText(context, "Уже обновляется", Toast.LENGTH_SHORT).show();
            }
        }else{ //isNetworkConnected()
            swipeLayout.setRefreshing(false);
            Toast.makeText(context, "Для обновления нужно интернет соединение", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==pag_go.getId()) {
            if (page_vopr < count_page_vopr) {
                page_vopr++;
                Main.save_value("page_vopr", String.valueOf(page_vopr)); //сохраняем в память чтоб при следующей загрузке
                count_page.setText(String.valueOf(page_vopr) + "(" + String.valueOf(count_page_vopr) + ")");
                swipeLayout.setRefreshing(true);
                onRefresh();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Последняя страница", Toast.LENGTH_SHORT).show();
            }
        }
        if(view.getId()==pag_back.getId()) {
            if (page_vopr > 1) {
                page_vopr--;
                Main.save_value("page_vopr", String.valueOf(page_vopr)); //сохраняем в память чтоб при следующей загрузке
                count_page.setText(String.valueOf(page_vopr) + "(" + String.valueOf(count_page_vopr) + ")");
                swipeLayout.setRefreshing(true);
                onRefresh();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Первая страница", Toast.LENGTH_SHORT).show();
            }
        }

        if(view.getId()==add_vopr.getId()) {
            if(Main.save_read_bool("authenticate")){
                Intent i = new Intent(context,Add_vopr.class);
                i.putExtra("fl",Main.save_read_bool("fl"));
                startActivity(i);
            }else{
                Toast.makeText(getActivity().getApplicationContext(), "Нужна аутефикация", Toast.LENGTH_SHORT).show();
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
                        return Vopros(params[0].toString());
                    case 0: // полный пинздец
                        return "Ресурс не отвечает";
                    default: return "Ресурс не отвечает";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Ресурс не отвечает";
            }

        }

        private String Vopros(String s){
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
                        .data("redirect", "/auth")
                        .data("login", login)
                        .data("password", parol)
                        .data("rememberme", "0")
                        .method(Connection.Method.POST)
                        .followRedirects(true);
                Connection.Response response2 = connection2.execute();

                //"id_user"
                Document doc = null;
                try {
                    doc = Jsoup
                            .connect(s)
                            .timeout(0)
                            .maxBodySize(0)
                            .get();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                pars_i_save_reviews(doc);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            posle_potoka(s); //обработка после потока
        }
    }




        public void pars_i_save_reviews(Element value){
            ArrayList vopr = new ArrayList(); // название коментария
            ArrayList item = new ArrayList(); // страница с подробностями
            ArrayList otvet = new ArrayList(); // дата отзова


            count_page_vopr = Integer.valueOf(value.select(".nopage").last().text());
            Main.save_value("count_page_vopr", String.valueOf(count_page_vopr));



            Elements question_list = value.select(".question-list");

            for (int i =0;i != question_list.size();i++){
                vopr.add(question_list.get(i).select(".question").text());
                otvet.add(question_list.get(i).select(".answer").text());
                item.add("http://michurinsk-film.ru"+question_list.get(i).select("a").attr("href").toString());///question/item/4312  +  http://michurinsk-film.ru
            }
            Main.setStringArrayPref(context, "element_vopr", vopr);
            Main.setStringArrayPref(context, "element_otv", otvet);
            Main.setStringArrayPref(context,"element_otv_item",item);



    }

    public  void posle_potoka(String s){
        if(!s.equals("Ресурс не отвечает")) {
            run_potok = false;
            Main.open_vopr();
        }else{
            run_potok = false;
            swipeLayout.setRefreshing(false);
            Toast("Ресурс не отвечает",1);
        }
    }




    private void Toast(String s,int t){

        if(t==1){
            SuperToast.create(Main.context, s, SuperToast.Duration.SHORT,
                    Style.getStyle(Style.GREEN, SuperToast.Animations.FLYIN)).show();
        }else {
            //tost---------------------------------------------------------------
            SuperToast superToast = new SuperToast(Main.context);
            superToast.setDuration(SuperToast.Duration.SHORT);
            switch (s) {
                case "Ресурс не отвечает":
                    superToast.setIcon(SuperToast.Icon.Dark.INFO, SuperToast.IconPosition.LEFT);
                    break;
                default:
                    superToast.setIcon(SuperToast.Icon.Dark.SAVE, SuperToast.IconPosition.LEFT);
            }
            superToast.setText(s);

            superToast.show();
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
