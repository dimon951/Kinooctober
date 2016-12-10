package dmitriy.deomin.kinooctober.Info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.R;

public class Adapter_vopros extends SimpleAdapter {

    private ArrayList<Map<String, Object>> results;
    private Context context;

    public Adapter_vopros(Context context, ArrayList<Map<String, Object>> data, int resource, String[] from, int[] to)
    {
        super(context, data, resource, from, to);
        this.results = data;
        this.context = context;
    }

    public View getView(int position, View view, ViewGroup parent) {

        View v = view;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.delegat_vopr, null);
        }
        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.fon_vopros);
        linearLayout.setBackgroundColor((position % 2 == 0) ? Main.COLOR_ITEM :Main.COLOR_ITEM2);

        ((TextView) v.findViewById(R.id.textView_vopr)).setText(results.get(position).get("vopros").toString());
        ((TextView) v.findViewById(R.id.textView_vopr)).setTypeface(Main.face);
        ((TextView) v.findViewById(R.id.textView_vopr)).setTextColor(Main.COLOR_TEXT);

        ((TextView) v.findViewById(R.id.textView_otv)).setText(results.get(position).get("otvet").toString());
        ((TextView) v.findViewById(R.id.textView_otv)).setTypeface(Main.face);
        ((TextView) v.findViewById(R.id.textView_otv)).setTextColor(Main.COLOR_TEXT);


 return v;
    }

}
