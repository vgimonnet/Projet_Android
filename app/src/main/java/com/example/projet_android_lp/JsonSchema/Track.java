package com.example.projet_android_lp.JsonSchema;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Track {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("artist")
    @Expose
    private String artist;

    public Long getId(){return this.id;}
    public void setId(Long id){this.id = id;}

    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

    public String getArtist(){return this.artist;}
    public void setArtist(String artist){this.artist= artist;}
}
