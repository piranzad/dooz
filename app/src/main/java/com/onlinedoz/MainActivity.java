package com.onlinedoz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import co.ronash.pushe.Pushe;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class MainActivity extends AppCompatActivity {


    Toolbar toolbar;
    DrawerLayout DLayout;
    NavigationView navigation;
    CircleImageView img_pro;
    TextView txt_name,txt_email,txt_toolbar;
    String username,name,harif_name,currentLang,Myimage,harif_image;
    ImageView img_editprof;
    int nofmsg,nofMSG_server;
    SharedPreferences sharedP;
    public static final String USERDATA = "UserData";
    public static final String USERNAME = "username";
    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String PASSWORD = "password";
    public static final String IMAGE = "image";
    public static final String LANGUAGE = "language";
    public static final String FONT = "font";
    public static final String NofMSG = "nofmsg";

    Button btn_2player,btn_msg,btn_1player,btn_friend,btn_comment,btn_editprofile;
    boolean checknewgame=true;
    Intent commends_intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Clear_Cache();
        sharedP = getSharedPreferences(USERDATA, Context.MODE_PRIVATE);
        currentLang = sharedP.getString(LANGUAGE, "fa");
        setLocale(currentLang);
        setContentView(R.layout.activity_main);

        Pushe.initialize(this,true);

        btn_2player = findViewById(R.id.btn_2player);
        btn_comment = findViewById(R.id.btn_comments);
        btn_1player = findViewById(R.id.btn_1player);
        btn_friend = findViewById(R.id.btn_friends);
        txt_toolbar = findViewById(R.id.txt_toolbar);
        img_editprof = findViewById(R.id.img_editprof);
        btn_editprofile = findViewById(R.id.btn_editprofile);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AndroidNetworking.initialize(MainActivity.this);

        SharedPreferences.Editor runEdit = sharedP.edit();

        boolean FirstRun = sharedP.getBoolean("BareAval",true);
        name = sharedP.getString(NAME,"user");
        String age = sharedP.getString(AGE,"");
        Myimage = sharedP.getString(IMAGE,"test.jpg");
        username = sharedP.getString(USERNAME,"");
        //sharedP.edit().putInt(MainActivity.NofMSG, 0).apply();

        if (FirstRun) {
            taptarget();
            runEdit.putBoolean("BareAval",false);
            runEdit.apply();
        }

        if(InternetConnection()){
            //Toast.makeText(this, " Connection", Toast.LENGTH_LONG).show();
            checknewgame=true;
        Timer ();
        setonline ();
        Create_msg_tbl();



        }else{
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }


    String textFont = sharedP.getString(FONT,"forte");
    Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/" + textFont + ".ttf");
        btn_comment.setTypeface(typeface);
        btn_2player.setTypeface(typeface);
        btn_friend.setTypeface(typeface);
        btn_1player.setTypeface(typeface);
        btn_editprofile.setTypeface(typeface);


   txt_toolbar.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           // baraye tayin clcik ro ye dokme coment ya massege balaye safe

           commends_intent = new Intent(MainActivity.this,Comments.class);
           commends_intent.putExtra("btn","btn_msg");
           commends_intent.putExtra("nofmsg",nofMSG_server);
           startActivity(commends_intent);
       }
   });

        btn_2player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



              // Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
               // startActivity(myIntent);
                startActivity(new Intent(MainActivity.this,List_online.class));
                checknewgame=false;
            }
        });
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                commends_intent = new Intent(MainActivity.this,Comments.class);
                commends_intent.putExtra("btn","nav_comments");
              //  commends_intent.putExtra("nofmsg",nofMSG_server);
                startActivity(commends_intent);
                //startActivity(new Intent(MainActivity.this,List_online.class));
            }
        });

        img_editprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (currentLang.equals("fa")) {
                    currentLang = "en";
                    sharedP.edit().putString(LANGUAGE, currentLang).apply();
                    sharedP.edit().putString(FONT, "forte").apply();
                    Toast.makeText(MainActivity.this, "English", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                    finish();
                } else {
                    currentLang = "fa";
                    sharedP.edit().putString(LANGUAGE, currentLang).apply();
                    sharedP.edit().putString(FONT, "bmorvarid").apply();
                    Toast.makeText(MainActivity.this, "فارسی", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                    finish();
                }
            }
        });

        btn_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,EditProfile.class));
            }
        });
        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,Friend_List.class));
            }
        });
        btn_1player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent oneplayer_intent = new Intent(MainActivity.this,YeNafare_Hard.class);
                oneplayer_intent.putExtra("playerName",name);
                oneplayer_intent.putExtra("MyImage",Myimage);
                startActivity(oneplayer_intent);
            }
        });


    }


    @Override
    public void onBackPressed() {
            super.onBackPressed();

    }

    //vaziat onilne karbar ra moshkhas mikonad
    private void Timer () {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Toast.makeText(MainActivity.this, "وضعیت آنلاین", Toast.LENGTH_SHORT).show();
                        setonline ();
                        // tedad massege haro mishmare
                        count_msg ();
                        //darkhasht mifresteh bebine bazi jadid anjam shode ya na
                        if (checknewgame == true){
                            CheckNewGame();

                        }

                    }
                });
            }
        }, 0, 5000);
    }

    private void setonline (){

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.SETONLINE)
                .addBodyParameter("ItemsUsername",username)
                .setTag("setonline")
                .build()
                .getAsObject(User_Items.class, new ParsedRequestListener<User_Items>() {


                    @Override
                    public void onResponse(User_Items response) {

                        //  Toast.makeText(MainActivity.this,  response.getItemsUsername()+"", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("MyError", String.valueOf(anError));
                        // Toast.makeText(MainActivity.this, " خطا در اتصال", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // in method check mikonad bebine kasi darkhast bazi barash ferstade ya na
    private void CheckNewGame(){

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.CHECK_GAME_R)
                .addBodyParameter("ItemsUsername",username)
                .addBodyParameter("Tashkhis","CheckNewGame")
                .setTag("CheckNewGame")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String javab = "";
                        String harif = "";

                        try {
                            javab = response.getString("javab");
                            harif = response.getString("harifname");
                            harif_image = response.getString("harifimage");

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                        if ( javab.equals("error")|| javab.equals("no") || javab.equals("no req")){

                            Log.d("جواب نیو گیم", javab);
                        }else if ( javab.equals("yes")) {

             Toast.makeText(MainActivity.this, harif+":درخواست بازی جدید از ", Toast.LENGTH_SHORT).show();

                            Alert_New_Game(harif,harif_image);
                            checknewgame =false;
                            harif_name = harif;
                            Log.d("جواب نیو گیم", harif);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("خطا", String.valueOf(anError));

                    }
                });
               /* .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("جواب نیو گیم", String.valueOf(response));


                        if ( response.equals("error")|| response.equals("no") || response.equals("no req")){

                        }else {

                            Toast.makeText(MainActivity.this, response+":درخواست بازی جدید از ", Toast.LENGTH_SHORT).show();

                            Alert_New_Game(response+":درخواست بازی جدید از ");
                            checknewgame =false;
                            harif_name = response;
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(MainActivity.this, "خطا در اتصال", Toast.LENGTH_SHORT).show();
                        Log.d("خطا نیوگیم", String.valueOf(anError));
                    }
                });*/
    }

    /*public void Alert_New_Game (String peqam){

        AlertDialog.Builder Hoshdar = new AlertDialog.Builder(MainActivity.this);
        Hoshdar.setMessage(peqam);
        Hoshdar.setCancelable(false);
        Hoshdar.setPositiveButton("بازی میکنم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Game_r_Reply("1");
                checknewgame =false;
                Intent game_intent = new Intent(MainActivity.this,Game_board.class);
                game_intent.putExtra("username",username);
                game_intent.putExtra("nobat","player2");
                game_intent.putExtra("playerName",name);
                game_intent.putExtra("harifName",harif_name);
                game_intent.putExtra("harifImage",harif_image);
                game_intent.putExtra("MyImage",Myimage);
                game_intent.putExtra("get_harekat",false);
                game_intent.putExtra("setEnable",true);
                startActivity(game_intent);

            }
        });
        Hoshdar.setNegativeButton("نه بیخیال", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Game_r_Reply("2");
                checknewgame =true;
            }
        });

        Hoshdar.create().show();
    }*/


    //javab darkhat bazi ro mifreste be game_R
    private void Game_r_Reply(String meqdar) {

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.GAME_R_REPLY)
                .addBodyParameter("ItemsUsername",username)
                .addBodyParameter("ItemsMeqdar",meqdar)
                .addBodyParameter("Tashkhis","Game_r_Reply")
                .addBodyParameter("Nobate","player2")
                .setTag("Game_r_Reply")
                .build()
                .getAsObject(User_Items.class, new ParsedRequestListener<User_Items>() {
                    @Override
                    public void onResponse(User_Items response) {
                        //   response.getItemsUsername();
                    }

                    @Override
                    public void onError(ANError anError) {

                        //Toast.makeText(MainActivity.this, " خطا در اتصال", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void Create_msg_tbl(){

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.CREATE_TBL)
                .addBodyParameter("ItemsUsername",username)
                .setTag("Create_msg_tbl")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("جواب ساختن جدول", String.valueOf(response));


                        if ( response.equals("error")){

                        }else {
                      //Toast.makeText(MainActivity.this, "جدول با موفقیت ساخته شد", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                      //  Toast.makeText(MainActivity.this, "خطا در اتصال", Toast.LENGTH_SHORT).show();
                        Log.d("خطا ساختن جدول", String.valueOf(anError));
                    }
                });
    }
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
    public void Clear_Cache() {

        try {
            File[] mycache = getBaseContext().getCacheDir().listFiles();
            for (File junkfile : mycache) {
                junkfile.delete();

            }
            Log.d("Clear_Cache", "کش پاک شد");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private int get_NofMSG (){

        return 0;
    }

    private void count_msg (){

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.COUNT_MSG)
                .addBodyParameter("ItemsUsername",username)
                .setPriority(Priority.HIGH)
                .setTag("count_msg")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                     // Toast.makeText(MainActivity.this,  response, Toast.LENGTH_SHORT).show();


                        if (response.equals("error")){

                        }else {
                            // check kardan vziyat in ke aya masssege jadid dari ya na be sever conncet mishe mifahme ino
                            // tedad payam haye jadid dakhel server

                            nofMSG_server = Integer.parseInt(response);
                            // teda ayam haye ghabli dakhel share prefrence
                            nofmsg = sharedP.getInt(NofMSG,nofMSG_server);
                            Log.d(" تعداد پیام سرور", response);
                            Log.d(" تعداد پیام قبلی", String.valueOf(nofmsg));

                            int res_noofmsg = nofMSG_server - nofmsg;
                            Log.d(" تعداد پیام جدید", String.valueOf(res_noofmsg));
                            if (res_noofmsg==0){

                               // Toast.makeText(MainActivity.this, "No New Message", Toast.LENGTH_SHORT).show();
                                txt_toolbar.setText("");
                            }else if (res_noofmsg > 0){
                                txt_toolbar.setText(res_noofmsg+"");
                            }
                        }


                    }

                    @Override
                    public void onError(ANError anError) {


                        Log.d("خطا شمارش", String.valueOf(anError));
                    }
                });
    }

    private  void Alert_New_Game (final String harif_name, final String imageharif){


        final Dialog AddPlayerNames = new Dialog(this);

        AddPlayerNames.setCancelable(true);
        AddPlayerNames.getWindow();
        AddPlayerNames.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AddPlayerNames.setContentView(R.layout.newgame_req_alert);
        //AddPlayerNames.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
        AddPlayerNames.show();

        TextView txt_harifname =  AddPlayerNames.findViewById(R.id.txt_harifname);

        Button btn_deny =  AddPlayerNames.findViewById(R.id.btn_deny);
        Button btn_accept =  AddPlayerNames.findViewById(R.id.btn_accept);
        CircleImageView img_harif =  AddPlayerNames.findViewById(R.id.img_harifimage);

        txt_harifname.append(harif_name);

        Glide
                .with(this)
                .load(User_Items.HOST_NAME+imageharif)
               // .error(R.drawable.example)
                .into(img_harif);



        btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // javab darkhast bazi ro be fres be server
                Game_r_Reply("2");
                checknewgame =true;
                AddPlayerNames.dismiss();

                }
        });


        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Game_r_Reply("1");
                checknewgame =false;
                Intent game_intent = new Intent(MainActivity.this,Game_board.class);
                game_intent.putExtra("username",username);
                game_intent.putExtra("nobat","player2");
                game_intent.putExtra("playerName",name);
                game_intent.putExtra("harifName",harif_name);
                game_intent.putExtra("harifImage",harif_image);
                game_intent.putExtra("MyImage",Myimage);
                game_intent.putExtra("get_harekat",false);
                game_intent.putExtra("setEnable",true);
                startActivity(game_intent);
                finish();


            }
        });


    }


//بررسی وضعبت اینترنت
    public boolean InternetConnection()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnectedOrConnecting())
        {
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnectedOrConnecting())
        {
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
    //اضافه کردن تب تارگ ها برای راهنمایی کاربر
    private void taptarget(){

     new MaterialTapTargetPrompt.Builder(MainActivity.this)
            .setTarget(R.id.img_editprof)

                .setPrimaryText("Change Language")
                .setSecondaryText("Use to Change Language Between English/Persian")
                //.setPrimaryTextColour(Color.parseColor("#13dc74"))
           // .setSecondaryTextColour(Color.parseColor("#13dc74"))
            .setBackButtonDismissEnabled(false)
       .setBackgroundColour(Color.parseColor("#541f06"))
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
    {
        @Override
        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
        {
            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED|| state==MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED)
            {
                // User has pressed the prompt target
                Toast.makeText(MainActivity.this, "اولی", Toast.LENGTH_SHORT).show();
                txt_target();
            }
        }
    })
            .show();
    }

    private void txt_target(){
        new MaterialTapTargetPrompt.Builder(MainActivity.this)
                .setTarget(R.id.txt_toolbar)

                .setPrimaryText("Message Box")
                .setSecondaryText("Use To Read Or Send Personal Message")
                .setBackgroundColour(Color.parseColor("#541f06"))
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED )
                        {
                            // User has pressed the prompt target
                            Toast.makeText(MainActivity.this, "دومی", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }


}
