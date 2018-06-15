package kondratkov.advocatesandnotariesrf.start_help;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

//import com.yandex.money.api.methods.params.PaymentParams;
//import com.yandex.money.api.methods.params.PhoneParams;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.MainActivity;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.Settings_no_login;
import kondratkov.advocatesandnotariesrf.input.LogIN;
import kondratkov.advocatesandnotariesrf.input.WhoToContactActivity;
//import ru.yandex.money.android.PaymentActivity;

public class Start_activity_no_login extends Activity implements View.OnTouchListener{

    IN in;
    TextView tv1, tv2, tv3, tv4, tv_fsar, tv_msr;
    ScrollView scroll;
    int view_height;
    Point point;

    public SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity_no_login);

        sPref = PreferenceManager.getDefaultSharedPreferences(this);

        in = new IN();
        tv1 = (TextView) findViewById(R.id.start_no_tv_1);
        tv2 = (TextView) findViewById(R.id.start_no_tv_2);
        tv3 = (TextView) findViewById(R.id.start_no_tv_3);
        tv4 = (TextView) findViewById(R.id.start_no_tv_4);
        tv_fsar = (TextView) findViewById(R.id.start_no_tv_fsar);
        tv_msr = (TextView) findViewById(R.id.start_no_tv_msr);

        Typeface fontMy_1 = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Black.ttf");
        Typeface fontMy_2 = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf");
        Typeface fontMy_3 = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Light.ttf");
        tv1.setTypeface(fontMy_2);
        tv2.setTypeface(fontMy_2);
        tv3.setTypeface(fontMy_2);
        tv4.setTypeface(fontMy_2);
        tv_fsar.setTypeface(fontMy_2);
        tv_msr.setTypeface(fontMy_3);

        scroll = (ScrollView) findViewById(R.id.start_no_scroll);
        scroll.setOnTouchListener(this);
        /*findViewById(R.id.start_no_but_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Start_activity_no_login.this, Form_advocates.class));
            }
        });*/

    }

    @Override
    public void onStart(){
        super.onStart();

        Log.d("qwerty","IIIJIJIJ");

        SharedPreferences.Editor ed = sPref.edit();
        //ed.putBoolean("one_one_start", false);

        in.set_context(Start_activity_no_login.this);
        in.set_activity(Start_activity_no_login.this);

        if(in.getOnemay()){
            Intent intent = new Intent(Start_activity_no_login.this, MainActivity.class);
            startActivity(intent);
            Start_activity_no_login.this.finish();
        }else{
            if(sPref.getBoolean("one_one_start", true)){
                Intent intent = new Intent(Start_activity_no_login.this, Start_one_quest.class);
                startActivity(intent);
                Start_activity_no_login.this.finish();
            }
        }
    }

    private static final String CLIENT_ID = "88DF88009F6599A626FBF8121343192FCCC8641E5AC4C910B6E59B6C77A3C068";
    private static final String HOST = "https://demomoney.yandex.ru";
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // payment was successful
        }
    }

    public void onClick_on(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.start_no_but_menu:
                Log.d("qwerty", "1234545");
                intent = new Intent(Start_activity_no_login.this, Settings_no_login.class);
                startActivity(intent);
                break;
            case R.id.start_no_but_2:
                intent = new Intent(Start_activity_no_login.this, LogIN.class);
                startActivity(intent);
                break;
            case R.id.start_no_but_3:
                intent = new Intent(Start_activity_no_login.this, LogIN.class);
                startActivity(intent);
                break;
            case R.id.start_no_but_1:
                intent = new Intent(Start_activity_no_login.this, WhoToContactActivity.class);
                startActivity(intent);
                break;
            case R.id.start_no_but_4:
                intent = new Intent(Start_activity_no_login.this, Top_quest.class);
                startActivity(intent);
                break;
        }
    }

    public int dpToPx(int dp){
        DisplayMetrics displayMetrics = Start_activity_no_login.this.getResources().getDisplayMetrics();
        int px =Math.round(dp *(displayMetrics.xdpi /DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public float xX = 0;
    public float xNew = 0;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_UP):
                if(xNew > 300){
                    Intent intent = new Intent(Start_activity_no_login.this, Settings_no_login.class);
                    startActivity(intent);
                }
                Log.d("qwerty", "ACTION_UP");
                break;
            case (MotionEvent.ACTION_DOWN):
                xX = event.getX();
                Log.d("qwerty", "ACTION_DOWN xX "+xX);
                break;
            case (MotionEvent.ACTION_MOVE):
                Log.d("qwerty", "ACTION_MOVE ");
                int historySize = event.getHistorySize();
                for (int i = 0; i <historySize; i++) {
                    float x = event.getHistoricalX(i);
                    processMovement(x);
                }
                float x = event.getX();
                processMovement(x);
        }
        return false;
    }
    private void processMovement(float _x) {
        // Todo: Обработка движения.
        xNew = xX -_x;
        Log.d("qwerty", "xNew " + xNew);
    }

}