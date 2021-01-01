package org.liberty.j.wasteland.controller;

import io.swagger.annotations.ApiOperation;
import org.liberty.j.wasteland.Exception.Result;
import org.liberty.j.wasteland.entity.ReservationBean;
import org.liberty.j.wasteland.service.FinanceService;
import org.liberty.j.wasteland.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
@RequestMapping(value = "/finance")
public class FinanceController
{
    @Autowired
    private FinanceService fs;
    @Autowired
    private ReservationService rs;

    @ApiOperation(value = "获取付款链接", notes = "用户的各种信息会附加在获取的链接上")
    @RequestMapping(value = "/getPaymentLink", method = RequestMethod.POST)
    public Result getPaymentLink(HttpServletResponse response,
                                 @RequestParam(value = "pid", required = true) String pnid,
                                 @RequestParam(value = "sid", required = true) String sid,
                                 @RequestParam(value = "isReserved", required = true) Boolean isReserved,
                                 @RequestParam(value = "resvid", required = false) String resvid) throws Exception
    {
        Double fee = fs.getDocFee(sid);
        String ret = "localhost:8080/finance/onClickBait1?pid="+pnid+"&sid="+sid+"fee="+fee.toString();
        if(isReserved && resvid != null)
        {
            ret += "&isReserved=true&resvid=";
            ret += resvid;
        }
        else
        {
            ret += "&isReserved=false";
        }
        return new Result(true, 200, "", ret);
    }

    @ApiOperation(value = "挂号付款链接", notes = "必须由用户点击")
    @RequestMapping(value = "/onClickBait1", method = RequestMethod.GET)
    public Result confirmPayment1(@RequestParam("pid") String pnid, @RequestParam("sid") String sid, @RequestParam("fee") Double fee,
                                  @RequestParam("isReserved") Boolean isReserved,
                                  @RequestParam(value = "resvid",required = false) String resvid) throws Exception
    {
        //首先核对链接上的信息
        if(rs.querySpecificPatient(pnid).size() == 0)
            return new Result(false, 200, "付款失败，用户不存在！");
        Double _fee = fs.getDocFee(sid);
        if(_fee!=fee)
            return new Result(false, 200, "付款失败，金额不符！");
        if(isReserved)
        {
            if(resvid==null)
                return new Result(false, 200, "付款失败，预约信息不符！");
            Boolean _rev = fs.isReservationExists(resvid, pnid);
            if(!_rev)
                return new Result(false, 200, "付款失败，您尚未预约！");
            ReservationBean rb = fs.getReservationDetails(resvid, pnid);
            if(rb.getsID()!=sid)
                return new Result(false, 200, "付款失败，预约信息不符！");
            //现在 我预约了 把我加到挂号表里，换取叫号的码
            // 我需要进入优先队列

        }
        else
        {
            //那么 我没有预约，首先，我需要生成一个排队码
            //我需要加入挂号表
            //我需要进入普通队列
        }
        return new Result(true, 200, "");
    }
}
