package org.liberty.j.wasteland.assistant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.liberty.j.wasteland.entity.DoctorBean;
import org.liberty.j.wasteland.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.*;

public class QueueProcesser {
    static HashMap<String, HashMap<String, Queue<String>>> theQueue = new HashMap<String, HashMap<String, Queue<String>>>();
    public static String readJsonFile(InputStream jsonFileStream)
    {
        String jsonStr = "";
        try {
//            File jsonFile = new File(fileName);
//            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(jsonFileStream,"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
//            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    static
    {
        //初始化,查询出所有医生
//        String docs = "src/main/resources/static/doctors.json";
        ClassPathResource cpr = new ClassPathResource("doctors.json");
        String jsonStr = null;
        try {
            jsonStr = QueueProcesser.readJsonFile(cpr.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jobj = JSON.parseObject(jsonStr);
        List<String> allDocs = (List<String>) jobj.get("doctors");
        for(String s_db : allDocs)
        {
            HashMap<String, Queue<String>> s_temp = new HashMap<String, Queue<String>>();
            s_temp.put("NORM", new LinkedList<String>());
            s_temp.put("PRIO", new LinkedList<String>());
            theQueue.put(s_db, s_temp);
        }
    }

    public static boolean joinIn(String sID, String resvID, String state)
    {
        if(theQueue.containsKey(sID))
        {
            if(theQueue.get(sID).containsKey(state))
            {
                boolean ret = theQueue.get(sID).get(state).add(resvID);
                return ret;
            }
            else return false;
        }
        else return false;
    }

    public static String callFor(String sID)
    {
        if(theQueue.containsKey(sID))
        {
            if(!theQueue.get(sID).get("PRIO").isEmpty())
            {
                String ret = theQueue.get(sID).get("PRIO").poll();
                return ret;
            }
            else if(!theQueue.get(sID).get("NORM").isEmpty())
            {
                String ret = theQueue.get(sID).get("NORM").poll();
                return ret;
            }
            else return "$DOC HAS NO WAITING PATIENT";
        }
        else return "$DOC DOES NOT EXIST: "+sID;
    }

    public static int getLen(String sID, String state, String cid)
    {
        if(theQueue.containsKey(sID))
        {
            if(theQueue.get(sID).containsKey(state))
            {
                LinkedList<String> temp = (LinkedList<String>)theQueue.get(sID).get(state);
                if(temp.contains(cid))
                    return temp.indexOf(cid);
                else return -1;
            }
            else return -1;
        }
        else return -1;
    }

    public static int getPriorityLen(String sID)
    {
        return theQueue.get(sID).get("PRIO").size();
    }

    public static int getTotalLen(String sID)
    {
        if(theQueue.containsKey(sID))
        {
            return theQueue.get(sID).get("PRIO").size() + theQueue.get(sID).get("NORM").size();
        }
        else
        {
            return -1;
        }
    }

    public static void main(String[] args) {
//        System.out.println(getLen("D00002", "PRIO"));
    }

}
