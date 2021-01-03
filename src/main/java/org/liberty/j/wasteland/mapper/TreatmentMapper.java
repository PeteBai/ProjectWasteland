package org.liberty.j.wasteland.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.liberty.j.wasteland.entity.AnalysisBean;
import org.liberty.j.wasteland.entity.MedicineBean;

import java.util.List;

@Mapper
public interface TreatmentMapper {
    @Select("select * from medicine_item")
    List<MedicineBean> getAllMedicine();

    @Select({"select * from medicine_item where medicineID=#{mid}"})
    List<MedicineBean> queryCertainMedicine(@Param("mid") String mid);

    @Select("select * from test_item")
    List<AnalysisBean> getAllAnalysis();

    @Select("select * from test_item where Dept=#{dept}")
    List<AnalysisBean> getAnaByDept(@Param("dept") String dept);

    @Select("select * from test_item where TestItemID=#{tiid}")
    List<AnalysisBean> queryCertainAnaItem(@Param("tiid") String tiid);

    @Insert("insert into medicine_record value(#{sid}, #{mtID}, #{medicineID}, #{Medicine_Number})")
    int insertOneMedRecord(@Param("sid") String sid, @Param("mtID") String mtid,
                           @Param("medicineID") String medid, @Param("Medicine_Number") int mednum);

    @Insert("insert into medicine_table value(#{sid}, #{mtID}, #{cid}, #{mtDate}, #{mtFee}, #{mtStatus})")
    int insertMedTable(@Param("sid") String sid, @Param("mtID") String mtid, @Param("cid") String cid,
                       @Param("mtDate") String mtDate, @Param("mtFee") double mtFee, @Param("mtStatus") String mtStatus);

    @Insert("insert into test_table value(#{sid}, #{ttid}, #{cid}, #{tDate}, #{tFee}, #{tStatus})")
    int insertTestTable(@Param("sid") String sid, @Param("ttid") String ttid,
                        @Param("cid") String cid, @Param("tDate") String tdate,
                        @Param("tFee") double tfee, @Param("tStatus") String tstatus);

    @Insert("insert into test_item_table value(#{sid}, #{ttid}, #{tiid})")
    int insertOneAnaItem(@Param("sid") String sid, @Param("ttid") String ttid, @Param("tiid") String tiid);
}
