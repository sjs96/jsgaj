#namespace("filesState")
  #define filesStateOrderBy()
    #if(orderBy=="999999")
    #else
      order by p.create_time desc
    #end
  #end
  #define filesStateFrom()
    select p.* from sys_files_state p
    where 1=1
    #@filesStateOrderBy()
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
  SELECT count(*) from (#@filesStateFrom())asdfghj
  #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryById")
  select * from sys_files_state where fs_id=?
  #end
 #sql("setDeleteInfo")
  UPDATE sys_files_state set delete_user = ?,delete_time=now() where  fs_id = ?
  #end

  #sql("delete")
  delete from sys_files_state where fs_id = ?
  #end

  #sql("queryByAll")
  select * from sys_files_state
  #end
 #sql("queryByLog")
 select p.fs_id,p.fs_inout_time,p.create_time,o.o_label,o2.o_label o_label2 from sys_files_state p
 left join fixed_sys_option o2 on o2.o_id=p.fs_type_option
 left join fixed_sys_option o on o.o_id=p.fs_inout_type_option
 where p.f_id=? order by fs_id desc
  #end
#end