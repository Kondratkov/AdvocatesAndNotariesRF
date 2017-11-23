package kondratkov.advocatesandnotariesrf;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import kondratkov.advocatesandnotariesrf.advocates.Advocates_list;
import kondratkov.advocatesandnotariesrf.consultation.consultation_online;
import kondratkov.advocatesandnotariesrf.doc_create.create_document;
import kondratkov.advocatesandnotariesrf.input.LogIN;
import kondratkov.advocatesandnotariesrf.maps.Map_jur_and_notary;
import kondratkov.advocatesandnotariesrf.my_info.My_orders;
import kondratkov.advocatesandnotariesrf.my_info.My_profile;
import kondratkov.advocatesandnotariesrf.my_info.My_questions;
import kondratkov.advocatesandnotariesrf.notary.Notary_list;
import kondratkov.advocatesandnotariesrf.paid_services.Paid_services;
import kondratkov.advocatesandnotariesrf.start_help.About_us;

public class Settings_no_login extends Activity implements View.OnTouchListener {

    public IN in;
    LinearLayout lila, lila_start;
    ScrollView scroll;
    public Button start_no_but_menu_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_no_login);
        in = new IN();

        Log.d("qwerty", "Start Sidebar");
        lila = (LinearLayout)findViewById(R.id.start_no_lila_menu_ons);
        lila_start = (LinearLayout)findViewById(R.id.start_no_lila_menu);

        start_no_but_menu_close = (Button)findViewById(R.id.start_no_but_menu_close);

        scroll = (ScrollView)findViewById(R.id.start_no_scroll);
        scroll.setOnTouchListener(this);
        start_no_but_menu_close.setOnTouchListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //lila.setBackgroundResource(R.color.menu_on);
        Animation anim = AnimationUtils.loadAnimation(Settings_no_login.this, R.anim.anim_menu_yes);
        lila_start.startAnimation(anim);

    }

    @Override
    protected  void onStop(){
        super.onStop();

    }

    public void on_close(){
        Animation anim = AnimationUtils.loadAnimation(Settings_no_login.this, R.anim.anim_menu_no);
        lila_start.startAnimation(anim);
        Settings_no_login.this.finish();
    }


    public void onClick (View view){
        switch (view.getId()){
            case R.id.start_no_but_menu_close:
                on_close();
                break;
            case R.id.start_no_but_menu_login:
                Intent intent = new Intent(Settings_no_login.this, LogIN.class);
                startActivity(intent);
                Settings_no_login.this.finish();
                break;
            case R.id.start_no_but_menu_about:
                Intent intent1 = new Intent(Settings_no_login.this, About_us.class);
                startActivity(intent1);
                Settings_no_login.this.finish();
                break;
        }
    }

    public float xX = 0;
    public float xNew = 0;
    Boolean tr = true;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_UP):
                if(xNew < -300){
                    Settings_no_login.this.finish();
                }
                tr = true;
                break;
            case (MotionEvent.ACTION_DOWN):
                xX = event.getX();
                xNew = 0;
                Log.d("qwerty", "ACTION_DOWN xX "+xX);
                break;
            case (MotionEvent.ACTION_MOVE):
                Log.d("qwerty", "ACTION_MOVE ");
                if(tr){
                    xX = event.getX();
                    tr= false;
                }else{
                    int historySize = event.getHistorySize();
                    for (int i = 0; i <historySize; i++) {
                        float x = event.getHistoricalX(i);
                        processMovement(x);
                    }
                    float x = event.getX();
                    processMovement(x);
                }

        }
        return false;
    }
    private void processMovement(float _x) {
        // Todo: Обработка движения.
        Log.d("qwerty", "xX " + xX);
        Log.d("qwerty", "_x " + _x);
        xNew = xX -_x;
        Log.d("qwerty", "xNew " + xNew);
        Log.d("qwerty", " ");
        Log.d("qwerty", " ");
    }
}

