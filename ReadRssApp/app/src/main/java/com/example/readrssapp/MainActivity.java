package com.example.readrssapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    CustomAdapter customAdapter;
    ArrayList<News> arrayListNews;
    String currentUrl = "https://vnexpress.net/rss/tin-moi-nhat.rss";
    Button btnFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        arrayListNews = new ArrayList<News>();
        if (isNetworkAvailable()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new ReadData().execute(currentUrl);
                }
            });
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Không có kết nối mạng")
                    .setMessage("Vui lòng kiểm tra lại kết nối mạng và thử lại sau.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("new", arrayListNews.get(i));
                startActivity(intent);
            }
        });

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        EditText searchEditText = findViewById(R.id.searchEditText);
        Button searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchEditText.getText().toString().trim();
                ArrayList<News> filteredList = new ArrayList<>();
                for (News news : arrayListNews) {
                    if (news.title.toLowerCase().contains(searchText.toLowerCase())) {
                        filteredList.add(news);
                    }
                }
                customAdapter = new CustomAdapter(MainActivity.this, android.R.layout.simple_list_item_1, filteredList);
                listView.setAdapter(customAdapter);
            }
        });


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Tin mới nhất"));
        tabLayout.addTab(tabLayout.newTab().setText("Thế giới"));
        tabLayout.addTab(tabLayout.newTab().setText("Thể thao"));
        tabLayout.addTab(tabLayout.newTab().setText("Giải trí"));
        tabLayout.addTab(tabLayout.newTab().setText("Kinh doanh"));
        tabLayout.addTab(tabLayout.newTab().setText("Công nghệ"));
        tabLayout.addTab(tabLayout.newTab().setText("Giáo dục "));
        tabLayout.addTab(tabLayout.newTab().setText("Sức khoẻ"));
        tabLayout.addTab(tabLayout.newTab().setText("Khoa học"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                arrayListNews.clear();
                String category = tab.getText().toString();
                String url = getCategoryUrl(category);
                new ReadData().execute(url);
            }

            private String getCategoryUrl(String category) {
                switch (category) {
                    case "Tin mới nhất":
                        return "https://vnexpress.net/rss/tin-moi-nhat.rss";
                    case "Thế giới":
                        return "https://vnexpress.net/rss/the-gioi.rss";
                    case "Kinh doanh":
                        return "https://vnexpress.net/rss/kinh-doanh.rss";
                    case "Giải trí":
                        return "https://vnexpress.net/rss/giai-tri.rss";
                    case "Thể thao":
                        return "https://vnexpress.net/rss/the-thao.rss";
                    case "Công nghệ":
                        return "https://vnexpress.net/rss/cong-nghe.rss";
                    default:
                        return "";
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.news_vnExpress:
                        arrayListNews.clear();
                        currentUrl = "https://vnexpress.net/rss/tin-moi-nhat.rss";
                        new ReadData().execute(currentUrl);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        tabLayout.getTabAt(0).select();
                        return true;
                    case R.id.news_24h:
                        arrayListNews.clear();
                        currentUrl = "https://cdn.24h.com.vn/upload/rss/tintuctrongngay.rss";
                        new ReadData().execute(currentUrl);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        tabLayout.getTabAt(0).select();
                        return true;
                    case R.id.news_zing:
                        arrayListNews.clear();
                        currentUrl = "https://news.zing.vn/rss/thoi-su.rss";
                        new ReadData().execute(currentUrl);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        tabLayout.getTabAt(0).select();
                        return true;
                    case R.id.world_news:
                        arrayListNews.clear();
                        currentUrl = "https://vnexpress.net/rss/the-gioi.rss";
                        new ReadData().execute(currentUrl);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        tabLayout.getTabAt(1).select();
                        return true;
                    case R.id.sports_news:
                        arrayListNews.clear();
                        currentUrl = "https://vnexpress.net/rss/the-thao.rss";
                        new ReadData().execute(currentUrl);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        tabLayout.getTabAt(2).select();
                        return true;
                    case R.id.entertainment_news:
                        arrayListNews.clear();
                        currentUrl = "https://vnexpress.net/rss/giai-tri.rss";
                        new ReadData().execute(currentUrl);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        tabLayout.getTabAt(3).select();
                        return true;
                    case R.id.business_news:
                        arrayListNews.clear();
                        currentUrl = "https://vnexpress.net/rss/kinh-doanh.rss";
                        new ReadData().execute(currentUrl);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        tabLayout.getTabAt(4).select();
                        return true;
                    case R.id.technology_news:
                        arrayListNews.clear();
                        currentUrl = "https://vnexpress.net/rss/cong-nghe.rss";
                        new ReadData().execute(currentUrl);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        tabLayout.getTabAt(5).select();
                        return true;
                    default:
                        return false;
                }
            }
        });

        btnFeedback = findViewById(R.id.btnFeedback);
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.popup_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menuFeedbackEmail:
                        sendFeedbackEmail();
                        return true;
                    case R.id.menuExitApp:
                        Intent intent1=new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.newsSaved:
                        Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void showFeedbackDialog() {
        final int[] icons = {R.drawable.ic_star, R.drawable.ic_star, R.drawable.ic_star, R.drawable.ic_star, R.drawable.ic_star};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Đánh giá ứng dụng");
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        final int[] selectedStars = {0, 0, 0, 0, 0};
        for (int i = 0; i < icons.length; i++) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(icons[i]);
            imageView.setPadding(50, 50, 50, 50);
            imageView.setTag(i);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    for (int i = 0; i < icons.length; i++) {
                        ((ImageView) layout.getChildAt(i)).setImageResource(R.drawable.ic_star);
                    }
                    for (int i = 0; i <= index; i++) {
                        ((ImageView) layout.getChildAt(i)).setImageResource(R.drawable.ic_star_filled);
                        selectedStars[i] = 1;
                    }
                    Toast.makeText(MainActivity.this, "Cảm ơn bạn đã đánh giá " + (index + 1) + " sao", Toast.LENGTH_SHORT).show();
                }
            });
            layout.addView(imageView);
        }
        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendFeedbackEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"nguyenhongson18052003@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Góp ý về ứng dụng của tôi");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Xin chào,\n\nTôi có một số góp ý về ứng dụng của bạn:\n");
        startActivity(Intent.createChooser(emailIntent, "Gửi email"));
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class ReadData extends AsyncTask<String, Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            return ReadContent(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");
            NodeList nodeListDes = document.getElementsByTagName("description");
            String title = "";
            String link = "";
            String image = "";
            String pubDate = "";
            for(int i=0; i<nodeList.getLength();i++){
                String cData = nodeListDes.item(i + 1).getTextContent();
                Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher = p.matcher(cData);
                if(matcher.find()){
                    image = matcher.group(1);
                }
                Element element = (Element) nodeList.item(i);
                title = parser.getValue(element, "title");
                link = parser.getValue(element,"link");
                pubDate = parser.getValue(element,"pubDate");


                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(inputFormat.parse(pubDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long timeInMillis = calendar.getTimeInMillis();
                long now = System.currentTimeMillis();
                long diffInMillis = now - timeInMillis;
                long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);

                SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, dd/MM/yyyy, HH:mm", new Locale("vi", "VN"));
                String formattedPubDate = outputFormat.format(calendar.getTime()) + "( " + diffInMinutes + " phút trước )";

                arrayListNews.add(new News(title,link,image,formattedPubDate));
            }
            customAdapter = new CustomAdapter(MainActivity.this, android.R.layout.simple_list_item_1,arrayListNews);
            listView.setAdapter(customAdapter);

            super.onPostExecute(s);
        }
    }

    private String ReadContent(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {

            URL url = new URL(theUrl);

            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
