package dmitriy.deomin.kinooctober.kino_sourse;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import dmitriy.deomin.kinooctober.Main;
import dmitriy.deomin.kinooctober.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Mani extends Fragment {


    public Mani() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.mani, null);

        ((LinearLayout)v.findViewById(R.id.fon_mani)).setBackgroundColor(Main.COLOR_FON);

        ((LinearLayout)v.findViewById(R.id.mani_format)).setBackgroundColor(Main.COLOR_ITEM);
        ((LinearLayout)v.findViewById(R.id.mani_format2)).setBackgroundColor(Main.COLOR_ITEM2);
        ((LinearLayout)v.findViewById(R.id.mani_adres)).setBackgroundColor(Main.COLOR_ITEM);



        ((Button)v.findViewById(R.id.button6)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("mailto:mfilm68@mail.ru");
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(it);
            }
        });









        return v;
    }


}
