package multiplexer.contentcreator.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import multiplexer.contentcreator.ImageEditor;
import multiplexer.contentcreator.Model.Template;
import multiplexer.contentcreator.R;

import static android.content.Context.MODE_PRIVATE;

public class Template_adapter extends RecyclerView.Adapter<Template_adapter.ViewHolder> {

    ArrayList<Template> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context mContext;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    String tempUri;
    private int screenWidth;
    // data is passed into the constructor
    public Template_adapter(Context context, ArrayList<Template> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        mContext = context;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        pref = mContext.getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.templates, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Template temp = mData.get(position);
        if(pref.contains("picUri")){
            final float height;
            if (isPortraitImage(Uri.parse(pref.getString("picUri","")))){
                height = Float.valueOf(mContext.getResources().getDimension(R.dimen.image_height_portrait));
            } else {
                height = Float.valueOf(mContext.getResources().getDimension(R.dimen.image_height_landscape));
            }
            Picasso.with(mContext)
                    .load(pref.getString("picUri",""))
                    .placeholder(R.drawable.image_processing)
                    .error(R.drawable.no_image)
                    .resize(screenWidth / 3, (int)height)
                    .onlyScaleDown()
                    .centerInside()
                    .into(holder.backgroundImage);

        } else {
            /*if(position == 0){
                Picasso.with(mContext).load(R.drawable.wood).into(holder.backgroundImage);
            } else if (position ==1){
                Picasso.with(mContext).load(R.drawable.rock).into(holder.backgroundImage);
            }else if (position ==2){
                Picasso.with(mContext).load(R.drawable.wood).into(holder.backgroundImage);
            }else if (position ==3){
                Picasso.with(mContext).load(R.drawable.rock).into(holder.backgroundImage);
            }else if (position ==4){
                Picasso.with(mContext).load(R.drawable.wood).into(holder.backgroundImage);
            }else if (position ==5){
                Picasso.with(mContext).load(R.drawable.wood).into(holder.backgroundImage);
            }*/
            final float height;
            if (isPortraitImage(Uri.parse(temp.getImg_url()))){
                height = Float.valueOf(mContext.getResources().getDimension(R.dimen.image_height_portrait));
            } else {
                height = Float.valueOf(mContext.getResources().getDimension(R.dimen.image_height_landscape));
            }
            Picasso.with(mContext)
                    .load(temp.getImg_url())
                    .placeholder(R.drawable.image_processing)
                    .error(R.drawable.no_image)
                    .resize(screenWidth / 3, (int)height)
                    .onlyScaleDown()
                    .centerInside()
                    .into(holder.backgroundImage);
            //Picasso.with(mContext).load(temp.getImg_url()).into(holder.backgroundImage);
        }
        holder.headLine.setText(temp.getHeadline());
        holder.subHeadline.setText(temp.getSubHeadline());
        holder.frontLine.setText(temp.getFrontLine());
       /* if(position == 0){
            RelativeLayout.LayoutParams headlineParams = (RelativeLayout.LayoutParams) holder.headLine.getLayoutParams();
            RelativeLayout.LayoutParams subHeadlineParams = (RelativeLayout.LayoutParams) holder.subHeadline.getLayoutParams();
            RelativeLayout.LayoutParams frontLineParams = (RelativeLayout.LayoutParams) holder.frontLine.getLayoutParams();
            headlineParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            subHeadlineParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            frontLineParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.headLine.setText(temp.getHeadline());
            holder.subHeadline.setText(temp.getSubHeadline());
            holder.frontLine.setText(temp.getFrontLine());

        } else if (position ==1){
            RelativeLayout.LayoutParams headlineParams = (RelativeLayout.LayoutParams) holder.headLine.getLayoutParams();
            RelativeLayout.LayoutParams subHeadlineParams = (RelativeLayout.LayoutParams) holder.subHeadline.getLayoutParams();
            RelativeLayout.LayoutParams frontLineParams = (RelativeLayout.LayoutParams) holder.frontLine.getLayoutParams();
            headlineParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            subHeadlineParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            frontLineParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.headLine.setText(temp.getHeadline());
            holder.subHeadline.setText(temp.getSubHeadline());
            holder.frontLine.setText(temp.getFrontLine());
        }else if (position ==2){
            RelativeLayout.LayoutParams headlineParams = (RelativeLayout.LayoutParams) holder.headLine.getLayoutParams();
            RelativeLayout.LayoutParams subHeadlineParams = (RelativeLayout.LayoutParams) holder.subHeadline.getLayoutParams();
            RelativeLayout.LayoutParams frontLineParams = (RelativeLayout.LayoutParams) holder.frontLine.getLayoutParams();
            headlineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            subHeadlineParams.addRule(RelativeLayout.ABOVE,R.id.headline);
            frontLineParams.addRule(RelativeLayout.ABOVE,R.id.subHeadline);
            holder.headLine.setText(temp.getHeadline());
            holder.subHeadline.setText(temp.getSubHeadline());
            holder.frontLine.setText(temp.getFrontLine());
        }else if (position ==3){
            RelativeLayout.LayoutParams headlineParams = (RelativeLayout.LayoutParams) holder.headLine.getLayoutParams();
            RelativeLayout.LayoutParams subHeadlineParams = (RelativeLayout.LayoutParams) holder.subHeadline.getLayoutParams();
            RelativeLayout.LayoutParams frontLineParams = (RelativeLayout.LayoutParams) holder.frontLine.getLayoutParams();
            headlineParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            subHeadlineParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            frontLineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            holder.headLine.setText(temp.getHeadline());
            holder.subHeadline.setText(temp.getSubHeadline());
            holder.frontLine.setText(temp.getFrontLine());
        }else if (position ==4){
            RelativeLayout.LayoutParams headlineParams = (RelativeLayout.LayoutParams) holder.headLine.getLayoutParams();
            RelativeLayout.LayoutParams subHeadlineParams = (RelativeLayout.LayoutParams) holder.subHeadline.getLayoutParams();
            RelativeLayout.LayoutParams frontLineParams = (RelativeLayout.LayoutParams) holder.frontLine.getLayoutParams();
            headlineParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            subHeadlineParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            frontLineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            holder.headLine.setText(temp.getHeadline());
            holder.subHeadline.setText(temp.getSubHeadline());
            holder.frontLine.setText(temp.getFrontLine());
        }else if (position ==5){
            RelativeLayout.LayoutParams headlineParams = (RelativeLayout.LayoutParams) holder.headLine.getLayoutParams();
            RelativeLayout.LayoutParams subHeadlineParams = (RelativeLayout.LayoutParams) holder.subHeadline.getLayoutParams();
            RelativeLayout.LayoutParams frontLineParams = (RelativeLayout.LayoutParams) holder.frontLine.getLayoutParams();
            headlineParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            subHeadlineParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            frontLineParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            frontLineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            holder.headLine.setText(temp.getHeadline());
            holder.subHeadline.setText(temp.getSubHeadline());
            holder.frontLine.setText(temp.getFrontLine());
        }*/
        if(pref.contains("picUri")){
             tempUri = pref.getString("picUri","");
        } else {
             tempUri = null;
        }
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tempUri!=null){
                    Intent i = new Intent(mContext, ImageEditor.class);
                    i.putExtra("uri",tempUri);
                    i.putExtra("headline",temp.getHeadline());
                    i.putExtra("subHeadline",temp.getSubHeadline());
                    i.putExtra("frontLine",temp.getFrontLine());
                    mContext.startActivity(i);
                } else {
                    Intent i = new Intent(mContext, ImageEditor.class);
                    i.putExtra("uri",temp.getImg_url());
                    i.putExtra("headline",temp.getHeadline());
                    i.putExtra("subHeadline",temp.getSubHeadline());
                    i.putExtra("frontLine",temp.getFrontLine());
                    mContext.startActivity(i);
                    //Toast.makeText(mContext,"Choose a picture from above",Toast.LENGTH_LONG).show();
                }

            }
        });

      /*  holder.headLine.setText(temp.getHeadline());
        holder.subHeadline.setText(temp.getSubHeadline());
        holder.frontLine.setText(temp.getFrontLine());*/


    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = mContext.getContentResolver().query(contentURI, null, null, null, null);
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

     private boolean isPortraitImage(Uri uri){
         boolean x = false;
         BitmapFactory.Options options = new BitmapFactory.Options();
         options.inJustDecodeBounds = true;
         BitmapFactory.decodeFile(getRealPathFromURI(uri), options);
         int imageHeight = options.outHeight;
         int imageWidth = options.outWidth;
         if (imageHeight>imageWidth){
             x = true;
         }
         Log.e("Is Potrait",x+"");
         return x;
     }
    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView headLine, subHeadline, frontLine;
        ImageView backgroundImage;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            headLine = (TextView) itemView.findViewById(R.id.headline);
            subHeadline = (TextView) itemView.findViewById(R.id.subHeadline);
            frontLine = (TextView) itemView.findViewById(R.id.frontLine);
            backgroundImage = (ImageView) itemView.findViewById(R.id.backgroundImg);
            parentLayout = (RelativeLayout) itemView.findViewById(R.id.parentLayout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Template getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}