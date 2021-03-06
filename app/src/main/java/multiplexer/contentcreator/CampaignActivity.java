package multiplexer.contentcreator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import multiplexer.contentcreator.Database.DatabaseHelper;
import multiplexer.contentcreator.Helper.EndPoints;
import multiplexer.contentcreator.Helper.JsonConstants;
import multiplexer.contentcreator.Model.Brands;
import multiplexer.contentcreator.Model.Campaign;

/**
 * Created by USER on 4/2/2017.
 */

public class CampaignActivity extends AppCompatActivity {

    String[] arr = {"Choose Headline", "Suggestion 1", "Suggestion 2", "Suggestion 3"};
    String[] arr2 = {"Choose Sub Headline", "Suggestion 1", "Suggestion 2", "Suggestion 3"};
    String[] arr3 = {"Choose Front Line", "Suggestion 1", "Suggestion 2", "Suggestion 3"};
    String[] arr4 = {"Choose Call To Actions", "Suggestion 1", "Suggestion 2", "Suggestion 3"};
    Spinner spinnerHeadline, spinnerSubHeadline, spinnerFinePrints,spinnerCallToActions;
    EditText headline, subHeader, finePrints,outcome,audience,callToActions;
    ImageView logo;
    DatabaseHelper db;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        db = new DatabaseHelper(CampaignActivity.this);
        spinnerHeadline = (Spinner) findViewById(R.id.spinnerHeadline);
        spinnerSubHeadline = (Spinner) findViewById(R.id.spinnerSubHeadline);
        spinnerFinePrints = (Spinner) findViewById(R.id.spinnerFinePrints);
        spinnerCallToActions = (Spinner) findViewById(R.id.spinnerCallToActions);
        headline = (EditText) findViewById(R.id.editTextHeadline);
        subHeader = (EditText) findViewById(R.id.editTextSubHeadline);
        finePrints = (EditText) findViewById(R.id.editTextFinePrints);
        callToActions = (EditText) findViewById(R.id.editTextCallToActions);
        outcome = (EditText) findViewById(R.id.editTextOutcome);
        audience = (EditText) findViewById(R.id.editTextAudience);
        logo = (ImageView) findViewById(R.id.imageViewLogo);
        if(db.getBrandsCount()>0){
            ArrayList<Brands> arrBrands = db.getAllBrandsData();
            //edtTagline.setText(arrBrands.get(arrBrands.size()).getBrand_tagline());
           /* Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), arrBrands.get(arrBrands.size()-1).getPicUri());
                logo.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/
            File image = new File(getRealPathFromURI(arrBrands.get(arrBrands.size()-1).getPicUri()));
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
            logo.setImageBitmap(bitmap);
        }
        if(getIntent().hasExtra("flagPosition")){
            getSupportActionBar().setTitle("Campaign Details");
            headline.append(getIntent().getStringExtra("headline"));
            subHeader.append(getIntent().getStringExtra("subHeadline"));
            finePrints.append(getIntent().getStringExtra("frontLine"));
            callToActions.append(getIntent().getStringExtra("callToActions"));
            outcome.append(getIntent().getStringExtra("outcome"));
            audience.append(getIntent().getStringExtra("audience"));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CampaignActivity.this, android.R.layout.simple_spinner_dropdown_item,
                ParseHeadlineJSON(pref.getString(new JsonConstants().JSON_HEADLINE,"")));
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(CampaignActivity.this, android.R.layout.simple_spinner_dropdown_item,
                ParseSubHeadlineJSON(pref.getString(new JsonConstants().JSON_SUBHEADLINE,"")));
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(CampaignActivity.this, android.R.layout.simple_spinner_dropdown_item,
                ParseFrontLineJSON(pref.getString(new JsonConstants().JSON_FRONTLINE,"")));
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(CampaignActivity.this, android.R.layout.simple_spinner_dropdown_item,
                ParseCallToActionJSON(pref.getString(new JsonConstants().JSON_CALL_TO_ACTION,"")));
        spinnerHeadline.setAdapter(adapter);
        spinnerSubHeadline.setAdapter(adapter2);
        spinnerFinePrints.setAdapter(adapter3);
        spinnerCallToActions.setAdapter(adapter4);

        spinnerHeadline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    alertSuggestion("Headline",parent.getSelectedItem().toString());
                    //headline.setText(parent.getSelectedItem().toString());
                } else {
                    if(getIntent().hasExtra("flagPosition")){
                        //do nothing
                    }else {
                        headline.setText("");
                    }
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
                    alertSuggestion("Sub Headline",parent.getSelectedItem().toString());
                    //subHeader.setText(parent.getSelectedItem().toString());
                } else {
                    if(getIntent().hasExtra("flagPosition")){
                        //do nothing
                    }else {
                        subHeader.setText("");
                    }
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
                    alertSuggestion("Fine Prints",parent.getSelectedItem().toString());
                    //finePrints.setText(parent.getSelectedItem().toString());
                } else {
                    if(getIntent().hasExtra("flagPosition")){
                        //do nothing
                    }else {
                        finePrints.setText("");
                    }
                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCallToActions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    alertSuggestion("Call To Actions",parent.getSelectedItem().toString());
                    //finePrints.setText(parent.getSelectedItem().toString());
                } else {
                    if(getIntent().hasExtra("flagPosition")){
                        //do nothing
                    }else {
                        callToActions.setText("");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private ArrayList<String> ParseHeadlineJSON(String json) {
        if (json != null) {
            try {
                ArrayList<String> tempList = new ArrayList<String>();
                JSONObject jsonObj = new JSONObject(json);
                JSONArray templates = jsonObj.getJSONArray(new JsonConstants().SUGGESTION_HEADLINE_ARRAY);
                tempList.add("Choose Headline");
                for (int i = 0; i < templates.length(); i++) {
                    JSONObject c = templates.getJSONObject(i);
                    tempList.add(c.getString(new JsonConstants().SUGGESTION_HEADLINE));
                }
                return tempList;
            } catch (JSONException e) {
                e.printStackTrace();

                return null;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP Request");
            new EndPoints().noInernetToast(this);
            return null;
        }
    }

    private ArrayList<String> ParseSubHeadlineJSON(String json) {
        if (json != null) {
            try {
                ArrayList<String> tempList = new ArrayList<String>();
                JSONObject jsonObj = new JSONObject(json);
                JSONArray templates = jsonObj.getJSONArray(new JsonConstants().SUGGESTION_SUBHEADLINE_ARRAY);
                tempList.add("Choose Sub-Headline");
                for (int i = 0; i < templates.length(); i++) {
                    JSONObject c = templates.getJSONObject(i);
                    tempList.add(c.getString(new JsonConstants().SUGGESTION_SUBHEADLINE));
                }
                return tempList;
            } catch (JSONException e) {
                e.printStackTrace();

                return null;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP Request");
            new EndPoints().noInernetToast(this);
            return null;
        }
    }

    private ArrayList<String> ParseFrontLineJSON(String json) {
        if (json != null) {
            try {
                ArrayList<String> tempList = new ArrayList<String>();
                JSONObject jsonObj = new JSONObject(json);
                JSONArray templates = jsonObj.getJSONArray(new JsonConstants().SUGGESTION_FRONT_LINE_ARRAY);
                tempList.add("Choose Front Line");
                for (int i = 0; i < templates.length(); i++) {
                    JSONObject c = templates.getJSONObject(i);
                    tempList.add(c.getString(new JsonConstants().SUGGESTION_FRONT_LINE));
                }
                return tempList;
            } catch (JSONException e) {
                e.printStackTrace();

                return null;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP Request");
            new EndPoints().noInernetToast(this);
            return null;
        }
    }

    private ArrayList<String> ParseCallToActionJSON(String json) {
        if (json != null) {
            try {
                ArrayList<String> tempList = new ArrayList<String>();
                JSONObject jsonObj = new JSONObject(json);
                JSONArray templates = jsonObj.getJSONArray(new JsonConstants().SUGGESTION_CALL_TO_ACTION_ARRAY);
                tempList.add("Choose Call To Action");
                for (int i = 0; i < templates.length(); i++) {
                    JSONObject c = templates.getJSONObject(i);
                    tempList.add(c.getString(new JsonConstants().SUGGESTION_CALL_TO_ACTION));
                }
                return tempList;
            } catch (JSONException e) {
                e.printStackTrace();

                return null;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP Request");
            new EndPoints().noInernetToast(this);
            return null;
        }
    }



    public void alertSuggestion(final String type, final String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(CampaignActivity.this);
        dialog.setTitle(type+" Suggestions").setCancelable(false);
        dialog.setMessage("Are you sure you want to choose "+message+" as your "+type.toLowerCase()+"?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(type.equals("Headline")){
                    headline.setText(message);
                } else if(type.equals("Sub Headline")){
                    subHeader.setText(message);
                }else if(type.equals("Fine Prints")){
                    finePrints.setText(message);
                } else if(type.equals("Call To Actions")){
                    callToActions.setText(message);
                } else {
                    //do nothing
                }
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(type.equals("Headline")){
                   spinnerHeadline.setSelection(0);
                } else if(type.equals("Sub Headline")){
                    spinnerSubHeadline.setSelection(0);
                }else if(type.equals("Fine Prints")){
                    spinnerFinePrints.setSelection(0);
                } else {
                    //do nothing
                }

            }
        });
        dialog.show();
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
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
        } else if(callToActions.getText().toString().equals("")){
            callToActions.setError("Please write your call to actions or choose from the suggestion");
        } else if(finePrints.getText().toString().equals("")){
            finePrints.setError("Please write something about front line or choose from the suggestion");
        }
        else {
            Campaign c = new Campaign(getIntent().getIntExtra("camp_id",0), headline.getText().toString(),outcome.getText().toString(),audience.getText().toString(),
                    subHeader.getText().toString(),finePrints.getText().toString(),callToActions.getText().toString());
            if(getIntent().hasExtra("flagPosition")){
                db.updateCampaignData(c,getIntent().getIntExtra("camp_id",0));
            } else {
                db.insertCampaingData(c);
            }

            Intent intentMain = new Intent(CampaignActivity.this, CreateContent.class);
            intentMain.putExtra("tag", "campaign");
            intentMain.putExtra("headline",headline.getText().toString());
            intentMain.putExtra("subHeadline",subHeader.getText().toString());
            intentMain.putExtra("frontLine",finePrints.getText().toString());
            intentMain.putExtra("callToActions",callToActions.getText().toString());
            startActivity(intentMain);
            finishAffinity();
        }

    }

}
