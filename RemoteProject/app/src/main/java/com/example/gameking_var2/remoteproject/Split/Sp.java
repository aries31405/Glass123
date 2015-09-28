package com.example.gameking_var2.remoteproject.Split;

/**
 * Created by 孔雀舞 on 2015/9/24.
 */

/*搜尋陣列 自訂類別*/
public class Sp {
    boolean ok = true;
    int count = 0;
    String[] titleId,latitude,longitude,all,userName,newtitleId,star,correct;
    public Sp(String msg)
    {
        all = msg.split("&");

        titleId = all[0].split(",");
        userName = all[1].split(",");
        latitude =all[2].split(",");
        longitude =all[3].split(",");
        star =all[4].split(",");
        correct =all[5].split(",");

        newtitleId = new String[titleId.length];
    }


    public void remandadd(int i,int ii)
    {
        newtitleId[i] = titleId[ii];

        for(int iii = ii;iii < titleId.length;iii++)
        {
            if((iii+1) == titleId.length)
            {
                titleId[iii] = "";
                titleId[iii] = "";
                userName[iii] = "";
                latitude[iii] ="";
                longitude[iii] ="";
                star[iii] ="";
                correct[iii] ="";
                count = count+1;
            }
            else
            {
                titleId[iii] = titleId[iii+1];
                userName[iii] = userName[iii+1];
                latitude[iii] =latitude[iii+1];
                longitude[iii] =longitude[iii+1];
                star[iii] = star[iii+1];
                correct[iii] = correct[iii+1];
            }
        }
    }

    public int getLenght()
    {
        return (titleId.length-count);
    }

    public int getTid(int i)
    {
        return Integer.parseInt(titleId[i]);
    }

    public String getUname(int i)
    {
        return userName[i];
    }

    public Double getX(int i)
    {
        return Double.parseDouble(latitude[i]);
    }

    public Double  getY(int i)
    {
        return Double.parseDouble(longitude[i]);
    }

    public String getNewTid(int i)
    {
        return newtitleId[i];
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
