#namespace("files")
  #define filesOrderBy()
    #if(orderBy=="999999")
    #else
       ORDER BY tx_type desc,f_type_str,p.create_time desc
    #end
  #end
  #define filesFrom()
select p.*,fsd.d_id,fs.fs_type_option,fsd.d_phone,fsd.d_name,dp.dp_nickname,fs.fs_type_text as fs_type_str,p.f_type_text f_type_str,fo.fo_operate_type_text fo_operate_type_str ,o3.o_value_special2 tx_message,o3.o_value_special3 cs_message,fo.fo_operate_time1,fo.fo_operate_time2,  date_add(fo_operate_time2, interval - ifnull(o3.o_value_special,0) day) tx_time1
, CASE
when fo_operate_time2 < now() and fs.fs_type_text = '在柜'  then 2
when date_add(fo_operate_time2, interval - ifnull(o3.o_value_special,0) day) < now() and fs.fs_type_text = '在柜' then 1
ELSE 0 END as tx_type,
CASE
when fs.fs_type_text = '移交' or fs.fs_type_text = '归档' or fs.fs_type_text = '移送起诉' then 1
ELSE 0 END as complete_state
from sys_files p
 LEFT JOIN (select *from sys_files_state where fs_id in ( select max(fs_id) from sys_files_state group by f_id )) fs on p.f_id = fs.f_id
 LEFT JOIN (select *from sys_files_operate where fo_id in ( select max(fo_id) from sys_files_operate group by f_id )) fo on p.f_id = fo.f_id
 left join fixed_sys_option o3 on o3.o_id=fo.fo_operate_type_option
 LEFT JOIN fixed_sys_directories fsd on fsd.d_id=p.u_id_create
 LEFT JOIN fixed_sys_department dp on fsd.dp_id=dp.dp_id
 where 1=1
     #if(f_type_option)
      AND p.f_type_option = #para(f_type_option)
    #end
     #if(fs_type_option)
      AND fs_type_option = #para(fs_type_option)
    #end
        #if(f_name)
      AND p.f_name LIKE concat('%',#para(f_name),'%')
    #end
        #if(f_suspect)
      AND p.f_suspect LIKE concat('%',#para(f_suspect),'%')
    #end
      #if(f_state=='1')
        AND  (fs.fs_type_text = '移交' or fs.fs_type_text = '归档' or fs.fs_type_text = '移送起诉')
      #end
      #if(f_state=='-1')
           AND  (fs.fs_type_text != '移交' and fs.fs_type_text != '归档' and fs.fs_type_text != '移送起诉')
      #end
        #if(dp_id)
      AND fsd.dp_id = #para(dp_id)
    #end
        #if(qzcs_time1)
      AND fo.fo_operate_time1 > #para(qzcs_time1)
    #end
         #if(qzcs_time2)
      AND fo.fo_operate_time1 < #para(qzcs_time2)
    #end
        #if(tx_time1)
      AND date_add(fo_operate_time2, interval - ifnull(o3.o_value_special,0) day) > #para(tx_time1)
    #end
         #if(tx_time2)
      AND date_add(fo_operate_time2, interval - ifnull(o3.o_value_special,0) day) < #para(tx_time2)
    #end
         #if(my_dp_id)
       AND fsd.dp_id =  #para(my_dp_id)
    #end
    #if(ids)
      and (
        1=11
        #for(x : ids)
           or p.f_id = #(x)
        #end
      )
    #end
    #@filesOrderBy()
  #end


#sql("query")
    SELECT
    #if(tableControlShow)
      #for(x : tableControlShow)
        #(x)
        #if(!for.last)
          ,
        #end
      #end
    #else
      *
    #end
    from (#@filesFrom())asdfghj
    #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryCount")
  SELECT count(*) from (#@filesFrom())asdfghj
  #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryById")
  select f.*,fs.fs_type_option,fs_inout_type_option,fs.fs_inout_type,o2.o_label f_type_str from sys_files f
  LEFT JOIN (select *from sys_files_state where fs_id in ( select max(fs_id) from sys_files_state group by f_id )) fs on f.f_id = fs.f_id
  left join fixed_sys_option o2 on o2.o_id=f.f_type_option
  where f.f_id=?
  #end
 #sql("setDeleteInfo")
  UPDATE sys_files set delete_user = ?,delete_time=now() where  f_id = ?
  #end

  #sql("delete")
  delete from sys_files where f_id = ?
  #end

  #sql("queryByAll")
  select * from sys_files
  #end
  #sql("queryByOvertime")
    SELECT * from (#@filesFrom())asdfghj where tx_type=2 or  tx_type=1
  #end

  #sql("queryByCreate1")
 select o.*  from fixed_sys_option o
  left join  fixed_sys_option f on o.o_id_top = f.o_id
  where f.o_label='出入柜操作' and  o.o_label='进柜'
  #end
  #sql("queryByCreate2")
 select o3.* from sys_files f
LEFT JOIN fixed_sys_option o on o.o_id = f.f_type_option
left join  fixed_sys_option o2 on o2.o_id_top = o.o_id
left join  fixed_sys_option o3 on o3.o_id_top = o2.o_id where o3.o_label='在柜' and f_id=?
  #end
  #sql("queryByExecl1")
  select f.create_time,f.f_name,f.f_suspect,fsd.d_name,dp.dp_nickname from sys_files f
  LEFT JOIN fixed_sys_directories fsd on fsd.d_id=f.u_id_create
  LEFT JOIN fixed_sys_department dp on fsd.dp_id=dp.dp_id where f_id=?
  #end
  #sql("queryByExecl2")
  select* from sys_files_state where f_id=? and fs_inout_type_text='出柜' order by fs_id
  #end
   #sql("queryByExecl3")
  select* from sys_files_state where f_id=? and fs_inout_type_text='进柜' order by fs_id LIMIT 1,9999999999
  #end

#end