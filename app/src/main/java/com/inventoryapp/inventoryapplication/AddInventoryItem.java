package com.inventoryapp.inventoryapplication;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddInventoryItem extends AppCompatActivity {

    private InventoryDatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_inventory_item);

        dbManager = new InventoryDatabaseManager(this);

        try {
            dbManager.open();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

        Button addItem = findViewById(R.id.buttonUpdateItem);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addItem.setOnClickListener(v -> addInventoryItem());
    }

    private void addInventoryItem() {
        TextView itemName = findViewById(R.id.editItemName);
        TextView quantity = findViewById(R.id.editItemQuantity);

        if(!dbManager.checkExists(itemName.getText().toString())) {
            dbManager.insert(itemName.getText().toString(), quantity.getText().toString());
        }

        Intent display_inventory_intent = new Intent(getApplicationContext(), DisplayInventory.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(display_inventory_intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }
}