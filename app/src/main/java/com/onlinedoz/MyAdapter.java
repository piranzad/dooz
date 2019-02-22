package com.onlinedoz;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{


    String Fr_activity ="";

    List<Items_list> items;
    Context context;

    SharedPreferences sharedP;
    public static final String USERDATA = "UserData";
    public static final String USERNAME = "username";
    String Myusername;

    public MyAdapter(List<Items_list> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_fr_recy,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Items_list item = this.items.get(position);
        holder.txt_name.setText(item.getName_list());
        holder.txt_status.setText(item.getLastseen_list());
        holder.txt_username.setText(item.getUsername_list());

        Glide
                .with(context)
                .load(User_Items.HOST_NAME+item.getImage_list())
                .into(holder.img_list);

        // neshan dadan online va gheyre online bodan ke dore axe asat


        if (holder.txt_status.getText().equals("online")){

            holder.img_list.setBorderColor(Color.parseColor("#15ca15"));
        }

        // check mikonad ke khodeman dar list online nabashim
        checkAdmin();
      if (holder.txt_username.getText().equals(Myusername)){

           items.remove(position);
        }

       if(Fr_activity.equals("Friend_List")) {

        }else if(Fr_activity.equals("List_online")) {

           holder.btn_delete.setVisibility(View.INVISIBLE);
          //holder.img_list.setBorderColor(Color.parseColor("#15ca15"));
       }


        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              // Toast.makeText(context, holder.txt_username.getText().toString(), Toast.LENGTH_SHORT).show();
                //hazf kardane dostan
               // boolean res = dbManager.deleteFr(holder.txt_username.getText().toString());
                boolean res = new DatabaseManager(context).deleteFr(holder.txt_username.getText().toString());
                if (res){
                    Toast.makeText(context, "از لیست دوستان حذف شد", Toast.LENGTH_SHORT).show();
                    ((Friend_List) context).items.remove(position);
                    ((Friend_List) context).Myadapter.notifyDataSetChanged();
                   // new Friend_List(context).items.remove(position);
                 //  new Friend_List(context).Myadapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(context, "حذف نشد", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img_list;
        TextView txt_name,txt_status,txt_username;
        Button btn_delete;

        public ViewHolder(View itemView) {
            super(itemView);

            img_list =  itemView.findViewById(R.id.img_list);
            txt_name = itemView.findViewById(R.id.textName);
            txt_status = itemView.findViewById(R.id.txt_status);
            txt_username = itemView.findViewById(R.id.txt_username);
            btn_delete =  itemView.findViewById(R.id.btn_delete);
        }
    }

    public void checkAdmin(){

        sharedP = context.getSharedPreferences(USERDATA, Context.MODE_PRIVATE);

        Myusername = sharedP.getString(USERNAME,"");

    }
}
