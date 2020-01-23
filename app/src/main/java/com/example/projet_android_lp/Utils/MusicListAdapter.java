package com.example.projet_android_lp.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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
    private List<Musique> mMusiques; // Cached copy of words

    public MusicListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_view_music_item, parent, false);
        return new MusicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MusicViewHolder holder, int position) {
        if (mMusiques != null) {
            Musique current = mMusiques.get(position);
            holder.txtArtiste.setText(current.getArtiste());
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

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mMusiques != null)
            return mMusiques.size();
        else return 0;
    }

}
