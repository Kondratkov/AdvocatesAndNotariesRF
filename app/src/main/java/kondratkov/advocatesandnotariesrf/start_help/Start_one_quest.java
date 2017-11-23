package kondratkov.advocatesandnotariesrf.start_help;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import kondratkov.advocatesandnotariesrf.Form_advocates;
import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.Settings_no_login;
import kondratkov.advocatesandnotariesrf.data_theme.theme_data;

public class Start_one_quest extends Activity {

    public SharedPreferences sPref;
    public theme_data t_db;
    IN in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_one_quest);
        in = new IN();
        t_db = new theme_data(in.get_context());
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void onClick(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.button_yes_one_dogovor:
                Log.d("qwerty", "SSTTAARRTT");
                SharedPreferences.Editor ed = sPref.edit();
                ed.putBoolean("one_one_start", false);
                ed.commit();
                intent = new Intent(Start_one_quest.this, Start_activity_no_login.class);
                startActivity(intent);
                Start_one_quest.this.finish();
                break;
        }
    }

}
