package kondratkov.advocatesandnotariesrf;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;

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

import kondratkov.advocatesandnotariesrf.my_info.My_frag_orders_cons;
import kondratkov.advocatesandnotariesrf.my_info.My_perep_orders_mess;

public class Pay_br_yandex extends Activity {

    WebView mWebView;
    IN in;
    public List<JSONObject> listJSON  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_br_yandex);

        in = new IN();
        mWebView = (WebView) findViewById(R.id.pay_webView);
        // включаем поддержку JavaScript
        findViewById(R.id.pay_web_but_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pay_br_yandex.this.finish();
            }
        });

        //new UrlConnectionTask().execute();
        //new AsyncTaskMess().execute();
        //start_activity("http://vsundupey.vds.serverdale.com/Orders/PayOrder/");
        //start_activity("http://195.128.124.172/Orders/PayOrder/");
        //start_activity("http://app.mmka.info/Orders/PayOrder/");

        Uri address = Uri.parse("http://app.mmka.info/Orders/PayOrder/"+String.valueOf(in.get_idc()));
        Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlinkIntent);
    }

    public void start_activity(String url){

        int ii = in.get_idc();
        String s = url+String.valueOf(in.get_idc());

        //data = Uri.parse("http://app.mmka.info/Orders/PayOrder/"+String.valueOf(in.get_idc()));
        mWebView.getSettings().setJavaScriptEnabled(true);
        //mWebView.loadUrl(data.toString());


        mWebView.setWebViewClient(new MyWebViewClient());
        //mWebView.loadUrl("http://app.mmka.info/api/JuristSpecializationSectors/GetJuristSpecializationSectors");


        Pay_br_yandex.this.finish();
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(Uri.parse(url).getHost().length() == 0) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://app.mmka.info/Orders/PayOrder/"+String.valueOf(in.get_idc())));
            view.getContext().startActivity(intent);
            return true;
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
                    .url("http://"+in.get_url()+"/Orders/PayOrder/"+in.get_idc())
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

            super.onPostExecute(result);
        }
    }
}
