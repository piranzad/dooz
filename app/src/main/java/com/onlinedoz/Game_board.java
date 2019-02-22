package com.onlinedoz;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Game_board extends AppCompatActivity {

    public static final int PLAYER1 = 1, PLAYER2 = 2,KHALI = 3,NOWIN = 0;
    int barande = NOWIN;
    int[] vaziat = {KHALI, KHALI, KHALI,
            KHALI, KHALI, KHALI,
            KHALI, KHALI, KHALI};

    int[][] BarandePos ={{0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}};

    TextView txtPlayer2,txtPlayer1, txt_EmtiazeShoma, txt_EmtiazeHarif;
    String username,harifName,MyName,harifImage,MyImage;
    int EmtiazeShoma = 0, EmtiazeHarif = 0,nobat,harekate_harif,SHOMA,HARIF,time = 30;

    private MediaPlayer click_snd,win_snd,fail_snd;
    ImageView imgsefrom,imgyekom,imgDowom,imgsevom,imgcharom,imgpanjom,imgsheshom,imghaftom,imghashtom;
    List<ImageView> Tasavir = new ArrayList<ImageView>();
    AnimatorSet animatorSet;
    boolean get_harekat,GameOver=false,Game_time_key;

    CircularProgressBar ProgressBar_1,ProgressBar_2;
    CircleImageView crl_img_player1,crl_img_player2;
     Timer game_timer;
     Dialog AddPlayerNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameboard);

        Clear_Cache();


        imgsefrom = (ImageView)findViewById(R.id.sefrom);
        imgyekom = (ImageView)findViewById(R.id.yekom);
        imgDowom = (ImageView)findViewById(R.id.dowom);
        imgsevom = (ImageView)findViewById(R.id.sowom);
        imgcharom = (ImageView)findViewById(R.id.charom);
        imgpanjom = (ImageView)findViewById(R.id.panjom);
        imgsheshom = (ImageView)findViewById(R.id.sheshom);
        imghaftom = (ImageView)findViewById(R.id.haftom);
        imghashtom = (ImageView)findViewById(R.id.hashtom);

        crl_img_player1 = (CircleImageView) findViewById(R.id.crl_img_player1);
        crl_img_player2 = (CircleImageView) findViewById(R.id.crl_img_player2);

        Tasavir.add(imgsefrom);
        Tasavir.add(imgyekom);
        Tasavir.add(imgDowom);
        Tasavir.add(imgsevom);
        Tasavir.add(imgcharom);
        Tasavir.add(imgpanjom);
        Tasavir.add(imgsheshom);
        Tasavir.add(imghaftom);
        Tasavir.add(imghashtom);

        txtPlayer2 =  findViewById(R.id.txt_player2);
        txtPlayer1 =  findViewById(R.id.txt_player1);
        txt_EmtiazeShoma =  findViewById(R.id.txt_EmtiazShoma);
        txt_EmtiazeHarif =  findViewById(R.id.txt_EmtiazHarif);

        AndroidNetworking.initialize(Game_board.this);

        ProgressBar_1 = findViewById(R.id.ProgressBar_1);
        ProgressBar_2 = findViewById(R.id.ProgressBar_2);
        ProgressBar_1.setColor(ContextCompat.getColor(this, R.color.progressBarColor));
        ProgressBar_2.setColor(ContextCompat.getColor(this, R.color.progressBarColor));
        ProgressBar_1.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundProgressBarColor));
        ProgressBar_2.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundProgressBarColor));
        //ProgressBar.setProgressBarWidth(25);
        //ProgressBar.setBackgroundProgressBarWidth(25);
        ProgressBar_1.setProgressMax(30);
        ProgressBar_2.setProgressMax(30);
        // ProgressBar.setProgress(15);


       // getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);


        username = getIntent().getStringExtra("username");
        String nobat_server = getIntent().getStringExtra("nobat");
         MyName = getIntent().getStringExtra("playerName");
         harifName = getIntent().getStringExtra("harifName");
        harifImage = getIntent().getStringExtra("harifImage");
        MyImage = getIntent().getStringExtra("MyImage");
        get_harekat = getIntent().getBooleanExtra("get_harekat",false);
        boolean set_enable=getIntent().getBooleanExtra("setEnable",false);
         setEnable(set_enable);

        Toast.makeText(this, nobat_server, Toast.LENGTH_SHORT).show();

        nobat = PLAYER2;
        if (set_enable){
            txtPlayer2.setText(MyName);
            //red
            //txtPlayer2.setTextColor(Color.parseColor("#ece633"));

            txtPlayer1.setText(harifName);
            //blue
            //txtPlayer1.setTextColor(Color.parseColor("#72eff1"));
            SHOMA =PLAYER2;
            HARIF =PLAYER1;


            int animationDuration = 30000; // 3000ms = 3s
            ProgressBar_2.setProgress(0);
            ProgressBar_2.setProgressWithAnimation(30, animationDuration); // Default duration = 1500ms


        }else {
            txtPlayer2.setText(MyName);
            //txtPlayer2.setTextColor(Color.parseColor("#1648ec"));
            txtPlayer1.setText(harifName);
            //txtPlayer1.setTextColor(Color.parseColor("#c40c0c"));
            SHOMA =PLAYER1;
            HARIF =PLAYER2;

            int animationDuration = 30000; // 30000ms = 30s
            ProgressBar_2.setProgress(0);
            ProgressBar_1.setProgressWithAnimation(30, animationDuration); // Default duration = 1500ms
            /*if (nobat_server.equals("player1")){
                nobat = PLAYER1;
            }else if (nobat_server.equals("player2")){
                nobat = PLAYER2;
            }*/
        }
        Glide
                .with(this)
                .load(User_Items.HOST_NAME+harifImage)
                .into(crl_img_player1);
        Glide
                .with(this)
                .load(User_Items.HOST_NAME+MyImage)
                .into(crl_img_player2);


        Timer ();
        Game_Time ();
       // Progress_Anim ();

        click_snd = MediaPlayer.create(this,R.raw.button);
        win_snd = MediaPlayer.create(this,R.raw.win_sound);
        fail_snd = MediaPlayer.create(this,R.raw.fail_sound);

    }
    public void bazi (View view){
        GameOver=false;
        int tag = Integer.parseInt((String) view.getTag());
        if(barande != NOWIN || vaziat[tag] != KHALI){
            return;
        }
        ImageView img = (ImageView) view;
        click_snd.start();

        vaziat[tag] = nobat;
        Result();

        if (nobat==PLAYER1){
            img.setImageResource(R.drawable.multiply);
            Set_Harekat(String.valueOf(tag),"player1");
            nobat=PLAYER2;
            setEnable(false);

        }else if (nobat==PLAYER2){
            img.setImageResource(R.drawable.circle);
            Set_Harekat(String.valueOf(tag),"player2");
           nobat=PLAYER1;
            setEnable(false);
        }
        get_harekat=true;
        Timer ();
        Progress_Anim ();
        game_timer.cancel();
        Toast.makeText(this, nobat+"", Toast.LENGTH_SHORT).show();

    }


    public void Result() {
        barande = checkWinner();
        if(barande != NOWIN || full()) {
            String ResultGame = "";

            if(barande == NOWIN){
                ResultGame = "No Win";
                game_timer.cancel();
            } else if(barande == PLAYER2 && SHOMA==PLAYER2){
                win_snd.start();
                //ResultGame = "برنده شد"+MyName;
                ResultGame = "You Win";
                EmtiazeShoma++;
               txt_EmtiazeShoma.setText(Integer.toString(EmtiazeShoma));
                game_timer.cancel();
            } else if(barande == PLAYER2 && SHOMA==PLAYER1){
                EmtiazeHarif++;
                txt_EmtiazeHarif.setText(Integer.toString(EmtiazeHarif));
                //ResultGame = "برنده شد"+harifName;
                ResultGame = "You Loss";
                fail_snd.start();
                game_timer.cancel();

            } else if(barande == PLAYER1 && SHOMA==PLAYER1) {
                EmtiazeShoma++;
                txt_EmtiazeShoma.setText(Integer.toString(EmtiazeShoma));
                //ResultGame = "برنده شد"+MyName;
                ResultGame = "You Win";
                win_snd.start();
                game_timer.cancel();

            }else if(barande == PLAYER1 && SHOMA==PLAYER2) {
                EmtiazeHarif++;
                txt_EmtiazeHarif.setText(Integer.toString(EmtiazeHarif));
                //ResultGame = "برنده شد"+harifName;
                ResultGame = "You Loss";
                fail_snd.start();
                game_timer.cancel();
            }
            ProgressBar_1.setProgress(0);
            ProgressBar_2.setProgress(0);
            ProgressBar_1.clearAnimation();
            ProgressBar_2.clearAnimation();
            GameOver = true;

            show_game_result(ResultGame);


        }
    }

    public int checkWinner(){
        for(int[] pos : BarandePos){
            if(     vaziat[pos[0]] == vaziat[pos[1]] &&
                    vaziat[pos[1]] == vaziat[pos[2]] &&
                    vaziat[pos[0]] != KHALI){
                animation(new int[] { pos[0], pos[1], pos[2] });
                return vaziat[pos[0]];
            }
        }
        return NOWIN;
    }

    public boolean full(){
        for(int i = 0; i < vaziat.length ; i++){
            if(vaziat[i] == KHALI){
                return false;
            }
        }
        return true;
    }

    public void reset(View v){

        AddPlayerNames.dismiss();
        GameOver = false;
        barande = NOWIN;
        for(int i = 0; i < vaziat.length ; i++){
            vaziat[i] = KHALI;
        }

        for (int i = 0; i< Tasavir.size(); i++){
            Tasavir.get(i).setImageResource(0);
        }


    }

    public void Anjame_harekat(int adad){

        if (GameOver){
            reset(txtPlayer1);
            GameOver = false;
        }
        if ( vaziat[adad]==KHALI && nobat ==PLAYER1) {
            if (adad == 0) {
                imgsefrom.setImageResource(R.drawable.multiply);
            } else if (adad == 1) {
                imgyekom.setImageResource(R.drawable.multiply);
            } else if (adad == 2) {
                imgDowom.setImageResource(R.drawable.multiply);
            } else if (adad == 3) {
                imgsevom.setImageResource(R.drawable.multiply);
            } else if (adad == 4) {
                imgcharom.setImageResource(R.drawable.multiply);
            } else if (adad == 5) {
                imgpanjom.setImageResource(R.drawable.multiply);
            } else if (adad == 6) {
                imgsheshom.setImageResource(R.drawable.multiply);
            } else if (adad == 7) {
                imghaftom.setImageResource(R.drawable.multiply);
            } else if (adad == 8) {
                imghashtom.setImageResource(R.drawable.multiply);
            }
            click_snd.start();
            get_harekat=false;
           // Timer ();
            vaziat[adad] = nobat;

                nobat=PLAYER2;
            setEnable(true);
            Progress_Anim ();
            game_timer.cancel();
            Result();
        }else if (vaziat[adad]==KHALI && nobat ==PLAYER2){

            if (adad == 0) {
                imgsefrom.setImageResource(R.drawable.circle);
            } else if (adad == 1) {
                imgyekom.setImageResource(R.drawable.circle);
            } else if (adad == 2) {
                imgDowom.setImageResource(R.drawable.circle);
            } else if (adad == 3) {
                imgsevom.setImageResource(R.drawable.circle);
            } else if (adad == 4) {
                imgcharom.setImageResource(R.drawable.circle);
            } else if (adad == 5) {
                imgpanjom.setImageResource(R.drawable.circle);
            } else if (adad == 6) {
                imgsheshom.setImageResource(R.drawable.circle);
            } else if (adad == 7) {
                imghaftom.setImageResource(R.drawable.circle);
            } else if (adad == 8) {
                imghashtom.setImageResource(R.drawable.circle);
            }

            vaziat[adad] = nobat;
            click_snd.start();
            get_harekat=false;
            setEnable(true);
                nobat=PLAYER1;
            Progress_Anim ();
            game_timer.cancel();
            Result();
        }

        Toast.makeText(this, nobat+"", Toast.LENGTH_SHORT).show();

    }

    public void animation(int[] array) {
        Tasavir.add(imgsefrom);
        Tasavir.add(imgyekom);
        Tasavir.add(imgDowom);
        Tasavir.add(imgsevom);
        Tasavir.add(imgcharom);
        Tasavir.add(imgpanjom);
        Tasavir.add(imgsheshom);
        Tasavir.add(imghaftom);
        Tasavir.add(imghashtom);

        List<Animator> animation_list = new ArrayList<Animator>();


        for (int i = 0; i < array.length; i++) {
            ObjectAnimator Myanim = ObjectAnimator.ofFloat(Tasavir.get(array[i]), "scaleX", 0.5f);
            Myanim.setRepeatCount(7);
            Myanim.setRepeatMode(ObjectAnimator.REVERSE);
            animation_list.add(Myanim);

            Myanim = ObjectAnimator.ofFloat(Tasavir.get(array[i]), "scaleY", 0.5f);
            Myanim.setRepeatCount(7);
            Myanim.setRepeatMode(ObjectAnimator.REVERSE);
            animation_list.add(Myanim);
        }

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(animation_list);
        animatorSet.setDuration(200);
        animatorSet.start();
    }



    // noe harkat ra moshkhas mikonad
    private void Set_Harekat(String meqdar,String nobat) {

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.GAME_R_REPLY)
                .addBodyParameter("ItemsUsername",username)
                .addBodyParameter("ItemsMeqdar",meqdar)
                .addBodyParameter("Nobate",nobat)
                .addBodyParameter("Tashkhis","Set_Harekat")
                .setTag("Set_Harekat")
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
// nobaat harkat ra moshaks mikonad va miravad be set harkat
    private void Get_Harekat(String nobat){

        AndroidNetworking.post(User_Items.HOST_NAME+User_Items.GET_HAREKAT)
                .addBodyParameter("ItemsUsername",username)
                .addBodyParameter("Nobate",nobat)
                .setTag("Get_Harekat")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("جواب گرفتن حرکت", String.valueOf(response));

                        if (response.equals("wait") || response.equals("error")){
                            Timer ();

                        }else {

                            if (response.equals("10")){
                                Timer ();
                            }else {
                                harekate_harif = Integer.parseInt(response);
                                // Toast.makeText(Game_board.this, harekate_harif, Toast.LENGTH_SHORT).show();
                                Anjame_harekat(harekate_harif);
                            }
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("خطای گرفتن حرکت", String.valueOf(anError));
                    }
                });
    }

    private void Timer () {

       if (get_harekat==true){

           new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (nobat ==PLAYER1){
                        Get_Harekat("player1");
                    }else if (nobat==PLAYER2){
                        Get_Harekat("player2");
                    }


                }
            },3000);

        }else if (get_harekat==false){

        }
    }

    // tabeyi ke khone ha dgro gheyre fall mikone  ta ion yeki betone bazi kone
    private void setEnable(boolean enable){

        for (int i = 0; i< Tasavir.size(); i++){
            Tasavir.get(i).setEnabled(enable);
        }
        TextView txt_nobat= findViewById(R.id.txt_nobat);
        if (enable){

            txt_nobat.setText(R.string.nobate_shoma);
            //getSupportActionBar().setTitle("نوبت خــودتــه");

        }else {
            txt_nobat.setText(R.string.nobate_harif);
            //getSupportActionBar().setTitle("نوبت حــریــفه");

        }
    }

    private void Progress_Anim () {

        int animationDuration = 30000; // 3000ms = 3s

        ProgressBar_1.setProgress(0);
        ProgressBar_2.setProgress(0);
        ProgressBar_1.clearAnimation();
        ProgressBar_2.clearAnimation();
        if (nobat ==PLAYER2 && SHOMA == PLAYER1){

        ProgressBar_1.setProgressWithAnimation(30, animationDuration); // Default duration = 1500ms

        }else if (nobat==PLAYER2 && SHOMA == PLAYER2){

            ProgressBar_2.setProgressWithAnimation(30, animationDuration); // Default duration = 1500ms

    }else if (nobat==PLAYER1 && SHOMA == PLAYER1){

        ProgressBar_2.setProgressWithAnimation(30, animationDuration); // Default duration = 1500ms
        }else if (nobat==PLAYER1 && SHOMA == PLAYER2){

            ProgressBar_1.setProgressWithAnimation(30, animationDuration); // Default duration = 1500ms
        }


        Game_Time ();


    }

    // modat zamani ke mitone fek kone ta entkehab kone
    private void Game_Time () {

         game_timer = new Timer();
        time=30;
        game_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                      //  getSupportActionBar().setTitle(""+time);
                        if (!GameOver){
                            if (time==0){
                                game_timer.cancel();
                        Toast.makeText(Game_board.this, "زمان به پایان رسید", Toast.LENGTH_SHORT).show();
                                String res = null;
                        if (nobat==SHOMA){
                            res  ="وقتت تموم شد و تو باختی";
                            fail_snd.start();
                        }else if (nobat == HARIF){
                             res ="وقت حریفت تموم شد و تو بردی";
                            win_snd.start();
                        }

                        show_game_result(res);
                                GameOver = true;
                            }else {
                                time--;
                                Log.e("زمان : ", String.valueOf(time));
                                // Toast.makeText(CircularProgress.this, time+"", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        }, 0, 1000);
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

    // natije bazi ro neshon mide ke bordi ya bakhti
    private  void show_game_result(String result) {


        AddPlayerNames = new Dialog(this);

        AddPlayerNames.setCancelable(true);
        AddPlayerNames.getWindow();
        AddPlayerNames.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AddPlayerNames.setContentView(R.layout.result_game);
        //AddPlayerNames.getWindow().getAttributes().windowAnimations = R.style.dialog_anim;
        AddPlayerNames.show();


        TextView txt_player1_res = (TextView) AddPlayerNames.findViewById(R.id.txt_player1_res);
        TextView txt_player2_res = (TextView) AddPlayerNames.findViewById(R.id.txt_player2_res);
        TextView txt_EmtiazShoma_res = (TextView) AddPlayerNames.findViewById(R.id.txt_EmtiazShoma_res);
        TextView txt_EmtiazHarif_res = (TextView) AddPlayerNames.findViewById(R.id.txt_EmtiazHarif_res);
        TextView txt_result = (TextView) AddPlayerNames.findViewById(R.id.txt_result);
        Button btn_playagain = (Button) AddPlayerNames.findViewById(R.id.btn_playagain);
        Button btn_mainmenu = (Button) AddPlayerNames.findViewById(R.id.btn_mainmenu);
        ImageView img = (ImageView) AddPlayerNames.findViewById(R.id.img_gameover);

        txt_player1_res.setText(harifName);
        txt_player2_res.setText(MyName);
        txt_result.setText(result);
        txt_EmtiazShoma_res.setText(EmtiazeShoma + "");
        txt_EmtiazHarif_res.setText(EmtiazeHarif + "");

        if (result.equals("تو بردی")){

            img.setImageResource(R.drawable.winimage);
        }else if (result.equals("تو باختی")){

            img.setImageResource(R.drawable.lossimage);
        }else {
            img.setImageResource(0);
        }





    }
    public void intent_main(View view){

        startActivity(new Intent(Game_board.this,MainActivity.class));
        finish();
    }
}
