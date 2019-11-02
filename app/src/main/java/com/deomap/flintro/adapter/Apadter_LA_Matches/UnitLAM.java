package com.deomap.flintro.adapter.Apadter_LA_Matches;

//аналогично UnitLAA

public class UnitLAM {
    private String userName, uID;
    private boolean liked;


    public UnitLAM(String userName, String uID){
        this.userName = userName;
        this.liked=true;
        this.uID = uID;
    }

    public String getUserName(){
        return this.userName;
    }
    public boolean getLiked() {
        return liked;
    }
    public String getuID(){return this.uID;}

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
