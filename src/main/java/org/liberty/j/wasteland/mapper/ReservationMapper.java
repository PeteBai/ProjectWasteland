package org.liberty.j.wasteland.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.liberty.j.wasteland.entity.ArrangementBean;
import org.liberty.j.wasteland.entity.DoctorBean;
import org.liberty.j.wasteland.entity.PatientBean;

@Mapper
public interface ReservationMapper {
    @Select("select work_time_table.eID, eName, eDept, eDesc, eFee, ePosition, eStartTime, eEndTime, eCurrState from work_time_table, employee_table where work_time_table.eID = employee_table.eID")
    List<ArrangementBean> getAllArrangement();

    @Select("select work_time_table.eID, eName, eDept, eDesc, eFee, ePosition, eStartTime, eEndTime, eCurrState from work_time_table, employee_table where work_time_table.eID = employee_table.eID and eCurrState = \'OPEN\'and eName=#{docname}")
    List<ArrangementBean> getDocArrangement(@Param("docname") String docname);

    @Select("select sID, sName, sDept, sDesc, sFee, sPosition from staff where sDept=#{dept}")
    List<DoctorBean> getDocsFromDept(@Param("dept") String dept);

    @Select("select * from patients_table")
    List<PatientBean> getAllPatients();

    @Select("select * from o_patient where pID=#{nid}")
    List<PatientBean> getSpecificPatient(@Param("nid") String nid);

    @Insert("insert into o_patient values(#{pb.pID}, #{pb.pName}, #{pb.pGender}, #{pb.pNationality}, #{pb.pIsMarried}, #{pb.pPhoneNumber})")
    int addPatient(@Param("pb") PatientBean pb);
}
