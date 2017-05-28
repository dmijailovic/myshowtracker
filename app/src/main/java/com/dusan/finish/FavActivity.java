package com.dusan.finish;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FavActivity extends ListActivity {

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

        cursor = db.rawQuery("SELECT _id, name, rating, date, genre FROM shows WHERE favourite=?;", new String[]{"yes"});

        final CursorAdapter listAdapter = new SimpleCursorAdapter(this,
                R.layout.custom_list_view,
                cursor,
                new String[]{"name", "rating", "date", "genre"},
                new int[]{R.id.show_name, R.id.show_rating, R.id.show_date, R.id.show_genre}, 0);

        if(listAdapter.getCount()!=0){
            list.setAdapter(listAdapter);
        }else{
            list.setBackgroundResource(R.drawable.wall_empty);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), "Long click show to remove it from favourites!", Toast.LENGTH_SHORT).show();

            }

        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv1 = (TextView) view.findViewById(R.id.show_name);

                result = tv1.getText().toString();

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        FavActivity.this);
                alert.setTitle("Removing show from favourites!");
                alert.setMessage("Are you sure?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        removeFromFavourites(result);

                        listAdapter.changeCursor(db.rawQuery("SELECT _id, name, rating, date, genre FROM shows WHERE favourite=?;", new String[]{"yes"}));

                        listAdapter.notifyDataSetChanged();

                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

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

    public void removeFromFavourites(String arg) {
        String query = "UPDATE shows SET favourite='no' WHERE name='"+arg+"'";
        dbw.execSQL(query);
    }

}
