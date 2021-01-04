package org.liberty.j.wasteland.service;

import org.liberty.j.wasteland.entity.AnaTableBean;
import org.liberty.j.wasteland.entity.DoctorBean;
import org.liberty.j.wasteland.mapper.FinanceMapper;
import org.liberty.j.wasteland.mapper.UtilsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilsService {
    @Autowired
    private UtilsMapper um;
    @Autowired
    private FinanceMapper fm;

    public DoctorBean getUserInfo(String sid)
    {
        List<DoctorBean> dbl = um.getUserInfo(sid);
        if(dbl.size() == 0)
            return null;
        return dbl.get(0);
    }

    public List<AnaTableBean> getAnaTable(String cid)
    {
        List<AnaTableBean> latb = um.getAnaTable(cid);
        return latb;
    }

    public boolean updateAnaTableStat(String ttid)
    {
        String newState = "已缴费 已检查";
        int res = fm.updateAnaTableStat(newState, ttid);
        return (res==0)?false:true;
    }

}
