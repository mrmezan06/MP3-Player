package com.mezan.musicplayer3;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener{

    static MediaPlayer player;
    ArrayList<File> mysongs;
    int position;

    Uri uri;
    SeekBar sb;
    Thread updateSB;
    Button btnPrv,btnFB,btnPlay,btnFF,btnNxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btnPlay=(Button)findViewById(R.id.btnplay);
        btnPrv=(Button)findViewById(R.id.btnprev);
        btnFB=(Button)findViewById(R.id.btnbackward);
        btnFF=(Button)findViewById(R.id.btnforward);
        btnNxt=(Button)findViewById(R.id.btnnext);
        sb=(SeekBar)findViewById(R.id.seekbar);

        updateSB=new Thread(){
            @Override
            public void run() {

                int tduration = player.getDuration();
                int cposition=0;
                while (cposition < tduration){
                    try {
                        sleep(500);
                        cposition=player.getCurrentPosition();
                        sb.setProgress(cposition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //super.run();
            }
        };

        btnPrv.setOnClickListener(this);
        btnFB.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnFF.setOnClickListener(this);
        btnNxt.setOnClickListener(this);
        if(player != null){
            player.stop();
            player.release();
        }

        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        mysongs=(ArrayList) b.getParcelableArrayList("songlist");
        position=b.getInt("pos",0);
        uri = Uri.parse(mysongs.get(position).toString());
        player = MediaPlayer.create(getApplicationContext(),uri);
        player.start();
        sb.setMax(player.getDuration());
        updateSB.start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());
            }
        });
        btnPlay.setBackgroundResource(R.drawable.ic_action_pause2);


    }

    @Override
    public void onClick(View v) {

        int id=v.getId();
        try{
            switch (id){
                case R.id.btnplay:
                    if(player.isPlaying()){
                        btnPlay.setBackgroundResource(R.drawable.ic_action_play);
                        player.pause();
                    }else {

                        btnPlay.setBackgroundResource(R.drawable.ic_action_pause2);
                        player.start();

                    }
                    break;
                case R.id.btnforward:
                    player.seekTo(player.getCurrentPosition()+5000);
                    sb.setProgress(player.getCurrentPosition());
                    break;
                case R.id.btnbackward:
                    player.seekTo(player.getCurrentPosition()-5000);
                    sb.setProgress(player.getCurrentPosition());
                    break;
                case R.id.btnnext:
                    player.stop();
                    player.release();
                    //position = (position+1)%mysongs.size();
                    if(position+1>=mysongs.size())
                        position=0;
                    else
                        position=position+1;//last song a thakle positition k first a anar jonno
                    uri = Uri.parse(mysongs.get(position).toString());
                    player = MediaPlayer.create(getApplicationContext(),uri);
                    player.start();
                    sb.setMax(player.getDuration());
                    sb.setProgress(player.getCurrentPosition());
                    break;
                case R.id.btnprev:
                    player.stop();
                    player.release();
                   // position = (position-1<0) ? mysongs.size()-1:position-1;  //last song a thakle positition k first a anar jonno
                if(position-1<0){
                    position=mysongs.size()-1;
                }else {
                    position=position-1;
                }

                    uri = Uri.parse(mysongs.get(position).toString());
                    player = MediaPlayer.create(getApplicationContext(),uri);
                    player.start();
                    sb.setMax(player.getDuration());
                    sb.setProgress(player.getCurrentPosition());
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
