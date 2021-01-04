package org.liberty.j.wasteland.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.liberty.j.wasteland.entity.*;

import java.util.List;

@Mapper
public interface FinanceMapper {
    @Select("select sFee from staff where sID=#{sid}")
    List<DoctorBean> getDocFee(@Param("sid") String sid);

    @Select("select * from reserved_patient where R_Reserve_number=#{resvid} and R_ID=#{pnid}")
    List<ReservationBean> getReservationRec(@Param("resvid") String resvid, @Param("pnid") String pnid);

    @Select("select * from arrangement where sid=#{sid} and docStartTime=#{startTime} and WeekDay=#{wkday}")
    List<ArrangementBean> getSpecificArrangement(@Param("sid") String sid, @Param("startTime") String StartTime, @Param("wkday") Integer wkday);

    @Select("select * from medicine_table where CID=#{cid} and left(mtStatus, 3)=\'未缴费\'")
    List<MedicineTableBean> getAllUnpaidMedTable(@Param("cid") String cid);

    @Select("select * from test_table where CID=#{cid} and left(tStatus, 3)=\'未缴费\'")
    List<AnaTableBean> getAllUnpaidAnaTable(@Param("cid") String cid);

    @Select("select * from medicine_table where mtID=#{mtid}")
    List<MedicineTableBean> getSpecificMedTable(@Param("mtid") String mtid);

    @Select("select * from test_table where ttID=#{ttid}")
    List<AnaTableBean> getSpecificAnaTable(@Param("ttid") String ttid);

    @Update("update medicine_table set mtStatus=#{newState} where mtID=#{taid}")
    int updateMedTableStat(@Param("newState") String newState, @Param("taid") String taid);

    @Update("update test_table set tStatus=#{newState} where ttID=#{taid}")
    int updateAnaTableStat(@Param("newState") String newState, @Param("taid") String taid);
}
