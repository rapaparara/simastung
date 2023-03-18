package rap.ung.simastung.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import rap.ung.simastung.API.APIRequestData;
import rap.ung.simastung.API.RetroServer;
import rap.ung.simastung.Model.ResponseModel;
import rap.ung.simastung.R;
import rap.ung.simastung.Session.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahActivity extends AppCompatActivity {

    private EditText etNama,etDeskripsi;
    private Button btnSimpan;
    private String nama,tipe,deskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        etNama = findViewById(R.id.et_nama);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        btnSimpan = findViewById(R.id.btn_simpan);


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManagement sessionManagement = new SessionManagement(TambahActivity.this);
                tipe = sessionManagement.getTipeUser();
                nama = etNama.getText().toString();
                deskripsi = etDeskripsi.getText().toString();

                if(nama.trim().equals("")){
                    etNama.setError("Nama aset harus diisi");
                } else if(tipe.trim().equals("")){
                    etDeskripsi.setError("Deskripsi harus diisi");
                } else {
                    createData();
                }
            }
        });
    }

    private void createData(){
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> simpanData = ardData.ardCreateData(nama,tipe,deskripsi);

        simpanData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(TambahActivity.this, "Kode : "+kode+" | Pesan :"+pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(TambahActivity.this, "Gagal Menyimpan | "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}