package multiplexer.contentcreator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import multiplexer.contentcreator.Database.DatabaseHelper;
import multiplexer.contentcreator.Model.Campaign;
import multiplexer.contentcreator.adapter.CampaignAdapter;
import multiplexer.contentcreator.utils.Constants;

public class MainListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CampaignAdapter mAdapter;
    private List<Campaign> campaignList = new ArrayList<>();
    DatabaseHelper db;
    TextView error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(MainListActivity.this);
        setContentView(R.layout.app_bar_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button skipBtn = (Button) toolbar.findViewById(R.id.buttonSkip);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMain = new Intent(MainListActivity.this, CreateContent.class);
                intentMain.putExtra("headline","Headline");
                intentMain.putExtra("subHeadline","Sub Headline");
                intentMain.putExtra("frontLine","Front Line");
                startActivity(intentMain);
            }
        });

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Sketch.ttf");
        TextView app_title = (TextView) findViewById(R.id.app_title);
        app_title.setTypeface(font);
        error = (TextView) findViewById(R.id.noData);
        recyclerView = (RecyclerView) findViewById(R.id.gallary_images);
        setData();
        mAdapter = new CampaignAdapter (campaignList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainListActivity.this);
                dialog.setTitle("Add brand and campaigns!").setCancelable(false);
                dialog.setMessage("Do you want to add your brand information and campaign details?");
                dialog.setPositiveButton("SKIP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentMain = new Intent(MainListActivity.this, CreateContent.class);
                        intentMain.putExtra("headline","Headline");
                        intentMain.putExtra("subHeadline","Sub Headline");
                        intentMain.putExtra("frontLine","Front Line");
                        startActivity(intentMain);
                        dialog.cancel();
                    }
                });

                dialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(db.getBrandsCount()<=0){
                            Toast.makeText(getBaseContext(),"You've to set your brands first then create your campaign",Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getBaseContext(),BrandsActivity.class);
                            i.putExtra("flag","must brand entry");
                            startActivity(i);
                            dialog.cancel();
                        } else {
                            Intent i = new Intent(getBaseContext(),CampaignActivity.class);
                            startActivity(i);
                            dialog.cancel();
                        }
                    }
                });
                dialog.show();

            }
        });
        checkForPermissions();

    }
    public void checkForPermissions() {
        if (hasStoragePermission(this)){

        }

        else
            requestStoragePermissions(this, Constants.REQUEST_STORAGE_PERMS);
    }

    public void requestStoragePermissions(Activity activity, int requestCode) {
        int hasReadPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasWritePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasCameraPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        List<String> permissions = new ArrayList<>();
        if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }

        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissions.toArray(new String[permissions.size()]), requestCode);
        }

    }

    public boolean hasStoragePermission(Context context) {
        int writePermissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (writePermissionCheck == PackageManager.PERMISSION_DENIED
                || readPermissionCheck == PackageManager.PERMISSION_DENIED
                || cameraPermissionCheck == PackageManager.PERMISSION_DENIED));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_STORAGE_PERMS:
                if (validateGrantedPermissions(grantResults)) {
                    //new Recent_Photo().getImagesFromStorage();
                    /*Intent i = new Intent(this, CreateContent.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();*/
                } else {
                    Toast.makeText(this, "Permissions not granted.", Toast.LENGTH_LONG).show();
                    requestStoragePermissions(this, Constants.REQUEST_STORAGE_PERMS);

                }
                break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    public boolean validateGrantedPermissions(int[] grantResults) {
        boolean isGranted = true;
        if (grantResults != null && grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    isGranted = false;
                    break;
                }
            }
            return isGranted;
        } else {
            isGranted = false;
            return isGranted;
        }
    }


    private void setData(){
       /* Campaign campaign = new Campaign("Campaign 1");
        campaignList.add(campaign);

        campaign = new Campaign("Campaign 2");
        campaignList.add(campaign);


        campaign = new Campaign("Campaign 3");
        campaignList.add(campaign);*/
        campaignList = db.getAllCampaingData();
        if(campaignList.size()<=0){
            error.setVisibility(View.VISIBLE);
        }

        //mAdapter.notifyDataSetChanged();
    }


}
