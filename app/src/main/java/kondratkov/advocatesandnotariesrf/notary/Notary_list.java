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
import android.location.LocationManager;
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
import kondratkov.advocatesandnotariesrf.maps.LocationListenerGPServices;
import kondratkov.advocatesandnotariesrf.maps.Map_coor;
import kondratkov.advocatesandnotariesrf.maps.Map_jur_and_notary;

public class Notary_list extends Activity implements View.OnTouchListener,
        SearchView.OnQueryTextListener {

    String LOG_TAG = "qwerty_not";


    public String SEARCH_NOT = "";
    public SearchView searchView_notar;

    public ListView listViewnotary;

    public LinearLayout lila_sort_panel, jurList_lila_sity, lila_sort_panel_down_tw;
    boolean b1 = true, b2 = true;


    public IN in = new IN();
    public int code;

    public boolean[] bool_sort;

    public Button but_sort_not_list1, but_sort_not_list2;
    public CheckBox checkBox_list_not1, checkBox_list_not2;
    public boolean b1_sort = false, b2_sort = false, b3_sort = false, b4_sort = false, b5_update=false;


    public Notary[] mcArrayNotary= null;
    public Notary[] mcSearchNotary = null;
    public Notary[] mcArrayOil = null;
    public LocationManager locationManager;
    /**
     * для поиска по ФИО
     */

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
                if(isChecked){
                    mcArrayOil = mcArrayNotary;
                    checkBox_list_not1.setTextColor(getResources().getColor(R.color.color_button_orange));
                }else {
                    mcArrayNotary =mcArrayOil;
                    checkBox_list_not1.setTextColor(getResources().getColor(R.color.color_button_gray));
                }
                start_s_server();
            }
        });
        checkBox_list_not2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                b2_sort = isChecked;
                if(isChecked){
                    mcArrayOil = mcArrayNotary;
                    checkBox_list_not2.setTextColor(getResources().getColor(R.color.color_button_orange));
                }else {
                    mcArrayNotary =mcArrayOil;
                    checkBox_list_not2.setTextColor(getResources().getColor(R.color.color_button_gray));
                }
                start_s_server();
            }
        });

        bool_sort = getIntent().getBooleanArrayExtra("SEARCH_NOTARY_ARRAY_BOOL");

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

        lila_sort_panel = (LinearLayout) findViewById(R.id.notary_lila_sort_panel);
        jurList_lila_sity = (LinearLayout) findViewById(R.id.notary_lila_city);

        setupSearchView();

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
        super.onStart();
        client.connect();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListenerGPServices l = new LocationListenerGPServices();
        l.start_my(locationManager, getApplicationContext());

        if (in.getOnemay()) {
        } else {
            Notary_list.this.finish();
        }
        in.setChoice_of_menus(8);
        listViewnotary.setOnTouchListener(this);


        mcSearchNotary =IN.RESULT_LIST_NOTARY;
        mcArrayNotary = mcSearchNotary;
        start_s_server();

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
                    b5_update = true;
                } else {
                    b3_sort = true;
                    b4_sort = false;
                }
                sort_po_void();
                break;
            case R.id.but_sort_not_list2:
                if (b4_sort) {
                    b4_sort = false;
                    b5_update = true;
                } else {
                    b4_sort = true;
                    b3_sort = false;
                }
                sort_po_void();
                break;
            case R.id.but_notarys_list_to_map:
                IN.ARRAY_NOTARY_TO_MAP = mcArrayNotary;
                Intent intent_sidebar = new Intent(Notary_list.this, Map_jur_and_notary.class);
                intent_sidebar.putExtra("TYPE", 1);
                intent_sidebar.putExtra("ARRAY", "");
                intent_sidebar.putExtra("ANSWERNEXT", false);
                startActivity(intent_sidebar);
                break;
        }
    }

    public boolean ff = true;

    public void sort_po_void() {
        if (b3_sort) {
            but_sort_not_list1.setTextColor(Color.argb(255, 255, 255, 255));
            but_sort_not_list2.setTextColor(Color.argb(80, 255, 255, 255));
        } else if (b4_sort) {
            but_sort_not_list1.setTextColor(Color.argb(80, 255, 255, 255));
            but_sort_not_list2.setTextColor(Color.argb(255, 255, 255, 255));
        } else {
            but_sort_not_list1.setTextColor(Color.argb(80, 255, 255, 255));
            but_sort_not_list2.setTextColor(Color.argb(80, 255, 255, 255));
        }
        start_s_server();
    }


    public void start_s_server() {

        Notary [] notaries;

        String notary_city_search = (String)getIntent().getSerializableExtra("CITY_SEARCH");
        if(b5_update){
            b5_update = false;
            mcArrayNotary = IN.RESULT_LIST_NOTARY;
        }

        List<Notary>notariesList = new ArrayList<Notary>();
        if(notary_city_search.length() > 0){

            for(int i = 0; i<mcArrayNotary.length; i++){
                if(mcArrayNotary[i].Address.City.equals(notary_city_search)){
                    notariesList.add(mcArrayNotary[i]);
                }
            }

            notaries = new Notary[notariesList.size()];
            for(int i =0; i<notariesList.size(); i++){
                notaries[i] = notariesList.get(i);
            }
            mcArrayNotary = notaries;
        }

        if(b1_sort){
            notariesList = new ArrayList<Notary>();
            for(int i = 0; i<mcArrayNotary.length; i++){
                String s = mcArrayNotary[i].Site;
                try{
                    if(mcArrayNotary[i].Site.equals("нет") == false){
                        notariesList.add(mcArrayNotary[i]);
                    }
                }catch (Exception e){
                }
            }

            notaries = new Notary[notariesList.size()];
            for(int i =0; i<notariesList.size(); i++){
                notaries[i] = notariesList.get(i);
            }
            mcArrayNotary = notaries;
        }

        if(b2_sort){
            notariesList = new ArrayList<Notary>();
            for(int i = 0; i<mcArrayNotary.length; i++){
                String s = mcArrayNotary[i].Site;
                if(mcArrayNotary[i].Consultation){
                    notariesList.add(mcArrayNotary[i]);
                }
            }

            notaries = new Notary[notariesList.size()];
            for(int i =0; i<notariesList.size(); i++){
                notaries[i] = notariesList.get(i);
            }
            mcArrayNotary = notaries;
        }

        if(b3_sort){
            ArrayList<Notary> notariesListSort = new ArrayList<Notary>();
            for(int i = 0; i<mcArrayNotary.length; i++){
                notariesListSort.add(mcArrayNotary[i]);
            }

            notariesListSort = Notary_list_sort.sortListBy(notariesListSort, Notary_list_sort.By.PRICE);
            mcArrayNotary= new Notary[notariesListSort.size()];
            for(int i = 0; i<notariesListSort.size(); i++){

                mcArrayNotary[i] = notariesListSort.get(i);
            }
        }else if(b4_sort){
            ArrayList<Notary> notariesListSort = new ArrayList<Notary>();
            for(int i = 0; i<mcArrayNotary.length; i++){
                notariesListSort.add(mcArrayNotary[i]);
            }

            notariesListSort = Notary_list_sort.sortListBy(notariesListSort, Notary_list_sort.By.RETING);
            mcArrayNotary= new Notary[notariesListSort.size()];
            for(int i = 0; i<notariesListSort.size(); i++){

                mcArrayNotary[i] = notariesListSort.get(i);
            }
        }

        try{
            if(mcArrayNotary != null){
                MyAdapterJsonList mam = new MyAdapterJsonList(this, mcArrayNotary);
                listViewnotary.setAdapter(mam);

                listViewnotary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position,
                                            long id) {

                        if(position != 0){
                            Intent int3 = new Intent(Notary_list.this, Notary_profile.class);
                            startActivity(int3);
                        }

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
        }catch (Exception e) {
        }
    }

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

    // Класс для сохранения во внешний класс и для ограничения доступа
    // из потомков класса
    static class ViewHolder {
        TextView tv_item_notary_fio;
        TextView tv_item_notary_adres_text;
        TextView tv_item_notary_phone_text;
        TextView tv_item_notary_holiday;
        TextView tv_item_notary_distance;
        ImageView iv_item_notary_vyezd;
        ImageView iv_item_notary_zapic;

        Button but_item_notary;
        ImageButton not_but_phone;
        Button but_item_notary_adres;
        ImageButton not_but_maps;

        LinearLayout lila_item_notary_adres;
    }



    class MyAdapterJsonList extends ArrayAdapter {

        private Notary[] arrayNotary = null;
        private Activity context;
        //public LinearLayout

        public MyAdapterJsonList(Activity context, Notary[] arrayNotary) {
            super(context, R.layout.notary_item_list, arrayNotary);
            this.context = context;
            this.arrayNotary = arrayNotary;

            //Log.d(LOG_TAG, "Adapter START");
        }

        public int dpToPx(int dp) {
            DisplayMetrics displayMetrics = Notary_list.this.getResources().getDisplayMetrics();
            int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return px;
        }

        public Boolean bool = true;

        public void start_profile_not(int i) {
            Intent intent = new Intent(Notary_list.this, Notary_profile.class);
            startActivity(intent);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            // ViewHolder буферизирует оценку различных полей шаблона элемента

            final ViewHolder holder;

            // Очищает сущетсвующий шаблон, если параметр задан
            // Работает только если базовый шаблон для всех классов один и тот же
            View rowView = convertView;

            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.notary_item_list, parent, false);//null;
                holder = new ViewHolder();

                //holder.textView = (TextView) rowView.findViewById(R.id.label);
                //holder.imageView = (ImageView) rowView.findViewById(R.id.icon);
                holder.tv_item_notary_fio = (TextView) rowView.findViewById(R.id.tv_item_notary_fio);
                holder.tv_item_notary_adres_text = (TextView) rowView.findViewById(R.id.tv_item_notary_adres_text);
                holder.tv_item_notary_phone_text = (TextView) rowView.findViewById(R.id.tv_item_notary_phone_text);
                holder.tv_item_notary_holiday = (TextView) rowView.findViewById(R.id.tv_item_notary_holiday);
                holder.iv_item_notary_vyezd = (ImageView) rowView.findViewById(R.id.iv_item_notary_vyezd);
                holder.iv_item_notary_zapic = (ImageView) rowView.findViewById(R.id.iv_item_notary_zapic);
                holder.tv_item_notary_distance = (TextView) rowView.findViewById(R.id.tv_item_notary_distance);


                holder.but_item_notary = (Button) rowView.findViewById(R.id.item_but_notary);
                holder.but_item_notary_adres = (Button) rowView.findViewById(R.id.but_item_notary_adres);
                holder.not_but_maps = (ImageButton) rowView.findViewById(R.id.not_but_maps);
                holder.not_but_phone = (ImageButton)rowView.findViewById(R.id.not_but_phone);
                holder.lila_item_notary_adres = (LinearLayout) rowView.findViewById(R.id.lila_item_notary_adres);

                rowView.setTag(holder);
            } else {
                holder = (ViewHolder) rowView.getTag();
            }

            holder.tv_item_notary_fio.setText(arrayNotary[position].Fio);
            try {
                holder.tv_item_notary_phone_text.setText(arrayNotary[position].Phone);
            } catch (Exception e) {
            }

            holder.tv_item_notary_adres_text.setText(arrayNotary[position].Address2);

            try {
                //tv_item_notary_phone_text.setText(arrayNotary[position].Contacts.Phone);
            } finally {

            }

            if (arrayNotary[position].WorkInOffDays) {
                holder.tv_item_notary_holiday.setText("пн-сб");
            } else {
                holder.tv_item_notary_holiday.setText("пн-пт");
            }
            if (arrayNotary[position].Equipage) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_exit_not_no);
                holder.iv_item_notary_vyezd.setImageBitmap(bitmap);
            } else {
                holder.iv_item_notary_vyezd.setBackgroundResource(R.drawable.exit_not_no);
            }
            if (arrayNotary[position].Appointments) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ain_ic_docrequest);
                holder.iv_item_notary_zapic.setImageBitmap(bitmap);
            }

            try{
                holder.tv_item_notary_distance.setText(
                        in.d_to_sDistance(arrayNotary[position].Distance)
                );
            }catch (Exception e){
                holder.tv_item_notary_distance.setText("");
            }

            holder.but_item_notary.setOnClickListener(new View.OnClickListener() {
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
                    try {
                        sm[2] = arrayNotary[position].Phone;
                        sm[3] = arrayNotary[position].Email;
                        sm[5] = arrayNotary[position].Site;
                    } catch (Exception e) {

                    }
                    in.set_not_prof(sm);

                    start_profile_not(position);
                }
            });


            holder.but_item_notary_adres.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (bool) {
                        Log.d(LOG_TAG, "position " + position + " " + bool);
                        holder.lila_item_notary_adres.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 0));
                        bool = false;
                    } else {
                        Log.d(LOG_TAG, "position " + position + " " + bool);
                        holder.lila_item_notary_adres.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(2), (float) 0));
                        bool = true;
                    }
                }
            });

            holder.not_but_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent intent12 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ holder.tv_item_notary_phone_text.getText()));
                        startActivity(intent12);
                    }catch (Exception e){}
                }
            });


            holder.not_but_maps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    in.set_text(String.valueOf(holder.tv_item_notary_adres_text.getText()));
                    in.set_fio_jur(String.valueOf(holder.tv_item_notary_fio.getText()));
                    in.set_jut_ili_not(false);


                    in.set_jut_ili_not(false);
                    Log.d(LOG_TAG, "для натариуса труе " + in.get_jut_ili_not());

                    double d_latitude = 0;
                    double d_longitude = 0;

                    try{
                        if(arrayNotary[position].Latitude == 0 || arrayNotary[position].Latitude == 0){
                        }else{
                            d_latitude = arrayNotary[position].Latitude;
                            d_longitude = arrayNotary[position].Longitude;
                        }
                    }catch (Exception e){}

                    Intent intent = new Intent(Notary_list.this, Map_coor.class);
                    intent.putExtra("THIS_LATITUDE", d_latitude);
                    intent.putExtra("THIS_LONGITUDE", d_longitude);
                    startActivity(intent);
                }
            });

            return rowView;
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

                break;
            case (MotionEvent.ACTION_DOWN):
                xX = event.getX();

                break;
            case (MotionEvent.ACTION_MOVE):

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
