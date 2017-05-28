package com.dusan.finish;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {getSupportActionBar().hide();}
        catch (Exception e) {
            Log.v("bar","fullscreen");
        }

    }

    public void movies(View view) { startActivity(new Intent(this, MoviesActivity.class)); }

    public void series(View view) { startActivity(new Intent(this, SeriesActivity.class)); }

    public void fav(View view) { startActivity(new Intent(this, FavActivity.class)); }

    public void add(View view) { startActivity(new Intent(this, AddActivity.class)); }

    public void search(View view) { startActivity(new Intent(this, SearchActivity.class)); }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
