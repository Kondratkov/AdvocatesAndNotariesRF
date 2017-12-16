package kondratkov.advocatesandnotariesrf.advocates;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.New_sidebar;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.account.Address;
import kondratkov.advocatesandnotariesrf.account.Bup;
import kondratkov.advocatesandnotariesrf.account.City;
import kondratkov.advocatesandnotariesrf.api_classes.Filter.FindByCoordinatesFilter;
import kondratkov.advocatesandnotariesrf.api_classes.Filter.FindJuristFilter;
import kondratkov.advocatesandnotariesrf.api_classes.Filter.FindNotaryByCoordinatesFilter;
import kondratkov.advocatesandnotariesrf.api_classes.JuristAccounClass;
import kondratkov.advocatesandnotariesrf.api_classes.TopQuestion;
import kondratkov.advocatesandnotariesrf.maps.Map_jur_and_notary;
import kondratkov.advocatesandnotariesrf.start_help.Start_activity_no_login;

import static android.R.id.list;

public class Advocates_list extends Activity implements View.OnTouchListener, SearchView.OnQueryTextListener{

    private static final String DEBUG_TAG = "qwerty";
    public String ADV_SEARCH = "";
    boolean bool_jurl_list = false;
    Point point;
    int view_height;
    public int kol;

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
    public Button but_sort1, but_sort2, but_sort3;
    boolean b1 = true, b2= true, bb_1=false, bb_2=false, bb_3=false ;
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

    public CheckBox jurList_checkbox_f;
    public SearchView searchView_adv;

    CheckBox jurList_checkBoxJurOnLine;
    public Map<CheckBox, Integer> map_idjur;

    public boolean[] bool_sort = new boolean[]{false, false, false, false, false, false, false, false, false, false, false};

    public JuristAccounClass[] mcArrayJuristAccoun;
    public JuristAccounClass[] mcArrayJuristAccounSearch;

    public int Search_id_city;
    public int Search_id_JuristAreas;
    public String Search_specialization;
    public Bup Search_bup;
    public FindJuristFilter.sortingType Search_sortingType1;
    public FindByCoordinatesFilter.sortingType Search_sortingType2;
    public String Search_surename;
    public boolean Search_IsOnline;
    public boolean Search_WorkInOffDays;
    public boolean Search_CanFastComing;

    public boolean sort_bool_fuck[] ;
    public boolean sortir = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advocates_list);

        Gson gson_Search = new Gson();
        Search_bup = gson_Search.fromJson((String)getIntent().getSerializableExtra("BUP"),Bup.class);
        //Search_id_city = (int)getIntent().getSerializableExtra("CITY");
        Search_id_JuristAreas = (int)getIntent().getSerializableExtra("SUDTER");
        Search_specialization = (String)getIntent().getSerializableExtra("SPECIALIZATION");
        Search_sortingType1 = FindJuristFilter.sortingType.Name;
        Search_sortingType2 = FindByCoordinatesFilter.sortingType.Name;
        sort_bool_fuck = new boolean[4];
        for(int i=0; i<4; i++ ){
            sort_bool_fuck[i]=false;
        }

        listViewJur = (ListView)findViewById(R.id.jurList_listView);
        listViewJur.setOnTouchListener(this);
        LinearLayout l = new LinearLayout(this);
        TextView t1 = new TextView(this);

        jurList_checkbox_f = (CheckBox)findViewById(R.id.jurList_checkbox_f);
        jurList_checkbox_f.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                search_jurist();
            }
        });

        searchView_adv = (SearchView)findViewById(R.id.searchView_adv);

        l.setPadding(0, dpToPx(86), 0, 0);
        t1.setText("");
        t1.setTextSize(1);
        t1.setWidth(1000);//dpToPx(100));
        t1.setPadding(15, 0, 0, 0);

        l.addView(t1);
        l.setBackgroundResource(R.drawable.transpar);
        listViewJur.addHeaderView(l);
        in = new IN();
        but_sort1 = (Button)findViewById(R.id.jurList_but_1);
        but_sort2 = (Button)findViewById(R.id.jurList_but_2);
        but_sort3 = (Button)findViewById(R.id.jurList_but_3);

        jurList_tv_sort_city = (TextView)findViewById(R.id.jurList_tv_sort_city);

        map_idjur = new HashMap<CheckBox, Integer>();

        search_jurist();
        setupSearchView();
    }

    private void setupSearchView() {
        searchView_adv.setIconifiedByDefault(true);
        searchView_adv.setOnQueryTextListener(this);
        searchView_adv.setSubmitButtonEnabled(false);
        searchView_adv.setQueryHint("поиск адвоката");
    }

    @Override
    protected void onStart(){
        super.onStart();

        if(in.getOnemay()){
            /*
            Gson gson = new Gson();
            if(in.get_filter_tip()==0){
                FindJuristFilter findJuristFilter = new FindJuristFilter();
                findJuristFilter.CityId = Search_id_city;
                findJuristFilter.Bups = Search_bup;
                findJuristFilter.JudicialAreaId = Search_id_JuristAreas;//.WorkInOffDays = bool_sort[2];
                findJuristFilter.Specialization = Search_specialization;//notary.Equipage = bool_sort[3];
                findJuristFilter.CanFastComing = sort_bool_fuck[0];
                findJuristFilter.WorkInOffDays = sort_bool_fuck[1];
                findJuristFilter.IsOnline = sort_bool_fuck[2];
                findJuristFilter.SortingType = Search_sortingType1;
                findJuristFilter.Radius = 50;
                String JSON = gson.toJson(findJuristFilter);
                new UrlConnectionTask().execute(JSON);
            }else{
                FindByCoordinatesFilter findByCoordinatesFilter = new FindByCoordinatesFilter();
                findByCoordinatesFilter.Latitude = in.get_latitude();//.WorkInOffDays = bool_sort[2];
                findByCoordinatesFilter.Longitude = in.get_longitude();
                findByCoordinatesFilter.Radius = 50;
                findByCoordinatesFilter.Bups = Search_bup;
                findByCoordinatesFilter.JudicialAreaId = Search_id_JuristAreas;//.WorkInOffDays = bool_sort[2];
                findByCoordinatesFilter.Specialization = Search_specialization;//notary.Equipage = bool_sort[3];
                findByCoordinatesFilter.CanFastComing = sort_bool_fuck[0];
                findByCoordinatesFilter.WorkInOffDays = sort_bool_fuck[1];
                findByCoordinatesFilter.IsOnline = sort_bool_fuck[2];
                findByCoordinatesFilter.SortingType = Search_sortingType2;
                String JSON = gson.toJson(findByCoordinatesFilter);
                new UrlConnectionTask().execute(JSON);//new AsyncTaskNotary().execute();
            }*/
        }else{
            Intent intent = new Intent(Advocates_list.this, Start_activity_no_login.class);
            startActivity(intent);
            Advocates_list.this.finish();
        }
        //OneStart();
    }

    public int dpToPx(int dp){
        DisplayMetrics displayMetrics = Advocates_list.this.getResources().getDisplayMetrics();
        int px =Math.round(dp *(displayMetrics.xdpi /DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public void search_jurist(){

        Gson gson = new Gson();
        if(in.get_filter_tip()==0){
            FindJuristFilter findJuristFilter = new FindJuristFilter();
            findJuristFilter.CityId = Search_id_city;
            findJuristFilter.Bups = Search_bup;
            findJuristFilter.JudicialAreaId = Search_id_JuristAreas;//.WorkInOffDays = bool_sort[2];
            findJuristFilter.Specialization = Search_specialization;//notary.Equipage = bool_sort[3];
            findJuristFilter.CanFastComing = sort_bool_fuck[0];
            findJuristFilter.WorkInOffDays = sort_bool_fuck[1];
            findJuristFilter.IsOnline = sort_bool_fuck[2];
            findJuristFilter.SortingType = Search_sortingType1;
            findJuristFilter.Radius = 50;
            String JSON = gson.toJson(findJuristFilter);
            new UrlConnectionTask().execute(JSON);
        }else{
            FindByCoordinatesFilter findByCoordinatesFilter = new FindByCoordinatesFilter();
            findByCoordinatesFilter.Latitude = (double)getIntent().getSerializableExtra("LANT"); //in.get_latitude();//.WorkInOffDays = bool_sort[2];
            findByCoordinatesFilter.Longitude = (double)getIntent().getSerializableExtra("LONG");
            findByCoordinatesFilter.Radius = 50;
            findByCoordinatesFilter.Bups = Search_bup;
            findByCoordinatesFilter.JudicialAreaId = Search_id_JuristAreas;//.WorkInOffDays = bool_sort[2];
            findByCoordinatesFilter.Specialization = Search_specialization;//notary.Equipage = bool_sort[3];
            findByCoordinatesFilter.CanFastComing = sort_bool_fuck[0];
            findByCoordinatesFilter.WorkInOffDays = sort_bool_fuck[1];
            findByCoordinatesFilter.IsOnline = sort_bool_fuck[2];
            findByCoordinatesFilter.SortingType = Search_sortingType2;
            String JSON = gson.toJson(findByCoordinatesFilter);
            new UrlConnectionTask().execute(JSON);//new AsyncTaskNotary().execute();
        }
    }

    public void search_jurist_pos(){
        if(sort_bool_fuck[0]==false && sort_bool_fuck[1]==false && sort_bool_fuck[2]==false){
            search_jurist();
        }else {
            try {
                mcArrayJuristAccoun = mcArrayJuristAccounSearch;

                if (sort_bool_fuck[0]) {
                    kol=0;
                    ArrayList<JuristAccounClass> jas = new ArrayList<JuristAccounClass>();
                    for (int i = 0; i < mcArrayJuristAccoun.length; i++) {
                        if (mcArrayJuristAccoun[i].CanFastComing) {
                            jas.add(mcArrayJuristAccoun[i]);
                            kol++;
                        }
                    }
                    mcArrayJuristAccoun = new JuristAccounClass[kol];
                    for(int i =0; i<kol; i++){
                        mcArrayJuristAccoun[i] = jas.get(i);
                    }
                }
                if (sort_bool_fuck[1]) {
                    kol=0;
                    ArrayList<JuristAccounClass> jas = new ArrayList<JuristAccounClass>();
                    for (int i = 0; i < mcArrayJuristAccoun.length; i++) {
                        if (mcArrayJuristAccoun[i].WorkInOffDays) {
                            jas.add(mcArrayJuristAccoun[i]);
                            kol++;
                        }
                    }
                    mcArrayJuristAccoun = new JuristAccounClass[kol];
                    for(int i =0; i<kol; i++){
                        mcArrayJuristAccoun[i] = jas.get(i);
                    }
                }
                if (sort_bool_fuck[2]) {
                    kol=0;
                    ArrayList<JuristAccounClass> jas = new ArrayList<JuristAccounClass>();
                    for (int i = 0; i < mcArrayJuristAccoun.length; i++) {
                        if (mcArrayJuristAccoun[i].IsOnline) {
                            jas.add(mcArrayJuristAccoun[i]);
                            kol++;
                        }
                    }
                    mcArrayJuristAccoun = new JuristAccounClass[kol];
                    for(int i =0; i<kol; i++){
                        mcArrayJuristAccoun[i] = jas.get(i);
                    }
                }

                MyAdapterJsonList mam = new MyAdapterJsonList(this, mcArrayJuristAccoun);//getjurList(jsonObjectjurList));
                listViewJur.setAdapter(mam);

                listViewJur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position,
                                            long id) {
                        if (position != 0) {
                            Gson gson = new Gson();
                            String ssd = gson.toJson(mcArrayJuristAccoun[position - 1]);
                            String sd = "sdf";
                            in.set_id_jur(mcArrayJuristAccoun[(int) id].Id);
                            Intent int3 = new Intent(Advocates_list.this, Advocate_profile.class);
                            startActivity(int3);
                        }

                    }
                });
            } catch (Exception e) {
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jurList_but_Cancel:
                Advocates_list.this.finish();
                break;
            case R.id.jurList_imbut_menu:
                Intent intent6 = new Intent(Advocates_list.this, New_sidebar.class);
                startActivity(intent6);
                break;
            case R.id.jurList_but_1:
                if(bb_1){
                    bb_1=false;
                    sort_bool_fuck[0]=false;
                    but_sort1.setTextColor(Color.argb(80,255,255,255));
                    search_jurist_pos();
                }else{
                    bb_1=true;
                    sort_bool_fuck[0]=true;
                    but_sort1.setTextColor(Color.argb(255,255,255,255));
                    search_jurist_pos();
                }
                break;
            case R.id.jurList_but_2:
                if(bb_2){
                    bb_2=false;
                    sort_bool_fuck[1]=false;
                    but_sort2.setTextColor(Color.argb(80,255,255,255));
                    search_jurist_pos();
                }else{
                    bb_2=true;
                    sort_bool_fuck[1]=true;
                    but_sort2.setTextColor(Color.argb(255,255,255,255));
                    search_jurist_pos();
                }
                break;
            case R.id.jurList_but_3:
                if(bb_3){
                    bb_3=false;
                    sort_bool_fuck[2]=false;
                    but_sort3.setTextColor(Color.argb(80,255,255,255));
                    search_jurist_pos();
                }else{
                    sort_bool_fuck[2]=true;
                    bb_3=true;
                    but_sort3.setTextColor(Color.argb(255,255,255,255));
                    search_jurist_pos();
                }
                break;
            case R.id.jurList_but_sort_city:
                dialog_advoc_sort1();
                break;

            case R.id.jurList_but_map:
                Intent intent_sidebar = new Intent(Advocates_list.this, Map_jur_and_notary.class);
                Gson gson = new Gson();
                intent_sidebar.putExtra("ARRAY", gson.toJson(mcArrayJuristAccoun));
                startActivity(intent_sidebar);
                break;
        }
    }

    public void dialog_advoc_sort1(){
        final Dialog dialog = new Dialog(Advocates_list.this);
        dialog.setTitle("Сортировка");
        dialog.setContentView(R.layout.advocate_list_dialog_sort);

        Button btnClose = (Button) dialog.getWindow().findViewById(
                R.id.but_adv_sort_cancel);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final Button btnYes = (Button) dialog.getWindow().findViewById(
                R.id.but_adv_sort_yes);

        final CheckBox ch1 = (CheckBox)dialog.getWindow().findViewById(R.id.adv_sort_checkBox);
        final CheckBox ch2 = (CheckBox)dialog.getWindow().findViewById(R.id.adv_sort_checkBox2);

        switch (int_sort){
            case 1:ch1.setChecked(true);break;
            case 2:ch2.setChecked(true);break;
        }

        ch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch1.isChecked()){
                    ch2.setChecked(false);
                }
            }
        });
        ch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch2.isChecked()){
                    ch1.setChecked(false);
                }
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch1.isChecked()){
                    int_sort=1;
                    Search_sortingType1 = FindJuristFilter.sortingType.Name;
                    Search_sortingType2 = FindByCoordinatesFilter.sortingType.Name;
                }else if(ch2.isChecked()){
                    Search_sortingType1 = FindJuristFilter.sortingType.Rating;
                    Search_sortingType2 = FindByCoordinatesFilter.sortingType.Rating;
                    int_sort=2;
                }else{
                    int_sort=0;
                }
                dialog.dismiss();
                search_jurist();
            }
        });
        dialog.show();
    }

    public void start_activity(){

        lila_sort_panel =(LinearLayout)findViewById(R.id.jurList_lila_sort_panel);
        jurList_lila_city =(LinearLayout)findViewById(R.id.jurList_lila_city);

        if((Boolean)getIntent().getSerializableExtra("ANSWERNEXT")){
            List <JuristAccounClass> mJuristAccounClasses = new ArrayList<JuristAccounClass>();
            for(JuristAccounClass juristAccounClass : mcArrayJuristAccoun){
                if(juristAccounClass.CurrentLatitude!=0 && juristAccounClass.CurrentLongitude !=0){
                    double mx = Math.abs(in.get_latitude()- juristAccounClass.CurrentLatitude);//51.714342);
                    double my = Math.abs(in.get_longitude() - juristAccounClass.CurrentLongitude);//39.275005);
                    double dist = Math.sqrt(Math.pow(mx, 2) + Math.pow(my,2));
                    if(dist< 0.15){
                        mJuristAccounClasses.add(juristAccounClass);
                    }
                }
            }

            mcArrayJuristAccoun = new JuristAccounClass[mJuristAccounClasses.size()];
            for(int i = 0; i<mJuristAccounClasses.size(); i++){
                mcArrayJuristAccoun[i] = mJuristAccounClasses.get(i);
            }
        }

        String city = (String)getIntent().getSerializableExtra("CITY");
        if(city.length() != 0){
            List <JuristAccounClass> mJuristAccounClasses = new ArrayList<JuristAccounClass>();
            for(JuristAccounClass juristAccounClass : mcArrayJuristAccoun){

                if(juristAccounClass.Address.City.equals(city)){
                    mJuristAccounClasses.add(juristAccounClass);
                }

            }

            mcArrayJuristAccoun = new JuristAccounClass[mJuristAccounClasses.size()];
            for(int i = 0; i<mJuristAccounClasses.size(); i++){
                mcArrayJuristAccoun[i] = mJuristAccounClasses.get(i);
            }
        }

        try {
            MyAdapterJsonList mam = new MyAdapterJsonList(this, mcArrayJuristAccoun);//getjurList(jsonObjectjurList));
            listViewJur.setAdapter(mam);

            listViewJur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position,
                                        long id) {
                    if(position !=0){
                        Gson gson = new Gson();
                        String ssd = gson.toJson(mcArrayJuristAccoun[position-1]);
                        String sd ="sdf";
                        in.set_id_jur(mcArrayJuristAccoun[(int) id].Id);
                        Intent int3 = new Intent(Advocates_list.this, Advocate_profile.class);
                        startActivity(int3);
                    }

                }
            });
    }catch (Exception e){}
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

                    Animation anim = AnimationUtils.loadAnimation(Advocates_list.this, R.anim.trans_down_sort);
                    Animation anim2 = AnimationUtils.loadAnimation(Advocates_list.this, R.anim.trans_up_sort2);

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

                    Animation anim = AnimationUtils.loadAnimation(Advocates_list.this, R.anim.trans_up_sort);
                    Animation anim2 = AnimationUtils.loadAnimation(Advocates_list.this, R.anim.trans_down_sort2);

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

        if(int_sort ==2){
            JuristAccounClass []m = new JuristAccounClass[mcArrayJuristAccoun.length];
            for(int i =0; i<mcArrayJuristAccoun.length; i++){
                m[i] = mcArrayJuristAccoun[mcArrayJuristAccoun.length-1-i];
            }
            mcArrayJuristAccoun = m;
            mcArrayJuristAccounSearch = m;
            try {
                MyAdapterJsonList mam = new MyAdapterJsonList(this, mcArrayJuristAccoun);//getjurList(jsonObjectjurList));
                listViewJur.setAdapter(mam);

                listViewJur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position,
                                            long id) {
                        if(position !=0){
                            Gson gson = new Gson();
                            String ssd = gson.toJson(mcArrayJuristAccoun[position-1]);
                            String sd ="sdf";
                            in.set_id_jur(mcArrayJuristAccoun[(int) id].Id);
                            Intent int3 = new Intent(Advocates_list.this, Advocate_profile.class);
                            startActivity(int3);
                        }

                    }
                });
            }catch (Exception e){}
        }

            if(sort_bool_fuck[0]==true || sort_bool_fuck[1]==true || sort_bool_fuck[2]==true){
                search_jurist_pos();
            }
        }

    public void stopProgressBar(){
        //asked_frameProg.setBackgroundResource(R.color.frameOff);
       //asked_frameProg.setClickable(false);
        //asked_progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        int kol_vo=0;
        for (int i = 0; i < mcArrayJuristAccounSearch.length; i++) {
            if(mcArrayJuristAccounSearch[i].Fio.toLowerCase().contains(newText.toString().toLowerCase())){
                kol_vo++;
            }
        }
        mcArrayJuristAccoun = new JuristAccounClass[kol_vo];
        int f=0;
        for (int i = 0; i < mcArrayJuristAccounSearch.length; i++) {
            if(mcArrayJuristAccounSearch[i].Fio.toLowerCase().contains(newText.toString().toLowerCase())){
                mcArrayJuristAccoun[f]=mcArrayJuristAccounSearch[i];
                f++;
            }
        }
        if(newText.toString()==""){
            mcArrayJuristAccoun=mcArrayJuristAccounSearch;
        }
        start_activity();

        return false;
    }

    class MyAdapterJsonList extends ArrayAdapter {

        private JuristAccounClass[] juristAccounClasses = null;
        private Activity context;

        public MyAdapterJsonList(Activity context, JuristAccounClass[] juristAccounClasses) {
            super(context, R.layout.advocates_item_list, juristAccounClasses);
            this.context = context;
            this.juristAccounClasses = juristAccounClasses;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = null;

            rowView = inflater.inflate(R.layout.advocates_item_list, parent, false);
            TextView tvSurname   = (TextView) rowView.findViewById(R.id.tvSurnameJur);
            TextView tvDist = (TextView) rowView.findViewById(R.id.textView6);
            tvSurname.setTextSize(in.get_font_1());

            TextView tvCityJur = (TextView) rowView.findViewById(R.id.tvJLcity);
            tvCityJur.setTextSize(in.get_font_2());

            TextView tvRatingJur = (TextView) rowView.findViewById(R.id.tvRatingJur);
            tvRatingJur.setTextSize(in.get_font_2());

            TextView tvOnLine    = (TextView) rowView.findViewById(R.id.tvOnLine);
            ImageView imageView  = (ImageView) rowView.findViewById(R.id.imageViewJur);
            LinearLayout lila_jur_item = (LinearLayout)rowView.findViewById(R.id.lila_jur_item);
            LinearLayout lila_jur_item_fon = (LinearLayout)rowView.findViewById(R.id.lila_jur_item_fon);

            tvSurname.setText(juristAccounClasses[position].Fio);

            try{
                tvCityJur.setText(String.valueOf(juristAccounClasses[position].Address.City));
            }catch (Exception e){}



            tvRatingJur.setText(String.valueOf(juristAccounClasses[position].Rating));

            try{
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                tvDist.setText(in.sDistance(juristAccounClasses[position].Latitude, juristAccounClasses[position].Longitude, lm, Advocates_list.this));
            }catch (Exception e){

            }

            if(juristAccounClasses[position].IsVip){
                    lila_jur_item.setBackgroundResource(R.drawable.status_vip);
                    lila_jur_item_fon.setBackgroundResource(R.color.colorvip);
                    tvSurname.setTextColor(Color.argb(255,255,211,35));
            }

            CheckBox jur_item_checkBox = (CheckBox)rowView.findViewById(R.id.jur_item_checkBox);
                /*if(juristAccounClasses[position].   list.get(position).getInt("liked")==0){
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
                });*/

            if(juristAccounClasses[position].IsOnline){
                //tvOnLine.setBackgroundResource(R.color.on_line_true);
                tvOnLine.setText("в сети");
                //lila_jur_item.setBackgroundColor(getResources().getColor(R.color.read_menu));
                //tvSurname.setTextColor(getResources().getColor(R.color.read0));
                tvOnLine.setTextColor(getResources().getColor(R.color.read0));
            }
            else{
                //tvOnLine.setBackgroundResource(R.color.on_line_false);

                tvOnLine.setText("");
                tvOnLine.setTextColor(getResources().getColor(R.color.read2));

            }

            //imageView.setImageResource(Integer.parseInt(list.get(position).getString("icon")));

            return rowView;
        }
    }

    /*class AsyncTaskLiked extends AsyncTask<Void, Integer, String> {
        // фоновая работа
        @Override
        protected String doInBackground(Void... params) {
            String str_url = "http://"+in.get_url()+"/123.jurlike";

            Log.d("qwerty", "сортировка или нет jurlike "+  json_query_jurlist);
            return ServerSendData.sendRegData(str_url, json_query_jurlist);
        }
        // выполняется после doInBackground, имеет доступ к UI
        protected void onPostExecute(String result) {
            Log.d("qwerty", "ИЗБРАННОЕ "+ result);
            query_reg();
        }
    }*/

    public void query_reg11(){
        Gson gson = new Gson();
        if(in.get_filter_tip()==0){
            FindJuristFilter findJuristFilter = new FindJuristFilter();
            findJuristFilter.CityId = Search_id_city;
            findJuristFilter.Bups = Search_bup;
            findJuristFilter.JudicialAreaId = Search_id_JuristAreas;//.WorkInOffDays = bool_sort[2];
            findJuristFilter.Specialization = Search_specialization;//notary.Equipage = bool_sort[3];
            findJuristFilter.CanFastComing = sort_bool_fuck[0];
            findJuristFilter.WorkInOffDays = sort_bool_fuck[1];
            findJuristFilter.IsOnline = sort_bool_fuck[2];
            findJuristFilter.SortingType = Search_sortingType1;
            findJuristFilter.Radius = 50;
            String JSON = gson.toJson(findJuristFilter);
            new UrlConnectionTask().execute(JSON);
        } else{
            FindByCoordinatesFilter findByCoordinatesFilter = new FindByCoordinatesFilter();
            findByCoordinatesFilter.Latitude = (double)getIntent().getSerializableExtra("LANT"); //in.get_latitude();//.WorkInOffDays = bool_sort[2];
            findByCoordinatesFilter.Longitude = (double)getIntent().getSerializableExtra("LONG");
            findByCoordinatesFilter.Radius = 50;
            findByCoordinatesFilter.Bups = Search_bup;
            findByCoordinatesFilter.JudicialAreaId = Search_id_JuristAreas;//.WorkInOffDays = bool_sort[2];
            findByCoordinatesFilter.Specialization = Search_specialization;//notary.Equipage = bool_sort[3];
            findByCoordinatesFilter.CanFastComing = sort_bool_fuck[0];
            findByCoordinatesFilter.WorkInOffDays = sort_bool_fuck[1];
            findByCoordinatesFilter.IsOnline = sort_bool_fuck[2];
            findByCoordinatesFilter.SortingType = Search_sortingType2;
            String JSON = gson.toJson(findByCoordinatesFilter);
            new UrlConnectionTask().execute(JSON);//new AsyncTaskNotary().execute();
        }

        //new AsyncTaskJurList().execute();
    }


    class UrlConnectionTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();

            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            //RequestBody formBody = RequestBody.create(JSON, json_signup);

            int iiyy = in.get_filter_tip();
            Request request;
            if(in.get_filter_tip()==0){
                String s= in.get_token_type()+" "+in.get_token();
                String s2 = params[0];
                String s3= "http://"+in.get_url()+"/JuristAccounts/GetJuristAccounts";
                String s4="213";
                String ssd = params[0];
                request = new Request.Builder()
                        .header("Authorization", in.get_token_type()+" "+in.get_token())
                        //.url("http://"+in.get_url()+"/JuristAccounts/GetJuristAccountsByFilter")
                        .url("http://"+in.get_url()+"/JuristAccounts/GetJuristAccounts")
                        //post(RequestBody.create(MEDIA_TYPE_MARKDOWN, params[0]))
                        .build();
            }
            else{
                String s= in.get_token_type()+" "+in.get_token();
                String s2 = params[0];
                String s3= "http://"+in.get_url()+"/JuristAccounts/GetJuristByAddressCoordinates";
                String s4="213";
                String ssd = params[0];
                request = new Request.Builder()
                        .header("Authorization", in.get_token_type()+" "+in.get_token())
                        .url("http://"+in.get_url()+"/JuristAccounts/GetJuristByAddressCoordinates")
                        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, params[0]))
                        .build();
            }

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
            String ddf = result;
            Gson gson = new Gson();
            if(result!=null){
                mcArrayJuristAccoun = gson.fromJson(result, JuristAccounClass[].class);
                mcArrayJuristAccounSearch = gson.fromJson(result, JuristAccounClass[].class);
                start_activity();
            }
            super.onPostExecute(result);
        }
    }

    public float xX = 0;
    public float xNew = 0;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_UP):
                if(xNew > 300){
                    Intent intent = new Intent(Advocates_list.this, New_sidebar.class);
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
    }
}
