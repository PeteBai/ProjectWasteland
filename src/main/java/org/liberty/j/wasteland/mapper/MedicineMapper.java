package org.liberty.j.wasteland.mapper;

import org.apache.ibatis.annotations.*;
import org.liberty.j.wasteland.entity.MedRetBean;
import org.liberty.j.wasteland.entity.MedicineBean;
import org.liberty.j.wasteland.entity.MedicineTableBean;

import java.util.List;

@Mapper
public interface MedicineMapper
{
    @Select("select * from medicine_table where CID=#{cid}")
    List<MedicineTableBean> getAllMedTable(@Param("cid") String cid);

    @Select("SELECT medicine_record.medicineID, medicineName, Medicine_Number AS \"count\", Company, MedicinePrice " +
            "FROM medicine_record, medicine_item WHERE medicine_record.medicineID=medicine_item.medicineID AND mtID=#{mtid}")
    List<MedicineBean> getMedicineFromTable(@Param("mtid") String mtid);

    @Insert("insert into medicine_return_table value(#{mtb.sID}, #{mtb.mrID}, #{mtb.pID}, #{mtb.mrDate}, #{mtb.mrFee}, #{mtb.mrStatus})")
    boolean insertMedRetTable(@Param("mtb") MedRetBean mtb);

    @Update("update medicine_return_table set mrStatus=\"已退药\" where mrID=#{mrid}")
    boolean finishMedRet(@Param("mrid") String mrid);

    @Select("select * from medicine_return_table where mrID=#{mrid}")
    List<MedRetBean> getSpecificRetTable(@Param("mrid") String mrid);
}
