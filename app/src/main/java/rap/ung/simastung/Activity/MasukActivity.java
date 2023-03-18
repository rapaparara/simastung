package rap.ung.simastung.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import rap.ung.simastung.API.APIRequestData;
import rap.ung.simastung.API.RetroServer;
import rap.ung.simastung.Model.ResponseModel;
import rap.ung.simastung.Model.UserModel;
import rap.ung.simastung.R;
import rap.ung.simastung.Session.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasukActivity extends AppCompatActivity {

    private EditText etNamaPengguna,etKataSandi;
    private String namaPengguna,kataSandi;
    private Button btnMasuk;
    private List<UserModel> listUser = new ArrayList<>();
    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private boolean checkPermissions(){
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions){
            result = ContextCompat.checkSelfPermission(MasukActivity.this,p);
            if(result != PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(MasukActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),100);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();

        setContentView(R.layout.activity_masuk);

        etNamaPengguna = findViewById(R.id.et_namaPengguna);
        etKataSandi = findViewById(R.id.et_kataSandi);
        btnMasuk = findViewById(R.id.btn_masuk);

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namaPengguna = etNamaPengguna.getText().toString();
                kataSandi = etKataSandi.getText().toString();

                if(namaPengguna.trim().equals("")){
                    etNamaPengguna.setError("Nama pengguna harus diisi");
                } else if(kataSandi.trim().equals("")){
                    etKataSandi.setError("Kata sandi harus diisi");
                } else{
                    penggunaMasuk();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }

    private void checkSession() {
        SessionManagement sessionManagement = new SessionManagement(MasukActivity.this);
        String namaPengguna = sessionManagement.getSession();
        if(namaPengguna!="") {
            moveToMainActivity();
        } else {

        }
    }


    private void penggunaMasuk(){
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> masuk = ardData.ardLogin(namaPengguna,kataSandi);

        masuk.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                if(kode==1) {
                    Toast.makeText(MasukActivity.this, "Kode : " + kode + " | Pesan :" + pesan, Toast.LENGTH_SHORT).show();

                    listUser = response.body().getDataPengguna();
                    int varId = listUser.get(0).getId();
                    String varNama = listUser.get(0).getNama();
                    String varTipe = listUser.get(0).getTipe();

                    SessionManagement sessionManagement = new SessionManagement(MasukActivity.this);
                    sessionManagement.saveSession(varNama,varTipe);
                    moveToMainActivity();
                } else {
                    Toast.makeText(MasukActivity.this, "Kode : " + kode + " | Pesan :" + pesan, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MasukActivity.this, "Gagal Menyimpan | "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveToMainActivity(){
        Intent intent = new Intent(MasukActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}