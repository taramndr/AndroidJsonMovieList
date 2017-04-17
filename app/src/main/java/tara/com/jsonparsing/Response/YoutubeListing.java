package tara.com.jsonparsing.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tara on 16-Apr-17.
 */

public class YoutubeListing {
    @SerializedName("id")
    private Integer id;

    @SerializedName("results")
    private ArrayList<VLinkResult> vLinkResults = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<VLinkResult> getVlinkresults() {
        return vLinkResults;
    }

    public void setVlinkresults(ArrayList<VLinkResult> vlinkresults) {
        this.vLinkResults = vlinkresults;
    }
}
