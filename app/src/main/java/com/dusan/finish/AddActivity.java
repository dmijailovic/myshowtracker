package com.dusan.finish;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.HashMap;

public class AddActivity extends AppCompatActivity {

    RadioGroup rg;
    EditText name_et, date_et, genre_et;
    RatingBar rating;
    CheckBox fav;
    DBHelper dbh;

    HashMap values = new HashMap();

    public static final String FAVOURITE_KEY = "favourite";
    public static final String TYPE_KEY = "type";
    public static final String RATING_KEY = "rating";
    public static final String NAME_KEY = "name";
    public static final String DATE_KEY = "date";
    public static final String GENRE_KEY = "genre";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        try {getSupportActionBar().hide();}
        catch (Exception e) {
            Log.v("bar","fullscreen");
        }

        rg = (RadioGroup) findViewById(R.id.type_rg);
        name_et = (EditText) findViewById(R.id.name_et);
        rating = (RatingBar) findViewById(R.id.ratingBar);
        fav = (CheckBox) findViewById(R.id.favourite_cb);
        date_et = (EditText) findViewById(R.id.date_et);
        genre_et = (EditText) findViewById(R.id.genre_et);
        dbh = new DBHelper(this, null, null, 1);
    }

    public void getCBValue(){
        values.clear();
        if(fav.isChecked()){
            values.put(FAVOURITE_KEY, "yes");
        } else {
            values.put(FAVOURITE_KEY, "no");
        }
    }

    public void getRadioValue(){
        int id = rg.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) findViewById(id);
        String type = rb.getText().toString();
        values.put(TYPE_KEY, type);
    }

    public void getSpinnerValue(){
        String rating_value = String.valueOf(rating.getRating());
        String last_number = Character.toString(rating_value.charAt(2));
        String number = Character.toString(rating_value.charAt(0));
        if(last_number.equals("0")) {
            values.put(RATING_KEY, number);}
        else {
            values.put(RATING_KEY, rating_value);
        }
    }

    public void getETValues(){
        String name = name_et.getText().toString();
        if(name.isEmpty()){
            values.put(NAME_KEY, "No name");}
        else {
            values.put(NAME_KEY, name);
        }
        String date = date_et.getText().toString();
        if(date.isEmpty()){
            values.put(DATE_KEY, "");
        } else{
            values.put(DATE_KEY, date);
        }
        String genre = genre_et.getText().toString();
        if(genre.isEmpty()){
            values.put(GENRE_KEY, "");
        } else{
            values.put(GENRE_KEY, genre);
        }

    }

    public void onAddClicked(View view){
        getCBValue();
        getRadioValue();
        getSpinnerValue();
        getETValues();

        String name = (String) values.get(NAME_KEY);
        String rating = (String) values.get(RATING_KEY);
        String favourite = (String) values.get(FAVOURITE_KEY);
        String type = (String) values.get(TYPE_KEY);
        String date = (String) values.get(DATE_KEY);
        String genre = (String) values.get(GENRE_KEY);

        dbh.insertShow(name, rating, favourite, type, date, genre);

        Toast.makeText(getApplicationContext(), "Show added successfully!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, MainActivity.class));
    }
}
