package kondratkov.advocatesandnotariesrf;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Settings extends Activity {

    public CheckBox ch1, ch2, ch3, ch4, p1, p2, p3;
    public SharedPreferences sPref;

    public IN in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        sPref = PreferenceManager.getDefaultSharedPreferences(this);//getPreferences(MODE_PRIVATE);

        in = new IN();

        ch1 = (CheckBox)findViewById(R.id.settings_ch_1);
        ch2 = (CheckBox)findViewById(R.id.settings_ch_2);
        ch3 = (CheckBox)findViewById(R.id.settings_ch_3);
        ch4 = (CheckBox)findViewById(R.id.settings_ch_4);

        p1 = (CheckBox)findViewById(R.id.settings_p_1);
        p2 = (CheckBox)findViewById(R.id.settings_p_2);
        p3 = (CheckBox)findViewById(R.id.settings_p_3);

        Log.d("qwerty", "размер шрифта настройки " + sPref.getInt("pref_sh", 0));
        switch (sPref.getInt("pref_sh", 0)){
            case 14:
                ch1.setChecked(true);
                ch2.setChecked(false);
                ch3.setChecked(false);
                ch4.setChecked(false);
                break;
            case 16:
                ch2.setChecked(true);
                ch1.setChecked(false);
                ch3.setChecked(false);
                ch4.setChecked(false);
                break;
            case 18:
                ch3.setChecked(true);
                ch1.setChecked(false);
                ch2.setChecked(false);
                ch4.setChecked(false);
                break;
            case 22:
                ch4.setChecked(true);
                ch1.setChecked(false);
                ch2.setChecked(false);
                ch3.setChecked(false);
                break;
        }

        ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ch2.setChecked(false);
                    ch3.setChecked(false);
                    ch4.setChecked(false);
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putInt("pref_sh", 14);
                    ed.commit();
                    in.set_font_1(14);
                    in.set_font_2(12);
                    in.set_font_3(8);
                }
                else {
                    if(ch2.isChecked()||ch3.isChecked()||ch4.isChecked()){

                    }else {
                        ch1.setChecked(true);
                    }
                    //
                }
            }
        });
        ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ch1.setChecked(false);
                    ch3.setChecked(false);
                    ch4.setChecked(false);
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putInt("pref_sh", 16);
                    ed.commit();
                    in.set_font_1(16);
                    in.set_font_2(14);
                    in.set_font_3(10);
                }
                else {
                    if(ch1.isChecked()||ch3.isChecked()||ch4.isChecked()){

                    }else {
                        ch2.setChecked(true);
                    }
                }
            }
        });
        ch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ch1.setChecked(false);
                    ch2.setChecked(false);
                    ch4.setChecked(false);
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putInt("pref_sh", 18);
                    ed.commit();
                    in.set_font_1(18);
                    in.set_font_2(16);
                    in.set_font_3(12);

                }
                else {
                    if(ch1.isChecked()||ch2.isChecked()||ch4.isChecked()){

                    }else {
                        ch3.setChecked(true);
                    }
                }
            }
        });
        ch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ch1.setChecked(false);
                    ch2.setChecked(false);
                    ch3.setChecked(false);
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putInt("pref_sh", 22);
                    ed.commit();
                    in.set_font_1(22);
                    in.set_font_2(20);
                    in.set_font_3(16);
                }
                else {

                    if(ch1.isChecked()||ch2.isChecked()||ch3.isChecked()){

                    }else {
                        ch4.setChecked(true);
                    }

                }
            }
        });

        if(sPref.getBoolean("pref_setting_push_1", true)){
            p1.setChecked(true);
        }
        else {
            p1.setChecked(false);
        }
        p1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putBoolean("pref_setting_push_1", true);
                    ed.commit();
                }
                else {
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putBoolean("pref_setting_push_1", false);
                    ed.commit();
                }
            }
        });

        if(sPref.getBoolean("pref_setting_push_2", true)){
            p2.setChecked(true);
        }
        else {
            p2.setChecked(false);
        }
        p2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putBoolean("pref_setting_push_2", true);
                    ed.commit();
                }
                else {
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putBoolean("pref_setting_push_2", false);
                    ed.commit();
                }
            }
        });

        if(sPref.getBoolean("pref_setting_push_3", true)){
            p3.setChecked(true);
        }
        else {
            p3.setChecked(false);
        }
        p3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putBoolean("pref_setting_push_3", true);
                    ed.commit();
                }
                else {
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putBoolean("pref_setting_push_3", false);
                    ed.commit();
                }
            }
        });
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.settings_but_cancel:
                Settings.this.finish();
                break;
        }
    }

}
