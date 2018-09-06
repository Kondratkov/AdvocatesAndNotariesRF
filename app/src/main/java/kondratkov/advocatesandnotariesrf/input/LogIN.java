package kondratkov.advocatesandnotariesrf.input;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Headers;
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
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.account.ClientAccount;
import kondratkov.advocatesandnotariesrf.account.JuristSpecialization;
import kondratkov.advocatesandnotariesrf.my_info.My_photo_redaction;

public class LogIN extends Activity {

    private static final String DEBUG_TAG = "qwerty";
    public SharedPreferences sPref;

    public EditText etPhoneEmail, etPasswl;
    public TextView tvPhoneEmail, tvPasswl, tv_log_in_error;
    public CheckBox cb_save;
    public int flagView = 0;
    public boolean bool_log_in_server;

    IN in;
    public String json_login = "phone_and_emali, password ";
    JSONObject jsonObject_res = null;

    public int code;

    public Dialog dialog;

    public EditText mEditText_test;
    public Button mButton_test;
    public LinearLayout mLinearLayout_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_log_in);
        dialog = new Dialog(LogIN.this);
        in = new IN();

        etPhoneEmail = (EditText) findViewById(R.id.etPhoneEmail);
        etPasswl = (EditText) findViewById(R.id.etPasswl);
        tvPhoneEmail = (TextView) findViewById(R.id.tvPhoneEmail);
        tvPasswl = (TextView) findViewById(R.id.tvPasswl);
        cb_save = (CheckBox)findViewById(R.id.log_in_cb_save);
        tv_log_in_error = (TextView) findViewById(R.id.tv_log_in_error);


        mEditText_test = (EditText)findViewById(R.id.editText_setting);
        mButton_test = (Button)findViewById(R.id.button_setting);
        mLinearLayout_test = (LinearLayout)findViewById(R.id.lial_setting_main);

        mButton_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                in.set_url(String.valueOf(mEditText_test.getText()));
            }
        });

        mLinearLayout_test.setVisibility(View.GONE);




        etPhoneEmail.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    flagView = 1;
                    onTouchL();
                }
                return false;
            }
        });

        etPasswl.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    flagView = 2;
                    onTouchL();
                }
                return false;
            }
        });
        sPref = getPreferences(MODE_PRIVATE);
        etPhoneEmail.setText(sPref.getString("login", ""));
        etPasswl.setText(sPref.getString("password", ""));
    }

    public void onStart(){
        super.onStart();
        if(etPhoneEmail.getText().toString().replaceAll(" ","").equals("")==false){
            tvPhoneEmail.setText(R.string.log_in_email_phone);
            tvPasswl.setText(R.string.log_in_password);
        }
    }

    public void onTouchL(){

        if(flagView == 1){}
        else {}

        if (etPhoneEmail.getText().toString().replaceAll(" ","").equals(""))
        {
            if(flagView == 1){tvPhoneEmail.setText(R.string.log_in_email_phone); etPhoneEmail.setHint("");}
            else {tvPhoneEmail.setText("");etPhoneEmail.setHint(R.string.log_in_email_phone);}
        }
        else {tvPhoneEmail.setText(R.string.log_in_email_phone);}

        if (etPasswl.getText().toString().equals(""))
        {
            if(flagView == 2){tvPasswl.setText(R.string.log_in_password); etPasswl.setHint("");}
            else {tvPasswl.setText("");etPasswl.setHint(R.string.log_in_password);}
        }
        else {tvPasswl.setText(R.string.log_in_password);}

    }

    public void idnah(String s, int it){
        /*Log.d("qwerty", "login " + s);
        try {
            jsonObject_res = new JSONObject(s);

            if (jsonObject_res.getString("status").equals("Error: Unknown login")) {

                tv_log_in_error.setText("Неверный e-mail или номер телефона!");
            } else if (jsonObject_res.getString("status").equals("Error: Bad password")) {

                tv_log_in_error.setText("Неверный пароль!");
            } else if (jsonObject_res.getString("status").equals("OK")) {
                tv_log_in_error.setText("");
                in.set_id_user(Integer.parseInt(jsonObject_res.getString("idj")));
                in.set_password(String.valueOf(etPasswl.getText()));
                in.set_nik_user(jsonObject_res.getString("name"));

                in.set_nik_user(jsonObject_res.getString("name"));

                in.setOnemay(true);
                tv_log_in_error.setText("вход выполнен!");

                if(cb_save.isChecked()){
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString("login", String.valueOf(etPhoneEmail.getText()));
                    ed.commit();
                    SharedPreferences.Editor ed1 = sPref.edit();
                    ed1.putString("password", String.valueOf(etPasswl.getText()));
                    ed1.commit();
                }
                LogIN.this.finish();
            } else {
                tv_log_in_error.setText("Ошибка!");
            }

            //in.set_id_user(String.valueOf(etPhoneEmali.getText()));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        if(code>=300 && code <500){
            tv_log_in_error.setText("Неверный e-mail или пароль!");
        }else if(code>=200){
            tv_log_in_error.setText("");

            try {
                JSONObject json = new JSONObject(s);
                in.set_token(json.getString("access_token"));
                in.set_token_type(json.getString("token_type"));
                Log.d("qwertyTOKEN", in.get_token());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            in.setOnemay(true);
            tv_log_in_error.setText("вход выполнен!");

            if(cb_save.isChecked()){
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString("login", String.valueOf(etPhoneEmail.getText()));
                ed.commit();
                SharedPreferences.Editor ed1 = sPref.edit();
                ed1.putString("password", String.valueOf(etPasswl.getText()));
                ed1.commit();
            }

            new UrlConnectionTaskJuristSpecializationSectors().execute();

        } else {
            tv_log_in_error.setText("Ошибка!");
        }

        Button b = (Button)findViewById(R.id.butInputL);
        b.setEnabled(true);
    }

    public void onClick(View view){
        switch(view.getId()){

            case R.id.butInputL:
                view.setEnabled(false);
                if(etPhoneEmail.getText().toString().replaceAll(" ","").equals("")){
                    tv_log_in_error.setText("Заполните поле E-mail или телефон!");
                    bool_log_in_server = false;
                    view.setEnabled(true);
                }
                else if(etPasswl.getText().toString().replaceAll(" ","").equals("")){
                    tv_log_in_error.setText("Заполните поле пароль!");
                    bool_log_in_server = false;
                    view.setEnabled(true);
                }
                else {
                    tv_log_in_error.setText("");
                    bool_log_in_server = true;
                }

                if(bool_log_in_server) {
                    flagView = 0;
                    onTouchL();

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("phone_and_emali", etPhoneEmail.getText());
                        jsonObject.put("password", etPasswl.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    json_login = String.valueOf(jsonObject);
                    Dialog_loc();
                    view.setEnabled(true);
                }
                break;

            case R.id.forget_password:
                //Intent intent = new Intent(LogIN.this, My_photo_redaction.class);
                //startActivity(intent);
                dialog_password_help();
                break;

            case R.id.butReg:
                Intent intentReg = new Intent(LogIN.this, SignUP.class);
                startActivity(intentReg);
                break;
        }
    }

    private void Dialog_loc() {
        dialog.setTitle("загрузка...");
        dialog.setContentView(R.layout.input_dialog_window);
        dialog.setCancelable(true);
        new UrlConnectionTask().execute("grant_type=password&username="+String.valueOf(etPhoneEmail.getText())+
                "&password="+String.valueOf(etPasswl.getText()));
        dialog.show();
    }

    public void dialog_password_help(){
        final Dialog dialog = new Dialog(LogIN.this);
        dialog.setTitle("");
        dialog.setContentView(R.layout.input_password_help);

        Button btnClose = (Button) dialog.getWindow().findViewById(
                R.id.button_help_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final Button btnYes = (Button) dialog.getWindow().findViewById(
                R.id.button_help_yes);

        final EditText et = (EditText) dialog.getWindow().findViewById(R.id.etPhoneEmail_help);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et.getText().length()<3){
                    Toast.makeText(LogIN.this,
                            "Для напоминания пароля нужно ввести e-mail!",
                            Toast.LENGTH_LONG).show();
                }else{
                    JSONObject json_st = new JSONObject();
                    try {
                        json_st.put("email", et.getText());//String.valueOf(in.get_id()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String json_str = String.valueOf(json_st);
                    new UrlConnectionTaskForgotPassword().execute(json_str);

                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    class UrlConnectionTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            OkHttpClient client = new OkHttpClient();

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            RequestBody formBody = RequestBody.create(JSON, params[0]);
            String sd = params[0];

            Request request = new Request.Builder()
                    .header("Content-Type", "x-www-form-urlencoded")
                    //.url("http://192.168.0.100/token")
                    //.url("http://vsundupey.vds.serverdale.com/token")
                    //.url("http://195.128.124.172/token")
                    .url("http://"+in.get_url_token())
                    .post(formBody)
                    .build();


            try {
                Response response = client.newCall(request).execute();

                code = response.code();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.d("DEBUG", responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                result = response.body().string();
                Log.d("DEBUG", result);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            if(result.length()==0){
                tv_log_in_error.setText("Нет сети!");
            }else{
                idnah(result, code);
                super.onPostExecute(result);
            }

        }
    }

    class UrlConnectionTaskForgotPassword extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();
            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");
String ss = "http://"+in.get_url()+"/Account/ForgotPassword";

            Request request = new Request.Builder()
                    //.header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://"+in.get_url()+"/Account/ForgotPassword")
                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, params[0]))
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
            Toast.makeText(LogIN.this,
                    result+"Запрос 8888!", Toast.LENGTH_LONG).show();
            if(code>=200&&code<300&&result!=null){
                Toast.makeText(LogIN.this,
                        "Запрос на напоминание пароля принят! Ответ будет направлен на Вашу почту!",
                        Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
    }

    class UrlConnectionTaskJuristSpecializationSectors extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();
            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");
            String ss = "http://"+in.get_url()+"/JuristSpecializationSectors/GetJuristSpecializationSectors";

            Request request = new Request.Builder()
                    //.header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://app.mmka.info/api/JuristSpecializationSectors/GetJuristSpecializationSectors")//"http://"+in.get_url()+"/JuristSpecializationSectors/GetJuristSpecializationSectors")
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
            if(code>=200&&code<300&&result!=null){

                try{
                    Gson gson = new Gson();
                    in.set_list_specialization(gson.fromJson(result, JuristSpecialization[].class));
                }catch (Exception e){}

                if(in.get_list_specialization().length!=0){

                    String s = in.get_list_specialization()[2].SectorName;
                    //Intent intentReg1 = new Intent(LogIN.this, SignUP.class);
                    //Intent intentReg1 = new Intent(LogIN.this, Asked_user.class);

                    LogIN.this.finish();
                }
                else {
                    Toast.makeText(LogIN.this,
                            "Нет связи с сервером!",
                            Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(LogIN.this,
                        "Нет связи с сервером!",
                        Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
    }

    public class Adres{
        public int I;
        public String S;
        public Object o;
    }
    public class Adres_two{
        public int I1;
        public String S2;
    }
}
