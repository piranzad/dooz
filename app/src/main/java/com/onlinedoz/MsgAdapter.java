package com.onlinedoz;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{


    List<Items_list> items;
    Context context;

    public static String BTN = "btn";
    public static String USERNAME_mabda = "username_mabda";
    public static String name_mabda = "name_mabda";
    public static String IMAGE_Mabda = "image_mabda";


    public static int ADMIN = 0;
    String meqdar_block;
   // public static ArrayList<Integer> IDS = new ArrayList<>();
   // public static ArrayList<String> username_dialog = new ArrayList<>();

    public MsgAdapter(List<Items_list> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        AndroidNetworking.initialize(context);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_msg_recy,parent,false);

       // Toast.makeText(context, BTN, Toast.LENGTH_SHORT).show();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Items_list item = this.items.get(position);
       // confirmed = item.getConfirmed_list();

        holder.txt_image.setText(item.getImage_list());
        holder.txt_name.setText(item.getName_list());
        holder.txtmsg.setText(item.getMsg_list());
        holder.txt_time.setText(item.getTime_list());
        holder.txt_date.setText(item.getDate_list());
        holder.txt_id.setText(item.getId_list());
        holder.confirm_meqdar.setText(item.getConfirmed_list());
        holder.txt_usern.setText(item.getUsername_list());
        holder.txt_block.setText(item.getBlock_list());

        holder.txt_confirm.setVisibility(View.INVISIBLE);
        holder.txt_delete.setVisibility(View.INVISIBLE);
        Glide
                .with(context)
                .load(User_Items.HOST_NAME+ holder.txt_image.getText().toString())
                .into(holder.img_msg);


        if (BTN.equals("nav_comments")){


            if ( ADMIN == 0){
                holder.txt_delete.setVisibility(View.INVISIBLE);
                holder.txt_confirm.setVisibility(View.INVISIBLE);
            }else if ( ADMIN == 1){
                holder.txt_delete.setVisibility(View.VISIBLE);
            }

            if (holder.confirm_meqdar.getText().toString().equals("0")){
                holder.txt_confirm.setVisibility(View.VISIBLE);
            }  else if (holder.confirm_meqdar.getText().toString().equals("1") ){
                holder.txt_delete.setVisibility(View.INVISIBLE);
            }

        }else if (BTN.equals("btn_msg")){
            holder.txt_delete.setVisibility(View.VISIBLE);
        }




       holder.txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Delete_msg(holder.txt_id.getText().toString(),BTN,position);
            }
        });

        holder.txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Confirm_msg(position,holder.txt_id.getText().toString());
            }
        });

      holder.lay_msg.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              showUserInfo(holder.txt_name.getText().toString(),holder.txt_usern.getText().toString(),holder.txt_image.getText().toString(),holder.txt_block.getText().toString());
          }
      });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img_msg;
        TextView txt_name,txt_time,txt_date,txt_msg,txtmsg,txt_delete,txt_confirm,txt_id,confirm_meqdar,txt_usern,txt_image,txt_block;
        Button btn_delete;
        LinearLayout lay_msg;

        public ViewHolder(View itemView) {
            super(itemView);

            img_msg =  itemView.findViewById(R.id.crl_img_msg);
            txt_name = itemView.findViewById(R.id.txt_Name);
            txt_time =itemView.findViewById(R.id.txt_time);
            txt_date = itemView.findViewById(R.id.txt_date);
            txtmsg =  itemView.findViewById(R.id.txt_msg);
            txt_delete =  itemView.findViewById(R.id.txt_delete);
            txt_confirm =  itemView.findViewById(R.id.txt_confirm);
            txt_id =  itemView.findViewById(R.id.txt_id);
            confirm_meqdar =  itemView.findViewById(R.id.txt_confirm_m);
            txt_usern = itemView.findViewById(R.id.txt_usern);
            txt_image =  itemView.findViewById(R.id.txt_image);
            txt_block = itemView.findViewById(R.id.txt_block);
            lay_msg =  itemView.findViewById(R.id.lay_msg);
        }
    }

    private void Delete_msg (final String id, String tashkhis, final int position){

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.DELETE_MSG)
                .addBodyParameter("ItemsID", id)
                .addBodyParameter("Tashkhis", tashkhis)
                .addBodyParameter("ItemsUsername",USERNAME_mabda)
                .setPriority(Priority.HIGH)
                .setTag("Delete_msg")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("yes")){

                            Toast.makeText(context, "پیغام با موفقیت حذف شد", Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "id"+id, Toast.LENGTH_SHORT).show();
                            // estefade az method jadidi ke mitvan az method haye class commnet be ravash zir estefade kard ba parantez
                            ((Comments) context).items.remove(position);
                            ((Comments) context).Myadapter.notifyDataSetChanged();

                        }else if (response.contains("no")){

                            Toast.makeText(context, "پیغام حذف نشد", Toast.LENGTH_SHORT).show();


                        }else {
                            Toast.makeText(context, "پیغام حذف نشد", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                       // Toast.makeText(context,  "خطا در اتصال به اینترنت", Toast.LENGTH_SHORT).show();
                        Log.d("خطا", String.valueOf(anError));
                    }
                });

    }

    private void Confirm_msg(int position,final String id) {

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.CONFIRM_MSG)
                .addBodyParameter("ItemsID", id)
                .setTag("Confirm_msg")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {



                        if ( response.equals("OK")){

                            Toast.makeText(context, "کامنت تایید شد", Toast.LENGTH_SHORT).show();
                        }else {

                            Toast.makeText(context, "خطا", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                       // Toast.makeText(context, "خطا در اتصال", Toast.LENGTH_SHORT).show();
                        Log.d("خطا تایید پیام", String.valueOf(anError));
                    }
                });
    }
    private  void showUserInfo(final String name, final String username, final String image, String block){


        final Dialog AddPlayerNames = new Dialog(context);

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
        txt_username.setText(username);

        txt_name.setVisibility(View.VISIBLE);
        txt_username.setVisibility(View.VISIBLE);
        img.setVisibility(View.VISIBLE);
        Glide
                .with(context)
                .load(User_Items.HOST_NAME+image)
                .into(img);

        btn_game.setVisibility(View.GONE);

// taghir vaziya block va unblck agar 1 bod block sefr bod block
        if (BTN.equals("nav_comments")){
            btn_block.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
        }
        if (block.equals("0")){
            btn_block.setText(R.string.block);
            meqdar_block= "1";
        }else {
            btn_block.setText(R.string.unblock);
            meqdar_block= "0";
        }

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                boolean b = new DatabaseManager(context).check(username);
                if (b){
                    Toast.makeText(context, "این فرد قبلا به لیست دوستان اضافه شده است", Toast.LENGTH_SHORT).show();
                }else {

                    new DatabaseManager(context).insertFr(name,username,image);

                    Toast.makeText(context, "به لیست دوستان اضافه شد", Toast.LENGTH_SHORT).show();

                }     }
        });


        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                send_msg_dialog(username);

            }
        });
        btn_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                set_block(username);


            }
        });
    }

    public void send_msg_dialog(final String username_maqsad){

        final Dialog sendmsgdialog = new Dialog(context);

        sendmsgdialog.setCancelable(true);
        sendmsgdialog.getWindow();
        sendmsgdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sendmsgdialog.setContentView(R.layout.send_message);
        //AddPlayerNames.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
        sendmsgdialog.show();

        final EditText edt_msg = sendmsgdialog.findViewById(R.id.edt_message);
        Button btn_send_msg = (Button) sendmsgdialog.findViewById(R.id.btn_send_message);

          // dokme zadan messgage bara esral ayam dar dilog sevom
        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = edt_msg.getText().toString();
                send_msg(username_maqsad,msg);

            }
        });
        // bara dolme back badan bezar dismiss

    }

    private void send_msg (String username_maqsad,String msg){

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.SEND_MSG)
                .addBodyParameter("ItemsName",name_mabda)
                .addBodyParameter("ItemsUsername_mabda",USERNAME_mabda)
                .addBodyParameter("ItemsUsername_maqsad",username_maqsad)
                .addBodyParameter("ItemsMsg",msg)
                .addBodyParameter("ItemsImage",IMAGE_Mabda)
                .setPriority(Priority.HIGH)
                .setTag("send_msg")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("success")){

                            Toast.makeText(context,  "پیام شما ارسال شد", Toast.LENGTH_SHORT).show();
                        }else if (response.contains("fail")){

                            Toast.makeText(context,  "پیام ارسال نشد", Toast.LENGTH_SHORT).show();

                        }else if (response.contains("block")){

                    Toast.makeText(context,  "شما بلاک هستید و نمیتوانید ارسال کنید", Toast.LENGTH_SHORT).show();

                        }else {

                            Toast.makeText(context,  "خطا در ارسال پیام", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(context,  "خطا در اتصال به اینترنت", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void set_block(final String username) {

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.SET_BLOCK)
                .addBodyParameter("tablename", USERNAME_mabda)
                .addBodyParameter("Username", username)
                .addBodyParameter("meqdar", meqdar_block)
                .setTag("Confirm_msg")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {



                        if ( response.equals("OK")){

                            Toast.makeText(context, "انجام شد", Toast.LENGTH_SHORT).show();
                        }else {

                            Toast.makeText(context, "خطا", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        // Toast.makeText(context, "خطا در اتصال", Toast.LENGTH_SHORT).show();
                        Log.d("خطا بلاک کاربر", String.valueOf(anError));
                    }
                });
    }
}