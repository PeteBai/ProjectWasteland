package org.liberty.j.wasteland.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.liberty.j.wasteland.Exception.Result;
import org.liberty.j.wasteland.assistant.PaymenCheck;
import org.liberty.j.wasteland.assistant.QueueProcesser;
import org.liberty.j.wasteland.entity.AnaTableBean;
import org.liberty.j.wasteland.entity.MedRetBean;
import org.liberty.j.wasteland.entity.MedicineTableBean;
import org.liberty.j.wasteland.entity.ReservationBean;
import org.liberty.j.wasteland.service.FinanceService;
import org.liberty.j.wasteland.service.MedicineService;
import org.liberty.j.wasteland.service.RegisterService;
import org.liberty.j.wasteland.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private MedicineService ms;

    private HashMap<String, Integer> getDayInt = new HashMap<String, Integer>();

    @ApiOperation(value = "获取挂号付款链接", notes = "用户的各种信息会附加在获取的链接上")
    @RequestMapping(value = "/getPaymentLink", method = RequestMethod.POST)
    public Result getPaymentLink(HttpServletResponse response,
                                 @RequestParam(value = "pid", required = true) String pnid,
                                 @RequestParam(value = "sid", required = true) String sid,
                                 @RequestParam(value = "isReserved", required = true) Boolean isReserved,
                                 @RequestParam(value = "resvid", required = false) String resvid) throws Exception
    {
        Double fee = fs.getDocFee(sid);
        String ret = "localhost:8080/finance/onClickBait1?pid="+pnid+"&sid="+sid+"&fee="+fee.toString();
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
            getDayInt.put("星期一", 2);
            getDayInt.put("星期二", 3);
            getDayInt.put("星期三", 4);
            getDayInt.put("星期四", 5);
            getDayInt.put("星期五", 6);
            getDayInt.put("星期六", 7);
            getDayInt.put("星期日", 1);
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

    @ApiOperation(value = "就诊后查询所有待付款项目", notes = "包括本次就诊所有未付款的检验单和领药单")
    @RequestMapping(value = "/getAllUnpaidItems", method = RequestMethod.GET)
    public Result getAllUnpaidItems(@RequestParam("cid") String cid)
    {
        HashMap<String, Object[]> ret = fs.getUnpaidItems(cid);
        return new Result(true, 200, "", ret);
    }

    @ApiOperation(value = "就诊后获取待付款项目链接", notes = "选择要付款的单据,系统返回付款链接")
    @RequestMapping(value = "/getItemsPayLink", method = RequestMethod.POST)
    public Result getItemsPayLink(@RequestParam("cid") String cid,
                                  @RequestBody Map<String, String[]> payList)
    {
        double sum = 0;
        // 分配付款任务ID
        String psu = UUID.randomUUID().toString();
        //按照数组内容查询是否存在单子
        if(payList.containsKey("领药单"))
        {
            if(payList.get("领药单").length != 0)
            {
                for(String item:payList.get("领药单"))
                {
                    MedicineTableBean mtb = fs.getSpecificMedTable(item);
                    if(mtb == null)
                        return new Result(false, 200, "领药单不存在!");
                    sum += mtb.getMtFee();
                }
                if(!PaymenCheck.addPayIDToMed(psu, payList.get("领药单")))
                    return new Result(false, 200, "您还存在未付款的链接,无法生成新付款链接!");
            }
        }
        //按照单子内容计算总价
        if(payList.containsKey("检验单"))
        {
            if(payList.get("检验单").length != 0)
            {
                for(String s:payList.get("检验单"))
                {
                    AnaTableBean atb = fs.getSpecificAnaTable(s);
                    if(atb == null)
                        return new Result(false, 200, "检验单不存在!");
                    sum += atb.gettFee();
                }
                if(!PaymenCheck.addPayIDToAna(psu, payList.get("检验单")))
                    return new Result(false, 200, "您还存在未付款的链接,无法生成新付款链接!");
            }
        }
        //生成付款链接
        String ret = "localhost:8080/finance/onClickBait2?payID=" + psu;
        return new Result(true, 200, "", ret);
    }

    @ApiOperation(value = "就诊后分项付款链接", notes = "由程序生成,由用户点击")
    @RequestMapping(value = "/onClickBait2", method = RequestMethod.GET)
    public Result confirmPayment2(@RequestParam("payID") String payID)
    {
        String[] medList = PaymenCheck.rmPayIDInMed(payID);
        if(medList != null)
        {
            for (String s : medList)
            {
                if (!fs.updateMedTableState(s, "已缴费 未领药"))
                    return new Result(false, 200, "m请联系工作人员寻求帮助");
            }
        }
        String[] anaList = PaymenCheck.rmPayIDInAna(payID);
        if(anaList != null)
        {
            for(String s : anaList)
            {
                if(!fs.updateAnaTableState(s, "已缴费 未检查"))
                    return new Result(false, 200, "a请联系工作人员寻求帮助");
            }
        }
        return new Result(true, 200, "付款成功");
    }

    @ApiOperation(value = "退药费", notes = "需要一个界面")
    @RequestMapping(value = "/medicineRefund", method = RequestMethod.POST)
    public Result medicineRefund(@RequestParam("pid") String pnid, @RequestParam("mrid") String mrid)
    {
        //获取退药单信息
        MedRetBean mrb = ms.querySpecificMedRetTable(mrid);
        if(mrb == null)
            return new Result(false, 200, "请检查退药单是否存在!");
        //比对用户信息
        if(pnid.compareTo(mrb.getpID()) != 0)
            return new Result(false, 200, "退药单用户信息不匹配!");
        //检查退药单状态
        if("未退费".compareTo(mrb.getMrStatus()) != 0)
            return new Result(false, 200, "退药单已经完成!");
        //更新退药单状态
        if(!ms.finishMedRet(mrid))
            return new Result(false, 200, "退药失败, 请联系工作人员寻求帮助");
        //财务系统退费
        return new Result(true, 200, "退费"+((Double) mrb.getMrFee()).toString()+"元成功!", mrb.getMrFee());
    }

    @ApiOperation(value = "退费", notes = "退未检验/未领药的费用,已经领药的去退药费那里,目前设计的是只能退本次治疗的东西")
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public Result refund(@RequestParam("cid") String cid, @RequestBody Map<String, String[]> retBody)
    {
        double retAmount = 0;
        String ret = "";
        boolean res = true;
        //查询领药单
        if(retBody.containsKey("领药单"))
        {
            ret += "领药单退费情况: ";
            for(String s:retBody.get("领药单"))
            {
                MedicineTableBean mtb = fs.getSpecificMedTable(s);
                if(mtb == null) {
//                    return new Result(false, 200, "该领药单不存在!");
                    ret += "该领药单不存在!";
                    res = false;
                }
                if(mtb.getMtStatus().compareTo("已缴费 未领药") == 0)
                {
                    retAmount += mtb.getMtFee();
                    if(!fs.updateMedTableState(s, "已退费")){
//                        return new Result(false, 200, "退药费失败, 请联系工作人员寻求帮助");
                        ret +="退药费失败, 请联系工作人员寻求帮助";
                        res = false;
                    }
                }
                else
                {
//                    return new Result(false, 200, "该领药单已经被使用过,无法退费!");
                    ret += "该领药单已经被使用过,无法退费!";
                    res = false;
                }
            }
            ret += "领药单退费"+retAmount+"元.";
        }
        //查询检验单
        if(retBody.containsKey("检验单"))
        {
            ret += "检验单退费情况: ";
            for(String s:retBody.get("检验单"))
            {
                AnaTableBean atb = fs.getSpecificAnaTable(s);
                if(atb == null){
//                    return new Result(false, 200, "该检验单不存在!");
                    ret += "该检验单不存在!";
                    res = false;
                }
                if(atb.gettStatus().compareTo("已缴费 未检查") == 0)
                {
                    retAmount += atb.gettFee();
                    if(!fs.updateAnaTableState(s, "已退费")) {
//                        return new Result(false, 200, "退检查费失败, 请联系工作人员寻求帮助");
                        ret += "退检查费失败, 请联系工作人员寻求帮助";
                        res = false;
                    }
                }
                else
                {
//                    return new Result(false, 200, "该检验单已经被使用过,无法退费!");
                    ret += "该检验单已经被使用过,无法退费!";
                    res = false;
                }
            }
            ret += "检验单退费"+retAmount+"元.";
        }
        //计算费用完成退费
        return new Result(res, 200, ret, retAmount);
    }

}
