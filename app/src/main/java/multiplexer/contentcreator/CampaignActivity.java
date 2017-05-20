package multiplexer.contentcreator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import multiplexer.contentcreator.Database.DatabaseHelper;
import multiplexer.contentcreator.Model.Brands;
import multiplexer.contentcreator.Model.Campaign;

/**
 * Created by USER on 4/2/2017.
 */

public class CampaignActivity extends AppCompatActivity {

    String[] arr = {"Choose Headline", "Suggestion 1", "Suggestion 2", "Suggestion 3"};
    String[] arr2 = {"Choose Sub Headline", "Suggestion 1", "Suggestion 2", "Suggestion 3"};
    String[] arr3 = {"Choose Front Lines", "Suggestion 1", "Suggestion 2", "Suggestion 3"};
    Spinner spinnerHeadline, spinnerSubHeadline, spinnerFinePrints;
    EditText headline, subHeader, finePrints,outcome,audience,story;
    ImageView logo;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        db = new DatabaseHelper(CampaignActivity.this);
        spinnerHeadline = (Spinner) findViewById(R.id.spinnerHeadline);
        spinnerSubHeadline = (Spinner) findViewById(R.id.spinnerSubHeadline);
        spinnerFinePrints = (Spinner) findViewById(R.id.spinnerFinePrints);
        headline = (EditText) findViewById(R.id.editTextHeadline);
        subHeader = (EditText) findViewById(R.id.editTextSubHeadline);
        finePrints = (EditText) findViewById(R.id.editTextFinePrints);
        outcome = (EditText) findViewById(R.id.editTextOutcome);
        audience = (EditText) findViewById(R.id.editTextAudience);
        story = (EditText) findViewById(R.id.editTextStory);
        logo = (ImageView) findViewById(R.id.imageViewLogo);
        if(db.getBrandsCount()>0){
            ArrayList<Brands> arrBrands = db.getAllBrandsData();
            //edtTagline.setText(arrBrands.get(arrBrands.size()).getBrand_tagline());
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), arrBrands.get(arrBrands.size()-1).getPicUri());
                logo.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(getIntent().hasExtra("flagPosition")){
            headline.setText(getIntent().getStringExtra("headline"));
            subHeader.setText(getIntent().getStringExtra("subHeadline"));
            finePrints.setText(getIntent().getStringExtra("frontLine"));
            outcome.setText(getIntent().getStringExtra("outcome"));
            audience.setText(getIntent().getStringExtra("audience"));
            story.setText(getIntent().getStringExtra("story"));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CampaignActivity.this, android.R.layout.simple_spinner_dropdown_item, arr);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(CampaignActivity.this, android.R.layout.simple_spinner_dropdown_item, arr2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(CampaignActivity.this, android.R.layout.simple_spinner_dropdown_item, arr3);
        spinnerHeadline.setAdapter(adapter);
        spinnerSubHeadline.setAdapter(adapter2);
        spinnerFinePrints.setAdapter(adapter3);

        spinnerHeadline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    headline.setText(parent.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerSubHeadline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    subHeader.setText(parent.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerFinePrints.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    finePrints.setText(parent.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void suggestionIntent(View view) {
        if(outcome.getText().toString().equals("")){
            outcome.setError("Please write something about outcome");
        } else if(audience.getText().toString().equals("")){
            audience.setError("Please write something about audience");
        } else if(headline.getText().toString().equals("")){
            headline.setError("Please write something about headline or choose from the suggestion");
        } else if(subHeader.getText().toString().equals("")){
            subHeader.setError("Please write something about sub headline or choose from the suggestion");
        } else if(story.getText().toString().equals("")){
            story.setError("Please write a story for your campaign");
        }else if(finePrints.getText().toString().equals("")){
            finePrints.setError("Please write something about front line or choose from the suggestion");
        }
        else {
            Campaign c = new Campaign(headline.getText().toString(),outcome.getText().toString(),audience.getText().toString(),
                    subHeader.getText().toString(),story.getText().toString(),finePrints.getText().toString());
            if(getIntent().hasExtra("flagPosition")){
                db.updateCampaignData(c,getIntent().getIntExtra("flagPosition",0)+"");
            } else {
                db.insertCampaingData(c);
            }

            Intent intentMain = new Intent(CampaignActivity.this, CreateContent.class);
            intentMain.putExtra("tag", "campaign");
            startActivity(intentMain);
            finish();
        }

    }

}
