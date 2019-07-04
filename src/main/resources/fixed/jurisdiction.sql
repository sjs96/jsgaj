#namespace("jurisdiction")

  #define jurisdictionOrderBy()
    #if(orderBy=="999999")
    #else
      order by j.create_time desc
    #end
  #end
  #define jurisdictionFrom()
   select j.*,r.r_name from fixed_sys_jurisdiction j
   left join  fixed_sys_role r on j.r_id=r.r_id  where 1 = 1
     #if(j_name)
      AND j_name LIKE concat('%',#para(j_name),'%')
    #end
      #@jurisdictionOrderBy()
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
    from (#@jurisdictionFrom())asdfghj
    #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryCount")
  SELECT count(*) from (#@jurisdictionFrom())asdfghj
  #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end



  #sql("queryById")
   select j.*,r.r_name from fixed_sys_jurisdiction j
   left join  fixed_sys_role r on j.r_id=r.r_id  where j_id=?
  #end


  #sql("setDeleteInfo")
  UPDATE fixed_sys_jurisdiction set delete_user = ?,delete_time=now() where  j_id = ?
  #end

  #sql("delete")
  delete from fixed_sys_jurisdiction where j_id = ?
  #end

 #sql("setDeleteInfor")
  UPDATE fixed_sys_role set delete_user = ?,delete_time=now() where  r_id = (select r_id from fixed_sys_jurisdiction where j_id=?)
  #end

  #sql("deleter")
  delete from fixed_sys_role where r_id = (select r_id from fixed_sys_jurisdiction where j_id=?)
  #end

  #sql("queryByAll")
  select * from fixed_sys_jurisdiction
  #end

  #sql("queryMenuByAll")
   select m.*,f.menu_name menu_name_f from fixed_sys_menu m
left join fixed_sys_menu f on m.menu_parent_id = f.menu_id where m.menu_level=2 order by m.menu_parent_id
  #end
  #sql("queryMenuByAll2")
  select m.menu_parent_id,count(*)num from fixed_sys_menu m
left join fixed_sys_menu f on m.menu_parent_id = f.menu_id where m.menu_level=2  group by m.menu_parent_id order by m.menu_parent_id
  #end
  #sql("queryMenuByOne")
  select * from fixed_sys_menu where menu_level=1
  #end

  #sql("queryMenuByTwo")
  select * from fixed_sys_menu where menu_parent_id=?
  #end

  #sql("queryByNew")
  select * from fixed_sys_jurisdiction order by create_time desc
  #end
 #sql("setDeleteDetailsInfor")
  UPDATE fixed_sys_jurisdiction_details set delete_user = ?,delete_time=now() where  j_id = ?
  #end

  #sql("deleteDetails")
  delete from fixed_sys_jurisdiction_details where j_id=?
  #end

 #sql("queryDetails")
 select * from fixed_sys_jurisdiction_details where j_id=?
  #end
 #sql("queryByUser")
select j.* from fixed_sys_jurisdiction j
LEFT JOIN fixed_sys_user u on u.r_id = j.r_id
where u.ui_id=?
  #end
 #sql("queryByUser2")
SELECT jd.jd_name,m.menu_linkurl from fixed_sys_jurisdiction_details jd
INNER JOIN fixed_sys_menu m on jd.m_id = m.menu_id
LEFT JOIN fixed_sys_jurisdiction j on jd.j_id = j.j_id
LEFT JOIN fixed_sys_user u on u.r_id = j.r_id
where u.ui_id=?
  #end

#end