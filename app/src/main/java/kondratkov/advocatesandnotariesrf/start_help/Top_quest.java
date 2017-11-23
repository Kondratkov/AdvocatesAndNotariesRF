package kondratkov.advocatesandnotariesrf.start_help;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.New_sidebar;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.Settings_no_login;
import kondratkov.advocatesandnotariesrf.api_classes.TopQuestion;

public class Top_quest extends Activity {

    String LOG_TAG = "qwerty_top";
    String json_top_quest = "";

    public ListView lv_top;

    public JSONArray jsonObject_top = null;
    public TopQuestion[] list_top = null;
    MyAdapterJsonList mam = null;

    public IN in = new IN();
    private long docTip = 1;

    public int sort = 0;
    public int sort_spin = 0;

    public String json_str = "";
    //  public  Type listType;

    public FrameLayout asked_frameProg;
    public ProgressBar asked_progressBar;

    TopQuestion[] mcArray;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_quest);

        lv_top = (ListView) findViewById(R.id.top_listView);
        //lv_top.setOnTouchListener(this);
        new UrlConnectionTask().execute();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.top_but_cancel:
                Top_quest.this.finish();
                break;
            case R.id.top_but_menu:
                if (in.getOnemay()) {
                    intent = new Intent(Top_quest.this, New_sidebar.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(Top_quest.this, Settings_no_login.class);
                    startActivity(intent);
                }

        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Top_quest Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class MyAdapterJsonList extends ArrayAdapter {

        private TopQuestion[] list = null;
        private Activity context;

        public String[] arrayTheme = getResources().getStringArray(R.array.ArrayTipAddQuest);

        public MyAdapterJsonList(Activity context, TopQuestion[] list) {
            super(context, R.layout.top_item, list);
            this.context = context;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.top_item, parent, false);

            TextView tv_top_theme = (TextView) rowView.findViewById(R.id.top_item_tv_theme);
            tv_top_theme.setTextSize(in.get_font_2());
            TextView tv_top_quest = (TextView) rowView.findViewById(R.id.top_item_tv_quest);
            tv_top_quest.setTextSize(in.get_font_1());

            if (position % 2 == 0) {
                rowView.setBackgroundColor(Color.argb(15, 255, 255, 255));
            } else {
                rowView.setBackgroundColor(Color.argb(2, 253, 190, 0));
            }
            tv_top_theme.setText(list[position].Question);
            tv_top_quest.setText(list[position].QuestionBody);


            return rowView;
        }
    }

    public void start_s_server() {

        MyAdapterJsonList mam = new MyAdapterJsonList(this, mcArray);//getnotaryList(jsonObjectnotaryList));
        lv_top.setAdapter(mam);

        lv_top.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {

                in.set_idt(mcArray[position].Id);
                in.setTopQuestion(mcArray[position]);

                String s = in.getTopQuestion().AnswerBody;
                int i =3;

                Intent int3 = new Intent(Top_quest.this, Top_quest_post.class);
                startActivity(int3);
            }
        });


        lv_top.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int position;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                position = firstVisibleItem;
            }
        });

    }


    class UrlConnectionTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://"+in.get_url()+"/TopQuestions/GetTopQuestions")
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
            Gson gg = new Gson();
            mcArray = gg.fromJson(result, TopQuestion[].class);
            start_s_server();
            super.onPostExecute(result);
        }
    }
}

