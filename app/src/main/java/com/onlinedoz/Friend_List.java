package com.onlinedoz;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.util.ArrayList;

public class Friend_List extends AppCompatActivity {

    TextView txt_title;
    private RecyclerView Myrecycler;
    public ArrayList<Items_list> items = new ArrayList<>();
    public MyAdapter Myadapter;

    private String[] Name,UserName,LastSeen,Image_fr;

    DatabaseManager dbManager = new DatabaseManager(this);

     String result;
    SharedPreferences sharedP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend__list);

        txt_title = findViewById(R.id.txt_title);


        Myrecycler = findViewById(R.id.recyclerview);
        Myadapter = new MyAdapter(items,Friend_List.this);

         Myrecycler.setLayoutManager(new LinearLayoutManager(Friend_List.this));
        //Myrecycler.setLayoutManager(new GridLayoutManager(Friend_List.this,2));
        Myrecycler.setAdapter(Myadapter);

        Creatlist();
       // new MyAdapter(items,Friend_List.this).Fr_activity ="yes";
        Myadapter.Fr_activity = "Friend_List";

        sharedP = getSharedPreferences(MainActivity.USERDATA, Context.MODE_PRIVATE);
        String textFont = sharedP.getString(MainActivity.FONT,"forte");
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/" + textFont + ".ttf");
        txt_title.setTypeface(typeface);

        Myrecycler.addOnItemTouchListener(new RecyclerItemClick(getBaseContext(), new RecyclerItemClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

               /*///hazf kardane dostan
                boolean res = dbManager.deleteFr(UserName[position]);
                if (res){
                    Toast.makeText(Friend_List.this, "از لیست دوستان حذف شد", Toast.LENGTH_SHORT).show();
                    items.remove(position);
                    Myadapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(Friend_List.this, "حذف نشد", Toast.LENGTH_SHORT).show();
                }*/

            }
        }));
    }

private void Creatlist(){

    Items_list items_list =new Items_list("","","","","","","","","","");
    final int tedad = dbManager.Count();

    UserName = new String[tedad];
    LastSeen = new String[tedad];
    Name = new String[tedad];
    Image_fr = new String[tedad];

    for (int i=0; i< tedad; i++){

        items_list = dbManager.DisplayName(i);
        Name[i] = items_list.getName_list();
        Image_fr[i] = items_list.getImage_list();
        UserName[i]= items_list.getUsername_list();

        checkLastOnline( items_list.getUsername_list(),i);
        items.add(new Items_list(Image_fr[i],Name[i],UserName[i],LastSeen[i],"","","","","",""));
    }
    Myadapter.notifyDataSetChanged();

    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            items.clear();
            for (int i=0; i< tedad; i++){

                // drugArrayList.add(Name[i]);
                // adapter.notifyDataSetChanged();

                items.add(new Items_list(Image_fr[i],Name[i],UserName[i],LastSeen[i],"","","","","",""));
            }

            Myadapter.notifyDataSetChanged();
        }
    },5000);


    dbManager.close();

}

    private void checkLastOnline(String username, final int position){


        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.CHECKLASTONLINE)
                .addBodyParameter("ItemsUsername",username)
                .setTag("checkLastOnline")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("خطا1", String.valueOf(response));
                         result = response;

                        LastSeen[position] = response;
                      //  Toast.makeText(Friend_List.this, LastSeen[position], Toast.LENGTH_SHORT).show();
                     //  Toast.makeText(Friend_List.this, result, Toast.LENGTH_SHORT).show();
                       /* if (response.contains("done")){

                            String aa = response;
                            Toast.makeText(Friend_List.this, "فایل با موفقیت آپلود شد", Toast.LENGTH_SHORT).show();
                            Toast.makeText(Friend_List.this, aa, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Friend_List.this, "نا موفق", Toast.LENGTH_SHORT).show();

                        }*/

                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(Friend_List.this, "خطا در اتصال", Toast.LENGTH_SHORT).show();
                        Log.d("خطا", String.valueOf(anError));
                    }
                });
    }

    public void deleteFr(){


    }
}
