package dmitriy.deomin.kinooctober.kino_sourse;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Map;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.R;

import static dmitriy.deomin.kinooctober.R.id;

public class Adapter_News extends SimpleAdapter {

    private ArrayList<Map<String, Object>> results;
    private Context context;
    Spannable text; // подкрашивание текста

    //для обводки картинок рамкой
    Transformation transformation;

    public Adapter_News(Context context, ArrayList<Map<String, Object>> data, int resource, String[] from, int[] to)
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
        LinearLayout ava_news;
        TextView title;
        TextView data;
        TextView text_news;
        ImageView ava_discrition;
        ImageView ava_news_imag;
    }

    public View getView(int position, View view, ViewGroup parent) {

        View v = view;
        final ViewHolder viewHolder;


        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.delegat_news, parent, false);
            viewHolder = new ViewHolder();

            //получаем все наши виджеты
            viewHolder.fon = (LinearLayout) v.findViewById(id.fon_news_adapter);
            viewHolder.ava_news = (LinearLayout)v.findViewById(id.ava_news_adapter);
            viewHolder.title = (TextView) v.findViewById(id.news_textView_title);
            viewHolder.data = (TextView) v.findViewById(id.news_textView_data);
            viewHolder.text_news =(TextView) v.findViewById(id.news_textView_text);
            viewHolder.ava_discrition = (ImageView) v.findViewById(id.ava_discriptions);
            viewHolder.ava_news_imag = (ImageView) v.findViewById(id.news_imageView_ava);

            v.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) v.getTag();
        }


        //ставим шрифт
        viewHolder.title.setTypeface(Main.face);
        viewHolder.data.setTypeface(Main.face);
        viewHolder.text_news.setTypeface(Main.face);

        //ставим цвет
        viewHolder.title.setTextColor(Main.COLOR_TEXT);
        viewHolder.data.setTextColor(Main.COLOR_TEXT);
        viewHolder.text_news.setTextColor(Main.COLOR_TEXT);


        viewHolder.fon.setBackgroundColor((position % 2 == 0) ? Main.COLOR_ITEM : Main.COLOR_ITEM2);
        viewHolder.ava_news.setVisibility((results.get(position).get("ava_news").toString().length() > 25) ? View.VISIBLE : View.GONE);
        viewHolder.title.setText(results.get(position).get("title_news").toString());
        viewHolder.data.setText(format_time(results.get(position).get("data_news").toString()));

//discription
        viewHolder.text_news.setText(format_diskription(results.get(position).get("description").toString()));

        Picasso.get()
                .load(results.get(position).get("ava_dis").toString())
                .resize(Main.width_d, Main.heigh_d/2)
                .centerCrop()
                .into(viewHolder.ava_discrition);


//***********
        Picasso.get()
                .load(results.get(position).get("ava_news").toString())
                .transform(transformation)
                .into(viewHolder.ava_news_imag);

      //  Log.d("TTT", "v delegate vot chto prichodit s avoy " + results.get(position).get("ava_news").toString());
        return v;
    }


    Spannable format_time(String value){
        text = new SpannableString(value);
        //  text.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
