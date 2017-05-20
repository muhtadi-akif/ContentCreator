package multiplexer.contentcreator;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import multiplexer.contentcreator.Database.DatabaseHelper;
import multiplexer.contentcreator.Model.Campaign;
import multiplexer.contentcreator.adapter.CampaignAdapter;

public class MainListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CampaignAdapter mAdapter;
    private List<Campaign> campaignList = new ArrayList<>();
    DatabaseHelper db;
    TextView error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(MainListActivity.this);

        setContentView(R.layout.app_bar_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Sketch.ttf");
        TextView app_title = (TextView) findViewById(R.id.app_title);
        app_title.setTypeface(font);
        error = (TextView) findViewById(R.id.noData);
        recyclerView = (RecyclerView) findViewById(R.id.gallary_images);
        setData();
        mAdapter = new CampaignAdapter (campaignList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.getBrandsCount()<=0){
                    Toast.makeText(getBaseContext(),"You've to set your brands first then create your campaign",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getBaseContext(),BrandsActivity.class);
                    i.putExtra("flag","must brand entry");
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(getBaseContext(),CampaignActivity.class);
                    startActivity(i);
                }

            }
        });

    }


    private void setData(){
       /* Campaign campaign = new Campaign("Campaign 1");
        campaignList.add(campaign);

        campaign = new Campaign("Campaign 2");
        campaignList.add(campaign);


        campaign = new Campaign("Campaign 3");
        campaignList.add(campaign);*/
        campaignList = db.getAllCampaingData();
        if(campaignList.size()<=0){
            error.setVisibility(View.VISIBLE);
        }

        //mAdapter.notifyDataSetChanged();
    }


}
