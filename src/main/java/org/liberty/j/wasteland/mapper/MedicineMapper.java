package org.liberty.j.wasteland.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
}
