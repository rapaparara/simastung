package rap.ung.simastung.Model;

public class KondisiModel {
    private int idKondisi,idAset;
    private String nama,waktu,foto;

    public int getIdKondisi() {
        return idKondisi;
    }

    public void setIdKondisi(int idKondisi) {
        this.idKondisi = idKondisi;
    }

    public int getIdAset() {
        return idAset;
    }

    public void setIdAset(int idAset) {
        this.idAset = idAset;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
