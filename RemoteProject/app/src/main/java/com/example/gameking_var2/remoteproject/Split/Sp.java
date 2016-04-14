package com.example.gameking_var2.remoteproject.Split;

/**
 * Created by 孔雀舞 on 2015/9/24.
 */

/*搜尋陣列 自訂類別*/
public class Sp {
    boolean ok = true;
    String[] titleId,all,userName,status,star,correct;
    public Sp(String msg)
    {
        all = msg.split("&");

        titleId = all[0].split(",");
        userName = all[1].split(",");
        status =all[2].split(",");
        star =all[3].split(",");
        correct =all[4].split(",");

    }


    public int getLenght()
    {
        return titleId.length;
    }

    public int getTid(int i)
    {
        return Integer.parseInt(titleId[i]);
    }

    public String getUname(int i)
    {
        return userName[i];
    }

    public String getStatus(int i)
    {
        return status[i];
    }

    public int getStar(int i){return  Integer.parseInt(star[i]);}

    public  int getCorrect(int i){return  Integer.parseInt(correct[i]);}

    public void upnow()
    {
        ok = false;
    }

    public void upend()
    {
        ok = true;
    }

    public boolean start()
    {
        return ok;
    }
}
