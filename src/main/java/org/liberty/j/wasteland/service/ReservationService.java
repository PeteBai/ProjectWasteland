package org.liberty.j.wasteland.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.liberty.j.wasteland.mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.liberty.j.wasteland.entity.ArrangementBean;
import org.liberty.j.wasteland.entity.DoctorBean;
import org.liberty.j.wasteland.entity.PatientBean;

@Service
public class ReservationService {
    @Autowired
    private ReservationMapper rm;
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
}
