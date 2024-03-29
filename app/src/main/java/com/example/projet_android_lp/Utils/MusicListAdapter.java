package com.example.projet_android_lp.Utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_android_lp.Models.Artiste;
import com.example.projet_android_lp.Models.Musique;
import com.example.projet_android_lp.R;

import java.util.ArrayList;
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
    private List<Musique> mMusiques;
    private List<Artiste> mArtistes;
    private ArrayList<Musique> mMusiquesCopy; // Cached copy of musiques
    private MyMusicPlayerViewModel myMusicPlayerViewModel;
    private boolean search;


    public MusicListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        myMusicPlayerViewModel = ViewModelProviders.of((FragmentActivity) context).get(MyMusicPlayerViewModel.class);
        search = false;
        mMusiquesCopy = new ArrayList<>();
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
            long id = current.getArtisteRefId();

            Artiste artiste = myMusicPlayerViewModel.getArtisteById(id);
            //Artiste artiste = mArtistes.get((int)id);
            if (artiste != null){
                holder.txtArtiste.setText(artiste.getNom());
            }else{
                holder.txtArtiste.setText("Non renseigné");
            }
            holder.txtTitre.setText(current.getTitre());

        } else {
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

    public void filterMusique(Context context, String text){
        if (!search){
            search = true;
            for(Musique item: mMusiques){
                mMusiquesCopy.add(item);
            }
        }

        if(text.isEmpty()){
            mMusiques.clear();
            mMusiques.addAll(mMusiquesCopy);
            search = true;
        } else{
            ArrayList<Musique> result = new ArrayList<>();
            text = text.toLowerCase();

            for(Musique item: mMusiques){
                if(item.getTitre().toLowerCase().contains(text)){
                    result.add(item);
                }
            }
            if(result.size()==0){
                Toast.makeText(context, "Aucune musique ne correspond à cette recherche", Toast.LENGTH_SHORT).show();
            }else{
                mMusiques.clear();
                mMusiques.addAll(result);
            }
        }
        notifyDataSetChanged();
    }

    public void filterArtiste(Context context, String text){
        if (!search){
            search = true;
            for(Musique item: mMusiques){
                mMusiquesCopy.add(item);
            }
        }

        if(text.isEmpty()){
            mMusiques.clear();
            mMusiques.addAll(mMusiquesCopy);
            search = true;
        } else{
            Artiste artiste = myMusicPlayerViewModel.getArtisteByName(text);
            if (artiste != null){
                Long idArtiste = artiste.getArtisteId();
                ArrayList<Musique> result = new ArrayList<>();
                text = text.toLowerCase();

                for(Musique item: mMusiques){
                    if(item.getArtisteRefId() == idArtiste){
                        result.add(item);
                    }
                }
                if (result.size() == 0){
                    Toast.makeText(context, "Aucune musique trouvé pour cette artiste", Toast.LENGTH_SHORT).show();
                }else{
                    mMusiques.clear();
                    mMusiques.addAll(result);
                }
            }
        }
        notifyDataSetChanged();
    }

    public Musique getCurrentMusique(View view, int position){
        return mMusiques.get(position);
    }

}
