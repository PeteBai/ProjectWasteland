package org.liberty.j.wasteland.service;

import org.liberty.j.wasteland.entity.MedicineBean;
import org.liberty.j.wasteland.entity.MedicineTableBean;
import org.liberty.j.wasteland.mapper.MedicineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicineService {
    @Autowired
    private MedicineMapper mm;

    public List<MedicineTableBean> getAllTableFromID(String cid)
    {
        return mm.getAllMedTable(cid);
    }

    public List<MedicineBean> getMedFromTable(String mtid)
    {
        return mm.getMedicineFromTable(mtid);
    }
}
