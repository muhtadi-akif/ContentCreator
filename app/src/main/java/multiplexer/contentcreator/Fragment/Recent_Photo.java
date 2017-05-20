package multiplexer.contentcreator.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import multiplexer.contentcreator.ImageEditor;
import multiplexer.contentcreator.R;
import multiplexer.contentcreator.adapter.GalleryImagesAdapter;
import multiplexer.contentcreator.utils.Constants;
import multiplexer.contentcreator.utils.Image;
import multiplexer.contentcreator.utils.Params;
import multiplexer.contentcreator.utils.Utils;
import multiplexer.contentcreator.views.CustomProgressDialog;


public class Recent_Photo extends Fragment {
    RelativeLayout parentLayout;
    RecyclerView recycler_view;
    public Params params;
    public CustomProgressDialog progressDialog;
    ArrayList<Image> imagesList = new ArrayList<>();
    GalleryImagesAdapter imageAdapter;

    public Recent_Photo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.recent_photo, container, false);
        parentLayout = (RelativeLayout) rootView.findViewById(R.id.parentLayout);
        recycler_view = (RecyclerView) rootView.findViewById(R.id.gallary_images);
        init();
        checkForPermissions();
        return rootView;
    }


    private void init() {
        //Object object = this.getIntent().getSerializableExtra(Constants.KEY_PARAMS);
        handleInputParams();
        recycler_view.setLayoutManager(new StaggeredGridLayoutManager(3, GridLayoutManager.VERTICAL));
    }

    private void handleInputParams() {
        params = new Params();
        params.setCaptureLimit(1);
        params.setPickerLimit(1);
    }

    public void checkForPermissions() {
        if (hasStoragePermission(getActivity()))
            getImagesFromStorage();
        else
            requestStoragePermissions(getActivity(), Constants.REQUEST_STORAGE_PERMS);
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
            progressDialog = new CustomProgressDialog(getActivity());
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

    private void populateView(ArrayList<Image> images) {
        if (imagesList == null)
            imagesList = new ArrayList<>();
        imagesList.addAll(images);
        ArrayList<Image> dupImageSet = new ArrayList<>();
        dupImageSet.addAll(imagesList);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, GridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recycler_view.setLayoutManager(mLayoutManager);
        imageAdapter = new GalleryImagesAdapter(getActivity(), dupImageSet, 3, params);
        recycler_view.setAdapter(imageAdapter);
        /*imageAdapter.setOnHolderClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long imageId = (long) view.getTag(R.id.image_id);
                imageAdapter.setSelectedItem(view, imageId);
                ((MainActivity) getActivity()).changeFabText(imageAdapter.getSelectedPhotoNumber());
            }
        });*/
    }

    public void prepareResult() {
        if(imageAdapter!=null){
            ArrayList<Long> selectedIDs = imageAdapter.getSelectedIDs();
            ArrayList<Image> selectedImages = new ArrayList<>(selectedIDs.size());
            for (Image image : imagesList) {
                if (selectedIDs.contains(image._id))
                    selectedImages.add(image);
            }
            Intent intent = new Intent(getActivity(), ImageEditor.class);
            intent.putParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST, selectedImages);
            startActivity(intent);
        } else {
            //do nothing
        }

    }

    public void getImagesFromStorage() {
        new ApiSimulator(getActivity()).executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    public class ApiSimulator extends AsyncTask<Void, Void, ArrayList<Image>> {
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
                imageCursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
                while (imageCursor.moveToNext() && i < 50) {
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
            if (getActivity().isFinishing()) {
                return;
            }
            if (error.length() == 0)
                populateView(images);
            else
                Utils.showLongSnack(parentLayout, error);

        }
    }

}



