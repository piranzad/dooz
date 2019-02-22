package com.onlinedoz;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class YeNafare_Hard extends AppCompatActivity {

    public static final int ROBOT = 1;
    public static final int PLAYER = 2;
    public static final int KHALI = 3;
    private static final int NOWIN = 0;
    int barande = NOWIN;
    int[] vaziat = {KHALI, KHALI, KHALI,
            KHALI, KHALI, KHALI,
            KHALI, KHALI, KHALI};

    int[][] BarandePos ={{0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}};
    RelativeLayout ResultLayout;
    TextView txtPlayer2,txtPlayer1, txt_EmtiazeShoma, txt_EmtiazeHarif;
    String PlayerName = "شما",MyName,MyImage;
    int EmtiazePlayer = 0;
    int EmtiazeRobot = 0;
    private MediaPlayer click_snd,win_snd,fail_snd;
    ImageView imgsefrom,imgyekom,imgDowom,imgsevom,imgcharom,imgpanjom,imgsheshom,imghaftom,imghashtom;
    List<ImageView> Tasavir = new ArrayList<ImageView>();
    AnimatorSet animatorSet;

    CircularProgressBar ProgressBar_1,ProgressBar_2;
    CircleImageView crl_img_player1,crl_img_player2;
    Dialog AddPlayerNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ye_nafare);


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

        txtPlayer2 = (TextView) findViewById(R.id.txt_player2);
        txtPlayer1 = (TextView) findViewById(R.id.txt_player1);
        txt_EmtiazeShoma = (TextView) findViewById(R.id.txt_EmtiazShoma);
        txt_EmtiazeHarif = (TextView) findViewById(R.id.txt_EmtiazHarif);

        click_snd = MediaPlayer.create(this,R.raw.button);
        win_snd = MediaPlayer.create(this,R.raw.win_sound);
        fail_snd = MediaPlayer.create(this,R.raw.fail_sound);



       MyName = getIntent().getStringExtra("playerName");
        MyImage = getIntent().getStringExtra("MyImage");



            txtPlayer2.setText(MyName);
            txtPlayer1.setText("Robot");
        Glide
                .with(this)
                .load(User_Items.HOST_NAME+MyImage)
                .into(crl_img_player2);
            Random();
    }
    public void bazi (View view){
        int tag = Integer.parseInt((String) view.getTag());
        if(barande != NOWIN || vaziat[tag] != KHALI){
            return;
        }
        ImageView img = (ImageView) view;
           click_snd.start();
        img.setImageResource(R.drawable.circle);
        vaziat[tag] = PLAYER;
        Hamle ();
        Result();

            }


    public void Result() {
        barande = checkWinner();
        if(barande != NOWIN || full()) {
            String ResultGame = "";

            if(barande == NOWIN){
                ResultGame = "NO WIN";
            } else if(barande == PLAYER){
                ResultGame = "You Win";
                win_snd.start();
               EmtiazePlayer++;
                txt_EmtiazeShoma.setText(Integer.toString(EmtiazePlayer));

            } else if(barande == ROBOT) {
                ResultGame = "You Loss";
                fail_snd.start();
                EmtiazeRobot++;
                txt_EmtiazeHarif.setText(Integer.toString(EmtiazeRobot));
            }

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
        barande = NOWIN;
        for(int i = 0; i < vaziat.length ; i++){
            vaziat[i] = KHALI;
        }

        for (int i = 0; i< Tasavir.size(); i++){
            Tasavir.get(i).setImageResource(0);
        }


        Random();
    }

    public void Moqabele (){
        if (vaziat[0]== PLAYER && vaziat[1]== PLAYER && vaziat[2]== KHALI){
            vaziat[2] = ROBOT;
            imgDowom.setImageResource(R.drawable.multiply);

        }else if (vaziat[1]== PLAYER && vaziat[2]== PLAYER && vaziat[0]== KHALI){
            vaziat[0] = ROBOT;
            imgsefrom.setImageResource(R.drawable.multiply);

        }else if (vaziat[0]== PLAYER && vaziat[2]== PLAYER && vaziat[1]== KHALI){
            vaziat[1] = ROBOT;
            imgyekom.setImageResource(R.drawable.multiply);

        }else if (vaziat[3]== PLAYER && vaziat[4]== PLAYER && vaziat[5]== KHALI){
            vaziat[5] = ROBOT;
            imgpanjom.setImageResource(R.drawable.multiply);

        }else if (vaziat[4]== PLAYER && vaziat[5]== PLAYER && vaziat[3]== KHALI){
            vaziat[3] = ROBOT;
            imgsevom.setImageResource(R.drawable.multiply);

        }else if (vaziat[3]== PLAYER && vaziat[5]== PLAYER && vaziat[4]== KHALI){
            vaziat[4] = ROBOT;
            imgcharom.setImageResource(R.drawable.multiply);

        }else if (vaziat[6]== PLAYER && vaziat[7]== PLAYER && vaziat[8]== KHALI){
            vaziat[8] = ROBOT;
            imghashtom.setImageResource(R.drawable.multiply);

        }else if (vaziat[7]== PLAYER && vaziat[8]== PLAYER && vaziat[6]== KHALI){
            vaziat[6] = ROBOT;
            imgsheshom.setImageResource(R.drawable.multiply);

        }else if (vaziat[6]== PLAYER && vaziat[8]== PLAYER && vaziat[7]== KHALI){
            vaziat[7] = ROBOT;
            imghaftom.setImageResource(R.drawable.multiply);

        }else if (vaziat[0]== PLAYER && vaziat[3]== PLAYER && vaziat[6]== KHALI){
            vaziat[6] = ROBOT;
            imgsheshom.setImageResource(R.drawable.multiply);

        }else if (vaziat[3]== PLAYER && vaziat[6]== PLAYER && vaziat[0]== KHALI){
            vaziat[0] = ROBOT;
            imgsefrom.setImageResource(R.drawable.multiply);

        }else if (vaziat[0]== PLAYER && vaziat[6]== PLAYER && vaziat[3]== KHALI){
            vaziat[3] = ROBOT;
            imgsevom.setImageResource(R.drawable.multiply);

        }else if (vaziat[1]== PLAYER && vaziat[4]== PLAYER && vaziat[7]== KHALI){
            vaziat[7] = ROBOT;
            imghaftom.setImageResource(R.drawable.multiply);

        }else if (vaziat[4]== PLAYER && vaziat[7]== PLAYER && vaziat[1]== KHALI){
            vaziat[1] = ROBOT;
            imgyekom.setImageResource(R.drawable.multiply);

        }else if (vaziat[1]== PLAYER && vaziat[7]== PLAYER && vaziat[4]== KHALI){
            vaziat[4] = ROBOT;
            imgcharom.setImageResource(R.drawable.multiply);

        }else if (vaziat[2]== PLAYER && vaziat[5]== PLAYER && vaziat[8]== KHALI){
            vaziat[8] = ROBOT;
            imghashtom.setImageResource(R.drawable.multiply);

        }else if (vaziat[5]== PLAYER && vaziat[8]== PLAYER && vaziat[2]== KHALI){
            vaziat[2] = ROBOT;
            imgDowom.setImageResource(R.drawable.multiply);

        }else if (vaziat[2]== PLAYER && vaziat[8]== PLAYER && vaziat[5]== KHALI){
            vaziat[5] = ROBOT;
            imgpanjom.setImageResource(R.drawable.multiply);

        }else if (vaziat[0]== PLAYER && vaziat[4]== PLAYER && vaziat[8]== KHALI){
            vaziat[8] = ROBOT;
            imghashtom.setImageResource(R.drawable.multiply);

        }else if (vaziat[4]== PLAYER && vaziat[8]== PLAYER && vaziat[0]== KHALI){
            vaziat[0] = ROBOT;
            imgsefrom.setImageResource(R.drawable.multiply);

        }else if (vaziat[0]== PLAYER && vaziat[8]== PLAYER && vaziat[4]== KHALI){
            vaziat[4] = ROBOT;
            imgcharom.setImageResource(R.drawable.multiply);

        }else if (vaziat[2]== PLAYER && vaziat[4]== PLAYER && vaziat[6]== KHALI){
            vaziat[6] = ROBOT;
            imgsheshom.setImageResource(R.drawable.multiply);

        }else if (vaziat[4]== PLAYER && vaziat[6]== PLAYER && vaziat[2]== KHALI){
            vaziat[2] = ROBOT;
            imgDowom.setImageResource(R.drawable.multiply);

        }else if (vaziat[2]== PLAYER && vaziat[6]== PLAYER && vaziat[4]== KHALI){
            vaziat[4] = ROBOT;
            imgcharom.setImageResource(R.drawable.multiply);

        }else {
            harakat(); }
    }
    public void Hamle (){
        if (vaziat[0]== ROBOT && vaziat[1]== ROBOT && vaziat[2]== KHALI){
            vaziat[2] = ROBOT;
            imgDowom.setImageResource(R.drawable.multiply);

        }else if (vaziat[1]== ROBOT && vaziat[2]== ROBOT && vaziat[0]== KHALI){
            vaziat[0] = ROBOT;
            imgsefrom.setImageResource(R.drawable.multiply);
        }else if (vaziat[0]== ROBOT && vaziat[2]== ROBOT && vaziat[1]== KHALI){
            vaziat[1] = ROBOT;
            imgyekom.setImageResource(R.drawable.multiply);

        }else if (vaziat[3]== ROBOT && vaziat[4]== ROBOT && vaziat[5]== KHALI){
            vaziat[5] = ROBOT;
            imgpanjom.setImageResource(R.drawable.multiply);

        }else if (vaziat[4]== ROBOT && vaziat[5]== ROBOT && vaziat[3]== KHALI){
            vaziat[3] = ROBOT;
            imgsevom.setImageResource(R.drawable.multiply);
        }else if (vaziat[3]== ROBOT && vaziat[5]== ROBOT && vaziat[4]== KHALI){
            vaziat[4] = ROBOT;
            imgcharom.setImageResource(R.drawable.multiply);
        }else if (vaziat[6]== ROBOT && vaziat[7]== ROBOT && vaziat[8]== KHALI){
            vaziat[8] = ROBOT;
            imghashtom.setImageResource(R.drawable.multiply);
        }else if (vaziat[7]== ROBOT && vaziat[8]== ROBOT && vaziat[6]== KHALI){
            vaziat[6] = ROBOT;
            imgsheshom.setImageResource(R.drawable.multiply);
        }else if (vaziat[6]== ROBOT && vaziat[8]== ROBOT && vaziat[7]== KHALI){
            vaziat[7] = ROBOT;
            imghaftom.setImageResource(R.drawable.multiply);
        }else if (vaziat[0]== ROBOT && vaziat[3]== ROBOT && vaziat[6]== KHALI){
            vaziat[6] = ROBOT;
            imgsheshom.setImageResource(R.drawable.multiply);
        }else if (vaziat[3]== ROBOT && vaziat[6]== ROBOT && vaziat[0]== KHALI){
            vaziat[0] = ROBOT;
            imgsefrom.setImageResource(R.drawable.multiply);
        }else if (vaziat[0]== ROBOT && vaziat[6]== ROBOT && vaziat[3]== KHALI){
            vaziat[3] = ROBOT;
            imgsevom.setImageResource(R.drawable.multiply);
        }else if (vaziat[1]== ROBOT && vaziat[4]== ROBOT && vaziat[7]== KHALI){
            vaziat[7] = ROBOT;
            imghaftom.setImageResource(R.drawable.multiply);
        }else if (vaziat[4]== ROBOT && vaziat[7]== ROBOT && vaziat[1]== KHALI){
            vaziat[1] = ROBOT;
            imgyekom.setImageResource(R.drawable.multiply);
        }else if (vaziat[1]== ROBOT && vaziat[7]== ROBOT && vaziat[4]== KHALI){
            vaziat[4] = ROBOT;
            imgcharom.setImageResource(R.drawable.multiply);
        }else if (vaziat[2]== ROBOT && vaziat[5]== ROBOT && vaziat[8]== KHALI){
            vaziat[8] = ROBOT;
            imghashtom.setImageResource(R.drawable.multiply);
        }else if (vaziat[5]== ROBOT && vaziat[8]== ROBOT && vaziat[2]== KHALI){
            vaziat[2] = ROBOT;
            imgDowom.setImageResource(R.drawable.multiply);
        }else if (vaziat[2]== ROBOT && vaziat[8]== ROBOT && vaziat[5]== KHALI){
            vaziat[5] = ROBOT;
            imgpanjom.setImageResource(R.drawable.multiply);
        }else if (vaziat[0]== ROBOT && vaziat[4]== ROBOT && vaziat[8]== KHALI){
            vaziat[8] = ROBOT;
            imghashtom.setImageResource(R.drawable.multiply);
        }else if (vaziat[4]== ROBOT && vaziat[8]== ROBOT && vaziat[0]== KHALI){
            vaziat[0] = ROBOT;
            imgsefrom.setImageResource(R.drawable.multiply);
        }else if (vaziat[0]== ROBOT && vaziat[8]== ROBOT && vaziat[4]== KHALI){
            vaziat[4] = ROBOT;
            imgcharom.setImageResource(R.drawable.multiply);
        }else if (vaziat[2]== ROBOT && vaziat[4]== ROBOT && vaziat[6]== KHALI){
            vaziat[6] = ROBOT;
            imgsheshom.setImageResource(R.drawable.multiply);
        }else if (vaziat[4]== ROBOT && vaziat[6]== ROBOT && vaziat[2]== KHALI){
            vaziat[2] = ROBOT;
            imgDowom.setImageResource(R.drawable.multiply);
        }else if (vaziat[2]== ROBOT && vaziat[6]== ROBOT && vaziat[4]== KHALI){
            vaziat[4] = ROBOT;
            imgcharom.setImageResource(R.drawable.multiply);
        }else {
         Moqabele();
        }

    }

    public void harakat() {
        if (vaziat[0]== ROBOT && vaziat[1]== KHALI){
            vaziat[1] = ROBOT;
            imgyekom.setImageResource(R.drawable.multiply);
        }else if (vaziat[0]== ROBOT && vaziat[3]== KHALI){
            vaziat[3] = ROBOT;
            imgsevom.setImageResource(R.drawable.multiply);
        }else if (vaziat[0]== ROBOT && vaziat[4]== KHALI){
            vaziat[4] = ROBOT;
            imgcharom.setImageResource(R.drawable.multiply);
        }else if (vaziat[1]== ROBOT && vaziat[0]== KHALI){
            vaziat[0] = ROBOT;
            imgsefrom.setImageResource(R.drawable.multiply);
        }else if (vaziat[1]== ROBOT && vaziat[2]== KHALI){
            vaziat[2] = ROBOT;
            imgDowom.setImageResource(R.drawable.multiply);
        }else if (vaziat[1]== ROBOT && vaziat[4]== KHALI){
            vaziat[4] = ROBOT;
            imgcharom.setImageResource(R.drawable.multiply);
        }else if (vaziat[2]== ROBOT && vaziat[1]== KHALI){
            vaziat[1] = ROBOT;
            imgyekom.setImageResource(R.drawable.multiply);
        }else if (vaziat[2]== ROBOT && vaziat[4]== KHALI){
            vaziat[4] = ROBOT;
            imgcharom.setImageResource(R.drawable.multiply);
        }else if (vaziat[2]== ROBOT && vaziat[5]== KHALI){
            vaziat[5] = ROBOT;
            imgpanjom.setImageResource(R.drawable.multiply);
        }else if (vaziat[3]== ROBOT && vaziat[0]== KHALI){
            vaziat[0] = ROBOT;
            imgsefrom.setImageResource(R.drawable.multiply);
        }else if (vaziat[3]== ROBOT && vaziat[4]== KHALI){
            vaziat[4] = ROBOT;
            imgcharom.setImageResource(R.drawable.multiply);
        }else if (vaziat[3]== ROBOT && vaziat[6]== KHALI){
            vaziat[6] = ROBOT;
            imgsheshom.setImageResource(R.drawable.multiply);
        } else if (vaziat[4]== ROBOT && vaziat[3]== KHALI){
            vaziat[3] = ROBOT;
            imgsevom.setImageResource(R.drawable.multiply);
        }else if (vaziat[4]== ROBOT && vaziat[1]== KHALI){
            vaziat[1] = ROBOT;
            imgyekom.setImageResource(R.drawable.multiply);
        }else if (vaziat[4]== ROBOT && vaziat[5]== KHALI) {
            vaziat[5] = ROBOT;
            imgpanjom.setImageResource(R.drawable.multiply);
        }else if (vaziat[4]== ROBOT && vaziat[7]== KHALI) {
            vaziat[7] = ROBOT;
            imghaftom.setImageResource(R.drawable.multiply);
        }else if (vaziat[5]== ROBOT && vaziat[2]== KHALI) {
            vaziat[2] = ROBOT;
            imgDowom.setImageResource(R.drawable.multiply);
        }else if (vaziat[5]== ROBOT && vaziat[4]== KHALI) {
            vaziat[4] = ROBOT;
            imgcharom.setImageResource(R.drawable.multiply);
        }else if (vaziat[5]== ROBOT && vaziat[8]== KHALI) {
            vaziat[8] = ROBOT;
            imghashtom.setImageResource(R.drawable.multiply);
        }else if (vaziat[6]== ROBOT && vaziat[3]== KHALI) {
            vaziat[3] = ROBOT;
            imgsevom.setImageResource(R.drawable.multiply);
        }else if (vaziat[6]== ROBOT && vaziat[7]== KHALI) {
            vaziat[7] = ROBOT;
            imghaftom.setImageResource(R.drawable.multiply);
        }else if (vaziat[6]== ROBOT && vaziat[4]== KHALI){
            vaziat[4] = ROBOT;
            imgcharom.setImageResource(R.drawable.multiply);
        }else if (vaziat[7]== ROBOT && vaziat[6]== KHALI) {
            vaziat[6] = ROBOT;
            imgsheshom.setImageResource(R.drawable.multiply);
        }else if (vaziat[7]== ROBOT && vaziat[4]== KHALI) {
            vaziat[4] = ROBOT;
            imgcharom.setImageResource(R.drawable.multiply);
        }else if (vaziat[7]== ROBOT && vaziat[8]== KHALI) {
            vaziat[8] = ROBOT;
            imghashtom.setImageResource(R.drawable.multiply);
        }else if (vaziat[8]== ROBOT && vaziat[5]== KHALI) {
            vaziat[5] = ROBOT;
            imgpanjom.setImageResource(R.drawable.multiply);

        }else if (vaziat[8]== ROBOT && vaziat[7]== KHALI) {
            vaziat[7] = ROBOT;
            imghaftom.setImageResource(R.drawable.multiply);

        }else if (vaziat[8]== ROBOT && vaziat[4]== KHALI){
            vaziat[4] = ROBOT;
            imgcharom.setImageResource(R.drawable.multiply);

        }}

    public void Random(){

        Random random = new Random();
        int adad = random.nextInt(8);
        vaziat[adad] = ROBOT;
        click_snd.start();
        if (adad==0){
            imgsefrom.setImageResource(R.drawable.multiply);
        }else if (adad==1){
            imgyekom.setImageResource(R.drawable.multiply);
        }else if (adad==2){
            imgDowom.setImageResource(R.drawable.multiply);
        }else if (adad==3){
            imgsevom.setImageResource(R.drawable.multiply);
        }else if (adad==4){
            imgcharom.setImageResource(R.drawable.multiply);
        }else if (adad==5){
            imgpanjom.setImageResource(R.drawable.multiply);
        }else if (adad==6){
            imgsheshom.setImageResource(R.drawable.multiply);
        }else if (adad==7){
            imghaftom.setImageResource(R.drawable.multiply);
        }else if (adad==8){
            imghashtom.setImageResource(R.drawable.multiply);
        }
    }

   public void animation(int[] array) {

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

        txt_player1_res.setText("Robot");
        txt_player2_res.setText(MyName);
        txt_result.setText(result);
        txt_EmtiazShoma_res.setText(EmtiazePlayer + "");
        txt_EmtiazHarif_res.setText(EmtiazeRobot + "");

        if (result.equals("You Win")){

            img.setImageResource(R.drawable.winimage);
        }else if (result.equals("You Loss")){

            img.setImageResource(R.drawable.lossimage);
        }else {
            img.setImageResource(0);
        }





    }
    public void intent_main(View view){

        startActivity(new Intent(YeNafare_Hard.this,MainActivity.class));
        finish();
    }
}
