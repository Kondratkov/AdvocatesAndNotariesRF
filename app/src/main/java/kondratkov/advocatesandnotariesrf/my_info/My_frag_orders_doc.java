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
import kondratkov.advocatesandnotariesrf.api_classes.DocumentOrder;

public class My_frag_orders_doc extends Fragment {

    public ListView lv_f = null;
    public Spinner spinner_f = null;
    public IN in;
    public int sort = 0;

    public String date = "-1";
    public Context context_view;

    public DocumentOrder[] msArraydocumentOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_orders_doc_list, container, false);

        in = new IN();

        lv_f = (ListView)view.findViewById(R.id.my_frag_ord_doc);
        onSpinnerS();
        return view;
    }

    public void onStart(){
        super.onStart();

        new UrlConnectionTask().execute();
        //new AsyncTaskMess().execute();
    }

    public void start_activity(){

        MyAdapterList mam = new MyAdapterList(in.get_activity(), msArraydocumentOrders);
        lv_f.setAdapter(mam);

        lv_f.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                in.set_place(1);
                in.set_idt(msArraydocumentOrders[position].Id);
                Intent int3 = new Intent(in.get_context(), My_perep_orders_mess.class);
                startActivity(int3);
            }
        });
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
                       // lv_f.setAdapter(mam);
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

    class MyAdapterList extends ArrayAdapter {

        private DocumentOrder[] documentOrder = null;
        private Activity context;
        public String[] arrayTheme;

        public MyAdapterList(Activity context, DocumentOrder []documentOrder1) {
            super(context, R.layout.my_orders_item_doc,  documentOrder1);
            this.context = context;
            this.documentOrder = documentOrder1;
        }

        public void start_profile_not(int i){
            //Intent intent = new Intent(Notary_list.this, Notary_profile.class);
            //startActivity(intent);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.my_orders_item_doc, parent, false);

            TextView tv_them = (TextView) rowView.findViewById(R.id.my_order_doc_tv_them);
            tv_them.setTextSize(in.get_font_2());
            TextView tv_text = (TextView) rowView.findViewById(R.id.my_order_doc_tv_text);
            tv_text.setTextSize(in.get_font_1());
            TextView tv_date = (TextView) rowView.findViewById(R.id.my_order_doc_tv_date);
            tv_date.setTextSize(in.get_font_3());
            TextView tv_tip  = (TextView) rowView.findViewById(R.id.my_order_doc_tv_nik);
            tv_tip.setTextSize(in.get_font_2());
            TextView tv_read = (TextView) rowView.findViewById(R.id.my_order_doc_tv_read);
            tv_read.setTextSize(in.get_font_2());

            tv_tip.setText(documentOrder[position].GetDocumentTyp());
            tv_them.setText(documentOrder[position].JuristSpecialization.SectorName);
            tv_text.setText(documentOrder[position].Description);
                //tv_date.setText(list.get(position).getString("dt"));

            switch (documentOrder[position].GetState()){
                case 0:
                    tv_read.setText("Согласование");
                    tv_read.setTextColor(getResources().getColor(R.color.read2));
                    break;
                case 1:
                    tv_read.setText(String.valueOf(documentOrder[position].Order.PaymentAmount)+" \u20BD");
                    tv_read.setTextColor(getResources().getColor(R.color.read1));
                    break;
                case 2:
                    tv_read.setText("Исполнение заказа");
                    tv_read.setTextColor(getResources().getColor(R.color.read1));
                     break;
                case 3:
                    tv_read.setText("Ваш заказ готов");
                    tv_read.setTextColor(getResources().getColor(R.color.read0));
                    break;
            }
                /*if(list.get(position).getInt("pay")==1){
                    tv_read.setText(list.get(position).getString("paysum")+" \u20BD");
                    tv_read.setTextColor(getResources().getColor(R.color.read1));
                }else if(list.get(position).getInt("pay")==2) {
                    tv_read.setText("оплачено");
                    tv_read.setTextColor(getResources().getColor(R.color.read0));
                }else
                    tv_read.setText("не подтвержден");
                tv_read.setTextColor(getResources().getColor(R.color.read2));*/


            /*String s = arrayTheme[list.get(position).get_theme()];
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


            Button but_item_notary_adres = (Button)rowView.findViewById(R.id.but_item_notary_adres);
            but_item_notary_adres.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lila_item_notary_adres = (LinearLayout) rowView.findViewById(R.id.lila_item_notary_adres);
                    if (bool) {
                        Log.d(LOG_TAG, "position " + position + " " + bool);
                        lila_item_notary_adres.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 0));
                        bool = false;
                    } else {
                        Log.d(LOG_TAG, "position " + position + " " + bool);
                        lila_item_notary_adres.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(2), (float) 0));
                        bool = true;
                    }
                    //lila_item_notary_adres = (LinearLayout)rowView.findViewById(R.id.lila_item_notary_adres);
                    //lila_item_notary_adres.setBackgroundResource(R.color.black_m);

                    //TextView r = (TextView)rowView.findViewById(R.id.tv_item_notary_adres_text);
                    //r.setText("d");
                    //r.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 0));
                    //lila_item_notary_adres.setLayoutParams(
                    // new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(165), (float) 0));
                }
            });
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

            //RequestBody formBody = RequestBody.create(JSON, json_signup);

            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    //.url("http://"+in.get_url()+"/DocumentOrders/GetNewDocumentOrders")
                    .url("http://"+in.get_url()+"/DocumentOrders/GetMyDocumentOrders")
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
            msArraydocumentOrders = gson.fromJson(result, DocumentOrder[].class);
            start_activity();
            super.onPostExecute(result);
        }
    }




    /*
    class AsyncTaskMess extends AsyncTask<Void, Integer, String> {

        String url = "";
        String json_str = "";

        @Override
        protected String doInBackground(Void... params) {

            url = "http://"+in.get_url()+"/123.getthemedoc";//URL_START;
            JSONObject json_st = new JSONObject();
            try {
                json_st.put("idu", in.get_id_user());//String.valueOf(in.get_id()));
                json_st.put("password", in.get_password());
                json_st.put("tip_who", 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json_str = String.valueOf(json_st);
            Log.d("qwerty", "results TO CHTO ?TOLLL DOK "+ json_str);
            return ServerSendData.sendRegData(url, json_str);
        }

        protected void onPostExecute(String result) {
            Log.d("qwerty", "results TO CHTO NADO DOK "+ result);
            if(result!=null){
                start_activity(result);
            }
            else{}
            //
        }
    }
    public static class ServerSendData {
        //public static IN iny = new IN();
        public static String sendRegData(String urls, String json) {

            String result =  null;
            try {
                java.net.URL url = new URL(urls);//new AdIn().getURL());//

                URLConnection connection = url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setDoOutput(true);
                httpConnection.setChunkedStreamingMode(0);

                OutputStream out = new BufferedOutputStream(httpConnection.getOutputStream());
                // writeStream(out);
                out.write(json.getBytes());

                out.flush();
                out.close();

                InputStream in = new BufferedInputStream(httpConnection.getInputStream());
                int responseCode = 0;
                responseCode = httpConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream is = httpConnection.getInputStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    result = r.readLine();

                } else {
                }
            } catch (MalformedURLException e) {
            } catch (IOException e1) {
            }
            return result;
        }
    }
*/

}
