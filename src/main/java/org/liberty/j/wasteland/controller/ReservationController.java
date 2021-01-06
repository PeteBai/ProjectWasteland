package org.liberty.j.wasteland.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.liberty.j.wasteland.Exception.Result;
import org.liberty.j.wasteland.assistant.NumberGenerater;
import org.liberty.j.wasteland.assistant.QueueProcesser;
import org.liberty.j.wasteland.entity.*;
import org.liberty.j.wasteland.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


@RestController
@CrossOrigin
@ApiOperation(value = "预约过程的API")
public class ReservationController
{
    @Autowired
    private ReservationService rs;
    @RequestMapping(value = "/reservation/queryAllDocs", method = RequestMethod.GET)
    public Result queryAllDocs() throws Exception
    {
        List<ArrangementBean> kvp = rs.queryAllReservation();
        Result r = new Result<>(true, 200, "", kvp);
        return r;
    }

    @ApiOperation(value = "查询所有部门")
    @RequestMapping(value = "/reservation/getAllDepts", method = RequestMethod.GET)
    public Result getAllDepts() throws Exception
    {
//        String localDepts = "departments.json";
        ClassPathResource cpr = new ClassPathResource("departments.json");
        String jsonStr = QueueProcesser.readJsonFile(cpr.getInputStream());
        JSONObject jobj = JSON.parseObject(jsonStr);
        Result r = new Result<>(true, 200, "", jobj);
        return r;
    }

    @ApiOperation(value = "查询每个医生的可预约时间")
    @RequestMapping(value = "/reservation/queryDocsAvailable", method = RequestMethod.GET)
    public Result queryDocsAvailable(@RequestParam String docid) throws Exception
    {
        List<ArrangementBean> kvp = rs.queryDocAvailableTime(docid);
        return new Result<>(true, 200, "", kvp);
    }

    @ApiOperation(value = "查询每个部门的医生")
    @RequestMapping(value = "/reservation/queryDocsFromDepts", method = RequestMethod.GET)
    public Result queryDocsFromDepts(@RequestParam String dept) throws Exception
    {
        List<DoctorBean> kvp = rs.queryDocsFromDept(dept);
        return new Result<>(true, 200, "", kvp);
    }

    @ApiOperation(value="查询用户是否在数据库里", notes="如果用户在数据库里应该能够在Data段里看到详细信息")
    @RequestMapping(value = "/reservation/pullPatientInfo", method = RequestMethod.POST)
    public Result querySpecificPatient(HttpServletResponse response, @RequestParam(value="nid", required = true) String nid) throws Exception
    {
        List<PatientBean> lpb = rs.querySpecificPatient(nid);
        if(lpb.size() > 0)
        {
            PatientBean pb = lpb.get(0);
            return new Result<>(true, 200, "User exists", pb);
        }
        else
        {
            return new Result<>(false, 200, "no existing user");
        }
    }

    @ApiOperation(value = "将新用户加入数据库")
    @RequestMapping(value = "/reservation/submitNewPatient", method = RequestMethod.POST)
    public Result submitNewPatient(HttpServletResponse response,
                                   @RequestParam(value = "pid", required = true) String pnid,
                                   @RequestParam(value = "pname", required = true) String pname,
                                   @RequestParam(value = "pgender", required = true) char pgender,
                                   @RequestParam(value = "page", required = true) Integer page,
                                   @RequestParam(value = "pna", required = true) String pna,
                                   @RequestParam(value = "pmar", required = true) Boolean pmar,
                                   @RequestParam(value = "phone", required = true) String phone) throws Exception
    {
        PatientBean pb = new PatientBean(pnid, pname, pgender, page, pna, phone, pmar);
        Boolean res = rs.addNewPatient(pb);
        return new Result<>(res, 200, "");
    }

    @ApiOperation(value = "进行预约")
    @RequestMapping(value = "/reservation/submitReservation", method = RequestMethod.POST)
    public Result submitReservation(HttpServletResponse response,
                                    @RequestParam(value = "pid") String pnid,
                                    @RequestParam(value = "sid") String sid,
                                    @RequestParam(value = "weekday") Integer weekday,
                                    @RequestParam(value = "startTime") String stime) throws Exception
    {
        // 要先查询该时间段是否已经被预约
        if(rs.queryIsReserved(sid, stime, weekday))
            return new Result(false, 200, "该时间段已被预约!");
        Map<Boolean, String> res = rs.sumbitReservation(pnid, sid, weekday, stime);
        return new Result<>(true, 200, "", res);
    }

    @ApiOperation(value = "查询领药单状态", notes = "仅用于手机端,显示所有就诊产生的记录")
    @RequestMapping(value = "/reservation/queryMedTable", method = RequestMethod.POST)
    public Result queryMedTable(@RequestParam("pid") String pnid) throws Exception
    {
        //检查用户是否存在
        List<PatientBean> lpb = rs.querySpecificPatient(pnid);
        if(lpb.size() > 0)
        {
            PatientBean pb = lpb.get(0);
            HashMap<String, List<MedicineTableBean>> allTable = rs.queryAllMedTableByUser(pb.getpID());
            if(allTable == null)
                return new Result<>(false, 200, "用户很健康,未曾就诊!");
            return new Result<>(true, 200, "", allTable);
        }
        else
        {
            return new Result<>(false, 200, "用户不存在!");
        }
    }

    @ApiOperation(value = "查询检验单状态", notes = "仅用于手机端,显示所有就诊产生的记录")
    @RequestMapping(value = "/reservation/queryAnaTable", method = RequestMethod.POST)
    public Result queryAnaTable(@RequestParam("pid") String pnid) throws Exception
    {
        //检查用户是否存在
        List<PatientBean> lpb = rs.querySpecificPatient(pnid);
        if(lpb.size() > 0)
        {
            PatientBean pb = lpb.get(0);
            HashMap<String, List<AnaTableBean>> allTable = rs.queryAllAnaTableByUser(pb.getpID());
            if(allTable == null)
                return new Result<>(false, 200, "用户很健康,未曾就诊!");
            return new Result<>(true, 200, "", allTable);
        }
        else
        {
            return new Result<>(false, 200, "用户不存在!");
        }
    }

    @ApiOperation(value = "查询退药单状态", notes = "仅用于手机端,显示用户的记录")
    @RequestMapping(value = "/reservation/queryMedRetTable", method = RequestMethod.POST)
    public Result queryMedRetTable(@RequestParam("pid") String pnid) throws Exception
    {
        List<PatientBean> lpb = rs.querySpecificPatient(pnid);
        if(lpb.size() > 0)
        {
            PatientBean pb = lpb.get(0);
            List<MedRetBean> allTable = rs.queryAllMedRetByUser(pb.getpID());
            if(allTable.size() == 0)
                return new Result<>(false, 200, "该用户未进行过退药.");
            return new Result<>(true, 200, "", allTable);
        }
        else
        {
            return new Result<>(false, 200, "用户不存在!");
        }
    }

    @ApiOperation(value = "查询就诊记录", notes = "仅用于手机端,显示用户的记录")
    @RequestMapping(value = "/reservation/queryClinicHistory", method = RequestMethod.POST)
    public Result queryClinicHistory(@RequestParam("pid") String pnid) throws Exception
    {
        List<PatientBean> lpb = rs.querySpecificPatient(pnid);
        if(lpb.size() > 0)
        {
            PatientBean pb = lpb.get(0);
            List<ClinicRecordBean> lcrb = rs.queryClinicRec(pb.getpID());
            if(lcrb.size() == 0)
                return new Result<>(false, 200, "用户很健康,未曾就诊!");
            return new Result<>(true, 200, "", lcrb);
        }
        else
        {
            return new Result<>(false, 200, "用户不存在!");
        }
    }

    @ApiOperation(value = "查询预约记录", notes = "仅用于手机端")
    @RequestMapping(value = "/reservation/queryReservationHistory", method = RequestMethod.POST)
    public Result queryReservationHistory(@RequestParam("pid") String pnid) throws Exception
    {
        List<PatientBean> lpb = rs.querySpecificPatient(pnid);
        if(lpb.size() > 0)
        {
            PatientBean pb = lpb.get(0);
            List<ReservationBean> lrb = rs.queryReservationsByUser(pnid);
            if(lrb.size() == 0)
                return new Result<>(false, 200, "用户未曾进行预约!");
            return new Result<>(true, 200, "", lrb);
        }
        else
        {
            return new Result<>(false, 200, "用户不存在!");
        }
    }
}