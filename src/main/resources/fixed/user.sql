#namespace("user")
#define userOrderBy()
    #if(orderBy=="999999")
    #else
      order by u.create_time desc
    #end
  #end
  #define userFrom()
   select u.*,dp.dp_name,r.r_name,if(u.m_account_error_time>NOW(),1,0) as error_state from fixed_sys_user u
     LEFT JOIN fixed_sys_role r on u.r_id= r.r_id
     LEFT JOIN fixed_sys_department dp on u.dp_id= dp.dp_id
     where 1=1
  #if(ui_code)
      AND u.ui_code LIKE concat('%',#para(ui_code),'%')
    #end
  #if(dp_id)
      AND u.dp_id = #para(dp_id)
    #end
     #if(ui_name)
      AND u.ui_name LIKE concat('%',#para(ui_name),'%')
    #end
     #if(ui_nickname)
      AND u.ui_nickname LIKE concat('%',#para(ui_nickname),'%')
    #end
     #if(ui_telno)
      AND u.ui_telno LIKE concat('%',#para(ui_telno),'%')
    #end
     #if(r_id)
      AND u.r_id = #para(r_id)
    #end
      #@userOrderBy()
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
    from (#@userFrom())asdfghj
    #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryCount")
  SELECT count(*) from (#@userFrom())asdfghj
  #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end




  #sql("queryById")
  select * from fixed_sys_user where ui_id=?
  #end
  #sql("queryByAll")
  select * from fixed_sys_user
  #end
  #sql("setDeleteInfo")
  UPDATE fixed_sys_user set delete_user = ?,delete_time=now() where  ui_id = ? and ui_code!='admin'
  #end
  #sql("delete")
  delete from fixed_sys_user where ui_id = ? and ui_code!='admin'
  #end
  #sql("login")
  select u.*,r.r_name as ui_landlord_name,if(u.m_account_error_time>NOW(),1,0) as error_state from fixed_sys_user u
  LEFT JOIN fixed_sys_role r on u.r_id=r.r_id
  where u.ui_code = ? and u.ui_password = ?
  #end
  #sql("weixin_login")
  select u.*,r.r_name as ui_landlord_name,if(u.m_account_error_time>NOW(),1,0) as error_state from fixed_sys_user u
  LEFT JOIN fixed_sys_role r on u.r_id=r.r_id
  where u.ui_weixin_openId = ?
  #end

  #sql("lockout")
  select u.* from fixed_sys_user u
  LEFT JOIN fixed_sys_role r on u.r_id=r.r_id
  where u.ui_code = ? and u.m_account_error_time>NOW()
  #end
   #sql("unlock")
  UPDATE fixed_sys_user set m_account_error_time = NULL  where ui_id=?
  #end
   #sql("binding")
  UPDATE fixed_sys_user set ui_weixin_openId = ?  where ui_code = ? and ui_password = ? and ui_weixin_openId is Null
  #end

 #sql("queryByNew")
 select * from fixed_sys_user order by create_time desc
  #end

  #sql("deletePost")
 delete from fixed_sys_user_post where ui_id = ?
  #end

   #sql("queryByPost")
  select * from fixed_sys_user_post where ui_id = ?
  #end
   #sql("isExistence")
  select * from fixed_sys_user where ui_code=?
  #end
  #sql("isExistence2")
  select * from fixed_sys_user where ui_code=? and ui_id!=?
  #end
  #sql("queryBaseById")
  select ui_id,ui_code,ui_name,ui_nickname,ui_telno from fixed_sys_user where ui_id=?
  #end
   #sql("AccountError")
  UPDATE fixed_sys_user set m_account_error_time = date_add(now(), interval 1 hour)  where ui_code=?
  #end
  #sql("savePassword")
  UPDATE fixed_sys_user set ui_code=?,ui_telno = ?,ui_name=?,ui_password=?  where ui_id=? and ui_password=?
  #end
  #sql("updatePassword")
  UPDATE fixed_sys_user set ui_password=?  where ui_id=? and ui_password=?
  #end
  #sql("updateInfo")
  UPDATE fixed_sys_user set ui_telno = ?,ui_name=?  where ui_id=?
  #end
  #sql("saveInfo")
  UPDATE fixed_sys_user set ui_code=?,ui_telno = ?,ui_name=?  where ui_id=? and ui_password=?
  #end
  #sql("isComplete")
  select * from fixed_sys_user where ui_id=? and ui_kmf_password_check is NOT  NULL  and ui_kmf_password is NOT  NULL
  #end
 #sql("resetPassword")
  UPDATE fixed_sys_user set ui_password=? where ui_id = ?
  #end
#end