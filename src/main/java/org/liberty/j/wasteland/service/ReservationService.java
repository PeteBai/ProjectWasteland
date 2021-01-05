package org.liberty.j.wasteland.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.liberty.j.wasteland.entity.*;
import org.liberty.j.wasteland.mapper.FinanceMapper;
import org.liberty.j.wasteland.mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    @Autowired
    private ReservationMapper rm;
    @Autowired
    private FinanceService fs;
    @Autowired
    private FinanceMapper fm;
    @Autowired
    private MedicineService ms;
    public List<ArrangementBean> queryAllReservation() throws Exception
    {
        return rm.getAllArrangement();
    }

    public List<ArrangementBean> queryDocAvailableTime(String docname) throws Exception
    {
        return rm.getDocArrangement(docname);
    }

    public List<DoctorBean> queryDocsFromDept(String dept) throws Exception
    {
        return rm.getDocsFromDept(dept);
    }

    public List<PatientBean> getAllPatients()
    {
        return rm.getAllPatients();
    }

    public List<PatientBean> querySpecificPatient(String nid) throws Exception
    {
        return rm.getSpecificPatient(nid);
    }

    public boolean addNewPatient(PatientBean pb) throws Exception
    {
        Integer addResult = rm.addPatient(pb);
        boolean ret = (addResult==0)?false:true;
        return ret;
    }

    public boolean queryIsReserved(String sID, String sTime, Integer wkDay)
    {
        List<ArrangementBean> lab = rm.getCertainArrangement(sID, sTime, wkDay);
        if(lab.size() == 0)
            return true;
        if(lab.get(0).getDocCurrState().compareTo("RESV") == 0)
            return true;
        return false;
    }

    public Map<Boolean, String> sumbitReservation(String pnid, String sid, Integer wkday, String stime) throws Exception
    {
        String resvID = UUID.randomUUID().toString();
        Integer insertResult = rm.addReservationRecord(pnid, resvID, sid, stime, wkday, "NOT_AT");
        Integer updateResult = rm.updateArrangementStat(pnid, sid, stime, wkday);
        //目前这里先用UUID，叫号的时候根据UUID查询姓名和身份证号
        Map<Boolean, String> falseRet = new HashMap<Boolean, String>();
        falseRet.put(false, "");
        Map<Boolean, String> trueRet = new HashMap<Boolean, String>();
        trueRet.put(true, resvID);
        if((updateResult == 0) || (insertResult == 0))
            return falseRet;
        return trueRet;
    }

    public HashMap<String, List<MedicineTableBean>> queryAllMedTableByUser(String pnid)
    {
        //先查询用户的就诊记录
        List<ClinicRecordBean> crb = rm.queryCIDByUser(pnid);
        HashMap<String, List<MedicineTableBean>> ret = new HashMap<String, List<MedicineTableBean>>();
        if(crb.size() == 0)
            return null;
        //根据每个就诊ID查到所有的领药单ID
        for(ClinicRecordBean x:crb)
        {
            List<MedicineTableBean> temp = ms.getAllTableFromID(x.getCID());
            if(temp.size() > 0)
            {
                ret.put(x.getCID(), temp);
            }
        }
        return ret;
    }

    public HashMap<String, List<AnaTableBean>> queryAllAnaTableByUser(String pnid)
    {
        List<ClinicRecordBean> crb = rm.queryCIDByUser(pnid);
        HashMap<String, List<AnaTableBean>> ret = new HashMap<String, List<AnaTableBean>>();
        if(crb.size() == 0)
            return null;
        for(ClinicRecordBean x:crb)
        {
            List<AnaTableBean> temp = rm.queryTestTableByCID(x.getCID());
            if(temp.size() > 0)
            {
                ret.put(x.getCID(), temp);
            }
        }
        return ret;
    }

    public List<MedRetBean> queryAllMedRetByUser(String pnid) { return rm.queryMedRetTableByPID(pnid); }

    public List<ClinicRecordBean> queryClinicRec(String pnid){ return rm.queryCIDByUser(pnid); }

    public List<ReservationBean> queryReservationsByUser(String pnid) { return rm.queryReservationByUser(pnid); }
}
