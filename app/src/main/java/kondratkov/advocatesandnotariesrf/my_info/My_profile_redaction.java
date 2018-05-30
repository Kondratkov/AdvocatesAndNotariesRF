package kondratkov.advocatesandnotariesrf.my_info;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.R;
import kondratkov.advocatesandnotariesrf.account.Address;
import kondratkov.advocatesandnotariesrf.account.BaseAccount;
import kondratkov.advocatesandnotariesrf.account.City;
import kondratkov.advocatesandnotariesrf.account.ClientAccount;
import kondratkov.advocatesandnotariesrf.account.GetProfileClient;
import kondratkov.advocatesandnotariesrf.account.Region;
import kondratkov.advocatesandnotariesrf.dop_dialog.Dialog_region;
import kondratkov.advocatesandnotariesrf.input.SignUP;

public class My_profile_redaction extends Activity implements Dialog_region.i_dialog_region {

    private static final String DEBUG_TAG = "qwerty";

    public String json_signup = "";

    public EditText etNikSign, etPhoneSign, etEmailSign, etNameSign, etPatronymicSign, etSurnameSign;
    public TextView tvNikSign, tvPhoneSign, tvEmailSign, tvNameSign, tvPatronymicSign, tvSurnameSign, etCitySign;
    public int flagView = 0;
    public String strErrors = "";

    public String City = "";
    public String City_text = "";
    public String Region = "";
    public String resul = "";
    public String phone_s = "";
    public String jsonStart = "";
    public JSONObject jsonObjectMyProf = null;
    public IN in;
    public ClientAccount clientAccount;
    public String JSON_REGISTER = "";
    City city_class;
    Region region_class;
    public Dialog dialog1;
    public int code;
    public int id_delete=-1;

    public GetProfileClient getProfileClient_new;
    public Address address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_redaction);

        in = new IN();
        etNikSign = (EditText) findViewById(R.id.etNikSign_red);

        etPhoneSign = (EditText) findViewById(R.id.etPhoneSign_red);
        etEmailSign = (EditText) findViewById(R.id.etEmailSign_red);
        etNameSign = (EditText) findViewById(R.id.etNameSign_red);
        etPatronymicSign = (EditText) findViewById(R.id.etPatronymicSign_red);
        etSurnameSign = (EditText) findViewById(R.id.etSurnameSign_red);


        tvNikSign = (TextView) findViewById(R.id.tvNikSign_red);
        String phone = "+7 ";
        // 9204539077
        for(int i =0; i<10; i++){

        }
        tvPhoneSign = (TextView) findViewById(R.id.tvPhoneSign_red);
        tvEmailSign = (TextView) findViewById(R.id.tvEmailSign_red);
        tvNameSign = (TextView) findViewById(R.id.tvNameSign_red);
        tvPatronymicSign = (TextView) findViewById(R.id.tvPatronymicSign_red);
        tvSurnameSign = (TextView) findViewById(R.id.tvSurnameSign_red);
        etCitySign = (TextView) findViewById(R.id.etCitySign_red);
        etCitySign.setText("");

        etPhoneSign.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phone_s = "+7df98df435+dsf7";

                Log.d("qwerty", "s.length " + s.length() + " __ " + etPhoneSign.length() + " " + phone_s);
                if(s.length()==0||s.length()==1){
                    etPhoneSign.setText("+7");
                    etPhoneSign.setSelection(s.length() + 1);
                }
                if(s.length()== 3 && String.valueOf(s.charAt(2)).equals(" ")==false){
                    String sd = String.valueOf(s.charAt(2));
                    etPhoneSign.setText("+7 "+sd);
                    etPhoneSign.setSelection(s.length() + 1);
                }
                if(s.length()== 7 && String.valueOf(s.charAt(6)).equals(" ")==false){
                    String sd = String.valueOf(s).substring(0, 6);
                    etPhoneSign.setText(sd+" "+String.valueOf(etPhoneSign.getText().charAt(6)));
                    etPhoneSign.setSelection(s.length() + 1);
                }
                if(s.length()== 11 && String.valueOf(s.charAt(10)).equals(" ")==false){
                    String sd = String.valueOf(s).substring(0,10);
                    etPhoneSign.setText(sd + " " + String.valueOf(etPhoneSign.getText().charAt(10)));
                    etPhoneSign.setSelection(s.length() + 1);
                }
                if(s.length()== 14 && String.valueOf(s.charAt(13)).equals("-")==false&& String.valueOf(s.charAt(13)).equals(" ")==false){
                    String sd = String.valueOf(s).substring(0,13);
                    etPhoneSign.setText(sd + "-" + String.valueOf(etPhoneSign.getText().charAt(13)));
                    etPhoneSign.setSelection(s.length() + 1);
                }
                if(s.length()==17){
                    String sd = String.valueOf(s).substring(0,16);
                    etPhoneSign.setText(sd);
                    etPhoneSign.setSelection(s.length()-1);
                }


                // etPhoneSign.setText(etPhoneSign.getText()+") ");
                //etPhoneSign.setSelection(s.length()+1);

                /*String formatted;
                String regex1 = "(\\+\\d)(\\d{3})";
                String regex2 = "(.+)(\\d{3})$";
                String regex3 = "(.+\\-)(\\d{2})$";

                // буду реализвывать ввод телефона в формате +х (ххх) ххх-хх-хх
                if (s.toString().matches(regex1)) {
                    formatted = String.valueOf(s).replaceFirst(regex1, "$1 ($2) ");
                    etPhoneSign.setText(formatted);
                    etPhoneSign.setSelection(formatted.length());
                } else if (s.toString().matches(regex2)) {
                    formatted = String.valueOf(s).replaceFirst(regex2, "$1$2-");
                    etPhoneSign.setText(formatted);
                    etPhoneSign.setSelection(formatted.length());
                } else if (s.toString().matches(regex3) && s.length() < 18) {
                    formatted = String.valueOf(s).replaceFirst(regex3, "$1$2-");
                    etPhoneSign.setText(formatted);
                    //etPhoneSign.setSelection(formatted.length());
                }*/
            }
        });

        etNikSign.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    flagView = 1;
                    onTouchL();
                }
                return false;
            }
        });
        etPhoneSign.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    flagView = 2;
                    onTouchL();
                }
                return false;
            }
        });
        etEmailSign.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    flagView = 3;
                    onTouchL();
                }
                return false;
            }
        });
        etNameSign.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    flagView = 4;
                    onTouchL();
                }
                return false;
            }
        });
        etPatronymicSign.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    flagView = 5;
                    onTouchL();
                }
                return false;
            }
        });
        etSurnameSign.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    flagView = 6;
                    onTouchL();
                }
                return false;
            }
        });

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idu",in.get_id_user());
            jsonObject.put("password", in.get_password());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonStart = String.valueOf(jsonObject);
        new UrlConnectionTask1().execute();
        //new UrlConnectionTaskDelete().execute();
       //new AsyncTaskProfile().execute();
    }

    public void start_activity(GetProfileClient _getProfileClient_new){

        //data_prof_v = getResources().getStringArray(R.array.array_profile);

        try {
            //etNikSign.setText(jsonObjectMyProf.getString("nik"));
            etPhoneSign.setText(getProfileClient_new.Phone);//jsonObjectMyProf.getString("phone"));
            etEmailSign.setText(getProfileClient_new.Email);//jsonObjectMyProf.getString("email"));
            etNameSign.setText(getProfileClient_new.Name);//jsonObjectMyProf.getString("name"));
            etPatronymicSign.setText(getProfileClient_new.Patronymic);//jsonObjectMyProf.getString("patronymic"));
            etSurnameSign.setText(getProfileClient_new.Surename);//jsonObjectMyProf.getString("surname"));
            etCitySign.setText(getProfileClient_new.Address.City);//jsonObjectMyProf.getString("city"));

        } catch (Exception e) {

        }
        onTouchL();
    }

    public void onTouchL() {

        if (etNikSign.getText().toString().equals("")) {
            if (flagView == 1) {
                tvNikSign.setText(R.string.sign_up_p_1);
                etNikSign.setHint("");
            } else {
                tvNikSign.setText("");
                etNikSign.setHint(R.string.sign_up_p_1);
            }
        } else {
            tvNikSign.setText(R.string.sign_up_p_1);
        }
//-----------------
        if (etPhoneSign.getText().toString().equals("")) {
            if (flagView == 2) {
                tvPhoneSign.setText(R.string.sign_up_p_2);
                etPhoneSign.setHint("");
            } else {
                tvPhoneSign.setText("");
                etPhoneSign.setHint(R.string.sign_up_p_2);
            }
        } else {
            tvPhoneSign.setText(R.string.sign_up_p_2);
        }
//-----------------
        if (etEmailSign.getText().toString().equals("")) {
            if (flagView == 3) {
                tvEmailSign.setText(R.string.sign_up_p_3);
                etEmailSign.setHint("");
            } else {
                tvEmailSign.setText("");
                etEmailSign.setHint(R.string.sign_up_p_3);
            }
        } else {
            tvEmailSign.setText(R.string.sign_up_p_3);
        }
//-----------------
        if (etNameSign.getText().toString().equals("")) {
            if (flagView == 4) {
                tvNameSign.setText(R.string.sign_up_p_7);
                etNameSign.setHint("");
            } else {
                tvNameSign.setText("");
                etNameSign.setHint(R.string.sign_up_p_7);
            }
        } else {
            tvNameSign.setText(R.string.sign_up_p_7);
        }
//-----------------
        if (etPatronymicSign.getText().toString().equals("")) {
            if (flagView == 5) {
                tvPatronymicSign.setText(R.string.sign_up_p_8);
                etPatronymicSign.setHint("");
            } else {
                tvPatronymicSign.setText("");
                etPatronymicSign.setHint(R.string.sign_up_p_8);
            }
        } else {
            tvPatronymicSign.setText(R.string.sign_up_p_8);
        }
//-----------------
        if (etSurnameSign.getText().toString().equals("")) {
            if (flagView == 6) {
                tvSurnameSign.setText(R.string.sign_up_p_6);
                etSurnameSign.setHint("");
            } else {
                tvSurnameSign.setText("");
                etSurnameSign.setHint(R.string.sign_up_p_6);
            }
        } else {
            tvSurnameSign.setText(R.string.sign_up_p_6);
        }
//-----------------

    }

    private final int D_0 = 0;
    private final int D_1 = 1;
    private final int D_2 = 2;
    private final int D_3 = 3;
    private final int D_4 = 4;
    private final int D_5 = 5;
    private final int D_6 = 6;
    private final int D_7 = 7;
    private final int D_8 = 8;
    private final int D_11 = 11;
    private final int D_13 = 13;


    @Override
    protected Dialog onCreateDialog(int id) {
        String s = strErrors;
        switch (id) {
            case 33:{
                Log.d("qwerty", "builderArray33");
                AlertDialog.Builder builder33 = new AlertDialog.Builder(this);
                builder33.setMessage("Запрос на регистрацию прошел успешно, после проверки Ваших данных вам будет выслано письмо по адресу "+etEmailSign.getText()+" с дальнейшими указаниями!")
                        .setCancelable(false)

                        .setNegativeButton("закрыть",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        My_profile_redaction.this.finish();
                                    }
                                });
                return builder33.create();
            }

            case D_0:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(s)
                        .setCancelable(false)

                        .setNegativeButton(R.string.project_close_dialog,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                return builder.create();
            case D_1:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage(s)
                        .setCancelable(false)

                        .setNegativeButton(R.string.project_close_dialog,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                return builder1.create();
            case D_2:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setMessage(s)
                        .setCancelable(false)

                        .setNegativeButton(R.string.project_close_dialog,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                return builder2.create();
            case D_3:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setMessage(s)
                        .setCancelable(false)

                        .setNegativeButton(R.string.project_close_dialog,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                return builder3.create();
            case D_4:
                AlertDialog.Builder builder4 = new AlertDialog.Builder(this);
                builder4.setMessage(s)
                        .setCancelable(false)

                        .setNegativeButton(R.string.project_close_dialog,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                return builder4.create();
            case D_5:
                AlertDialog.Builder builder5 = new AlertDialog.Builder(this);
                builder5.setMessage(s)
                        .setCancelable(false)

                        .setNegativeButton(R.string.project_close_dialog,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                return builder5.create();
            case D_6:
                AlertDialog.Builder builder6 = new AlertDialog.Builder(this);
                builder6.setMessage(s)
                        .setCancelable(false)

                        .setNegativeButton(R.string.project_close_dialog,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                return builder6.create();
            case D_7:
                AlertDialog.Builder builder7 = new AlertDialog.Builder(this);
                builder7.setMessage(s)
                        .setCancelable(false)

                        .setNegativeButton(R.string.project_close_dialog,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                return builder7.create();

            case D_11:
                AlertDialog.Builder builder11 = new AlertDialog.Builder(this);
                builder11.setMessage("Регистрация пользователя прошла успешно.")
                        .setCancelable(false)

                        .setNegativeButton("закрыть",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        My_profile_redaction.this.finish();
                                    }
                                });
                return builder11.create();

            case D_13:
                AlertDialog.Builder builder13 = new AlertDialog.Builder(this);
                builder13.setMessage("Изменить информацию по данному профилю невозможно!")
                        .setCancelable(false)

                        .setNegativeButton("закрыть",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        My_profile_redaction.this.finish();
                                    }
                                });
                return builder13.create();

            case D_8:
                AlertDialog.Builder builder8 = new AlertDialog.Builder(this);
                builder8.setMessage(s)
                        .setCancelable(false)

                        .setNegativeButton(R.string.project_close_dialog,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                return builder8.create();

            default:
                return null;
        }
    }

    @SuppressLint("ResourceAsColor")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registr_red:
                v.setEnabled(false);
                for (int i = 0; i < 9; i++) {
                    switch (i) {
                        case 0:
                            if (etNikSign.getText().toString().length() == 0 ||
                                    String.valueOf(etNikSign.getText().toString().charAt(0)) == " ") {
                                etNikSign.setHintTextColor(getResources().getColor(R.color.apperror_color));
                                etNikSign.setBackgroundResource(R.drawable.apperror_edit_text_holo_light);
                                tvNikSign.setTextColor(getResources().getColor(R.color.apperror_color));

                                strErrors = getString(R.string.sign_error_one) + " " + getString(R.string.sign_up_p_1) +" "+ getString(R.string.sign_error_two);
                                showDialog(D_0);

                                i = 9;
                            } else {
                                etNikSign.setHintTextColor(getResources().getColor(R.color.text_one));
                                etNikSign.setBackgroundResource(R.drawable.regestr_et_edit_text_holo_light);
                                tvNikSign.setTextColor(getResources().getColor(R.color.text_one));
                            }
                            break;

                        case 1:
                            if (etPhoneSign.getText().toString().length() == 0 ||
                                    String.valueOf(etPhoneSign.getText().toString().charAt(0)) == " " ||
                                    etPhoneSign.getText().toString().length() != 16
                                    ) {
                                etPhoneSign.setHintTextColor(getResources().getColor(R.color.apperror_color));
                                etPhoneSign.setBackgroundResource(R.drawable.apperror_edit_text_holo_light);
                                tvPhoneSign.setTextColor(getResources().getColor(R.color.apperror_color));


                                strErrors = getString(R.string.sign_error_phone_one);
                                Log.d("qwerty", strErrors);
                                showDialog(D_1);

                                i = 9;
                            } else {
                                etPhoneSign.setHintTextColor(getResources().getColor(R.color.text_one));
                                etPhoneSign.setBackgroundResource(R.drawable.regestr_et_edit_text_holo_light);
                                tvPhoneSign.setTextColor(getResources().getColor(R.color.text_one));
                            }
                            break;

                        case 2:
                            if (etEmailSign.getText().toString().length() == 0 ||
                                    String.valueOf(etEmailSign.getText().toString().charAt(0)) == " ") {
                                etEmailSign.setHintTextColor(getResources().getColor(R.color.apperror_color));
                                etEmailSign.setBackgroundResource(R.drawable.apperror_edit_text_holo_light);
                                tvEmailSign.setTextColor(getResources().getColor(R.color.apperror_color));

                                strErrors = getString(R.string.sign_error_one) + " '" + getString(R.string.sign_up_p_3) + "' " + getString(R.string.sign_error_two);
                                showDialog(D_2);

                                i = 9;
                            } else {
                                etEmailSign.setHintTextColor(getResources().getColor(R.color.text_one));
                                etEmailSign.setBackgroundResource(R.drawable.regestr_et_edit_text_holo_light);
                                tvEmailSign.setTextColor(getResources().getColor(R.color.text_one));
                            }
                            break;

                        case 8:


                            Gson gson = new Gson();

                            GetProfileClient getProfileClient_nn = getProfileClient_new;

                            getProfileClient_nn.Name = String.valueOf(etNameSign.getText());
                            getProfileClient_nn.Surename = String.valueOf(etSurnameSign.getText());
                            getProfileClient_nn.Patronymic = String.valueOf(etPatronymicSign.getText());
                            getProfileClient_nn.Email = String.valueOf(etEmailSign.getText());
                            getProfileClient_nn.Phone = String.valueOf(etPhoneSign.getText());
//                            Address address = new Address();
//                            address.City = String.valueOf(city_class);
//                            address.Region = String.valueOf(region_class);
//                            getProfileClient_nn.Address = address;
                            getProfileClient_nn.Login = String.valueOf(etEmailSign.getText());

//                            clientAccount = new ClientAccount();
//                            clientAccount.UserName = String.valueOf(etEmailSign.getText());
//                            clientAccount.Password = String.valueOf(etPasswordSign.getText());
//                            clientAccount.ConfirmPassword = String.valueOf(etPasswordSign.getText());
//                            clientAccount.AccountType = ClientAccount.AccountTypes.Client;
//                            clientAccount.AccountProfile = baseAccount;
                            JSON_REGISTER =  gson.toJson(getProfileClient_nn);





//
//                            Set_baseAccount baseAccount = new Set_baseAccount();
//                            baseAccount.Name = String.valueOf(etNameSign.getText());
//                            baseAccount.Surename = String.valueOf(etSurnameSign.getText());
//                            baseAccount.Patronymic = String.valueOf(etPatronymicSign.getText());
//                            baseAccount.Email = String.valueOf(etEmailSign.getText());
//                            baseAccount.Phone = String.valueOf(etPhoneSign.getText());
//                            Address address = new Address();
//                            address.City = String.valueOf(city_class);
//                            address.Region = String.valueOf(region_class);
//                            baseAccount.Address = address;
//
//                            clientAccount = new ClientAccount();
//                            clientAccount.UserName = String.valueOf(etEmailSign.getText());
//                            clientAccount.Password = String.valueOf(etPasswordSign.getText());
//                            clientAccount.ConfirmPassword = String.valueOf(etPasswordSign.getText());
//                            clientAccount.AccountType = ClientAccount.AccountTypes.Client;
//                            clientAccount.AccountProfile = baseAccount;
//                            JSON_REGISTER =  gson.toJson(clientAccount);
                            //Log.d("qwerty", json_register);

                            Dialog_loc();

/*                      JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("Nik", etNikSign.getText());
                                jsonObject.put("Phone", etPhoneSign.getText());
                                jsonObject.put("Email", etEmailSign.getText());
                                jsonObject.put("Name", etNameSign.getText());
                                jsonObject.put("Patronymic", etPatronymicSign.getText());
                                jsonObject.put("Surname", etSurnameSign.getText());
                                //jsonObject.put("region", Region);
                                jsonObject.put("city", in.get_id_city());
                                jsonObject.put("Password", etPasswordSign.getText());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            json_signup = String.valueOf(jsonObject);
                            new AsyncTaskExample().execute();
                            if (Boolean.parseBoolean(resul) == true) {
                                My_profile_redaction.this.finish();
                            } else {
                            }*/

                    }
                }
                v.setEnabled(true);
                break;

            case R.id.but_city_sign_red:
                Dialog_region dr = new Dialog_region(My_profile_redaction.this, in.get_url());
                dr.openDialogRegion();
                break;
        }
    }

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

        final Dialog dialog = new Dialog(My_profile_redaction.this);
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
                //etCitySign.setText(City_string);
                City = String.valueOf(City_num);
                Log.d("qwerty", "city "+ City);
                dialog.dismiss();
            }
        });

        btnmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // etCitySign.setText(City_string);
                City = String.valueOf(City_num);
                Log.d("qwerty", "city "+ City);
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
                City = int_str_city[pos];
                etCitySign.setText("");
            }
        });

        dialog.show();
    }


    private void Dialog_loc() {

        //showDialog(D_13);

        dialog1 = new Dialog(My_profile_redaction.this);
        dialog1.setTitle("передача...");
        dialog1.setContentView(R.layout.input_dialog_window);
        dialog1.setCancelable(true);


        new UrlConnectionTask().execute(JSON_REGISTER);
        dialog1.show();
    }

    @Override
    public void iv_onRegion(City city, Region region, boolean b) {
        //in.set_id_city(city_id);

        if(b){
            etCitySign.setText(city.Name);

            getProfileClient_new.Address.City = city.Name;
            getProfileClient_new.Address.Region = region.Name;
        }else{
            etCitySign.setText("");
        }
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
                                Region_string= jsonArray.getJSONObject(i).getString("name");
                                int_str_city[i]=jsonArray.getJSONObject(i).getString("id");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("qwerty", "массив ГОРОДОВ длина "+ City_num);

                        ArrayAdapter<String> adapter_city = new ArrayAdapter<String>(My_profile_redaction.this,
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

            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            //RequestBody formBody = RequestBody.create(JSON, json_signup);
            String dd = params[0];
            String ddf = "http://"+in.get_url()+"/ClientAccounts/PutClientAccount/"+String.valueOf(id_delete);
            code = 0;

            Request request = new Request.Builder()
                    .url("http://"+in.get_url()+"/ClientAccounts/PutClientAccount/"+String.valueOf(id_delete))
                    .put(RequestBody.create(MEDIA_TYPE_MARKDOWN, params[0]))
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
            if (result!=null && code>=200 && code<300) {
                showDialog(D_11);

            }else{
                Toast.makeText(My_profile_redaction.this,
                        "Ошибка!",
                        Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
    }

    class UrlConnectionTask1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            OkHttpClient client = new OkHttpClient();

            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

            //RequestBody formBody = RequestBody.create(JSON, json_signup);

            String FFFD= in.get_token_type()+" "+in.get_token();
            Request request = new Request.Builder()
                    .header("Authorization", in.get_token_type()+" "+in.get_token())
                    .url("http://"+in.get_url()+"/account/GetProfile")
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

            if(result!=null && code>=200 && code<300){
                Gson gson = new Gson();
                getProfileClient_new = gson.fromJson(result, GetProfileClient.class);
                id_delete = getProfileClient_new.Id;
                start_activity(getProfileClient_new);
            }else{
                Toast.makeText(My_profile_redaction.this,
                        "Нет связи с сервером!",
                        Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
    }


    class AsyncTaskProfile extends AsyncTask<Void, Integer, String> {
        @Override
        protected String doInBackground(Void... params) {
            String url_s = "http://"+in.get_url()+"/123.profusr";
            return ServerSendData.sendRegData(url_s, jsonStart);
        }
        protected void onPostExecute(String result) {
            Log.d("qwerty", "JOPETT_PROFILE "+ result);
            if(result!=null) {
               // start_activity(result);
            }
        }
    }
    public static class ServerSendData {

        public static String sendRegData(String Url, String json) {

            String result = null;
            try {
                URL url = new URL(Url);

                URLConnection connection = url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;

                httpConnection.setDoOutput(true);
                httpConnection.setChunkedStreamingMode(0);
                OutputStream out = new BufferedOutputStream(httpConnection.getOutputStream());
                // writeStream(out);
                out.write(json.getBytes());
                out.flush();
                out.close();

                InputStream in = new BufferedInputStream(httpConnection.getInputStream());
                int responseCode = 0;

                responseCode = httpConnection.getResponseCode();
                //Log.d(DEBUG_TAG, "z4  " + String.valueOf(in));

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

    public class UsPhoneNumberFormatter implements TextWatcher {

        private boolean mFormatting;
        private boolean clearFlag;
        private int mLastStartLocation;
        private String mLastBeforeText;
        private WeakReference<EditText> mWeakEditText;

        public UsPhoneNumberFormatter(WeakReference<EditText> weakEditText) {
            this.mWeakEditText = weakEditText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (after == 0&& s.toString().equals("380 ")) {
                clearFlag = true;
            }
            mLastStartLocation = start;
            mLastBeforeText = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            if (!mFormatting) {
                mFormatting = true;
                int curPos = mLastStartLocation;
                String beforeValue = mLastBeforeText;
                String currentValue = s.toString();
                String formattedValue = formatUsNumber(s);
                if (currentValue.length() > beforeValue.length()) {
                    int setCusorPos = formattedValue.length()
                            - (beforeValue.length() - curPos);
                    mWeakEditText.get().setSelection(setCusorPos <0 ? 0 : setCusorPos);
                } else {
                    int setCusorPos = formattedValue.length()
                            - (currentValue.length() - curPos);
                    if(setCusorPos >0&& !Character.isDigit(formattedValue.charAt(setCusorPos - 1))){
                        setCusorPos--;
                    }
                    mWeakEditText.get().setSelection(setCusorPos <0 ? 0 : setCusorPos);
                }
                mFormatting = false;
            }
        }

        private String formatUsNumber(Editable text) {
            StringBuilder formattedString = new StringBuilder();
            // удаляем все кроме цифр
            int p = 0;
            while (p < text.length()) {
                char ch = text.charAt(p);
                if (!Character.isDigit(ch)) {
                    text.delete(p, p + 1);
                } else {
                    p++;
                }
            }
            //оставляем ток цифры
            String allDigitString = text.toString();

            int totalDigitCount = allDigitString.length();

            if (totalDigitCount == 0
                    || (totalDigitCount >12&& !allDigitString.startsWith("380"))
                    || totalDigitCount >13) {
                text.clear();
                text.append(allDigitString);
                return allDigitString;
            }
            int alreadyPlacedDigitCount = 0;
            //если нажали бекспейс стираем все
            if (allDigitString.equals("380") && clearFlag) {
                text.clear();
                clearFlag = false;
                return"";
            }
            if (allDigitString.startsWith("380")) {
                formattedString.append("380 ");
                alreadyPlacedDigitCount+=3;
            }
            // Первые 3 цифры"380" должен быть заключен в скобки "()"
            if (totalDigitCount - alreadyPlacedDigitCount >2&& totalDigitCount >3) {
                formattedString.append("("
                        + allDigitString.substring(alreadyPlacedDigitCount,
                        alreadyPlacedDigitCount + 2) + ") ");
                alreadyPlacedDigitCount += 2;
            }
            // Вставляем "-" после 3 цифр
            if (totalDigitCount - alreadyPlacedDigitCount >3) {
                formattedString.append(allDigitString.substring(
                        alreadyPlacedDigitCount, alreadyPlacedDigitCount + 3)
                        + "-");
                alreadyPlacedDigitCount += 3;
            }
            // Вставляем "-" после 10 чисел
            if (totalDigitCount - alreadyPlacedDigitCount >2&& totalDigitCount >10) {
                formattedString.append(allDigitString.substring(
                        alreadyPlacedDigitCount, alreadyPlacedDigitCount + 2)
                        + "-");
                alreadyPlacedDigitCount += 2;
            }

            if (totalDigitCount > alreadyPlacedDigitCount) {
                formattedString.append(allDigitString
                        .substring(alreadyPlacedDigitCount));
            }

            text.clear();
            text.append(formattedString.toString());
            return formattedString.toString();
        }
    }public class Set_baseAccount extends BaseAccount {}

}
