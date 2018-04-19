package com.example.aaron.seo1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RestaurantActivity extends AppCompatActivity implements View.OnClickListener
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_restauraunt);

        Button button  = (Button)findViewById(R.id.btnSubmit);
        button.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        EditText etRestaurantName = (EditText)findViewById(R.id.restNameInput);
        EditText etRestaurantAddress = (EditText)findViewById(R.id.restAddressInput);
        EditText etRestaurantCuisine = (EditText)findViewById(R.id.restCuisineInput);
        EditText etRestaurantRating = (EditText)findViewById(R.id.restRatingInput);

        String strRestaurantName = etRestaurantName.getText().toString();
        String strRestaurantAddress = etRestaurantAddress.getText().toString();
        String strRestaurantCuisine = etRestaurantCuisine.getText().toString();
        String strRestaurantRating = etRestaurantRating.getText().toString();

        bundle.putString("com.example.RestaurantName", strRestaurantName);
        bundle.putString("com.example.RestaurantAddress", strRestaurantAddress);
        bundle.putString("com.example.RestaurantCuisine", strRestaurantCuisine);
        bundle.putString("com.example.RestaurantRating", strRestaurantRating);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
