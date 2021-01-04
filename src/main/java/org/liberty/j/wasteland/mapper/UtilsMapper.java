package org.liberty.j.wasteland.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.liberty.j.wasteland.entity.AnaTableBean;
import org.liberty.j.wasteland.entity.DoctorBean;

import java.util.List;

@Mapper
public interface UtilsMapper {
    @Select("select * from staff where sid=#{sid}")
    List<DoctorBean> getUserInfo(@Param("sid") String sid);

    @Select("select * from test_table where CID=#{cid}")
    List<AnaTableBean> getAnaTable(@Param("cid") String cid);

}
