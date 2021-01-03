package org.liberty.j.wasteland.controller;

import io.swagger.annotations.ApiOperation;
import org.liberty.j.wasteland.Exception.Result;
import org.liberty.j.wasteland.assistant.QueueProcesser;
import org.liberty.j.wasteland.entity.PatientBean;
import org.liberty.j.wasteland.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/register")
public class RegisterController {
    @Autowired
    private RegisterService rs;
    @ApiOperation(value = "查询前面排队人数", notes = "由手机端发起请求,因此仅限预约用户")

    @RequestMapping(value = "/queryQueueLength", method = RequestMethod.GET)
    public Result queryQLength(@RequestParam(value = "sid") String sid, @RequestParam("cid") String cid)
    {
        int len = QueueProcesser.getLen(sid, "PRIO", cid);
        if(len == -1)
            return new Result(false, 200, "您尚未挂号!");
        return new Result(true, 200, "", len);
    }

    @ApiOperation(value = "普通号查询前面人数", notes = "这个需要先知道医生的ID,而且因为普通号患者没有预约,没法在手机上看")
    @RequestMapping(value = "/queryNormalQLength", method = RequestMethod.GET)
    public Result queryNQLength(@RequestParam("sid") String sid, @RequestParam("cid") String cid)
    {
        int len2 = QueueProcesser.getLen(sid, "NORM", cid);
        if(len2 == -1)
            return new Result(false, 200, "您尚未挂号!");
        int len = QueueProcesser.getPriorityLen(sid) + len2;
        return new Result(true, 200, "", len);
    }

    @ApiOperation(value = "医生叫号", notes = "医生点击下一次时,返回下一个病人的就诊号/身份证号/姓名,但显示的时候不要用就诊号")
    @RequestMapping(value = "/callForNext", method = RequestMethod.GET)
    public Result callForNext(@RequestParam("sid") String sid)
    {
        String next = QueueProcesser.callFor(sid);
        if(next.contains("$"))
            return new Result(false, 200, next);
        //需要更新等待状态
        boolean ir = rs.updateClinicStatus(next);
        //获取病人信息
        List<PatientBean> lpb = rs.getNextPatient(next);
        if(lpb.size() == 0)
            return new Result(false, 200, "出现错误,请联系工作人员", next);
        //打包病人信息
        HashMap<String, String> ret = new HashMap<>();
        ret.put("cID", next);
        ret.put("pName", lpb.get(0).getpName());
        ret.put("pID", lpb.get(0).getpID());
        if(!ir)
            return new Result(false, 200, "出现错误,请手动叫号并联系工作人员", next);
        return new Result(true, 200, "", ret);
    }
}
