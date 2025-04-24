package com.inventoryapp.inventoryapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import androidx.gridlayout.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class DisplayInventory extends AppCompatActivity{

    private InventoryDatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display_inventory);

        dbManager = new InventoryDatabaseManager(this);

        try {
            dbManager.open();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button addItem = findViewById(R.id.buttonAddItemView);
        Button updateItem = findViewById(R.id.buttonUpdateItem);

        addItem.setOnClickListener(v -> addItem());
        updateItem.setOnClickListener(v -> updateItem());

        update();
    }

    private void addItem() {
        Intent add_item_intent = new Intent(getApplicationContext(), AddInventoryItem.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(add_item_intent);
        update();
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }


    private void updateItem() {
        Intent add_update_intent = new Intent(getApplicationContext(), UpdateInventoryItem.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(add_update_intent);
        update();
    }

    public void update() {
        int idx = 0;
        Cursor c = dbManager.fetch();
        GridLayout inventory = findViewById(R.id.inventoryGrid);
        inventory.removeAllViews();

        if (c.moveToFirst()) {
            do {
                TextView id = new TextView(this);
                id.setMinWidth(100);
                TextView name = new TextView(this);
                name.setMinWidth(300);
                TextView quantity = new TextView(this);
                quantity.setMinWidth(350);
                Button delete = new Button(this);
                delete.setMinWidth(50);
                delete.setBackgroundColor(Color.CYAN);

                id.setText(c.getString(0));
                name.setText(c.getString(1));
                quantity.setText(c.getString(2));
                delete.setText("Delete");
                delete.setOnClickListener(v -> delete(id.getText().toString()));

                inventory.addView(id, idx++);
                inventory.addView(name, idx++);
                inventory.addView(quantity, idx++);
                inventory.addView(delete, idx++);
            } while (c.moveToNext());
            TextView idHeader = new TextView(this);
            idHeader.setMinWidth(100);
            TextView nameHeader = new TextView(this);
            nameHeader.setMinWidth(300);
            TextView quantityHeader = new TextView(this);
            quantityHeader.setMinWidth(350);
            TextView fakeButton = new TextView(this);
            fakeButton.setMinWidth(50);

            idHeader.setText("ID");
            nameHeader.setText("Item Name");
            quantityHeader.setText("Quantity");
            fakeButton.setText("");

            inventory.addView(idHeader,0);
            inventory.addView(nameHeader,1);
            inventory.addView(quantityHeader,2);
            inventory.addView(fakeButton, 3);
        }
        c.close();
    }


    public void delete(String id) {
        dbManager.delete(Long.parseLong(id));
        update();
    }
}