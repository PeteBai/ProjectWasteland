package org.liberty.j.wasteland.controller;

import io.swagger.annotations.ApiOperation;
import org.liberty.j.wasteland.Exception.Result;
import org.liberty.j.wasteland.entity.AnalysisBean;
import org.liberty.j.wasteland.entity.MedicineBean;
import org.liberty.j.wasteland.service.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
@ApiOperation(value = "治疗过程的API")
@RequestMapping("/treatment")
public class TreatmentController {
    @Autowired
    private TreatmentService ts;
    @ApiOperation(value = "获取所有药品信息", notes = "用于医生开领药单时选取")
    @RequestMapping(value = "/getAllMedicine", method = RequestMethod.GET)
    public Result getAllMedicine()
    {
        List<MedicineBean> lmb = ts.getAllMedicine();
        return new Result(true, 200, "", lmb);
    }

    @ApiOperation(value = "获取检验项目信息", notes = "如果传入部门,就只获取部门的,否则获取所有的")
    @RequestMapping(value = "/getAnalysisItems", method = RequestMethod.GET)
    public Result getAnalysisItems(@RequestParam(value = "dept", required = false) String dept)
    {
        if(dept == null)
        {
            List<AnalysisBean> lab = ts.getAllAnalysis();
            return new Result(true, 200, "", lab);
        }
        else
        {
            List<AnalysisBean> lab = ts.getAnaByDept(dept);
            return new Result(true, 200, "", lab);
        }
    }

    @ApiOperation(value = "提交领药单", notes = "以键-药品ID, 值-数量的形式提交药品列表,成功返回领药单ID")
    @RequestMapping(value = "/submitMedicineTable", method = RequestMethod.POST)
    public Result submitMedicineTable(@RequestParam(value = "sid", required = true) String sid,
                                      @RequestParam(value = "cid", required = true) String cid,
                                      @RequestBody Map<String, Integer> medList)
    {
        //拆解键值对数组
        HashMap<String, Integer> _medList = (HashMap<String, Integer>)medList;
        //获取当前时间
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String _now = sdf.format(now);
        //生成领药单ID
        String _medID = UUID.randomUUID().toString();
        //生成当前状态
        String _currState = "未缴费 未领药";
        //查询数据库以计算领药单费用
        double _fee = ts.calcMedTotalFee(_medList);
        //提交领药单到数据库
        boolean succ = ts.submitMedTable(sid, _medID, cid, _now, _fee, _currState);
        if(!succ)
            return new Result(false, 200, "出现问题,请联系工作人员");
        //提交药品项到数据库
        succ = ts.submitMedList(_medList, sid, _medID);
        if(!succ)
            return new Result(false, 200, "出现问题,请联系工作人员");
        return new Result(true, 200, "", _medID);
    }

    @ApiOperation(value = "提交检验单", notes = "提交字符数组(检验项目ID),成功返回检验单ID")
    @RequestMapping(value = "/submitAnalysisTable", method = RequestMethod.POST)
    public Result submitAnaTable(@RequestParam(value = "sid", required = true) String sid,
                                 @RequestParam(value = "cid", required = true) String cid,
                                 @RequestBody String[] anaList)
    {
        //获取当前时间
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String _now = sdf.format(now);
        //生成检验单ID
        String _anaID = UUID.randomUUID().toString();
        //生成当前状态
        String _currState = "未缴费 未检验";
        //查询数据库以计算检验单费用
        double _fee = ts.calcAnaTotalFee(anaList);
        //提交检验单到数据库
        boolean succ = ts.submitAnaTable(sid, _anaID, cid, _now, _fee, _currState);
        if(!succ)
            return new Result(false, 200, "出现问题,请联系工作人员");
        //提交检验项目到数据库
        succ = ts.submitAnaItem(sid, _anaID, anaList);
        if(!succ)
            return new Result(false, 200, "出现问题,请联系工作人员");
        return new Result(true, 200, "", _anaID);
    }

}
