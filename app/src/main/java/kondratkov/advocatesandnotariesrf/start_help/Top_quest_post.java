package kondratkov.advocatesandnotariesrf.start_help;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
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
import java.io.InvalidObjectException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.MainActivity;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.api_classes.TopQuestion;
import kondratkov.advocatesandnotariesrf.consultation.Consult_phone_time;
import kondratkov.advocatesandnotariesrf.data_theme.mess_data;

public class Top_quest_post extends AppCompatActivity {

    public ListView lv_f = null;
    public Spinner spinner_f = null;
    public IN in;
    public int sort = 0;
    public List<String> list_top  = null;

    public String date = "-1";

    public FrameLayout asked_frameProg;
    public ProgressBar asked_progressBar;
    public boolean my_yes_no = true;
    public boolean bu = true;

    TopQuestion topQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_quest_post);

        in = new IN();
        findViewById(R.id.top_quest_but_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Top_quest_post.this.finish();
            }
        });

        lv_f = (ListView)findViewById(R.id.ask_listViewMess_top);

        new UrlConnectionTask().execute();
    }
    public void start_s_server(){
        list_top = getList(topQuestion);
        MyAdapterList myAdapterList = new MyAdapterList(Top_quest_post.this, list_top);
        lv_f.setAdapter(myAdapterList);
    }

    public List<String> getList(TopQuestion topQuestion) {

        List<String> ListRT = new ArrayList<String>();

        for (int i = 0; i <2; i++) {
            if(i==0){
                ListRT.add(topQuestion.QuestionBody);
            }else{
                ListRT.add(topQuestion.AnswerBody);
            }
        }

        return ListRT;
    }

    class MyAdapterList extends ArrayAdapter {

        private List<String>list= null;
        private Activity context;
        int di=0;

        public String[] arrayTheme = getResources().getStringArray(R.array.ArraySpecialization);

        public MyAdapterList(Activity context, List<String>list) {
            super(context, R.layout.ask_item_user, list);
            this.context = context;
            this.list = list;
        }

        public void start_profile_not(int i){
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.ask_item_user, parent, false);
            TextView tv_them;
            TextView tv_text;
            TextView tv_date;
            TextView tv_nik;
            TextView tv_read;


                if(di == 0){
                    rowView = inflater.inflate(R.layout.ask_item_user, parent, false);

                    TextView tv_mess = (TextView) rowView.findViewById(R.id.mess_user_tv);
                    tv_mess.setTextSize(in.get_font_1());
                    TextView tv_nik_user_mess = (TextView) rowView.findViewById(R.id.mess_user_tv_nik);
                    tv_nik_user_mess.setTextSize(in.get_font_2());

                    tv_mess.setText(list.get(0));;
                    tv_nik_user_mess.setText("Клиент");//list.get(position).get_name());
                    //icon_user_mess.setBackgroundResource(Integer.parseInt(list.get(position).getString("iconuser")));
                    di=1;
                } else{
                    rowView = inflater.inflate(R.layout.ask_item_jur, parent, false);

                    ImageView icon_mess_jur = (ImageView)rowView.findViewById(R.id.mess_jur_icon);
                    TextView tv_surmname_mess_jur=(TextView) rowView.findViewById(R.id.mess_jur_tv_surmname);
                    tv_surmname_mess_jur.setTextSize(in.get_font_2());
                    TextView tv_name_mess_jur=(TextView)rowView.findViewById(R.id.mess_jur_tv_name);
                    TextView tv_mess_jur=(TextView)rowView.findViewById(R.id.mess_jur_tv);
                    tv_mess_jur.setTextSize(in.get_font_1());
                    
                    Button but_icon = (Button)rowView.findViewById(R.id.mess_jur_but_icon);

                    tv_surmname_mess_jur.setText("Адвокат");//list.get(0).getJSONObject("jur").getString("Name"));
                    //tv_name_mess_jur.setText(list.get(position).getString("namejur"));
                    tv_mess_jur.setText(list.get(1));
                    //icon_mess_jur.setBackgroundResource(Integer.parseInt(list.get(position).getString("iconjur")));

                    /*but_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int iidj = 0;
                            for(int i=0; i<position+1; i++){
                                if(v.getId() == mas_view[i]){
                                    iidj = i;
                                }
                            }

                            in.set_id_jur(list.get(iidj).get_idj());

                            //Intent intent = new Intent(AskAQuestion.this,Profile_Jur.class);
//!!!!!!!!!!!!!!!!!!                            //startActivity(intent);
                        }
                    });*/
                }



            /*String s = arrayTheme[list.get(position).get_theme()];tv_text.setText(list.get(position).getString("specname"));
            tv_them.setText(s);
            tv_text.setText(list.get(position).get_text());//String("text"));

            tv_nik.setText(list.get(position).get_date());//"от "+list.get(position).get_name());//String("name"));

            if(list.get(position).get_read()==2){
                //tv_read.setBackgroundResource(R.color.read0);
                tv_read.setTextColor(getResources().getColor(R.color.read0));
                tv_nik.setTextColor(getResources().getColor(R.color.read0));
                tv_read.setText("новый");
            }
            else if(list.get(position).get_read()==0||list.get(position).get_read()==1||list.get(position).get_read()==4){
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
            }*/
            return rowView;
        }
    }

    class UrlConnectionTask extends AsyncTask<String, Void, String> {

        String result = "";

        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormEncodingBuilder()
                    .add("id", "1")
                    .build();

            Log.d("qwerty", formBody.toString());
            Request request = new Request.Builder().url("http://"+in.get_url()+"/TopQuestions/GetTopQuestion/"+in.get_idt()).build();
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
            Gson gg = new Gson();
            topQuestion = gg.fromJson(result, TopQuestion.class);
            start_s_server();
            super.onPostExecute(result);
        }
    }
}
