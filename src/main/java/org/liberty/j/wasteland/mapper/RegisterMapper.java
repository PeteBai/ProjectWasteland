package org.liberty.j.wasteland.mapper;

import org.apache.ibatis.annotations.*;
import org.liberty.j.wasteland.entity.DoctorBean;
import org.liberty.j.wasteland.entity.PatientBean;

import java.util.List;

@Mapper
public interface RegisterMapper {
    @Select("select * from staff where left(sID, 1)=\'D\'")
    List<DoctorBean> getAllDocs();

    @Insert("insert into clinic_record value(#{CID}, #{pnid}, #{currState})")
    int insertClinicRecord(@Param("CID") String cid, @Param("pnid") String pnid, @Param("currState") String initState);

    @Update("update reserved_patient set R_Status=\'AT\' where R_Reserve_number=#{resvid}")
    int updateReservationStatus(@Param("resvid") String resvid);

    @Update("update clinic_record set Curr_State=\'ON_PAR\' where CID=#{cid}")
    int updateClinicStatus(@Param("cid") String cid);

    @Select("select patient.pID, pName from patient, clinic_record where patient.pID=clinic_record.pID and CID=#{cid}")
    List<PatientBean> getNextPatient(@Param("cid") String cid);
}
