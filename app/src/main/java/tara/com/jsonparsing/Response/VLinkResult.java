package tara.com.jsonparsing.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tara on 16-Apr-17.
 */

public class VLinkResult {


        @SerializedName("id")
        private int id;

        @SerializedName("key")
        private String Key;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKey() {
            return Key;
        }

        public void setKey(String key) {
            Key = key;
        }

}
