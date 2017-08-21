package multiplexer.contentcreator.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import multiplexer.contentcreator.CampaignActivity;
import multiplexer.contentcreator.CreateContent;
import multiplexer.contentcreator.Database.DatabaseHelper;
import multiplexer.contentcreator.MainListActivity;
import multiplexer.contentcreator.Model.Campaign;
import multiplexer.contentcreator.R;

public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.MyViewHolder> {

    private List<Campaign> campaignList;
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public RelativeLayout parentLayout;
        ImageButton btnDelete;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            parentLayout = (RelativeLayout) view.findViewById(R.id.parentLayout);
            btnDelete = (ImageButton) view.findViewById(R.id.btnDelete);

        }
    }


    public CampaignAdapter(List<Campaign> campaignList) {
        this.campaignList = campaignList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.campaign_list_row, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Campaign campaign = campaignList.get(position);
        holder.title.setText(campaign.getHeadline());
        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(context, CampaignActivity.class);
                i.putExtra("camp_id",campaign.getCamp_id());
                i.putExtra("headline",campaign.getHeadline());
                i.putExtra("subHeadline",campaign.getSub_headline());
                i.putExtra("frontLine",campaign.getFront_lines());
                i.putExtra("callToActions",campaign.getCall_to_action());
                i.putExtra("outcome",campaign.getOutcome());
                i.putExtra("audience",campaign.getAudience());
                i.putExtra("flagPosition",position+1);
                context.startActivity(i);
                return true;
            }
        });
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMain = new Intent(context, CreateContent.class);
                intentMain.putExtra("tag", "campaign");
                intentMain.putExtra("headline",campaign.getHeadline());
                intentMain.putExtra("subHeadline",campaign.getSub_headline());
                intentMain.putExtra("frontLine",campaign.getFront_lines());
                intentMain.putExtra("callToActions",campaign.getCall_to_action());
                context.startActivity(intentMain);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDelete(context,campaign.getCamp_id());


            }
        });

    }



    public void alertDelete(final Context context, final int camp_id){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Campaign").setCancelable(false);
        dialog.setMessage("Are you sure you want to delete the campaign");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper db = new DatabaseHelper(context);
               db.deleteCampaign(camp_id);
                context.startActivity(new Intent(context, MainListActivity.class));
                ((Activity)context).finishAffinity();
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();

    }



    @Override
    public int getItemCount() {
        return campaignList.size();
    }
}