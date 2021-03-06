package com.dusan.finish;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SeriesActivity extends ListActivity {

    SQLiteDatabase db, dbw;
    Cursor cursor;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ListView list = getListView();
        list.setBackgroundResource(R.drawable.wall_lv);

        final SQLiteOpenHelper helper = new DBHelper(this, null, null, 1);

        db = helper.getReadableDatabase();
        dbw = helper.getWritableDatabase();

        cursor = db.rawQuery("SELECT _id, name, rating, date, genre FROM shows WHERE type=?", new String[]{"Series"});

        final CursorAdapter listAdapter = new SimpleCursorAdapter(this,
                R.layout.custom_lv_series,
                cursor,
                new String[]{"name", "rating", "date", "genre"},
                new int[]{R.id.show_names, R.id.show_ratings, R.id.show_dates, R.id.show_genres}, 0);

        if(listAdapter.getCount()!=0){
            list.setAdapter(listAdapter);
        }else{
            list.setBackgroundResource(R.drawable.wall_empty);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), "Long click series for more options!", Toast.LENGTH_SHORT).show();

            }

        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv1 = (TextView) view.findViewById(R.id.show_names);

                result = tv1.getText().toString();

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        SeriesActivity.this);
                alert.setTitle("Series");
                alert.setMessage("Choose your option:");
                alert.setPositiveButton("Delete series", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteShow(result);

                        listAdapter.changeCursor(db.rawQuery("SELECT _id, name, rating, date, genre FROM shows WHERE type=?", new String[]{"Series"}));

                        listAdapter.notifyDataSetChanged();

                        dialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Series deleted!", Toast.LENGTH_SHORT).show();

                    }
                });

                alert.setNegativeButton("Add to favourites", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        addToFavourites(result);

                        dialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Series added to favourites!", Toast.LENGTH_SHORT).show();
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
