package org.liberty.j.wasteland.service;

import org.apache.ibatis.annotations.Param;
import org.liberty.j.wasteland.entity.AnalysisBean;
import org.liberty.j.wasteland.entity.HistoryBean;
import org.liberty.j.wasteland.entity.MedicineBean;
import org.liberty.j.wasteland.mapper.TreatmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TreatmentService {
    @Autowired
    TreatmentMapper tm;
    public List<MedicineBean> getAllMedicine()
    {
        return tm.getAllMedicine();
    }

    public List<AnalysisBean> getAllAnalysis()
    {
        return tm.getAllAnalysis();
    }

    public List<AnalysisBean> getAnaByDept(String dept)
    {
        return tm.getAnaByDept(dept);
    }

    public double calcMedTotalFee(HashMap<String, Integer> medList)
    {
        double res = 0.0;
        for(Map.Entry<String, Integer> entry:medList.entrySet())
        {
            MedicineBean mb = tm.queryCertainMedicine(entry.getKey()).get(0);
            res += mb.getMedicinePrice() * entry.getValue();
        }
        return res;
    }

    public boolean submitMedTable(String sid, String mtid, String cid, String mtDate, double mtFee, String mtStatus)
    {
        int res = tm.insertMedTable(sid, mtid, cid, mtDate, mtFee, mtStatus);
        return (res==0)?false:true;
    }

    public boolean submitMedList(HashMap<String, Integer> medList, String sid, String mtID)
    {
        for(Map.Entry<String, Integer> entry:medList.entrySet())
        {
            int res = tm.insertOneMedRecord(sid, mtID, entry.getKey(), entry.getValue());
            if(res == 0)
                return false;
        }
        return true;
    }

    public double calcAnaTotalFee(String[] anaList)
    {
        double res =0.0;
        for(String s : anaList)
            res += tm.queryCertainAnaItem(s).get(0).getTestPrice();
        return res;
    }

    public boolean submitAnaTable(String sid, String ttid, String cid, String tDate, double tFee, String tStatus)
    {
        int res = tm.insertTestTable(sid, ttid, cid, tDate, tFee, tStatus);
        return (res==0)?false:true;
    }

    public boolean submitAnaItem(String sid, String ttid, String[] anaList)
    {
        for(String s : anaList)
        {
            int res = tm.insertOneAnaItem(sid, ttid, s);
            if(res == 0)
                return false;
        }
        return true;
    }

    public List<HistoryBean> getPatientHistory(String pnid)
    {
        return tm.getPatientHistory(pnid);
    }

    public boolean finishTreatment(String cid) { return tm.finishClinicRecord(cid); }

    public boolean submitPatientHistory(HistoryBean hb)
    {
        int res = tm.insertPatientHistory(hb);
        return (res==0)?false:true;
    }

}
