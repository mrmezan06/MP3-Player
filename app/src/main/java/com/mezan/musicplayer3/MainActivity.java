package com.mezan.musicplayer3;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=(ListView)findViewById(R.id.listview);

        final ArrayList<File> mysongs = findSong(Environment.getExternalStorageDirectory());

        items = new String[mysongs.size()];
        for(int i=0;i<mysongs.size();i++){
            Toast.makeText(getApplicationContext(),mysongs.get(i).getName(),Toast.LENGTH_SHORT);
            items[i]=mysongs.get(i).getName().replace(".mp3","");
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,items);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",position).putExtra("songlist",mysongs));
            }
        });

    }
    public ArrayList<File> findSong(File root){

        ArrayList<File> al = new ArrayList<File>();
        File[] files=root.listFiles(); //all file and folder
        for(File singleFile : files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
              al.addAll(findSong(singleFile));
            }else {
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".MP3")){
                    al.add(singleFile);
                }
            }
        }

        return al;
    }
}
