package rap.ung.simastung.Model;

import com.google.gson.annotations.SerializedName;

public class UploadModel {
    @SerializedName("kode")
    String kode;
    @SerializedName("pesan")
    String pesan;

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }
}
