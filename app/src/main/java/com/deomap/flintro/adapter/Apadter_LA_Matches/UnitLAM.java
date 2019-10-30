package com.deomap.flintro.adapter.Apadter_LA_Matches;

public class UnitLAM {
    private String userName;
    private boolean liked;
    private String uID;


    public UnitLAM(String userName, String uID){
        this.userName = userName;
        this.liked=true;
        this.uID = uID;
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
