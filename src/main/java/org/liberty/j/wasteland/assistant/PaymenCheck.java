package org.liberty.j.wasteland.assistant;

import java.util.HashMap;

public class PaymenCheck {
    static HashMap<String, String[]> medList = new HashMap<String, String[]>();
    static HashMap<String, String[]> anaList = new HashMap<String, String[]>();

    @org.jetbrains.annotations.Nullable
    public static String[] rmPayIDInMed(String payID)
    {
        if(medList.containsKey(payID))
            return medList.remove(payID);
        else
            return null;
    }

    @org.jetbrains.annotations.Nullable
    public static String[] rmPayIDInAna(String payID)
    {
        if(anaList.containsKey(payID))
            return anaList.remove(payID);
        else
            return null;
    }

    //实际上每次只有生成链接的时候才加进来,所以一旦已经存在就说明一个链接已经存在但未付款,需要先付款才能继续生成链接
    public static boolean addPayIDToMed(String payID, String[] payMedItem)
    {
        if(medList.containsKey(payID))
            return false;
        medList.put(payID, payMedItem);
        return true;
    }

    public static boolean addPayIDToAna(String payID, String[] payAnaItem)
    {
        if(anaList.containsKey(payID))
            return false;
        anaList.put(payID, payAnaItem);
        return true;
    }

}
