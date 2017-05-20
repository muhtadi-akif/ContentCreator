package multiplexer.contentcreator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CampaignListActivity extends AppCompatActivity {

    ListView listViewCampaign;
    String[] arr= {"Campaign 1", "Campaign 2", "Campaign 3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_list);

        listViewCampaign = (ListView) findViewById(R.id.listViewCampaign);

        listViewCampaign.setAdapter(new ArrayAdapter<String>(CampaignListActivity.this, android.R.layout.simple_list_item_1, arr));
        listViewCampaign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CampaignListActivity.this, "Campaign details will be shown!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
