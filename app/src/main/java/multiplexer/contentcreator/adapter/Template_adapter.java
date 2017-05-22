package multiplexer.contentcreator.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    // data is passed into the constructor
    public Template_adapter(Context context, ArrayList<Template> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        mContext = context;
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
            Picasso.with(mContext).load(pref.getString("picUri","")).into(holder.backgroundImage);
        } else {
            if(position == 0){
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
            }
        }

        if(position == 0){
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
        }
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
                    Toast.makeText(mContext,"Choose a picture from above",Toast.LENGTH_LONG).show();
                }

            }
        });

      /*  holder.headLine.setText(temp.getHeadline());
        holder.subHeadline.setText(temp.getSubHeadline());
        holder.frontLine.setText(temp.getFrontLine());*/


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