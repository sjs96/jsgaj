#namespace("filesOperate")
  #define filesOperateOrderBy()
    #if(orderBy=="999999")
    #else
      order by p.create_time desc
    #end
  #end
  #define filesOperateFrom()
    select p.* from sys_files_operate p
    where 1=1
    #@filesOperateOrderBy()
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
    from (#@filesOperateFrom())asdfghj
    #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryCount")
  SELECT count(*) from (#@filesOperateFrom())asdfghj
  #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryById")
  select * from sys_files_operate where fo_id=?
  #end
 #sql("setDeleteInfo")
  UPDATE sys_files_operate set delete_user = ?,delete_time=now() where  fo_id = ?
  #end
 #sql("queryByLog")
  select p.fo_operate_time1,p.fo_operate_time2, o.o_label,p.create_time from sys_files_operate p
  left join fixed_sys_option o on o.o_id=p.fo_operate_type_option
  where p.f_id=? ORDER BY fo_id desc
  #end
  #sql("delete")
  delete from sys_files_operate where fo_id = ?
  #end

  #sql("queryByAll")
  select * from sys_files_operate
  #end


#end