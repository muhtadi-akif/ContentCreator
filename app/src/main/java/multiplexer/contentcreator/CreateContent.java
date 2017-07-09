package multiplexer.contentcreator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import multiplexer.contentcreator.Model.Template;
import multiplexer.contentcreator.adapter.GalleryImagesAdapter;
import multiplexer.contentcreator.adapter.Template_adapter;
import multiplexer.contentcreator.utils.Constants;
import multiplexer.contentcreator.utils.Image;
import multiplexer.contentcreator.utils.Params;
import multiplexer.contentcreator.utils.Utils;
import multiplexer.contentcreator.views.CustomProgressDialog;

public class CreateContent extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RelativeLayout parentLayout;
    RecyclerView recycler_view_gallery,recyclerView_template;
    public Params params;
    public CustomProgressDialog progressDialog;
    ArrayList<Image> imagesList = new ArrayList<>();
    GalleryImagesAdapter imageAdapter;
    AlertDialog alertDialog;
    ArrayList<Template> templatesList;
    Template_adapter template_adapter ;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    ImageButton refreshButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Content");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);
        recycler_view_gallery = (RecyclerView) findViewById(R.id.gallary_images);
        recyclerView_template = (RecyclerView) findViewById(R.id.templates);
        refreshButton = (ImageButton) findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.contains("picUri")){
                    editor.remove("picUri");
                    editor.commit();
                }
                Toast.makeText(getBaseContext(),"Refreshed",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(),CreateContent.class);
                startActivity(i);
                finish();
                overridePendingTransition(0,0);
            }
        });
        init();
        checkForPermissions();

    }

    private void init() {
        //Object object = this.getIntent().getSerializableExtra(Constants.KEY_PARAMS);
        handleInputParams();
        recycler_view_gallery.setLayoutManager(new StaggeredGridLayoutManager(3, GridLayoutManager.VERTICAL));
        recyclerView_template.setLayoutManager(new GridLayoutManager(this, 3));


        setDummyDataForTemplate();
    }

    public void setDummyDataForTemplate(){
        ArrayList <String> headlines = new ArrayList<>();
        ArrayList <String> subHeadlines = new ArrayList<>();
        ArrayList <String> frontLines = new ArrayList<>();

        for(int i = 0; i<10;i++){
            headlines.add(getIntent().getStringExtra("headline"));
            subHeadlines.add(getIntent().getStringExtra("subHeadline"));
            frontLines.add(getIntent().getStringExtra("frontLine"));
        }

        templatesList = new ArrayList<>();

        for(int i = 0 ; i < frontLines.size();i++){
            Template temp = new Template(headlines.get(i),subHeadlines.get(i),frontLines.get(i));
            templatesList.add(temp);
        }

        template_adapter = new Template_adapter(this,templatesList);
        recyclerView_template.setAdapter(template_adapter);
    }

    private void handleInputParams() {
        params = new Params();
        params.setCaptureLimit(1);
        params.setPickerLimit(1);
    }

    public void checkForPermissions() {
        if (hasStoragePermission(this))
            getImagesFromStorage();
        else
            requestStoragePermissions(this, Constants.REQUEST_STORAGE_PERMS);
    }

    public void requestStoragePermissions(Activity activity, int requestCode) {
        int hasReadPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasWritePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> permissions = new ArrayList<>();
        if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (writePermissionCheck == PackageManager.PERMISSION_DENIED
                || readPermissionCheck == PackageManager.PERMISSION_DENIED));
    }


    public void showProgressDialog(String message) {
        if (progressDialog == null)
            progressDialog = new CustomProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null)
            if (progressDialog.isShowing())
                progressDialog.dismiss();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_STORAGE_PERMS:
                if (validateGrantedPermissions(grantResults)) {
                    //new Recent_Photo().getImagesFromStorage();
                    Intent i = new Intent(this, CreateContent.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
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

    private void populateView(ArrayList<Image> images) {
        if (imagesList == null)
            imagesList = new ArrayList<>();
        imagesList.addAll(images);
        ArrayList<Image> dupImageSet = new ArrayList<>();
        dupImageSet.addAll(imagesList);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, GridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recycler_view_gallery.setLayoutManager(mLayoutManager);
        imageAdapter = new GalleryImagesAdapter(this, dupImageSet, 3, params);
        recycler_view_gallery.setAdapter(imageAdapter);
       /* imageAdapter.setOnHolderClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long imageId = (long) view.getTag(R.id.image_id);
                imageAdapter.setSelectedItem(view, imageId);
                ((MainActivity) getActivity()).changeFabText(imageAdapter.getSelectedPhotoNumber());
            }
        });*/

    }

    public void getImagesFromStorage() {
        new ApiSimulator(this).executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    private class ApiSimulator extends AsyncTask<Void, Void, ArrayList<Image>> {
        Activity context;
        String error = "";

        public ApiSimulator(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("Loading..");
        }

        @Override
        protected ArrayList<Image> doInBackground(@NonNull Void... voids) {
            ArrayList<Image> images = new ArrayList<>();
            Cursor imageCursor = null;
            int i = 0;
            try {
                final String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media.HEIGHT, MediaStore.Images.Media.WIDTH};
                final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
                imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
                while (imageCursor.moveToNext() && i < 500) {
                    long _id = imageCursor.getLong(imageCursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                    int height = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT));
                    int width = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH));
                    String imagePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(_id));
                    Image image = new Image(_id, uri, imagePath, (height > width) ? true : false);
                    images.add(image);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                error = e.toString();
            } finally {
                if (imageCursor != null && !imageCursor.isClosed()) {
                    imageCursor.close();
                }
            }
            return images;
        }

        @Override
        protected void onPostExecute(ArrayList<Image> images) {
            super.onPostExecute(images);
            dismissProgressDialog();
            if (isFinishing()) {
                return;
            }
            if (error.length() == 0)
                populateView(images);
            else
                Utils.showLongSnack(parentLayout, error);

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

    public void showLimitAlert(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("Alert")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onStop() {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onStop();
        if(pref.contains("picUri")){
            editor.remove("picUri");
            editor.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_brand) {
            Intent brands = new Intent(getBaseContext(), BrandsActivity.class);
            startActivity(brands);

        } else if (id == R.id.nav_campagin_new) {
            Intent campaignIntent = new Intent(getBaseContext(), CampaignActivity.class);
            startActivity(campaignIntent);

        } else if (id == R.id.nav_campaign_existing) {
            Intent intentCampaignList = new Intent(getBaseContext(), CampaignListActivity.class);
            startActivity(intentCampaignList);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
