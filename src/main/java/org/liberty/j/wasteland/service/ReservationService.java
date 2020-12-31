package org.liberty.j.wasteland.service;

import java.util.List;

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
}
