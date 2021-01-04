package org.liberty.j.wasteland.service;

import org.liberty.j.wasteland.entity.AnaTableBean;
import org.liberty.j.wasteland.entity.ArrangementBean;
import org.liberty.j.wasteland.entity.MedicineTableBean;
import org.liberty.j.wasteland.entity.ReservationBean;
import org.liberty.j.wasteland.mapper.FinanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class FinanceService {
    @Autowired
    private FinanceMapper fm;

    public Double getDocFee(String sid)
    {
        return fm.getDocFee(sid).get(0).getsFee();
    }

    public Boolean isReservationExists(String resvid, String pnid)
    {
        List<ReservationBean> lrb = fm.getReservationRec(resvid, pnid);
        if(lrb.size() == 0)
            return false;
        else
            return true;
    }

    public ReservationBean getReservationDetails(String resvid, String pnid)
    {
        return fm.getReservationRec(resvid, pnid).get(0);
    }

    public ArrangementBean queryDocSpecificArrangement(String sid, String startTime, Integer wkDay)
    {
        return fm.getSpecificArrangement(sid, startTime, wkDay).get(0);
    }

    public HashMap<String, Object[]> getUnpaidItems(String cid)
    {
        List<MedicineTableBean> lmtb = fm.getAllUnpaidMedTable(cid);
        List<AnaTableBean> latb = fm.getAllUnpaidAnaTable(cid);
        HashMap<String, Object[]> ret = new HashMap<String, Object[]>();
        ret.put("领药单", lmtb.toArray());
        ret.put("检验单", latb.toArray());
        return ret;
    }

    public MedicineTableBean getSpecificMedTable(String mtid)
    {
        List<MedicineTableBean> lmtb = fm.getSpecificMedTable(mtid);
        if(lmtb.size() == 0)
            return null;
        return lmtb.get(0);
    }

    public AnaTableBean getSpecificAnaTable(String ttid)
    {
        List<AnaTableBean> latb = fm.getSpecificAnaTable(ttid);
        if(latb.size() == 0)
            return null;
        return latb.get(0);
    }

    public boolean updateMedTableState(String taid, String newState)
    {
        int res = fm.updateMedTableStat(newState, taid);
        System.out.println(taid);
        return (res==0)?false:true;
    }

    public boolean updateAnaTableState(String taid, String newState)
    {
        int res = fm.updateAnaTableStat(newState, taid);
        return (res==0)?false:true;
    }


}
