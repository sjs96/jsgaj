#namespace("directories")
  #define directoriesOrderBy()
    #if(orderBy=="999999")
    #else
      order by p.create_time desc
    #end
  #end
  #define directoriesFrom()
    select p.*,d.dp_name from fixed_sys_directories p
    left JOIN fixed_sys_department d on d.dp_id = p.dp_id
    where 1=1
    #@directoriesOrderBy()
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
    from (#@directoriesFrom())asdfghj
    #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryCount")
  SELECT count(*) from (#@directoriesFrom())asdfghj
  #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryById")
  select * from fixed_sys_directories where d_id=?
  #end
 #sql("setDeleteInfo")
  UPDATE fixed_sys_directories set delete_user = ?,delete_time=now() where  d_id = ?
  #end

  #sql("delete")
  delete from fixed_sys_directories where d_id = ?
  #end

  #sql("queryByAll")
  select * from fixed_sys_directories
  #end
  #sql("queryByDepartment")
  select * from fixed_sys_directories where 1=1
    #if(my_dp_id)
       AND dp_id =  #para(my_dp_id)
    #end
  #end

#end