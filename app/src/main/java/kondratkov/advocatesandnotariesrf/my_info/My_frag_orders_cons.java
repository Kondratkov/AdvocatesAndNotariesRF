package kondratkov.advocatesandnotariesrf.my_info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.List;

import kondratkov.advocatesandnotariesrf.AskAQuestion;
import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.api_classes.PostConsultation;
import kondratkov.advocatesandnotariesrf.data_theme.AllQuestions_forum;
import kondratkov.advocatesandnotariesrf.data_theme.theme_data_forum;
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
import java.util.List;

import kondratkov.advocatesandnotariesrf.AskAQuestion;
import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.data_theme.AllQuestions_forum;
import kondratkov.advocatesandnotariesrf.data_theme.theme_data_forum;
import kondratkov.advocatesandnotariesrf.notary.Notary_profile;

public class My_frag_orders_cons extends Fragment {

    public ListView lv_f = null;
    public Spinner spinner_f = null;
    public IN in;
    public int sort = 0;


    public String date = "-1";
    public Context context_view;

    public PostConsultation[] mcArrayConsultation;
    public JSONArray jsonArrayList = null;

    public SharedPreferences sPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_orders_cons, container, false);

        in = new IN();

        sPref = PreferenceManager.getDefaultSharedPreferences(view.getContext());//getPreferences(MODE_PRIVATE);

        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("pref_new_consult", 0);
        ed.commit();

        context_view = in.get_context();

        lv_f = (ListView)view.findViewById(R.id.my_frag_ord_cons);
        onSpinnerS();

        return view;
    }

    public void onStart(){
        super.onStart();
        Log.d("qwerty e", "!!!! tp=3 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        new UrlConnectionTask().execute();
        //new AsyncTaskMess().execute();
    }

    public void onSpinnerS() {
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(in.get_context(), R.array.my_quest_array_sorting, R.layout.my_quest_item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /*spinner_f.setAdapter(adapter);
        //docTip = spinner_sorting.getSelectedItemId();
        spinner_f.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                switch (pos) {
                    case 0:
                        sort = 0;
                        //MyAdapterList mam = new MyAdapterList(in.get_activity(), t_db.getSort2Question());
                        //lv_f.setAdapter(mam);
                        break;
                    case 1:
                        sort = 1;
                        //MyAdapterList mam1 = new MyAdapterList(in.get_activity(), t_db.getSort1Question());
                        //lv_f.setAdapter(mam1);
                        break;
                    case 2:
                        sort = 2;
                        //MyAdapterList mam2 = new MyAdapterList(in.get_activity(), t_db.getSort3Question());
                        //lv_f.setAdapter(mam2);
                        break;
                }
            }
        });*/
    }

    public void start_activity(){

        MyAdapterList mam = new MyAdapterList(in.get_activity(), mcArrayConsultation);
        lv_f.setAdapter(mam);

        lv_f.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                /*try {
                    in.set_idt(listJSON.get(position).getInt("message"));
                    if(listJSON.get(position).getInt("connect")==1){
                        in.set_doc_tip("Консультация по телефону");
                    }else{
                        in.set_doc_tip("Консультация online");
                    }
                    in.set_doc_them(listJSON.get(position).getString("specname"));
                    in.set_doc_dt(listJSON.get(position).getString("dt"));
                    in.set_doc_status(listJSON.get(position).getInt("pay"));
                    in.set_doc_pay(listJSON.get(position).getString("paysum"));
                    in.set_doc_num(listJSON.get(position).getString("paynum"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                in.set_place(0);
                in.set_idt(mcArrayConsultation[position].Id);
                Intent int3 = new Intent(in.get_context(), My_perep_orders_mess.class);
                startActivity(int3);
            }
        });
    }

    class MyAdapterList extends ArrayAdapter {

        private PostConsultation[] postConsultation= null;
        private Activity context;

        public String[] arrayTheme = getResources().getStringArray(R.array.ArraySpecialization);

        public MyAdapterList(Activity context, PostConsultation[] postConsultation) {
            super(context, R.layout.my_orders_item_mess, postConsultation);
            this.context = context;
            this.postConsultation = postConsultation;
        }

        public void start_profile_not(int i){
            //Intent intent = new Intent(Notary_list.this, Notary_profile.class);
            //startActivity(intent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.my_orders_item_mess, parent, false);
            TextView tv_them;
            TextView tv_text;
            TextView tv_date;
            TextView tv_nik;
            TextView tv_read;


                if(postConsultation[position].ContactMethod ==PostConsultation.preferredContactMethod.Phone){
                    rowView = inflater.inflate(R.layout.my_orders_item_phone, parent, false);

                    tv_them = (TextView) rowView.findViewById(R.id .my_order_phone_tv_them);
                    tv_them.setTextSize(in.get_font_2());
                    tv_text = (TextView) rowView.findViewById(R.id.my_order_phone_tv_text);
                    tv_text.setTextSize(in.get_font_1());
                    tv_date = (TextView) rowView.findViewById(R.id.my_order_phone_tv_date);
                    tv_date.setTextSize(in.get_font_3());
                    tv_nik  = (TextView) rowView.findViewById(R.id.my_order_phone_tv_nik);
                    tv_nik.setTextSize(in.get_font_2());
                    tv_read = (TextView) rowView.findViewById(R.id.my_order_phone_tv_read);
                    tv_read.setTextSize(in.get_font_2());

                    tv_nik.setText("Консультация по телефону");
                    //tv_them.setText(postConsultation[position].ClientPreferredConsultationDate.);
                    tv_text.setText(postConsultation[position].ConsultationQuestion);
                    try {
                        tv_them .setText(in.dateDisplayConsPhone(postConsultation[position].ClientPreferredConsultationDate
                        ));
                    }catch (Exception e){}
                    if(postConsultation[position].State == PostConsultation.ServiceState.InProgress){
                        tv_read.setText("обработка");
                        tv_read.setTextColor(getResources().getColor(R.color.read2));
                    }else if(postConsultation[position].State == PostConsultation.ServiceState.Completed) {
                        tv_read.setText("выполнен");
                        tv_read.setTextColor(getResources().getColor(R.color.read3));
                    }else if(postConsultation[position].State == PostConsultation.ServiceState.WaitingForPayment) {
                        tv_read.setText(String.valueOf(postConsultation[position].Order.PaymentAmount)+" \u20BD");
                        tv_read.setTextColor(getResources().getColor(R.color.read1));
                    }else {
                        tv_read.setText("принят");
                        tv_read.setTextColor(getResources().getColor(R.color.read3));
                    }


                }else{
                    rowView = inflater.inflate(R.layout.my_orders_item_mess, parent, false);

                    tv_them = (TextView) rowView.findViewById(R.id.my_order_mess_tv_them);
                    tv_them.setTextSize(in.get_font_2());
                    tv_text = (TextView) rowView.findViewById(R.id.my_order_mess_tv_text);
                    tv_text.setTextSize(in.get_font_1());
                    tv_date = (TextView) rowView.findViewById(R.id.my_order_mess_tv_date);
                    tv_date.setTextSize(in.get_font_3());
                    tv_nik  = (TextView) rowView.findViewById(R.id.my_order_mess_tv_nik);
                    tv_nik.setTextSize(in.get_font_2());
                    tv_read = (TextView) rowView.findViewById(R.id.my_order_mess_tv_read);
                    tv_read.setTextSize(in.get_font_2());

                    tv_nik.setText("Консультация online");
                    //tv_them.setText(list.get(position).getString("specname"));
                    //tv_text.setText(list.get(position).getString("desc"));
                    tv_text.setText(postConsultation[position].ConsultationQuestion);

                    try {
                        tv_date.setText(in.dateDisplay(postConsultation[position].ClientPreferredConsultationDate));
                    }catch (Exception e){}

                    if(postConsultation[position].State == PostConsultation.ServiceState.InProgress){
                        tv_read.setText("обработка");
                        tv_read.setTextColor(getResources().getColor(R.color.read2));
                    }else if(postConsultation[position].State == PostConsultation.ServiceState.Completed) {
                        tv_read.setText("выполнен");
                        tv_read.setTextColor(getResources().getColor(R.color.read0));
                    }else if(postConsultation[position].State == PostConsultation.ServiceState.WaitingForPayment) {
                        tv_read.setText(String.valueOf(postConsultation[position].Order.PaymentAmount)+" \u20BD");
                        tv_read.setTextColor(getResources().getColor(R.color.read1));
                    }else {
                        tv_read.setText("принят");
                        tv_read.setTextColor(getResources().getColor(R.color.read3));
                    }
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

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();

            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            String s1 = in.get_token_type()+" "+in.get_token();
            String s2 = "http://"+in.get_url()+"/Consultations/GetConsultations";
            //RequestBody formBody = RequestBody.create(JSON, json_signup);

            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://"+in.get_url()+"/Consultations/GetMyConsultations")
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

            try{
                Gson gson = new Gson();
                mcArrayConsultation = gson.fromJson(result, PostConsultation[].class);
                start_activity();
            }catch (Exception e){}


            super.onPostExecute(result);
        }
    }
}