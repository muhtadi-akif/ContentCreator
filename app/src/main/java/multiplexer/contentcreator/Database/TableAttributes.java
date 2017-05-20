package multiplexer.contentcreator.Database;


public class TableAttributes {

    public static final String CAMPAIGN_TABLE_NAME = "Campaign";
    public static final String CAMPAIGN_EXPECTED_OUTCOME= "outcome";
    public static final String CAMPAIGN_AUDIENCE= "audience";
    public static final String CAMPAIGN_HEADLINE = "headline";
    public static final String CAMPAIGN_SUB_HEADLINE = "sub_headline";
    public static final String CAMPAIGN_STORY = "story";
    public static final String CAMPAIGN_FRONT_LINES = "front_lines";

    public static final String BRANDS_TABLE_NAME = "BrandsTable";
    public static final String BRANDS_TAGLINE = "brand_tagline";
    public static final String BRANDS_AUDIENCE_AGE_GROUP = "audience_age_group";
    public static final String BRANDS_AUDIENCE_GENDER = "audience_gender";
    public static final String BRANDS_AUDIENCE_SITUATION = "audience_situation";
    public static final String BRANDS_AUDIENCE_PURPOSE= "audience_purpose";
    public static final String BRANDS_AUDIENCE_OUTCOME = "audience_outcome";
    public static final String BRANDS_PERSONALITY_OBJECT = "personality_object";
    public static final String BRANDS_CELEBRITY_PESONALITY= "celebrity_personality";
    public static final String BRANDS_PRODUCT_NAME = "product_name";
    public static final String BRANDS_PRODUCT_DESCRIPTION= "product_desc";
    public static final String BRANDS_PRODUCT_JOB= "product_job";
    public static final String BRANDS_PRODUCT= "product";
    public static final String BRANDS_PRODUCT_COMPARE= "product_compare";
    public static final String BRANDS_PRODUCT_COMPETITOR_NAME= "competitor_name";
    public static final String BRANDS_PRODUCT_COMPETATOR_DESCRIPTION= "competitor_desc";
    public static final String BRANDS_PRODUCT_COMPETATOR_LINKS= "competitor_links";
    public static final String BRANDS_PRODUCT_ALTERNATIVE_NAME= "alternative_name";
    public static final String BRANDS_PRODUCT_ALTERNATIVE_DESCRIPTION= "alternative_desc";
    public static final String BRANDS_PRODUCT_ALTERNATIVE_LINKS= "alternative_links";
    public static final String BRAND_LOGO= "picUri";

    public String brandsTableCreateQuery(){
        return "CREATE TABLE "+BRANDS_TABLE_NAME+"(brand_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                BRANDS_TAGLINE+" TEXT," +
                BRANDS_PRODUCT_NAME+" TEXT," +
                BRANDS_PRODUCT_JOB+" TEXT," +
                BRANDS_PRODUCT_DESCRIPTION+" TEXT," +
                BRANDS_PRODUCT_COMPETITOR_NAME+" TEXT," +
                BRANDS_PRODUCT_COMPETATOR_LINKS+" TEXT," +
                BRANDS_PRODUCT_COMPETATOR_DESCRIPTION+" TEXT," +
                BRANDS_PRODUCT_COMPARE+" TEXT," +
                BRANDS_PRODUCT+" TEXT," +
                BRANDS_AUDIENCE_AGE_GROUP+" TEXT," +
                BRANDS_AUDIENCE_GENDER+" TEXT," +
                BRANDS_AUDIENCE_OUTCOME+" TEXT," +
                BRANDS_AUDIENCE_PURPOSE+" TEXT," +
                BRANDS_AUDIENCE_SITUATION+" TEXT," +
                BRANDS_PERSONALITY_OBJECT+" TEXT," +
                BRANDS_CELEBRITY_PESONALITY+" TEXT," +
                BRANDS_PRODUCT_ALTERNATIVE_DESCRIPTION+" TEXT," +
                BRANDS_PRODUCT_ALTERNATIVE_LINKS+" TEXT," +
                BRAND_LOGO+" TEXT," +
                BRANDS_PRODUCT_ALTERNATIVE_NAME+" TEXT)";
    }

    public String campaignTableCreateQuery(){
        return "CREATE TABLE "+CAMPAIGN_TABLE_NAME+"(campaign_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                CAMPAIGN_EXPECTED_OUTCOME+" TEXT," +
                CAMPAIGN_AUDIENCE+" TEXT," +
                CAMPAIGN_HEADLINE+" TEXT," +
                CAMPAIGN_SUB_HEADLINE+" TEXT," +
                CAMPAIGN_STORY+" TEXT," +
                CAMPAIGN_FRONT_LINES+" TEXT)";
    }
}
