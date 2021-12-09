package algonquin.cst2335.cst_2335_mobile_final_project;

import java.io.Serializable;

/**
 * This class contain the structure of the pexels result
 *
 * @author Yingying Zhao
 * @version 1.0
 */
public class PhotosBean implements Serializable {

    /**
     * class fields
     */
    private int id;
    private Integer width;
    private Integer height;
    private String photographer;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getPhotographer() {
        return photographer;
    }

    public void setPhotographer(String photographer) {
        this.photographer = photographer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PhotosBean{" +
                "id=" + id +
                ", width=" + width +
                ", height=" + height +
                ", photographer='" + photographer + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
