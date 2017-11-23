package kondratkov.advocatesandnotariesrf;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class Sidebar extends Activity implements View.OnTouchListener{

    IN in;
    public LinearLayout sidebar_lila;
    public TextView sidebar_tv_nik, sidebar_tv_input;
    public ImageView sidebar_image_user;
    public LinearLayout lila_side_1, lila_side_2, lila_side_3,
            lila_side_4, lila_side_5, lila_side_6, lila_side_7,
            lila_side_8, lila_side_9, lila_side_10, lila_side_11,
            lila_side_12, lila_side_13, lila_side_14, lila_side_15, lila_side_16;

    public LinearLayout[] ff;
    public ScrollView sidebar_ss, sidebar_ss_s;
    public Button butSidebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidebar);

        in = new IN();

        Log.d("qwerty", "Start Sidebar");

        sidebar_lila = (LinearLayout)findViewById(R.id.sidebar_lila);

        lila_side_1  = (LinearLayout)findViewById(R.id.lila_side_1);
        lila_side_2  = (LinearLayout)findViewById(R.id.lila_side_2);
        lila_side_3  = (LinearLayout)findViewById(R.id.lila_side_3);
        lila_side_4  = (LinearLayout)findViewById(R.id.lila_side_4);
        lila_side_5  = (LinearLayout)findViewById(R.id.lila_side_5);
        lila_side_6  = (LinearLayout)findViewById(R.id.lila_side_6);
        lila_side_7  = (LinearLayout)findViewById(R.id.lila_side_7);
        lila_side_8  = (LinearLayout)findViewById(R.id.lila_side_8);
        lila_side_9  = (LinearLayout)findViewById(R.id.lila_side_9);
        lila_side_10 = (LinearLayout)findViewById(R.id.lila_side_10);
        lila_side_11 = (LinearLayout)findViewById(R.id.lila_side_11);
        lila_side_12 = (LinearLayout)findViewById(R.id.lila_side_12);
        lila_side_13 = (LinearLayout)findViewById(R.id.lila_side_13);
        lila_side_14 = (LinearLayout)findViewById(R.id.lila_side_14);
        lila_side_15 = (LinearLayout)findViewById(R.id.lila_side_15);
        lila_side_16 = (LinearLayout)findViewById(R.id.lila_side_16);

        ff = new LinearLayout[]{
                lila_side_1, lila_side_2, lila_side_3, lila_side_4,
                lila_side_5, lila_side_6, lila_side_7, lila_side_8,
                lila_side_9, lila_side_10, lila_side_11, lila_side_12,
                lila_side_13, lila_side_14, lila_side_15, lila_side_16};

        sidebar_tv_nik = (TextView)findViewById(R.id.sidebar_tv_nik);
        sidebar_tv_input =(TextView)findViewById(R.id.sidebar_tv_input);
        sidebar_image_user = (ImageView)findViewById(R.id.sidebar_image_user);

        butSidebar = (Button)findViewById(R.id.butSidebar);

        sidebar_ss = (ScrollView)findViewById(R.id.sidebar_ss);
        sidebar_ss_s = (ScrollView)findViewById(R.id.sidebar_ss_s);
        sidebar_ss.setOnTouchListener(this);
        sidebar_ss_s.setOnTouchListener(this);
        butSidebar.setOnTouchListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Sidebar.this.finish();
        if(in.getOnemay())
        {
            sidebar_tv_input.setText("");
            sidebar_tv_nik.setText(in.get_nik_user());
            sidebar_image_user.setImageResource(R.drawable.ricfas);
        }
        else
        {
            sidebar_tv_input.setText(R.string.input);
            sidebar_tv_nik.setText("");
            sidebar_image_user.setImageResource(R.drawable.transpar);
            Intent intent = new Intent(Sidebar.this, Settings_no_login.class);
            startActivity(intent);
        }
        //in.setBooleanMenu(false);

        for(int i = 0; i<16; i++){
            if(i == Integer.parseInt(String.valueOf(in.getChoice_of_menus()))){
                ff[i].setBackgroundResource(R.color.sidebar_on);
            }
            else {
                ff[i].setBackgroundResource(R.drawable.transpar);
            }
        }
    }

    public void onClickSidebar (View view){
        Intent intent_sidebar = null;
        switch (view.getId()){
            case R.id.sidebar_but_vhod:
                if(in.getOnemay()){
                }else {
                    intent_sidebar = new Intent(Sidebar.this,LogIN.class);
                    startActivity(intent_sidebar);
                    Sidebar.this.finish();
                }
                break;
            case R.id.butSidebar:
                sidebar_lila.setBackgroundResource(R.drawable.transpar);
                //in.setBooleanMenu(true);
                Sidebar.this.finish();
                break;
            case R.id.sid_but_1:
                if(in.getOnemay())
                {
                    intent_sidebar = new Intent(Sidebar.this, My_profile.class);
                    startActivity(intent_sidebar);
                    Sidebar.this.finish();
                }
                else{
                    openDialog();
                }
                break;
            case R.id.sid_but_2:
                if(in.getOnemay())
                {
                    intent_sidebar = new Intent(Sidebar.this, My_questions.class);
                    startActivity(intent_sidebar);
                    Sidebar.this.finish();
                }
                else{
                    openDialog();
                }
                break;
            case R.id.sid_but_3:
                intent_sidebar = new Intent(Sidebar.this, My_orders.class);
                startActivity(intent_sidebar);
                Sidebar.this.finish();
                break;
            case R.id.sid_but_4:
                intent_sidebar = new Intent(Sidebar.this, AddQuestion.class);
                in.set_add_quest_tip(0);
                startActivity(intent_sidebar);
                Sidebar.this.finish();
                break;
            case R.id.sid_but_5:
                intent_sidebar = new Intent(Sidebar.this, Advocates_list.class);
                startActivity(intent_sidebar);
                Sidebar.this.finish();
                break;
            case R.id.sid_but_6:
                intent_sidebar = new Intent(Sidebar.this, Map_jur_and_notary.class);
                startActivity(intent_sidebar);
                Sidebar.this.finish();
                break;
            case R.id.sid_but_7:
                intent_sidebar = new Intent(Sidebar.this, consultation_online.class);
                startActivity(intent_sidebar);
                Sidebar.this.finish();
                break;
            case R.id.sid_but_8:
                intent_sidebar = new Intent(Sidebar.this, create_document.class);
                startActivity(intent_sidebar);
                Sidebar.this.finish();
                break;
            case R.id.sid_but_9:
                intent_sidebar = new Intent(Sidebar.this, Notary_list.class);
                startActivity(intent_sidebar);
                Sidebar.this.finish();
                break;
            case R.id.sid_but_10:
                //-----------------------------------------------------------
                break;
            case R.id.sid_but_11:
                //-----------------------------------------------------------
                break;
            case R.id.sid_but_12:
                intent_sidebar = new Intent(Sidebar.this, Forum.class);
                startActivity(intent_sidebar);
                Sidebar.this.finish();
                break;
            case R.id.sid_but_13:
                intent_sidebar = new Intent(Sidebar.this, Paid_services.class);
                startActivity(intent_sidebar);
                Sidebar.this.finish();
                break;
            case R.id.sid_but_14:
                //-----------------------------------------------------------
                break;
            case R.id.sid_but_15:
                intent_sidebar = new Intent(Sidebar.this, Settings.class);
                startActivity(intent_sidebar);
                Sidebar.this.finish();
                break;
            case R.id.sid_but_16:
                intent_sidebar = new Intent(Sidebar.this, About_us.class);
                startActivity(intent_sidebar);
                Sidebar.this.finish();
                //-----------------------------------------------------------
                break;
        }
    }

    private void openDialog() {
        final Dialog dialog = new Dialog(Sidebar.this);
        dialog.setTitle("ВОЙТИ В СИСТЕМУ?");
        dialog.setContentView(R.layout.input_dialog_not_registered);

        Button but_dialog_reg = (Button) dialog.getWindow().findViewById(
                R.id.but_dialog_reg);

        but_dialog_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intD = new Intent(Sidebar.this, LogIN.class);
                startActivity(intD);
                Sidebar.this.finish();
            }
        });
        Button but_dialog_reg_cancel = (Button) dialog.getWindow().findViewById(
                R.id.but_dialog_reg_cancel);

        but_dialog_reg_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public float xX = 0;
    public float xNew = 0;
    Boolean tr = true;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_UP):
                if(xNew < -300){
                    Sidebar.this.finish();
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
