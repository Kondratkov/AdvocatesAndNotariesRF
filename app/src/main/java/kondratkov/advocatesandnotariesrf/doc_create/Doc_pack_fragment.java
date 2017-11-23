package kondratkov.advocatesandnotariesrf.doc_create;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import kondratkov.advocatesandnotariesrf.my_info.My_orders;

public class Doc_pack_fragment extends Fragment {
    public static final String DOC_THEM = "doc_them";
    public static final String DOC_PAY = "doc_pay";
    public static final String DOC_LIST = "doc_list";
    public static final String ID_PACK = "id_pack";

    public IN in;
    public int code;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doc_pack_fragment, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String them = arguments.getString(DOC_THEM);
            String pay = arguments.getString(DOC_PAY);
            String doc_list = arguments.getString(DOC_LIST);
            String id_pack = arguments.getString(ID_PACK);

            displayValues(view, them, pay, doc_list, id_pack);
        }
        return view;
    }

    private void displayValues(View v, String them, String pay,
                               String doc_list, final String id_pack) {
        TextView tv_them = (TextView) v
                .findViewById(R.id.doc_pack_fr_text);
        TextView tv_pay = (TextView) v
                .findViewById(R.id.doc_pack_fr_sum);
        TextView tv_doc = (TextView) v
                .findViewById(R.id.doc_pack_fr_dok_list);

        in = new IN();
        v.findViewById(R.id.doc_pack_fr_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in.set_id_doc_tip(Integer.parseInt(id_pack));
                new UrlConnectionTask().execute();
                //new AsyncTaskExample().execute();
            }
        });

        tv_them.setText(them);
        tv_pay.setText(pay+" \u20BD");
        tv_doc.setText(doc_list);

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
                    .url("http://"+in.get_url()+"/DocumentPacketOrders/CreateDocumentPacketOrder/"+in.get_id_doc_tip())
                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, ""))
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
                startActivity(new Intent(in.get_activity(), My_orders.class));
                in.get_activity().finish();

            }

            super.onPostExecute(result);
        }
    }


}
