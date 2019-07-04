#namespace("set")
  #define setOrderBy()
    #if(orderBy=="999999")
    #else
      order by ff.o_label, o.o_order desc
    #end
  #end
  #define setFrom()
    select ff.o_label as f_label,o.o_label ,o.o_value_special,o.o_value_special4,o.o_id from fixed_sys_option o
    left join  fixed_sys_option f on o.o_id_top = f.o_id
    left join  fixed_sys_option ff on f.o_id_top = ff.o_id
    where (f.o_label='强制措施' or f.o_label='行政处罚决定') and o.o_state=1
    #@setOrderBy()
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
    from (#@setFrom())asdfghj
    #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryCount")
  SELECT count(*) from (#@setFrom())asdfghj
  #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryById")
  select * from fixed_sys_option where o_id=?
  #end
 #sql("setDeleteInfo")
  UPDATE fixed_sys_option set delete_user = ?,delete_time=now() where  o_id = ?
  #end

  #sql("delete")
  delete from fixed_sys_option where o_id = ?
  #end

  #sql("queryByAll")
  select ff.o_label,o.o_label as label,o.o_value_special from fixed_sys_option o
  left join  fixed_sys_option f on o.o_id_top = f.o_id
  left join  fixed_sys_option ff on f.o_id_top = ff.o_id
  where f.o_label='强制措施' and o.o_state=1 order by ff.o_label, o.o_order desc
  #end

#end