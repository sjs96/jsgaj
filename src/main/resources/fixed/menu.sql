#namespace("menu")

#define menuOrderBy()
    #if(orderBy=="999999")
    #else
      order by a.menu_parent_id , a.menu_order desc
    #end
  #end

  #define menuFrom()
    select a.*,
      CASE
    when a.menu_level = 1 then '主页面'
    when a.menu_level = 2 then '子页面'
     ELSE '未知' END as menu_level_str,
      CASE
    when a.menu_valid = 1 then '有效'
    when a.menu_valid = -1 then '无效'
     ELSE '未知' END as menu_valid_str,
    b.menu_name menu_parent_name from fixed_sys_menu a left join fixed_sys_menu b on a.menu_parent_id = b.menu_id where 1 = 1
    #if(menu_parent_id)
      AND a.menu_parent_id = #para(menu_parent_id)
    #end
    #if(menu_name)
      AND a.menu_name LIKE concat('%',#para(menu_name),'%')
    #end
    #if(menu_valid)
      AND a.menu_valid = #para(menu_valid)
    #end
      #@menuOrderBy()
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
    from (#@menuFrom())asdfghj
    #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryCount")
  SELECT count(*) from (#@menuFrom())asdfghj
  #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryById")
  select * from fixed_sys_menu where menu_id=?
  #end

#sql("setDeleteInfo")
  UPDATE fixed_sys_menu set delete_user = ?,delete_time=now() where  menu_id = ?
  #end

  #sql("delete")
  delete from fixed_sys_menu where menu_id = ?
  #end

  #sql("queryByFirst")
  select * from fixed_sys_menu where menu_level=1 order by menu_order desc
  #end

  #sql("queryBySecond2")
  SELECT distinct m.* from fixed_sys_menu m
  LEFT JOIN fixed_sys_jurisdiction_details jd on jd.m_id = m.menu_id
  LEFT JOIN fixed_sys_jurisdiction j on jd.j_id = j.j_id
  LEFT JOIN fixed_sys_user u on u.r_id = j.r_id
  where u.ui_id=? and m.menu_level=2 order by menu_order desc
  #end

 #sql("queryBySecond")
   select * from fixed_sys_menu where menu_linkurl in (
    #for(x : list)
        #if( !for.first)
        ,
        #end
       "#(x)"
    #end
  )
  #end


  #sql("queryByNew")
  select * from fixed_sys_menu order by create_time desc
  #end
   #sql("queryByAll")
  select * from fixed_sys_menu
  #end
  #sql("setDeleteAttributeInfo")
  UPDATE fixed_sys_menu_attribute set delete_user = ?,delete_time=now() where  m_id = ?
  #end
  #sql("deleteAttribute")
  delete from fixed_sys_menu_attribute where m_id=?
  #end

   #sql("queryAttribute")
 select * from fixed_sys_menu_attribute where m_id=?
  #end
#end