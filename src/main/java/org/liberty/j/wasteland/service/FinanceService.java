package org.liberty.j.wasteland.service;

import org.liberty.j.wasteland.entity.ReservationBean;
import org.liberty.j.wasteland.mapper.FinanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
