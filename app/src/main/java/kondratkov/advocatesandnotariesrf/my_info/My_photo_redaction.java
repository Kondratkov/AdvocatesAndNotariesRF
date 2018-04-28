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
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private Uri picUri;
    private IN in;

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

                if(picUri !=null){
                    My_photo_redaction.this.finish();
                }
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
        Bitmap bitmap = null;

        switch (requestCode) {
            case PIC_CROP:
                try{
                    Bundle extras = data.getExtras();
                    bitmap = extras.getParcelable("data");


                    imageView_photo_user.setImageBitmap(bitmap);
                    //


                }catch (Exception e){}

                break;
            case CAMERA_RESULT:
                //File photo = new File(Environment.getExternalStorageDirectory(), String.valueOf(in.get_id_user())+"_photo");
                try {
                    Bundle extras = data.getExtras();
                    bitmap = extras.getParcelable("data");
                    FileOutputStream fos = new FileOutputStream(bitmap + String.valueOf(in.get_id_user())+"_photo");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);

                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    Log.e("MyLog", e.toString());
                }

                picUri  =Uri.parse(bitmap + String.valueOf(in.get_id_user())+"_photo");
                performCrop();
                //imageView_photo_user.setImageBitmap(bitmap);//setImageURI(picUri);

              //  photo = new File(data.pa)
                //    // кадрируем его
                   // performCrop();
//
//                try{
//                    bitmap = (Bitmap) data.getExtras().get("data");
//                }catch (Exception e){}
//                //ImageView ivCamera = (ImageView) findViewById(R.id.iv_camera);
//                //ivCamera.setImageBitmap(thumbnail);
//                if(bitmap!=null){
//
//
////                    imageViewLayoutParams = new ViewGroup.LayoutParams(Convector_DP_PX.dpToPx(150, this), Convector_DP_PX.dpToPx(150, this));
////                    imageView.setLayoutParams(imageViewLayoutParams);
////                    imageView.setPadding(Convector_DP_PX.dpToPx(5, this), Convector_DP_PX.dpToPx(5, this), Convector_DP_PX.dpToPx(5, this), Convector_DP_PX.dpToPx(5, this));
////
////                    imageView.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            Toast.makeText(AddViolationActivity.this, "# (123123Long click)", Toast.LENGTH_SHORT).show();
////                        }
////                    });
////
////                    linearLayout_add_violation_image.addView(imageView);
//                }
                break;
            case GALLERY_REQUEST:
                // Получим Uri снимка
                picUri = data.getData();
                // кадрируем его
                performCrop();
//                if (resultCode == RESULT_OK) {
//                    Uri selectedImage = data.getData();
//
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    if(bitmap!=null){
//
//                        imageView_photo_user.setImageBitmap(bitmap);
//
//                    }
//                }
                break;
        }

    }

//    class UrlConnectionTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            String result = "";
//
//            OkHttpClient client = new OkHttpClient();
//
//            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");
//
//            //RequestBody formBody = RequestBody.create(JSON, json_signup);
//
//            Request request = new Request.Builder()
//                    .header("Authorization", in.get_token_type()+" "+in.get_token())
//                    .url("http://"+in.get_url()+"/AccauntPostUserImage/")
//                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, params[0]))
//                    .build();
//            try {
//                Response response = client.newCall(request).execute();
//                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//                result = response.body().string();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
////            if(result!=null && code>=200 && code<300){
////                Gson gson = new Gson();
////                getProfileClient = gson.fromJson(result, GetProfileClient.class);
////                start_activity();
////            }else{
////                Toast.makeText(My_profile.this,
////                        "Нет связи с сервером!",
////                        Toast.LENGTH_LONG).show();
////            }
//            super.onPostExecute(result);
//        }
//    }
}
