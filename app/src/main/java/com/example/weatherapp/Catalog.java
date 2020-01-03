package com.example.weatherapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Catalog extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new MyAdapter(this, getMyList());
        mRecyclerView.setAdapter(myAdapter);
    }

    private ArrayList<CatalogModel> getMyList() {

        ArrayList<CatalogModel> models = new ArrayList<>();

        CatalogModel m = new CatalogModel();
        m.setTitle("Pick Axe");
        m.setDescription("TABOR TOOLS Pick Mattock with Fiberglass Handle, Garden Pick, Great for Loosening Soil, Archaeological Projects, and Cultivating Vegetable Gardens or Flower Beds.");
        m.setImg(R.drawable.pickaxe);
        models.add(m);

        m = new CatalogModel();
        m.setTitle("Hoe");
        m.setDescription("DeWit Bio Onion Hand Hoe, Earth friendly way to get rid of weeds");
        m.setImg(R.drawable.hoe);
        models.add(m);

        m = new CatalogModel();
        m.setTitle("Shovel");
        m.setDescription("First hybrid shovel/spade guaranteed to significantly reduce digging efforts.");
        m.setImg(R.drawable.shovel);
        models.add(m);

        m = new CatalogModel();
        m.setTitle("Sickle");
        m.setDescription("Zenport Aluminum Handle K310 Brush Clearing Sickle with Carbon Steel Blade and Aluminu, 9\"");
        m.setImg(R.drawable.sickle);
        models.add(m);

        m = new CatalogModel();
        m.setTitle("Spade");
        m.setDescription("ROOT ASSASSIN RA-002 32-Inch Carbon Steel Shovel");
        m.setImg(R.drawable.spade);
        models.add(m);

        m = new CatalogModel();
        m.setTitle("Organic Fertilizer");
        m.setDescription("3 in 1 Bio-Organic Fertilizer 5-5-5 ( 60% organic content, 400grams, pellet form)");
        m.setImg(R.drawable.fertilizer);
        models.add(m);

        m = new CatalogModel();
        m.setTitle("Set of Fertilizer");
        m.setDescription("Golden Tree: Best Plant Food For Plants & Trees - Yield Increaser - Plant Rescuer - Excelurator - All-In-One");
        m.setImg(R.drawable.organic_fertilizer);
        models.add(m);

        return models;
    }
}
