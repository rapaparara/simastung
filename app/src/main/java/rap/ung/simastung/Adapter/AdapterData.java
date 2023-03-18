package rap.ung.simastung.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import rap.ung.simastung.API.APIRequestData;
import rap.ung.simastung.API.RetroServer;
import rap.ung.simastung.Activity.DetailActivity;
import rap.ung.simastung.Activity.MainActivity;
import rap.ung.simastung.Activity.MasukActivity;
import rap.ung.simastung.Activity.UbahActivity;
import rap.ung.simastung.Model.DataModel;
import rap.ung.simastung.Model.ResponseModel;
import rap.ung.simastung.Model.UserModel;
import rap.ung.simastung.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData>{
    private Context ctx;
    private List<DataModel> listAset;
    private List<DataModel> listAsetbyId;
    private int idAset;

    public AdapterData(Context ctx, List<DataModel> listAset) {
        this.ctx = ctx;
        this.listAset = listAset;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModel dm = listAset.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvNama.setText(dm.getNama());
        holder.tvDeskripsi.setText(dm.getDeskripsi());

    }

    @Override
    public int getItemCount() {
        return listAset.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{
        TextView tvId,tvNama,tvDeskripsi;
        public HolderData(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tvId);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    idAset = Integer.parseInt(tvId.getText().toString());
                    getDataDetail();

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder dialogPesan = new AlertDialog.Builder(ctx);
                    dialogPesan.setMessage("Pilih operasi yang akan dilakukan");
                    dialogPesan.setTitle("Perhatian");
                    dialogPesan.setIcon(R.drawable.ic_launcher_foreground);
                    dialogPesan.setCancelable(true);

                    idAset = Integer.parseInt(tvId.getText().toString());

                    dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteData();
                            dialogInterface.dismiss();
                            ((MainActivity) ctx).retrieveData();
                        }
                    });

                    dialogPesan.setNegativeButton("Ubah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getDataUbah();
                            dialogInterface.dismiss();
                        }
                    });
                    dialogPesan.show();
                    return true;
                }
            });
        }

        private void deleteData(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> hapusData = ardData.ardDeleteData(idAset);

            hapusData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(ctx, "Kode : "+kode+" | Pesan : "+pesan, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal menghapus | "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void getDataUbah(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> ambilData = ardData.ardGetData(idAset);

            ambilData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    listAsetbyId = response.body().getData();

                    int varIdAset = listAsetbyId.get(0).getId();
                    String varNamaAset = listAsetbyId.get(0).getNama();
                    String varTipe = listAsetbyId.get(0).getTipe();
                    String varDesksripsi = listAsetbyId.get(0).getDeskripsi();

                    Toast.makeText(ctx, "Kode : "+kode+" | Pesan : "+pesan, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ctx, UbahActivity.class);
                    intent.putExtra("xIdAset",varIdAset);
                    intent.putExtra("xNamaAset",varNamaAset);
                    intent.putExtra("xTipe",varTipe);
                    intent.putExtra("xDeskripsi",varDesksripsi);
                    ctx.startActivity(intent);
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal mengambil data | "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void getDataDetail(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> ambilData = ardData.ardGetData(idAset);

            ambilData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    listAsetbyId = response.body().getData();

                    int varIdAset = listAsetbyId.get(0).getId();
                    String varNamaAset = listAsetbyId.get(0).getNama();
                    String varTipe = listAsetbyId.get(0).getTipe();
                    String varDesksripsi = listAsetbyId.get(0).getDeskripsi();

                    Toast.makeText(ctx, "Kode : "+kode+" | Pesan : "+pesan, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ctx, DetailActivity.class);
                    intent.putExtra("xIdAset",varIdAset);
                    intent.putExtra("xNamaAset",varNamaAset);
                    intent.putExtra("xTipe",varTipe);
                    intent.putExtra("xDeskripsi",varDesksripsi);
                    ctx.startActivity(intent);
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal mengambil data | "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
