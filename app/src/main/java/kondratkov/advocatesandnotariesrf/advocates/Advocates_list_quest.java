package kondratkov.advocatesandnotariesrf.advocates;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kondratkov.advocatesandnotariesrf.AddQuestion;
import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.Sidebar;
import kondratkov.advocatesandnotariesrf.start_help.Start_activity_no_login;

public class Advocates_list_quest extends Activity {

    private static final String DEBUG_TAG = "qwerty";
    boolean bool_jurl_list = false;
    Point point;
    int view_height;

    String json_jur =
            "{\"jurList\":{\"array\":[" +
                    "{\"surname\":\"Иванов\",\"name\":\"Иванqqq\",\"icon\":\""+String.valueOf(R.drawable.ricfas)+"\",\"idjur\":\"1\",\"online\":\"1\",\"city\":\"Воронеж\"}," +
                    "{\"surname\":\"Петров\",\"name\":\"Семенqq\",\"icon\":\""+String.valueOf(R.drawable.ricfas)+"\",\"idjur\":\"2\",\"online\":\"1\",\"city\":\"Москва\" }, " +
                    "{\"surname\":\"Гродев\",\"name\":\"Петрqqq\",\"icon\":\""+String.valueOf(R.drawable.ricfas)+"\",\"idjur\":\"4\",\"online\":\"1\",\"city\":\"Калиниград\" }]}}";

    public ListView listViewJur;
    public JSONArray jsonArrayjurList = null;
    public JSONObject jsonObjectjurList = null;
    public List<JSONObject> listJurJSON  = null;

    public LinearLayout lila_sort_panel, jurList_lila_city;
    public TextView jurList_tv_sort_city;
    boolean b1 = true, b2= true;
    public View lm[] = new View[3];//-ff
    public Spinner spinner_jur_sort= null;
    public String sort_jur = "";
    public String [] m_sort_jur= null;
    public String sort_jurs = "выбрать регион";
    public String [] m_sort_jurs= null;
    public Button jurList_but_sity;
    public IN in = new IN();
    public int int_sort = 0;
    public int sort_list = 0;

    public String SORT_CITY = "";

    public String json_query_jurlist = "";
    public String URL_GET_JURLIST ;

    public FrameLayout asked_frameProg;
    public ProgressBar asked_progressBar;

    public Map<CheckBox, Integer> map_idjur;
    CheckBox jurList_checkBoxJurOnLine;

    public boolean[] bool_sort = new boolean[]{false, true, true, true, true, true, true, true, true, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advocates_list_quest);

        listViewJur = (ListView)findViewById(R.id.jurList_quest_listView);
        LinearLayout l = new LinearLayout(this);
        TextView t1 = new TextView(this);

        l.setPadding(0, dpToPx(86), 0, 0);
        t1.setText("");
        t1.setTextSize(1);
        t1.setWidth(1000);//dpToPx(100));
        t1.setPadding(15, 0, 0, 0);

        l.addView(t1);
        l.setBackgroundResource(R.drawable.transpar);
        listViewJur.addHeaderView(l);
        in = new IN();
        jurList_checkBoxJurOnLine = (CheckBox)findViewById(R.id.jurList_quest_checkBoxJurOnLine);
        jurList_checkBoxJurOnLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                query_reg();
            }
        });
        jurList_tv_sort_city = (TextView)findViewById(R.id.jurList_quest_tv_sort_city);
        map_idjur = new HashMap<CheckBox, Integer>();
    }

    @Override
    protected void onStart() {
        super.onStart();



            if(in.getOnemay()){

            }else{
                Intent intent = new Intent(Advocates_list_quest.this, Start_activity_no_login.class);
                startActivity(intent);
                Advocates_list_quest.this.finish();
            }
            //OneStart();


        in.setChoice_of_menus(4);

        asked_frameProg = (FrameLayout)findViewById(R.id.adv_quest_list_frameLayout);
        asked_progressBar = (ProgressBar)findViewById(R.id.adv_quest_list_progressBar);

        asked_frameProg.setBackgroundResource(R.color.frameOn);
        asked_frameProg.setClickable(true);
        asked_progressBar.setVisibility(ProgressBar.VISIBLE);

        query_reg();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];
        Button button = (Button) findViewById(R.id.jurList_quest_but_jur_filter);

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        button.getLocationOnScreen(location);
        view_height = button.getHeight();

        // Initialize the Point with x, and y positions
        point = new Point();
        point.x = location[0];
        point.y = location[1];
    }

    public void start_activity(String s){
        json_jur = s;
        Log.d("qwerty", "!!!!@@ -- " + s);
//dlya sortirovki

        lila_sort_panel =(LinearLayout)findViewById(R.id.jurList_quest_lila_sort_panel);
        jurList_lila_city =(LinearLayout)findViewById(R.id.jurList_quest_lila_city);

        try {
            //json_jur = getString(R.string.mess_json);
            jsonObjectjurList = new JSONObject(json_jur);

        } catch (JSONException e) {
            // TODO Auto-generatedcatchblock
            e.printStackTrace();
        }

        listJurJSON = getjurList(jsonObjectjurList);
        MyAdapterJsonList mam = new MyAdapterJsonList(this, listJurJSON);//getjurList(jsonObjectjurList));
        listViewJur.setAdapter(mam);

        listViewJur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                try {
                    in.set_id_jur(Integer.parseInt(listJurJSON.get(position - 1).getString("idjur")));
                    in.set_add_quest_tip(3);
                    in.set_fio_jur(listJurJSON.get(position - 1).getString("surname") + " " + listJurJSON.get(position - 1).getString("name").charAt(0) + ". ");// + listJurJSON.get(position - 1).getString("patronymic").charAt(0) + ". ");
                    Intent intent = new Intent(Advocates_list_quest.this, AddQuestion.class);
                    startActivity(intent);
                    Advocates_list_quest.this.finish();

                    //in.set_jur_surname(listJurJSON.get(position - 1).getString("surname"));
                    //in.set_jur_name(listJurJSON.get(position - 1).getString("name"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        listViewJur.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int position;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (position < firstVisibleItem && b1 == true) {
                    Log.d("qwerty", "vnizvverh");

                    lila_sort_panel.setLayoutParams(
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, (float) 0.07));


                    bool_jurl_list = false;

                    Animation anim = AnimationUtils.loadAnimation(Advocates_list_quest.this, R.anim.trans_down_sort);
                    Animation anim2 = AnimationUtils.loadAnimation(Advocates_list_quest.this, R.anim.trans_up_sort2);

                    lila_sort_panel.setPadding(0, dpToPx(14), 0, 0);//lila_sort_panel.setPadding(0, dpToPx(64),0,0);
                    lila_sort_panel.startAnimation(anim);

                    jurList_lila_city.setPadding(0, 0, 0, dpToPx(85));
                    jurList_lila_city.startAnimation(anim2);
                    //FrameLayout jurList_frla = (FrameLayout)findViewById(R.id.jurList_frla);
                    //jurList_frla.startAnimation(anim2);
                    //jurList_lila_sity.startAnimation(anim2);
                    //jurList_lila_sity.setPadding(0, 0, 0, dpToPx(60));

                    b1 = false;
                    b2 = true;
                }
                if (position > firstVisibleItem && b2 == true) {

                    Animation anim = AnimationUtils.loadAnimation(Advocates_list_quest.this, R.anim.trans_up_sort);
                    Animation anim2 = AnimationUtils.loadAnimation(Advocates_list_quest.this, R.anim.trans_down_sort2);

                    lila_sort_panel.setPadding(0, dpToPx(54), 0, 0);
                    lila_sort_panel.startAnimation(anim);


                    jurList_lila_city.startAnimation(anim2);
                    jurList_lila_city.setPadding(0, 0, 0, 0);
                    //jurList_lila_sity.setPadding(0, 0, 0, 0);
                    //jurList_lila_sity.startAnimation(anim2);

                    b2 = false;
                    b1 = true;
                }
                position = firstVisibleItem;
            }
        });
        stopProgressBar();
    }
    //--------------------------------------------------------------filter
    private void openDialogFilter(final Activity context, Point p){

        /*LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.advocate_dialog_filter, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);



        popupWindow.showAsDropDown(lila_sort_panel, 50, -30);*/
        Button button = (Button)findViewById(R.id.jurList_but_1);
        int popupWidth = button.getWidth();
        //int popupHeight = 270;

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = layoutInflater.inflate(R.layout.advocate_dialog_filter, null);


        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(layout,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        //popup.setHeight(popupHeight);
        popup.setFocusable(true);
        popup.showAsDropDown(button);

        // Some offset to align the popup a bit to the right, and a bit down,
        // relative to button's position.
        int OFFSET_X = 0;
        int OFFSET_Y = view_height - 5;

        CheckBox filter_check_iz = (CheckBox)layout.findViewById(R.id.filter_check_iz);
        CheckBox filter_check_ud = (CheckBox)layout.findViewById(R.id.filter_check_ud);
        CheckBox filter_check_rating = (CheckBox)layout.findViewById(R.id.filter_check_rating);

        CheckBox filter_check_spc_1 = (CheckBox)layout.findViewById(R.id.filter_check_spc_1);
        CheckBox filter_check_spc_2 = (CheckBox)layout.findViewById(R.id.filter_check_spc_2);
        CheckBox filter_check_spc_3 = (CheckBox)layout.findViewById(R.id.filter_check_spc_3);
        CheckBox filter_check_spc_4 = (CheckBox)layout.findViewById(R.id.filter_check_spc_4);
        CheckBox filter_check_spc_5 = (CheckBox)layout.findViewById(R.id.filter_check_spc_5);
        CheckBox filter_check_spc_6 = (CheckBox)layout.findViewById(R.id.filter_check_spc_6);
        CheckBox filter_check_spc_7 = (CheckBox)layout.findViewById(R.id.filter_check_spc_7);
        CheckBox filter_check_spc_8 = (CheckBox)layout.findViewById(R.id.filter_check_spc_8);
        final CheckBox [] check_spc = new CheckBox[]{filter_check_iz , filter_check_spc_1, filter_check_spc_2, filter_check_spc_3, filter_check_spc_4,
                filter_check_spc_5, filter_check_spc_6, filter_check_spc_7, filter_check_spc_8, filter_check_ud, filter_check_rating};

        for(int i = 0; i<check_spc.length; i++){
            check_spc[i].setChecked(bool_sort[i]);
        }
        // Clear the default translucent background

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(button, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
        //popupWindow.showAsDropDown(butOpenPopup, 200, -15);

        // Getting a reference to Close button, and close the popup when
        // clicked.
        Button close = (Button) layout.findViewById(R.id.filter_but_close);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        Button add_filter = (Button) layout.findViewById(R.id.filter_but_start_sort);
        add_filter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for(int i = 0; i<bool_sort.length; i++){
                    bool_sort[i]= check_spc[i].isChecked();
                }
                query_reg();
                popup.dismiss();
            }
        });

    }
    //--------------------------------------------------------------region
    public int City_num = 0;
    public String Region_string ="";
    public String City_string = "";
    public String [] str_region = null;
    public int[] int_region = null;
    public String [] str_city = null;
    public String[] int_str_city = null;
    public Spinner spinner = null;
    private void openDialogRegion(){

        final Dialog dialog = new Dialog(Advocates_list_quest.this);
        dialog.setTitle("region");
        dialog.setContentView(R.layout.advocate_dialog_sort_city);

        final Spinner spinner_region = (Spinner)dialog.findViewById(R.id.dialog_sort_spinner_region);
        final Spinner spinner_city = (Spinner)dialog.findViewById(R.id.dialog_sort_spinner_city);

        Button btnDismiss = (Button) dialog.getWindow().findViewById(
                R.id.dialog_sort_button_close);
        Button btnmiss = (Button) dialog.getWindow().findViewById(
                R.id.dialog_sort_button_yes);


        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query_reg();
                dialog.dismiss();
            }
        });


        try {
            JSONObject jsonObject = new JSONObject(Region_string);
            JSONArray jsonArray = jsonObject.getJSONArray("array");
            str_region = new String[jsonArray.length()];
            int_region = new int[jsonArray.length()];
            for(int i = 0; i< jsonArray.length(); i++ ){
                str_region[i] = jsonArray.getJSONObject(i).getString("name");
                int_region[i] = jsonArray.getJSONObject(i).getInt("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("qwerty", "массив РЕГИОНОВ длина "+ str_region.length+" а что там на "+ str_region[1]);

        final ArrayAdapter<String> adapter_region = new ArrayAdapter<String>(this,
                R.layout.my_quest_item_spinner, str_region);
        //ArrayAdapter.createFromResource(this, R.layout.my_quest_item_spinner, new String(){""});
        spinner_region.setAdapter(adapter_region);
        spinner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                Log.d("qwerty", "Я чтото выбрал " + City_num);
                City_num = int_region[Integer.parseInt(String.valueOf(id))];
                spinner = spinner_city;
                new AsyncTaskDialog().execute();

            }
        });
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                SORT_CITY = int_str_city[pos];
                jurList_tv_sort_city.setText(str_city[pos]);
            }
        });

        dialog.show();
    }
    class AsyncTaskDialog extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... params) {
            String url_s = "";
            String json_s = "";
            if(City_num == 0){
                url_s = "http://"+in.get_url()+"/123.getreg";
            }else{
                url_s = "http://"+in.get_url()+"/123.getcity";
                JSONObject json = new JSONObject();
                try {
                    if(City_num<10){
                        json.put("code", "0"+City_num);
                    }else{
                        json.put("code", City_num);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                json_s = String.valueOf(json);
            }

            return ServerSendData.sendRegData(url_s, json_s);
        }

        protected void onPostExecute(String result) {
            Log.d("qwerty", "Ответ на запрос региона "+ result+ " citi_num = "+City_num);
            if(result != null){
                if(City_num==0){
                    Region_string = result;
                    City_num = 1;
                    openDialogRegion();
                }
                else {
                    City_string = result;
                    if(City_string.length()!=0){
                        try {
                            JSONObject jsonObject = new JSONObject(City_string);
                            JSONArray jsonArray = jsonObject.getJSONArray("array");
                            str_city = new String[jsonArray.length()];
                            int_str_city = new String[jsonArray.length()];
                            for(int i = 0; i< jsonArray.length(); i++ ){
                                str_city[i] = jsonArray.getJSONObject(i).getString("name");
                                int_str_city[i]=jsonArray.getJSONObject(i).getString("id");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("qwerty", "массив ГОРОДОВ длина "+ City_num);

                        ArrayAdapter<String> adapter_city = new ArrayAdapter<String>(Advocates_list_quest.this,
                                R.layout.my_quest_item_spinner, str_city);
                        //ArrayAdapter.createFromResource(this, R.layout.my_quest_item_spinner, new String(){""});
                        spinner.setAdapter(adapter_city);
                    }
                }

            }
            //progressBarRegionSign.setVisibility(ProgressBar.INVISIBLE);
            //progressBarCitySign.setVisibility(ProgressBar.INVISIBLE);
        }
    }
//___|||__|____|___|_______|____|_____|_______|____|__region

    public void id_nah(String s){

    }

    public int dpToPx(int dp){
        DisplayMetrics displayMetrics = Advocates_list_quest.this.getResources().getDisplayMetrics();
        int px =Math.round(dp *(displayMetrics.xdpi /DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jurList_quest_but_Cancel:
                Advocates_list_quest.this.finish();
                break;

            case R.id.jurList_quest_but_jur_filter:
                openDialogFilter(this, point);
                break;

            case R.id.jurList_quest_but_sort_city:
                City_num = 0;
                new AsyncTaskDialog().execute();
                break;

            /*case R.id.jurList_but_sity:

                if(bool_jurl_list == false){
                    Log.d("qwerty", "dfsdfsdf");
                    lila_sort_panel_down.setLayoutParams(
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, (float) 0.8));

                    lila_sort_panel.setLayoutParams(
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, (float) 0.2));

                    bool_jurl_list = true;
                }
                else{
                    for (int i = 0; i < 3; i++) {

                        //lila_sort_panel_down_tw.removeView(lm[i]);

                    }
                    lila_sort_panel_down.setLayoutParams(
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, (float) 0.93));
                    lila_sort_panel.setLayoutParams(
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, (float) 0.07));
                    Log.d("qwerty", "1111111");
                    but_jurList_sort.setText(sort_jurs);
                    bool_jurl_list = false;
                    //lila_jur_list_sort.setMinimumHeight(45);
                }
                break;*/
        }

    }

    public List<JSONObject> getjurList(JSONObject jsonObjectjurList) {

        List jurListRT = new ArrayList<>();
        try {

            //json_jur = getString(R.string.mess_json);
            JSONObject results = jsonObjectjurList.getJSONObject("jurList");

            jsonArrayjurList = results.getJSONArray("array");

            for (int i = 0; i <jsonArrayjurList.length(); i++) {
                JSONObject arrayElement = jsonArrayjurList.getJSONObject(i);
                jurListRT.add(arrayElement);
            }

        } catch (JSONException e) {
            // TODO Auto-generatedcatchblock
            e.printStackTrace();
        }
        return jurListRT;
    }

    public void stopProgressBar(){
        asked_frameProg.setBackgroundResource(R.color.frameOff);
        asked_frameProg.setClickable(false);
        asked_progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    class MyAdapterJsonList extends ArrayAdapter {

        private List<JSONObject>list= null;
        private Activity context;

        public MyAdapterJsonList(Activity context, List<JSONObject>list) {
            super(context, R.layout.advocates_item_list, list);
            this.context = context;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = null;

            try {
                rowView = inflater.inflate(R.layout.advocates_item_list, parent, false);
                TextView tvSurname   = (TextView) rowView.findViewById(R.id.tvSurnameJur);
                tvSurname.setTextSize(in.get_font_1());

                TextView tvRatingJur = (TextView) rowView.findViewById(R.id.tvJLcity);
                tvRatingJur.setTextSize(in.get_font_2());

                TextView tvOnLine    = (TextView) rowView.findViewById(R.id.tvOnLine);
                ImageView imageView  = (ImageView) rowView.findViewById(R.id.imageViewJur);
                LinearLayout lila_jur_item = (LinearLayout)rowView.findViewById(R.id.lila_jur_item);

                tvSurname.setText(list.get(position).getString("surname")+" "+list.get(position).getString("name")+".");
                tvRatingJur.setText(list.get(position).getString("city"));

                CheckBox jur_item_checkBox = (CheckBox)rowView.findViewById(R.id.jur_item_checkBox);
                if(list.get(position).getInt("liked")==0){
                    jur_item_checkBox.setChecked(false);
                }
                else{
                    jur_item_checkBox.setChecked(true);
                }

                map_idjur.put(jur_item_checkBox, list.get(position).getInt("idjur"));
                jur_item_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("idu", in.get_id_user());
                            jsonObj.put("password", in.get_password());
                            if(isChecked){
                                jsonObj.put("type", 1);
                            }else{
                                jsonObj.put("type", 0);
                            }
                            jsonObj.put("idj", map_idjur.get(buttonView));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        json_query_jurlist = String.valueOf(jsonObj);

                        new AsyncTaskLiked().execute();
                    }
                });
                //tvOnLine.setText(list.get(position).getString("online"));

                if(list.get(position).getBoolean("online")){
                    //tvOnLine.setBackgroundResource(R.color.on_line_true);
                    tvOnLine.setText("в сети");
                    lila_jur_item.setBackgroundColor(getResources().getColor(R.color.read_menu));
                    //tvSurname.setTextColor(getResources().getColor(R.color.read0));
                    tvOnLine.setTextColor(getResources().getColor(R.color.read0));
                }
                else{
                    //tvOnLine.setBackgroundResource(R.color.on_line_false);
                    tvOnLine.setText("");
                    tvOnLine.setTextColor(getResources().getColor(R.color.read2));

                }
                //imageView.setImageResource(Integer.parseInt(list.get(position).getString("icon")));

            } catch (JSONException e) {
                // TODO Auto-generatedcatchblock
                e.printStackTrace();
            }
            return rowView;
        }
    }

    class AsyncTaskLiked extends AsyncTask<Void, Integer, String> {
        // фоновая работа
        @Override
        protected String doInBackground(Void... params) {
            String str_url = "http://"+in.get_url()+"/123.jurlike";

            Log.d("qwerty", "сортировка или нет "+  json_query_jurlist);
            return ServerSendData.sendRegData(str_url, json_query_jurlist);
        }
        // выполняется после doInBackground, имеет доступ к UI
        protected void onPostExecute(String result) {
            Log.d("qwerty", "ИЗБРАННОЕ "+ result);

        }
    }


    public void query_reg(){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("idu", in.get_id_user());
            jsonObj.put("password", in.get_password());
            if(jurList_checkBoxJurOnLine.isChecked()){
                jsonObj.put("sort_online", 1);
            }else{
                jsonObj.put("sort_online", 0);
            }
            jsonObj.put("sort_city", SORT_CITY);
            jsonObj.put("sort_iz", bool_sort[0]);
            String s = "";
            for(int i = 1; i<bool_sort.length - 2; i++){
                if(bool_sort[i+1]){
                    s = s + i+ " ";
                }
            }
            jsonObj.put("sort_spec", s);
            jsonObj.put("sort_ud", bool_sort[9]);
            jsonObj.put("coor_x", in.get_latitude());
            jsonObj.put("coor_y", in.get_longitude());
            jsonObj.put("sort_reting", bool_sort[10]);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        json_query_jurlist = String.valueOf(jsonObj);
        Log.d("qwerty", "сортировка или нет "+  json_query_jurlist);
        new AsyncTaskJurList().execute();
    }

    class AsyncTaskJurList extends AsyncTask<Void, Integer, String> {
        // фоновая работа
        @Override
        protected String doInBackground(Void... params) {
            String str_url = "http://"+in.get_url()+"/123.getjurlist";

            Log.d("qwerty", "сортировка или нет "+  json_query_jurlist);
            return ServerSendData.sendRegData(str_url, json_query_jurlist);
        }
        // выполняется после doInBackground, имеет доступ к UI
        protected void onPostExecute(String result) {
            if(result == null){
                asked_progressBar.setVisibility(ProgressBar.VISIBLE);
                Toast.makeText(Advocates_list_quest.this,
                        "Нет связи с сервером!",
                        Toast.LENGTH_LONG).show();
                Advocates_list_quest.this.finish();
            }else{
                start_activity(result);
            }
        }
    }
    public static class ServerSendData {

        public static String sendRegData(String stringUrl, String json_user_str) {

            String result =  null;
            try {
                URL url = new URL(stringUrl);

                URLConnection connection = url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;

                httpConnection.setDoOutput(true);
                httpConnection.setChunkedStreamingMode(0);
                OutputStream out = new BufferedOutputStream(httpConnection.getOutputStream());

                out.write(json_user_str.getBytes());

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
            } catch (MalformedURLException e) { Log.d(DEBUG_TAG, "z8");result="-11";
            } catch (IOException e1) { Log.d(DEBUG_TAG, "z9");result="-11";
            }

            return result;
        }
    }
}

/*listViewJur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {

                try {
                    in.set_id_jur(Integer.parseInt(listJurJSON.get(position - 1).getString("idjur")));
                    in.set_add_quest_tip(3);
                    in.set_fio_jur(listJurJSON.get(position - 1).getString("surname") + " " + listJurJSON.get(position - 1).getString("name").charAt(0) + ". ");// + listJurJSON.get(position - 1).getString("patronymic").charAt(0) + ". ");
                    Intent intent = new Intent(Advocates_list_quest.this, AddQuestion.class);
                    startActivity(intent);
                    Advocates_list_quest.this.finish();

                    //in.set_jur_surname(listJurJSON.get(position - 1).getString("surname"));
                    //in.set_jur_name(listJurJSON.get(position - 1).getString("name"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/