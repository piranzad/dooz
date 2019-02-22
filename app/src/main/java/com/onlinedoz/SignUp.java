package com.onlinedoz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

public class SignUp extends AppCompatActivity {

    EditText edt_name,edt_username,edt_password,edt_password_R, edt_age;
    Button btn_signup;
    TextView txt_login;
    SharedPreferences sharedP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edt_name = findViewById(R.id.edt_name);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        edt_password_R = findViewById(R.id.edt_password_R);
        edt_age = findViewById(R.id.edt_age);
        btn_signup = findViewById(R.id.btn_signup);
        txt_login = findViewById(R.id.txt_login);

        AndroidNetworking.initialize(this);
        // برای جلوگیری از تکرار تتعریف متغییر ها در اکتویتی اصلی اساستیک تعریف کردیم و اینا به صورت زیر فراخوانی می کنیم

        sharedP = getSharedPreferences(MainActivity.USERDATA, Context.MODE_PRIVATE);

        String textFont = sharedP.getString(MainActivity.FONT,"forte");
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/" + textFont + ".ttf");
        //edt_name.setTypeface(typeface);
        //edt_username.setTypeface(typeface);
        //edt_password.setTypeface(typeface);
        //edt_password_R.setTypeface(typeface);
       // edt_age.setTypeface(typeface);
        btn_signup.setTypeface(typeface);
        txt_login.setTypeface(typeface);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//در این قسمت فیلد ها اجباری یا ستاره دار می کنیم
                if (edt_name.length()==0 || edt_username.length()==0|| edt_password.length()==0|| edt_password_R.length()==0|| edt_age.length()==0){

                   // SendSMS();
                    Toast.makeText(SignUp.this, "لطفا همه موارد را وارد کنید", Toast.LENGTH_SHORT).show();
                }else {
// در این قسمت رمز عبور و تکرار آن برای همخوانی انجام می شود
                    if (edt_password.getText().toString().equals(edt_password_R.getText().toString())){

       AddUser(edt_name.getText().toString(),edt_username.getText().toString(),edt_password.getText().toString(), edt_age.getText().toString());


                    }else {
            Toast.makeText(SignUp.this, "رمز عبور با تکرار آن همخوانی ندارد", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SignUp.this,Login.class));
                finish();
            }
        });

    }



    private void AddUser(String name,String username, String password,String age){

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.ADDUSER)
                .addBodyParameter("ItemsName",name)
                .addBodyParameter("ItemsUsername",username)
                .addBodyParameter("ItemsPassword",password)
                .addBodyParameter("ItemsAge",age)
                .setTag("Sign up")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("success")){

                            startActivity(new Intent(SignUp.this,Login.class));
                            finish();
                            Toast.makeText(SignUp.this,  "حساب کاربری با موفقیت ایجاد شد", Toast.LENGTH_SHORT).show();
                        }else if (response.contains("tekrari")){

                            Toast.makeText(SignUp.this,  "این نام کاربری وجود دارد", Toast.LENGTH_SHORT).show();

                        }else {

                            Toast.makeText(SignUp.this,  "خطا در ایجاد حساب کاربری", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(SignUp.this,  "خطا در اتصال به اینترنت", Toast.LENGTH_SHORT).show();

                    }
                });
    }
    //سرویس ارسال پیامک  از سرویس های ارسال گیامک استفاده کنید
    private void SendSMS() {

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("09215455455", "", " Your password is : ", null, null);
        } catch (Exception e) {
            Toast.makeText(SignUp.this, "ارسال پیام ناموفق بود", Toast.LENGTH_LONG).show();

        }
    }

}
