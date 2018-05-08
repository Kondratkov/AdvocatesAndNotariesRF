package kondratkov.advocatesandnotariesrf.my_info;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.account.GetProfileClient;

public class My_photo_redaction extends AppCompatActivity {

    private Button button_photo_get, button_photo_add, button_photo_yes, button_photo_close;
    private ImageView imageView_photo_user;

    private final int GALLERY_REQUEST = 1;
    private final int CAMERA_RESULT = 2;
    private final int PIC_CROP = 3;

    public File fileImage = null;
    public File filePhoto = null;
    public String pathImage = "";
    private Uri picUri;
    private IN in;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_photo_redaction);

        button_photo_get = (Button)findViewById(R.id.button_photo_get);
        button_photo_yes = (Button)findViewById(R.id.button_photo_yes);
        button_photo_close = (Button)findViewById(R.id.button_photo_close);
        button_photo_add = (Button)findViewById(R.id.button_photo_add);
        imageView_photo_user = (ImageView)findViewById(R.id.imageView_photo_user);

        in = new IN();

        button_photo_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //use standard intent to capture an image
                    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //we will handle the returned data in onActivityResult
                    startActivityForResult(captureIntent, CAMERA_RESULT);
                }catch (Exception e){}
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
//                startActivityForResult(cameraIntent, CAMERA_RESULT);
            }
        });

        button_photo_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent();
                photoPickerIntent.setType("image/*");
                photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });

        button_photo_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    new UrlConnectionImage().execute();

                //}
            }
        });

        button_photo_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                My_photo_redaction.this.finish();
            }
        });
    }

    private void performCrop(){
        //call the standard crop action intent (the user device may not support it)
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri
        cropIntent.setDataAndType(picUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        //start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, PIC_CROP);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ViewGroup.LayoutParams imageViewLayoutParams;

        switch (requestCode) {
            case PIC_CROP:
                try{
                    Bundle extras = data.getExtras();
                    bitmap = extras.getParcelable("data");

                    imageView_photo_user.setImageBitmap(bitmap);

                    fileImage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "savedBitmap"+in.get_id_user()+".png");

                    try {
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(fileImage);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        } finally {
                            if (fos != null) fos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

              }catch (Exception e){}

                break;
            case CAMERA_RESULT:
                Bundle extras = data.getExtras();
                bitmap = extras.getParcelable("data");

                imageView_photo_user.setImageBitmap(bitmap);

                fileImage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "savedBitmap"+in.get_id_user()+".png");

                Bitmap.createScaledBitmap(bitmap, 250, 250, true);
                //bitmap.setPixel(150, 150, 0);
                try {
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(fileImage);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    } finally {
                        if (fos != null) fos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case GALLERY_REQUEST:
                try{
                    if(data.getData() != null){
                        picUri = data.getData();
                        // кадрируем его
                        performCrop();
                    }
                }catch (Exception e){

                }

                break;
        }

    }

    int code;
    class UrlConnectionImage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody;
            Request request;

            requestBody = new MultipartBuilder()
                    .type((MultipartBuilder.FORM))
                    .addFormDataPart("file",fileImage.getName(), RequestBody.create(MediaType.parse("*/*"), fileImage))
                    .addFormDataPart("some-field", "some-value")
                    .build();

            request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://app.mmka.info/api/Account/PostUserImage")
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                result = response.body().string();
                Log.d("qwerty_q", "result - " + result);
                code = response.code();
                Log.d("qwerty_q", result +" - "+String.valueOf(code));
            } catch (IOException e) {
                Log.e("qwerty_q", result);
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("qwerty_q", result);
            My_photo_redaction.this.finish();

            super.onPostExecute(result);
        }
    }

}
