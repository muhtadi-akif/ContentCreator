package multiplexer.contentcreator;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import multiplexer.contentcreator.views.AutoImageView;


/**
 * Created by USER on 3/20/2017.
 */

public class ImageEditor extends AppCompatActivity {
    private int screenWidth;
    TextView txtHeadline,txtSubHeadline,txtFrontLine;
    float dX;
    float dY;
    int lastAction;
    float lastX;
    float lastY;
    String TAG = "Photo save testing";
    private android.widget.RelativeLayout.LayoutParams layoutParams;
    AutoImageView imageView;
    RelativeLayout saveViewLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_editor_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#333333"));
        }
        imageView = (AutoImageView) findViewById(R.id.imageView);
        saveViewLayout = (RelativeLayout) findViewById(R.id.actualView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        ImageButton save = (ImageButton) toolbar.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //writeTextOnDrawable(Uri.parse(getIntent().getStringExtra("uri")),"Drag Testing",lastX,lastY);
                CreateBitmap(saveViewLayout);
            }
        });
        Picasso.with(this)
                .load(Uri.parse((getIntent().getStringExtra("uri"))))
                .placeholder(R.drawable.image_processing)
                .error(R.drawable.no_image)
                .into(imageView);

        txtHeadline = (TextView) findViewById(R.id.text);
        txtSubHeadline = (TextView) findViewById(R.id.subHeadlineText);
        txtFrontLine = (TextView) findViewById(R.id.frontLineText);

        txtHeadline.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        lastY =(event.getRawY() + dY);
                        lastX = (event.getRawX() + dX);
                        view.setY(event.getRawY() + dY);
                        view.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN)
                            //Toast.makeText(getBaseContext(), "Clicked!", Toast.LENGTH_SHORT).show();
                        editDialog("headline");
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });

        txtSubHeadline.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        lastY =(event.getRawY() + dY);
                        lastX = (event.getRawX() + dX);
                        view.setY(event.getRawY() + dY);
                        view.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN)
                            //Toast.makeText(getBaseContext(), "Clicked!", Toast.LENGTH_SHORT).show();
                        editDialog("sub headline");
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });

        txtFrontLine.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        lastY =(event.getRawY() + dY);
                        lastX = (event.getRawX() + dX);
                        view.setY(event.getRawY() + dY);
                        view.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN)
                            //Toast.makeText(getBaseContext(), "Clicked!", Toast.LENGTH_SHORT).show();
                        editDialog("front line");
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });
    }



    public void editDialog(final String position){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.text_editor_dialog);

        final EditText edt_unit = (EditText) dialog.findViewById(R.id.input_unit);
        TextView title_dialog = (TextView) dialog.findViewById(R.id.text_dialog);
        if(position=="headline"){
            title_dialog.setText("Change your headline here");
            if(txtHeadline.getText().equals("Your Headline")){
                edt_unit.setHint("Your headline here");
            } else {
                edt_unit.setText(txtHeadline.getText());
                edt_unit.append("");
            }

        } else if(position=="sub headline"){
            title_dialog.setText("Change your sub headline here");
            if(txtSubHeadline.getText().equals("Your Sub Headline")){
                edt_unit.setHint("Your sub headline here");
            } else {
                edt_unit.setText(txtSubHeadline.getText());
                edt_unit.append("");
            }


        } else if(position=="front line"){
            title_dialog.setText("Change your front line here");
            if(txtFrontLine.getText().equals("Your Front Line")){
                edt_unit.setHint("Your front line here");
            } else {
                edt_unit.setText(txtFrontLine.getText());
                edt_unit.append("");
            }
        }
        Button neg_dialogButton = (Button) dialog.findViewById(R.id.btn_neg);
        neg_dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button pos_dialogButton = (Button) dialog.findViewById(R.id.btn_pos);
        pos_dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_unit.getText().toString().equals("")){
                    dialog.dismiss();
                }else {
                    if(position=="headline"){
                        txtHeadline.setText(edt_unit.getText().toString());
                    } else if(position=="sub headline"){
                        txtSubHeadline.setText(edt_unit.getText().toString());
                    } else if(position=="front line"){
                        txtFrontLine.setText(edt_unit.getText().toString());
                    }

                    dialog.dismiss();
                }
            }
        });

        dialog.show();

    }

    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void CreateBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
         storeImage(bitmap);
    }

    private void writeTextOnDrawable(Uri imageUri, String text, float x , float y ) {

     /*   Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);*/

        Bitmap bm_take = null;
        try {
            bm_take = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bm = bm_take.copy(Bitmap.Config.ARGB_8888, true);
        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);
        float fontSize = txtHeadline.getTextSize();
        fontSize+=fontSize*0.2f;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        //paint.setTextSize(convertToPixels(this, 11));
        paint.setTextSize(fontSize);

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(this, 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (int) (((x) / 2) - 2);     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) (((y)/ 2) - ((paint.descent() + paint.ascent()) / 2)) ;
        Log.d("Canvas width",
                canvas.getWidth()+"");// e.getMessage());
        Log.d("Canvas Height",
                canvas.getHeight()+"");// e.getMessage());
        Log.d("Float X",
                x+"");// e.getMessage());
        Log.d("Float Y",
              y+"");// e.getMessage());
        canvas.drawText(text, xPos, yPos, paint);

        storeImage(bm);
        //return new BitmapDrawable(getResources(), bm);
    }



    public static int convertToPixels(Context context, int nDP)
    {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f) ;

    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            Toast.makeText(getBaseContext(),"Image Saved Successfully",Toast.LENGTH_LONG).show();
            refreshGallery(pictureFile);
            finish();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }


    }


    private void refreshGallery(File pictureFile){
        File imageFile = pictureFile;
        MediaScannerConnection.scanFile(this, new String[] { imageFile.getPath() }, new String[] { "image/*" }, null);
    }

    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Content Creator");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
}


