package multiplexer.contentcreator.Model;

/**
 * Created by U on 4/28/2017.
 */

public class Template {
    private String headline, subHeadline, frontLine,img_url,user_img_uri,
            headline_top_position,headline_left_position,headline_font_size,headline_text_color,headline_font,
            sub_headline_top_position,sub_headline_left_position,sub_headline_font_size,sub_headline_text_color,sub_headline_font,
            fronLine_top_position,frontLine_left_position,frontLine_font_size,frontLine_text_color,front_line_font;
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

    public void setUser_img_uri(String user_img_uri) {
        this.user_img_uri = user_img_uri;
    }

    public String getUser_img_uri() {
        return user_img_uri;
    }


    public String getHeadline_top_position() {
        return headline_top_position;
    }

    public void setHeadline_top_position(String headline_top_position) {
        this.headline_top_position = headline_top_position;
    }

    public String getHeadline_left_position() {
        return headline_left_position;
    }

    public void setHeadline_left_position(String headline_left_position) {
        this.headline_left_position = headline_left_position;
    }

    public String getHeadline_font_size() {
        return headline_font_size;
    }

    public void setHeadline_font_size(String headline_font_size) {
        this.headline_font_size = headline_font_size;
    }

    public String getHeadline_text_color() {
        return headline_text_color;
    }

    public void setHeadline_text_color(String headline_text_color) {
        this.headline_text_color = headline_text_color;
    }

    public String getSub_headline_top_position() {
        return sub_headline_top_position;
    }

    public void setSub_headline_top_position(String sub_headline_top_position) {
        this.sub_headline_top_position = sub_headline_top_position;
    }

    public String getSub_headline_left_position() {
        return sub_headline_left_position;
    }

    public void setSub_headline_left_position(String sub_headline_left_position) {
        this.sub_headline_left_position = sub_headline_left_position;
    }

    public String getSub_headline_font_size() {
        return sub_headline_font_size;
    }

    public void setSub_headline_font_size(String sub_headline_font_size) {
        this.sub_headline_font_size = sub_headline_font_size;
    }

    public String getSub_headline_text_color() {
        return sub_headline_text_color;
    }

    public void setSub_headline_text_color(String sub_headline_text_color) {
        this.sub_headline_text_color = sub_headline_text_color;
    }

    public String getFronLine_top_position() {
        return fronLine_top_position;
    }

    public void setFronLine_top_position(String fronLine_top_position) {
        this.fronLine_top_position = fronLine_top_position;
    }

    public String getFrontLine_left_position() {
        return frontLine_left_position;
    }

    public void setFrontLine_left_position(String frontLine_left_position) {
        this.frontLine_left_position = frontLine_left_position;
    }

    public String getFrontLine_font_size() {
        return frontLine_font_size;
    }

    public void setFrontLine_font_size(String frontLine_font_size) {
        this.frontLine_font_size = frontLine_font_size;
    }

    public String getFrontLine_text_color() {
        return frontLine_text_color;
    }

    public void setFrontLine_text_color(String frontLine_text_color) {
        this.frontLine_text_color = frontLine_text_color;
    }

    public String getHeadline_font() {
        return headline_font;
    }

    public void setHeadline_font(String headline_font) {
        this.headline_font = headline_font;
    }

    public String getSub_headline_font() {
        return sub_headline_font;
    }

    public void setSub_headline_font(String sub_headline_font) {
        this.sub_headline_font = sub_headline_font;
    }

    public String getFront_line_font() {
        return front_line_font;
    }

    public void setFront_line_font(String front_line_font) {
        this.front_line_font = front_line_font;
    }
}
