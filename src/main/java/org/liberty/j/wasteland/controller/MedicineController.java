package org.liberty.j.wasteland.controller;

import io.swagger.annotations.ApiOperation;
import org.liberty.j.wasteland.Exception.Result;
import org.liberty.j.wasteland.assistant.MedicineCheck;
import org.liberty.j.wasteland.entity.MedicineBean;
import org.liberty.j.wasteland.entity.MedicineTableBean;
import org.liberty.j.wasteland.service.FinanceService;
import org.liberty.j.wasteland.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/medicine")
public class MedicineController {
    @Autowired
    private MedicineService ms;
    @Autowired
    private FinanceService fs;

    @ApiOperation(value = "配药师获取配药单", notes = "该就诊ID的所有配药单,再从中做出选择")
    @RequestMapping(value = "/getTableFromID", method = RequestMethod.GET)
    public Result getTableFromID(@RequestParam("cid") String cid)
    {
        return new Result(true, 200, "", ms.getAllTableFromID(cid));
    }

    @ApiOperation(value = "查看配药单详情", notes = "包括配药单的信息和里面药品的信息")
    @RequestMapping(value = "/getDetailedTable", method = RequestMethod.GET)
    public Result getDetailedTable(@RequestParam("mtid") String mtid)
    {
        MedicineTableBean mtb = fs.getSpecificMedTable(mtid);
        List<MedicineBean> lmb = ms.getMedFromTable(mtid);
        HashMap<String, Object> ret = new HashMap<String, Object>();
        ret.put("领药单", mtb);
        ret.put("药品单", lmb);
        return new Result(true, 200, "", ret);
    }

    @ApiOperation(value = "配药师提交配药申请", notes = "指定加入等待审核队列,返回选择的药剂师ID")
    @RequestMapping(value = "/submitCensorApplication", method = RequestMethod.GET)
    public Result submitCensorAppl(@RequestParam("mtid") String mtid)
    {
        String phar = MedicineCheck.pick_a_pharmacist();
        boolean succ = MedicineCheck.addNewCensorship(mtid, phar);
        if(!succ)
            return new Result(false, 200, "请核对领药单ID");
        return new Result(true, 200, "", phar);
    }

    @ApiOperation(value = "药剂师待审核列表")
    @RequestMapping(value = "/queryAllAwaitItem", method = RequestMethod.GET)
    public Result awaitCensorAppl(@RequestParam("sid") String sid)
    {
        List<String> awaitList = MedicineCheck.getAllWaitingList(sid);
        if(awaitList == null)
            return new Result(false, 200, "请核对您自己的ID");
        return new Result(true, 200, "", awaitList);
    }

    @ApiOperation(value = "药剂师完成审核", notes = "药剂师选择一个单子进行审核")
    @RequestMapping(value = "/completeOneCensor", method = RequestMethod.GET)
    public Result approveOneCensor(@RequestParam("sid") String sid, @RequestParam("mtid") String mtid)
    {
        boolean res = MedicineCheck.removeCensorship(mtid, sid);
        if(!res)
            return new Result(false, 200, "请核对您自己的ID和领药单ID");
        return new Result(true, 200, "", mtid);
    }

}
