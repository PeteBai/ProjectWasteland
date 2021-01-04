package org.liberty.j.wasteland.assistant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class MedicineCheck {
    //从文件获取药剂师的名单
    static HashMap<String, List<String>> waitingList = new HashMap<String, List<String>>();
    static List<String> allPhar;
    static {
        String loc = "src/main/java/org/liberty/j/wasteland/static/pharmacists.json";
        String jsonStr = QueueProcesser.readJsonFile(loc);
        JSONObject jobj = JSON.parseObject(jsonStr);
        List<String> allDocs = (List<String>) jobj.get("pharmacists");
        allPhar = allDocs;
        for(String s:allDocs)
        {
            waitingList.put(s, new ArrayList<String>());
        }
    }
    //为每个药剂师建立队列
    //每次配完药都要入队
    static public boolean addNewCensorship(String mtID, String sID)
    {
        if(!waitingList.containsKey(sID))
            return false;
        if(waitingList.get(sID).contains(mtID))
            return false;
        waitingList.get(sID).add(mtID);
        return true;
    }
    //每次完成一个则要出队
    static public boolean removeCensorship(String mtID, String sID)
    {
        if(!waitingList.containsKey(sID))
            return false;
        if(!waitingList.get(sID).contains(mtID))
            return false;
        return waitingList.get(sID).remove(mtID);
    }

    static public String pick_a_pharmacist()
    {
        int len = waitingList.size();
        Random r = new Random();
        String phaSelected = allPhar.get(r.nextInt(len));
        return phaSelected;
    }

    static public List<String> getAllWaitingList(String sid)
    {
        if(!waitingList.containsKey(sid))
            return null;
        return waitingList.get(sid);
    }

}
