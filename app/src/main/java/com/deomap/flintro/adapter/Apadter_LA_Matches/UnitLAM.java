package com.deomap.flintro.adapter.Apadter_LA_Matches;

public class UnitLAM {
    private String userName;
    private boolean liked;
    private String uID;


    public UnitLAM(String userName){
        this.userName = userName;
        this.liked=true;
    }
    public String getUserName(){
        return this.userName;
    }
    public void setLiked(boolean liked) {
        this.liked = liked;
    }
    public String getuID(){return this.uID;}

    public boolean getLiked() {
        return liked;
    }
}
