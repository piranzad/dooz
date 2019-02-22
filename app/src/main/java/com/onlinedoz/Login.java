package com.onlinedoz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

public class Login extends AppCompatActivity {


    EditText edt_user,edt_pass;
    Button btn_login;
    TextView txt_newacount;
    SharedPreferences sharedP;

    //public static final String USERDATA = "UserData";
   // public static final String USERNAME = "username";
   // public static final String NAME = "name";
   // public static final String PASSWORD = "password";
   // public static final String AGE = "age";
   // public static final String IMAGE = "image";
    //public static final String LANGUAGE = "language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_user = (EditText)findViewById(R.id.edt_username_L);
        edt_pass = (EditText)findViewById(R.id.edt_password_L);
        txt_newacount = findViewById(R.id.txt_newacount);

        btn_login = (Button)findViewById(R.id.btn_login);


        AndroidNetworking.initialize(this);

        sharedP = getSharedPreferences(MainActivity.USERDATA, Context.MODE_PRIVATE);

        String textFont = sharedP.getString(MainActivity.FONT,"forte");
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/" + textFont + ".ttf");
        //edt_user.setTypeface(typeface);
        //edt_pass.setTypeface(typeface);
        btn_login.setTypeface(typeface);
        txt_newacount.setTypeface(typeface);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edt_user.length()==0 && edt_pass.length()==0){
                    Toast.makeText(Login.this, "Please Fill All Field", Toast.LENGTH_SHORT).show();
                }else {
                    Login(edt_user.getText().toString(),edt_pass.getText().toString());
                }

            }
        });

        txt_newacount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,SignUp.class));
                finish();
            }
        });
        // marbot be emalil ma ferstand ramz obro faramos shode

      /*  MqttConnectOptions options = new MqttConnectOptions();
        options.setConnectionTimeout(60);
        options.setWill(STATUS, ("PASS_OFFLINE_STATUS_OF_USER").getBytes(), 0, true);
        client.setCallback(ApplozicMqttService.this);

        client.connect(options);*/
    }


    private void Login(final String username, String password){

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.LOGIN)
                .addBodyParameter("ItemsUsername",username)
                .addBodyParameter("ItemsPassword",password)
                .setTag("logIn")
                .build()
               .getAsObject(User_Items.class, new ParsedRequestListener<User_Items>() {


                   @Override
                   public void onResponse(User_Items response) {

                       if (response.getItemsUsername().toLowerCase().equals(username.toLowerCase())){


                       SharedPreferences.Editor edit = sharedP.edit();
                      edit.putString(MainActivity.USERNAME, response.getItemsUsername());
                       edit.putString(MainActivity.NAME, response.getItemsName());
                       edit.putString(MainActivity.PASSWORD, response.getItemsPassword());
                       edit.putString(MainActivity.AGE, response.getItemsAge());
                       edit.putString(MainActivity.IMAGE, response.getItemsimage());
                       edit.putString(MainActivity.LANGUAGE, "en");
                       edit.apply();



                        startActivity(new Intent(Login.this,MainActivity.class));
                        finish();
                       Toast.makeText(Login.this,  response.getItemsName()+"", Toast.LENGTH_SHORT).show();


                       }else {
                          Toast.makeText(Login.this, "نام کاربری یا رمز عبور اشتباه است", Toast.LENGTH_SHORT).show();
                        }
                   }

                   @Override
                   public void onError(ANError anError) {

                       Toast.makeText(Login.this, " خطا در اتصال", Toast.LENGTH_SHORT).show();
                   }
               });
                /*.getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            String aa = response.getString("username");
                            Toast.makeText(Login.this, aa+"", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                        Log.d("res", String.valueOf(response));
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("خطا", String.valueOf(anError));

                    }
                });*/
    }
}
