package dmitriy.deomin.kinooctober;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.danielnilsson9.colorpickerview.dialog.ColorPickerDialogFragment;


public class Setting extends Activity implements ColorPickerDialogFragment.ColorPickerDialogListener {

    private int DIALOG_ID;
    Button edit_fon;
    Button edit_pos1;
    Button edit_pos2;
    Button edit_text_color;

    TextView textView_edit_color_posty;
    TextView textView_edit_color_posty2;
    TextView textView_edit_fon_color;
    TextView textView_edit_color_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        //во весь экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //ставим шрифт и цвет текста
        textView_edit_color_posty = (TextView)findViewById(R.id.textView_edit_color_posty);
        textView_edit_color_posty.setTypeface(Main.face);
        textView_edit_color_posty.setTextColor(Main.COLOR_TEXT);

        textView_edit_color_posty2 = (TextView)findViewById(R.id.textView_edit_color_posty2);
        textView_edit_color_posty2.setTypeface(Main.face);
        textView_edit_color_posty2.setTextColor(Main.COLOR_TEXT);

        textView_edit_fon_color = (TextView)findViewById(R.id.textView_edit_fon_color);
        textView_edit_fon_color.setTypeface(Main.face);
        textView_edit_fon_color.setTextColor(Main.COLOR_TEXT);

        textView_edit_color_text = (TextView)findViewById(R.id.textView_edit_color_text);
        textView_edit_color_text.setTypeface(Main.face);
        textView_edit_color_text.setTextColor(Main.COLOR_TEXT);


        ((Button)findViewById(R.id.button_edit_fonts)).setTextColor(Main.COLOR_TEXT);

        //ставим цвет
        ((LinearLayout)findViewById(R.id.fon_setting)).setBackgroundColor(Main.COLOR_FON);

        edit_fon = ((Button)findViewById(R.id.button_edit_fon_color));
        edit_fon.setTypeface(Main.face);
        edit_fon.setTextColor(Main.COLOR_TEXT);
        edit_fon.setBackgroundColor(Main.COLOR_FON);
        edit_fon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DIALOG_ID = 0;
                ColorPickerDialogFragment f = ColorPickerDialogFragment
                        .newInstance(DIALOG_ID, null, null, getResources().getColor(R.color.fon), true);

                f.setStyle(DialogFragment.STYLE_NORMAL, R.style.LightPickerDialogTheme);
                f.show(getFragmentManager(), "d");
            }
        });

        edit_pos1 = ((Button)findViewById(R.id.button_edit_color_posty));
        edit_pos1.setTypeface(Main.face);
        edit_pos1.setTextColor(Main.COLOR_TEXT);
        edit_pos1.setTextColor(Main.COLOR_TEXT);
        edit_pos1.setBackgroundColor(Main.COLOR_ITEM);
        edit_pos1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DIALOG_ID = 1;
                ColorPickerDialogFragment f = ColorPickerDialogFragment
                        .newInstance(DIALOG_ID, null, null, getResources().getColor(R.color.post1), true);

                f.setStyle(DialogFragment.STYLE_NORMAL, R.style.LightPickerDialogTheme);
                f.show(getFragmentManager(), "d");
            }
        });

        edit_pos2 = ((Button)findViewById(R.id.button_edit_color_posty2));
        edit_pos2.setTypeface(Main.face);
        edit_pos2.setTextColor(Main.COLOR_TEXT);
        edit_pos2.setBackgroundColor(Main.COLOR_ITEM2);
        edit_pos2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DIALOG_ID = 2;
                ColorPickerDialogFragment f = ColorPickerDialogFragment
                        .newInstance(DIALOG_ID, null, null, getResources().getColor(R.color.post2), true);

                f.setStyle(DialogFragment.STYLE_NORMAL, R.style.LightPickerDialogTheme);
                f.show(getFragmentManager(), "d");
            }
        });


        edit_text_color = ((Button)findViewById(R.id.button_edit_color_text));
        edit_text_color.setTypeface(Main.face);
        edit_text_color.setTextColor(Main.COLOR_TEXT);
        edit_text_color.setBackgroundColor(Main.COLOR_FON);
        edit_text_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DIALOG_ID = 3;
                ColorPickerDialogFragment f = ColorPickerDialogFragment
                        .newInstance(DIALOG_ID, null, null, getResources().getColor(R.color.text), true);

                f.setStyle(DialogFragment.STYLE_NORMAL, R.style.LightPickerDialogTheme);
                f.show(getFragmentManager(), "d");
            }
        });

    }


    @Override
    public void onColorSelected(int dialogId, int color) {
        switch(dialogId) {
            case 0:
                Main.save_value_int("color_fon",color);
                Main.COLOR_FON = color;
                Main.liner_boss.setBackgroundColor(color);
                ((LinearLayout)findViewById(R.id.fon_setting)).setBackgroundColor(color);
                edit_fon.setBackgroundColor(Main.COLOR_FON);
                break;

            case 1:
                Main.save_value_int("color_post1",color);
                Main.COLOR_ITEM = color;
                edit_pos1.setBackgroundColor(Main.COLOR_ITEM);
                break;

            case 2:
                Main.save_value_int("color_post2",color);
                Main.COLOR_ITEM2 = color;
                edit_pos2.setBackgroundColor(Main.COLOR_ITEM2);
                break;

            case 3:
                Main.save_value_int("color_text",color);
                Main.COLOR_TEXT = color;

                textView_edit_color_posty.setTextColor(Main.COLOR_TEXT);
                textView_edit_color_posty2.setTextColor(Main.COLOR_TEXT);
                textView_edit_fon_color.setTextColor(Main.COLOR_TEXT);
                textView_edit_color_text.setTextColor(Main.COLOR_TEXT);

                edit_fon.setTextColor(Main.COLOR_TEXT);
                edit_pos1.setTextColor(Main.COLOR_TEXT);
                edit_pos2.setTextColor(Main.COLOR_TEXT);
                edit_text_color.setTextColor(Main.COLOR_TEXT);
                break;
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

    public void edit_fonts(View v) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        v.startAnimation(anim);
        Intent i =  new Intent(this,Fonts_vibor.class);
        startActivity(i);
    }
}
