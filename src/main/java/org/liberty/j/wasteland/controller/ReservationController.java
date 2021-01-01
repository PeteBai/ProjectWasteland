package org.liberty.j.wasteland.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiOperation;
import org.liberty.j.wasteland.Exception.Result;
import org.liberty.j.wasteland.assistant.NumberGenerater;
import org.liberty.j.wasteland.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.liberty.j.wasteland.entity.ArrangementBean;
import org.liberty.j.wasteland.entity.DoctorBean;
import org.liberty.j.wasteland.entity.PatientBean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


@RestController
@CrossOrigin
public class ReservationController extends NumberGenerater
{
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
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
        String localDepts = "src/main/java/org/liberty/j/wasteland/static/departments.json";
        String jsonStr = readJsonFile(localDepts);
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
            return new Result<>(true, 200, "no existing user");
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
        Map<Boolean, String> res = rs.sumbitReservation(pnid, sid, weekday, stime);
        return new Result<>(true, 200, "", res);
    }

}