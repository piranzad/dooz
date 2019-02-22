package com.onlinedoz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;



public class Comments extends AppCompatActivity {


    SharedPreferences sharedP;
    public static final String USERDATA = "UserData";
    public static final String USERNAME = "username";
    public static final String NAME = "name";
    public static final String IMAGE = "image";
  //  public static final String NofMSG = "nofmsg";

    String Myusername,msg,Myname,Myimage,Admin="no",btn;

    private RecyclerView Myrecycler;
    public ArrayList<Items_list> items = new ArrayList<>();
    public MsgAdapter Myadapter;
    LinearLayoutManager lin;
    public String[] Name_user,Username_user,Time_msg,Date_msg,Message,Images;
   /*public static ArrayList<String> Name_user = new ArrayList<>();
    public static ArrayList<String> Username_user = new ArrayList<>();
    public static ArrayList<String> Date_msg = new ArrayList<>();
    public static ArrayList<String> Message = new ArrayList<>();
    public static ArrayList<String> Images = new ArrayList<>();
    public static ArrayList<String> Time_msg = new ArrayList<>();*/
    EditText edt_comment;
    ImageView submitButton;
    int page_number = 0;
    int firstVisible;
    boolean get_data =false;
    RelativeLayout rlt_sendmsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Clear_Cache();

        Myrecycler = findViewById(R.id.recycler_chatroom);
        submitButton =  findViewById(R.id.submit_btn);
        edt_comment =  findViewById(R.id.emojicon_edit_text);
        rlt_sendmsg =  findViewById(R.id.rlt_sendmsg);

        Myadapter = new MsgAdapter(items,Comments.this);

        AndroidNetworking.initialize(Comments.this);

        sharedP = getSharedPreferences(USERDATA, Context.MODE_PRIVATE);
        Myusername = sharedP.getString(USERNAME,"");
        Myname = sharedP.getString(NAME,"");
        Myimage = sharedP.getString(IMAGE,"test.jpg");

         lin = new LinearLayoutManager(Comments.this);
         // metod zir miravad be akharin peygham
        lin.setStackFromEnd(false);
        Myrecycler.setLayoutManager(lin);
        // Myrecycler.setLayoutManager(new GridLayoutManager(List_online.this,2));
        Myrecycler.setAdapter(Myadapter);


        Check_admin ();
         btn = getIntent().getStringExtra("btn");
        if (btn.equals("nav_comments")){
            Get_comments();
        }else if (btn.equals("btn_msg")){
            int nofMSG_server = getIntent().getIntExtra("nofmsg",0);
            sharedP.edit().putInt(MainActivity.NofMSG, nofMSG_server).apply();
            Get_message();
            //agar az commnet raft message bede agar az messeg box arft commnet hari mibine va mitone ro commnet clik kone va ye seri vizhgeyi dare
            edt_comment.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);
            rlt_sendmsg.setVisibility(View.GONE);
        }


        // avalin didan commnet
        firstVisible = lin.findFirstVisibleItemPosition();
//safe safe payam haro be ma neshon mide va barname hang nemikoneh
        Myrecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                int currentFirstVisible = lin.findLastVisibleItemPosition();


                if (get_data){


                    if(currentFirstVisible > firstVisible){
                        //Toast.makeText(Comments.this, "به سمت بالا", Toast.LENGTH_SHORT).show();
                    } else{
                        //scrool kardan ke lazy load be on migoyand ke aval sefre bad zabdar yek mishavad  (pagenumber)ke dar file php mojode

                        get_data=false;
                        page_number++;
                        if (btn.equals("nav_comments")){
                            Get_comments();
                        }else if (btn.equals("btn_msg")){
                            Get_message();
                        }
                        //Toast.makeText(Comments.this, "به سمت پایین", Toast.LENGTH_SHORT).show();
                    }
                    firstVisible = currentFirstVisible;
                }


            }
            //jalbe emtehanesh kon

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
              /* if (dy > 0) {
                    //Scrolling up
                    //Toast.makeText(Comments.this, "به سمت بالا", Toast.LENGTH_SHORT).show();
                   // Log.i("RecyclerView scrolled: ", "scroll up!");
                } else {
                    // Scrolling down
                   // Toast.makeText(Comments.this, "به سمت پایین", Toast.LENGTH_SHORT).show();
                   // Log.i("RecyclerView scrolled: ", "scroll down!");

                   }*/
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_comment.getText().length()>0){

                    msg = edt_comment.getText().toString();
                    Add_comment();
                    edt_comment.setText("");
                    Toast.makeText(Comments.this, msg+"", Toast.LENGTH_SHORT).show();

                    items.clear();

                    Get_comments();
                }
            }
        });

    }

    private void Add_comment(){

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.ADDMSG)
                .addBodyParameter("ItemsName",Myname)
                .addBodyParameter("ItemsUsername",Myusername)
                .addBodyParameter("ItemsImage",Myimage)
                .addBodyParameter("ItemsMsg",msg)
                .setPriority(Priority.HIGH)
                .setTag("addmsg")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("success")){

            Toast.makeText(Comments.this,  "پیام شما ارسال شد", Toast.LENGTH_SHORT).show();
                        }else if (response.contains("fail")){

     Toast.makeText(Comments.this,  "پیام ارسال نشد", Toast.LENGTH_SHORT).show();

                        }else {

                            Toast.makeText(Comments.this,  "خطا در ارسال پیام", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(Comments.this,  "خطا در اتصال به اینترنت", Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void Get_comments(){

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.GETCOMMENTS)
                .addBodyParameter("ItemsPage", String.valueOf(page_number))
                .setTag("getMsg")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {


                        try {
                            //JSONObject object =response.getJSONObject(0);
                            // String name = object.getString("name");
                            //Toast.makeText(List_online.this, name, Toast.LENGTH_SHORT).show();
                            int tedad = response.length();


                          /* Name_user = new String[tedad];
                            Username_user = new String[tedad];
                            Time_msg = new String[tedad];
                            Date_msg = new String[tedad];
                            Message = new String[tedad];
                            Images = new String[tedad];*/

                           /* if (Admin.equals("yes")){

                                MsgAdapter.IDS = new int[tedad];
                            }*/

                          /* Name_user.clear();
                           Username_user.clear();
                           Time_msg.clear();
                           Date_msg.clear();
                           Message.clear();
                           Images.clear();*/

                           // MsgAdapter.IDS.clear();
                           // MsgAdapter.username_dialog.clear();

                            for (int i = 0; i<response.length(); i++)
                            {
                                JSONObject object =response.getJSONObject(i);
                                String msg_time = object.getString("msg_time");
                                String name = object.getString("name");
                                String username = object.getString("username");
                                String msg_date = object.getString("msg_date");
                                String msg = object.getString("msg");
                                String image = object.getString("image");
                                String confirmed = object.getString("confirmed");
                                String id = object.getString("id");

                               /* Name_user[i]= name;
                                Username_user[i]= username;
                                Time_msg[i]= msg_time;
                                Date_msg[i]= msg_date;
                                Message[i]= msg;
                                Images[i]= msg;*/
                                /*Name_user.add(name);
                                Username_user.add(username);
                                Time_msg.add(msg_time);
                                Date_msg.add(msg_date);
                                Message.add(msg);
                                Images.add(image);

                               /* if (Admin.equals("yes")){
                                   MsgAdapter.IDS.add(id);
                                }*/

                               // MsgAdapter.username_dialog.add(username);


                                // chek kardan admin in commnet ha
                                if (confirmed.equals("0") && Admin.equals("yes")){

       items.add(new Items_list(image,name,username,"",msg,msg_time,msg_date,confirmed,id,""));
                                }else if (confirmed.equals("0") && Admin.equals("no")){

                                }else if (confirmed.equals("1")){

           items.add(new Items_list(image,name,username,"",msg,msg_time,msg_date,confirmed,id,""));
                                }
                                MsgAdapter.BTN = "nav_comments";
                                MsgAdapter.USERNAME_mabda = Myusername;
                                MsgAdapter.name_mabda = Myname;
                                MsgAdapter.IMAGE_Mabda = Myimage;


                            }
                            Myadapter.notifyDataSetChanged();
                            get_data = true;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void Check_admin (){

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.CHECK_ADMIN)
                .addBodyParameter("ItemsUsername",Myusername)
                .setPriority(Priority.HIGH)
                .setTag("Check_admin")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("yes")){

                            Toast.makeText(Comments.this,  "شما ادمین هستید", Toast.LENGTH_SHORT).show();

                            MsgAdapter.ADMIN =1;
                            Admin = "yes";

                        }else if (response.contains("no")){

                            Toast.makeText(Comments.this,  "شما ادمین نیستید", Toast.LENGTH_SHORT).show();

                            MsgAdapter.ADMIN = 0;
                            Admin = "no";

                        }else {

                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(Comments.this,  "خطا در اتصال به اینترنت", Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void Get_message(){

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.GET_MSG)
                .addBodyParameter("ItemsPage", String.valueOf(page_number))
                .addBodyParameter("ItemsUsername",Myusername)
                .setTag("Get_message")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {


                        try {
                            //JSONObject object =response.getJSONObject(0);
                            // String name = object.getString("name");
                            //Toast.makeText(List_online.this, name, Toast.LENGTH_SHORT).show();
                           /* int tedad = response.length();
                           Name_user = new String[tedad];
                            Username_user = new String[tedad];
                            Time_msg = new String[tedad];
                            Date_msg = new String[tedad];
                            Message = new String[tedad];
                            Images = new String[tedad];*/

                             //MsgAdapter.IDS = new int[tedad];

                            for (int i = 0 ; i<response.length();i++)
                            {
                                JSONObject object =response.getJSONObject(i);
                                String msg_time = object.getString("msgtime");
                                String name = object.getString("name");
                                String username = object.getString("username");
                                String msg_date = object.getString("msgdate");
                                String msg = object.getString("message");
                                String image = object.getString("image");
                                String block = object.getString("block");
                                String id = object.getString("id");

                               /* Name_user[i]= name;
                                Username_user[i]= username;
                                Time_msg[i]= msg_time;
                                Date_msg[i]= msg_date;
                                Message[i]= msg;
                                Images[i]= msg;*/
                                /*Name_user.add(name);
                                Username_user.add(username);
                                Time_msg.add(msg_time);
                                Date_msg.add(msg_date);
                                Message.add(msg);
                                Images.add(image);*/

                                // MsgAdapter.IDS.add(id);



                  items.add(new Items_list(image,name,username,"",msg,msg_time,msg_date,"",id,block));

                                MsgAdapter.BTN = "btn_msg";
                                MsgAdapter.USERNAME_mabda = Myusername;
                                MsgAdapter.name_mabda = Myname;
                                MsgAdapter.IMAGE_Mabda = Myimage;

                            }
                            Myadapter.notifyDataSetChanged();
                            get_data = true;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
    //khali kardan chash commnet ha

    public void Clear_Cache(){

        try {
            File[] mycache = getBaseContext().getCacheDir().listFiles();
            for (File junkfile : mycache){
                junkfile.delete();

            }
            Log.d("پاک کردن کش", "کش پاک شد");
        }catch (Exception e){
           e.printStackTrace();
        }



    }

}
