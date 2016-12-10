package dmitriy.deomin.kinooctober.kino_sourse;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import dmitriy.deomin.kinooctober.Main;

import static dmitriy.deomin.kinooctober.R.id;
import static dmitriy.deomin.kinooctober.R.layout;

public class Adapter_otzivi extends SimpleAdapter {

    private ArrayList<Map<String, Object>> results;
    private Context context;
    Spannable text; // подкрашивание текста

    public Adapter_otzivi(Context context, ArrayList<Map<String, Object>> data, int resource, String[] from, int[] to)
    {
        super(context, data, resource, from, to);
        this.results = data;
        this.context = context;
    }

    static class ViewHolder {
        LinearLayout fon;
        TextView zag_com;
        TextView otzivi_avtor;
        TextView otzivi_film;
        TextView otzivi_data;
        TextView otzivi_comentariy;
    }

    public View getView(int position, View view, ViewGroup parent) {

        View v = view;
        final ViewHolder viewHolder;


        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(layout.delegat_otzivi, parent, false);
            viewHolder = new ViewHolder();

            //получаем все наши виджеты
            viewHolder.fon = (LinearLayout) v.findViewById(id.fon_otzivi_delegat);
            viewHolder.zag_com =(TextView) v.findViewById(id.ozivi_textView_zag_com);
            viewHolder.otzivi_avtor = (TextView) v.findViewById(id.ozivi_textView_avtor);
            viewHolder.otzivi_film = (TextView) v.findViewById(id.ozivi_textView_film);
            viewHolder.otzivi_data = (TextView) v.findViewById(id.ozivi_textView_data);
            viewHolder.otzivi_comentariy  =(TextView) v.findViewById(id.ozivi_textView_comentariy);

            v.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) v.getTag();
        }

        //ставим шрифт
        viewHolder.zag_com.setTypeface(Main.face);
        viewHolder.otzivi_avtor.setTypeface(Main.face);
        viewHolder.otzivi_film.setTypeface(Main.face);
        viewHolder.otzivi_data.setTypeface(Main.face);
        viewHolder.otzivi_comentariy.setTypeface(Main.face);

        //ставим цвет
        viewHolder.zag_com.setTextColor(Main.COLOR_TEXT);
        viewHolder.otzivi_avtor.setTextColor(Main.COLOR_TEXT);
        viewHolder.otzivi_film.setTextColor(Main.COLOR_TEXT);
        viewHolder.otzivi_data.setTextColor(Main.COLOR_TEXT);
        viewHolder.otzivi_comentariy.setTextColor(Main.COLOR_TEXT);

        //заполнение

        viewHolder.fon.setBackgroundColor((position % 2 == 0) ? Main.COLOR_ITEM : Main.COLOR_ITEM2);
        viewHolder.zag_com.setText(results.get(position).get("title_com").toString());
        viewHolder.otzivi_avtor.setText(results.get(position).get("avtor").toString());
        viewHolder.otzivi_film.setText(results.get(position).get("film").toString());
        viewHolder.otzivi_data.setText(results.get(position).get("data_com").toString());
        viewHolder.otzivi_comentariy.setText(results.get(position).get("coment").toString());

//        ImageView image = (ImageView) v.findViewById(id.news_imageView_ava);
//        Picasso.with(context).load(results.get(position).get("ava_news").toString()).into(image);

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
            text.setSpan(new ForegroundColorSpan(Color.BLUE), 19, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new RelativeSizeSpan(0.7f), 19, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //****************************************************
            return text;
        }else
        {
            //******************************************************
            text = new SpannableString("Расписание от " + date);
            text.setSpan(new StyleSpan(Typeface.BOLD), 14, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new ForegroundColorSpan(Color.BLUE), 14, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
    Spannable format_raspisanie_segodana(String value){
        text = new SpannableString(value);
        text.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(Color.RED), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //****************************************************
        return text;
    }
}
