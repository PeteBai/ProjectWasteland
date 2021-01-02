package org.liberty.j.wasteland.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.liberty.j.wasteland.entity.ArrangementBean;
import org.liberty.j.wasteland.entity.DoctorBean;
import org.liberty.j.wasteland.entity.ReservationBean;

import java.util.List;

@Mapper
public interface FinanceMapper {
    @Select("select sFee from staff where sID=#{sid}")
    List<DoctorBean> getDocFee(@Param("sid") String sid);

    @Select("select * from reserved_patient where R_Reserve_number=#{resvid} and R_ID=#{pnid}")
    List<ReservationBean> getReservationRec(@Param("resvid") String resvid, @Param("pnid") String pnid);

    @Select("select * from arrangement where sid=#{sid} and docStartTime=#{startTime} and WeekDay=#{wkday}")
    List<ArrangementBean> getSpecificArrangement(@Param("sid") String sid, @Param("startTime") String StartTime, @Param("wkday") Integer wkday);
}
