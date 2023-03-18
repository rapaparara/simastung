package rap.ung.simastung.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rap.ung.simastung.API.APIRequestData;
import rap.ung.simastung.API.RetroServer;
import rap.ung.simastung.Adapter.AdapterKondisi;
import rap.ung.simastung.Model.KondisiModel;
import rap.ung.simastung.Model.ResponseModel;
import rap.ung.simastung.R;
import rap.ung.simastung.Session.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KondisiActivity extends AppCompatActivity {

    private RecyclerView rvData;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager lmData;
    private List<KondisiModel> listData = new ArrayList<>();
    private FloatingActionButton fabTambah;
    private SwipeRefreshLayout srlData;
    private ProgressBar pbData;
    String part_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kondisi);

        rvData = findViewById(R.id.rv_data);
        srlData = findViewById(R.id.srl_data);
        pbData = findViewById(R.id.pb_data);
        fabTambah = findViewById(R.id.fab_tambah);

        lmData = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvData.setLayoutManager(lmData);

        srlData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlData.setRefreshing(true);
                retrieveData();
                srlData.setRefreshing(false);
            }
        });

        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToTambahKondisi();
                }

        });
    }

    private void moveToTambahKondisi(){
        Intent intent = new Intent(KondisiActivity.this, TambahKondisiActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK){
            Uri dataImage = data.getData();
            String[] imageProjection = {MediaStore.Images.Media.DATA};

        SessionManagement sessionManagement = new SessionManagement(KondisiActivity.this);
        int idAset = sessionManagement.getIdAset();

        Cursor cursor = getContentResolver().query(dataImage, imageProjection, null, null,null);
        //        if (cursor != null) {
            cursor.moveToFirst();
            int indexImage = cursor.getColumnIndex(imageProjection[0]);
            part_image = cursor.getString(indexImage);
            cursor.close();

            File imagefile = new File(dataImage.getPath());
//            File file = new File(dataImage.getPath());
//            Uri imagefile = getImageContentUri(KondisiActivity.this,file);
            RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), imagefile);
            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idAset));
//            MultipartBody.Part body = MultipartBody.Part.createFormData("imageUpload", imagefile.getName(), reqBody);
            MultipartBody.Part body = MultipartBody.Part.createFormData("imageUpload", imagefile.getPath(), reqBody);

            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> upload = ardData.ardUpload(id, body);
            upload.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {

                }
            });

//            }
        } else Toast.makeText(KondisiActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
    }

    public static Uri getImageContentUri(Context context, File imageFile){
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=?",
                new String[] {filePath},null);
        if (cursor != null && cursor.moveToFirst()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri,""+id);
        } else {
            if (imageFile.exists()){
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
            } else {
                return null;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        retrieveData();
    }

    private void retrieveData() {
        pbData.setVisibility(View.VISIBLE);

        SessionManagement sessionManagement = new SessionManagement(KondisiActivity.this);
        int idAset = sessionManagement.getIdAset();

        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> tampilData = ardData.ardRetrieveKondisi(idAset);

        tampilData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                listData = response.body().getDataKondisi();
                Toast.makeText(KondisiActivity.this, "Kode : "+kode+" | Pesan : "+pesan, Toast.LENGTH_SHORT).show();

                adData = new AdapterKondisi(KondisiActivity.this,listData);
                rvData.setAdapter(adData);
                adData.notifyDataSetChanged();

                pbData.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(KondisiActivity.this, "Gagal menghubungkan"+t.getMessage(), Toast.LENGTH_SHORT).show();

                pbData.setVisibility(View.INVISIBLE);
            }
        });
    }
}