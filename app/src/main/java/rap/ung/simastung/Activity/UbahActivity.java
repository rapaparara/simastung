package rap.ung.simastung.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import rap.ung.simastung.API.APIRequestData;
import rap.ung.simastung.API.RetroServer;
import rap.ung.simastung.Model.ResponseModel;
import rap.ung.simastung.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahActivity extends AppCompatActivity {
    private int xId;
    private String xNamaAset,xTipe,xDeskripsi;
    private EditText etNamaAset,etDeskripsi;
    private Button btnUbah;
    private String yNamaAset,yDeskripsi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);

        Intent intent = getIntent();
        xId = intent.getIntExtra("xIdAset", -1);
        xNamaAset = intent.getStringExtra("xNamaAset");
        xTipe = intent.getStringExtra("xTipe");
        xDeskripsi = intent.getStringExtra("xDeskripsi");

        etNamaAset = findViewById(R.id.et_nama);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        btnUbah = findViewById(R.id.btn_ubah);

        etNamaAset.setText(xNamaAset);
        etDeskripsi.setText(xDeskripsi);

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yNamaAset = etNamaAset.getText().toString();
                yDeskripsi = etDeskripsi.getText().toString();
                updateData();
            }
        });
    }

    private void updateData() {
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> ubahData = ardData.ardUpdateData(xId,yNamaAset,xTipe,yDeskripsi);

        ubahData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(UbahActivity.this, "Kode : "+kode+" | Pesan :"+pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(UbahActivity.this, "Gagal Menyimpan | "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}