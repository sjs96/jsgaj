#namespace("message")
  #define messageOrderBy()
    #if(orderBy=="999999")
    #else
      order by p.create_time desc
    #end
  #end
  #define messageFrom()
    select p.*,d.dp_name,u.ui_name,IFNULL(num,'0')num from fixed_message p
    left join (select m_id,count(*)num from fixed_message_info GROUP BY m_id) i on p.m_id=i.m_id
    left join fixed_sys_user u on u.ui_id=p.create_user
    left join fixed_sys_department d on p.dp_id=d.dp_id
    where 1=1
    #if(m_name)
      AND m_name LIKE concat('%',#para(m_name),'%')
    #end
      #@messageOrderBy()
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
    from (#@messageFrom())asdfghj
    #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryCount")
  SELECT count(*) from (#@messageFrom())asdfghj
  #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryById")
  select * from fixed_message where m_id=?
  #end
 #sql("setDeleteInfo")
  UPDATE fixed_message set delete_user = ?,delete_time=now() where  m_id = ?
  #end

  #sql("delete")
  delete from fixed_message where m_id = ?
  #end

  #sql("queryByAll")
  select * from fixed_message
  #end
  #sql("queryByMSG")
  select u_id,u_name from sys_base_station_user where u_send_state=1 and b_name in (
    #for(x : area)
        #if( !for.first)
        ,
        #end
       "#(x)"
    #end
  )
  #end

#end