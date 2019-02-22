package com.onlinedoz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public  class  List_online extends AppCompatActivity {

    TextView txt_title;
    private RecyclerView Myrecycler;
    private ArrayList<Items_list> items = new ArrayList<>();
    private MyAdapter Myadapter;

    public String[] Name, UserName, Image_fr,Age;
    int pos;
    DatabaseManager dbManager = new DatabaseManager(this);


    SharedPreferences sharedP;
    public static final String USERDATA = "UserData";
    public static final String NAME = "name";
    public static final String USERNAME = "username";
    public static final String IMAGE = "image";
    String myname, myusername, myimage;
    boolean get_reply_game_r = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Clear_Cache();

        sharedP = getSharedPreferences(USERDATA, Context.MODE_PRIVATE);
        String currentLang = sharedP.getString(MainActivity.LANGUAGE, "fa");
        setLocale(currentLang);
        setContentView(R.layout.activity_list_online);



        checkOnline();

        Myrecycler = (RecyclerView) findViewById(R.id.recyclerview);
        txt_title =  findViewById(R.id.txt_title_);
        Myadapter = new MyAdapter(items, List_online.this);

        Myadapter.Fr_activity = "List_online";
        // LinearLayoutManager linearLayoutManager =
        //  new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
        // Myrecycler.setLayoutManager(linearLayoutManager);

/* AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(Myadapter);
  SlideInRightAnimationAdapter slideInRightAnimationAdapter = new SlideInRightAnimationAdapter(animationAdapter);
  slideInRightAnimationAdapter.setDuration(300);
   slideInRightAnimationAdapter.setFirstOnly(false);
  Myrecycler.setAdapter(slideInRightAnimationAdapter);*/
        // Myrecycler.setLayoutManager(new GridLayoutManager(MainActivity.this,3));

        Myrecycler.setLayoutManager(new LinearLayoutManager(List_online.this));
        // Myrecycler.setLayoutManager(new GridLayoutManager(List_online.this,2));
        Myrecycler.setAdapter(Myadapter);

        myname = sharedP.getString(NAME, "");
        myimage = sharedP.getString(IMAGE, "test.jpg");
        myusername = sharedP.getString(USERNAME, "");

        String textFont = sharedP.getString(MainActivity.FONT,"forte");
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/" + textFont + ".ttf");
        txt_title.setTypeface(typeface);
        Myrecycler.addOnItemTouchListener(new RecyclerItemClick(getBaseContext(), new RecyclerItemClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                showUserInfo(Name[position], UserName[position], Image_fr[position],Age[position]);

                pos = position;
            }
        }));
    }


    private void checkOnline() {

        AndroidNetworking.post(User_Items.HOST_NAME + User_Items.CHECKONLINE)

                .setTag("checkonline")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {


                        try {
                            //JSONObject object =response.getJSONObject(0);
                            // String name = object.getString("name");
                            //Toast.makeText(List_online.this, name, Toast.LENGTH_SHORT).show();
                            int tedad = response.length();
                            Name = new String[tedad];
                            UserName = new String[tedad];
                            Image_fr = new String[tedad];
                            Age = new String[tedad];

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                String username = object.getString("username");
                                String name = object.getString("name");
                                String image = object.getString("image");
                                String age = object.getString("age");

                                Name[i] = name;
                                UserName[i] = username;
                                Image_fr[i] = image;
                                Age[i] = age;
                                items.add(new Items_list(image, name, username, "online", "", "", "", "", "", ""));
                            }
                            Myadapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void showUserInfo(final String name, final String username, String image,String Age) {

      /*  AndroidNetworking.post(User_Items.HOST_NAME+User_Items.SHOWUSERINFO)
                .addBodyParameter("ItemsUsername",username)
                .setTag("showUserInfo")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {


                        try {
                            //JSONObject object =response.getJSONObject(0);
                            // String name = object.getString("name");
                            //Toast.makeText(List_online.this, name, Toast.LENGTH_SHORT).show();

                                JSONObject object =response.getJSONObject(0);
                                String username = object.getString("username");
                                String name = object.getString("name");
                                String image = object.getString("image");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });*/

        final Dialog AddPlayerNames = new Dialog(this);

        AddPlayerNames.setCancelable(true);
        AddPlayerNames.getWindow();
        AddPlayerNames.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AddPlayerNames.setContentView(R.layout.show_user_info);
        //AddPlayerNames.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
        AddPlayerNames.show();

        TextView txt_name = (TextView) AddPlayerNames.findViewById(R.id.txt_name_show);
        TextView txt_username = (TextView) AddPlayerNames.findViewById(R.id.txt_age_show);
        TextView txt_email = (TextView) AddPlayerNames.findViewById(R.id.txt_email_show);
        Button btn_add = (Button) AddPlayerNames.findViewById(R.id.btn_add);
        Button btn_game = (Button) AddPlayerNames.findViewById(R.id.btn_game);
        Button btn_send_msg = (Button) AddPlayerNames.findViewById(R.id.btn_send_msg);
        Button btn_block = (Button) AddPlayerNames.findViewById(R.id.btn_block);
        CircleImageView img = (CircleImageView) AddPlayerNames.findViewById(R.id.img_user_show);

        txt_name.setText(name);
        txt_username.setText(Age);

        btn_block.setVisibility(View.INVISIBLE);
        Glide
                .with(this)
                .load(User_Items.HOST_NAME + image)
                .into(img);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                boolean b = dbManager.check(UserName[pos]);
                if (b) {
                    Toast.makeText(List_online.this, "این فرد قبلا به لیست دوستان اضافه شده است", Toast.LENGTH_SHORT).show();
                } else {

                    dbManager.insertFr(Name[pos], UserName[pos], Image_fr[pos]);

                    Toast.makeText(List_online.this, "به لیست دوستان اضافه شد", Toast.LENGTH_SHORT).show();

                }
            }
        });

        btn_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                darkhaste_bazi(username, myname, "0");
                AddPlayerNames.cancel();

            }
        });
        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                send_msg_dialog(username);
                AddPlayerNames.cancel();

            }
        });
    }

    // methiod dar khast bazi bar in ke ye nafar pishnahad bazi midahad
    private void darkhaste_bazi(String username, String harif, String meqdar) {

        AndroidNetworking.post(User_Items.HOST_NAME + User_Items.GAME_R)
                .addBodyParameter("ItemsUsername", username)
                .addBodyParameter("ItemsHarif", harif)
                .addBodyParameter("HarifImage", myimage)
                .addBodyParameter("ItemsMeqdar", meqdar)
                .setTag("darkhaste_bazi")
                .build()
                .getAsObject(User_Items.class, new ParsedRequestListener<User_Items>() {


                    @Override
                    public void onResponse(User_Items response) {
                        //   response.getItemsUsername();
                    }

                    @Override
                    public void onError(ANError anError) {

                      //  Toast.makeText(List_online.this, " خطا در اتصال", Toast.LENGTH_SHORT).show();
                        Log.d("خطا درخواست بازی", String.valueOf(anError));
                    }
                });

        get_reply_game_r = true;
        Timer();

    }

    private void Get_Reply_Game_r() {

        AndroidNetworking.post(User_Items.HOST_NAME + User_Items.CHECK_GAME_R)
                .addBodyParameter("ItemsUsername", UserName[pos])
                .addBodyParameter("Tashkhis", "Get_Reply_Game_r")
                .setTag("Get_Reply_Game_r")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String javab = "";
                        String harif = "";
                        String nobat = "";
                        try {
                            javab = response.getString("javab");
                            harif = response.getString("harifname");
                            nobat = response.getString("nobat");

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                        if (javab.equals("error") || javab.equals("no req")) {

                            Timer();
                            Log.d("جواب درخواست", javab);
                        } else if (javab.equals("no")) {
                       Toast.makeText(List_online.this, "درخواست بازی رد شد ", Toast.LENGTH_SHORT).show();
                            get_reply_game_r = false;
                        } else if (javab.equals("yes")) {

                            Timer();

                        } else if (nobat.equals("player2")|| javab.equals("")) {

                    Toast.makeText(List_online.this, "درخواست بازی پذیرفته شد ", Toast.LENGTH_SHORT).show();
                            get_reply_game_r = false;
                            new MainActivity().checknewgame = false;

                            Intent game_intent = new Intent(List_online.this, Game_board.class);
                            game_intent.putExtra("username", UserName[pos]);
                            game_intent.putExtra("nobat", nobat);
                            game_intent.putExtra("playerName", myname);
                            game_intent.putExtra("harifName", Name[pos]);
                            game_intent.putExtra("harifImage", Image_fr[pos]);
                            game_intent.putExtra("MyImage", myimage);
                            game_intent.putExtra("get_harekat", true);
                            game_intent.putExtra("setEnable", false);
                            startActivity(game_intent);
                        }
                        Log.d("جواب درخواست", javab);
                    }


                    @Override
                    public void onError(ANError anError) {

                        Log.d("خطا", String.valueOf(anError));

                    }
                });
                /*.getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {



                        String result = response;

                        if ( response.equals("error") || response.equals("no req")){

                            Timer ();

                        }else if (response.equals("no")){

                    Toast.makeText(List_online.this, "درخواست بازی رد شد ", Toast.LENGTH_SHORT).show();
                            get_reply_game_r=false;

                        }else if (response.equals("player2")){
                   Toast.makeText(List_online.this, "درخواست بازی پذیرفته شد ", Toast.LENGTH_SHORT).show();
                            get_reply_game_r =false;
                            new MainActivity().checknewgame=false;

                            Intent game_intent = new Intent(List_online.this,Game_board.class);
                            game_intent.putExtra("username",UserName[pos]);
                            game_intent.putExtra("nobat",response);
                            game_intent.putExtra("playerName",harif);
                            game_intent.putExtra("harifName",Name[pos]);
                            game_intent.putExtra("harifImage",Image_fr[pos]);
                            game_intent.putExtra("MyImage",IMAGE);
                            game_intent.putExtra("get_harekat",true);
                            game_intent.putExtra("setEnable",false);
                            startActivity(game_intent);
                        }else if (response.equals(harif)){

                            Timer ();
                        }
                        Log.d("جواب درخواست", String.valueOf(response));

                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(List_online.this, "خطا در اتصال", Toast.LENGTH_SHORT).show();
                        Log.d("خطا", String.valueOf(anError));
                    }
                });*/
    }

    private void Timer() {
      /*  Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Toast.makeText(MainActivity.this, "وضعیت آنلاین", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 0, 10000);*/

        if (get_reply_game_r == true) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Get_Reply_Game_r();
                }
            }, 5000);

        }

    }

    public void send_msg_dialog(final String username_maqsad) {

        final Dialog sendmsgdialog = new Dialog(this);

        sendmsgdialog.setCancelable(true);
        sendmsgdialog.getWindow();
        sendmsgdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sendmsgdialog.setContentView(R.layout.send_message);
        //AddPlayerNames.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
        sendmsgdialog.show();

        final EditText edt_msg = sendmsgdialog.findViewById(R.id.edt_message);
        Button btn_send_msg = (Button) sendmsgdialog.findViewById(R.id.btn_send_message);


        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = edt_msg.getText().toString();
                send_msg(username_maqsad, msg);
                sendmsgdialog.cancel();

            }
        });

    }

    private void send_msg(String username_maqsad, String msg) {

        AndroidNetworking.post(User_Items.HOST_NAME + User_Items.SEND_MSG)
                .addBodyParameter("ItemsName", myname)
                .addBodyParameter("ItemsUsername_mabda", myusername)
                .addBodyParameter("ItemsUsername_maqsad", username_maqsad)
                .addBodyParameter("ItemsMsg", msg)
                .addBodyParameter("ItemsImage", myimage)
                .setPriority(Priority.HIGH)
                .setTag("send_msg")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("success")) {

                            Toast.makeText(List_online.this, "پیام شما ارسال شد", Toast.LENGTH_SHORT).show();
                        } else if (response.contains("fail")) {

                            Toast.makeText(List_online.this, "پیام ارسال نشد", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(List_online.this, "خطا در ارسال پیام", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(List_online.this, "خطا در اتصال به اینترنت", Toast.LENGTH_SHORT).show();

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
}