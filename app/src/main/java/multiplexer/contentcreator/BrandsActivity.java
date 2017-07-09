package multiplexer.contentcreator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.File;
import java.util.ArrayList;

import multiplexer.contentcreator.Database.DatabaseHelper;
import multiplexer.contentcreator.Model.Brands;
import multiplexer.contentcreator.adapter.ImageAdapter;
import multiplexer.contentcreator.adapter.ScreenSlidePageFragment;
import multiplexer.contentcreator.utils.ColorDialog;
import multiplexer.contentcreator.utils.Utils;

/**
 * Created by Miral on 3/20/2017.
 */

public class BrandsActivity extends AppCompatActivity implements
        ColorDialog.OnColorChangedListener {
    int selectedColor;
    int darkenedColor;
    final int CATEGORY_ID = 0;
    Dialog dialog;
    private Animation animBounce;
    private Context mContext;
    private int mPickedColor = Color.WHITE;
    LinearLayout mainL;
    TextView productDetails, competition, alternate;
    Spinner spinnerAgeGroup, spinnerGender;
    TextView animalChooser;
    EditText edtTagline, edtObject, edtCelebrity, edtProductName, edtProductDesc, edtProductJob, edtProduct, edtProductBetter, edtCompetitorName, edtCompetitorDesc, edtCompetitorLinks, edtAlternateName, edtAlternateDesc, edtAlternateLinks;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    Uri selectedImage = null;
    DatabaseHelper db;
    ImageView logoImage;
    TextView color;
    Button btnUpload;
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brands);
        db = new DatabaseHelper(BrandsActivity.this);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        selectedColor = fetchAccentColor();
        darkenedColor = Utils.getDarkColor(selectedColor);
        Spinner imageSpinner = (Spinner) findViewById(R.id.spinnerEmotion);
        ImageAdapter adapter = new ImageAdapter(BrandsActivity.this,
                new Integer[]{R.drawable.happy, R.drawable.unhappy, R.drawable.super_happy, R.drawable.angry});
        imageSpinner.setAdapter(adapter);
        animalChooser = (TextView) findViewById(R.id.textViewAnimal);
        /*spinnerAgeGroup = (Spinner) findViewById(R.id.spinnerAgeGroup);
        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        */
        productDetails = (TextView) findViewById(R.id.productDetails);
        competition = (TextView) findViewById(R.id.competition);
        alternate = (TextView) findViewById(R.id.alternate);
        productDetails.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapsibledown, 0, 0, 0);
        competition.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapsibledown, 0, 0, 0);
        alternate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapsibledown, 0, 0, 0);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        edtTagline = (EditText) findViewById(R.id.eTTagline);
        edtObject = (EditText) findViewById(R.id.eTObject);
        edtCelebrity = (EditText) findViewById(R.id.eTCelebrityPersonality);
        edtProductName = (EditText) findViewById(R.id.eTproductName);
        edtProductDesc = (EditText) findViewById(R.id.eTproductDesc);
        edtProductJob = (EditText) findViewById(R.id.eTJob);
        edtProduct = (EditText) findViewById(R.id.eTproduct);
        edtProductBetter = (EditText) findViewById(R.id.etBetter);
        edtCompetitorName = (EditText) findViewById(R.id.etCompetitorName);
        edtCompetitorDesc = (EditText) findViewById(R.id.etCompetitorDesc);
        edtCompetitorLinks = (EditText) findViewById(R.id.etCompetitorLinks);
        edtAlternateName = (EditText) findViewById(R.id.etAlternateName);
        edtAlternateDesc = (EditText) findViewById(R.id.etAlternateDesc);
        edtAlternateLinks = (EditText) findViewById(R.id.etAlternateLinks);
        logoImage = (ImageView) findViewById(R.id.imageViewLogo);
        if(db.getBrandsCount()>0){
            getSupportActionBar().setTitle("Your Brand");
            btnUpload.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            ArrayList<Brands> arrBrands = db.getAllBrandsData();
            edtTagline.setText(arrBrands.get(arrBrands.size()-1).getBrand_tagline());
            selectedImage = arrBrands.get(arrBrands.size()-1).getPicUri();
           /* //Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                logoImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
*/
            File image = new File(getRealPathFromURI(selectedImage));
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
            logoImage.setImageBitmap(bitmap);

            edtObject.setText(arrBrands.get(arrBrands.size()-1).getPersonality_object());
            edtCelebrity.setText(arrBrands.get(arrBrands.size()-1).getCelebrity_personality());
            edtProductName.setText(arrBrands.get(arrBrands.size()-1).getProduct_name());
            edtProductDesc.setText(arrBrands.get(arrBrands.size()-1).getProduct_desc());
            edtProductJob.setText(arrBrands.get(arrBrands.size()-1).getProduct_job());
            edtProduct.setText(arrBrands.get(arrBrands.size()-1).getProduct());
            edtProductBetter.setText(arrBrands.get(arrBrands.size()-1).getProduct_compare());
            edtCompetitorName.setText(arrBrands.get(arrBrands.size()-1).getCompetitor_name());
            edtCompetitorDesc.setText(arrBrands.get(arrBrands.size()-1).getCompetitor_desc());
            edtCompetitorLinks.setText(arrBrands.get(arrBrands.size()-1).getCompetitor_links());
            edtAlternateName.setText(arrBrands.get(arrBrands.size()-1).getAlternative_name());
            edtAlternateDesc.setText(arrBrands.get(arrBrands.size()-1).getAlternative_desc());
            edtAlternateLinks.setText(arrBrands.get(arrBrands.size()-1).getAlter_links());


        }
        mainL = (LinearLayout) findViewById(R.id.mainL);
        mContext = getApplicationContext();
         color = (TextView) findViewById(R.id.eTColor);
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new ColorDialog(BrandsActivity.this, BrandsActivity.this, R.color.colorPrimary).show();
               /* GridView gv = (GridView) ColorPicker.getColorPicker(BrandsActivity.this);

                // Initialize a new AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(BrandsActivity.this);

                // Set the alert dialog content to GridView (color picker)
                builder.setView(gv);

                // Initialize a new AlertDialog object
                final AlertDialog dialog = builder.create();

                // Show the color picker window
                dialog.show();

                // Set the color picker dialog size
                dialog.getWindow().setLayout(
                        getScreenSize().x - mainL.getPaddingLeft() - mainL.getPaddingRight(),
                        getScreenSize().y - getStatusBarHeight() - mainL.getPaddingTop() - mainL.getPaddingBottom());

                // Set an item click listener for GridView widget
                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get the pickedColor from AdapterView
                        mPickedColor = (int) parent.getItemAtPosition(position);

                        // Set the layout background color as picked color
                        color.setBackgroundColor(mPickedColor);
                        color.setTextColor(Color.WHITE);

                        // close the color picker
                        dialog.dismiss();
                    }
                });*/
                setCustomTheme();
            }
        });
        final TextView tVPersonality = (TextView) findViewById(R.id.personality);
        tVPersonality.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapsibledown, 0, 0, 0);

        tVPersonality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout collapsibleLinearL = (LinearLayout) findViewById(R.id.collapsibleLinearLayout);
                if (collapsibleLinearL.getVisibility() == View.GONE) {
                    tVPersonality.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapsibleright, 0, 0, 0);
                    collapsibleLinearL.setVisibility(View.VISIBLE);
                    setAnimation();
                    collapsibleLinearL.setAnimation(animBounce);
                } else {
                    tVPersonality.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapsibledown, 0, 0, 0);
                    collapsibleLinearL.setVisibility(View.GONE);
                    collapsibleLinearL.setAnimation(null);
                }
            }
        });
        final TextView tVProducts = (TextView) findViewById(R.id.products);
        tVProducts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapsibledown, 0, 0, 0);
        tVProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout collapsibleLinearL = (LinearLayout) findViewById(R.id.collapsibleLinearLayout2);
                if (collapsibleLinearL.getVisibility() == View.GONE) {
                    tVProducts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapsibleright, 0, 0, 0);
                    collapsibleLinearL.setVisibility(View.VISIBLE);
                    setAnimation();
                    collapsibleLinearL.setAnimation(animBounce);
                } else {
                    tVProducts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapsibledown, 0, 0, 0);
                    collapsibleLinearL.setVisibility(View.GONE);
                    collapsibleLinearL.setAnimation(null);
                }
            }
        });

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });
        if (mPager.getCurrentItem() == 0) {
            findViewById(R.id.previous).setVisibility(View.INVISIBLE);
        }

        findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });
        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                findViewById(R.id.previous).setVisibility(View.VISIBLE);
            }
        });


        Button btnSave = (Button) findViewById(R.id.save_btn);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String brand_tagline = "", audience_age_group, audience_gender, audience_situation, audience_purpose, audience_outcome, personality_object=null, celebrity_personality=null, product_name=null, product_desc=null, product_job=null, product=null, product_compare=null, competitor_name=null, competitor_desc=null,
                        competitor_links =null , alternative_name =null, alternative_desc=null, alter_links=null, picUri;

                audience_age_group = "-";
                audience_gender = "-";
                audience_situation = "-";
                audience_purpose = "-";
                audience_outcome = "-";
                if (edtTagline.getText().toString().trim().equals("")) {
                  edtTagline.setError("Please set a tagline first");
                }       else if (edtObject.getText().equals("")) {
                    edtObject.setError("Please fill up the field");
                }
                else if (edtCelebrity.getText().equals("")) {
                    edtCelebrity.setError("Please fill up the field");
                }
                else  if (edtProductName.getText().equals("")) {

                }
                else  if (edtProductDesc.getText().equals("")) {
                    edtProductDesc.setError("Please fill up the field");
                }
                else   if (edtProductJob.getText().equals("")) {
                    edtProductJob.setError("Please fill up the field");
                }
                else   if (edtProduct.getText().equals("")) {
                    edtProduct.setError("Please fill up the field");
                }
                else  if (edtProductBetter.getText().equals("")) {
                    edtProductBetter.setError("Please fill up the field");
                }
                else  if (edtCompetitorName.getText().equals("")) {
                    edtCompetitorName.setError("Please fill up the field");
                }
                else  if (edtCompetitorDesc.getText().equals("")) {
                    edtCompetitorDesc.setError("Please fill up the field");
                }
                else if (edtCompetitorLinks.getText().equals("")) {
                    edtCompetitorLinks.setError("Please fill up the field");
                }

                else  if (edtAlternateName.getText().equals("")) {
                    edtAlternateName.setError("Please fill up the field");
                }
                else if (edtAlternateDesc.getText().equals("")) {
                    edtAlternateDesc.setError("Please fill up the field");
                }
                else if (edtAlternateLinks.getText().equals("")) {
                    edtAlternateLinks.setError("Please fill up the field");
                }
                else  if (selectedImage == null) {
                  Toast.makeText(getBaseContext(),"You haven't select any logo yet",Toast.LENGTH_LONG).show();
                } else if(!pref.contains("audGender")){
                        Toast.makeText(getBaseContext(),"Please set and save your Audience",Toast.LENGTH_LONG).show();
                }  else {
                    brand_tagline = edtTagline.getText().toString();
                    personality_object = edtObject.getText().toString();
                    celebrity_personality = edtCelebrity.getText().toString();
                    product_name = edtProductName.getText().toString();
                    product_desc = edtProductDesc.getText().toString();
                    product_job = edtProductJob.getText().toString();
                    product = edtProduct.getText().toString();
                    product_compare = edtProductBetter.getText().toString();
                    competitor_name = edtCompetitorName.getText().toString();
                    competitor_desc = edtCompetitorDesc.getText().toString();
                    competitor_links = edtCompetitorLinks.getText().toString();
                    alternative_name = edtAlternateName.getText().toString();
                    alternative_desc = edtAlternateDesc.getText().toString();
                    alter_links = edtAlternateLinks.getText().toString();
                    picUri = getRealPathFromURI(selectedImage);
                    Brands b = new Brands(brand_tagline, audience_age_group, audience_gender, audience_situation, audience_purpose, audience_outcome, personality_object, celebrity_personality
                            , product_name, product_desc, product_job, product, product_compare, competitor_name, competitor_desc,
                            competitor_links, alternative_name, alternative_desc, alter_links, Uri.parse(picUri));
                    db.insertBrand(b);
                    if(getIntent().hasExtra("flag") && getIntent().getStringExtra("flag").equals("must brand entry")){
                        Toast.makeText(getBaseContext(),"Now create your campaign",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getBaseContext(),CampaignActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        finish();
                    }

                }



            }
        });

    }

    private int fetchAccentColor() {
        TypedValue typedValue = new TypedValue();
        TypedArray a = obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    private Point getScreenSize() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        //Display dimensions in pixels
        display.getSize(size);
        return size;
    }

    // Custom method to get status bar height in pixels
    public int getStatusBarHeight() {
        int height = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    private void setCustomTheme() {
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
        color.setBackgroundColor(selectedColor);
        //Toast.makeText(this,"New color selected" + Integer.toHexString(selectedColor),Toast.LENGTH_LONG).show();
    }


    @Override
    public void colorChanged(int color) {
        BrandsActivity.this.findViewById(android.R.id.content)
                .setBackgroundColor(color);
    }

    private void setAnimation() {
        animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        animBounce.setDuration(1250);
    }

    public void productDetailsOnClick(View view) {
        LinearLayout collapsibleLinearL = (LinearLayout) findViewById(R.id.collapsibleProduct);
        if (collapsibleLinearL.getVisibility() == View.GONE) {
            productDetails.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapsibleright, 0, 0, 0);
            collapsibleLinearL.setVisibility(View.VISIBLE);
            setAnimation();
            collapsibleLinearL.setAnimation(animBounce);
        } else {
            productDetails.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapsibledown, 0, 0, 0);
            collapsibleLinearL.setVisibility(View.GONE);
            collapsibleLinearL.setAnimation(null);
        }
    }

    public void competionOnClick(View view) {
        LinearLayout collapsibleLinearL = (LinearLayout) findViewById(R.id.collapsibleCompetition);

        if (collapsibleLinearL.getVisibility() == View.GONE) {
            competition.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapsibleright, 0, 0, 0);
            collapsibleLinearL.setVisibility(View.VISIBLE);
            setAnimation();
            collapsibleLinearL.setAnimation(animBounce);
        } else {
            competition.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapsibledown, 0, 0, 0);
            collapsibleLinearL.setVisibility(View.GONE);
            collapsibleLinearL.setAnimation(null);
        }
    }

    public void alternateClick(View view) {
        LinearLayout collapsibleLinearL = (LinearLayout) findViewById(R.id.collapsibleAlternate);
        if (collapsibleLinearL.getVisibility() == View.GONE) {
            collapsibleLinearL.setVisibility(View.VISIBLE);
            alternate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapsibleright, 0, 0, 0);
            setAnimation();
            collapsibleLinearL.setAnimation(animBounce);
        } else {
            collapsibleLinearL.setVisibility(View.GONE);
            collapsibleLinearL.setAnimation(null);
            alternate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapsibledown, 0, 0, 0);
        }
    }

    public void uploadImage(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            //Bitmap bitmap = null;
            /*try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                logoImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/

            File image = new File(getRealPathFromURI(selectedImage));
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
            logoImage.setImageBitmap(bitmap);
        }
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
    public void animalChooser(View view) {
        showDialog(CATEGORY_ID);
    }

    protected Dialog onCreateDialog(int id) {

        switch (id) {

            case CATEGORY_ID:

                AlertDialog.Builder builder;
                Context mContext = this;
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.animal_dialog, (ViewGroup) findViewById(R.id.layout_root));
                GridView gridview = (GridView) layout.findViewById(R.id.gridview);
                gridview.setAdapter(new ImageAdapterAnimal(BrandsActivity.this));

                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        Toast.makeText(arg1.getContext(), "Position is " + arg2, Toast.LENGTH_LONG).show();
                    }
                });

                ImageView close = (ImageView) layout.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                builder = new AlertDialog.Builder(mContext);
                builder.setView(layout);
                dialog = builder.create();
                break;
            default:
                dialog = null;
        }
        return dialog;
    }

    public class ImageAdapterAnimal extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;

        public ImageAdapterAnimal(Context c) {
            mInflater = LayoutInflater.from(c);
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {  // if it's not recycled,
                convertView = mInflater.inflate(R.layout.custom_gridview_item, null);
                // convertView.setLayoutParams(new GridView.LayoutParams(1000,1000));
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.text);
                holder.icon = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.icon.setAdjustViewBounds(true);
            holder.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.icon.setPadding(5, 5, 5, 5);
            //holder.title.setText(categoryContent[position]);
            holder.icon.setImageResource(mThumbIds[position]);
            return convertView;
        }

        class ViewHolder {
            TextView title;
            ImageView icon;
        }

        // references to our images
        private Integer[] mThumbIds = {
                R.drawable.lion, R.drawable.lion, R.drawable.lion, R.drawable.lion, R.drawable.lion, R.drawable.lion,
                R.drawable.lion, R.drawable.lion, R.drawable.lion
        };

    }

    private String[] categoryContent = {
            "Pubs", "Restuarants", "shopping",
            "theatre", "train", "taxi",
            "gas", "police", "hospital"
    };

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_screen_slide, menu);

        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);

        // Add either a "next" or "finish" button to the action bar, depending on which page
        // is currently selected.
        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
                (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
                        ? R.string.action_finish
                        : R.string.action_next);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;

            case R.id.action_previous:
                // Go to the previous step in the wizard. If there is no previous step,
                // setCurrentItem will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                return true;

            case R.id.action_next:
                // Advance to the next step in the wizard. If there is no next step, setCurrentItem
                // will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    /**
     * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
