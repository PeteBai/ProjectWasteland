package org.liberty.j.wasteland.assistant;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NumberGenerater 
{
    private Date judgeTimeStamp = new Date();
    private SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
    private String judgeDate = formatter.format(judgeTimeStamp);
    private Integer currPriorityCount = 0;
    private Integer currNormalCount = 0;

    public boolean judgeDateChange()
    {
        Date _newDate = new Date();
        String newDate = formatter.format(_newDate);
        if(judgeDate.equals(newDate))
        {
            return false;
        }
        else
        {
            judgeDate = newDate;
            judgeTimeStamp = _newDate;
            currPriorityCount = 0;
            currNormalCount = 0;
            return true;
        }
    }
    public String assignNewPriorityNumber()
    {
        boolean isChanged = judgeDateChange();
        String ret = Integer.toString(currPriorityCount) + "T";
        currPriorityCount += 1;
        System.out.println("Date Changed: "+Boolean.valueOf(isChanged));
        return ret;
    }

    public String assignNewNormalNumber()
    {
        boolean isChanged = judgeDateChange();
        String ret = Integer.toString(currNormalCount);
        currNormalCount += 1;
        System.out.println("Date Changed: "+Boolean.valueOf(isChanged));
        return ret;
    }

    public String getDate()
    {
        return judgeDate;
    }
    public static void main(String[] args) 
    {
        NumberGenerater x = new NumberGenerater();
        System.out.println(x.getDate());
        System.out.println(x.assignNewNormalNumber());
        System.out.println(x.assignNewPriorityNumber());
        System.out.println(x.assignNewNormalNumber());
        System.out.println(x.assignNewPriorityNumber());
    }
}
