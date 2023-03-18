package rap.ung.simastung.Model;

import java.util.List;

public class ResponseModel {
    private int kode;
    private String pesan;
    private List<DataModel> data;
    private List<UserModel> dataPengguna;
    private List<KondisiModel> dataKondisi;

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }

    public List<UserModel> getDataPengguna() {
        return dataPengguna;
    }

    public void setDataPengguna(List<UserModel> dataPengguna) {
        this.dataPengguna = dataPengguna;
    }

    public List<KondisiModel> getDataKondisi() {
        return dataKondisi;
    }

    public void setDataKondisi(List<KondisiModel> dataKondisi) {
        this.dataKondisi = dataKondisi;
    }
}
