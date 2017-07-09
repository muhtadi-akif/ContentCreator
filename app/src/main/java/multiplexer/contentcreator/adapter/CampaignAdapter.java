package multiplexer.contentcreator.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import multiplexer.contentcreator.CampaignActivity;
import multiplexer.contentcreator.CreateContent;
import multiplexer.contentcreator.Model.Campaign;
import multiplexer.contentcreator.R;

public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.MyViewHolder> {

    private List<Campaign> campaignList;
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public RelativeLayout parentLayout;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            parentLayout = (RelativeLayout) view.findViewById(R.id.parentLayout);
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Campaign campaign = campaignList.get(position);
        holder.title.setText(campaign.getHeadline());
        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(context, CampaignActivity.class);
                i.putExtra("headline",campaign.getHeadline());
                i.putExtra("subHeadline",campaign.getSub_headline());
                i.putExtra("frontLine",campaign.getFront_lines());
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
                context.startActivity(intentMain);
            }
        });
    }

    @Override
    public int getItemCount() {
        return campaignList.size();
    }
}