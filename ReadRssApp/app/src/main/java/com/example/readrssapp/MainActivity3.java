package com.example.readrssapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {
    AppDatabase database = null;
    ItemDAO itemDAO = null;
    List<News> items = null;
    ListView listView;
    CustomAdapter customAdapter;
    ImageView imgDeleteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        listView = findViewById(R.id.listView);
        imgDeleteAll = findViewById(R.id.imgDelete);

        database = Room.databaseBuilder(this, AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();

        itemDAO = database.getItemDAO();
        items = itemDAO.getItems();
        customAdapter = new CustomAdapter(MainActivity3.this, android.R.layout.simple_list_item_1, (ArrayList<News>) items);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
                intent.putExtra("new", items.get(i));
                startActivity(intent);
            }
        });

        imgDeleteAll.setOnClickListener(view -> {
            new AlertDialog.Builder(MainActivity3.this)
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this entry?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            itemDAO.deleteAll();
                            items.clear();
                            customAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_delete)
                    .show();
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(MainActivity3.this)
                        .setTitle("Delete item")
                        .setMessage("Are you want to delete?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                itemDAO.delete(items.get(position));
                                items.remove(items.get(position));
                                customAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_delete)
                        .show();

                return false;
            }
        });

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}