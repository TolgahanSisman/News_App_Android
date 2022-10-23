package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newsapp.Models.Articles;
import com.example.newsapp.Models.Headlines;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText SearchText;
    Button btnSearch;

    final String API_KEY = "4b818c8deb90418487d3e5ca7cf7be89";
    Adapter adapter;
    List<Articles> articles= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SearchText = findViewById(R.id.SearchText);
        btnSearch = findViewById(R.id.btnSearch);

        String country = getCountry();
        retrieveJson("",country,API_KEY);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!SearchText.getText().toString().equals("")){
                    retrieveJson(SearchText.getText().toString(),country,API_KEY);
                }else{
                    retrieveJson("",country,API_KEY);
                }
            }
        });



    }

    public void retrieveJson(String query, String country, String apiKey){

        Call<Headlines> call;
        if(!SearchText.getText().toString().equals("")){
            call = ApiClient.getInstance().getApi().getSpecificData(query,apiKey);
        }else{
            call = ApiClient.getInstance().getApi().getSpecificData(country,apiKey);
        }

        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if (response.isSuccessful() && response.body().getArticles() != null){
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(MainActivity.this,articles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }




}