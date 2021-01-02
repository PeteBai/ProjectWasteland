package org.liberty.j.wasteland.service;

import org.liberty.j.wasteland.entity.DoctorBean;
import org.liberty.j.wasteland.entity.PatientBean;
import org.liberty.j.wasteland.mapper.RegisterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterService {
    @Autowired
    RegisterMapper rm;

    public List<DoctorBean> getAllDocs()
    {
        return rm.getAllDocs();
    }

    public Boolean insertClinicRecords(String CID, String pnid)
    {
        String stat = "WAITING";
        int result = rm.insertClinicRecord(CID, pnid, stat);
        return (result==0)?false:true;
    }

    public Boolean updateReservationStatus(String resvid)
    {
        int result = rm.updateReservationStatus(resvid);
        return (result==0)?false:true;
    }

    public Boolean updateClinicStatus(String cid)
    {
        int result = rm.updateClinicStatus(cid);
        return (result==0)?false:true;
    }

    public List<PatientBean> getNextPatient(String cid)
    {
        return rm.getNextPatient(cid);
    }

}
