package multiplexer.contentcreator.Model;


import android.net.Uri;

/**
 * Created by U on 4/19/2017.
 */

public class Brands {
    private String brand_tagline,audience_age_group,audience_gender,audience_situation,audience_purpose,audience_outcome,personality_object,celebrity_personality
            ,product_name,product_desc,product_job,product,product_compare,competitor_name,competitor_desc,
            competitor_links,alternative_name,alternative_desc,alter_links;
    private Uri picUri;
    public Brands(String brand_tagline,String audience_age_group,String audience_gender,String audience_situation,String audience_purpose,String audience_outcome,
                  String personality_object,String celebrity_personality,String product_name,String product_desc,String product_job,String product,
                  String product_compare,String competitor_name,String competitor_desc,String competitor_links,String alternative_name,String alternative_desc,String alter_links,Uri picUri
                ){

        this.brand_tagline = brand_tagline;
        this.audience_age_group = audience_age_group;
        this.audience_gender = audience_gender;
        this.audience_situation = audience_situation;
        this.audience_purpose = audience_purpose;
        this.audience_outcome = audience_outcome;
        this.personality_object = personality_object;
        this.celebrity_personality = celebrity_personality;
        this.product_name = product_name;
        this.product_desc = product_desc;
        this.product_job = product_job;
        this.product = product;
        this.product_compare = product_compare;
        this.competitor_name = competitor_name;
        this.competitor_desc = competitor_desc;
        this.competitor_links = competitor_links;
        this.alternative_name = alternative_name;
        this.alternative_desc = alternative_desc;
        this.alter_links = alter_links;
        this.picUri = picUri;
    }

    public String getBrand_tagline() {
        return brand_tagline;
    }

    public void setBrand_tagline(String brand_tagline) {
        this.brand_tagline = brand_tagline;
    }

    public String getAudience_age_group() {
        return audience_age_group;
    }

    public void setAudience_age_group(String audience_age_group) {
        this.audience_age_group = audience_age_group;
    }

    public String getAudience_gender() {
        return audience_gender;
    }

    public void setAudience_gender(String audience_gender) {
        this.audience_gender = audience_gender;
    }

    public String getAudience_situation() {
        return audience_situation;
    }

    public void setAudience_situation(String audience_situation) {
        this.audience_situation = audience_situation;
    }

    public String getAudience_purpose() {
        return audience_purpose;
    }

    public void setAudience_purpose(String audience_purpose) {
        this.audience_purpose = audience_purpose;
    }

    public String getAudience_outcome() {
        return audience_outcome;
    }

    public void setAudience_outcome(String audience_outcome) {
        this.audience_outcome = audience_outcome;
    }

    public String getPersonality_object() {
        return personality_object;
    }

    public void setPersonality_object(String personality_object) {
        this.personality_object = personality_object;
    }

    public String getCelebrity_personality() {
        return celebrity_personality;
    }

    public void setCelebrity_personality(String celebrity_personality) {
        this.celebrity_personality = celebrity_personality;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getProduct_job() {
        return product_job;
    }

    public void setProduct_job(String product_job) {
        this.product_job = product_job;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProduct_compare() {
        return product_compare;
    }

    public void setProduct_compare(String product_compare) {
        this.product_compare = product_compare;
    }

    public String getCompetitor_name() {
        return competitor_name;
    }

    public void setCompetitor_name(String competitor_name) {
        this.competitor_name = competitor_name;
    }

    public String getCompetitor_desc() {
        return competitor_desc;
    }

    public void setCompetitor_desc(String competitor_desc) {
        this.competitor_desc = competitor_desc;
    }

    public String getCompetitor_links() {
        return competitor_links;
    }

    public void setCompetitor_links(String competitor_links) {
        this.competitor_links = competitor_links;
    }

    public String getAlternative_name() {
        return alternative_name;
    }

    public void setAlternative_name(String alternative_name) {
        this.alternative_name = alternative_name;
    }

    public String getAlternative_desc() {
        return alternative_desc;
    }

    public void setAlternative_desc(String alternative_desc) {
        this.alternative_desc = alternative_desc;
    }

    public String getAlter_links() {
        return alter_links;
    }

    public void setAlter_links(String alter_links) {
        this.alter_links = alter_links;
    }

    public Uri getPicUri() {
        return picUri;
    }

    public void setPicUri(Uri picUri) {
        this.picUri = picUri;
    }
}
