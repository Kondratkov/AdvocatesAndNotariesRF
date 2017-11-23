package kondratkov.advocatesandnotariesrf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class Forum extends Activity implements View.OnTouchListener{

    public Spinner spinner_sorting;
    private static final String DEBUG_TAG = "qwerty";

    String json_quest =
            "{\"mymess\":{\"array\":[" +
                    "{\"idt\":\"13\",\"reply\":\"1\",\"theme\":\"3\",\"text\":\" вот и ладно\",\"date\":\"1432452584532\"}," +
                    "{\"idt\":\"12\",\"reply\":\"0\",\"theme\":\"1\",\"text\":\"Отили нет это нужно или не нужно\",\"date\":\"1451452684532\"}," +
                    "{\"idt\":\"14\",\"reply\":\"2\",\"theme\":\"3\",\"text\":\"Отвт выадлжовыа лоаывлдо ывллывсоалвоал о ооапн пн неангаа нгенгеа нгеанге ае аап апроаг аеап аоаге агеа е ае аеа ное анаеаоаноеане ае аопеапоаоеаео ае аноеаоеапоаро аоне аноеа аапеа е ае аеае ае ае ае аеаоаоеаоа о а\",\"date\":\"1451552784532\"}," +
                    "{\"idt\":\"15\",\"reply\":\"2\",\"theme\":\"1\",\"text\":\"Ну ладно, а что если?\",\"date\":\"1423456884532\"}," +
                    "{\"idt\":\"16\",\"reply\":\"0\",\"theme\":\"3\",\"text\":\"выафыв Ну ладно, а что если?\",\"date\":\"1465468684532\"}," +
                    "{\"id\":\"17\",\"reply\":\"4\",\"theme\":\"1\",\"text\":\"выаНу ладно, а что fdfd fdfdf df df df df df если?\",\"date\":\"1423468884532\"}," +
                    "{\"idt\":\"18\",\"reply\":\"6\",\"theme\":\"1\",\"text\":\"Нртмиявпу ладно, а что если?\",\"date\":\"1467846884532\"}," +
                    "{\"idt\":\"19\",\"reply\":\"5\",\"theme\":\"5\",\"text\":\"Нуукчспвап ладно, а что если?\",\"date\":\"1452446884532\"}," +
                    "{\"idt\":\"20\",\"reply\":\"6\",\"theme\":\"1\",\"text\":\"Нуукаич ладно, а что если?\",\"date\":\"1478946884532\"}," +
                    "{\"idt\":\"21\",\"reply\":\"7\",\"theme\":\"1\",\"text\":\"Нувапп ладно, а что если?\",\"date\":\"1439766884532\"}," +
                    "{\"idt\":\"22\",\"reply\":\"1\",\"theme\":\"0\",\"text\":\"Нчыапу ладно, а что если?\",\"date\":\"1562446884532\"}," +
                    "{\"idt\":\"23\",\"reply\":\"0\",\"theme\":\"1\",\"text\":\"Ныпу имладно, а что если?\",\"date\":\"1879746884532\"}," +
                    "{\"idt\":\"24\",\"reply\":\"0\",\"theme\":\"1\",\"text\":\"Нываппу ладно, а что если?\",\"date\":\"1346446884532\"}," +
                    "{\"idt\":\"25\",\"reply\":\"1\",\"theme\":\"2\",\"text\":\"Нывпу ладно, а что если?\",\"date\":\"1242446884532\"}," +
                    "{\"idt\":\"26\",\"reply\":\"2\",\"theme\":\"1\",\"text\":\"Да нет, что вы.  давоыда олдыоа ывадло оыва лдодоыва шо\",\"date\":\"1434552984532\"}]}}";

    public ListView lv_forum;
    public JSONArray jsonArray_forum = null;
    public JSONObject jsonObject_forum = null;
    public List<JSONObject>list_forum  = null;
    MyAdapterJsonList mam = null;

    public IN in = new IN();
    private long docTip =1;

    public int sort = 0 ;
    public int sort_spin = 0;

    public String json_str = "";

    public FrameLayout asked_frameProg;
    public ProgressBar asked_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum);

        spinner_sorting = (Spinner)findViewById(R.id.forum_spinner_sort);



        //onSpinnerS();

        try {
            //json_jur = getString(R.string.mess_json);
            jsonObject_forum = new JSONObject(json_quest);

        } catch (JSONException e) {
            // TODO Auto-generatedcatchblock
            e.printStackTrace();
        }

        //list_forum = getQuestList(jsonObject_forum);
        //MyAdapterJsonList mam = new MyAdapterJsonList(this, list_forum);//getJurList(jsonObjectJurList));
        //lv_forum.setAdapter(mam);

        //lv_forum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //@Override
        //public void onItemClick(AdapterView<?> parent, View v, int position,
        //  long id) {


        //Forum.this.finish();
        // }
        //});
    }

    @Override
    protected void onStart() {
        super.onStart();

        in.set_place(1);
        in.setChoice_of_menus(11);
        JSONObject json_st = new JSONObject();
        try {
            json_st.put("idu", in.get_id_user());//String.valueOf(in.get_id()));
            json_st.put("idj", in.get_id_jur());
            json_st.put("password", in.get_password());
            json_st.put("tip_who", "0");//String.valueOf(adin.get_mess_id()));
            json_st.put("place", in.get_place());
            json_st.put("self",0);
            json_st.put("date",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json_str = String.valueOf(json_st);
        lv_forum = (ListView) findViewById(R.id.lv_forum);
        lv_forum.setOnTouchListener(this);

        asked_frameProg = (FrameLayout)findViewById(R.id.forum_frameLayout);
        asked_progressBar = (ProgressBar)findViewById(R.id.forum_progressBar);

        asked_frameProg.setBackgroundResource(R.color.frameOn);
        asked_frameProg.setClickable(true);
        asked_progressBar.setVisibility(ProgressBar.VISIBLE);

        new AsyncTaskMess().execute();
    }

    public void start_activity(String sf) {
        json_quest = sf;

        try {
            jsonObject_forum = new JSONObject(sf);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        list_forum = getQuestList(jsonObject_forum);
        mam = new MyAdapterJsonList(Forum.this, list_forum);//getJurList(jsonObjectJurList));
        lv_forum.setAdapter(mam);
        lv_forum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                try {
                    JSONObject jsonObject = new JSONObject(json_quest);
                    JSONObject j = new JSONObject(String.valueOf(jsonObject.getJSONObject("mymess")));
                    in.set_idt(j.getJSONArray("array").getJSONObject(position).getInt("idt"));
                    //in.set_id_user(j.getJSONArray("array").getJSONObject(position).getString("idusr"));
                    in.set_id_jur(j.getJSONArray("array").getJSONObject(position).getInt("idj"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent inte = new Intent(Forum.this, AskAQuestion.class);
                //inte.putExtra("id", in.get_idt());
                startActivity(inte);
            }
        });
        stopProgressBar();
    }


    public void onSpinnerS() {

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forum_but_cancel:
                Forum.this.finish();
                break;
            case  R.id.forum_but_menu:
                Intent intent134 = new Intent(Forum.this, Sidebar.class);//DocCreation.class);
                startActivity(intent134);
                break;
            case R.id.forum_but_add_mess:
                in.set_add_quest_tip(1);
                Intent intentAdd = new Intent(Forum.this, AddQuestion.class);//DocCreation.class);
                startActivity(intentAdd);
                break;
        }
    }

    public void stopProgressBar(){
        asked_frameProg.setBackgroundResource(R.color.frameOff);
        asked_frameProg.setClickable(false);
        asked_progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    public List<JSONObject> getQuestList(JSONObject jsonObjectList) {

        List listMyMess = new ArrayList<>();
        List listMyMessN = new ArrayList<>();
        List listMyMessSort = new ArrayList<>();

        try {
            //json_jur = getString(R.string.mess_json);
            JSONObject results = jsonObjectList.getJSONObject("mymess");

            jsonArray_forum = results.getJSONArray("array");

            for (int i = 0; i < jsonArray_forum.length(); i++) {
                JSONObject arrayElement = jsonArray_forum.getJSONObject(i);
                listMyMess.add(arrayElement);
            }

        } catch (JSONException e) {
            // TODO Auto-generatedcatchblock
            e.printStackTrace();
        }
        lv_forum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {

                Intent inte = new Intent(Forum.this, AskAQuestion.class);
                startActivity(inte);
                Forum.this.finish();
            }
        });

        return listMyMess;
    }
    class MyAdapterJsonList extends ArrayAdapter {

        private List<JSONObject> list = null;
        private Activity context;

        public String[] arrayTheme = getResources().getStringArray(R.array.ArrayTipAddQuest);

        public MyAdapterJsonList(Activity context, List<JSONObject> list) {
            super(context, R.layout.forum_item, list);
            this.context = context;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.forum_item, parent, false);

            TextView tv_forum_theme = (TextView) rowView.findViewById(R.id.forum_item_tv_theme);
            tv_forum_theme.setTextSize(in.get_font_2());
            TextView tv_forum_quest = (TextView) rowView.findViewById(R.id.forum_item_tv_quest);
            tv_forum_quest.setTextSize(in.get_font_1());
            TextView tv_forum_kol_otv = (TextView) rowView.findViewById(R.id.forum_item_tv_kol_otv);
            TextView tv_forum_quest_date = (TextView) rowView.findViewById(R.id.forum_item_tv_quest_date);
            tv_forum_quest_date.setTextSize(in.get_font_3());
            TextView tv_forum_quest_nik = (TextView) rowView.findViewById(R.id.forum_item_tv_quest_nik);
            tv_forum_quest_nik.setTextSize(in.get_font_3());

            if(position%2 == 0){
                rowView.setBackgroundColor(Color.argb(45,255,255,255));
            }else {
                rowView.setBackgroundColor(Color.argb(2,253,190,0));
            }


            try {
                String s = arrayTheme[Integer.parseInt(list.get(position).getString("theme"))];
                tv_forum_theme.setText(s);
                tv_forum_quest.setText(list.get(position).getString("text"));

                //CharSequence dateJSON = DateFormat.format("dd.MM.yyyy", Long.decode(list.get(position).getString("date")));
                tv_forum_quest_date.setText(String.valueOf(list.get(position).getString("date").substring(0, 5) + " " + list.get(position).getString("date").substring(11, 16)));//dateJSON));

                tv_forum_kol_otv.setText(list.get(position).getString("count"));
                if(Integer.parseInt(list.get(position).getString("count"))==0)
                {
                    tv_forum_kol_otv.setBackgroundResource(R.color.count2);
                }
                else{
                    tv_forum_kol_otv.setBackgroundResource(R.color.count1);
                }
                tv_forum_quest_nik.setText(list.get(position).getString("name"));

            } catch (JSONException e) {
                // TODO Auto-generatedcatchblock
                e.printStackTrace();
            }
            return rowView;
        }
    }

    class AsyncTaskMess extends AsyncTask<Void, Integer, String> {
        String url = "";
        @Override
        protected String doInBackground(Void... params) {
            url = "http://"+in.get_url()+"/123.gettheme";//URL_START;
            Log.d(DEBUG_TAG, "ЗАПРОС НА ПОЛУЧЕНИЕ ФОРУМА " + json_str);
            return ServerSendData.sendRegData(url, json_str);
        }

        protected void onPostExecute(String result) {
            if(result != null){
                start_activity(result);
            }
            else {
                asked_progressBar.setVisibility(ProgressBar.INVISIBLE);
                Toast.makeText(Forum.this,
                        "Нет связи с сервером!",
                        Toast.LENGTH_LONG).show();
            }
            Log.d(DEBUG_TAG, "ОТВЕТ НА ДАННЫЕ ФОРУМА " + json_str);

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
                //readStream(in);
                //finally {
                //    httpConnection.disconnect();
                // }

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


    public float xX = 0;
    public float xNew = 0;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_UP):
                if(xNew > 300){
                    Intent intent = new Intent(Forum.this, Sidebar.class);
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
