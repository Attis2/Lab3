package com.example.lab3databases;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText productName, productPrice;
    Button addBtn, findBtn, deleteBtn;
    ListView productListView;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        addBtn = findViewById(R.id.addBtn);
        findBtn = findViewById(R.id.findBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        productListView = findViewById(R.id.productListView);

        dbHandler = new MyDBHandler(this, null, null, 1);
        refreshList();

        addBtn.setOnClickListener(v -> {
            String name = productName.getText().toString();
            String priceStr = productPrice.getText().toString();
            if (!name.isEmpty() && !priceStr.isEmpty()) {
                double price = Double.parseDouble(priceStr);
                dbHandler.addProduct(new Product(name, price));
                refreshList();
            }
        });

        findBtn.setOnClickListener(v -> {
            String name = productName.getText().toString();
            Product p = dbHandler.findProduct(name);
            ArrayList<String> list = new ArrayList<>();
            if (p != null) list.add(p.toString());
            else list.add("No match found");
            productListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
        });

        deleteBtn.setOnClickListener(v -> {
            String name = productName.getText().toString();
            dbHandler.deleteProduct(name);
            refreshList();
        });
    }

    private void refreshList() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM products", null);
        ArrayList<String> products = new ArrayList<>();
        while (c.moveToNext()) products.add(c.getString(1) + " - $" + c.getDouble(2));
        c.close();
        db.close();
        productListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products));
    }
}