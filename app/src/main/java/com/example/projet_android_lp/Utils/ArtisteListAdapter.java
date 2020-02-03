package com.example.projet_android_lp.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_android_lp.Models.Artiste;
import com.example.projet_android_lp.Models.Musique;
import com.example.projet_android_lp.R;

import java.util.ArrayList;
import java.util.List;

public class ArtisteListAdapter extends RecyclerView.Adapter<ArtisteListAdapter.ArtistViewHolder>  {

    class ArtistViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtArtiste;
        private final TextView txtNbMusiques;

        private ArtistViewHolder(View itemView) {
            super(itemView);
            txtArtiste = itemView.findViewById(R.id.lblArtisteItemArtisteData);
            txtNbMusiques = itemView.findViewById(R.id.lblNbMusiqueItemArtisteData);
        }
    }

    private final LayoutInflater mInflater;
    private List<Musique> mMusiques;
    private List<Artiste> mArtistes;
    private ArrayList<Artiste> mArtistesCopy; // Cached copy of musiques
    private MyMusicPlayerViewModel myMusicPlayerViewModel;
    private boolean search;


    public ArtisteListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        myMusicPlayerViewModel = ViewModelProviders.of((FragmentActivity) context).get(MyMusicPlayerViewModel.class);
        search = false;
        mArtistesCopy = new ArrayList<>();
    }

    @Override
    public ArtisteListAdapter.ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_view_artist_item, parent, false);
        return new ArtisteListAdapter.ArtistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArtisteListAdapter.ArtistViewHolder holder, int position) {
        if (mArtistes != null) {
            Artiste current = mArtistes.get(position);
            long id = current.getArtisteId() - 1;
            int nbMusiques = 0;

            if (mMusiques != null){
                for (Musique m : mMusiques) {
                    if (m.getArtisteRefId() == id){
                        nbMusiques++;
                    }
                }
            }

            if (current != null){
                holder.txtArtiste.setText(current.getNom());
                holder.txtNbMusiques.setText(Integer.toString(nbMusiques));
            }else{
                holder.txtArtiste.setText("Non renseigné");
                holder.txtNbMusiques.setText("Non renseigné");
            }

        } else {
            holder.txtArtiste.setText("Aucun artiste");
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
        if (mArtistes != null)
            return mArtistes.size();
        else return 0;
    }

    public void filterArtiste(Context context, String text){
        if (!search){
            search = true;
            for(Artiste item: mArtistes){
                mArtistesCopy.add(item);
            }
        }

        if(text.isEmpty()){
            mArtistes.clear();
            mArtistes.addAll(mArtistesCopy);
            search = true;
        } else{
            ArrayList<Artiste> result = new ArrayList<>();
            text = text.toLowerCase();

            for(Artiste item: mArtistes){
                if(item.getNom().toLowerCase().contains(text)){
                    result.add(item);
                }
            }
            if(result.size()==0){
                Toast.makeText(context, "Aucun artiste ne correspond à cette recherche", Toast.LENGTH_SHORT).show();
            }else{
                mArtistes.clear();
                mArtistes.addAll(result);
            }
        }
        notifyDataSetChanged();
    }

}
