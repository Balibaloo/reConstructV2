package com.example.reconstructv2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reconstructv2.Models.ListingItem;
import com.example.reconstructv2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditListingItemActivity extends AppCompatActivity {
    private ListingItem currItem;

    private EditText nameEditText;
    private FloatingActionButton returnFab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listing_item);

        currItem = (ListingItem) getIntent().getSerializableExtra("itemToEdit");
        System.out.println("in edit activity, received name : " + currItem.getName());
        System.out.println("in edit activity, received id : " + currItem.getItemID());

        initViews();
        setOnClickListeners();
        displayItem(currItem);
    }



    private void initViews(){
    nameEditText = findViewById(R.id.edit_listing_item_editText);

    returnFab = findViewById(R.id.edit_listing_item_ReturnFAB);
    }


    private void setOnClickListeners(){
        returnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();

                resultIntent.putExtra("itemToEdit", currItem);
                System.out.println("on return name = " + currItem.getName());

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void displayItem(ListingItem item){

        nameEditText.setText(item.getName());

    };



}
