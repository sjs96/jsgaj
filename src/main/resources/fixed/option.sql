#namespace("option")
  #define optionOrderBy()
    #if(orderBy=="999999")
    #else
      order by p.create_time desc
    #end
  #end
  #define optionFrom()
    select p.*,f.o_label o_label_f,
     CASE
    when p.o_state = -1 then '无效'
    when p.o_state = 1 then '有效'
     ELSE '未知' END as o_state_str
       from fixed_sys_option p
   left join  fixed_sys_option f on p.o_id_top = f.o_id
    where 1=1
    #@optionOrderBy()
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
    from (#@optionFrom())asdfghj
    #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryCount")
  SELECT count(*) from (#@optionFrom())asdfghj
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
   select o.*,f.o_label o_label_f from fixed_sys_option o
   left join  fixed_sys_option f on o.o_id_top = f.o_id
  #end

  #sql("queryByLabel")
  select o.o_label as label,o.o_id as value,o.o_value from fixed_sys_option o
  left join  fixed_sys_option f on o.o_id_top = f.o_id
  where f.o_label=? and o.o_state=1 order by o.o_order desc
  #end
  #sql("queryByLabel2")
  select o.o_label as label,o.o_id as value,o.o_value from fixed_sys_option o
  left join  fixed_sys_option f on o.o_id_top = f.o_id
  where f.o_label=? and f.o_id_top=? and o.o_state=1 order by o.o_order desc
  #end

  #sql("isExistence")
  select * from fixed_sys_option where o_value=? and o_id_top=? and o_id!=?
  #end
#end