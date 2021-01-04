package org.liberty.j.wasteland.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.liberty.j.wasteland.Exception.Result;
import org.liberty.j.wasteland.entity.AnaTableBean;
import org.liberty.j.wasteland.entity.DoctorBean;
import org.liberty.j.wasteland.service.UtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/utils")
public class UtilsController {
    @Autowired
    private UtilsService us;

    @ApiOperation(value = "医院工作人员登录", notes = "前端需要将登录信息存储到session里")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestParam("sid") String sid, @RequestParam("pw") String pw)
    {
        DoctorBean db = us.getUserInfo(sid);
        if(db == null)
            return new Result(false, 200, "用户不存在!");
        if(pw.compareTo(db.getsPassword()) != 0) {
            System.out.println(pw.compareTo(db.getsPassword()));
            return new Result(false, 200, "用户密码错误!");
        }
        return new Result(true, 200, "登录成功!", db);
    }

    @ApiOperation(value = "确认检验完成", notes = "因为没有专门的检验科室的页面,故在这里提供确认检验的接口")
    @RequestMapping(value = "/confirmAnaFinished", method = RequestMethod.GET)
    public Result confirmAnaFinished(@RequestParam("cid") String cid)
    {
        //查询到当此就诊的所有检验单
        List<AnaTableBean> latb = us.getAnaTable(cid);
        //依次设置每个检验单的状态
        for(AnaTableBean atb:latb)
        {
            if(atb.gettStatus().substring(0,3).compareTo("已缴费") == 0)
            {
                //设置
                boolean res = us.updateAnaTableStat(atb.getTtID());
                if(!res)
                    return new Result(false, 200, "更新检验单状态失败,请联系工作人员");
            }
            else
            {
                return new Result(false, 200, "存在未缴费的检验单", atb);
            }
        }
        return new Result(true, 200, "");
    }
}
