package multiplexer.contentcreator.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
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

import multiplexer.contentcreator.Helper.JsonConstants;
import multiplexer.contentcreator.ImageEditor;
import multiplexer.contentcreator.Model.Template;
import multiplexer.contentcreator.R;
import multiplexer.contentcreator.views.AutoImageView;

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
        if(temp.getUser_img_uri()!=null){
            final float height;
            if (isPortraitImage(Uri.parse(temp.getUser_img_uri()))){
                height = Float.valueOf(mContext.getResources().getDimension(R.dimen.image_height_portrait));
            } else {
                height = Float.valueOf(mContext.getResources().getDimension(R.dimen.image_height_landscape));
            }
            Picasso.with(mContext)
                    .load(temp.getUser_img_uri())
                    .placeholder(R.drawable.image_processing)
                    .error(R.drawable.no_image)
                    .resize(screenWidth , (int)height)
                    .onlyScaleDown()
                    .centerInside()
                    .into(holder.userImage);
            Picasso.with(mContext)
                    .load(temp.getImg_url())
                    .placeholder(R.drawable.image_processing)
                    .error(R.drawable.no_image)
                    .resize(screenWidth , (int)mContext.getResources().getDimension(R.dimen.image_height_portrait))
                    .onlyScaleDown()
                    .centerInside()
                    .into(holder.backgroundImage);

        } else {
            Picasso.with(mContext)
                    .load(temp.getImg_url())
                    .placeholder(R.drawable.image_processing)
                    .error(R.drawable.no_image)
                    .resize(screenWidth , (int)mContext.getResources().getDimension(R.dimen.image_height_portrait))
                    .onlyScaleDown()
                    .centerInside()
                    .into(holder.backgroundImage);
            //Picasso.with(mContext).load(temp.getImg_url()).into(holder.backgroundImage);
        }
        PercentRelativeLayout.LayoutParams headlineParams = (PercentRelativeLayout.LayoutParams) holder.headLine.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo headlinePercentLayoutInfo = headlineParams.getPercentLayoutInfo();
        headlinePercentLayoutInfo.leftMarginPercent = ((Integer.parseInt(temp.getHeadline_left_position())-10) * 0.01f); //15%
        headlinePercentLayoutInfo.topMarginPercent = ((Integer.parseInt(temp.getHeadline_top_position())-10) * 0.01f); //15%
        holder.headLine.setText(temp.getHeadline());
        holder.headLine.setTextColor(Color.parseColor(temp.getHeadline_text_color()));
        holder.headLine.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(temp.getHeadline_font_size())*2);
        holder.headLine.setLayoutParams(headlineParams);
        PercentRelativeLayout.LayoutParams subHeadlineParams = (PercentRelativeLayout.LayoutParams) holder.subHeadline.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo subHeadlinePercentLayoutInfo = subHeadlineParams.getPercentLayoutInfo();
        subHeadlinePercentLayoutInfo.leftMarginPercent = ((Integer.parseInt(temp.getSub_headline_left_position())-10) * 0.01f); //35%
        subHeadlinePercentLayoutInfo.topMarginPercent = ((Integer.parseInt(temp.getSub_headline_top_position())-10) * 0.01f); //35%
        holder.subHeadline.setText(temp.getSubHeadline());
        holder.subHeadline.setTextColor(Color.parseColor(temp.getSub_headline_text_color()));
        holder.subHeadline.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(temp.getSub_headline_font_size())*2);
        holder.subHeadline.setLayoutParams(subHeadlineParams);
        PercentRelativeLayout.LayoutParams frontLineParams = (PercentRelativeLayout.LayoutParams) holder.frontLine.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo frontLinePercentLayoutInfo = frontLineParams.getPercentLayoutInfo();
        frontLinePercentLayoutInfo.leftMarginPercent =  ((Integer.parseInt(temp.getFrontLine_left_position())-10) * 0.01f);//45%
        frontLinePercentLayoutInfo.topMarginPercent =  ((Integer.parseInt(temp.getFronLine_top_position())-10) * 0.01f); //45%
        holder.frontLine.setText(temp.getFrontLine());
        holder.frontLine.setTextColor(Color.parseColor(temp.getFrontLine_text_color()));
        holder.frontLine.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(temp.getFrontLine_font_size())*2);
        holder.frontLine.setLayoutParams(frontLineParams);

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
        if(temp.getUser_img_uri()!=null){
             tempUri = temp.getUser_img_uri();
        } else {
             tempUri = null;
        }
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tempUri!=null){
                    Intent i = new Intent(mContext, ImageEditor.class);
                    i.putExtra("userUri",tempUri);
                    i.putExtra("uri",temp.getImg_url());
                    i.putExtra(new JsonConstants().TEMPLATE_HEADLINE,temp.getHeadline());
                    i.putExtra(new JsonConstants().TEMPLATE_HEADLINE_LEFT_POSITION,temp.getHeadline_left_position());
                    i.putExtra(new JsonConstants().TEMPLATE_HEADLINE_TOP_POSITION,temp.getHeadline_top_position());
                    i.putExtra(new JsonConstants().TEMPLATE_HEADLINE_TEXT_SIZE,temp.getHeadline_font_size());
                    i.putExtra(new JsonConstants().TEMPLATE_HEADLINE_TEXT_COLOR,temp.getHeadline_text_color());
                    i.putExtra(new JsonConstants().TEMPLATE_SUBHEADLINE,temp.getSubHeadline());
                    i.putExtra(new JsonConstants().TEMPLATE_SUBHEADLINE_LEFT_POSITION,temp.getSub_headline_left_position());
                    i.putExtra(new JsonConstants().TEMPLATE_SUBHEADLINE_TOP_POSITION,temp.getSub_headline_top_position());
                    i.putExtra(new JsonConstants().TEMPLATE_SUBHEADLINE_TEXT_SIZE,temp.getSub_headline_font_size());
                    i.putExtra(new JsonConstants().TEMPLATE_SUBHEADLINE_TEXT_COLOR,temp.getSub_headline_text_color());
                    i.putExtra(new JsonConstants().TEMPLATE_FRONTLINE,temp.getFrontLine());
                    i.putExtra(new JsonConstants().TEMPLATE_FRONTLINE_LEFT_POSITION,temp.getFrontLine_left_position());
                    i.putExtra(new JsonConstants().TEMPLATE_FRONTLINE_TOP_POSITION,temp.getFronLine_top_position());
                    i.putExtra(new JsonConstants().TEMPLATE_FRONTLINE_TEXT_SIZE,temp.getFrontLine_font_size());
                    i.putExtra(new JsonConstants().TEMPLATE_FRONTLINE_TEXT_COLOR,temp.getFrontLine_text_color());

                    mContext.startActivity(i);
                } else {
                    Intent i = new Intent(mContext, ImageEditor.class);
                    i.putExtra("uri",temp.getImg_url());
                    i.putExtra(new JsonConstants().TEMPLATE_HEADLINE,temp.getHeadline());
                    i.putExtra(new JsonConstants().TEMPLATE_HEADLINE_LEFT_POSITION,temp.getHeadline_left_position());
                    i.putExtra(new JsonConstants().TEMPLATE_HEADLINE_TOP_POSITION,temp.getHeadline_top_position());
                    i.putExtra(new JsonConstants().TEMPLATE_HEADLINE_TEXT_SIZE,temp.getHeadline_font_size());
                    i.putExtra(new JsonConstants().TEMPLATE_HEADLINE_TEXT_COLOR,temp.getHeadline_text_color());
                    i.putExtra(new JsonConstants().TEMPLATE_SUBHEADLINE,temp.getSubHeadline());
                    i.putExtra(new JsonConstants().TEMPLATE_SUBHEADLINE_LEFT_POSITION,temp.getSub_headline_left_position());
                    i.putExtra(new JsonConstants().TEMPLATE_SUBHEADLINE_TOP_POSITION,temp.getSub_headline_top_position());
                    i.putExtra(new JsonConstants().TEMPLATE_SUBHEADLINE_TEXT_SIZE,temp.getSub_headline_font_size());
                    i.putExtra(new JsonConstants().TEMPLATE_SUBHEADLINE_TEXT_COLOR,temp.getSub_headline_text_color());
                    i.putExtra(new JsonConstants().TEMPLATE_FRONTLINE,temp.getFrontLine());
                    i.putExtra(new JsonConstants().TEMPLATE_FRONTLINE_LEFT_POSITION,temp.getFrontLine_left_position());
                    i.putExtra(new JsonConstants().TEMPLATE_FRONTLINE_TOP_POSITION,temp.getFronLine_top_position());
                    i.putExtra(new JsonConstants().TEMPLATE_FRONTLINE_TEXT_SIZE,temp.getFrontLine_font_size());
                    i.putExtra(new JsonConstants().TEMPLATE_FRONTLINE_TEXT_COLOR,temp.getFrontLine_text_color());
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
        AutoImageView userImage;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            headLine = (TextView) itemView.findViewById(R.id.headline);
            subHeadline = (TextView) itemView.findViewById(R.id.subHeadline);
            frontLine = (TextView) itemView.findViewById(R.id.frontLine);
            backgroundImage = (ImageView) itemView.findViewById(R.id.backgroundImg);
            userImage = (AutoImageView) itemView.findViewById(R.id.userImg);
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