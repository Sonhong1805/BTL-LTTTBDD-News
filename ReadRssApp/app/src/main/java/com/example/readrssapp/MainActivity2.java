package com.example.readrssapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {
    WebView webView;
    BottomNavigationView navigationView;
    boolean isBookmarked = false;
    News news;
    AppDatabase database = null;
    ItemDAO itemDAO = null;
    List<News> items = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        database = Room.databaseBuilder(this, AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();

        itemDAO = database.getItemDAO();
        items = itemDAO.getItems();

        webView = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        if (intent != null) {
            news = (News) intent.getSerializableExtra("new");
        }
        String duongLink = news.getLink();
        webView.loadUrl(duongLink);
        webView.setWebViewClient(new WebViewClient());

        navigationView = findViewById(R.id.navBottom);
        MenuItem menuItem = navigationView.getMenu().findItem(R.id.storage_nav);
        int dem =0;
        for (News news1 : items){
            if (news1.getLink().equals(duongLink) ){
                break;
            }else{
               dem ++;
            }
        }

        if (items.size() == dem ){
            menuItem.setIcon(R.drawable.ic_baseline_bookmark_border_24);
        }else{
            menuItem.setIcon(R.drawable.ic_baseline_bookmark_24);
        }

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share_nav:
                        shareLink();
                        return true;
                    case R.id.home_nav:
                        // Trở về MainActivity
                        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.storage_nav:
//                        isBookmarked = !isBookmarked;
//                        item.setIcon(isBookmarked ? R.drawable.ic_baseline_bookmark_24: R.drawable.ic_baseline_bookmark_border_24);
//                        String message = isBookmarked ? "Đã lưu trang web" : "Đã bỏ lưu trang web";
//                        Toast.makeText(MainActivity2.this, message, Toast.LENGTH_SHORT).show();


                        boolean check = true;
                        News newsTB = null;
                        for (News news1 : items) {
                            if (news1.link.equals(news.link)) {
                                check = false;
                                newsTB = news1;
                            }
                        }

                        if (check) {
                            itemDAO.insert(news);
                            Toast.makeText(MainActivity2.this, "Đã lưu trang web", Toast.LENGTH_SHORT).show();
                            item.setIcon(R.drawable.ic_baseline_bookmark_24);
                        } else {
                            itemDAO.delete(newsTB);
                            Toast.makeText(MainActivity2.this, "Đã bỏ lưu trang web", Toast.LENGTH_SHORT).show();
                            item.setIcon(R.drawable.ic_baseline_bookmark_border_24);
                        }

                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void shareLink() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"nguyenhongson18052003@email.com"});
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Chia sẻ bài báo ");
        shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
        startActivity(Intent.createChooser(shareIntent, "Share link"));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}