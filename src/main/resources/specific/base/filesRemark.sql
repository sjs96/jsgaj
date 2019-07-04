#namespace("filesRemark")
  #define filesRemarkOrderBy()
    #if(orderBy=="999999")
    #else
      order by p.create_time desc
    #end
  #end
  #define filesRemarkFrom()
    select p.* from sys_files_remark p
    where 1=1
    #@filesRemarkOrderBy()
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
  SELECT count(*) from (#@filesRemarkFrom())asdfghj
  #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryById")
  select * from sys_files_remark where fr_id=?
  #end
 #sql("setDeleteInfo")
  UPDATE sys_files_remark set delete_user = ?,delete_time=now() where  fr_id = ?
  #end

  #sql("delete")
  delete from sys_files_remark where fr_id = ?
  #end

  #sql("queryByAll")
  select * from sys_files_remark
  #end

#sql("queryByLog")
select p.create_time,p.fr_remark,u.ui_name from sys_files_remark p
LEFT JOIN fixed_sys_user u on p.create_user=u.ui_id
where p.f_id=? order by fr_id desc
  #end
#end