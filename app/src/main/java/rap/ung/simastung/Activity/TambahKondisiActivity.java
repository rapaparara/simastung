package rap.ung.simastung.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rap.ung.simastung.API.APIRequestData;
import rap.ung.simastung.API.RetroServer;
import rap.ung.simastung.Model.ResponseModel;
import rap.ung.simastung.R;
import rap.ung.simastung.Session.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahKondisiActivity extends AppCompatActivity {

    ImageView img;
    FloatingActionButton fab_tambah;
    Button btn_simpan;
    String part_image;
    final int REQUEST_GALLERY = 9164;
    private ActivityResultLauncher<String> galleryLauncher;

    //kode baru
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    //kode baru

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kondisi);

        img = findViewById(R.id.kondisiImg);
        fab_tambah = findViewById(R.id.fab_simpankondisi);
        btn_simpan = findViewById(R.id.btnTambah);

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        // handle the result URI here
                    }
                });

        fab_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                galleryLauncher.launch("image/*");
                if (ContextCompat.checkSelfPermission(TambahKondisiActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TambahKondisiActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA_PERMISSION);
                } else {
                    openCamera();
                }
            }

        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File imageFile = null;
                if (part_image != null) {
                    imageFile = new File(part_image);
                }
                if (imageFile == null) {
                    Toast.makeText(TambahKondisiActivity.this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                MultipartBody.Part partImage = MultipartBody.Part.createFormData("imageUpload", imageFile.getName(), reqBody);

                SessionManagement sessionManagement = new SessionManagement(TambahKondisiActivity.this);
                int idAset = sessionManagement.getIdAset();

                RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idAset));

                APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
                Call<ResponseModel> upload = ardData.ardUpload(id, partImage);
                upload.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful()) {
                            int kode = response.body().getKode();
                            String pesan = response.body().getPesan();

                            Toast.makeText(TambahKondisiActivity.this, "Kode : " + kode + " | Pesan :" + pesan, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(TambahKondisiActivity.this, "Gagal Menyimpan Data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(TambahKondisiActivity.this, "Gagal Menyimpan | " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                img.setImageBitmap(imageBitmap);
                File imageFile = getFileFromBitmap(imageBitmap);
                RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                MultipartBody.Part partImage = MultipartBody.Part.createFormData("imageUpload", imageFile.getName(), reqBody);
                SessionManagement sessionManagement = new SessionManagement(TambahKondisiActivity.this);
                int idAset = sessionManagement.getIdAset();

                RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idAset));

                APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
                Call<ResponseModel> upload = ardData.ardUpload(id, partImage);
                upload.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful()) {
                            int kode = response.body().getKode();
                            String pesan = response.body().getPesan();

                            Toast.makeText(TambahKondisiActivity.this, "Kode : " + kode + " | Pesan :" + pesan, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TambahKondisiActivity.this, TambahKondisiActivity.class);
//                            intent.putExtra("pesan", "Data berhasil disimpan");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(TambahKondisiActivity.this, "Gagal Menyimpan Data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(TambahKondisiActivity.this, "Gagal Menyimpan | " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private File getFileFromBitmap(Bitmap bitmap) {
        //create a file to write bitmap data
        File file = new File(getCacheDir(), "temp_image");
        try {
            file.createNewFile();

            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}