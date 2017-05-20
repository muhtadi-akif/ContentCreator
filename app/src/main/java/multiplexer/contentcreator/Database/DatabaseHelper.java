package multiplexer.contentcreator.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

import multiplexer.contentcreator.Model.Brands;
import multiplexer.contentcreator.Model.Campaign;

/**
 * Created by USER on 11/10/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ContentCreatordb.db";

    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableAttributes tableAttr = new TableAttributes();
        String query = tableAttr.campaignTableCreateQuery();
        String brandsQuery = tableAttr.brandsTableCreateQuery();

        try {
            db.execSQL(query);
            Log.i("Campaign", "Hoise");
        } catch (SQLException e) {
            Log.e("Create Error", e.toString());
        }
        try {
            db.execSQL(brandsQuery);
            Log.i("Brands", "Hoise");
        } catch (SQLException e) {
            Log.e("Create Error", e.toString());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableAttributes.CAMPAIGN_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableAttributes.BRANDS_TABLE_NAME);

        onCreate(db);
    }

    public void insertCampaingData(Campaign campaign) {

        SQLiteDatabase dbInsert = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableAttributes.CAMPAIGN_AUDIENCE, campaign.getAudience());
        values.put(TableAttributes.CAMPAIGN_EXPECTED_OUTCOME, campaign.getOutcome());
        values.put(TableAttributes.CAMPAIGN_FRONT_LINES, campaign.getFront_lines());
        values.put(TableAttributes.CAMPAIGN_HEADLINE, campaign.getHeadline());
        values.put(TableAttributes.CAMPAIGN_STORY, campaign.getStory());
        values.put(TableAttributes.CAMPAIGN_SUB_HEADLINE, campaign.getSub_headline());
        try {
            dbInsert.insert(TableAttributes.CAMPAIGN_TABLE_NAME, null, values);
            Log.i("Data", values.toString());
        } catch (SQLException e) {
            Log.e("Insert Error", e.toString());
        }
    }

    public void updateCampaignData(Campaign campaign,String position){
        SQLiteDatabase dbInsert = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableAttributes.CAMPAIGN_AUDIENCE, campaign.getAudience());
        values.put(TableAttributes.CAMPAIGN_EXPECTED_OUTCOME, campaign.getOutcome());
        values.put(TableAttributes.CAMPAIGN_FRONT_LINES, campaign.getFront_lines());
        values.put(TableAttributes.CAMPAIGN_HEADLINE, campaign.getHeadline());
        values.put(TableAttributes.CAMPAIGN_STORY, campaign.getStory());
        values.put(TableAttributes.CAMPAIGN_SUB_HEADLINE, campaign.getSub_headline());
        dbInsert.update(TableAttributes.CAMPAIGN_TABLE_NAME, values, "campaign_id = ?",
                new String[] { position });
    }

    public void insertBrand(Brands brand) {
        SQLiteDatabase dbInsert = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableAttributes.BRANDS_AUDIENCE_AGE_GROUP, brand.getAudience_age_group());
        values.put(TableAttributes.BRANDS_AUDIENCE_GENDER, brand.getAudience_gender());
        values.put(TableAttributes.BRANDS_AUDIENCE_OUTCOME, brand.getAudience_outcome());
        values.put(TableAttributes.BRANDS_AUDIENCE_PURPOSE, brand.getAudience_purpose());
        values.put(TableAttributes.BRANDS_AUDIENCE_SITUATION, brand.getAudience_situation());
        values.put(TableAttributes.BRANDS_CELEBRITY_PESONALITY, brand.getCelebrity_personality());
        values.put(TableAttributes.BRANDS_PERSONALITY_OBJECT, brand.getPersonality_object());
        values.put(TableAttributes.BRANDS_PRODUCT, brand.getProduct());
        values.put(TableAttributes.BRANDS_PRODUCT_ALTERNATIVE_DESCRIPTION, brand.getAlternative_desc());
        values.put(TableAttributes.BRANDS_PRODUCT_ALTERNATIVE_LINKS, brand.getAlter_links());
        values.put(TableAttributes.BRANDS_PRODUCT_ALTERNATIVE_NAME, brand.getAlternative_name());
        values.put(TableAttributes.BRANDS_PRODUCT_COMPARE, brand.getProduct_compare());
        values.put(TableAttributes.BRANDS_PRODUCT_COMPETATOR_DESCRIPTION, brand.getCompetitor_desc());
        values.put(TableAttributes.BRANDS_PRODUCT_COMPETATOR_LINKS, brand.getCompetitor_links());
        values.put(TableAttributes.BRANDS_PRODUCT_COMPETITOR_NAME, brand.getCompetitor_name());
        values.put(TableAttributes.BRANDS_PRODUCT_DESCRIPTION, brand.getProduct_desc());
        values.put(TableAttributes.BRANDS_PRODUCT_JOB, brand.getProduct_job());
        values.put(TableAttributes.BRANDS_PRODUCT_NAME, brand.getProduct_name());
        values.put(TableAttributes.BRANDS_TAGLINE, brand.getBrand_tagline());
        values.put(TableAttributes.BRAND_LOGO, String.valueOf(brand.getPicUri()));
        if(getBrandsCount()<=0){
            try {
                dbInsert.insert(TableAttributes.BRANDS_TABLE_NAME, null, values);
                Log.i("Data", values.toString());
            } catch (SQLException e) {
                Log.e("Insert Error", e.toString());
            }
        } else {
            dbInsert.update(TableAttributes.BRANDS_TABLE_NAME, values, "brand_id = ?",
                    new String[] { String.valueOf(1) });
        }

    }

    public ArrayList<Campaign> getAllCampaingData() {
        ArrayList<Campaign> arrayList = new ArrayList<Campaign>();
        SQLiteDatabase dbFetch = this.getReadableDatabase();
        String query = "SELECT * FROM " + TableAttributes.CAMPAIGN_TABLE_NAME;
        //String phone_number_query = "SELECT "+ TableAttributes.STUDENT_PHONENO+" FROM " + TableAttributes.STUDENT_TABLE_NAME;
        Cursor cur = dbFetch.rawQuery(query, null);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            String headline, outcome, audience, subHeadline, story, frontLines;
            headline = cur.getString(cur.getColumnIndex(TableAttributes.CAMPAIGN_HEADLINE));
            outcome = cur.getString(cur.getColumnIndex(TableAttributes.CAMPAIGN_EXPECTED_OUTCOME));
            audience = cur.getString(cur.getColumnIndex(TableAttributes.CAMPAIGN_AUDIENCE));
            subHeadline = cur.getString(cur.getColumnIndex(TableAttributes.CAMPAIGN_SUB_HEADLINE));
            story = cur.getString(cur.getColumnIndex(TableAttributes.CAMPAIGN_STORY));
            frontLines = cur.getString(cur.getColumnIndex(TableAttributes.CAMPAIGN_FRONT_LINES));
            Campaign campaign = new Campaign(headline, outcome, audience, subHeadline, story, frontLines);
           /* campaign.setUsername(cur.getString(cur.getColumnIndex(TableAttributes.STUDENT_NAME)));
            campaign.setPassword(cur.getString(cur.getColumnIndex(TableAttributes.STUDENT_PASSWORD)));
            campaign.setPhoneNo(cur.getString(cur.getColumnIndex(TableAttributes.STUDENT_PHONENO)));
            campaign.setAge(cur.getInt(cur.getColumnIndex(TableAttributes.STUDENT_AGE)));
*/
            arrayList.add(campaign);
            cur.moveToNext();
        }
        return arrayList;
    }

    public ArrayList<Brands> getAllBrandsData() {
        ArrayList<Brands> arrayList = new ArrayList<Brands>();
        SQLiteDatabase dbFetch = this.getReadableDatabase();
        String query = "SELECT * FROM " + TableAttributes.BRANDS_TABLE_NAME;
        Cursor cur = dbFetch.rawQuery(query, null);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            String brand_tagline, audience_age_group, audience_gender, audience_situation, audience_purpose, audience_outcome, personality_object, celebrity_personality, product_name, product_desc, product_job, product, product_compare, competitor_name, competitor_desc,
                    competitor_links, alternative_name, alternative_desc, alter_links, picUri;
            brand_tagline = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_TAGLINE));
            audience_age_group = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_AUDIENCE_AGE_GROUP));
            audience_gender = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_AUDIENCE_GENDER));
            audience_situation = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_AUDIENCE_SITUATION));
            audience_purpose = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_AUDIENCE_PURPOSE));
            audience_outcome = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_AUDIENCE_OUTCOME));
            personality_object = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_PERSONALITY_OBJECT));
            celebrity_personality = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_CELEBRITY_PESONALITY));
            product_name = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_PRODUCT_NAME));
            product_desc = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_PRODUCT_DESCRIPTION));
            product_job = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_PRODUCT_JOB));
            product = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_PRODUCT));
            product_compare = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_PRODUCT_COMPARE));
            competitor_name = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_PRODUCT_COMPETITOR_NAME));
            competitor_desc = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_PRODUCT_COMPETATOR_DESCRIPTION));
            competitor_links = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_PRODUCT_COMPETATOR_LINKS));
            alternative_name = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_PRODUCT_ALTERNATIVE_NAME));
            alternative_desc = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_PRODUCT_ALTERNATIVE_DESCRIPTION));
            alter_links = cur.getString(cur.getColumnIndex(TableAttributes.BRANDS_PRODUCT_ALTERNATIVE_LINKS));
            picUri = cur.getString(cur.getColumnIndex(TableAttributes.BRAND_LOGO));
            Brands brands = new Brands(brand_tagline, audience_age_group, audience_gender, audience_situation, audience_purpose, audience_outcome, personality_object, celebrity_personality,
                    product_name, product_desc, product_job, product, product_compare, competitor_name, competitor_desc, competitor_links, alternative_name, alternative_desc, alter_links, Uri.parse(picUri));
           /* campaign.setUsername(cur.getString(cur.getColumnIndex(TableAttributes.STUDENT_NAME)));
            campaign.setPassword(cur.getString(cur.getColumnIndex(TableAttributes.STUDENT_PASSWORD)));
            campaign.setPhoneNo(cur.getString(cur.getColumnIndex(TableAttributes.STUDENT_PHONENO)));
            campaign.setAge(cur.getInt(cur.getColumnIndex(TableAttributes.STUDENT_AGE)));
*/
            arrayList.add(brands);
            cur.moveToNext();
        }
        return arrayList;
    }


    public int getBrandsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + TableAttributes.BRANDS_TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }



  /*  public ArrayList<Student> getStudent(String name) {
        ArrayList<Student> arrayList = new ArrayList<Student>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(TableAttributes.STUDENT_TABLE_NAME, new String[] { "student_id",
                        TableAttributes.STUDENT_NAME, TableAttributes.STUDENT_PHONENO , TableAttributes.STUDENT_PASSWORD , TableAttributes.STUDENT_AGE  }, TableAttributes.STUDENT_NAME + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null);
        if (cur != null)
            cur.moveToFirst();

        while(!cur.isAfterLast()){
            Student stdObj = new Student();
            stdObj.setUsername(cur.getString(cur.getColumnIndex(TableAttributes.STUDENT_NAME)));
            stdObj.setPassword(cur.getString(cur.getColumnIndex(TableAttributes.STUDENT_PASSWORD)));
            stdObj.setPhoneNo(cur.getString(cur.getColumnIndex(TableAttributes.STUDENT_PHONENO)));
            stdObj.setAge(cur.getInt(cur.getColumnIndex(TableAttributes.STUDENT_AGE)));

            arrayList.add(stdObj);
            cur.moveToNext();
        }

        return arrayList;
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TableAttributes.STUDENT_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    public int updateContact(String oldName,String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableAttributes.STUDENT_NAME, newName);
        // updating row
        return db.update(TableAttributes.STUDENT_TABLE_NAME, values, TableAttributes.STUDENT_NAME + " = ?",
                new String[] { String.valueOf(oldName) });
    }

    public void deleteContact(Student std) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TableAttributes.STUDENT_TABLE_NAME, TableAttributes.STUDENT_PHONENO + " = ?",
                new String[] { String.valueOf(std.getPhoneNo()) });
        db.close();
    }*/
    }
