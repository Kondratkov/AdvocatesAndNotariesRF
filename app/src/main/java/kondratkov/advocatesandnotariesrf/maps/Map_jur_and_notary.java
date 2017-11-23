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
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.Sidebar;
import kondratkov.advocatesandnotariesrf.account.Bup;
import kondratkov.advocatesandnotariesrf.api_classes.Filter.FindByCoordinatesFilter;
import kondratkov.advocatesandnotariesrf.api_classes.Filter.FindJuristFilter;
import kondratkov.advocatesandnotariesrf.api_classes.JuristAccounClass;

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
    public boolean sortir = true;

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

        mark_jur = new LatLng(in.get_latitude(), in.get_longitude());
        map1.moveCamera(CameraUpdateFactory.newLatLngZoom(mark_jur, 17));

        //setDataMap(json_maps);
        Gson gson = new Gson();
        mcArrayJuristAccoun = gson.fromJson((String)getIntent().getSerializableExtra("ARRAY"), JuristAccounClass[].class);
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

    //--S----L--O--C----------------------------------------------------
    /*@Override
    protected void onResume() {
        super.onResume();
        Log.d("qwerty", "onResume()");
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Просим пользователя включить GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Настройка");
            builder.setMessage("Сейчас GPS отлючён.\n" + "Включить?");
            builder.setPositiveButton("Да",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });
            builder.setNegativeButton("Нет",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Не обязательно
                            finish();
                        }
                    });
            builder.create().show();
        }

        // Регистрируемся для обновлений
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
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_FOR_UPDATES,
                mLocationListener);
        // Получаем текущие координаты при запуске
        showCurrentLocation();
    }
    protected void showCurrentLocation() {
        Log.d("qwerty", "showCurrentLocation()");
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
        Location location = mLocationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            JSONObject json_st = new JSONObject();
            try {
                json_st.put("idu", in.get_id_user());//String.valueOf(in.get_id()));
                json_st.put("password", in.get_password_user());
                json_st.put("tip_who", "0");//String.valueOf(adin.get_mess_id()));
                json_st.put("latitude", String.valueOf(location.getLatitude()));//String.valueOf(adin.get_mess_id()));
                json_st.put("longitude", String.valueOf(location.getLongitude()));//String.valueOf(adin.get_mess_id()));
                json_st.put("dist", et_dist.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            in.set_latitude(location.getLatitude());
            in.set_longitude(location.getLongitude());

            mark_jur = new LatLng(in.get_latitude(), in.get_longitude());
            map1.moveCamera(CameraUpdateFactory.newLatLngZoom(mark_jur, 17));

            json_s = String.valueOf(json_st);//String.valueOf(json_st);
            //json_s = String.valueOf(location.getLatitude())+" + "+String.valueOf(location.getLongitude());
            new AsyncTaskMaps().execute();
        }
        else{
            in.set_latitude(55.75222);
            in.set_longitude(37.61556);
            JSONObject json_st = new JSONObject();
            try {
                json_st.put("idu", in.get_id_user());//String.valueOf(in.get_id()));
                json_st.put("password", in.get_password_user());
                json_st.put("tip_who", "0");//String.valueOf(adin.get_mess_id()));
                json_st.put("latitude", in.get_latitude() );//String.valueOf(adin.get_mess_id()));
                json_st.put("longitude", in.get_longitude());//String.valueOf(adin.get_mess_id()));
                json_st.put("dist", et_dist.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mark_jur = new LatLng(in.get_latitude(), in.get_longitude());
            map1.moveCamera(CameraUpdateFactory.newLatLngZoom(mark_jur, 17));

            json_s = String.valueOf(json_st);//String.valueOf(json_st);
            //json_s = String.valueOf(location.getLatitude())+" + "+String.valueOf(location.getLongitude());
            new AsyncTaskMaps().execute();
            Log.d("qwerty","chto ne tack" );
        }
    }
    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            Log.d("qwerty", " MyLoc 1");
            String message = "Новое местоположение \n Долгота: " +
                    location.getLongitude() + "\n Широта: " + location.getLatitude();
            Toast.makeText(Map_jur_and_notary.this, message, Toast.LENGTH_LONG)
                    .show();
            showCurrentLocation();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Log.d("qwerty", " MyLoc 2");
            Toast.makeText(Map_jur_and_notary.this, "Статус провайдера изменился",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Log.d("qwerty", " MyLoc 3");
            Toast.makeText(Map_jur_and_notary.this,
                    "Провайдер заблокирован пользователем. GPS выключен",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
            Log.d("qwerty", " MyLoc 4");
            Toast.makeText(Map_jur_and_notary.this,
                    "Провайдер включен пользователем. GPS включён",
                    Toast.LENGTH_LONG).show();
        }
    }*/
    //--F----L--O--C----------------------------------------------------

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

        for(int i = 0 ; i<mcArrayJuristAccoun.length; i++){
            try {
                Double dX = mcArrayJuristAccoun[i].Latitude;//list.get(i).getDouble("X");//Double.parseDouble(list.get(i).getString("X").replaceAll(",", "")) / 10000;
                Double dY = mcArrayJuristAccoun[i].Longitude;//list.get(i).getDouble("Y");//Double.parseDouble(list.get(i).getString("Y").replaceAll(",", "")) / 10000;
                Log.d("qwerty", "LL "+i+" "+dX+" "+dY );

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

                map1.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        boolFilter = true;
                       // dopInfo(mcArrayJuristAccoun[i].Fio);
                        return false;
                    }
                });

                //marker = map1.addMarker(new MarkerOptions().title(list.get(i).getString("name")).icon(bitmapDescriptor).snippet(list.get(i).getString("adres"))
                //     .snippet(list.get(i).getString("adres")));//+ " сайт: "+list.get(i).getString("site")).position(mark_jur));

                //marker = map1.addMarker(new MarkerOptions().title("dfdfdf"));
                dropPinEffect(marker);

                //map1.setOnMapLongClickListener(Map_jur_and_notary.this);


                //map1.moveCamera(CameraUpdateFactory.newLatLngZoom(mark_jur, 17));
                //map1.addMarker(new MarkerOptions().position(mark_jur).title("адрес").snippet(list.get(i).getString("adres")));

            } catch (Exception e) {

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
/*
    class AsyncTaskMaps extends AsyncTask<Void, Integer, String> {
        // фоновая работа
        String url_s = "";
        @Override
        protected String doInBackground(Void... params) {
            JSONObject json_st = new JSONObject();
            try {
                json_st.put("idu", in.get_id_user());//String.valueOf(in.get_id()));
                json_st.put("password", in.get_password());
                json_st.put("tip_who", "0");//String.valueOf(adin.get_mess_id()));
                json_st.put("latitude", String.valueOf(in.get_latitude()));//String.valueOf(adin.get_mess_id()));
                json_st.put("longitude", String.valueOf(in.get_longitude()));//String.valueOf(adin.get_mess_id()));
                json_st.put("dist", in.get_text());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json_s = String.valueOf(json_st);
            Log.d("qwerty", "START json_s "+ json_s);
            url_s ="http://"+in.get_url()+"/123.geolocjur";
            return ServerSendData.sendRegData(url_s, json_s);
        }

        // выполняется после doInBackground, имеет доступ к UI
        protected void onPostExecute(String result) {
            if(result != null){
                start_activity();
                Log.d("qwerty", "START "+ result);
            }
            else{
                Log.d("qwerty", "START  "+ "NULL");
            }

        }
    }
    public static class ServerSendData {

        public static IN iny = new IN();
        public static String sendRegData(String stringUrl, String json_str) {

            String result =  null;
            try {

                URL url = new URL(stringUrl);
                URLConnection connection = url.openConnection();

                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setDoOutput(true);

                httpConnection.setChunkedStreamingMode(0);
                OutputStream out = new BufferedOutputStream(httpConnection.getOutputStream());

                out.write(json_str.getBytes());

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
            } catch (MalformedURLException e) { Log.d(DEBUG_TAG, "z8");
            } catch (IOException e1) { Log.d(DEBUG_TAG, "z9");
            }

            return result;
        }
    }
*/
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
}

