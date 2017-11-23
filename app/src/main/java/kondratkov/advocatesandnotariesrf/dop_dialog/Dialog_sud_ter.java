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
import kondratkov.advocatesandnotariesrf.account.JudicialAreas;
import kondratkov.advocatesandnotariesrf.account.Region;

/**
 * Created by Modest on 14.10.2016.
 */
public class Dialog_sud_ter {
    public int SudTer_num = 0;
    public String Region_string = "";
    public String SudTer_string = "";

    public String[] str_region = null;
    public int[] int_region = null;
    public String[] str_SudTer = null;
    public String[] int_str_SudTer = null;
    public Spinner spinner = null;

    public Activity activity = null;
    public Context context = null;
    public String SudTer_url = "";
    public String SudTer_name = "";
    public String SudTer_id = "";
    public i_dialog_sudTer di_r;
    IN in;

    JudicialAreas post_SudTer;
    Region region;
    Region[] mcArrayRegions;
    JudicialAreas[] mcArraySudTer;
    public int id_SudTer = 0;

    public ProgressBar progressBar;
    public FrameLayout frame;

    public Spinner spinner_region, spinner_SudTer;

    public Dialog_sud_ter(Context context, String url) {
        this.context = context;
        this.SudTer_url = url;
    }

    public void openDialogSudTer() {


        final Dialog dialog = new Dialog(context);
        dialog.setTitle("Выбор судебной территории");
        dialog.setContentView(R.layout.advocate_dialog_sort_city);

        di_r = (i_dialog_sudTer) context;

        frame = (FrameLayout) dialog.findViewById(R.id.frame_dialog);
        progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar_dialog);

        frame.setBackgroundResource(R.color.frameOn);
        frame.setClickable(true);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        spinner_region = (Spinner) dialog.findViewById(R.id.dialog_sort_spinner_region);
        spinner_SudTer = (Spinner) dialog.findViewById(R.id.dialog_sort_spinner_city);

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
                Region region_res = new Region();
                JudicialAreas ja = new JudicialAreas();
                di_r.iv_onRegion(ja, region_res, false);
                dialog.dismiss();
            }
        });

        btnmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region.Cities[id_SudTer].Region = region;
                String START;
                START = region.JudicialAreas[id_SudTer].Name;
                START = region.Name;
                START = new Gson().toJson(region);
                START = region.Name;
                di_r.iv_onRegion(region.JudicialAreas[id_SudTer], region, true);
                dialog.dismiss();
            }
        });

        dialog.show();
        new UrlConnectionTask().execute();
        // /new AsyncTaskDialog().execute();
    }

    public void stopProgressBar() {
        frame.setBackgroundResource(R.color.frameOff);
        frame.setClickable(false);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    public void onSpinner() {

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

        str_region = new String[mcArrayRegions.length];
        int_region = new int[mcArrayRegions.length];

        for (int i = 0; i < mcArrayRegions.length; i++) {
            str_region[i] = mcArrayRegions[i].Name;
            int_region[i] = mcArrayRegions[i].Id;
        }

        final ArrayAdapter<String> adapter_region = new ArrayAdapter<String>(context, R.layout.my_quest_item_spinner, str_region);
        //ArrayAdapter.createFromResource(this, R.layout.my_quest_item_spinner, new String(){""});
        spinner_region.setAdapter(adapter_region);
        spinner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                SudTer_num = int_region[Integer.parseInt(String.valueOf(id))];
                //new AsyncTaskDialog().execute();
                new UrlConnectionTask().execute();
            }
        });
        spinner_SudTer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                SudTer_name = str_SudTer[pos];
                post_SudTer = region.JudicialAreas[pos];
                id_SudTer = (int) id;
                stopProgressBar();
            }
        });
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
            Request request = null;

            if (SudTer_num == 0) {
                request = new Request.Builder()
                        .url("http://" + SudTer_url + "/Regions/GetRegions")
                        .build();
            } else {
                request = new Request.Builder()
                        .url("http://" + SudTer_url + "/Regions/GetRegion/" + SudTer_num)
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

            if (SudTer_num == 0) {
                mcArrayRegions = gg.fromJson(result, Region[].class);
                Region_string = result;
                SudTer_num = 1;
                onSpinner();
            } else {
                region = gg.fromJson(result, Region.class);
                str_SudTer = new String[region.JudicialAreas.length];
                for (int i = 0; i < region.JudicialAreas.length; i++) {
                    Log.d("qwerty", String.valueOf(i));
                    str_SudTer[i] = region.JudicialAreas[i].Name;
                }
                ArrayAdapter<String> adapter_SudTer = new ArrayAdapter<String>(context,
                        R.layout.my_quest_item_spinner, str_SudTer);
                //ArrayAdapter.createFromResource(this, R.layout.my_quest_item_spinner, new String(){""});
                spinner_SudTer.setAdapter(adapter_SudTer);
                stopProgressBar();
            }

            super.onPostExecute(result);
        }
    }

    public static interface i_dialog_sudTer {
        public void iv_onRegion(JudicialAreas SudTer, Region region, boolean b);

    }
}