package rap.ung.simastung.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import rap.ung.simastung.API.APIRequestData;
import rap.ung.simastung.API.RetroServer;
import rap.ung.simastung.Model.DataModel;
import rap.ung.simastung.Model.KondisiModel;
import rap.ung.simastung.Model.ResponseModel;
import rap.ung.simastung.R;
import rap.ung.simastung.Session.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private int xId;
    private String xNamaAset,xTipe,xDeskripsi;
    private TextView txtNamaAset, txtTipe,txtDeskripsi;
    private List<KondisiModel> listKondisi;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Intent intent = getIntent();
        xId = intent.getIntExtra("xIdAset", -1);
        xNamaAset = intent.getStringExtra("xNamaAset");
        xTipe = intent.getStringExtra("xTipe");
        xDeskripsi = intent.getStringExtra("xDeskripsi");

        txtNamaAset = findViewById(R.id.tv_nama);
        txtTipe = findViewById(R.id.tv_tipe);
        txtDeskripsi = findViewById(R.id.tv_deskripsi);
        btnUpdate = findViewById(R.id.btn_update);

        txtNamaAset.setText(xNamaAset);
        txtTipe.setText(xTipe);
        txtDeskripsi.setText(xDeskripsi);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, KondisiActivity.class);
                intent.putExtra("xIdAset",xId);

                SessionManagement sessionManagement = new SessionManagement(DetailActivity.this);
                sessionManagement.saveIdAset(xId);
                startActivity(intent);
            }
        });

    }

    private void getDataKondisi() {
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> ambilKondisi = ardData.ardRetrieveKondisi(xId);

        ambilKondisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listKondisi = response.body().getDataKondisi();

                int varIdKondisi = listKondisi.get(0).getIdKondisi();
                int varIdAset = listKondisi.get(0).getIdAset();
                String varNama = listKondisi.get(0).getNama();
                String varWaktu = listKondisi.get(0).getWaktu();
                String varFoto = listKondisi.get(0).getFoto();

                Toast.makeText(DetailActivity.this, "Kode : "+kode+" | Pesan : "+pesan, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailActivity.this, KondisiActivity.class);
                intent.putExtra("xIdKondisi",varIdKondisi);
                intent.putExtra("xidAset",varIdAset);
                intent.putExtra("xNama",varNama);
                intent.putExtra("xWaktu",varWaktu);
                intent.putExtra("xFoto",varFoto);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }
}