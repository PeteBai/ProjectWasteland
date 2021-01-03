package org.liberty.j.wasteland.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.liberty.j.wasteland.Exception.Result;
import org.liberty.j.wasteland.assistant.QueueProcesser;
import org.liberty.j.wasteland.entity.ReservationBean;
import org.liberty.j.wasteland.service.FinanceService;
import org.liberty.j.wasteland.service.RegisterService;
import org.liberty.j.wasteland.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@ApiOperation(value = "财务系统的API")
@RequestMapping(value = "/finance")
public class FinanceController
{
    @Autowired
    private FinanceService fs;
    @Autowired
    private ReservationService rs;
    @Autowired
    private RegisterService regs;

    private HashMap<String, Integer> getDayInt = new HashMap<String, Integer>();

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
        long _fee = Double.doubleToLongBits(fs.getDocFee(sid));
        if(_fee!=Double.doubleToLongBits(fee))
            return new Result(false, 200, "付款失败，金额不符！", _fee);
        if(isReserved)
        {
            if(resvid==null)
                return new Result(false, 200, "付款失败，预约信息不符！");
            Boolean _rev = fs.isReservationExists(resvid, pnid);
            if(!_rev)
                return new Result(false, 200, "付款失败，您尚未预约！");
            ReservationBean rb = fs.getReservationDetails(resvid, pnid);
            if(!rb.getsID().equals(sid))
                return new Result(false, 200, "付款失败，预约信息不符！", sid);
            //现在 我预约了 但我要先检查当前时间跟预约时间
            Integer resvDay = rb.getWeekNum();
            Date today = new Date();
            // 星期的判断，不在同一天到就不行
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            String _resvDay = sdf.format(today);
            getDayInt.put("星期一", 1);
            getDayInt.put("星期二", 2);
            getDayInt.put("星期三", 3);
            getDayInt.put("星期四", 4);
            getDayInt.put("星期五", 5);
            getDayInt.put("星期六", 6);
            getDayInt.put("星期日", 7);
            Integer __resvDay = getDayInt.get(_resvDay);
            if(__resvDay.intValue() != resvDay.intValue())
                return new Result(false, 200, "付款失败，预约信息不符！", resvDay);
            // 当天到达
            String resvTime = rb.getDocStartTime();
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            Long _resvTime = sdf.parse(("2000-03-13 "+resvTime)).getTime();
            Long _now = sdf.parse(("2000-03-13 "+sdf2.format(today))).getTime();
            Integer weekDay = rb.getWeekNum();
            String resvEndTime = fs.queryDocSpecificArrangement(sid, resvTime, weekDay).getDocEndTime();
            Long _resvEndTime = sdf.parse(("2000-03-13 "+resvEndTime)).getTime();
            //按时到达
            if(_now >= _resvTime && _now <= _resvEndTime)
            {
                // 把我加到挂号表里
                boolean ret = regs.insertClinicRecords(resvid, pnid);
                if(!ret)
                    return new Result(false, 200, "请再试一次");
                // 换取叫号的码
                // 我需要进入优先队列
                ret = QueueProcesser.joinIn(sid, resvid, "PRIO");
                if(!ret)
                    return new Result(false, 200, "请联系工作人员寻求帮助");
                // 我需要更改预约的状态
                ret = regs.updateReservationStatus(resvid);
                if(!ret)
                    return new Result(false, 200, "请联系工作人员寻求帮助");
                return new Result(true, 200, "挂专家号成功", resvid);
            }
            else
            {
                //未按时到达 加到挂号表里
                boolean ret = regs.insertClinicRecords(resvid, pnid);
                if(!ret)
                    return new Result(false, 200, "请再试一次");
                //进入普通队列
                ret = QueueProcesser.joinIn(sid, resvid, "NORM");
                if(!ret)
                    return new Result(false, 200, "请联系工作人员寻求帮助");
                //修改预约状态
                ret = regs.updateReservationStatus(resvid);
                if(!ret)
                    return new Result(false, 200, "请联系工作人员寻求帮助");
                return new Result(true, 200, "挂科室号成功", resvid);
            }

        }
        else
        {
            //那么 我没有预约，首先，我需要生成一个排队码
            String cid = UUID.randomUUID().toString();
            //我需要加入挂号表
            boolean ret = regs.insertClinicRecords(cid, pnid);
            if(!ret)
                return new Result(false, 200, "请再试一次");
            //我需要进入普通队列
            ret = QueueProcesser.joinIn(sid, cid, "NORM");
            if(!ret)
                return new Result(false, 200, "请联系工作人员寻求帮助");
            return new Result(true, 200, "挂科室号成功", cid);
        }
    }
}
