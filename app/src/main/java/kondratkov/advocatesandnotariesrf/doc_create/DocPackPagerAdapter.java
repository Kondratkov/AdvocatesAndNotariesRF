package kondratkov.advocatesandnotariesrf.doc_create;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.api_classes.DocumentPacket;

public class DocPackPagerAdapter extends FragmentPagerAdapter {

    String json_doc_pack1 =
            "{\"array\":[" +
                    "{\"id\":\"1\",\"name\":\" 1 кейс документов\",\"sum\":\"100\",\"ThemeID\":\"1\",\"doc\":\"   at android.view.View$DeclaredOnClickListener.resolveMethod(View.java:4479)\n" +
                    "                      at android.view.View$DeclaredOnClickListener.onClick(View.java:4443)\n" +
                    "                      at android.view.View.performClick(View.java:5198)\n" +
                    "                      at android.view.View$PerformClick.run(View.java:21147)\n" +
                    "                      at android.os.Handler.handleCallback(Handler.java:739)\",\"them\":\"Административное и трудовое право\"}," +
                    "{\"id\":\"1\",\"name\":\" 2 кейс документов\",\"sum\":\"250\",\"ThemeID\":\"3\",\"doc\":\"null\",\"them\":\"Гражданское право\"}," +
                    "{\"id\":\"1\",\"name\":\" 3 кейс документов\",\"sum\":\"300\",\"ThemeID\":\"1\",\"doc\":\"null\",\"them\":\"Возможность или нет\"}," +
                    "{\"id\":\"1\",\"name\":\" 4 кейс документов\",\"sum\":\"500\",\"ThemeID\":\"6\",\"doc\":\"null\",\"them\":\"Что то там еще\" }]}}";
    public DocumentPacket[] jsonArray_pack;
    public IN in;

    public DocPackPagerAdapter(FragmentManager fm) {
        super(fm);
        in = new IN();
        // TODO Auto-generated constructor stub
        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObject = new JSONObject(in.get_json_doc_pack());
            Log.d("qwerty", "!!!!!!!!start "+ jsonObject);
            jsonArray = jsonObject.getJSONArray("array");
            Log.d("qwerty", "!!!!!!!!jsonArray "+ jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        jsonArray_pack = gson.fromJson(in.get_json_doc_pack(),DocumentPacket[].class);

    }

    @Override
    public Fragment getItem(int idx) {

        // TODO Auto-generated method stub
        Gson gson = new Gson();
        jsonArray_pack = gson.fromJson(in.get_json_doc_pack(),DocumentPacket[].class);
        Bundle arguments = new Bundle();

            arguments.putString(Doc_pack_fragment.DOC_THEM, jsonArray_pack[idx].Header);//jsonArray.getJSONObject(idx).getString("them"));
            arguments.putString(Doc_pack_fragment.DOC_PAY, String.valueOf(jsonArray_pack[idx].Cost));//jsonArray.getJSONObject(idx).getString("sum"));
            arguments.putString(Doc_pack_fragment.DOC_LIST, jsonArray_pack[idx].Description);//jsonArray.getJSONObject(idx).getString("doc"));
            arguments.putString(Doc_pack_fragment.ID_PACK, String.valueOf(jsonArray_pack[idx].Id));//jsonArray.getJSONObject(idx).getString("id"));


        // Create the fragment instance and pass the arguments
        Doc_pack_fragment dFragment = new Doc_pack_fragment();
        dFragment.setArguments(arguments);
        // return the fragment instance
        return dFragment;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        Gson gson1 = new Gson();
        DocumentPacket[] documentPacket = gson1.fromJson(in.get_json_doc_pack(), DocumentPacket[].class);

        return documentPacket.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {

        Gson gson1 = new Gson();
        DocumentPacket[] documentPacket = gson1.fromJson(in.get_json_doc_pack(), DocumentPacket[].class);

        String s ="e";
        try {
            s = documentPacket[position].Header;//jsonArray.getJSONObject(position).getString("name");
        } catch (Exception e) {
        }
        return s;
    }
}
