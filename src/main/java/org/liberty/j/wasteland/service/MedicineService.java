package org.liberty.j.wasteland.service;

import org.liberty.j.wasteland.entity.MedRetBean;
import org.liberty.j.wasteland.entity.MedicineBean;
import org.liberty.j.wasteland.entity.MedicineTableBean;
import org.liberty.j.wasteland.mapper.MedicineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//landing action yeah!!!!
@Service
public class MedicineService {
    @Autowired
    private MedicineMapper mm;
    @Autowired
    private FinanceService fs;

    public List<MedicineTableBean> getAllTableFromID(String cid)
    {
        return mm.getAllMedTable(cid);
    }

    public List<MedicineBean> getMedFromTable(String mtid)
    {
        return mm.getMedicineFromTable(mtid);
    }

    public boolean isTablePaid(String mtid)
    {
        MedicineTableBean mtb = fs.getSpecificMedTable(mtid);
        if(mtb.getMtStatus().equals("已缴费 未领药"))
            return true;
        else
            return false;
    }

    public boolean insertMedRetTable(MedRetBean mrb){ return mm.insertMedRetTable(mrb); }

    public boolean finishMedRet(String mrid){ return mm.finishMedRet(mrid); }

    public MedRetBean querySpecificMedRetTable(String mrid)
    {
        List<MedRetBean> lmrb = mm.getSpecificRetTable(mrid);
        if(lmrb.size() == 0)
            return null;
        return lmrb.get(0);
    }
}
