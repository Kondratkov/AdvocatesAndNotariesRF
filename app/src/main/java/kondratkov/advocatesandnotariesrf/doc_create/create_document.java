package kondratkov.advocatesandnotariesrf.doc_create;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.Sidebar;
import kondratkov.advocatesandnotariesrf.account.JuristSpecializationSector;
import kondratkov.advocatesandnotariesrf.api_classes.DocumentOrder;
import kondratkov.advocatesandnotariesrf.base_he.ListFail;

public class create_document extends Activity implements View.OnTouchListener{

    private long docTip = 2;
    public ArrayAdapter<?> adapterVid;// = ArrayAdapter.createFromResource(DocCreation.this, R.array.ArrayVidDocPr, R.layout.item_spinner_doc);
    public String[] docTips = null;
    public String[] docVids = null;
    public IN in;
    EditText etDocText;
    TextView tvWhom, tvDocFail;
    ScrollView doc_scroll;
    int idt_id=-1;
    JuristSpecializationSector juristSpecializationSector;
    public int UI = 0;
    public File file;
    ArrayList<File>files;
    public int intClose = 0;
    public int intSetI = 0;
    public int code;

    public String JSON_POST="";
    static final int GALLERY_REQUEST = 1;
    static final int IMG_GALLERY =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_create);

        doc_scroll = (ScrollView)findViewById(R.id.doc_scroll);
        doc_scroll.setOnTouchListener(this);

        files = new ArrayList<File>();
        //in.FilesRestart();

        final Spinner spinnerDocTip = (Spinner)findViewById(R.id.spinnerDocTip);
        final Spinner spinnerDocVid = (Spinner)findViewById(R.id.spinnerDokVid);
        etDocText = (EditText)findViewById(R.id.tvmulDocForm);

        tvDocFail = (TextView)findViewById(R.id.tvDocFail);

        in = new IN();
        in.setDoc_id_jur(0);
        in.setDoc_tip("");
        in.setDoc_vid("");
        in.setDoc_text("");
        in.setDoc_fail("");
        in.setTip_request(0);

        docTips = getResources().getStringArray(R.array.ArrayTipDoc);
        docVids = getResources().getStringArray(R.array.ArrayVidDoc);

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.ArrayTipDoc, R.layout.my_quest_item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<?> adapter2 =
                ArrayAdapter.createFromResource(this, R.array.ArrayVidDoc, R.layout.my_quest_item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDocTip.setAdapter(adapter);
        spinnerDocVid.setAdapter(adapter2);


        docTip = spinnerDocTip.getSelectedItemId();

        //adapterVid = ArrayAdapter.createFromResource(this, R.array.ArrayVidDocMegPr, R.layout.item_spinner_doc);
        //adapterVid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinnerDocVid.setAdapter(adapterVid);


        spinnerDocTip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                juristSpecializationSector = new JuristSpecializationSector();
                juristSpecializationSector.Id = (int) id+1;
                juristSpecializationSector.SectorName = getResources().getStringArray(R.array.ArrayTipDoc)[(int) id];
                in.set_id_doc_vid(pos);
                in.setDoc_tip(docTips[pos]);

            }
        });

        spinnerDocVid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                //adapterVid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //spinnerDocVid.setAdapter(adapterVid);
                in.set_id_doc_vid(pos);
                in.setDoc_vid(docVids[pos]);
                UI = (int) id;
                //CustomAdapter.flag = true;
            }
        });

//@###################################################################################3v
        /*CustomAdapter adapter =
                new CustomAdapter(this, R.layout.item_spinner_doc, choose);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDocTip.setAdapter(adapter);

        docTip = spinnerDocTip.getSelectedItemId();

        //adapterVid = ArrayAdapter.createFromResource(this, R.array.ArrayVidDocMegPr, R.layout.item_spinner_doc);
        //adapterVid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinnerDocVid.setAdapter(adapterVid);


        Spinner spinner = (Spinner) findViewById(R.id.spinnerDocTip);
        CustomAdapter adapter = new CustomAdapter(this,
                android.R.layout.simple_spinner_item, cats);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onNothingSelected(AdapterView<?>parent) {
            }
            @Override
            public void onItemSelected(AdapterView<?>parent, View view,
                                       int pos, long id) {
                // Setadapterflagthatsomethinghasbeenchosen
                CustomAdapter.flag = true;
            }
        });


        spinnerDocTip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Ваш выбор: " + selectedItemPosition, Toast.LENGTH_SHORT);
                toast.show();

                switch (selectedItemPosition) {
                    case 0:
                        adapterVid = ArrayAdapter.createFromResource(DocCreation.this, R.array.ArrayVidDocPr, R.layout.item_spinner_doc);
                        adapterVid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDocVid.setAdapter(adapterVid);
                        break;
                    case 1:
                        adapterVid = ArrayAdapter.createFromResource(DocCreation.this, R.array.ArrayVidDocPr, R.layout.item_spinner_doc);
                        adapterVid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDocVid.setAdapter(adapterVid);
                        break;
                    case 2:
                        adapterVid = ArrayAdapter.createFromResource(DocCreation.this, R.array.ArrayVidDocPr, R.layout.item_spinner_doc);
                        adapterVid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDocVid.setAdapter(adapterVid);
                        break;
                    case 3:
                        adapterVid = ArrayAdapter.createFromResource(DocCreation.this, R.array.ArrayVidDocUgolPr, R.layout.item_spinner_doc);
                        adapterVid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDocVid.setAdapter(adapterVid);
                        break;
                    case 4:
                        adapterVid = ArrayAdapter.createFromResource(DocCreation.this, R.array.ArrayVidDocPr, R.layout.item_spinner_doc);
                        adapterVid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDocVid.setAdapter(adapterVid);
                        break;
                    case 5:
                        adapterVid = ArrayAdapter.createFromResource(DocCreation.this, R.array.ArrayVidDocMegPr, R.layout.item_spinner_doc);
                        adapterVid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDocVid.setAdapter(adapterVid);
                        break;
                    case 6:
                        adapterVid = ArrayAdapter.createFromResource(DocCreation.this, R.array.ArrayVidDocRegFirm, R.layout.item_spinner_doc);
                        adapterVid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDocVid.setAdapter(adapterVid);
                        break;
                    case 7:
                        adapterVid = ArrayAdapter.createFromResource(DocCreation.this, R.array.ArrayVidDocSdNedvig, R.layout.item_spinner_doc);
                        adapterVid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDocVid.setAdapter(adapterVid);
                        break;
                    case 8:
                        adapterVid = ArrayAdapter.createFromResource(DocCreation.this, R.array.ArrayVidDocPr, R.layout.item_spinner_doc);
                        adapterVid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDocVid.setAdapter(adapterVid);
                        break;
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        in.setChoice_of_menus(7);

        tvDocFail.setText(String.valueOf(in.getDoc_fail()));
    }

    public void updateText()
    {
        tvDocFail.setText(String.valueOf(in.getDoc_fail()));

        Log.d("qwerty", "4q");/*Log.d("qwerty", "3q");
*/
    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.butToSend:
                updateText();
                in.setDoc_text(String.valueOf(etDocText.getText()));
                openDialog();
                break;

            case R.id.butAddDoc:

                Intent pickIntent = new Intent();
                pickIntent.setType("*/*");
                pickIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(pickIntent, "Выберите фаил"), IMG_GALLERY);



                //Intent intent_fail = new Intent(create_document.this, ListFail.class);
                //startActivity(intent_fail);
                break;

            case R.id.but_doc_cancel:
                create_document.this.finish();
                break;

            case R.id.but_doc_menu:
                Intent intent134 = new Intent(create_document.this, Sidebar.class);//DocCreation.class);
                startActivity(intent134);
                break;
        }
        //Toast toast = Toast.makeText(getApplicationContext(),
        //"Click выбор: " + "dddd", Toast.LENGTH_SHORT);
        //toast.show();/**/
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMG_GALLERY) {
                Uri pickedUri = data.getData();

                String imagePath;
                String[] imgData = {MediaStore.Images.Media.DATA};

                Cursor imgCursor = getApplicationContext().getContentResolver()
                        .query(pickedUri, imgData, null, null, null);
                if (imgCursor != null) {
                    int index = imgCursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    imgCursor.moveToFirst();
                    imagePath = imgCursor.getString(index);
                    imgCursor.close();
                } else
                    imagePath = pickedUri.getPath();

                File file  = new File(imagePath);
                in.addFiles(file);
                String ff;
                if(in.getDoc_fail().length()==0){
                    ff =file.getName();
                }else{
                    ff = in.getDoc_fail()+"\n"+file.getName();
                }
                in.setDoc_fail(ff);
                //mWebView.loadUrl("file:///" + imagePath);
            }
        }
    }

    private void openDialogTypeDoc(){
        final Dialog dialog = new Dialog(create_document.this);
        dialog.setTitle("Выбирите ");    }

    private void openDialog() {
        final Dialog dialog = new Dialog(create_document.this);
        dialog.setTitle(R.string.title_text_doc);
        dialog.setContentView(R.layout.doc_dialog_create);

        TextView tv_doc_id_jur = (TextView) dialog.findViewById(R.id.tvDocWhomB);
        TextView tv_doc_tip    = (TextView) dialog.findViewById(R.id.tvDocTipB);
        TextView tv_doc_vid    = (TextView) dialog.findViewById(R.id.tvDocVidB);
        TextView tv_doc_text   = (TextView) dialog.findViewById(R.id.tvDocTextB);
        TextView tv_doc_fail   = (TextView) dialog.findViewById(R.id.tvDocFileB);

        tv_doc_tip.setText(in.getDoc_tip());
        tv_doc_vid.setText(in.getDoc_vid());
        tv_doc_text.setText(in.getDoc_text());
        tv_doc_fail.setText(in.getDoc_fail());

        Button btnAdd = (Button) dialog.getWindow().findViewById(
                R.id.but_okB);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();


                DocumentOrder documentOrder = new DocumentOrder();
                /*switch (UI){
                    case 0:documentOrder.DocumentType = DocumentOrder.documentTyp.Pretension;
                        break;
                    case 1:documentOrder.DocumentType = DocumentOrder.documentTyp.ComplaintActionsOfficials;
                        break;
                    case 2:documentOrder.DocumentType = DocumentOrder.documentTyp.AppealToStateBodies;
                        break;
                    case 3:documentOrder.DocumentType = DocumentOrder.documentTyp.Statement;
                        break;
                    case 4:documentOrder.DocumentType = DocumentOrder.documentTyp.Plaint;
                        break;
                    case 5:documentOrder.DocumentType = DocumentOrder.documentTyp.ObjectionToClaim;
                        break;
                    case 6:documentOrder.DocumentType = DocumentOrder.documentTyp.AppealCourtDecision;
                        break;
                    case 7:documentOrder.DocumentType = DocumentOrder.documentTyp.DifficultToChoose;
                        break;
                }*/
int dsf = UI;
                documentOrder.DocumentType = documentOrder.SetDocumentTyp(UI);
                documentOrder.JuristSpecialization = juristSpecializationSector;
                documentOrder.Description = in.getDoc_text();
                JSON_POST = gson.toJson(documentOrder);

                new UrlConnectionTask().execute(JSON_POST);
                //new AsyncTaskExample().execute();
                dialog.dismiss();
            }
        });

        Button btnDismiss = (Button) dialog.getWindow().findViewById(
                R.id.btn_cancelB);

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class UrlConnectionTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();

            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");
            String s= in.get_token_type()+" "+in.get_token();
            String s2 = params[0];
            String s3= "http://"+in.get_url()+"/DocumentOrders/PostDocumentOrder";
            String s4="213";

            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://"+in.get_url()+"/DocumentOrders/PostDocumentOrder")
                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, params[0]))
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                result = response.body().string();
                code = response.code();

                if(result!=null) {
                    Gson gson = new Gson();

                    DocumentOrder documentOrder = new DocumentOrder();
                    documentOrder = gson.fromJson(result, DocumentOrder.class);
                    in.set_idt(documentOrder.Id);
                }else{
                    Toast.makeText(create_document.this,
                            "Нет связи с сервером!",
                            Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("qwerty", result);
            if (code>=200 && code<300) {
                onPostFiles();
            }else{
                Toast.makeText(create_document.this,
                        "Нет связи с сервером!",
                        Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
    }

    public void onPostFiles(){
        files = in.getFiles();
        intClose = files.size();
        if(intClose==0){
            Toast.makeText(create_document.this,
                    "Заказ на составление документа принят!",
                    Toast.LENGTH_LONG).show();
            create_document.this.finish();
        }else{
            for(int i=0; i<in.getFiles().size(); i++){
                intSetI = i;
                in.setFile(files.get(i));
                new UrlConnectionTaskDoc().execute();
            }
        }
    }

    class UrlConnectionTaskDoc extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            OkHttpClient client = new OkHttpClient();

            //MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("multipart/form-data; charset=utf-8");
            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("multipart/form-data; charset=utf-8");
            String s= in.get_token_type()+" "+in.get_token();
            String s3= "http://"+in.get_url()+"/DocumentOrders/SendClientFiles/"+in.get_idt();
            String s4=String.valueOf(in.getFile());
            String d5 =in.getDoc_fail();

            RequestBody requestBody;
            Request request;

            requestBody = new MultipartBuilder()
                    .type((MultipartBuilder.FORM))
                    .addFormDataPart("file",in.getFile().getName(), RequestBody.create(MediaType.parse("*/*"), in.getFile()))
                    .addFormDataPart("some-field", "some-value")
                    .build();

            request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://"+in.get_url()+"/DocumentOrders/SendClientFiles/"+in.get_idt())
                    .post(requestBody)
                    .build();

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
            Log.d("qwerty", result);
            if (result!=null) {
                if(intClose==intSetI+1){
                    Toast.makeText(create_document.this,
                            "Заказ на составление документа принят!",
                            Toast.LENGTH_LONG).show();
                    in.FilesRestart();
                    in.setDoc_fail_name("");
                    create_document.this.finish();
                }
            }else{
                Toast.makeText(create_document.this,
                        "Нет связи с сервером!",
                        Toast.LENGTH_LONG).show();
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
                    Intent intent = new Intent( create_document.this, Sidebar.class);
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
/*class Item {
  Object key;
  Object value;
  Item(Object k, Object v) {
    key = k; value = v;
  }
}
List<Item> list = new ArrayList<Item>();
list.add(new Item("A", "a"));
list.add(new Item("B", "b"));
list.add(new Item("B", "c"));
list.add(new Item("C", "c"));
Collections.sort(list, new Comparator<Item>() {
  int compare(Item o1, Item o2) {
    return o1.key.compareTo(o2.key);
  }
});*/