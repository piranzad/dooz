package com.onlinedoz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfile extends AppCompatActivity {

    CircleImageView img_user;
    Button btn_save,btn_camera,btn_gallery;
    String img_patch,username,image_camera_st,name,password, age;

    private File imageFile;
    private Bitmap bm;

    private ProgressDialog dialog = null;

    SharedPreferences sharedP;
    public static final String USERDATA = "UserData";
    public static final String USERNAME = "username";
    public static final String IMAGE = "image";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String AGE = "age";
    EditText editName,editUsername,edtPassword,editPassword_R, ediAge;
    Button btn_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addimage);

        img_user = findViewById(R.id.img_user);
        btn_save = findViewById(R.id.btn_save);
        btn_camera = findViewById(R.id.btn_camera);
        btn_gallery = findViewById(R.id.btnGallery);

        editName = findViewById(R.id.edt_Editname);
        editUsername = findViewById(R.id.edt_Editusername);
        edtPassword = findViewById(R.id.edt_Editpassword);
        editPassword_R = findViewById(R.id.edt_Editpassword_R);
        ediAge = findViewById(R.id.edt_EditAge);
        btn_edit = findViewById(R.id.btn_edit);

        AndroidNetworking.initialize(EditProfile.this);


        sharedP = getSharedPreferences(USERDATA, Context.MODE_PRIVATE);

         username = sharedP.getString(USERNAME,"");

        name = sharedP.getString(NAME,"");
        username = sharedP.getString(USERNAME,"");
        password = sharedP.getString(PASSWORD,"");
        age = sharedP.getString(AGE,"");

        editName.setText(name);
        editUsername.setText(username);
        edtPassword.setText(password);
        editPassword_R.setText(password);
        ediAge.setText(age);

        //Toast.makeText(this, username+"", Toast.LENGTH_SHORT).show();

        String textFont = sharedP.getString(MainActivity.FONT,"bmorvarid");
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/" + textFont + ".ttf");
        editName.setTypeface(typeface);
        editUsername.setTypeface(typeface);
        edtPassword.setTypeface(typeface);
        editPassword_R.setTypeface(typeface);
        ediAge.setTypeface(typeface);
        btn_save.setTypeface(typeface);
        btn_edit.setTypeface(typeface);


        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                permission();

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                set_image(image_camera_st);
                 //upload_img ();
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                camera();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtPassword.getText().toString().equals(editPassword_R.getText().toString()) ){

                    Edit_Profile(editName.getText().toString(),username,edtPassword.getText().toString(), ediAge.getText().toString());
                }else {
                    Toast.makeText(EditProfile.this, "رمز عبور با تکرار آن همخوانی ندارد", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void get_image(){

        /*Intent myintent = new Intent(Intent.ACTION_GET_CONTENT);
        myintent.setType("image/*");
        startActivityForResult(Intent.createChooser(myintent,"لطفا تصویر را انتخاب کنید"),1);*/

        // bara entekhab ax az galeri
        //az actionpick estefade mikonim ke betonim harchi khastim bar darim ke inja img bar midarim
        Intent myintent = new Intent(Intent.ACTION_PICK);
        // format in image ro badan taghir bede
        myintent.setType("image/*");
        startActivityForResult(myintent, 50);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /*if (requestCode == 1 && resultCode == Activity.RESULT_OK && data.getData() !=null){
           final Uri selectedImageUri = data.getData();
           img_user.setImageURI(data.getData());
           String imagepath = getRealPathFromURI(selectedImageUri,EditProfile.this);
           imageFile = new File(imagepath);
          // Toast.makeText(this, imagepath, Toast.LENGTH_SHORT).show();
        }*/

        if (requestCode == 50 && resultCode == RESULT_OK) {

                final Uri selectedImageUri = data.getData();
               img_user.setImageURI(data.getData());
                //String imagepath = getRealPathFromURI(selectedImageUri,this);
                // imageFile = new File(imagepath);
           // Toast.makeText(this, imagepath, Toast.LENGTH_SHORT).show();

            bm = ((BitmapDrawable) img_user.getDrawable()).getBitmap();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG,50,stream);
            byte[] bytes = stream.toByteArray();
            image_camera_st = Base64.encodeToString(bytes,Base64.DEFAULT);

    }

    if (requestCode == 24 && resultCode == RESULT_OK){

        Bundle Mybundle = data.getExtras();
        bm = (Bitmap) Mybundle.get("data");
        img_user.setImageBitmap(bm);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,80,stream);
        byte[] bytes = stream.toByteArray();
        image_camera_st = Base64.encodeToString(bytes,0);

    }


    }

    // ravesh ghadmimi ke test kardim jadiesh payine to set image
    /*private void upload_img (){

        AndroidNetworking.upload(User_Items.HOST_NAME+User_Items.UPLOADIMG)
              // .addMultipartFile("Itemsimage",new File("/sdcard/Download/aa.jpg"))
               .addMultipartFile("Itemsimage", imageFile)
                .addMultipartParameter("ItemsUsername",username)
                .setPriority(Priority.HIGH)
                .setTag("uploadimg")
                .build()
                .getAsObject(User_Items.class, new ParsedRequestListener<User_Items>() {
            @Override
            public void onResponse(User_Items response) {


                SharedPreferences.Editor edit = sharedP.edit();

                edit.putString(IMAGE, response.getItemsimage());
                edit.apply();
                    startActivity(new Intent(EditProfile.this,MainActivity.class));
                    finish();
                    Toast.makeText(EditProfile.this,  response.getItemsimage()+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(ANError anError) {
                Log.d("خطا", String.valueOf(anError));
                Toast.makeText(EditProfile.this, " خطا در اتصال", Toast.LENGTH_SHORT).show();
            }
        });*/
    private void permission(){

        Dexter.withActivity(EditProfile.this)
                .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        get_image();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        if (response !=null && response.isPermanentlyDenied()){
                            // baraye namayesh permision ayine safe ye halate tost mide ke permison ro bede

                            Snackbar.make(btn_save,"لطفا اجازه دسترسی را تایید کنید",Snackbar.LENGTH_INDEFINITE).setAction("اجازه دادن", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.fromParts("package",getPackageName(),null));
                                    startActivity(intent);
                                }
                            }).show();
                        }else {

                            Snackbar.make(btn_save,"لطفا اجازه دسترسی را تایید کنید",Snackbar.LENGTH_INDEFINITE).show();

                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, final PermissionToken token) {

             Snackbar.make(btn_save,"لطفا اجازه دسترسی را تایید کنید",Snackbar.LENGTH_INDEFINITE).setAction("اجازه دادن", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                token.cancelPermissionRequest();
                            }
                        }).show();
                    }
                }).check();


    }
    //gerftan be ravaesh digar agar nakhsti pakesh kon


  /*  public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }


    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
*/


    private void camera(){

     if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){

         Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         startActivityForResult(camera_intent,24);

     }else {
         Toast.makeText(EditProfile.this, "NO Camera", Toast.LENGTH_SHORT).show();
     }

 }
    private void set_image(final String imagestring) {

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.SET_IMAGE)
                .addBodyParameter("images", imagestring)
                .addBodyParameter("ItemsUsername",username)
                .setTag("set_image")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {



                        if ( response.equals("OK")){

                            Toast.makeText(EditProfile.this, "انجام شد", Toast.LENGTH_SHORT).show();
                        }else {

                            Toast.makeText(EditProfile.this, "خطا", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        // Toast.makeText(context, "خطا در اتصال", Toast.LENGTH_SHORT).show();
                        Log.d("خطا ذخیره تصویر", String.valueOf(anError));
                    }
                });
    }

    private void Edit_Profile(String name,String username,String password,String age) {

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.EDITPROFILE)
                .addBodyParameter("ItemsName",name)
                .addBodyParameter("ItemsUsername",username)
                .addBodyParameter("ItemsPassword",password)
                .addBodyParameter("ItemsAge",age)
                .setTag("edit")
                .build()
                .getAsObject(User_Items.class, new ParsedRequestListener<User_Items>() {


                    @Override
                    public void onResponse(User_Items response) {

                        SharedPreferences.Editor edit = sharedP.edit();
                        edit.putString(USERNAME, response.getItemsUsername());
                        edit.putString(NAME, response.getItemsName());
                        edit.putString(PASSWORD, response.getItemsPassword());
                        edit.putString(AGE, response.getItemsAge());
                        edit.apply();

                        startActivity(new Intent(EditProfile.this,MainActivity.class));
                        Toast.makeText(EditProfile.this,  "اطلاعات پروفایل شما ویرایش شد", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onError(ANError anError) {

                      //  Toast.makeText(EditProfile.this, " خطا در اتصال", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

 /*AndroidNetworking.upload(User_Items.HOST_NAME+User_Items.UPLOAD)
         //.addMultipartFile("Itemsimage",new File(img_patch))
         .addMultipartFile("file",new File("/sdcard/Download/aa.jpg"))
         .addMultipartParameter("ItemsUsername",username)
         .setTag("uploadimg")
         .build()
         .getAsString(new StringRequestListener() {
@Override
public void onResponse(String response) {
        Log.d("خطا1", String.valueOf(response));
        if (response.contains("done")){

        Toast.makeText(EditProfile.this, "فایل با موفقیت آپلود شد", Toast.LENGTH_SHORT).show();
        }else {
        Toast.makeText(EditProfile.this, "نا موفق", Toast.LENGTH_SHORT).show();

        }

        }

@Override
public void onError(ANError anError) {

        Toast.makeText(EditProfile.this, "خطا در اتصال", Toast.LENGTH_SHORT).show();
        Log.d("خطا", String.valueOf(anError));
        }
        });*/