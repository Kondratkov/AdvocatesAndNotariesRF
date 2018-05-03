package kondratkov.advocatesandnotariesrf;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.renderscript.Sampler;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

import org.json.JSONArray;

import java.io.File;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kondratkov.advocatesandnotariesrf.account.City;
import kondratkov.advocatesandnotariesrf.account.JuristSpecialization;
import kondratkov.advocatesandnotariesrf.api_classes.ClientQuestion;
import kondratkov.advocatesandnotariesrf.api_classes.Comment;
import kondratkov.advocatesandnotariesrf.api_classes.DocumentPacketOrder;
import kondratkov.advocatesandnotariesrf.api_classes.JuristAccounClass;
import kondratkov.advocatesandnotariesrf.api_classes.Notary;
import kondratkov.advocatesandnotariesrf.api_classes.Order;
import kondratkov.advocatesandnotariesrf.api_classes.TopQuestion;
import kondratkov.advocatesandnotariesrf.data_theme.AllQuestions;
import kondratkov.advocatesandnotariesrf.data_theme.AllQuestions_forum;
import kondratkov.advocatesandnotariesrf.maps.LocationListenerGPServices;

public class IN {

    public static boolean onemay = false;
    public boolean getOnemay(){return this.onemay;}
    public void setOnemay(boolean onemay){this.onemay = onemay;}

    public static int id_user = -1;
    public int get_id_user(){return this.id_user;}
    public void set_id_user(int id_user){this.id_user = id_user;}

    public static String password = "";
    public String get_password(){return this.password;}
    public void set_password(String password){this.password = password;}

    public static int idt = 0;
    public int get_idt(){return this.idt;}
    public void set_idt(int idt){this.idt = idt;}

    public static int idc = 0;
    public int get_idc(){return this.idc;}
    public void set_idc(int idc){this.idc = idc;}

    public static int id_jur = 0;
    public int get_id_jur(){return this.id_jur;}
    public void set_id_jur (int id_jur){this.id_jur = id_jur;}

    public static String id_jurS = "";
    public String get_id_jurS(){return this.id_jurS;}
    public void set_id_jurS (String id_jurS){this.id_jurS = id_jurS;}

    public static ClientQuestion [] msArrayClientQuestions;
    public ClientQuestion[]get_msArrayClientQuestions(){return this.get_msArrayClientQuestions();}
    public void set_setMsArrayClientQuestions(ClientQuestion [] msArrayClientQuestions){this.msArrayClientQuestions = msArrayClientQuestions;}

    public int int_start_activity= 0 ;
    public int  get_int_start_activity(){return (int_start_activity);}
    public void set_int_start_activity(int int_start_activity){this.int_start_activity=int_start_activity;}

    public static boolean start_service = false;
    public boolean get_start_service(){return this.start_service;}
    public void set_start_service (boolean start_service){this.start_service = start_service;}

    public static String nik_user = "";
    public String get_nik_user(){return this.nik_user;}
    public void set_nik_user (String nik_user){this.nik_user = nik_user;}

    public static Notary[] RESULT_LIST_NOTARY;
    public static Notary[] ARRAY_NOTARY_TO_MAP;

    public static int choice_of_menus = -1;
    public int  getChoice_of_menus(){return (choice_of_menus);}
    public void setChoice_of_menus(int choice_of_menus){this.choice_of_menus=choice_of_menus;}

    public static String ur232l = "93.170.186.54";
    //public static String url = "192.168.0.100/api";
    //public static String url = "vsundupey.vds.serverdale.com/api";


    //public static String url = "195.128.124.172/api";
    public static String url = "app.mmka.info/api";
    public String get_url(){return this.url;}
    public void set_url (String url){this.url = url;}

    public static String token = "";
    public String get_token(){return this.token;}
    public void set_token (String token){this.token = token;}

    public static String token_type = "";
    public String get_token_type(){return this.token_type;}
    public void set_token_type (String token_type){this.token_type = token_type;}

    public City city;
    public City get_city(){return this.city;}
    public void set_city(City city){this.city = city;}

    public TopQuestion topQuestion;
    public TopQuestion getTopQuestion(){return  this.topQuestion;}
    public void setTopQuestion(TopQuestion topQuestion){this.topQuestion = topQuestion;}

    public static Context context = null;
    public Context get_context(){return this.context;}
    public void set_context (Context context){this.context = context;}

    public static Activity activity = null;
    public Activity get_activity(){return this.activity;}
    public void set_activity (Activity activity){this.activity = activity;}

    public static int text_size_18 = 18;
    public int get_text_size_18(){return this.text_size_18;}
    public void set_text_size_18(int text_size_18){this.text_size_18 = text_size_18;}

    public static String date = "0";
    public String get_date(){return this.date;}
    public void set_date (String date){this.date = date;}

    public static int place =0;
    public int get_place(){return this.place;}
    public void set_place (int place){this.place = place;}

    public static String text = "";
    public String get_text(){return this.text;}
    public void set_text (String text){this.text = text;}

    public static String json_doc_pack = "";
    public String get_json_doc_pack(){return this.json_doc_pack;}
    public void set_json_doc_pack (String json_doc_pack){this.json_doc_pack = json_doc_pack;}

    public static List<AllQuestions> list_pr = null;
    public List<AllQuestions> get_list_pr(){return this.list_pr;}
    public void set_list_pr (List<AllQuestions> list_pr){this.list_pr = list_pr;}

    public static List<AllQuestions_forum> list_f = null;
    public List<AllQuestions_forum> get_list_f(){return this.list_f;}
    public void set_list_f (List<AllQuestions_forum> list_f){this.list_f = list_f;}

    public  static JSONArray jsonArray = null;
    public JSONArray get_jsonArray(){return this.jsonArray;}
    public void set_jsonArray (JSONArray jsonArray){this.jsonArray = jsonArray;}

    public static int font_1 = 0;
    public int get_font_1(){return this.font_1;}
    public void set_font_1 (int font_1){this.font_1 = font_1;}

    public static int font_2 = 0;
    public int get_font_2(){return this.font_2;}
    public void set_font_2 (int font_2){this.font_2 = font_2;}

    public static int font_3 = 0;
    public int get_font_3(){return this.font_3;}
    public void set_font_3 (int font_3){this.font_3 = font_3;}

    public static double latitude = 0;
    public double get_latitude(){return this.latitude;}
    public void set_latitude (double latitude){this.latitude = latitude;}

    public static double longitude = 0;
    public double get_longitude(){return this.longitude;}
    public void set_longitude (double longitude){this.longitude = longitude;}

    public static double latitude_my = 0;
    public double get_latitude_my(){return this.latitude_my;}
    public void set_latitude_my (double latitude_my){this.latitude_my = latitude_my;}

    public static String msArraydocumentOrders="";
    public String get_msArraydocumentOrders(){return this.msArraydocumentOrders;}
    public void set_msArraydocumentOrders(String msArraydocumentOrders){ this.msArraydocumentOrders=  msArraydocumentOrders;}

    public static double longitude_my = 0;
    public double get_longitude_my(){return this.longitude_my;}
    public void set_longitude_my (double longitude_my){this.longitude_my = longitude_my;}

    public static int filter_tip;
    public int get_filter_tip(){return this.filter_tip;}
    public void set_filter_tip (int filter_tip){this.filter_tip = filter_tip;}

    public static boolean Tip;
    public boolean get_Tiptip(){return Tip;}
    public void set_Tiptip(boolean Tip){this.Tip = Tip;}

    public static String fio_jur = "advocate";
    public String get_fio_jur(){return this.fio_jur;}
    public void set_fio_jur (String fio_jur){this.fio_jur = fio_jur;}

    public static boolean jut_ili_not = true;
    public boolean get_jut_ili_not(){return this.jut_ili_not;}
    public void set_jut_ili_not(boolean jut_ili_not){this.jut_ili_not = jut_ili_not;}

    public static int add_quest_tip = 0;
    public int get_add_quest_tip() {return (add_quest_tip);}
    public void set_add_quest_tip(int add_quest_tip){this.add_quest_tip = add_quest_tip;}

    public static String [] not_prof;
    public String[]get_not_prof(){return this.not_prof;}
    public void set_not_prof (String [] not_prof){this.not_prof = not_prof;}

    public static int doc_id_jur =0;
    public int  getDoc_id_jur(){return (doc_id_jur);}
    public void setDoc_id_jur(int doc_id_jur){this.doc_id_jur=doc_id_jur;}

    public static String doc_tip = "";
    public String getDoc_tip(){return (doc_tip);}
    public void setDoc_tip (String doc_tip){this.doc_tip = doc_tip;}
    public String get_doc_tip(){return (doc_tip);}
    public void set_doc_tip (String doc_tip){this.doc_tip = doc_tip;}

    public static String doc_vid = "";
    public String getDoc_vid(){return (doc_vid);}
    public void setDoc_vid (String doc_vid){this.doc_vid = doc_vid;}

    public static int id_doc_tip=0;
    public int get_id_doc_tip() {return (id_doc_tip);}
    public void set_id_doc_tip(int id_doc_tip){this.id_doc_tip = id_doc_tip;}

    public static int id_doc_vid=0;
    public int get_id_doc_vid() {return (id_doc_vid);}
    public void set_id_doc_vid(int id_doc_vid){this.id_doc_vid = id_doc_vid;}

    public static String doc_text = "";
    public String getDoc_text(){return (doc_text);}
    public void setDoc_text (String doc_text){this.doc_text = doc_text;}

    public static String doc_fail = "";
    public String getDoc_fail(){return (doc_fail);}
    public void setDoc_fail (String doc_fail){this.doc_fail = doc_fail;}

    public static String doc_fail64 = "";
    public String getDoc_fail64(){return (doc_fail64);}
    public void setDoc_fail64 (String doc_fail64){this.doc_fail64 = doc_fail64;}

    public static String doc_fail_name = "";
    public String getDoc_fail_name(){return (doc_fail_name);}
    public void setDoc_fail_name (String doc_fail_name){this.doc_fail_name = doc_fail_name;}

    public static File file;
    public File getFile(){return (file);}
    public void setFile (File file){this.file = file;}

    public static ArrayList<File>files= new ArrayList<File>();
    public ArrayList<File> getFiles(){return (files);}
    public void addFiles(File fileADD){this.files.add(fileADD);}
    public void FilesRestart(){files.clear();}

    public static JuristSpecialization[] list_specialization = null;
    public JuristSpecialization[] get_list_specialization(){return this.list_specialization;}
    public void set_list_specialization (JuristSpecialization[] list_specialization){this.list_specialization = list_specialization;}

    public static int tip_request = 0;
    //1 - whom
    public int  getTip_request(){return (tip_request);}
    public void setTip_request(int tip_request){this.tip_request=tip_request;}

    public static int id_jud_terr = 0;///id суд.терр. для отмены
    public int  getid_jud_terr(){return (id_jud_terr);}
    public void setid_jud_terr(int id_jud_terr){this.id_jud_terr=id_jud_terr;}

    public static Comment comment;
    public Comment getComment(){return comment;}
    public void setComment(Comment comment){this.comment = comment;}

    public static String id_city = "";
    public String get_id_city(){return (id_city);}
    public void set_id_city (String id_city){this.id_city = id_city;}

    public static String id_sud_ter = "";
    public String get_id_sud_ter(){return (id_sud_ter);}
    public void set_id_sud_ter (String id_sud_ter){this.id_sud_ter = id_sud_ter;}

    public static String data_servise = "";///для проверки даты в сервисе
    public String get_data_servise(){return (data_servise);}
    public void set_data_servise (String data_servise){this.data_servise = data_servise;}

    public static boolean[] sort_notary = {false, false, false, false, false, false, false, false, false, false, false, false, false};
    public boolean[] get_sort_notary(){return (sort_notary);}
    public void set_sort_notary (boolean[] sort_notary){this.sort_notary = sort_notary;}

    public static String doc_them = "";
    public String get_doc_them(){return (doc_them);}
    public void set_doc_them (String doc_them){this.doc_them = doc_them;}

    public static String doc_dt = "";
    public String get_doc_dt(){return (doc_dt);}
    public void set_doc_dt (String doc_dt){this.doc_dt = doc_dt;}

    public static int doc_status = 0;
    public int get_doc_status(){return (doc_status);}
    public void set_doc_status (int doc_status){this.doc_status = doc_status;}

    public static String doc_pay = "";
    public String get_doc_pay(){return (doc_pay);}
    public void set_doc_pay (String doc_pay){this.doc_pay = doc_pay;}

    public static String doc_num = "";
    public String get_doc_num(){return (doc_num);}
    public void set_doc_num (String doc_num){this.doc_num = doc_num;}

    public static Order[] msArrayOrders = null;

    //для времени в сообщениях
    public String dateDisplayConsPhone(String sDate){
        String sDateDisplay="";
        long feedTime=0;
        long myTime = System.currentTimeMillis();
        String dated = sDate.substring(0,10)+" "+sDate.substring(11,19);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(dated);
            feedTime = date.getTime();
            java.text.DateFormat mediumDateFormat = DateFormat.getMediumDateFormat(get_context());
            java.text.DateFormat timeFormat = DateFormat.getTimeFormat(get_context());
            sDateDisplay =timeFormat.format(date)+" "+mediumDateFormat.format(date) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sDateDisplay;
    }
    public String dateDisplay(String sDate){
        String sDateDisplay="";
        long feedTime=0;
        long myTime = System.currentTimeMillis();
        String dated = sDate.substring(0,10)+" "+sDate.substring(11,19);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(dated);
            feedTime = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(feedTime>0){
            if(myTime-feedTime<60000){
                sDateDisplay = DateUtils.formatDateTime(context, feedTime,
                        DateUtils.FORMAT_SHOW_TIME);
            }
            else if(myTime-feedTime<3600000){
                CharSequence timeago;
                timeago = DateUtils.getRelativeTimeSpanString(
                        feedTime, System.currentTimeMillis(),
                        DateUtils.SECOND_IN_MILLIS);
                sDateDisplay= (String) timeago;
            }else{
                sDateDisplay = DateUtils.formatDateTime(context, feedTime,
                        DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_SHOW_TIME);
            }
        }

        return sDateDisplay;
    }
    public String dateDisplayStag(String sDate){
        String sDateDisplay="";
        long feedTime=0;
        long myTime = System.currentTimeMillis();
        String dated = sDate.substring(0,10)+" "+sDate.substring(11,19);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(dated);
            feedTime = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(feedTime>0){
            long msYour = myTime-feedTime;
            double dYour = msYour/31536000000.0;
            if(dYour<1){
                sDateDisplay = "менее года";
            }else if(1>dYour&&dYour<5){
                sDateDisplay = "от 1 до 5 лет";
            }else {
                sDateDisplay = "свыше 5 лет";
            }
        }
        return sDateDisplay;
    }

    public double dDistance(double Latitude, double Longitude, LocationManager locationManager, Context context){
        LocationListenerGPServices l = new LocationListenerGPServices();
        l.start_my(locationManager, context);

        return Math.sqrt(Math.pow((latitude_my - Latitude), 2) + Math.pow((longitude_my - Longitude), 2))*100000;
    }

    public String d_to_sDistance(double d){
        String distance="";

        int dist_int = (int) d;
        if(dist_int<1000){
            distance = String.valueOf(dist_int)+" м";
        }else if(dist_int>10000000){
            distance = "нет коор.";
        }
        else{
            distance = String.valueOf(dist_int/1000)+" км";
        }

        return distance;
    }

    public String sDistance(double Latitude2, double Longitude2, LocationManager locationManager, Context context){
        String distance="";
        LocationListenerGPServices l = new LocationListenerGPServices();
        l.start_my(locationManager, context);

        double dist = Math.sqrt(Math.pow((latitude_my - Latitude2), 2) + Math.pow((longitude_my - Longitude2), 2))*100000;
        int dist_int = (int) dist;
        if(dist_int<1000){
            distance = String.valueOf(dist_int)+" м";
        }else if(dist_int>10000000){
            distance = "нет коор.";
        }
        else{
            distance = String.valueOf(dist_int/1000)+" км";
        }

        return distance;
    }

}
