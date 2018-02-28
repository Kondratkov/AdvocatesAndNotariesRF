package kondratkov.advocatesandnotariesrf.notary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
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
import java.util.List;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.New_sidebar;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.Sidebar;
import kondratkov.advocatesandnotariesrf.api_classes.Filter.FindNotaryByCoordinatesFilter;
import kondratkov.advocatesandnotariesrf.api_classes.Filter.FindNotaryByFilter;
import kondratkov.advocatesandnotariesrf.api_classes.Filter.GetNotaryByAddressCoordinates;
import kondratkov.advocatesandnotariesrf.api_classes.Notary;
import kondratkov.advocatesandnotariesrf.maps.Map_coor;

public class Notary_list extends Activity implements View.OnTouchListener,
        SearchView.OnQueryTextListener {

    boolean bool_notaryl_list = false;
    String LOG_TAG = "qwerty_not";

    String json_notary = "";
    Point point;
    public String SEARCH_NOT = "";
    public SearchView searchView_notar;

    public ListView listViewnotary;
    public JSONArray jsonArraynotaryList = null;
    public JSONObject jsonObjectnotaryList = null;
    public List<JSONObject> listnotaryJSON = null;
    public List<Boolean> iz_po_list = null;
    public boolean add_or_set = true;
    public TextView notary_tv_sort_city;

    //dlya sortirovki
    public LinearLayout lila_sort_panel, jurList_lila_sity, lila_sort_panel_down_tw;
    boolean b1 = true, b2 = true;
    public View lm[] = new View[3];//-ff
    public Spinner spinner_notary_sort = null;
    public String sort_notary = "";
    public String[] m_sort_notary = null;
    public String sort_notarys = "������� ������";
    public String[] m_sort_notarys = null;

    public Button but_notaryList_sort;

    public IN in = new IN();
    public int code;

    public boolean[] bool_sort = new boolean[]{false, false, false, false, false, false, false, false, false, false};
    int view_height;
    public String SORT_CITY = "";

    public Button but_sort_not_list1, but_sort_not_list2;
    public CheckBox checkBox_list_not1, checkBox_list_not2;
    public boolean b1_sort = false, b2_sort = false, b3_sort = false, b4_sort = false;
    public int sort_po = 0;

    public Notary[] mcArrayNotary;
    public Notary[] mcSearchNotary;
    /**
     * для поиска по ФИО
     */
    public String[] fio_notaty = null;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notary_list);

        in = new IN();

        searchView_notar = (SearchView) findViewById(R.id.searchView_notar);

        but_sort_not_list1 = (Button) findViewById(R.id.but_sort_not_list1);
        but_sort_not_list2 = (Button) findViewById(R.id.but_sort_not_list2);

        checkBox_list_not1 = (CheckBox) findViewById(R.id.checkBox_list_not1);
        checkBox_list_not2 = (CheckBox) findViewById(R.id.checkBox_list_not2);

        checkBox_list_not1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                b1_sort = isChecked;
                // new UrlConnectionTask().execute();//new AsyncTaskNotary().execute();
            }
        });
        checkBox_list_not2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                b2_sort = isChecked;
                //new UrlConnectionTask().execute();//new AsyncTaskNotary().execute();
            }
        });

        bool_sort = in.get_sort_notary();

        listViewnotary = (ListView) findViewById(R.id.notary_listView);
        LinearLayout l = new LinearLayout(this);
        TextView t1 = new TextView(this);

        l.setPadding(0, dpToPx(81), 0, 0);

        t1.setText("");
        t1.setTextSize(1);
        t1.setWidth(200);
        t1.setPadding(15, 0, 0, 0);

        l.addView(t1);
        l.setBackgroundResource(R.drawable.transpar);
        listViewnotary.addHeaderView(l);

//dlya sortirovki
        lila_sort_panel = (LinearLayout) findViewById(R.id.notary_lila_sort_panel);
        jurList_lila_sity = (LinearLayout) findViewById(R.id.notary_lila_city);

        try {
            //json_notary = getString(R.string.mess_json);
            jsonObjectnotaryList = new JSONObject(json_notary);

        } catch (JSONException e) {
            // TODO Auto-generatedcatchblock
            e.printStackTrace();
        }
        setupSearchView();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setupSearchView() {
        searchView_notar.setIconifiedByDefault(true);
        searchView_notar.setOnQueryTextListener(this);
        searchView_notar.setSubmitButtonEnabled(false);
        searchView_notar.setQueryHint("поиск нотариуса");
    }

    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();

        if (in.getOnemay()) {
        } else {
            Notary_list.this.finish();
        }
        in.setChoice_of_menus(8);
        listViewnotary.setOnTouchListener(this);

        Gson gson = new Gson();
        if (in.get_filter_tip() == 0) {
            FindNotaryByFilter notary= new FindNotaryByFilter();
            notary.WorkInOffDays = bool_sort[2];
            notary.Equipage = bool_sort[3];
            notary.Appointments = bool_sort[4];
            notary.AppointmentsEmail = bool_sort[5];
            notary.WorkWithJur = bool_sort[6];
            notary.IsPleadtingHereditaryCases = bool_sort[7];
            notary.IsSitesCertification = bool_sort[8];
            notary.LockDocuments = bool_sort[9];
            notary.DepositsReception = bool_sort[10];
            notary.RequestsAndDuplicate = bool_sort[11];
            notary.Transactions = bool_sort[12];
            notary.Consultation = false;

            notary.SortingType = FindNotaryByFilter.sortingType.Name;
            String JSON = gson.toJson(notary);
            new UrlConnectionTaskFilter().execute(JSON);//new AsyncTaskNotary().execute();

        } else{
            GetNotaryByAddressCoordinates notary= new GetNotaryByAddressCoordinates();
            notary.WorkInOffDays = bool_sort[2];
            notary.Equipage = bool_sort[3];
            notary.Appointments = bool_sort[4];
            notary.AppointmentsEmail = bool_sort[5];
            notary.WorkWithJur = bool_sort[6];
            notary.IsPleadtingHereditaryCases = bool_sort[7];
            notary.IsSitesCertification = bool_sort[8];
            notary.LockDocuments = bool_sort[9];
            notary.DepositsReception = bool_sort[10];
            notary.RequestsAndDuplicate = bool_sort[11];
            notary.Transactions = bool_sort[12];
            notary.Consultation = false;

            notary.Latitude = in.get_latitude();
            notary.Longitude = in.get_longitude();
            notary.Radius = 50;
            notary.SortingType = GetNotaryByAddressCoordinates.sortingType.Name;
            String JSON = gson.toJson(notary);
            new UrlConnectionTaskFilter().execute(JSON);//new AsyncTaskNotary().execute();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = Notary_list.this.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notary_but_cancel:
                Notary_list.this.finish();
                break;
            case R.id.notary_but_menu:
                Intent intent_n = new Intent(Notary_list.this, New_sidebar.class);
                startActivity(intent_n);
                break;
            case R.id.but_sort_not_list1:
                if (b3_sort) {
                    b3_sort = false;
                } else {
                    b3_sort = true;
                    b4_sort = false;
                }
                sort_po_void();
                break;
            case R.id.but_sort_not_list2:
                if (b4_sort) {
                    b4_sort = false;
                } else {
                    b4_sort = true;
                    b3_sort = false;
                }
                sort_po_void();
                break;
            /*case R.id.notary_but_sort_city:
                City_num = 0;

                new AsyncTaskDialog().execute();
                break;*/
        }
    }

    public boolean ff = true;

    public void sort_po_void() {
        if (b3_sort) {
            sort_po = 1;
            but_sort_not_list1.setTextColor(Color.argb(255, 255, 255, 255));
            but_sort_not_list2.setTextColor(Color.argb(80, 255, 255, 255));
        } else if (b4_sort) {
            sort_po = 2;
            but_sort_not_list1.setTextColor(Color.argb(80, 255, 255, 255));
            but_sort_not_list2.setTextColor(Color.argb(255, 255, 255, 255));
        } else {
            sort_po = 0;
            but_sort_not_list1.setTextColor(Color.argb(80, 255, 255, 255));
            but_sort_not_list2.setTextColor(Color.argb(80, 255, 255, 255));
        }
        //new UrlConnectionTask().execute();
        Gson gson = new Gson();

            GetNotaryByAddressCoordinates notary= new GetNotaryByAddressCoordinates();
            notary.WorkInOffDays = bool_sort[2];
            notary.Equipage = bool_sort[3];
            notary.Appointments = bool_sort[4];
            notary.AppointmentsEmail = bool_sort[5];
            notary.WorkWithJur = bool_sort[6];
            notary.IsPleadtingHereditaryCases = bool_sort[7];
            notary.IsSitesCertification = bool_sort[8];
            notary.LockDocuments = bool_sort[9];
            notary.DepositsReception = bool_sort[10];
            notary.RequestsAndDuplicate = bool_sort[11];
            notary.Transactions = bool_sort[12];
            notary.Consultation = false;

            notary.Latitude = in.get_latitude();
            notary.Longitude = in.get_longitude();
            notary.Radius = 50;
            notary.SortingType = GetNotaryByAddressCoordinates.sortingType.Name;
            String JSON = gson.toJson(notary);
            new UrlConnectionTaskFilter().execute(JSON);//new AsyncTaskNotary().execute();

    }


    public void start_s_server() {
        Log.d(LOG_TAG, "start_s_server");
        JSONObject jsonObject = null;
        JSONArray jsonArray = new JSONArray();

        Notary [] notaries;

        String notary_city_search = (String)getIntent().getSerializableExtra("CITY_SEARCH");


        List<Notary>notariesList = new ArrayList<Notary>();

//        if(notary_city_search.length() > 0){
//
//            for(int i = 0; i<mcArrayNotary.length; i++){
//                String dd1 = mcArrayNotary[i].Address2;
//                String dd = mcArrayNotary[i].Address.City;
//                if(mcArrayNotary[i].Address.City.equals(notary_city_search)){
//                    notariesList.add(mcArrayNotary[i]);
//                }
//            }
//
//            notaries = new Notary[notariesList.size()];
//            for(int i =0; i<notariesList.size(); i++){
//                notaries[i] = notariesList.get(i);
//            }
//            mcArrayNotary = notaries;
//        }

        MyAdapterJsonList mam = new MyAdapterJsonList(this, mcArrayNotary);//getnotaryList(jsonObjectnotaryList));
        listViewnotary.setAdapter(mam);

        listViewnotary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {

                Intent int3 = new Intent(Notary_list.this, Notary_profile.class);
                startActivity(int3);
            }
        });


        listViewnotary.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int position;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (position < firstVisibleItem && b1 == true) {
                    Log.d(LOG_TAG, "vnizvverh");

                    lila_sort_panel.setLayoutParams(
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, (float) 0.07));

                    Animation anim = AnimationUtils.loadAnimation(Notary_list.this, R.anim.trans_down_sort);
                    Animation anim2 = AnimationUtils.loadAnimation(Notary_list.this, R.anim.trans_up_sort2);

                    lila_sort_panel.setPadding(0, dpToPx(14), 0, 0);//lila_sort_panel.setPadding(0, dpToPx(64),0,0);
                    lila_sort_panel.startAnimation(anim);

                    jurList_lila_sity.setPadding(0, 10000000, 0, dpToPx(80));
                    jurList_lila_sity.startAnimation(anim2);
                    //FrameLayout jurList_frla = (FrameLayout)findViewById(R.id.jurList_frla);
                    //jurList_frla.startAnimation(anim2);
                    //jurList_lila_sity.startAnimation(anim2);
                    //jurList_lila_sity.setPadding(0, 0, 0, dpToPx(60));

                    b1 = false;
                    b2 = true;
                }
                if (position > firstVisibleItem && b2 == true) {

                    Animation anim = AnimationUtils.loadAnimation(Notary_list.this, R.anim.trans_up_sort);
                    Animation anim2 = AnimationUtils.loadAnimation(Notary_list.this, R.anim.trans_down_sort2);

                    lila_sort_panel.setPadding(0, dpToPx(52), 0, 0);
                    lila_sort_panel.startAnimation(anim);


                    jurList_lila_sity.startAnimation(anim2);
                    jurList_lila_sity.setPadding(0, 0, 0, 0);
                    //jurList_lila_sity.setPadding(0, 0, 0, 0);
                    //jurList_lila_sity.startAnimation(anim2);

                    b2 = false;
                    b1 = true;
                }
                position = firstVisibleItem;
            }
        });

    }

    /*public List<JSONObject> getnotaryList(JSONObject jsonObjectnotaryList) {

        List notaryListRT = new ArrayList<>();
        try {
            Log.d(LOG_TAG, "заполнение листа "+jsonObjectnotaryList);
            //json_notary = getString(R.string.mess_json);
            //JSONObject results = jsonObjectnotaryList.getJSONObject("notaryList");

            jsonArraynotaryList = jsonObjectnotaryList.getJSONArray("array");

            for (int i = 0; i <jsonArraynotaryList.length(); i++) {
                JSONObject arrayElement = jsonArraynotaryList.getJSONObject(i);
                notaryListRT.add(arrayElement);
            }

        } catch (JSONException e) {
            // TODO Auto-generatedcatchblock
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "List OK");
        return notaryListRT;
    }*/

    @Override
    public boolean onQueryTextSubmit(String query) {
        SEARCH_NOT = "";
        // new UrlConnectionTask().execute();//new AsyncTaskNotary().execute();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

       int kol_vo=0;
        for (int i = 0; i < mcSearchNotary.length; i++) {
            if(mcSearchNotary[i].Fio.toLowerCase().contains(newText.toString().toLowerCase())){
                kol_vo++;
            }
        }
        mcArrayNotary = new Notary[kol_vo];
        int f=0;
        for (int i = 0; i < mcSearchNotary.length; i++) {
            if(mcSearchNotary[i].Fio.toLowerCase().contains(newText.toString().toLowerCase())){
                mcArrayNotary[f]=mcSearchNotary[i];
                f++;
            }
        }
        if(newText.toString()==""){
            mcArrayNotary=mcSearchNotary;
        }
        start_s_server();

        SEARCH_NOT = newText;
        // new UrlConnectionTask().execute();//new AsyncTaskNotary().execute();
        return false;

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Notary_list Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
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

        private Notary[] arrayNotary = null;
        private Activity context;
        //public LinearLayout

        public MyAdapterJsonList(Activity context, Notary[] arrayNotary) {
            super(context, R.layout.notary_item_list, arrayNotary);
            this.context = context;
            this.arrayNotary = arrayNotary;

            Log.d(LOG_TAG, "Adapter START");
        }

        public int dpToPx(int dp) {
            DisplayMetrics displayMetrics = Notary_list.this.getResources().getDisplayMetrics();
            int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return px;
        }

        public LinearLayout lila_item_notary_adres;
        public Boolean bool = true;

        public void start_map() {
            in.set_jut_ili_not(false);
            Log.d(LOG_TAG, "для натариуса труе " + in.get_jut_ili_not());
            Intent intent = new Intent(Notary_list.this, Map_coor.class);
            startActivity(intent);
        }

        public void start_profile_not(int i) {
            Intent intent = new Intent(Notary_list.this, Notary_profile.class);
            startActivity(intent);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.notary_item_list, parent, false);//null;


            TextView tv_item_notary_fio = (TextView) rowView.findViewById(R.id.tv_item_notary_fio);
            TextView tv_item_notary_adres_text = (TextView) rowView.findViewById(R.id.tv_item_notary_adres_text);
            TextView tv_item_notary_phone_text = (TextView) rowView.findViewById(R.id.tv_item_notary_phone_text);
            TextView tv_item_notary_holiday = (TextView) rowView.findViewById(R.id.tv_item_notary_holiday);
            ImageView iv_item_notary_vyezd = (ImageView) rowView.findViewById(R.id.iv_item_notary_vyezd);
            ImageView iv_item_notary_zapic = (ImageView) rowView.findViewById(R.id.iv_item_notary_zapic);

            tv_item_notary_fio.setText(arrayNotary[position].Fio);
            try{
                tv_item_notary_phone_text.setText(arrayNotary[position].Phone);
            }catch (Exception e){
            }

            tv_item_notary_adres_text.setText(arrayNotary[position].Address2);

            try {
                //tv_item_notary_phone_text.setText(arrayNotary[position].Contacts.Phone);
            } finally {

            }

            if (arrayNotary[position].WorkInOffDays) {
                tv_item_notary_holiday.setText("пн-сб");
            } else {
                tv_item_notary_holiday.setText("пн-пт");
            }
            if (arrayNotary[position].Equipage) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_exit_not_no);
                iv_item_notary_vyezd.setImageBitmap(bitmap);
            } else {
                iv_item_notary_vyezd.setBackgroundResource(R.drawable.exit_not_no);
            }
            if (arrayNotary[position].Appointments) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ain_ic_docrequest);
                iv_item_notary_zapic.setImageBitmap(bitmap);
            }



            //LinearLayout lila_item_notaryAdres = (LinearLayout)rowView.findViewById(R.id.lila_item_notary_adres);
            //lila_item_notaryAdres.setLayoutParams(
            // new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(300), (float) 0));

            TextView tv = (TextView) rowView.findViewById(R.id.tv_item_notary_fio);

            //tv.setText("qweqwrqwr");

            Button but_item_notary = (Button) rowView.findViewById(R.id.item_but_notary);
            but_item_notary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String sm[] = new String[]{
                            arrayNotary[position].Fio,
                            "",//String.valueOf(arrayNotary[position].Address.City),
                            arrayNotary[position].Phone,
                            "",//arrayNotary[position].Contacts.Email,
                            arrayNotary[position].Address2,
                            "",//arrayNotary[position].Contacts.Site,
                            String.valueOf(arrayNotary[position].Appointments),//"false",//arrayNotary[position].list.get(position).getString("predvzapis"),
                            String.valueOf(arrayNotary[position].WorkInOffDays),
                            String.valueOf(arrayNotary[position].AppointmentsEmail),//"false",//arrayNotary[position].list.get(position).getString("zakazpoemail"),
                            String.valueOf(arrayNotary[position].Equipage),
                            String.valueOf(arrayNotary[position].WorkWithJur),//"false",//arrayNotary[position].list.get(position).getString("urlica"),
                            String.valueOf(arrayNotary[position].IsSitesCertification),
                            String.valueOf(arrayNotary[position].IsPleadtingHereditaryCases),
                            String.valueOf(arrayNotary[position].LockDocuments),//"false",//arrayNotary[position].list.get(position).getString("fixdokaz"),
                            String.valueOf(arrayNotary[position].RequestsAndDuplicate),//"false",//arrayNotary[position].list.get(position).getString("duplicat"),
                            String.valueOf(arrayNotary[position].DepositsReception),
                            String.valueOf(arrayNotary[position].Id),
                            String.valueOf(arrayNotary[position].Latitude),//"-1",//arrayNotary[position].list.get(position).getString("X"),
                            String.valueOf(arrayNotary[position].Longitude)//"-1",//arrayNotary[position].list.get(position).getString("Y")
                    };
                    try{
                        sm[2] = arrayNotary[position].Phone;
                        sm[3] = arrayNotary[position].Email;
                        sm[5] = arrayNotary[position].Site;
                    }catch (Exception e){

                    }
                    in.set_not_prof(sm);

                    start_profile_not(position);
                    //lila_item_notary_adres = (LinearLayout)rowView.findViewById(R.id.lila_item_notary_adres);
                    //lila_item_notary_adres.setBackgroundResource(R.color.black_m);

                    //TextView r = (TextView)rowView.findViewById(R.id.tv_item_notary_adres_text);
                    //r.setText("d");
                    //r.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 0));
                    //lila_item_notary_adres.setLayoutParams(
                    // new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(165), (float) 0));
                }
            });

            Button but_item_notary_adres = (Button) rowView.findViewById(R.id.but_item_notary_adres);
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

            ImageButton not_but_maps = (ImageButton) rowView.findViewById(R.id.not_but_maps);
            not_but_maps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    in.set_latitude(0);
                    in.set_longitude(0);
                    TextView tv_fio = (TextView) rowView.findViewById(R.id.tv_item_notary_fio);
                    TextView tv_adres = (TextView) rowView.findViewById(R.id.tv_item_notary_adres_text);
                    in.set_text(String.valueOf(tv_adres.getText()));
                    in.set_fio_jur(String.valueOf(tv_fio.getText()));
                    in.set_jut_ili_not(false);

                    start_map();
                    //Intent intent = new Intent(Notary_list.this, Map_coor.class);
                    //startActivity(intent);
                    //lila_item_notary_adres = (LinearLayout)rowView.findViewById(R.id.lila_item_notary_adres);
                    //lila_item_notary_adres.setBackgroundResource(R.color.black_m);

                    //TextView r = (TextView)rowView.findViewById(R.id.tv_item_notary_adres_text);
                    //r.setText("d");
                    //r.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 0));
                    //lila_item_notary_adres.setLayoutParams(
                    // new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(165), (float) 0));
                }
            });

            //searchView_notar.setQuery("", false);
           // searchView_notar.clearFocus();
          //  searchView_notar.setIconified(true);
            return rowView;
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
                    .header("Authorization", in.get_token_type() + " " + in.get_token())
                    .url("http://" + in.get_url() + "/Notaries/GetNotaries")
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
            mcArrayNotary = gson.fromJson(result, Notary[].class);
            start_s_server();
            super.onPostExecute(result);
        }
    }

    class UrlConnectionTaskFilter extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();

            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            String s1 = in.get_token_type() + " " + in.get_token();
            String s2 = "http://" + in.get_url() + "/Notaries/GetNotaryByFilter";
            String s3 = params[0];
            String s4 = "sdf";
            String s5 = "sdf";


            /*
             {"Appointments":false,
             "AppointmentsEmail":false,
             "Consultation":false,
             "DepositsReception":false,
             "Equipage":true,
             "IsPleadtingHereditaryCases":false,
             "IsSitesCertification":false,
             "Latitude":-1.0,
             "LockDocuments":false,
             "Longitude":-1.0,
             "Radius":0,
             "RequestsAndDuplicate":false,
             "SortingType":"Name",
             "Transactions":false,
             "WorkInOffDays":false,
             "WorkWithJur":false}

             {"Longitude":0.0,
             "Latitude":0.0,
             "Radius":0,
             "SortingType":0,
             "City":null,
             "Appointments":false,
             "WorkWithJur":false,
             "Transactions":false,
             "RequestsAndDuplicate":false,
             "LockDocuments":false,
             "Consultation":false,
             "WorkInOffDays":false,
             "Equipage":true,
             "AppointmentsEmail":false,
             "IsPleadtingHereditaryCases":false,
             "IsSitesCertification":false,
             "DepositsReception":false}
            */



            //RequestBody formBody = RequestBody.create(JSON, json_signup);
            Request request = null;
            if (in.get_filter_tip() == 0) {
                request = new Request.Builder()
                        .header("Authorization", in.get_token_type() + " " + in.get_token())
                        .url("http://" + in.get_url() + "/Notaries/GetNotaryByFilter")
                        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, params[0]))
                        .build();
            }else {
                request = new Request.Builder()
                        .header("Authorization", in.get_token_type() + " " + in.get_token())
                        .url("http://" + in.get_url() + "/Notaries/GetNotaryByAddressCoordinates")
                        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, params[0]))
                        .build();
            }

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
            Gson gson = new Gson();
            if (result != null && code >= 200 && code < 300) {

                //SEARCH заполнение списка с ФИО нотариусов
                mcSearchNotary = gson.fromJson(result, Notary[].class);
                mcArrayNotary = gson.fromJson(result, Notary[].class);
                start_s_server();
            }
            super.onPostExecute(result);
        }
    }


    class AsyncTaskNotary1 extends AsyncTask<Void, Integer, String> {
        // фоновая работа
        @Override
        protected String doInBackground(Void... params) {
            String str_url = "http://" + in.get_url() + "/123.notget";
            String json = "";
            JSONObject jsonObject = new JSONObject();

            FindNotaryByCoordinatesFilter notary = new FindNotaryByCoordinatesFilter();
            notary.WorkInOffDays = bool_sort[2];
            notary.Equipage = bool_sort[3];
            notary.Appointments = bool_sort[4];
            notary.AppointmentsEmail = bool_sort[5];
            notary.WorkWithJur = bool_sort[6];
            notary.IsPleadtingHereditaryCases = bool_sort[7];
            notary.IsSitesCertification = bool_sort[8];
            notary.LockDocuments = bool_sort[9];
            notary.DepositsReception = bool_sort[10];
            notary.RequestsAndDuplicate = bool_sort[11];
            notary.Transactions = bool_sort[12];

            notary.Latitude = in.get_latitude();
            notary.Longitude = in.get_longitude();
            notary.SortingType = FindNotaryByCoordinatesFilter.sortingType.Name;

            try {
                jsonObject.put("idu", in.get_id_user());//String.valueOf(in.get_id()));
                jsonObject.put("idj", in.get_id_jur()); //in.get_id_jur() );
                jsonObject.put("password", in.get_password());//adin.get_password_jur());
                if (bool_sort[2]) {
                    jsonObject.put("holiday", 1);//adin.get_password_jur());
                } else {
                    jsonObject.put("holiday", 0);//adin.get_password_jur());
                }
                if (bool_sort[3]) {
                    jsonObject.put("vyezd", 1);//adin.get_password_jur());
                } else {
                    jsonObject.put("vyezd", 0);//adin.get_password_jur());
                }
                if (bool_sort[4]) {
                    jsonObject.put("predvzapis", 1);//adin.get_password_jur());
                } else {
                    jsonObject.put("predvzapis", 0);//adin.get_password_jur());
                }
                if (bool_sort[5]) {
                    jsonObject.put("zakazpoemail", 1);//adin.get_password_jur());
                } else {
                    jsonObject.put("zakazpoemail", 0);//adin.get_password_jur());
                }
                if (bool_sort[6]) {
                    jsonObject.put("urlica", 1);//adin.get_password_jur());
                } else {
                    jsonObject.put("urlica", 0);//adin.get_password_jur());
                }
                if (bool_sort[7]) {
                    jsonObject.put("nasledstvo", 1);//adin.get_password_jur());
                } else {
                    jsonObject.put("nasledstvo", 0);//adin.get_password_jur());
                }
                if (bool_sort[8]) {
                    jsonObject.put("zaversite", 1);//adin.get_password_jur());
                } else {
                    jsonObject.put("zaversite", 0);//adin.get_password_jur());
                }
                if (bool_sort[9]) {
                    jsonObject.put("fixdokaz", 1);//adin.get_password_jur());
                } else {
                    jsonObject.put("fixdokaz", 0);//adin.get_password_jur());
                }
                if (bool_sort[10]) {
                    jsonObject.put("deposit", 1);//adin.get_password_jur());
                } else {
                    jsonObject.put("deposit", 0);//adin.get_password_jur());
                }
                if (bool_sort[11]) {
                    jsonObject.put("duplicat", 1);//adin.get_password_jur());
                } else {
                    jsonObject.put("duplicat", 0);//adin.get_password_jur());
                }
                if (bool_sort[12]) {
                    jsonObject.put("sdelki", 1);//adin.get_password_jur());
                } else {
                    jsonObject.put("sdelki", 0);//adin.get_password_jur());
                }
                jsonObject.put("x", in.get_latitude());//adin.get_password_jur());
                jsonObject.put("y", in.get_longitude());//adin.get_password_jur());
                jsonObject.put("city", in.get_id_city());//adin.get_password_jur());
                jsonObject.put("fio", SEARCH_NOT);//adin.get_password_jur());
                jsonObject.put("po_of_site", "'" + String.valueOf(b1_sort) + "'");//adin.get_password_jur());
                jsonObject.put("po_consult", "'" + String.valueOf(b2_sort) + "'");//adin.get_password_jur());
                jsonObject.put("sort", sort_po);//adin.get_password_jur());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            json = String.valueOf(jsonObject);

            Log.d(LOG_TAG, "сор " + json);
            return ServerSendData.sendRegData(str_url, json);
        }

        // выполняется после doInBackground, имеет доступ к UI
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.d(LOG_TAG, "сортировка или нет " + result);
                start_s_server();
            } else {
                Log.d(LOG_TAG, "снет " + result);
            }
        }
    }

    public static class ServerSendData {

        public static String sendRegData(String stringUrl, String json_user_str) {

            String result = null;
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
                if (xNew > 300) {
                    Intent intent = new Intent(Notary_list.this, New_sidebar.class);
                    startActivity(intent);
                }
                Log.d("qwerty", "ACTION_UP");
                break;
            case (MotionEvent.ACTION_DOWN):
                xX = event.getX();
                Log.d("qwerty", "ACTION_DOWN xX " + xX);
                break;
            case (MotionEvent.ACTION_MOVE):
                Log.d("qwerty", "ACTION_MOVE ");
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
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
        xNew = xX - _x;
    }
}

/*
//--------------------------------------------------------------filter
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];
        Button button = (Button) findViewById(R.id.notary_but_filter);

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        button.getLocationOnScreen(location);
        view_height = button.getHeight();

        // Initialize the Point with x, and y positions
        point = new Point();
        point.x = location[0];
        point.y = location[1];
    }

    private void openDialogFilter(final Activity context, Point p){
        Log.d(LOG_TAG, "start filter_1");
        /*LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.advocate_dialog_filter, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.showAsDropDown(lila_sort_panel, 50, -30);/
Button button = (Button)findViewById(R.id.notary_but_filter);
    int popupWidth = button.getWidth();
//int popupHeight = 270;
Log.d(LOG_TAG, "start filter_2");
        LayoutInflater layoutInflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = layoutInflater.inflate(R.layout.notary_dialog_filter, null);

        Log.d(LOG_TAG, "start filter_3");
// Creating the PopupWindow
final PopupWindow popup = new PopupWindow(layout,
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        Log.d(LOG_TAG, "start filter_4");
        //popup.setHeight(popupHeight);
        popup.setFocusable(true);
        popup.showAsDropDown(button);
        Log.d(LOG_TAG, "start filter_5");
        // Some offset to align the popup a bit to the right, and a bit down,
        // relative to button's position.
        int OFFSET_X = 0;
        int OFFSET_Y = view_height - 5;

final LinearLayout lila_spec = (LinearLayout)layout.findViewById(R.id.filter_lila_spec);
        Log.d(LOG_TAG, "start filter_5");
        CheckBox filter_check_spc1 = (CheckBox)layout.findViewById(R.id.filter_not_check_1);
        CheckBox filter_check_spc2 = (CheckBox)layout.findViewById(R.id.filter_not_check_2);
        CheckBox filter_check_spc3 = (CheckBox)layout.findViewById(R.id.filter_not_check_3);
        CheckBox filter_check_spc4 = (CheckBox)layout.findViewById(R.id.filter_not_check_4);
        CheckBox filter_check_spc5 = (CheckBox)layout.findViewById(R.id.filter_not_check_5);
        CheckBox filter_check_spc6 = (CheckBox)layout.findViewById(R.id.filter_not_check_6);
        CheckBox filter_check_spc7 = (CheckBox)layout.findViewById(R.id.filter_not_check_7);
        CheckBox filter_check_spc8 = (CheckBox)layout.findViewById(R.id.filter_not_check_8);
        CheckBox filter_check_spc9 = (CheckBox)layout.findViewById(R.id.filter_not_check_9);
        CheckBox filter_check_spc10 = (CheckBox)layout.findViewById(R.id.filter_not_check_10);
        Log.d(LOG_TAG, "start filter_6");
final CheckBox [] check_spc = new CheckBox[]{filter_check_spc1 , filter_check_spc2, filter_check_spc3,filter_check_spc4,
        filter_check_spc5,filter_check_spc6,filter_check_spc7,filter_check_spc8,filter_check_spc9,filter_check_spc10};

        for(int i = 0; i<check_spc.length; i++){
        check_spc[i].setChecked(bool_sort[i]);
        }
        Log.d(LOG_TAG, "start filter_7");
        /*for(int i = 0; i<check_spc.length; i++){
            check_spc[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    for(int j = 0; j<check_spc.length; j++) {

                        check_spc[j].setChecked(false);

                        buttonView.setChecked(isChecked);
                    }
                }
            });
        }/
        // Clear the default translucent background

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(button, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
        //popupWindow.showAsDropDown(butOpenPopup, 200, -15);

        // Getting a reference to Close button, and close the popup when
        // clicked.

        /*Button str_spec = (Button) layout.findViewById(R.id.filter_but_not_spec);
        str_spec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(ff == true){
                    lila_spec.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    ff=false;
                }else{
                    lila_spec.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
                    ff=true;
                }
            }
        });/

        Button close = (Button) layout.findViewById(R.id.filter_but_not_close);
        close.setOnClickListener(new View.OnClickListener() {

@Override
public void onClick(View v) {
        ff = true;
        popup.dismiss();
        }
        });

        Button add_filter = (Button) layout.findViewById(R.id.filter_but_not_start_sort);
        add_filter.setOnClickListener(new View.OnClickListener() {

@Override
public void onClick(View v) {
        for(int i = 0; i<bool_sort.length; i++){
        bool_sort[i]= check_spc[i].isChecked();
        }
        ff = true;
        new AsyncTaskNotary().execute();
        popup.dismiss();
        }
        });

        }
//______________________________________________________________filter

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

final Dialog dialog = new Dialog(Notary_list.this);
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
        new AsyncTaskNotary().execute();
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
        notary_tv_sort_city.setText(str_city[pos]);
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

                    ArrayAdapter<String> adapter_city = new ArrayAdapter<String>(Notary_list.this,
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
*/