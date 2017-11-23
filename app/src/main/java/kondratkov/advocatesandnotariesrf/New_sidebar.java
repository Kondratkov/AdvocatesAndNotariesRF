package kondratkov.advocatesandnotariesrf;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class New_sidebar extends Activity implements View.OnTouchListener{

    Button butSidebar, button_new_sid;
    IN in;
    public LinearLayout sidebar_lila, lila_start;
    public TextView sidebar_tv_nik, sidebar_tv_input;
    public TextView tv1, tv2, tv3, tv4, tv5, tv6;
    public ImageView sidebar_image_user;
    public LinearLayout lila_side_1, lila_side_2, lila_side_3,
            lila_side_4, lila_side_5, lila_side_6;
    public TextView [] tv_mas;

    public LinearLayout[] ff;
    public ScrollView sidebar_ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_sidebar);

        in = new IN();

        Log.d("qwerty", "Start Sidebar");

        button_new_sid = (Button)findViewById(R.id.button_new_sid);

        sidebar_lila = (LinearLayout)findViewById(R.id.sidebar_sid_lila);
        lila_start = (LinearLayout)findViewById(R.id.sidebar_sid_lila_ons);

        lila_side_1  = (LinearLayout)findViewById(R.id.lila_side_1);
        lila_side_2  = (LinearLayout)findViewById(R.id.lila_side_2);
        lila_side_3  = (LinearLayout)findViewById(R.id.lila_side_3);
        lila_side_4  = (LinearLayout)findViewById(R.id.lila_side_4);
        lila_side_5  = (LinearLayout)findViewById(R.id.lila_side_5);
        lila_side_6  = (LinearLayout)findViewById(R.id.lila_side_6);

        tv1 = (TextView)findViewById(R.id.sidebar_tv1);
        tv2 = (TextView)findViewById(R.id.sidebar_tv2);
        tv3 = (TextView)findViewById(R.id.sidebar_tv3);
        tv4 = (TextView)findViewById(R.id.sidebar_tv4);
        tv5 = (TextView)findViewById(R.id.sidebar_tv5);
        tv6 = (TextView)findViewById(R.id.sidebar_tv6);

        tv_mas = new TextView[]{tv1, tv2, tv3, tv4, tv5, tv6};
        for(int i=0; i<tv_mas.length; i++){
            tv_mas[i].setTextSize(in.get_font_1());
        }

        ff = new LinearLayout[]{
                lila_side_1, lila_side_2, lila_side_3, lila_side_4,
                lila_side_5, lila_side_6};

        sidebar_tv_nik = (TextView)findViewById(R.id.sidebar_sid_tv_nik);
        sidebar_tv_input =(TextView)findViewById(R.id.sidebar_sid_tv_input);
        sidebar_image_user = (ImageView)findViewById(R.id.sidebar_sid_image_user);

        sidebar_ss = (ScrollView)findViewById(R.id.sidebar_sid_ss);

        sidebar_ss.setOnTouchListener(this);
        button_new_sid.setOnTouchListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

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
        }
        //in.setBooleanMenu(false);

        for(int i = 0; i<6; i++){
            if(i == Integer.parseInt(String.valueOf(in.getChoice_of_menus()))){
                ff[i].setBackgroundResource(R.color.sidebar_on);
            }
            else {
                ff[i].setBackgroundResource(R.drawable.transpar);
            }
        }
        Animation anim = AnimationUtils.loadAnimation(New_sidebar.this, R.anim.anim_menu_yes);
        sidebar_lila.startAnimation(anim);
    }

    public void on_close(){
        Animation anim = AnimationUtils.loadAnimation(New_sidebar.this, R.anim.anim_menu_no);
        sidebar_lila.startAnimation(anim);
        New_sidebar.this.finish();
    }

    public void onClickSidebar (View view){
        Intent intent_sidebar = null;
        switch (view.getId()){
            case R.id.button_new_sid:
                on_close();
                sidebar_lila.setBackgroundResource(R.drawable.transpar);

                break;
            case R.id.sidebar_sid_but_vhod:
                if(in.getOnemay()){
                }else {
                    intent_sidebar = new Intent(New_sidebar.this,LogIN.class);
                    startActivity(intent_sidebar);
                    on_close();
                }
                break;
            case R.id.sid_sid_but_1:
                if(in.getOnemay())
                {
                    intent_sidebar = new Intent(New_sidebar.this, My_profile.class);
                    startActivity(intent_sidebar);
                    on_close();
                }
                else{
                    openDialog();
                }
                break;
            case R.id.sid_sid_but_2:
                if(in.getOnemay())
                {
                    intent_sidebar = new Intent(New_sidebar.this, My_questions.class);
                    startActivity(intent_sidebar);
                    on_close();
                }
                else{
                    openDialog();
                }
                break;
            case R.id.sid_sid_but_3:
                intent_sidebar = new Intent(New_sidebar.this, My_orders.class);
                startActivity(intent_sidebar);
                on_close();
                break;
            case R.id.sid_sid_but_4:
                //intent_sidebar = new Intent(New_sidebar.this, AddQuestion.class);
                //in.set_add_quest_tip(0);
                //startActivity(intent_sidebar);
                on_close();
                break;
            case R.id.sid_sid_but_5:
                intent_sidebar = new Intent(New_sidebar.this, Settings.class);
                startActivity(intent_sidebar);
                on_close();
                break;
            case R.id.sid_sid_but_6:
                intent_sidebar = new Intent(New_sidebar.this, About_us.class);
                startActivity(intent_sidebar);
                on_close();
                break;
            /*case R.id.sid_but_7:
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
                break;*/
        }
    }

    private void openDialog() {
        final Dialog dialog = new Dialog(New_sidebar.this);
        dialog.setTitle("ВОЙТИ В СИСТЕМУ?");
        dialog.setContentView(R.layout.input_dialog_not_registered);

        Button but_dialog_reg = (Button) dialog.getWindow().findViewById(
                R.id.but_dialog_reg);

        but_dialog_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intD = new Intent(New_sidebar.this, LogIN.class);
                startActivity(intD);
                on_close();
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
                    on_close();
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
