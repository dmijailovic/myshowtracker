package com.dusan.finish;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    Spinner spinner2;
    EditText pretraga_vrednost;
    ListView lista_pregled;
    String crit, val, result;
    SQLiteDatabase db, dbw;
    Cursor cursor;
    CursorAdapter listAdapter;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new);

        layout = (RelativeLayout) findViewById(R.id.search_layout);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        try {getSupportActionBar().hide();}
        catch (Exception e) {
            Log.v("bar","fullscreen");
        }

        spinner2 = (Spinner) findViewById(R.id.by_spinner);
        pretraga_vrednost = (EditText) findViewById(R.id.search_value);
        lista_pregled = (ListView) findViewById(R.id.part_lv);

        lista_pregled.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), "Long click movie for more options!", Toast.LENGTH_SHORT).show();

            }

        });

        lista_pregled.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv1 = (TextView) view.findViewById(R.id.show_name);

                result = tv1.getText().toString();

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        SearchActivity.this);
                alert.setTitle("Show");
                alert.setMessage("Choose your option:");
                alert.setPositiveButton("Delete show", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteShow(result);

                        if(crit.equals("Genre")){
                            listAdapter.changeCursor(db.rawQuery("SELECT _id, name, rating, date, genre FROM shows WHERE genre LIKE ?", new String[]{val}));}
                        else {
                            listAdapter.changeCursor(db.rawQuery("SELECT _id, name, rating, date, genre FROM shows WHERE name LIKE ?", new String[]{val}));
                        }


                        listAdapter.notifyDataSetChanged();

                        dialog.dismiss();

                    }
                });

                alert.setNegativeButton("Add to favourites", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        addToFavourites(result);

                        dialog.dismiss();
                    }
                });

                alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

                alert.show();

                return true;


            }
        });

    }

    public void onSearchClicked(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);

        final SQLiteOpenHelper helper = new DBHelper(this, null, null, 1);
        db = helper.getReadableDatabase();
        dbw = helper.getWritableDatabase();

        crit = spinner2.getSelectedItem().toString();
        val = pretraga_vrednost.getText().toString();

        if(crit.equals("Name")){
            cursor = db.rawQuery("SELECT _id, name, rating, date, genre FROM shows WHERE name LIKE ?", new String[]{val});
        } else {
            cursor = db.rawQuery("SELECT _id, name, rating, date, genre FROM shows WHERE genre LIKE ?", new String[]{val});
        }

        listAdapter = new SimpleCursorAdapter(this,
                R.layout.custom_list_view,
                cursor,
                new String[]{"name", "rating", "date", "genre"},
                new int[]{R.id.show_name, R.id.show_rating, R.id.show_date, R.id.show_genre}, 0);

        listAdapter.notifyDataSetChanged();


        if(listAdapter.getCount() != 0){
            lista_pregled.setAdapter(listAdapter);}
        else {
            Toast.makeText(getApplicationContext(), "No shows found! Try again!",Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteShow(String arg) {
        String table = "shows";
        String whereClause = "name=?";
        String[] whereArgs = new String[]{arg};
        dbw.delete(table, whereClause, whereArgs);
    }

    public void addToFavourites(String arg) {
        String query = "UPDATE shows SET favourite='yes' WHERE name='"+arg+"'";
        dbw.execSQL(query);
    }

}
