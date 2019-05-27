package dmitriy.deomin.kinooctober.kino_sourse;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static dmitriy.deomin.kinooctober.R.id;

public class Adapter_Raspisan extends SimpleAdapter {

    private ArrayList<Map<String, Object>> results;
    private Context context;
    Spannable text; // подкрашивание текста
    //для обводки картинок рамкой
    Transformation transformation;

    public Adapter_Raspisan(Context context, ArrayList<Map<String, Object>> data, int resource, String[] from, int[] to)
    {
        super(context, data, resource, from, to);
        this.results = data;
        this.context = context;

        transformation = new RoundedTransformationBuilder()
                .borderColor(Main.COLOR_TEXT)
                .borderWidthDp(Main.TOLSHINA_RAMKI)
                .cornerRadiusDp(Main.RADIUS_CKRUGLENIA)
                .oval(false)
                .build();
    }

    static class ViewHolder {
        LinearLayout fon;
        TextView title;
        TextView data;
        TextView opisanie;
        ImageView delegat_ava;
        TextView raspisanie_vse;


        TextView text_coment;
        Button add_coment;
        Button b_likes;
        Button b_dislike;
        LinearLayout liner_like_i_dislike;
        LinearLayout liner_komentariev;
        LinearLayout liner_raspisania_segoda_color;

        TextView time1;
        TextView time2;
        TextView time3;
        TextView time4;
        TextView time5;
        TextView time6;
        TextView time7;
        TextView time8;

        ExpandableRelativeLayout expandableLayout;

    }

    public View getView(final int position, View view, ViewGroup parent) {

        View v = view;
        final ViewHolder viewHolder;


        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.delegat_sessions, parent, false);
            viewHolder = new ViewHolder();

            //получаем все наши виджеты
            viewHolder.fon = (LinearLayout) v.findViewById(id.fon_glavniy);
            viewHolder.title = (TextView) v.findViewById(id.delegat_title);
            viewHolder.data = (TextView) v.findViewById(id.delegat_pokaz_nachalo);
            viewHolder.opisanie =(TextView) v.findViewById(id.delegat_opisanie);
            viewHolder.delegat_ava = (ImageView) v.findViewById(id.delegat_ava);
            viewHolder.raspisanie_vse = (TextView)v.findViewById(R.id.delegat_raspisanie_vse);
            viewHolder.liner_raspisania_segoda_color = (LinearLayout)v.findViewById(R.id.liner_raspisania_segoda_color);

            viewHolder.text_coment = (TextView)v.findViewById(R.id.text_coment);
            viewHolder.add_coment = (Button)v.findViewById(R.id.add_coment);
            viewHolder.b_likes = (Button)v.findViewById(R.id.b_likes);
            viewHolder.b_dislike = (Button)v.findViewById(R.id.b_dislike);
            viewHolder.liner_like_i_dislike = (LinearLayout)v.findViewById(R.id.liner_like_i_dislike);
            viewHolder.liner_komentariev = (LinearLayout)v.findViewById(R.id.liner_komentariev);

            viewHolder.time1 =(TextView)v.findViewById(id.textView_time1);
            viewHolder.time2 =(TextView)v.findViewById(id.textView_time2);
            viewHolder.time3 =(TextView)v.findViewById(id.textView_time3);
            viewHolder.time4 =(TextView)v.findViewById(id.textView_time4);
            viewHolder.time5 =(TextView)v.findViewById(id.textView_time5);
            viewHolder.time6 =(TextView)v.findViewById(id.textView_time6);
            viewHolder.time7 =(TextView)v.findViewById(id.textView_time7);
            viewHolder.time8 =(TextView)v.findViewById(id.textView_time8);

            viewHolder.expandableLayout =  (ExpandableRelativeLayout) v.findViewById(R.id.expandableLayout);

            v.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) v.getTag();
        }




        viewHolder.expandableLayout.setExpanded(false);

        //Заполним лайки и коменты

        if(isOnline(context)){
            viewHolder.liner_like_i_dislike.setVisibility(VISIBLE);
            viewHolder.liner_komentariev.setVisibility(VISIBLE);

            final ArrayList arrayList = new ArrayList();
            arrayList.add(0,results.get(position).get("title").toString());
            arrayList.add(1,viewHolder.b_likes);
            arrayList.add(2,viewHolder.b_dislike);
            arrayList.add(3,viewHolder.text_coment);

            Get_baza_info get_baza_info = new Get_baza_info();
            get_baza_info.execute(arrayList);
        }else{
            viewHolder.liner_like_i_dislike.setVisibility(GONE);
            viewHolder.liner_komentariev.setVisibility(GONE);
        }


viewHolder.liner_raspisania_segoda_color.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        viewHolder.expandableLayout.toggle();
    }
});






//заполнение
        viewHolder.fon.setBackgroundColor((position % 2 == 0) ? Main.COLOR_ITEM : Main.COLOR_ITEM2);
        viewHolder.title.setText(format_title(results.get(position).get("title").toString()));
        viewHolder.data.setText(format_nachalo(results.get(position).get("nachalo").toString()));
        viewHolder.opisanie.setText(format_diskription(results.get(position).get("description").toString()));

        Picasso.get().load(results.get(position).get("image").toString()).fit().transform(transformation).into(viewHolder.delegat_ava);

        viewHolder.delegat_ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Ava.class);
                i.putExtra("ava", results.get(position).get("image").toString());
                i.putExtra("item", results.get(position).get("item_podrobno").toString());
                context.startActivity(i);
            }
        });

        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ComponentName cm = new ComponentName(
//                        "com.google.android.youtube",
//                        "com.google.android.apps.youtube.app.WatchWhileActivity");
//
//                Intent intent = new Intent();
//                intent.setComponent(cm);
//                context.startActivity(intent);

                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY,results.get(position).get("title").toString() );
                context.startActivity(intent);
            }
        });


        viewHolder.text_coment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo));
                final View content = LayoutInflater.from(context).inflate(R.layout.custom_dialog_text_koment_clik, null);
                builder.setView(content);
                ((LinearLayout)content.findViewById(id.fon_liner_komentariev)).setBackgroundColor(Main.COLOR_FON);
                ((TextView) content.findViewById(R.id.textView_text_cokent_dialog)).setText(viewHolder.text_coment.getText().toString());
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });


        viewHolder.add_coment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(context, R.anim.myalpha);
                viewHolder.add_coment.startAnimation(anim);

                final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo));
                final View content = LayoutInflater.from(context).inflate(R.layout.custum_dialog_add_coment_item, null);
                builder.setView(content);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                ((LinearLayout)content.findViewById(id.dialog_fon)).setBackgroundColor(Main.COLOR_FON);

                ((Button) content.findViewById(R.id.button_add_com_dialog)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Animation anim = AnimationUtils.loadAnimation(context, R.anim.myalpha);
                        ((Button) content.findViewById(R.id.button_add_com_dialog)).startAnimation(anim);

                       // text = params[0].get(0).toString();
                       // com = (TextView) params[0].get(1);
                       // old_text = params[0].get(2).toString();
                      //  title = params[0].get(3).toString();
                        ArrayList arrayList = new ArrayList();

                        String maseg =  Main.save_read("ima_user")+":"+((EditText) content.findViewById(R.id.editText_add_cometn_dialog)).getText().toString()+"&";

                        arrayList.add(0, maseg);
                        arrayList.add(1,viewHolder.text_coment);
                        arrayList.add(2,viewHolder.text_coment.getText().toString());
                        arrayList.add(3,results.get(position).get("title").toString());

                        Add_coment add_coment = new Add_coment();
                        add_coment.execute(arrayList);

                        alertDialog.dismiss();
                    }
                });
            }
        });




        //like*******************************************************************************************************
        viewHolder.b_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Animation anim = AnimationUtils.loadAnimation(context, R.anim.myalpha);
                viewHolder.b_likes.startAnimation(anim);

                ArrayList arrayList = new ArrayList();
                arrayList.add(0, results.get(position).get("title").toString());
                arrayList.add(1, Main.save_read("ima_user"));
                arrayList.add(2, "");
                arrayList.add(3, "1");
                arrayList.add(4,viewHolder.b_dislike);
                arrayList.add(5,viewHolder.b_likes);
                arrayList.add(6,((viewHolder.text_coment.getText().length()>3)?"чето есть":""));

                Add_like add_like = new Add_like();
                add_like.execute(arrayList);
            }
        });
//**********************************************************************************************************
        viewHolder.b_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation anim = AnimationUtils.loadAnimation(context, R.anim.myalpha);
                viewHolder.b_dislike.startAnimation(anim);

                ArrayList arrayList = new ArrayList();
                arrayList.add(0, results.get(position).get("title").toString());
                arrayList.add(1, Main.save_read("ima_user"));
                arrayList.add(2, "1");
                arrayList.add(3, "");
                arrayList.add(4,viewHolder.b_dislike);
                arrayList.add(5,viewHolder.b_likes);
                arrayList.add(6,((viewHolder.text_coment.getText().length()>3)?"чето есть":""));

                Add_like add_like = new Add_like();
                add_like.execute(arrayList);
            }
        });


        viewHolder.raspisanie_vse.setText(results.get(position).get("vsetime").toString());
        viewHolder.raspisanie_vse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo));
                final View content = LayoutInflater.from(context).inflate(R.layout.vopros_copy_ili_send_raspisanie, null);
                builder.setView(content);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                ((LinearLayout)content.findViewById(id.fon_send_raspisanie_ili_copy_bufer)).setBackgroundColor(Main.COLOR_FON);
                ((TextView)content.findViewById(R.id.previu_raspisanie)).setText(results.get(position).get("title").toString()+"\n"+results.get(position).get("vsetime").toString());
                ((Button)content.findViewById(R.id.button_copy_raspisanie_v_bufer)).setTypeface(Main.face);
                ((Button)content.findViewById(R.id.button_send_raspisanie_sms)).setTypeface(Main.face);


                ((Button)content.findViewById(R.id.button_copy_raspisanie_v_bufer)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Animation anim = AnimationUtils.loadAnimation(context, R.anim.myalpha);
                        v.startAnimation(anim);

                        Main.putText(results.get(position).get("title").toString()+": "+results.get(position).get("vsetime").toString());

                        alertDialog.dismiss();

                    }
                });

                ((Button) content.findViewById(R.id.button_send_raspisanie_sms)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Animation anim = AnimationUtils.loadAnimation(context, R.anim.myalpha);
                        v.startAnimation(anim);

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,results.get(position).get("title").toString()+": "+results.get(position).get("vsetime").toString());
                        sendIntent.setType("text/plain");
                        context.startActivity(sendIntent);

                        alertDialog.dismiss();
                    }
                });
            }
        });

//расписание на сегодня
        // время  качество  и цвет
        viewHolder.time1.setText(format_time(results.get(position).get("time1").toString(), results.get(position).get("time1format").toString()));
        viewHolder.time1.setBackgroundColor(Integer.valueOf((!results.get(position).get("time1color").toString().isEmpty()) ? results.get(position).get("time1color").toString() : (String.valueOf(Color.GRAY))));
        viewHolder.time1.setVisibility((results.get(position).get("time1").toString().length() > 1) ? VISIBLE :GONE);


       // время  качество  и цвет
        viewHolder.time2.setText(format_time(results.get(position).get("time2").toString(), results.get(position).get("time2format").toString()));
        viewHolder.time2.setBackgroundColor(Integer.valueOf((!results.get(position).get("time2color").toString().isEmpty()) ? results.get(position).get("time2color").toString() : (String.valueOf(Color.GRAY))));
        viewHolder.time2.setVisibility((results.get(position).get("time2").toString().length()>1)?VISIBLE:GONE);

        // время  качество  и цвет
        viewHolder.time3.setText(format_time(results.get(position).get("time3").toString(), results.get(position).get("time3format").toString()));
        viewHolder.time3.setBackgroundColor(Integer.valueOf((!results.get(position).get("time3color").toString().isEmpty()) ? results.get(position).get("time3color").toString() : (String.valueOf(Color.GRAY))));
        viewHolder.time3.setVisibility((results.get(position).get("time3").toString().length()>1)?VISIBLE:GONE);

        // время  качество  и цвет
        viewHolder.time4.setText(format_time(results.get(position).get("time4").toString(), results.get(position).get("time4format").toString()));
        viewHolder.time4.setBackgroundColor(Integer.valueOf((!results.get(position).get("time4color").toString().isEmpty()) ? results.get(position).get("time4color").toString() : (String.valueOf(Color.GRAY))));
        viewHolder.time4.setVisibility((results.get(position).get("time4").toString().length()>1)?VISIBLE:GONE);

        // время  качество  и цвет
        viewHolder.time5.setText(format_time(results.get(position).get("time5").toString(), results.get(position).get("time5format").toString()));
        viewHolder.time5.setBackgroundColor(Integer.valueOf((!results.get(position).get("time5color").toString().isEmpty()) ? results.get(position).get("time5color").toString() : (String.valueOf(Color.GRAY))));
        viewHolder.time5.setVisibility((results.get(position).get("time5").toString().length()>1)?VISIBLE:GONE);

        // время  качество  и цвет
        viewHolder.time6.setText(format_time(results.get(position).get("time6").toString(), results.get(position).get("time6format").toString()));
        viewHolder.time6.setBackgroundColor(Integer.valueOf((!results.get(position).get("time6color").toString().isEmpty()) ? results.get(position).get("time6color").toString() : (String.valueOf(Color.GRAY))));
        viewHolder.time6.setVisibility((results.get(position).get("time6").toString().length()>1)?VISIBLE:GONE);

       // время  качество  и цвет
        viewHolder.time7.setText(format_time(results.get(position).get("time7").toString(), results.get(position).get("time6format").toString()));
        viewHolder.time7.setBackgroundColor(Integer.valueOf((!results.get(position).get("time7color").toString().isEmpty()) ? results.get(position).get("time7color").toString() : (String.valueOf(Color.GRAY))));
        viewHolder.time7.setVisibility((results.get(position).get("time7").toString().length()>1)?VISIBLE:GONE);

        // время  качество  и цвет
        viewHolder.time8.setText(format_time(results.get(position).get("time8").toString(), results.get(position).get("time8format").toString()));
        viewHolder.time8.setBackgroundColor(Integer.valueOf((!results.get(position).get("time8color").toString().isEmpty()) ? results.get(position).get("time8color").toString() : (String.valueOf(Color.GRAY))));
        viewHolder.time8.setVisibility((results.get(position).get("time8").toString().length()>1)?VISIBLE:GONE);

        //дата обновления + Расписание сегодня
        ((TextView)v.findViewById(id.delegat_raspisanie_segodna)).setText(date_update(results.get(position).get("update_date").toString()));

        return v;
    }

    Spannable date_update(String date){
        text = new SpannableString(date);
        //Расписание сегодня 12 мая или Расписание от 12 мая
        String d = (String) DateFormat.format("dd", new Date()); //yyyy-MM-dd kk:mm:ss   день
        String m = return_mesac((String) DateFormat.format("M", new Date()));

        //проверяется дата (че седня есть)
        if((d+m).contains(date)) {
            //******************************************************
            text = new SpannableString("Расписание сегодня " + date);
            text.setSpan(new StyleSpan(Typeface.BOLD), 19, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new ForegroundColorSpan(Color.BLUE), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new RelativeSizeSpan(0.7f), 19, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //****************************************************
            return text;
        }else
        {
            //******************************************************
            text = new SpannableString("Расписание от " + date);
            text.setSpan(new StyleSpan(Typeface.BOLD), 14, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new ForegroundColorSpan(Color.BLUE), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new RelativeSizeSpan(0.7f), 14, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //****************************************************
            return text;
        }

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

    Spannable format_time(String t,String f){
        text = new SpannableString(t+f);
        text.setSpan(new RelativeSizeSpan(0.7f), t.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return  text;
    }
    Spannable format_nachalo(String value){
        //******************************************
        //форматирование текста
        //UnderlineSpan() - подчеркнутый текст
        //StyleSpan(Typeface.BOLD) - полужирный тектс
        //StyleSpan(Typeface.ITALIC) - курсив
        //ForegroundColorSpan(Color.GREEN) - цвет
        text = new SpannableString(value);
        //text.setSpan(new UnderlineSpan(), 0, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(Color.BLUE), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //****************************************************
        return text;
    }
    Spannable format_title(String value){
        text = new SpannableString(value);
        text.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //****************************************************
        return text;
    }
    Spannable format_diskription(String value){
        text = new SpannableString(value);
      //  text.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //****************************************************
        return text;
    }
    Spannable format_raspisanie_segodana(String value){
        text = new SpannableString(value);
        text.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(Color.RED), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //****************************************************
        return text;
    }


public class Add_coment extends AsyncTask<ArrayList,Void,Boolean> {
    String old_text;
    String text;
    String title;
    TextView com;
    String and_text;


    @Override
    protected  Boolean doInBackground(ArrayList... params) {

        text = params[0].get(0).toString();
        com = (TextView) params[0].get(1);
        old_text = params[0].get(2).toString();
        title = params[0].get(3).toString();

        //в отличии есть ли в базе запись будем добавлять или обновлять
        String zapros;

        //если * значить в базе вообще такой записи нет
        if(old_text.equals("*")){
            and_text = text;
            zapros ="INSERT INTO mich_kino(title,like_item,dislike_item,koment) VALUES('"+title+"','0','0','"+and_text+"');";
        }else{ //иначе обновим
            and_text = old_text+"&"+text;
            zapros ="UPDATE mich_kino SET koment = '"+and_text+"' WHERE title='"+title+"'";
        }

        Connection.Response response=null;
        try {
            response = Jsoup.connect("http://i9027296.bget.ru/mich_kino/db_add_coment.php")
                    .data("Zapros",zapros)
                    .method(Connection.Method.POST)
                    .timeout(3000)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response!=null) {
            String otvet =null;

            try {
                otvet = response.parse().select("otvet").text();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(otvet.length()>2){
                return true;
            }else {
                return false;
            }
        }else{
            return false;
        }
    }
    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);
        if(b){
          com.setText(and_text.replace("&", "\n"));
        }else {
          com.setText("*");
        }
    }
}

    public class Get_baza_info extends AsyncTask<ArrayList,Void,Elements> {
        String title;
        Button l;
        Button d;
        TextView c;

        @Override
        protected  Elements doInBackground(ArrayList... params) {

            title = params[0].get(0).toString();
            l = (Button)params[0].get(1);
            d = (Button)params[0].get(2);
            c = (TextView) params[0].get(3);


            Connection.Response response=null;
            try {
                response = Jsoup.connect("http://i9027296.bget.ru/mich_kino/read_baza.php")
                        .data("Zapros","SELECT * FROM `mich_kino` WHERE title ='"+title+"'")
                        .method(Connection.Method.POST)
                        .timeout(3000)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(response!=null) {
                String otvet =null;

                try {
                    otvet = response.parse().select("otvet").text();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(otvet.length()>2){
                    Elements el = null;
                    try {
                        el = response.parse().select("otvet_data");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return el;
                }else {
                    return null;
                }
            }else{
                return null;
            }
        }
        @Override
        protected void onPostExecute(Elements elements) {
            super.onPostExecute(elements);
            if(elements!=null){
                l.setText(elements.select("like_item").text().toString());
                d.setText(elements.select("dislike_item").text().toString());
                c.setText(elements.select("koment").text().toString().replace("&", "\n"));
            }else {
                l.setText("0");
                d.setText("0");
                c.setText("*");
            }
        }
    }



    public class Add_like extends AsyncTask<ArrayList, Integer, String> {
        Button bd;
        Button bl;

        @Override
        protected String doInBackground(ArrayList... params) {
            bd = (Button) params[0].get(4);
            bl = (Button) params[0].get(5);

            String loginURL = "http://i9027296.bget.ru/mich_kino/db_add_like_dislake.php";
            //0 - item_id
            //1 - like_user_id
            //2 - dislike_item
            //3 - like_item
            Connection.Response response = null;
            try {
                //делаем пост запрос
                response = Jsoup.connect(loginURL)
                        .data("item_id", params[0].get(0).toString())
                        .data("l_u_id", params[0].get(1).toString())
                        .data("dislike_item", params[0].get(2).toString())
                        .data("like_item", params[0].get(3).toString())
                        .data("add_ili_update",params[0].get(6).toString())
                        .method(Connection.Method.POST)
                        .timeout(5000)
                        .followRedirects(true)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response != null) {
                try {
                    return response.parse().select("otvet_add_db").text().toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                if (s.equals("adddis")) {
                    bd.setText(String.valueOf(Integer.valueOf(bd.getText().toString()) + 1));
                }
                if (s.equals("addlike")) {
                    bl.setText(String.valueOf(Integer.valueOf(bl.getText().toString()) + 1));
                }
                if (s.length() > 7) {
                    String[] mas = s.split(":");
                    bd.setText(mas[1].toString());
                    bl.setText(mas[2].toString());

                }
            }else {
                Main.Toast_error("Ошибка");
            }
        }
    }





    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }



}
