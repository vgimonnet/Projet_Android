package com.example.projet_android_lp.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_android_lp.Models.Artiste;
import com.example.projet_android_lp.Models.Musique;
import com.example.projet_android_lp.R;

import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicViewHolder> {

    class MusicViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitre;
        private final TextView txtArtiste;

        private MusicViewHolder(View itemView) {
            super(itemView);
            txtArtiste = itemView.findViewById(R.id.lblArtisteItemMusicData);
            txtTitre = itemView.findViewById(R.id.lblTitreItemMusicData);
        }
    }

    private final LayoutInflater mInflater;
    private List<Musique> mMusiques; // Cached copy of musiques
    private List<Artiste> mArtistes; // Cached copy of artistes
    private MyMusicPlayerViewModel myMusicPlayerViewModel;

    /*public MusicListAdapter(Context context, MyMusicPlayerViewModel viewModel) {
        mInflater = LayoutInflater.from(context);
        myMusicPlayerViewModel = viewModel;
    }*/

    public MusicListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        myMusicPlayerViewModel = ViewModelProviders.of((FragmentActivity) context).get(MyMusicPlayerViewModel.class);
    }

    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_view_music_item, parent, false);
        return new MusicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MusicViewHolder holder, int position) {
        if (mMusiques != null) {
            Musique current = mMusiques.get(position);
            Log.d("test", "id artiste =  " + current.getArtisteRefId());
            long id = current.getArtisteRefId();
            for (Artiste a: mArtistes
                 ) {
                Log.d("test", a.getNom() + " " + a.getArtisteId());
            }

            Artiste artiste = mArtistes.get((int)id);
            if (artiste != null){
                holder.txtArtiste.setText(artiste.getNom());
            }else{
                holder.txtArtiste.setText("Non renseigné");
            }
            holder.txtTitre.setText(current.getTitre());

        } else {
            // Covers the case of data not being ready yet.
            holder.txtArtiste.setText("Aucun artiste");
            holder.txtTitre.setText("Aucun titre");
        }
    }

    public void setMusiques(List<Musique> musiques){
        mMusiques = musiques;
        notifyDataSetChanged();
    }

    public void setArtistes(List<Artiste> artistes){
        mArtistes = artistes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mMusiques != null)
            return mMusiques.size();
        else return 0;
    }

    public void removeItem(int position) {
        mMusiques.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Musique item, int position) {
        mMusiques.add(position, item);
        notifyItemInserted(position);
    }

    public List<Musique> getData() {
        return mMusiques;
    }

}
