package kondratkov.advocatesandnotariesrf;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import kondratkov.advocatesandnotariesrf.account.GetProfileClient;
import kondratkov.advocatesandnotariesrf.advocates.Advocates_list;
import kondratkov.advocatesandnotariesrf.my_info.My_profile;
import kondratkov.advocatesandnotariesrf.notary.Notary_filter;
import kondratkov.advocatesandnotariesrf.notary.Notary_list;
import kondratkov.advocatesandnotariesrf.start_help.Start_activity_no_login;
import kondratkov.advocatesandnotariesrf.start_help.Top_quest;

public class MainActivity extends Activity implements View.OnTouchListener{

    public SharedPreferences sPref;
    ScrollView main_scrollView;
    IN in;
    TextView textViewNewMess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        in = new IN();
        main_scrollView = (ScrollView) findViewById(R.id.main_scrollView);
        main_scrollView.setOnTouchListener(this);

        textViewNewMess =(TextView)findViewById(R.id.textViewNewMess);

        sPref = PreferenceManager.getDefaultSharedPreferences(this);// getPreferences(MODE_PRIVATE);
        //Log.d("qwerty", "Что же там "+ String.valueOf(sPref.getBoolean("one_start", true)));
    }

    @Override
    protected void onStart(){
        super.onStart();
        OneStart();
        in.setChoice_of_menus(-1);
        if(in.getOnemay()){
            in = new IN();

            if(in.get_start_service()==false){
                startService(new Intent(this, Service_mess.class));
                in.set_start_service(true);
            }
            in.set_context(MainActivity.this);
            in.set_activity(MainActivity.this);
            new UrlConnectionTask().execute();
        }else{
            Intent intent = new Intent(MainActivity.this, Start_activity_no_login.class);
            startActivity(intent);
            MainActivity.this.finish();
        }

        if( sPref.getInt("pref_new_message",0) + sPref.getInt("pref_new_consult",0)>0){
            textViewNewMess.setText(String.valueOf(sPref.getInt("pref_new_message",0)+sPref.getInt("pref_new_consult",0)));
            textViewNewMess.setBackgroundResource(R.drawable.ic_oval_info);
        }else {
            textViewNewMess.setText("");
            textViewNewMess.setBackgroundResource(R.drawable.ic_oval_info_no);
        }

        //OneStart();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("qwerty", "STOP SERVIS");
        stopService(new Intent(this, Service_mess.class));
    }

    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.main_but_menu:
                intent = new Intent(MainActivity.this, New_sidebar.class);
                startActivity(intent);
                break;
            case R.id.main_but_2:
                //in.set_add_quest_tip(0);
                //intent = new Intent(MainActivity.this, AddQuestion.class);
                //startActivity(intent);
                intent = new Intent(MainActivity.this, Form_advocates.class);
                startActivity(intent);
                break;
            case R.id.main_but_3:
                intent = new Intent(MainActivity.this, Notary_filter.class);
                startActivity(intent);
                break;
            case R.id.main_but_4:
                intent = new Intent(MainActivity.this, Top_quest.class);
                startActivity(intent);
                break;

        }
    }

    public void OneStart(){
        if(sPref.getBoolean("one_start", true)){
            Log.d("qwerty", "вошли в превый раз " );
            SharedPreferences.Editor ed = sPref.edit();
            ed.putBoolean("one_start", false);
            ed.putInt("pref_sh", 16);
            ed.putBoolean("pref_setting_push_1", true);
            ed.putBoolean("pref_setting_push_2", true);
            ed.putBoolean("pref_setting_push_3", false);
            ed.commit();

            Log.d("qwerty", "размер шрифта "+ sPref.getInt("pref_sh", 16));
            in.set_font_1(sPref.getInt("pref_sh", 16));
            in.set_font_2(sPref.getInt("pref_sh", 16)-3);
            in.set_font_3(sPref.getInt("pref_sh", 16)-6);

        } else {
            Log.d("qwerty", "размер шрифта второй " + sPref.getInt("pref_sh", 0));
            in.set_font_1(sPref.getInt("pref_sh", 0));
            in.set_font_2(sPref.getInt("pref_sh", 0)-3);
            in.set_font_3(sPref.getInt("pref_sh", 0)-6);

        }
    }

    public int dpToPx(int dp){
        DisplayMetrics displayMetrics = MainActivity.this.getResources().getDisplayMetrics();
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
                    Intent intent = new Intent(MainActivity.this, New_sidebar.class);
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

    int code;
    class UrlConnectionTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();

            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            //RequestBody formBody = RequestBody.create(JSON, json_signup);

            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://"+in.get_url()+"/account/GetProfile")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                result = response.body().string();
                code = response.code();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            if(result!=null && code>=200 && code<300){
                Gson gson = new Gson();
                 GetProfileClient getProfileClient = gson.fromJson(result, GetProfileClient.class);
                in.set_id_user(getProfileClient.Id);
            }
            super.onPostExecute(result);
        }
    }
}

