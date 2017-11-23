package kondratkov.advocatesandnotariesrf;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import java.util.Calendar;

import kondratkov.advocatesandnotariesrf.advocates.Advocates_list_quest;
import kondratkov.advocatesandnotariesrf.api_classes.ClientQuestion;
import kondratkov.advocatesandnotariesrf.consultation.Consult_phone_time;
import kondratkov.advocatesandnotariesrf.input.LogIN;

public class AddQuestion extends Activity implements View.OnTouchListener{
    public TextView surname, textView;
    public ImageView imageIcon;
    public Spinner spinnerAddQuest;
    public EditText etAddQuest;

    public String []strTip = null;
    public IN in= new IN();;
    private long docTip =1 ;

    public int tp;

    public SharedPreferences sPref_add_quest;

    public JSONObject jsonObject_mess;
    public String json_add_mess;
    public String json_id_mess;
    public String tip_mess = "";
    public int int_tip_mess = 0;
    public boolean bool_tip_mess = true;

    public  String URL_ADD_MESS  = "";//mess please
    public final String MESS_ITEM = "";//mess please
    private static final String DEBUG_TAG = "qwerty";
    //public final String SAVED_KOL_MESS = getResources().getString(R.string.kol_mess);

    public FrameLayout asked_frameProg;
    public ProgressBar asked_progressBar;

    ScrollView add_quest_scrollView;
    public String JSON_POST = "";
    public String SPECIALIZATION = "";

    public int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_question);

        in = new IN();

        add_quest_scrollView = (ScrollView) findViewById(R.id.add_quest_scrollView);
        add_quest_scrollView.setOnTouchListener(this);

        surname = (TextView)findViewById(R.id.addQuest_surname_jur);
        textView = (TextView)findViewById(R.id.addQuest_text_forum);
        etAddQuest = (EditText)findViewById(R.id.addQuest_et_new_mess);

        asked_frameProg = (FrameLayout)findViewById(R.id.add_qu_frameProg);
        asked_progressBar = (ProgressBar)findViewById(R.id.add_qu_progressBar);
        // etAddQuest = (EditText)findViewById(R.id.etAddQuest);

        //CharSequence date = DateFormat.format("h:mm", System.currentTimeMillis());("yyyy-MM-dd HH:mm:ss");

        strTip = getResources().getStringArray(R.array.ArrayTipAddQuest);
        spinnerAddQuest = (Spinner)findViewById(R.id.addQuest_spinner_them);
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.ArrayTipAddQuest, R.layout.my_quest_item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAddQuest.setAdapter(adapter);
        docTip = spinnerAddQuest.getSelectedItemId();
        spinnerAddQuest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                SPECIALIZATION = (String) getResources().getTextArray(R.array.ArraySpecialization)[pos];
                String ss = SPECIALIZATION;
                switch (pos) {
                    case 0:
                        in.set_doc_tip(strTip[pos]);
                        tip_mess = strTip[pos];
                        int_tip_mess = pos;
                        break;
                    case 1:
                        in.set_doc_tip(strTip[pos]);
                        tip_mess = strTip[pos];int_tip_mess = pos;
                        break;
                    case 2:
                        in.set_doc_tip(strTip[pos]);
                        tip_mess = strTip[pos];int_tip_mess = pos;
                        break;
                    case 3:
                        in.set_doc_tip(strTip[pos]);
                        tip_mess = strTip[pos];int_tip_mess = pos;
                        break;
                    case 4:
                        in.set_doc_tip(strTip[pos]);
                        tip_mess = strTip[pos];int_tip_mess = pos;
                        break;
                    case 5:
                        in.set_doc_tip(strTip[pos]);
                        tip_mess = strTip[pos];int_tip_mess = pos;
                        break;
                    case 6:
                        in.set_doc_tip(strTip[pos]);
                        tip_mess = strTip[pos];int_tip_mess = pos;
                        break;
                    case 7:
                        in.set_doc_tip(strTip[pos]);
                        tip_mess = strTip[pos];int_tip_mess = pos;
                        break;
                    case 8:
                        in.set_doc_tip(strTip[pos]);
                        tip_mess = strTip[pos];int_tip_mess = pos;
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        in.setChoice_of_menus(3);

        switch (in.get_add_quest_tip()){
            case 0:
                textView.setText(getResources().getString(R.string.add_question_no_who_mess_str));
                surname.setText("");
                break;
            case 1:
                textView.setText("Вопрос в чат");
                //textView.setBackgroundResource(R.color.reply1);
                surname.setText("");
                tp=1;
                break;
            case 2:
                textView.setText("");
                //textView.setBackgroundResource(R.color.reply1);
                in.set_place(0);
                surname.setText(in.get_fio_jur());//+" "+in.getName_addQuest().charAt(0)+".");
                tp=0;
                break;
            case 3:
                textView.setText("");
                surname.setText(in.get_fio_jur());//getSurname_addQuest() + " " + in.getName_addQuest().charAt(0) + ".");
                tp=0;
                break;
            case 4:
                textView.setText("Вопрос в чат");
                surname.setText("");
                //imageIcon.setImageResource(R.drawable.transpar);
                tp=1;
                break;

        }
    }

    public void id_nah(String s){
        try {
            JSONObject j = new JSONObject(s);
            if(j.getString("status").equals("OK")){
                in.set_idt(j.getInt("idt"));
                Intent intentQuest = new Intent(AddQuestion.this, AskAQuestion.class);
                startActivity(intentQuest);
                AddQuestion.this.finish();
            }
            else{
                Toast.makeText(this, "Произошла ошибка!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onClickAddQuest(View v){
        switch (v.getId()){
            case R.id.addQuest_but_menu:
                Intent intent134 = new Intent(AddQuestion.this, New_sidebar.class);//DocCreation.class);
                startActivity(intent134);
                break;


            case R.id.addQuest_but_new_mess:
                if(textView.getText().equals(getResources().getString(R.string.add_question_no_who_mess_str))){
                    Toast.makeText(this, "Не выбран адресат вопроса!", Toast.LENGTH_SHORT).show();
                }
                else if(String.valueOf(etAddQuest.getText()).replaceAll(" ", "").equals("")){
                    Toast.makeText(this, "!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Calendar date = Calendar.getInstance();
                    jsonObject_mess = new JSONObject();
                    /*try {
                        if(in.getOnemay()){
                            jsonObject_mess.put("idu",in.get_id_user());
                            jsonObject_mess.put("password", in.get_password_user());
                        }else {
                            jsonObject_mess.put("idu","no_idu");
                            jsonObject_mess.put("password", in.get_password_user());
                        }
                        if(textView.getText().equals(getResources().getString(R.string.name_side_forum))){
                            jsonObject_mess.put("tip_mess","true");
                            jsonObject_mess.put("idj", "");
                        }else {
                            jsonObject_mess.put("tip_mess","false");
                            jsonObject_mess.put("idj", in.getid_jur_addQuest());
                        }
                        jsonObject_mess.put("tip_who", 0);
                        jsonObject_mess.put("them", int_tip_mess);
                        jsonObject_mess.put("text", etAddQuest.getText());
                        jsonObject_mess.put("date", date.getTimeInMillis());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                    ClientQuestion clientQuestion = new ClientQuestion();
                    switch (in.get_add_quest_tip()){
                        case 1:
                            clientQuestion.Header = SPECIALIZATION;//String.valueOf(getResources().getStringArray(R.array.ArrayTipAddQuest)[int_tip_mess]);
                            clientQuestion.Body = String.valueOf(etAddQuest.getText());
                            clientQuestion.JuristId = 0;
                            break;
                        case 2:
                            clientQuestion.Header = SPECIALIZATION;//String.valueOf(getResources().getStringArray(R.array.ArrayTipAddQuest)[int_tip_mess]);
                            clientQuestion.Body = String.valueOf(etAddQuest.getText());
                            clientQuestion.AccountType = ClientQuestion.AccountTypes.Jurist;
                            clientQuestion.JuristId = in.get_id_jur();
                            break;
                    }


                    Gson gson = new Gson();
                    JSON_POST = gson.toJson(clientQuestion);

                    json_add_mess = String.valueOf(jsonObject_mess);

                    sPref_add_quest = getPreferences(MODE_PRIVATE);
                    int kol_mess = sPref_add_quest.getInt(getResources().getString(R.string.kol_mess), 0);
                    kol_mess = kol_mess + 1;
                    SharedPreferences.Editor ed = sPref_add_quest.edit();
                    ed.putInt(getResources().getString(R.string.kol_mess), kol_mess );
                    ed.commit();
                    SharedPreferences.Editor ed1 = sPref_add_quest.edit();
                    ed1.putLong(getResources().getString(R.string.kol_mess_date), date.getTimeInMillis());
                    ed1.commit();/**/

                    asked_frameProg.setBackgroundResource(R.color.frameOn);
                    asked_frameProg.setClickable(true);
                    asked_progressBar.setVisibility(ProgressBar.VISIBLE);

                    new UrlConnectionTask().execute(JSON_POST);
                    //new AsyncTaskAddQuest().execute();
                }
                break;
            case R.id.addQuest_but_close:
                AddQuestion.this.finish();
                break;
        }
    }

    private void openDialog() {
        final Dialog dialog = new Dialog(AddQuestion.this);
        dialog.setTitle("Куда направить вопрос?");
        dialog.setContentView(R.layout.add_quest_dialog_whom);

        Button btnchat = (Button) dialog.getWindow().findViewById(
                R.id.but_dialog_chat);

        Button btnjur = (Button) dialog.getWindow().findViewById(
                R.id.but_dialog_jur);

        Button btncancel = (Button) dialog.getWindow().findViewById(
                R.id.but_dialog_cancel);

        btnchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in.set_add_quest_tip(4);
                textView.setText(R.string.add_question_forum);
                surname.setText("");
                imageIcon.setImageResource(R.drawable.transpar);
                dialog.dismiss();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnjur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(in.getOnemay()){
                    //выбор юристa
                    Intent intent134 = new Intent(AddQuestion.this, Advocates_list_quest.class);//DocCreation.class);
                    startActivity(intent134);
                    dialog.dismiss();
                }
                else{
                    openDialog_no_reg();
                    //выбор только общий форум
                }
            }
        });

        dialog.show();
    }

    private void openDialog_no_reg() {
        final Dialog dialog_no = new Dialog(AddQuestion.this);
        dialog_no.setTitle("");
        dialog_no.setContentView(R.layout.add_quest_dialog_no_reg);

        Button dialog_no_reg_input = (Button) dialog_no.getWindow().findViewById(
                R.id.dialog_no_reg_input);

        Button dialog_no_reg_cancel = (Button) dialog_no.getWindow().findViewById(
                R.id.dialog_no_reg_cancel);

        dialog_no_reg_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent134 = new Intent(AddQuestion.this, LogIN.class);//DocCreation.class);
                startActivity(intent134);
                dialog_no.dismiss();
            }
        });
        dialog_no_reg_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_no.dismiss();
            }
        });

        dialog_no.show();
    }

    public void stopProgressBar(){
        asked_frameProg.setBackgroundResource(R.color.frameOff);
        asked_frameProg.setClickable(false);
        asked_progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    class UrlConnectionTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();

            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");
            String s= in.get_token_type()+" "+in.get_token();
            String s2 = params[0];
            String s3= "http://"+in.get_url()+"/ClientQuestions/PostClientQuestion";
            String s4="213";

            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://"+in.get_url()+"/ClientQuestions/PostClientQuestion")
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
            Log.d("qwerty", result);
            if (code>=200 && code<300) {
                Toast.makeText(AddQuestion.this,
                        "Вопрос отправлен!",
                        Toast.LENGTH_LONG).show();
                AddQuestion.this.finish();
            }else{
                Toast.makeText(AddQuestion.this,
                        "Нет связи с сервером!",
                        Toast.LENGTH_LONG).show();
                AddQuestion.this.finish();
            }
            super.onPostExecute(result);
        }
    }

    public float xX = 0;
    public float xNew = 0;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_UP):
                if(xNew > 300){
                    Intent intent = new Intent(AddQuestion.this, New_sidebar.class);
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
