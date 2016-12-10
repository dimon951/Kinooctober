package dmitriy.deomin.kinooctober.kino_sourse;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.R;

public class Adapter_Premiera extends SimpleAdapter {

    private ArrayList<Map<String, Object>> results;
    private Context context;
    Spannable text; // подкрашивание текста

    public Adapter_Premiera(Context context, ArrayList<Map<String, Object>> data, int resource, String[] from, int[] to)
    {
        super(context, data, resource, from, to);
        this.results = data;
        this.context = context;
    }

    static class ViewHolder {
        LinearLayout fon;
        TextView title;
        TextView pokaz_nachalo;
        TextView opisanie;
        ImageView ava;
    }

    public View getView(int position, View view, ViewGroup parent) {

        View v = view;
        final ViewHolder viewHolder;


        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.delegat_premiere, parent, false);
            viewHolder = new ViewHolder();

            //получаем все наши виджеты
            viewHolder.fon = (LinearLayout) v.findViewById(R.id.fon_skoro);
            viewHolder.title =(TextView) v.findViewById(R.id.delegat_title);
            viewHolder.pokaz_nachalo =(TextView) v.findViewById(R.id.delegat_pokaz_nachalo);
            viewHolder.opisanie = (TextView) v.findViewById(R.id.delegat_opisanie);
            viewHolder.ava = (ImageView) v.findViewById(R.id.delegat_ava);


            v.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) v.getTag();
        }

        //ставим шрифт
        viewHolder.title.setTypeface(Main.face);
        viewHolder.pokaz_nachalo.setTypeface(Main.face);
        viewHolder.opisanie.setTypeface(Main.face);


        //заполнение

        viewHolder.fon.setBackgroundColor((position % 2 == 0) ? Main.COLOR_ITEM : Main.COLOR_ITEM2);
        viewHolder.title.setText(format_title(results.get(position).get("title").toString()));
        viewHolder.pokaz_nachalo.setText(format_nachalo(results.get(position).get("nachalo").toString()));
        viewHolder.opisanie.setText(format_diskription(results.get(position).get("description").toString()));

        Picasso.with(context).load(results.get(position).get("image").toString()).into(viewHolder.ava);
        return v;
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
}
