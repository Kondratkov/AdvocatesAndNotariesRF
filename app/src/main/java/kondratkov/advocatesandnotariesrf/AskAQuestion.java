package kondratkov.advocatesandnotariesrf;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kondratkov.advocatesandnotariesrf.advocates.Advocate_profile;
import kondratkov.advocatesandnotariesrf.advocates.Advocates_list_quest;
import kondratkov.advocatesandnotariesrf.api_classes.ClientQuestion;
import kondratkov.advocatesandnotariesrf.api_classes.ClientQuestionAndComment;
import kondratkov.advocatesandnotariesrf.api_classes.Comment;
import kondratkov.advocatesandnotariesrf.api_classes.JuristAccounClass;
import kondratkov.advocatesandnotariesrf.api_classes.PostConsultation;
import kondratkov.advocatesandnotariesrf.api_classes.QuestionAnswer;
import kondratkov.advocatesandnotariesrf.consultation.Consult_phone_time;
import kondratkov.advocatesandnotariesrf.data_theme.AllMess;
import kondratkov.advocatesandnotariesrf.data_theme.mess_data;
import kondratkov.advocatesandnotariesrf.my_info.My_frag_orders_doc;
import kondratkov.advocatesandnotariesrf.my_info.My_questions;

public class AskAQuestion extends Activity {

    public ListView lv_mess;
    public JSONArray jsonArrayMess = null;
    public EditText etTextQues;
    public JSONObject res = null;
    public int [] mas_view;

    public IN in;
    public Calendar date = Calendar.getInstance();
    public boolean bool_ulr_newmess = true;
    public Thread thread;
    TextView ask_tv_forum;

    public FrameLayout asked_frameProg;
    public ProgressBar asked_progressBar;
    public FrameLayout asked_add_frameProg;
    public ProgressBar asked_add_progressBar;
    public boolean my_yes_no = true;
    public boolean bu = true;

    public Comment[] msArraycomments;
    public ArrayList<Comment>arrayList;
    public int code;

    public int selection_list=100;
    public int topOffset;

    public boolean startPositionList = true;
    public int numberColomn = 0;

    public LinearLayout lila1, lila2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_aquestion);

        in = new IN();

        lila1 = (LinearLayout) findViewById(R.id.lila_vopros_chat);
        lila2 = (LinearLayout) findViewById(R.id.lila_vopros_lichca);
        boolean v = in.get_Tiptip();

        if(in.get_Tiptip()){
            lila1.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1), (float) 0));
            lila2.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 0));
        }else {
            lila1.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(55), (float) 0));
            lila2.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1), (float) 0));
        }

        jsonArrayMess = new JSONArray();
        arrayList = new ArrayList<Comment>();

        etTextQues = (EditText)findViewById(R.id.ask_et_new_mess);
        lv_mess = (ListView)findViewById(R.id.ask_listViewMess);

        ask_tv_forum = (TextView)findViewById(R.id.ask_tv_forum);

        //if (in.get_place()==0) {
        ask_tv_forum.setText("");
        //}
        //else{
        //    ask_tv_forum.setText("чат");
        //}
    }

    public boolean bn = true;
    public void potock(boolean b){
        bn = b;
        bu = false;
        Runnable runnable = new Runnable() {
            public void run() {
                // Переносим сюда старый код
                long endTime = System.currentTimeMillis()
                        + 6* 1000;
                while (System.currentTimeMillis() < endTime) {
                    synchronized (this) {
                        try {
                            //new AsyncTaskM().execute();
                            Log.d("122","1q2");
                            selection_list = lv_mess.getFirstVisiblePosition();
                            View v = lv_mess.getChildAt(0);
                            topOffset = (v == null) ? 0 : v.getTop();
                            new UrlConnectionTask1().execute();

                            if(bn){wait(endTime - System.currentTimeMillis());
                                endTime = System.currentTimeMillis() + 6 * 1000;}
                            else{
                                wait(endTime - System.currentTimeMillis());
                                endTime = System.currentTimeMillis()-10;
                            }
                        } catch (Exception e) {Log.d("qwerty", "поток asked остановлен");
                        }
                    }
                }
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }

    public int dpToPx(int dp){
        DisplayMetrics displayMetrics = AskAQuestion.this.getResources().getDisplayMetrics();
        int px =Math.round(dp *(displayMetrics.xdpi /DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(in.getOnemay()==false){
            AskAQuestion.this.finish();
        }
        bu = true;

        bool_ulr_newmess = false;

        asked_frameProg = (FrameLayout)findViewById(R.id.asked_frameProg);
        asked_progressBar = (ProgressBar)findViewById(R.id.asked_progressBar);
        asked_add_frameProg = (FrameLayout)findViewById(R.id.asked_add_frameProg);
        asked_add_progressBar = (ProgressBar)findViewById(R.id.asked_add_progressBar);

        asked_frameProg.setBackgroundResource(R.color.frameOn);
        asked_frameProg.setClickable(true);
        asked_progressBar.setVisibility(ProgressBar.VISIBLE);

        //new UrlConnectionTask().execute();
        in.set_date("0");
        potock(true);
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("qwerty", "onStop");
        potock(false);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        potock(false);
        Log.d("qwerty", "onDestroy");
    }

    public void start_new_list(){

        if(bool_ulr_newmess){
            bool_ulr_newmess = false;
            MyAdapterList mam = new MyAdapterList(in.get_activity(), arrayList);
            lv_mess.setAdapter(mam);
            //new AsyncTaskMess().execute();
        }else{
            if(msArraycomments == null){
                Log.d("qwerty", "1");
                Comment comment = new Comment();
                comment.Message = "123123";//clientQuestions.Body;
                comment.CommentType = Comment.CommentType1.Question;
                comment.AccountType = Comment.AccountTypes.Client;
                msArraycomments = new Comment[]{comment};
                MyAdapterList mam = new MyAdapterList(in.get_activity(), arrayList);
                lv_mess.setAdapter(mam);
            }else{
                Log.d("qwerty", "2");
                MyAdapterList mam = new MyAdapterList(in.get_activity(), arrayList);
                lv_mess.setAdapter(mam);
            }
        }
        if(selection_list > arrayList.size()-2){
            lv_mess.setSelection(arrayList.size());
        }else{
            lv_mess.setSelectionFromTop(selection_list, topOffset);
        }

        if(startPositionList){
            startPositionList = false;
            numberColomn = arrayList.size();
            topOffset = arrayList.size()-1;
            lv_mess.setSelection(arrayList.size());
        }else if(numberColomn!=arrayList.size()){
            numberColomn = arrayList.size();
            topOffset = arrayList.size();
            lv_mess.setSelection(arrayList.size());
        }else{}



    }
    @TargetApi(Build.VERSION_CODES.M)
    private void openDialog(final int id, final Comment comment) {
        final Dialog dialog = new Dialog(AskAQuestion.this);
        dialog.setTitle("");
        dialog.setContentView(R.layout.add_new_quest_advak);
        final int IDD= id;

        TextView tv_time    = (TextView) dialog.findViewById(R.id.cons_phone_tv_tipnew);
        final EditText editText1 = (EditText)dialog.findViewById(R.id.editTextnnn);


        /*if(textView_time.getText().length()>4){
            String sss = textView_time.getText().toString();
            if(Integer.parseInt(String.valueOf(sss.charAt(1)))>2){
                datetime = s+" PM";
            }else {
                datetime = s+" AM";
            }
        }else{
            datetime = s+" AM";
        }
        s = String.valueOf(timePicker.getHour())+":";
        if(timePicker.getMinute()<10) {
            s += "0"+ String.valueOf(timePicker.getMinute())+ " ";
        }else {
            s += String.valueOf(timePicker.getMinute())+ " ";
        }
        s +=String.valueOf(datePicker.getDayOfMonth())+"."+String.valueOf(datePicker.getMonth()+1)+"."+String.valueOf(datePicker.getYear());

        datetime = String.valueOf(datePicker.getDayOfMonth())+"/"+String.valueOf(datePicker.getMonth()+1)+"/"+String.valueOf(datePicker.getYear())+" ";
        if(timePicker.getHour()>12){
            datetime += String.valueOf(24-timePicker.getHour())+":"+String.valueOf(timePicker.getMinute())+ " PM";
        }else{
            datetime += String.valueOf(timePicker.getHour())+":"+String.valueOf(timePicker.getMinute())+" AM";
        }
*/
        editText1.setText("'" +arrayList.get(0).Message+"'");
        in.set_text(String.valueOf(editText1.getText()));

        Button btnAdd = (Button) dialog.getWindow().findViewById(
                R.id.cons_phone_but_okBNew);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                ClientQuestion clientQuestion = new ClientQuestion();

                clientQuestion.Header = "общение после ответа на вопрос из чата";//comment.From;
                clientQuestion.Body = String.valueOf(editText1.getText());

                clientQuestion.AccountType = ClientQuestion.AccountTypes.Jurist;
                clientQuestion.JuristId = IDD;

                String JSON_POST = gson.toJson(clientQuestion);

                new UrlConnectionTask11().execute(JSON_POST);

                dialog.dismiss();
            }
        });

        Button btnDismiss = (Button) dialog.getWindow().findViewById(
                R.id.cons_phone_btn_cancelBNew);

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    class UrlConnectionTask11 extends AsyncTask<String, Void, String> {

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
                Toast.makeText(AskAQuestion.this,
                        "Вопрос отправлен! Теперь общение с выбранным адвокатом будет в списке 'частные'!",
                        Toast.LENGTH_LONG).show();
                AskAQuestion.this.finish();
            }else{
                Toast.makeText(AskAQuestion.this,
                        "Нет связи с сервером!",
                        Toast.LENGTH_LONG).show();
                AskAQuestion.this.finish();
            }
            super.onPostExecute(result);
        }
    }

    class MyAdapterList extends ArrayAdapter {

        private ArrayList<Comment> comments1;
        private Activity context;

        public String[] arrayTheme = getResources().getStringArray(R.array.ArraySpecialization);

        public MyAdapterList(Activity context, ArrayList<Comment>comments) {
            super(context, R.layout.ask_item_user, comments);
            this.context = context;
            this.comments1 = comments;
            this.comments1.add(0, in.getComment());
            mas_view = new int[comments.size()+1];
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = null;
            CharSequence dateISNow = DateFormat.format("dd.MM", System.currentTimeMillis());
            CharSequence dateJSON = null;

            if(comments1.get(position).AccountType == Comment.AccountTypes.Client){
                rowView = inflater.inflate(R.layout.ask_item_user, parent, false);

                try{
                    LinearLayout lilamess_jur_tv = (LinearLayout)rowView.findViewById(R.id.lilamess_jur_tv);

                    TextView tv_mess = (TextView) rowView.findViewById(R.id.mess_user_tv);
                    ImageView imageView = (ImageView)rowView.findViewById(R.id.mess_user_icon);
                    tv_mess.setTextSize(in.get_font_1());
                    TextView tv_mess_date = (TextView) rowView.findViewById(R.id.mess_user_tv_date);
                    tv_mess_date.setTextSize(in.get_font_2());
                    TextView tv_nik_user_mess = (TextView) rowView.findViewById(R.id.mess_user_tv_nik);
                    tv_nik_user_mess.setTextSize(in.get_font_2());

                    tv_mess.setText(comments1.get(position).Message);

                    tv_mess_date.setText(in.dateDisplay(comments1.get(position).Date));
                    in.set_data_servise(comments1.get(position).Date);

                    tv_nik_user_mess.setText("Вы");//list.get(position).get_name())

                    //imageView


                    //icon_user_mess.setBackgroundResource(Integer.parseInt(list.get(position).getString("iconuser")));
                }catch (Exception e){}

            } else{
                rowView = inflater.inflate(R.layout.ask_item_jur, parent, false);
                try{
                    LinearLayout lilamess_jur_tv = (LinearLayout)rowView.findViewById(R.id.lilamess_jur_tv);
                    ImageView icon_mess_jur = (ImageView)rowView.findViewById(R.id.mess_jur_icon);
                    TextView tv_surmname_mess_jur=(TextView) rowView.findViewById(R.id.mess_jur_tv_surmname);
                    tv_surmname_mess_jur.setTextSize(in.get_font_2());
                    TextView tv_name_mess_jur=(TextView)rowView.findViewById(R.id.mess_jur_tv_name);
                    TextView tv_mess_jur=(TextView)rowView.findViewById(R.id.mess_jur_tv);
                    tv_mess_jur.setTextSize(in.get_font_1());
                    TextView tv_mess_jur_date=(TextView)rowView.findViewById(R.id.mess_jur_tv_date);
                    tv_mess_jur_date.setTextSize(in.get_font_2());
                    Button but_icon = (Button)rowView.findViewById(R.id.mess_jur_but_icon);
                    mas_view[position] = but_icon.getId();

                    //tv_mess_jur_date.setText(list.get(position).getString("date"));

                    tv_surmname_mess_jur.setText(comments1.get(position).From);

                    tv_mess_jur_date.setText(in.dateDisplay(comments1.get(position).Date));
                    in.set_data_servise(comments1.get(position).Date);

                    //tv_name_mess_jur.setText(list.get(position).getString("namejur"));
                    tv_mess_jur.setText(comments1.get(position).Message);
                    tv_mess_jur.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //region.Cities[id_SudTer].Region = region;
                            if(in.get_Tiptip()==false){
                                if(comments1.get(position).GetAccountTypes()==1){
                                    openDialog(comments1.get(position).AccountId, comments1.get(position));
                                }
                            }
                        }
                    });
                    lilamess_jur_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //region.Cities[id_SudTer].Region = region;
                            if(in.get_Tiptip()==false){
                                if(comments1.get(position).GetAccountTypes()==1){
                                    openDialog(comments1.get(position).AccountId, comments1.get(position));
                                }
                            }
                        }
                    });

                    //icon_mess_jur.setBackgroundResource(Integer.parseInt(list.get(position).getString("iconjur")));

                    but_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int iidj = 0;
                            for(int i=0; i<position+1; i++){
                                if(v.getId() == mas_view[i]){
                                    iidj = i;
                                }
                            }
                            in.set_id_jur(comments1.get(position).AccountId);

                            Intent intent = new Intent(AskAQuestion.this, Advocate_profile.class);
                            startActivity(intent);
                        }
                    });
                }catch (Exception e){}

            }
            return rowView;
        }
    }

    public void stopProgressBar(){
        asked_frameProg.setBackgroundResource(R.color.frameOff);
        asked_frameProg.setClickable(false);
        asked_progressBar.setVisibility(ProgressBar.INVISIBLE);

        if(my_yes_no = true) {
            asked_add_frameProg.setBackgroundResource(R.color.frameOff);
            asked_add_frameProg.setClickable(false);
            asked_add_progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    public void onClickMess(View v){
        switch (v.getId()){
            case R.id.ask_but_cancel:
                AskAQuestion.this.finish();
                break;
            case R.id.ask_ibut_add_mess:

                if(etTextQues.getText().length() == 0){

                }else{
                    bool_ulr_newmess = true;

                    in.set_text(String.valueOf(etTextQues.getText()));
                    etTextQues.setText("");
                    asked_add_frameProg.setBackgroundResource(R.color.frameOn);
                    asked_add_frameProg.setClickable(true);
                    asked_add_progressBar.setVisibility(ProgressBar.VISIBLE);

                    JSONObject json_st = new JSONObject();
                    try {
                        json_st.put("MessageBody", in.get_text());//String.valueOf(in.get_id()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String json_str = String.valueOf(json_st);

                    new UrlConnectionTaskNewMess().execute(json_str);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etTextQues.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

                break;
        }
    }

    class UrlConnectionTask1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            OkHttpClient client = new OkHttpClient();
            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");
            //RequestBody formBody = RequestBody.create(JSON, json_signup);
            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://"+in.get_url()+"/ClientQuestions/GetNewComment/"+in.get_idt())
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
            Gson gson = new Gson();
            if(result!=null && 200<=code && code<300) {

                msArraycomments = gson.fromJson(result, Comment[].class);
                arrayList = new ArrayList<Comment>();
                for (int i = 0; i < msArraycomments.length; i++) {
                    arrayList.add(msArraycomments[i]);
                }
                start_new_list();
            }
            super.onPostExecute(result);
        }
    }

    class UrlConnectionTaskNewMess extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            OkHttpClient client = new OkHttpClient();
            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            //RequestBody formBody = RequestBody.create(JSON, json_signup);
            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://"+in.get_url()+"/ClientQuestions/AddCommentOne/"+in.get_idt())
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
            //Gson gson = new Gson();
            if(result!=null && 200<=code && code<300){
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Comment comment = gson.fromJson(result, Comment.class);
                arrayList.add(comment);
                selection_list = arrayList.size()-2;
                start_new_list();
            }
            super.onPostExecute(result);
        }
    }
}
