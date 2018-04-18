package kondratkov.advocatesandnotariesrf.my_info;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.New_sidebar;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.Sidebar;
import kondratkov.advocatesandnotariesrf.account.GetProfileClient;

public class My_profile extends Activity {

    private static final String DEBUG_TAG = "qwerty";

    IN in;

    public boolean ld = true;
    public TextView tv_nik,  tv_surname ,tv_name, tv_patron, tv_email, tv_phone, tv_kol_quest;
    public LinearLayout lila_ld;
    public int code;

    public JSONObject jsonObjectMyProf = null;

    public String [] data_prof_v = null;//getResources().getStringArray(R.array.array_profile);
    public String [] data_profile = new String[6];

    public View lm[] = new View[6];//-ff

    public GetProfileClient getProfileClient;

    private ImageView my_prof_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);

        in = new IN();
        tv_nik = (TextView) findViewById(R.id.my_prof_tv_nik);
        tv_surname = (TextView) findViewById(R.id.my_prof_tv_surname);
        tv_name = (TextView) findViewById(R.id.my_prof_tv_name);
        tv_patron = (TextView) findViewById(R.id.my_prof_tv_patron);
        tv_email = (TextView) findViewById(R.id.my_prof_tv_email);
        tv_phone = (TextView) findViewById(R.id.my_prof_tv_phone);
        tv_kol_quest = (TextView) findViewById(R.id.my_prof_tv_kol_quest);

        my_prof_image = (ImageView)findViewById(R.id.my_prof_image);

        my_prof_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPhotoProfile();
            }
        });

        lila_ld = (LinearLayout) findViewById(R.id.my_prof_lila_ld);
        findViewById(R.id.my_prof_but_redactor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte1 = new Intent(My_profile.this, My_profile_redaction.class);
                startActivity(inte1);
               // startActivity(new Intent(My_profile.this, kondratkov.advocatesandnotariesrf.paid_services.Paid_services.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        in.setChoice_of_menus(0);

        if(in.getOnemay())
        {}
        else
        {
            My_profile.this.finish();
        }

        //new AsyncTaskProfile_my().execute();
        new UrlConnectionTask().execute();

    }
    public void start_activity(){

        //listZ(jsonObjectMyProf);
        //data_prof_v = getResources().getStringArray(R.array.array_profile);
        tv_nik.setText(in.get_nik_user());
        tv_surname.setText(getProfileClient.Surename);
        tv_name.setText(getProfileClient.Name);//jsonObjectMyProf.getString("name"));
        tv_patron.setText(getProfileClient.Patronymic);//jsonObjectMyProf.getString("patronymic"));
        tv_email.setText(getProfileClient.Email);//jsonObjectMyProf.getString("email"));
        tv_phone.setText(getProfileClient.Phone);//jsonObjectMyProf.getString("phone"));
        try {
            tv_kol_quest.setText(getProfileClient.Address.City);//jsonObjectMyProf.getString("city"));
        }catch (Exception e){}
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = My_profile.this.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));

        return px;
    }

    private void setPhotoProfile(){
        Intent intent = new Intent(My_profile.this, My_photo_redaction.class);
        startActivity(intent);
    }

    public void onClick(View v){
        switch(v.getId()){

            case R.id.my_prof_but_close_ak:
                in.setOnemay(false);
                My_profile.this.finish();
                break;

            case R.id.my_prof_but_close:
                try {
                    My_profile.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.my_prof_but_menu:
                Intent inte = new Intent(My_profile.this, New_sidebar.class);
                startActivity(inte);
                break;
        }
    }

    public void listZ1 (JSONObject jsonObjectMyProf) {
        for (int i =0; i<6; i++){
            switch(i){
                case 0:
                    try {
                        data_profile[0] = jsonObjectMyProf.getString("surname");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        data_profile[1] = jsonObjectMyProf.getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        data_profile[2] = jsonObjectMyProf.getString("patronymic");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        data_profile[3] = jsonObjectMyProf.getString("phone");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        data_profile[4] = jsonObjectMyProf.getString("email");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        data_profile[5] = jsonObjectMyProf.getString("city");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }


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
                getProfileClient = gson.fromJson(result, GetProfileClient.class);
                start_activity();
            }else{
                Toast.makeText(My_profile.this,
                        "Нет связи с сервером!",
                        Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
    }
}
