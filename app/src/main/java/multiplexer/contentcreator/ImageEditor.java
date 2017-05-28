package multiplexer.contentcreator;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.squareup.picasso.Picasso;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;
import multiplexer.contentcreator.Helper.ConvolutionMatrix;
import multiplexer.contentcreator.utils.Utils;
import multiplexer.contentcreator.views.AutoImageView;

/**
 * Created by USER on 3/20/2017.
 */

public class ImageEditor extends AppCompatActivity {
    static
    {
        System.loadLibrary("NativeImageProcessor");
    }
    private int screenWidth;
    TextView txtHeadline, txtSubHeadline, txtFrontLine;
    int selectedColor;
    int darkenedColor;
    int BGselectedColor;
    int BGdarkenedColor;
    float dX;
    float dY;
    int lastAction;
    float lastX;
    float lastY;
    String TAG = "Photo save testing";
    private android.widget.RelativeLayout.LayoutParams layoutParams;
    AutoImageView imageView;
    RelativeLayout saveViewLayout;
    ImageButton blur_btn, brightness_btn, sharpen_btn, saturation_btn, colorify_btn, background_btn;
    int brightness = 0, blur = 0, sharpen = 0, saturation = 0;
    private DrawableView drawableView;
    private DrawableViewConfig config = new DrawableViewConfig();
    CircleImageView LightFx, BlueFx, StruckVibe, LimeFx, NightFx;
    boolean colorify = false,litFx = false, bluFx = false,stVibe = false,lime=false,nit =false;
    ProgressDialog prog_dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_editor_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#333333"));
        }
        selectedColor = getResources().getColor(android.R.color.black);
        darkenedColor = Utils.getDarkColor(selectedColor);
        BGselectedColor = getResources().getColor(android.R.color.white);
        BGdarkenedColor = Utils.getDarkColor(selectedColor);
        imageView = (AutoImageView) findViewById(R.id.imageView);
        imageView.setDrawingCacheEnabled(true);
        saveViewLayout = (RelativeLayout) findViewById(R.id.actualView);
        blur_btn = (ImageButton) findViewById(R.id.blur);
        brightness_btn = (ImageButton) findViewById(R.id.brightness);
        background_btn = (ImageButton) findViewById(R.id.background);
        sharpen_btn = (ImageButton) findViewById(R.id.sharpen);
        colorify_btn = (ImageButton) findViewById(R.id.colorify);
        saturation_btn = (ImageButton) findViewById(R.id.saturation);
        drawableView = (DrawableView) findViewById(R.id.paintView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        Button strokeWidthMinusButton = (Button) toolbar.findViewById(R.id.strokeWidthMinusButton);
        Button strokeWidthPlusButton = (Button) toolbar.findViewById(R.id.strokeWidthPlusButton);
        Button changeColorButton = (Button) toolbar.findViewById(R.id.changeColorButton);
        Button undoButton = (Button) toolbar.findViewById(R.id.undoButton);
        ImageButton homeButton = (ImageButton) toolbar.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        strokeWidthPlusButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (colorify == true) {
                    config.setStrokeWidth(config.getStrokeWidth() + 10);
                } else {
                    Toast.makeText(getBaseContext(), "You need to enable colorify first", Toast.LENGTH_LONG).show();
                }

            }
        });
        strokeWidthMinusButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (colorify == true) {
                    config.setStrokeWidth(config.getStrokeWidth() - 10);
                } else {
                    Toast.makeText(getBaseContext(), "You need to enable colorify first", Toast.LENGTH_LONG).show();
                }

            }
        });
        changeColorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (colorify == true) {
                    setCustomColor();
                } else {
                    Toast.makeText(getBaseContext(), "You need to enable colorify first", Toast.LENGTH_LONG).show();
                }

            }
        });
        undoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (colorify == true) {
                    drawableView.undo();
                } else {
                    Toast.makeText(getBaseContext(), "You need to enable colorify first", Toast.LENGTH_LONG).show();
                }

            }
        });
        ImageButton save = (ImageButton) toolbar.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //writeTextOnDrawable(Uri.parse(getIntent().getStringExtra("uri")),"Drag Testing",lastX,lastY);
                prog_dialog = ProgressDialog.show(ImageEditor.this, "",
                        "Creating your image content....", true);
                prog_dialog.setCancelable(false);
                prog_dialog.show();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        //TODO your background code
                        CreateBitmap(saveViewLayout);
                    }
                });

            }
        });
        Picasso.with(this)
                .load(Uri.parse((getIntent().getStringExtra("uri"))))
                .placeholder(R.drawable.image_processing)
                .error(R.drawable.no_image)
                .into(imageView);
        initFilters();
        brightness_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekEdit("brightness");
            }
        });
        blur_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekEdit("blur");
            }
        });
        sharpen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekEdit("sharpen");
            }
        });
        colorify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initColorify();
            }
        });
        saturation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekEdit("saturation");
            }
        });
        background_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackgroundColor();
            }
        });
        txtHeadline = (TextView) findViewById(R.id.text);
        txtSubHeadline = (TextView) findViewById(R.id.subHeadlineText);
        txtFrontLine = (TextView) findViewById(R.id.frontLineText);
        txtHeadline.setText(getIntent().getStringExtra("headline"));
        txtSubHeadline.setText(getIntent().getStringExtra("subHeadline"));
        txtFrontLine.setText(getIntent().getStringExtra("frontLine"));
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
                        lastY = (event.getRawY() + dY);
                        lastX = (event.getRawX() + dX);
                        view.setY(event.getRawY() + dY);
                        view.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                       /* if (lastAction == MotionEvent.ACTION_DOWN){
                            editDialog("headline");
                        }*/
                        editDialog("headline");
                        //Toast.makeText(getBaseContext(), "Clicked!", Toast.LENGTH_SHORT).show();

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
                        lastY = (event.getRawY() + dY);
                        lastX = (event.getRawX() + dX);
                        view.setY(event.getRawY() + dY);
                        view.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                      /*  if (lastAction == MotionEvent.ACTION_DOWN)*/
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
                        lastY = (event.getRawY() + dY);
                        lastX = (event.getRawX() + dX);
                        view.setY(event.getRawY() + dY);
                        view.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                      /*  if (lastAction == MotionEvent.ACTION_DOWN)*/
                        //Toast.makeText(getBaseContext(), "Clicked!", Toast.LENGTH_SHORT).show();
                        editDialog("front line");
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        lastY = (event.getRawY() + dY);
                        lastX = (event.getRawX() + dX);
                        view.setY(event.getRawY() + dY);
                        view.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                      /*  if (lastAction == MotionEvent.ACTION_DOWN)*/
                        //Toast.makeText(getBaseContext(), "Clicked!", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });
    }

    public void initFilters() {
        LightFx = (CircleImageView) findViewById(R.id.light_fx);
        BlueFx = (CircleImageView) findViewById(R.id.blue_fx);
        StruckVibe = (CircleImageView) findViewById(R.id.struck_vibe_fx);
        LimeFx = (CircleImageView) findViewById(R.id.lime_fx);
        NightFx = (CircleImageView) findViewById(R.id.night_fx);

        //Bitmap outputImage = null;
        Filter filter = null;

        filter = SampleFilters.getStarLitFilter();
        LightFx.setImageBitmap(filter.processFilter(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.compress_sample), 500, 333, false)));
        LightFx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(litFx==false){
                    litFx = true;
                } else {
                    litFx = false;
                }
                changeEffectOnPic();
            }

        });


        filter = SampleFilters.getBlueMessFilter();
        BlueFx.setImageBitmap(filter.processFilter(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.compress_sample), 500, 333, false)));
        BlueFx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluFx==false){
                    bluFx = true;
                } else {
                    bluFx = false;
                }
                changeEffectOnPic();
            }
        });

        filter = SampleFilters.getAweStruckVibeFilter();
        StruckVibe.setImageBitmap(filter.processFilter(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.compress_sample), 500, 333, false)));
        StruckVibe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stVibe==false){
                    stVibe = true;
                } else {
                    stVibe = false;
                }
                changeEffectOnPic();
            }
        });

        filter = SampleFilters.getLimeStutterFilter();
        LimeFx.setImageBitmap(filter.processFilter(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.compress_sample), 500, 333, false)));
        LimeFx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lime==false){
                    lime = true;
                } else {
                    lime = false;
                }
                changeEffectOnPic();
            }
        });

        filter = SampleFilters.getNightWhisperFilter();
        NightFx.setImageBitmap(filter.processFilter(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.compress_sample), 500, 333, false)));
        NightFx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nit==false){
                    nit = true;
                } else {
                    nit = false;
                }
                changeEffectOnPic();
            }
        });
    }


    private void setCustomColor() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(selectedColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton("OK", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        changeColor(selectedColor);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void changeColor(int selectedColor) {
        this.selectedColor = selectedColor;
        this.darkenedColor = Utils.getDarkColor(selectedColor);
        config.setStrokeColor(selectedColor);
        //Toast.makeText(this,"New color selected" + Integer.toHexString(selectedColor),Toast.LENGTH_LONG).show();
    }


    private void setBackgroundColor() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(BGselectedColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton("OK", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        ChangeBackGroundColor(selectedColor);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void ChangeBackGroundColor(int selectedColor) {
        this.BGselectedColor = selectedColor;
        this.BGdarkenedColor = Utils.getDarkColor(selectedColor);
        saveViewLayout.setBackgroundColor(BGselectedColor);
        //Toast.makeText(this,"New color selected" + Integer.toHexString(selectedColor),Toast.LENGTH_LONG).show();
    }

    private void initColorify() {
        if (colorify == false) {
            Toast.makeText(getBaseContext(), "Colorify is enabled now you can draw anything on the image", Toast.LENGTH_LONG).show();
            drawableView.setVisibility(View.VISIBLE);
            colorify = true;
            colorify_btn.setBackgroundColor(getResources().getColor(R.color.blue));
            config.setStrokeColor(getResources().getColor(android.R.color.black));
            config.setShowCanvasBounds(true);
            config.setStrokeWidth(20.0f);
            config.setMinZoom(1.0f);
            config.setMaxZoom(3.0f);
            config.setCanvasHeight(1920);
            config.setCanvasWidth(1920);
            drawableView.setConfig(config);
        } else {
            Toast.makeText(getBaseContext(), "Colorify is disabled", Toast.LENGTH_LONG).show();
            drawableView.setVisibility(View.INVISIBLE);
            colorify_btn.setBackgroundColor(Color.TRANSPARENT);
            colorify = false;
            config.setStrokeColor(getResources().getColor(android.R.color.black));
            config.setShowCanvasBounds(false);
            config.setStrokeWidth(0);
            config.setMinZoom(0);
            config.setMaxZoom(0);
            config.setCanvasHeight(0);
            config.setCanvasWidth(0);
            drawableView.setConfig(config);
        }

    }

    public void seekEdit(final String for_) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.editor_slider);
        final TextView txt_loading = (TextView) dialog.findViewById(R.id.txt_loading);
        AppCompatSeekBar seekBar = (AppCompatSeekBar) dialog.findViewById(R.id.seekBar);
        if (for_.equals("brightness")) {
            seekBar.setProgress(brightness);
        } else if (for_.equals("blur")) {
            seekBar.setProgress(blur);
        } else if (for_.equals("sharpen")) {
            seekBar.setProgress(sharpen);
        } else if (for_.equals("saturation")) {
            seekBar.setProgress(saturation);
        } else {
            seekBar.setProgress(0);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                seekBar.setVisibility(View.GONE);
                txt_loading.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (for_.equals("brightness")) {
                            brightness = seekBar.getProgress();
                            imageView.setColorFilter(brightIt(brightness));
                            changeEffectOnPic();
                        } else if (for_.equals("blur")) {
                            blur = seekBar.getProgress();
                            changeEffectOnPic();

                        } else if (for_.equals("sharpen")) {
                            sharpen = seekBar.getProgress();
                            changeEffectOnPic();

                        } else if (for_.equals("saturation")) {
                            saturation = seekBar.getProgress();
                            changeEffectOnPic();

                        } else {
                            //do nothing
                        }
                        dialog.dismiss();
                    }
                }, 1000);

            }
        });

        dialog.show();

    }

    public void changeEffectOnPic() {
        Picasso.with(getBaseContext())
                .load(Uri.parse((getIntent().getStringExtra("uri"))))
                .placeholder(R.drawable.image_processing)
                .error(R.drawable.no_image)
                .into(imageView);
        if (sharpen > 0) {
            Bitmap bitmap = imageView.getDrawingCache();
            Bitmap sharpened = sharpen(bitmap, sharpen); //second parametre is radius
            imageView.setImageBitmap(sharpened);
            imageView.invalidate();
            sharpen_btn.setBackgroundColor(getResources().getColor(R.color.blue));
        } else {
            sharpen_btn.setBackgroundColor(Color.TRANSPARENT);
        }
        if (saturation > 0) {
            Bitmap bitmap = imageView.getDrawingCache();
            Bitmap saturated = applySaturationFilter(bitmap, saturation); //second parametre is radius
            imageView.setImageBitmap(saturated);
            imageView.invalidate();
            saturation_btn.setBackgroundColor(getResources().getColor(R.color.blue));
        } else {
            saturation_btn.setBackgroundColor(Color.TRANSPARENT);
        }
        if (brightness > 0) {
            //imageView.setColorFilter(brightIt(brightness));
            brightness_btn.setBackgroundColor(getResources().getColor(R.color.blue));
        } else {
            brightness_btn.setBackgroundColor(Color.TRANSPARENT);
        }
        if (blur > 0) {
            Bitmap bitmap = imageView.getDrawingCache();
            Bitmap blurred = fastblur(bitmap, 1, blur);//second parametre is radius
            imageView.setImageBitmap(blurred);
            imageView.invalidate();
            blur_btn.setBackgroundColor(getResources().getColor(R.color.blue));
        } else {
            blur_btn.setBackgroundColor(Color.TRANSPARENT);
        }

        if(litFx){
            Bitmap bitmap = imageView.getDrawingCache();
            Filter myFilter = SampleFilters.getStarLitFilter();
                /*Bitmap outputImage = myFilter.processFilter(bitmap);
                imageView.setImageBitmap(outputImage);
                imageView.invalidate();*/
            LightFx.setBackgroundColor(getResources().getColor(R.color.blue));
            imageView.setImageBitmap(myFilter.processFilter(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false)));
        } else {
            LightFx.setBackgroundColor(Color.TRANSPARENT);
        }
        if(bluFx){
            Bitmap bitmap = imageView.getDrawingCache();
            Filter myFilter = SampleFilters.getBlueMessFilter();
                /*Bitmap outputImage = myFilter.processFilter(bitmap);
                imageView.setImageBitmap(outputImage);
                imageView.invalidate();*/
            BlueFx.setBackgroundColor(getResources().getColor(R.color.blue));
            imageView.setImageBitmap(myFilter.processFilter(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false)));
        } else {
            BlueFx.setBackgroundColor(Color.TRANSPARENT);
        }

        if(stVibe){
            Bitmap bitmap = imageView.getDrawingCache();
            Filter myFilter = SampleFilters.getAweStruckVibeFilter();
                /*Bitmap outputImage = myFilter.processFilter(bitmap);
                imageView.setImageBitmap(outputImage);
                imageView.invalidate();*/
            StruckVibe.setBackgroundColor(getResources().getColor(R.color.blue));
            imageView.setImageBitmap(myFilter.processFilter(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false)));
        } else {
            StruckVibe.setBackgroundColor(Color.TRANSPARENT);
        }

        if(lime){
            Bitmap bitmap = imageView.getDrawingCache();
            Filter myFilter = SampleFilters.getLimeStutterFilter();
                /*Bitmap outputImage = myFilter.processFilter(bitmap);
                imageView.setImageBitmap(outputImage);
                imageView.invalidate();*/
            LimeFx.setBackgroundColor(getResources().getColor(R.color.blue));
            imageView.setImageBitmap(myFilter.processFilter(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false)));
        } else {
            LimeFx.setBackgroundColor(Color.TRANSPARENT);
        }
        if(nit){
            Bitmap bitmap = imageView.getDrawingCache();
            Filter myFilter = SampleFilters.getNightWhisperFilter();
                /*Bitmap outputImage = myFilter.processFilter(bitmap);
                imageView.setImageBitmap(outputImage);
                imageView.invalidate();*/
            NightFx.setBackgroundColor(getResources().getColor(R.color.blue));
            imageView.setImageBitmap(myFilter.processFilter(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false)));
        } else {
            NightFx.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public static Bitmap applySaturationFilter(Bitmap source, int level) {
        // get image size
        int width = source.getWidth();
        int height = source.getHeight();
        int[] pixels = new int[width * height];
        float[] HSV = new float[3];
        // get pixel array from source
        source.getPixels(pixels, 0, width, 0, 0, width, height);

        int index = 0;
        // iteration through pixels
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                // get current index in 2D-matrix
                index = y * width + x;
                // convert to HSV
                Color.colorToHSV(pixels[index], HSV);
                // increase Saturation level
                HSV[1] *= level;
                HSV[1] = (float) Math.max(0.0, Math.min(HSV[1], 1.0));
                // take color back
                pixels[index] |= Color.HSVToColor(HSV);
            }
        }
        // output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }

    public static Bitmap sharpen(Bitmap src, double weight) {
        double[][] SharpConfig = new double[][]{
                {0, -2, 0},
                {-2, weight, -2},
                {0, -2, 0}
        };
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.applyConfig(SharpConfig);
        convMatrix.Factor = weight - 8;
        return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
    }

    public Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) {

        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;
        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];
        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;
                sir = stack[i + radius];
                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];
                rbs = r1 - Math.abs(i);
                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


    public static ColorMatrixColorFilter brightIt(int fb) {
        ColorMatrix cmB = new ColorMatrix();
        cmB.set(new float[]{
                1, 0, 0, 0, fb,
                0, 1, 0, 0, fb,
                0, 0, 1, 0, fb,
                0, 0, 0, 1, 0});

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(cmB);
//Canvas c = new Canvas(b2);
//Paint paint = new Paint();
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(colorMatrix);
//paint.setColorFilter(f);
        return f;
    }

    public void editDialog(final String position) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.text_editor_dialog);

        final EditText edt_unit = (EditText) dialog.findViewById(R.id.input_unit);
        TextView title_dialog = (TextView) dialog.findViewById(R.id.text_dialog);
        if (position == "headline") {
            title_dialog.setText("Change your headline here");
            if (txtHeadline.getText().equals("Your Headline")) {
                edt_unit.setHint("Your headline here");
            } else {
                edt_unit.setText(txtHeadline.getText());
                edt_unit.append("");
            }

        } else if (position == "sub headline") {
            title_dialog.setText("Change your sub headline here");
            if (txtSubHeadline.getText().equals("Your Sub Headline")) {
                edt_unit.setHint("Your sub headline here");
            } else {
                edt_unit.setText(txtSubHeadline.getText());
                edt_unit.append("");
            }


        } else if (position == "front line") {
            title_dialog.setText("Change your front line here");
            if (txtFrontLine.getText().equals("Your Front Line")) {
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
                if (edt_unit.getText().toString().equals("")) {
                    dialog.dismiss();
                } else {
                    if (position == "headline") {
                        txtHeadline.setText(edt_unit.getText().toString());
                    } else if (position == "sub headline") {
                        txtSubHeadline.setText(edt_unit.getText().toString());
                    } else if (position == "front line") {
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

    private void writeTextOnDrawable(Uri imageUri, String text, float x, float y) {

     /*   Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);*/

        Bitmap bm_take = null;
        try {
            bm_take = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bm = bm_take.copy(Bitmap.Config.ARGB_8888, true);
        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);
        float fontSize = txtHeadline.getTextSize();
        fontSize += fontSize * 0.2f;
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
        if (textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(this, 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (int) (((x) / 2) - 2);     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) (((y) / 2) - ((paint.descent() + paint.ascent()) / 2));
        Log.d("Canvas width",
                canvas.getWidth() + "");// e.getMessage());
        Log.d("Canvas Height",
                canvas.getHeight() + "");// e.getMessage());
        Log.d("Float X",
                x + "");// e.getMessage());
        Log.d("Float Y",
                y + "");// e.getMessage());
        canvas.drawText(text, xPos, yPos, paint);

        storeImage(bm);
        //return new BitmapDrawable(getResources(), bm);
    }


    public static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f);

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
            ImageEditor.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(ImageEditor.this, "Image Saved Successfully", Toast.LENGTH_LONG).show();
                }
            });
            refreshGallery(pictureFile);
            Intent i = new Intent(getBaseContext(),ShareActivity.class);
            i.putExtra("uri",pictureFile+"");
            startActivity(i);
            if(prog_dialog.isShowing()){
                prog_dialog.dismiss();
            }
            finish();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }


    }


    private void refreshGallery(File pictureFile) {
        File imageFile = pictureFile;
        MediaScannerConnection.scanFile(this, new String[]{imageFile.getPath()}, new String[]{"image/*"}, null);
    }

    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Content Creator");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
}


