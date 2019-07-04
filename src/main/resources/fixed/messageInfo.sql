#namespace("messageInfo")
  #define messageInfoOrderBy()
    #if(orderBy=="999999")
    #else
      order by i.create_time desc
    #end
  #end
  #define messageInfoFrom()
    select i.i_id,dp.dp_name,m.m_content,d.d_name,d.d_phone,i.send_time from fixed_message_info i
    left join fixed_message m on m.m_id = i.m_id
    left join fixed_sys_directories d on i.u_id = d.d_id
    left join fixed_sys_department dp on d.dp_id = dp.dp_id
    #if(i_state)
      AND i.i_state =  #para(i_state)
    #end
    #if(m_id)
      AND i.m_id = #para(m_id)
    #end
      #@messageInfoOrderBy()
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
    from (#@messageInfoFrom())asdfghj
    #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryCount")
  SELECT count(*) from (#@messageInfoFrom())asdfghj
  #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryById")
  select *, case
    when i_state=0 then '已提交'
    when i_state=1 then '发送完成'
    else '未知 '
    end as i_state_str from fixed_message_info i
  left join fixed_message m on i.m_id=m.m_id
     left join sys_base_station_user u on i.u_id=u.u_id
  where i_id=?
  #end
 #sql("setDeleteInfo")
  UPDATE fixed_message_info set delete_user = ?,delete_time=now() where  i_id = ?
  #end
  #sql("delete")
  delete from fixed_message_info where i_id = ?
  #end

  #sql("queryByAll")
  select * from fixed_message_info
  #end
  #sql("updataState")
  UPDATE fixed_message_info set i_state = 1,send_time=now() where i_id=?
  #end
  #sql("queryByState")
  select i_id,m_time from fixed_message_info i
  left join fixed_message m on i.m_id=m.m_id
  left join sys_base_station_user u on i.u_id=u.u_id
  where i.i_state=0 and date_sub(now(),interval 10 minute) <  m_time  order by i_id desc
  #end
 #sql("updataStateErr")
  UPDATE fixed_message_info set i_state = -1 where  i_id = ?
  #end
#end