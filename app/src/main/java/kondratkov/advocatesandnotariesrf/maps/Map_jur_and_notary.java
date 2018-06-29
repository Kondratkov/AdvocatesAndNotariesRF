package kondratkov.advocatesandnotariesrf.maps;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.Sidebar;
import kondratkov.advocatesandnotariesrf.account.Bup;
import kondratkov.advocatesandnotariesrf.advocates.Advocate_profile;
import kondratkov.advocatesandnotariesrf.api_classes.ClientQuestion;
import kondratkov.advocatesandnotariesrf.api_classes.Filter.FindByCoordinatesFilter;
import kondratkov.advocatesandnotariesrf.api_classes.Filter.FindJuristFilter;
import kondratkov.advocatesandnotariesrf.api_classes.JuristAccounClass;
import kondratkov.advocatesandnotariesrf.api_classes.Notary;
import kondratkov.advocatesandnotariesrf.notary.Notary_profile;

public class Map_jur_and_notary extends Activity implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,SeekBar.OnSeekBarChangeListener {
    private static final String DEBUG_TAG = "qwerty";

    public String json_s ="";
    public ListView listViewMap;
    public JSONArray jsonArrayMap = null;
    public JSONObject jsonObjectMap = null;
    public List<JSONObject> listMap = null;
    public TextView tv1, tv2;

    public EditText et_dist;
    public SeekBar map_seekBar;

    public IN in = new IN();
    private GoogleMap map1;
    public LatLng mark_jur;

    public LinearLayout lila_map_filter, lilamapone;
    public Boolean boolFilter = false;

    public Marker marker;

    public JuristAccounClass[] mcArrayJuristAccoun;
    public Notary[] mcArrayNotary;

    public int Search_id_city;
    public int Search_id_JuristAreas;
    public String Search_specialization;
    public String s1, s2;
    public Bup Search_bup;
    public FindJuristFilter.sortingType Search_sortingType1;
    public FindByCoordinatesFilter.sortingType Search_sortingType2;
    public String Search_surename;
    public boolean Search_IsOnline;
    public boolean Search_WorkInOffDays;
    public boolean Search_CanFastComing;

    public boolean sort_bool_fuck[] ;
    public boolean b_answer_next = false;
    public int id_notary;

    //--S----L--O--C----------------------------------------------------
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private static final long MINIMUM_DISTANCE_FOR_UPDATES = 1000; // в метрах
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 360000; // в мс
    //--F----L--O--C----------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_jur_and_notary);
    }

    @Override
    protected void onStart() {
        super.onStart();
        in.setChoice_of_menus(7);

        //--S----L--O--C----------------------------------------------------
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //mLocationListener = new MyLocationListener();
        //--F----L--O--C----------------------------------------------------

        lila_map_filter = (LinearLayout) findViewById(R.id.lila_map_filter);
        lilamapone = (LinearLayout)findViewById(R.id.lilamapone);

        tv1 = (TextView)findViewById(R.id.tv_m_adress);
        tv2 = (TextView)findViewById(R.id.tv_m_dopinfo);

        et_dist = (EditText)findViewById(R.id.et_dist);
        map_seekBar = (SeekBar)findViewById(R.id.map_seekBar);
        map_seekBar.setOnSeekBarChangeListener(this);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment1 = (MapFragment) fragmentManager
                .findFragmentById(R.id.fragment_map);
        map1 = mapFragment1.getMap();
        // выбираем тип карты
        map1.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map1.setMyLocationEnabled(true);

        LocationListenerGPServices l = new LocationListenerGPServices();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        l.start_my(lm, Map_jur_and_notary.this);

        mark_jur = new LatLng(IN.latitude_my, IN.longitude_my);
        map1.moveCamera(CameraUpdateFactory.newLatLngZoom(mark_jur, 10));

        //setDataMap(json_maps);
        Gson gson = new Gson();

        if(getIntent().getIntExtra("TYPE", 0)==0){
            b_answer_next = getIntent().getBooleanExtra("ANSWERNEXT", false);
            mcArrayJuristAccoun = gson.fromJson((String)getIntent().getSerializableExtra("ARRAY"), JuristAccounClass[].class);
        }else{
            mcArrayNotary = IN.ARRAY_NOTARY_TO_MAP;
        }

        start_activity();

        map1.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(DEBUG_TAG, "onMapClick: " + latLng.latitude + "," + latLng.longitude);

            }
        });

        map1.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.d(DEBUG_TAG, "onMapLongClick: " + latLng.latitude + "," + latLng.longitude);
            }
        });

        map1.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition camera) {
                Log.d(DEBUG_TAG, "onCameraChange: " + camera.target.latitude + "," + camera.target.longitude);


                double l_x = in.get_latitude()-camera.target.latitude;
                double l_y = in.get_longitude()-camera.target.longitude;


                if(Math.abs(l_x)>0.0002||Math.abs(l_y)>0.0002){
                    //in.set_latitude(camera.target.latitude);
                  // in.set_longitude(camera.target.longitude);
                   // in.set_text(String.valueOf(et_dist.getText()));

                    //mark_jur = new LatLng(in.get_latitude(), in.get_longitude());
                    //marker = map1.addMarker(new MarkerOptions().title("dfdfdf").position(mark_jur));

                    if(boolFilter){
                        lila_map_filter.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(200), dpToPx(250), (float) 0));
                        boolFilter= false;
                    }else {Log.d("qwerty", "nado li popo22222 ");
                        lilamapone.setLayoutParams(
                                new LinearLayout.LayoutParams(dpToPx(1), dpToPx(250), (float) 0));
                    }

                    //new AsyncTaskMaps().execute();
                }
                //mark_jur = new LatLng(in.get_latitude(), in.get_longitude());
                //map1.moveCamera(CameraUpdateFactory.newLatLngZoom(mark_jur, 17));
            }
        });
    }

    public void start_activity(){

        add_mark_maps();
        //setDataMap(json_maps);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // TODO Auto-generated method stub
        et_dist.setText(String.valueOf(seekBar.getProgress()*250));
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    public void onClickMap(View v) {
        switch (v.getId()) {
            case R.id.but_map_filter:
                if (boolFilter == true) {
                    lila_map_filter.setLayoutParams(
                            new LinearLayout.LayoutParams(dpToPx(270), dpToPx(400), (float) 0));
                    Animation anim = AnimationUtils.loadAnimation(Map_jur_and_notary.this, R.anim.trans_map_filter_on);
                    lila_map_filter.startAnimation(anim);

                    boolFilter = false;
                } else {
                    Animation anim1 = AnimationUtils.loadAnimation(Map_jur_and_notary.this, R.anim.trans_map_filter_off);
                    lila_map_filter.startAnimation(anim1);

                    lilamapone.setLayoutParams(
                            new LinearLayout.LayoutParams(dpToPx(1), dpToPx(250), (float) 0));

                    in.set_text(String.valueOf(et_dist.getText()));
                    start_activity();
                    boolFilter = true;
                }/**/
                break;



            case R.id.but_map_close:
                Map_jur_and_notary.this.finish();
                break;
        }
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = Map_jur_and_notary.this.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public List<JSONObject> getMark(JSONObject jsonObjectMess) {

        List markList = new ArrayList<>();
        try {
            jsonArrayMap = jsonObjectMess.getJSONArray("array");

            for (int i = 0; i <jsonArrayMap.length(); i++) {
                JSONObject arrayElement = jsonArrayMap.getJSONObject(i);
                markList.add(arrayElement);
            }

        } catch (JSONException e) {
            // TODO Auto-generatedcatchblock
            e.printStackTrace();
        }
        return markList;
    }

    public Map<Marker, Integer> map_jur = new HashMap<Marker, Integer>();
    public Map<Marker, Integer> map_notary = new HashMap<Marker, Integer>();

    public void add_mark_maps(){

        map1.clear();
        mark_jur = new LatLng(in.get_latitude(), in.get_longitude());
        CircleOptions circleOptions = new CircleOptions()
                .center(mark_jur)
                .radius(Integer.parseInt(String.valueOf(et_dist.getText())))
                .fillColor(getResources().getColor(R.color.maps_color))
                .strokeColor(getResources().getColor(R.color.maps_color2))
                .strokeWidth(5);
       // map1.addCircle(circleOptions);//рисует круг

        if(getIntent().getIntExtra("TYPE", 0)==0){
            /**
             * если адвокаты
             */
            for(int i = 0 ; i<mcArrayJuristAccoun.length; i++){
                try {

                    Double dX = 0.0;
                    Double dY = 0.0;

                    if(b_answer_next){
                        dX =  mcArrayJuristAccoun[i].CurrentLatitude;
                        dY =  mcArrayJuristAccoun[i].CurrentLongitude;
                    }else {
                        dX =  mcArrayJuristAccoun[i].Latitude;
                        dY =  mcArrayJuristAccoun[i].Longitude;
                    }

                    mark_jur = new LatLng(dX, dY);
                    //map1.setInfoWindowAdapter(new MyInfoWindowAdapter());

                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.x_marker_jur);
                    String online;
                    if(mcArrayJuristAccoun[i].IsOnline){
                        online = "онлайн";
                    }else{
                        online = "не доступен";
                    }
                    marker = map1.addMarker(new MarkerOptions().title(mcArrayJuristAccoun[i].Fio).snippet(online).position(mark_jur).icon(bitmapDescriptor));

                    map_jur.put(marker, mcArrayJuristAccoun[i].Id);

                    map1.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            boolFilter = true;
                            //dopInfo(mcArrayJuristAccoun[i].Fio);
                            return false;
                        }
                    });
                    map1.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Intent intent = new Intent(Map_jur_and_notary.this, Advocate_profile.class);
                            in.set_id_jur(map_jur.get(marker));
                            in.setType_jur(ClientQuestion.AccountTypes.Jurist);
                            startActivity(intent);
                        }
                    });

                    dropPinEffect(marker);

                } catch (Exception e) {

                }
            }
        }else {
            for(int i = 0 ; i<mcArrayNotary.length; i++){
                /**
                 * если нотариусы
                 */
                try {

                    Double dX = 0.0;
                    Double dY = 0.0;

                    dX =  mcArrayNotary[i].Latitude;
                    dY =  mcArrayNotary[i].Longitude;

                    mark_jur = new LatLng(dX, dY);
                    //map1.setInfoWindowAdapter(new MyInfoWindowAdapter());

                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.x_marker_not);

                    marker = map1.addMarker(new MarkerOptions().title(mcArrayNotary[i].Fio).position(mark_jur).icon(bitmapDescriptor));

                    map_notary.put(marker, mcArrayNotary[i].Id);

                    map1.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            boolFilter = true;
                            return false;
                        }
                    });

                    map1.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            //Intent intent = new Intent(Map_jur_and_notary.this, Notary_profile.class);
                            id_notary = map_notary.get(marker);
                            new UrlConnectionTask().execute();
                            //startActivity(intent);
                        }
                    });

                    dropPinEffect(marker);

                } catch (Exception e) {

                }
            }
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map1.setMyLocationEnabled(true);
        map1.setOnMapClickListener(this);
    }


    public void dopInfo(String s1, String s2){
        Log.d("QWERTY", "!!!!!!!!!!!!!");
        boolFilter = true;
        lilamapone.setLayoutParams(
                new LinearLayout.LayoutParams(dpToPx(200), dpToPx(250), (float) 0));
        tv1.setText(s1);
        tv2.setText(s2);
        //Animation anim = AnimationUtils.loadAnimation(Map_jur_and_notary.this, R.anim.trans_map_filter_on);
       // lila_map_filter.startAnimation(anim);
    }


    public void setDataMap(String s) {
        try {
            jsonObjectMap = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonArrayMap = jsonObjectMap.getJSONArray("array");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArrayMap.length(); i++) {
            try {
                jsonArrayMap.getJSONObject(i);

                mark_jur = new LatLng(Double.parseDouble(jsonArrayMap.getJSONObject(i).getString("X")),
                        Double.parseDouble(jsonArrayMap.getJSONObject(i).getString("Y")));

                //map1.moveCamera(CameraUpdateFactory.newLatLngZoom(mark_jur, 17));
                map1.addMarker(new MarkerOptions().position(mark_jur)
                        .title("адрес").snippet(jsonArrayMap.getJSONObject(i).getString("adres")));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map1.setMyLocationEnabled(true);
        map1.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    protected  void createLocationRequest() {
        LocationRequest mLocationRequest =  new  LocationRequest ();
        mLocationRequest . setInterval ( 10000 );
        mLocationRequest . setFastestInterval(5000);
        mLocationRequest . setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mContentView;

        MyInfoWindowAdapter() {
            mContentView = getLayoutInflater().inflate(
                    R.layout.maps_info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            TextView tv_title = (TextView) mContentView.findViewById(R.id.title);
            tv_title.setText(marker.getTitle());

            TextView tv_snippet = (TextView) mContentView.findViewById(R.id.snippet);
            tv_snippet.setText(marker.getSnippet());

            return mContentView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }
    }
    @Override
    public void onMapLongClick(LatLng point) {
        // TODO Auto-generated method stub
        Marker newMarker = map1.addMarker(new MarkerOptions()
                .position(point)
                .snippet(point.toString()));

        newMarker.setTitle(newMarker.getId());
    }

    private void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 300;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 15ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                marker.setAnchor(0.5f, 1.0f + 1 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 15);
                } else { // done elapsing, show window

                }
            }
        });
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
                    .url("http://"+in.get_url()+"/Notaries/GetNotary/"+id_notary)
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
            Notary notary = gson.fromJson(result, Notary.class);

            String sm[] = new String[]{
                    notary.Fio,
                    "",//String.valueOf(notary.Address.City),
                    notary.Phone,
                    "",//notary.Contacts.Email,
                    notary.Address2,
                    "",//notary.Contacts.Site,
                    String.valueOf(notary.Appointments),//"false",//notary.list.get(position).getString("predvzapis"),
                    String.valueOf(notary.WorkInOffDays),
                    String.valueOf(notary.AppointmentsEmail),//"false",//notary.list.get(position).getString("zakazpoemail"),
                    String.valueOf(notary.Equipage),
                    String.valueOf(notary.WorkWithJur),//"false",//notary.list.get(position).getString("urlica"),
                    String.valueOf(notary.IsSitesCertification),
                    String.valueOf(notary.IsPleadtingHereditaryCases),
                    String.valueOf(notary.LockDocuments),//"false",//notary.list.get(position).getString("fixdokaz"),
                    String.valueOf(notary.RequestsAndDuplicate),//"false",//notary.list.get(position).getString("duplicat"),
                    String.valueOf(notary.DepositsReception),
                    String.valueOf(notary.Id),
                    String.valueOf(notary.Latitude),//"-1",//notary.list.get(position).getString("X"),
                    String.valueOf(notary.Longitude)//"-1",//notary.list.get(position).getString("Y")
            };
            try {
                sm[2] = notary.Phone;
                sm[3] = notary.Email;
                sm[5] = notary.Site;
            } catch (Exception e) {

            }
            in.set_not_prof(sm);
            Intent intent = new Intent(Map_jur_and_notary.this, Notary_profile.class);
            startActivity(intent);
            super.onPostExecute(result);
        }
    }

}

