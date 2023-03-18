package rap.ung.simastung.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rap.ung.simastung.Activity.KondisiActivity;
import rap.ung.simastung.Model.DataModel;
import rap.ung.simastung.Model.KondisiModel;
import rap.ung.simastung.R;

public class AdapterKondisi extends RecyclerView.Adapter<AdapterKondisi.HolderKondisi> {

    private Context ctx;
    private List<KondisiModel> listKondisi;

    public AdapterKondisi(Context ctx, List<KondisiModel> listKondisi) {
        this.ctx = ctx;
        this.listKondisi = listKondisi;
    }

    public class HolderKondisi extends RecyclerView.ViewHolder {
        TextView tvIdKondisi,tvWaktu;
        ImageView imgFoto;
        public HolderKondisi(@NonNull View itemView) {
            super(itemView);

            tvIdKondisi = itemView.findViewById(R.id.tvIdKondisi);
            tvWaktu = itemView.findViewById(R.id.tvWaktu);
            imgFoto = itemView.findViewById(R.id.imgFoto);

        }
    }
    @NonNull
    @Override
    public HolderKondisi onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_kondisi,parent,false);
        HolderKondisi holder = new HolderKondisi(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderKondisi holder, int position) {
        KondisiModel km = listKondisi.get(position);

        holder.tvIdKondisi.setText(String.valueOf(km.getIdKondisi()));
        holder.tvWaktu.setText(km.getWaktu());
        String txtFoto = km.getFoto();
        Picasso.get().load("https://familiarizing-study.000webhostapp.com/upload/"+txtFoto).fit().centerCrop().into(holder.imgFoto);
    }

    @Override
    public int getItemCount() {
        if (listKondisi == null) return 0;
        else return listKondisi.size();
    }

}
