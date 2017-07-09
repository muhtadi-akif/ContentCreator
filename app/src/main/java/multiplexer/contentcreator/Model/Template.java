package multiplexer.contentcreator.Model;

/**
 * Created by U on 4/28/2017.
 */

public class Template {
    private String headline, subHeadline, frontLine,img_url;
    public Template(String headline,String subHeadline, String frontLine,String img_url){
        this.headline = headline;
        this.subHeadline = subHeadline;
        this.frontLine = frontLine;
        this.img_url = img_url;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSubHeadline() {
        return subHeadline;
    }

    public void setSubHeadline(String subHeadline) {
        this.subHeadline = subHeadline;
    }

    public String getFrontLine() {
        return frontLine;
    }

    public void setFrontLine(String frontLine) {
        this.frontLine = frontLine;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
