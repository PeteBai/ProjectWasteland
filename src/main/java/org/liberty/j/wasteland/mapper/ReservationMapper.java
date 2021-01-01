package org.liberty.j.wasteland.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.liberty.j.wasteland.entity.ArrangementBean;
import org.liberty.j.wasteland.entity.DoctorBean;
import org.liberty.j.wasteland.entity.PatientBean;

@Mapper
public interface ReservationMapper {
    @Select("select work_time_table.eID, eName, eDept, eDesc, eFee, ePosition, eStartTime, eEndTime, eCurrState from work_time_table, employee_table where work_time_table.eID = employee_table.eID")
    List<ArrangementBean> getAllArrangement();

    @Select("select arrangement.sID, sName, sDept, sDesc, sFee, sPosition, docStartTime, docEndTime, docCurrState from arrangement, staff where arrangement.sID = staff.sID and docCurrState = \'OPEN\' and arrangement.sID=#{docname}")
    List<ArrangementBean> getDocArrangement(@Param("docname") String docname);

    @Select("select sID, sName, sDept, sDesc, sFee, sPosition from staff where sDept=#{dept}")
    List<DoctorBean> getDocsFromDept(@Param("dept") String dept);

    @Select("select * from patient")
    List<PatientBean> getAllPatients();

    @Select("select * from patient where pID=#{nid}")
    List<PatientBean> getSpecificPatient(@Param("nid") String nid);

    @Insert("insert into patient values(#{pb.pID}, #{pb.pName}, #{pb.pGender}, #{pb.pAge}, #{pb.pIsMarried}, #{pb.pNationality}, #{pb.pPhoneNumber})")
    int addPatient(@Param("pb") PatientBean pb);

    @Update("update arrangement set docCurrState=\'RESV\', R_ID=#{pnid} where sID=#{sid} and docStartTime=#{stime} and WeekDay=#{wkday}")
    int updateArrangementStat(@Param("pnid") String pnid, @Param("sid") String sid, @Param("stime") String stime, @Param("wkday") Integer wkday);

    @Insert("insert into reserved_patient values(#{pnid}, #{reservedID}, #{sid}, #{docStartTime}, #{weekNum}, #{status})")
    int addReservationRecord(@Param("pnid") String pnid, @Param("reservedID") String resvID, @Param("sid") String sid,
                             @Param("docStartTime") String docStartTime, @Param("weekNum") Integer wkNum,
                             @Param("status") String status);
}
