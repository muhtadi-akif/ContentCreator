package multiplexer.contentcreator.Model;

/**
 * Created by U on 4/19/2017.
 */

public class Campaign {
    private String headline,outcome,audience,sub_headline,front_lines,call_to_action;
    int camp_id;
    public Campaign(int camp_id,String headline,String outcome,String audience,String sub_headline,String front_lines,String call_to_action){
        this.headline = headline;
        this.outcome = outcome;
        this.audience = audience;
        this.sub_headline = sub_headline;
        this.front_lines = front_lines;
        this.camp_id = camp_id;
        this.call_to_action = call_to_action;
    }
    public Campaign(String headline){
        this.headline = headline;
    }
    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getSub_headline() {
        return sub_headline;
    }

    public void setSub_headline(String sub_headline) {
        this.sub_headline = sub_headline;
    }

    public String getFront_lines() {
        return front_lines;
    }

    public void setFront_lines(String front_lines) {
        this.front_lines = front_lines;
    }

    public int getCamp_id() {
        return camp_id;
    }

    public void setCamp_id(int camp_id) {
        this.camp_id = camp_id;
    }

    public String getCall_to_action() {
        return call_to_action;
    }

    public void setCall_to_action(String call_to_action) {
        this.call_to_action = call_to_action;
    }
}
