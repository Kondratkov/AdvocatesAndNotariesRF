package kondratkov.advocatesandnotariesrf.dop_dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.gson.Gson;
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

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.account.City;
import kondratkov.advocatesandnotariesrf.account.Region;

public class Dialog_region {
    public int City_num = 0;
    public String Region_string ="";
    public String City_string = "";

    public String [] str_region = null;
    public int[] int_region = null;
    public String [] str_city = null;
    public String[] int_str_city = null;
    public Spinner spinner = null;

    public Activity activity = null;
    public Context context = null;
    public String City_url = "";
    public String City_name = "";
    public String City_id = "";
    public i_dialog_region di_r;
    IN in;

    City post_city;
    Region region;
    Region[] mcArrayRegions;
    City [] mcArrayCity;
    public int id_city = 0;

    public ProgressBar progressBar;
    public FrameLayout frame;

    public Spinner spinner_region, spinner_city;

    public Dialog_region(Context context, String url){
        this.context = context;
        this.City_url = url;
    }

    public void openDialogRegion(){


        final Dialog dialog = new Dialog(context);
        dialog.setTitle("Выбор города");
        dialog.setContentView(R.layout.advocate_dialog_sort_city);

        di_r = (i_dialog_region) context;

        frame = (FrameLayout)dialog.findViewById(R.id.frame_dialog);
        progressBar = (ProgressBar)dialog.findViewById(R.id.progressBar_dialog);

        frame.setBackgroundResource(R.color.frameOn);
        frame.setClickable(true);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        spinner_region = (Spinner)dialog.findViewById(R.id.dialog_sort_spinner_region);
        spinner_city = (Spinner)dialog.findViewById(R.id.dialog_sort_spinner_city);

        Button btnDismiss = (Button) dialog.getWindow().findViewById(
                R.id.dialog_sort_button_close);

        Button btnRes = (Button) dialog.getWindow().findViewById(
                R.id.dialog_sort_button_res);

        Button btnmiss = (Button) dialog.getWindow().findViewById(
                R.id.dialog_sort_button_yes);


        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                City c = new City();
                Region r = new Region();
                di_r.iv_onRegion(c, r, false);
                dialog.dismiss();
            }
        });

        btnmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                di_r.iv_onRegion(region.Cities[id_city], region, true);
                dialog.dismiss();
            }
        });

        dialog.show();
        new UrlConnectionTask().execute();
        // /new AsyncTaskDialog().execute();
    }

    public void stopProgressBar(){
        frame.setBackgroundResource(R.color.frameOff);
        frame.setClickable(false);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    public void onSpinner(){

        /*try {
            JSONObject jsonObject = new JSONObject(Region_string);
            JSONArray jsonArray = jsonObject.getJSONArray("array");

            for(int i = 0; i< jsonArray.length(); i++ ){
                str_region[i] = jsonArray.getJSONObject(i).getString("name");
                int_region[i] = jsonArray.getJSONObject(i).getInt("id");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
try {
    str_region = new String[mcArrayRegions.length];
    int_region = new int[mcArrayRegions.length];

        for(int i=0; i<mcArrayRegions.length; i++){
            str_region[i] = mcArrayRegions[i].Name;
            int_region[i] = mcArrayRegions[i].Id;
        }

        final ArrayAdapter<String> adapter_region = new ArrayAdapter<String>(context,R.layout.my_quest_item_spinner, str_region);
        //ArrayAdapter.createFromResource(this, R.layout.my_quest_item_spinner, new String(){""});
        spinner_region.setAdapter(adapter_region);
        spinner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                City_num = int_region[Integer.parseInt(String.valueOf(id))];
                //new AsyncTaskDialog().execute();
                new UrlConnectionTask().execute();
            }
        });
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                City_name = str_city[pos];
                post_city = region.Cities[pos];
                id_city = (int) id;
                stopProgressBar();
            }
        });
}catch (Exception e){}
    }

    class UrlConnectionTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            /*try {
                URL url = new URL("http://"+in.get_url()+"/TopQuestions/GetTopQuestions");
                URLConnection urlConnection = url.openConnection();


                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }*/

            OkHttpClient client = new OkHttpClient();
            Request request= null;

            if(City_num==0){
                request = new Request.Builder()
                        .url("http://"+City_url+"/Regions/GetRegions")
                        .build();
            }else{
                request = new Request.Builder()
                        .url("http://"+City_url+"/Regions/GetRegion/"+City_num)
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
            Gson gg = new Gson();
            if(result!=null){
                if(City_num==0){
                    mcArrayRegions = gg.fromJson(result, Region[].class);
                    Region_string = result;
                    City_num = 1;
                    onSpinner();
                }else{
                    region = gg.fromJson(result, Region.class);
                    str_city = new String[region.Cities.length];
                    for(int i = 0; i< region.Cities.length; i++ )
                    {
                        Log.d("qwerty", String.valueOf(i));
                        str_city[i] =  region.Cities[i].Name;
                    }
                    ArrayAdapter<String> adapter_city = new ArrayAdapter<String>(context,
                            R.layout.my_quest_item_spinner, str_city);
                    //ArrayAdapter.createFromResource(this, R.layout.my_quest_item_spinner, new String(){""});
                    spinner_city.setAdapter(adapter_city);
                    stopProgressBar();
                }
            }else{

            }

            super.onPostExecute(result);
        }
    }


    class AsyncTaskDialog extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... params) {
            String url_s = "";
            String json_s = "";
            if(City_num == 0){
                url_s = "http://"+City_url+"/123.getreg";
            }else{
                url_s = "http://"+City_url+"/123.getcity";
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
            if(result != null){
                if(City_num==0){
                    Region_string = result;
                    City_num = 1;
                    onSpinner();
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
                        ArrayAdapter<String> adapter_city = new ArrayAdapter<String>(context,
                                R.layout.my_quest_item_spinner, str_city);
                        //ArrayAdapter.createFromResource(this, R.layout.my_quest_item_spinner, new String(){""});
                        spinner_city.setAdapter(adapter_city);
                        stopProgressBar();
                    }
                }
            }

            //progressBarRegionSign.setVisibility(ProgressBar.INVISIBLE);
            //progressBarCitySign.setVisibility(ProgressBar.INVISIBLE);
        }
    }
    public static class ServerSendData {

        //public static IN iny = new IN();
        public static String sendRegData(String stringUrl, String json_user_str) {

            //String log = new AdIn().get_id_user(); iny.get_id_user();
            //String pas = iny.get_password_user();

            String result =  null;
            try {
                // Log.d(DEBUG_TAG, "z3 - "+String.valueOf(iny.getURL()));
                URL url = new URL(stringUrl);//new AdIn().getURL());//
                URLConnection connection = url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setDoOutput(true);
                httpConnection.setChunkedStreamingMode(0);
                OutputStream out = new BufferedOutputStream(httpConnection.getOutputStream());

                out.write(json_user_str.getBytes());
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

    public static interface i_dialog_region{
        public void iv_onRegion(City city, Region region, boolean b);

    }
}
