package kondratkov.advocatesandnotariesrf.my_info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import kondratkov.advocatesandnotariesrf.AskAQuestion;
import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.api_classes.ClientQuestion;
import kondratkov.advocatesandnotariesrf.api_classes.Comment;
import kondratkov.advocatesandnotariesrf.data_theme.AllQuestions;
import kondratkov.advocatesandnotariesrf.data_theme.theme_data;

import static com.google.android.gms.internal.zzir.runOnUiThread;

public class My_frag_quest_private extends Fragment {
    public ListView lv_p = null;
    public Spinner spinner_p = null;
    public IN in;
    public int sort = 0;

    public theme_data t_db;
    private List<AllQuestions> quest_list;
    public String date = "-1";
    public Context context_view;

    public ClientQuestion[] msArrayclientQuestion;
    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    public ClientQuestion[] clientQuestion_now;
    public ArrayList<Comment> arrayList;
    public int code;
    public int selection_list=100;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_quest_frag_private, container, false);

        in = new IN();


        context_view = in.get_context();
        arrayList = new ArrayList<Comment>();

        //t_db = new theme_data(in.get_context());

        //if(t_db.getContactsCount()==0){in.set_date("0");}//date new add
        //else {in.set_date(t_db.getSort1Question().get(t_db.getContactsCount() - 1).get_date());}//date new add

        lv_p = (ListView)view.findViewById(R.id.my_quest_frag_listView_p);

        lv_p.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                in.set_idt(clientQuestion_now[(int) id].Id);
                in.set_Tiptip(true);

                Comment comment = new Comment();
                comment.Date = clientQuestion_now[(int) id].Date;
                comment.AccountType = Comment.AccountTypes.Client;
                comment.Message = clientQuestion_now[(int) id].Body;
                in.setComment(comment);

                Intent inte = new Intent(in.get_context(), AskAQuestion.class);
                //inte.putExtra("id", in.get_idt());
                startActivity(inte);
            }
        });
        spinner_p = (Spinner)view.findViewById(R.id.my_quest_frag_spinner_p);
        onSpinnerS();

        return view;
    }

    public void onStart(){
        super.onStart();

        if (mTimer != null) {
            mTimer.cancel();
        }

        // re-schedule timer here
        // otherwise, IllegalStateException of
        // "TimerTask is scheduled already"
        // will be thrown
        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();

        // delay 1000ms, repeat in 5000ms
        mTimer.schedule(mMyTimerTask, 500);

       // msArrayclientQuestion = in.get_msArrayClientQuestions();
        /*MyAdapterList mam = new MyAdapterList(in.get_activity(), t_db.getSort2Question());
        lv_p.setAdapter(mam);
        lv_p.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                in.set_idt(in.get_list_pr().get(position).get_idt());
                in.set_id_user(in.get_list_pr().get(position).get_idu());
                in.set_id_jur(in.get_list_pr().get(position).get_idj());
                in.set_place(0);

                Intent inte = new Intent(in.get_context(), AskAQuestion.class);
                //inte.putExtra("id", in.get_idt());
                startActivity(inte);
            }
        });
        //for(int i = 0 ; i<t_db.getContactsCount(); i++){
            //t_db.deleteQuest(t_db.getAllQuestion().get(0));
        //}*/
        //new UrlConnectionTask().execute();

        //new AsyncTaskMess().execute();
    }

    public void onSpinnerS() {
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(in.get_context(), R.array.my_quest_array_sorting, R.layout.my_quest_item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_p.setAdapter(adapter);
        //docTip = spinner_sorting.getSelectedItemId();
        spinner_p.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                switch (pos) {
                    case 0:
                        sort = 0;
                       // MyAdapterList mam = new MyAdapterList(in.get_activity(), t_db.getSort2Question());
                        //lv_p.setAdapter(mam);
                        break;
                    case 1:
                        sort = 1;
                      //  MyAdapterList mam1 = new MyAdapterList(in.get_activity(), t_db.getSort1Question());
                      //  lv_p.setAdapter(mam1);
                        break;
                    case 2:
                        sort = 2;
                      //  MyAdapterList mam2 = new MyAdapterList(in.get_activity(), t_db.getSort3Question());
                     //  lv_p.setAdapter(mam2);
                        break;
                }
            }
        });
    }

    public void start_activity(){
        int now_int = 0;
        try{
            for(int i = 0; i<msArrayclientQuestion.length; i++){
            if(msArrayclientQuestion[i].JuristId!=0) {
                now_int++;
            }
        }}
        catch (Exception e){

        }

        clientQuestion_now = new ClientQuestion[now_int];
        int u=0;
        try {
            for(int i = 0; i<msArrayclientQuestion.length; i++){
                if(msArrayclientQuestion[i].JuristId!=0) {
                    //clientQuestion_now[u] = msArrayclientQuestion[i];
                    int d = now_int-1;
                    int df = d-u;
                    clientQuestion_now[df] = msArrayclientQuestion[i];
                    u++;
                }
            }
        }catch (Exception e){}


        MyAdapterList mam = new MyAdapterList(in.get_activity(), clientQuestion_now);
        lv_p.setAdapter(mam);

    }

    class MyAdapterList extends ArrayAdapter {

        private ClientQuestion[] clientQuestion1;
        private Activity context;

        public String[] arrayTheme = getResources().getStringArray(R.array.ArraySpecialization);

        public MyAdapterList(Activity context, ClientQuestion[] clientQuestions) {
            super(context, R.layout.my_quest_item_list, clientQuestions);
            this.context = context;
            this.clientQuestion1 = clientQuestions ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.my_quest_item_list, parent, false);

            TextView tv_them = (TextView) rowView.findViewById(R.id.my_ans_item_tv_them);
            tv_them.setTextSize(in.get_font_2());
            TextView tv_text = (TextView) rowView.findViewById(R.id.my_ans_item_tv_text);
            tv_text.setTextSize(in.get_font_1());
            TextView tv_date = (TextView) rowView.findViewById(R.id.my_ans_item_tv_date);
            tv_date.setTextSize(in.get_font_3());
            TextView tv_nik  = (TextView) rowView.findViewById(R.id.my_ans_item_tv_nik);
            tv_nik.setTextSize(in.get_font_2());
            TextView tv_read = (TextView) rowView.findViewById(R.id.my_ans_item_tv_read);
            tv_read.setTextSize(in.get_font_2());


            tv_them.setText(clientQuestion1[position].Header);
            tv_text.setText(clientQuestion1[position].Body);

            try {
                tv_nik.setText(clientQuestion1[position].Answers[0].Account.Fio);
            }catch (Exception e){

            }
            //String("text"));
                /*CharSequence dateJSON = DateFormat.format("dd.MM.yyyy", Long.parseLong(list.get(position).getString("date")));
                tv_date.setText(String.valueOf(dateJSON));*/
            /*if(list.get(position).get_date().length()>15) {
                tv_date.setText(list.get(position).get_date().substring(0, 5) + " " + list.get(position).get_date().substring(11, 16));//String("date"));
            }
            else{
                tv_date.setText(list.get(position).get_date());
            }*/
            //tv_nik.setText(list.get(position).get_date());//"от "+list.get(position).get_name());//String("name"));

            if(clientQuestion1[position].IsNotReadedAnswers>0){
                //tv_read.setBackgroundResource(R.color.read0);
                tv_read.setTextColor(getResources().getColor(R.color.read0));
                tv_nik.setTextColor(getResources().getColor(R.color.read0));
                tv_read.setText("новый");
            }
            else if(clientQuestion1[position].IsNotReadedAnswers==1){//list.get(position).get_read()==0||list.get(position).get_read()==1||list.get(position).get_read()==4){
                //tv_read.setBackgroundResource(R.color.read1);
                tv_read.setTextColor(getResources().getColor(R.color.read1));
                tv_nik.setTextColor(getResources().getColor(R.color.read1));
                tv_read.setText("неотв.");
            }
            else{
                //tv_read.setBackgroundResource(R.color.read2);
                tv_read.setTextColor(getResources().getColor(R.color.read2));
                tv_nik.setTextColor(getResources().getColor(R.color.read2));
                tv_read.setText("просм.");
            }
            return rowView;
        }
    }

    class UrlConnectionTask1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();

            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            //RequestBody formBody = RequestBody.create(JSON, json_signup);

            String s= in.get_token_type()+" "+in.get_token();
            String s3= "http://"+in.get_url()+"/ClientQuestions/GetMyQuestions";
            String d5 =in.getDoc_fail();


            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://"+in.get_url()+"/ClientQuestions/GetMyQuestions")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                result = response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Gson gson = new Gson();
            if(result!=null) {
                msArrayclientQuestion = gson.fromJson(result, ClientQuestion[].class);
                start_activity();
            }
            super.onPostExecute(result);
        }
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "dd:MMMM:yyyy HH:mm:ss a", Locale.getDefault());
            final String strDate = simpleDateFormat.format(calendar.getTime());

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //mCounterTextView.setText(strDate);
                    new UrlConnectionTask1().execute();
                }
            });
        }
    }
}
