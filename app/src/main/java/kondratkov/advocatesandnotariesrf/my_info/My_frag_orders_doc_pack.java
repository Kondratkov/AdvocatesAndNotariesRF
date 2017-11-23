package kondratkov.advocatesandnotariesrf.my_info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.List;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.Pay_br_yandex;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.api_classes.DocumentOrder;
import kondratkov.advocatesandnotariesrf.api_classes.DocumentPacketOrder;
import kondratkov.advocatesandnotariesrf.data_theme.AllQuestions_forum;
import kondratkov.advocatesandnotariesrf.data_theme.theme_data_forum;
import kondratkov.advocatesandnotariesrf.maps.Map_coor;
import kondratkov.advocatesandnotariesrf.notary.Notary_profile;

public class My_frag_orders_doc_pack extends Fragment {

    public ListView lv_f = null;
    public Spinner spinner_f = null;
    public IN in;
    public int sort = 0;

    public String date = "-1";
    public Context context_view;

    public DocumentPacketOrder[] msArraydocumentOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_orders_doc_pack_list, container, false);

        in = new IN();
        lv_f = (ListView)view.findViewById(R.id.my_frag_ord_doc_pack);
        //onSpinnerS();

        return view;
    }

    public void onStart(){
        super.onStart();
        new UrlConnectionTask().execute();
    }

    public void start_activity(){
        Gson gson = new Gson();
        if(in.get_msArraydocumentOrders().length()!=0){
            msArraydocumentOrders = gson.fromJson(in.get_msArraydocumentOrders(), DocumentPacketOrder[].class);
        }

        MyAdapterList mam = new MyAdapterList(in.get_activity(), msArraydocumentOrders);
        lv_f.setAdapter(mam);

        lv_f.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
               // in.set_place(1);
                //in.set_idt(msArraydocumentOrders[position].Order.Id);
               // in.set_idc(msArraydocumentOrders[position].Order.Id);
                //Intent intent1 = new Intent(in.get_context(), Pay_br_yandex.class);
               // startActivity(intent1);
                //Intent int3 = new Intent(in.get_context(), My_perep_orders_pack.class);
                //startActivity(int3);
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
    class MyAdapterList extends ArrayAdapter {

        private DocumentPacketOrder[] documentOrder = null;
        private Activity context;

        public String[] arrayTheme = getResources().getStringArray(R.array.ArraySpecialization);

        public MyAdapterList(Activity context, DocumentPacketOrder []documentOrder1) {
            super(context, R.layout.my_orders_item_doc_pack,  documentOrder1);
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

            tv_tip.setText("Кейс документов");
            try{
                tv_them.setText(documentOrder[position].Header);
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
            }catch (Exception e){}


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
                    .url("http://"+in.get_url()+"/DocumentPacketOrders/GetMyDocumentPacketOrders")
                    .build();

            String s = in.get_token_type()+" "+in.get_token();

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                result = response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
Log.d("qwerty", "JJJJJLJLJLJLJLJLJL");
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result!=null) {
                Gson gson = new Gson();
                msArraydocumentOrders = gson.fromJson(result, DocumentPacketOrder[].class);
                try{
                    if(msArraydocumentOrders[0].Description!=null){
                        in.set_msArraydocumentOrders(result);
                    }
                }catch (Exception e){
                }

                start_activity();
            }
            super.onPostExecute(result);
        }
    }
}
