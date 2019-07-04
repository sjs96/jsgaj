#namespace("department")
  #define departmentOrderBy()
    #if(orderBy=="999999")
    #else
      order by p.create_time desc
    #end
  #end
  #define departmentFrom()
    select p.*,f.dp_name as dp_name_f from fixed_sys_department p
    left JOIN fixed_sys_department f on f.dp_id = p.dp_topid
    where 1=1
    #@departmentOrderBy()
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
    from (#@departmentFrom())asdfghj
    #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryCount")
  SELECT count(*) from (#@departmentFrom())asdfghj
  #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryById")
  select * from fixed_sys_department where dp_id=?
  #end
  #sql("queryByName")
  select * from fixed_sys_department where dp_name=?
  #end
 #sql("setDeleteInfo")
  UPDATE fixed_sys_department set delete_user = ?,delete_time=now() where  dp_id = ?
  #end

  #sql("delete")
  delete from fixed_sys_department where dp_id = ?
  #end

  #sql("queryByAll")
  select * from fixed_sys_department
  #end

#end