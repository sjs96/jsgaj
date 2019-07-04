#namespace("tableSetting")
#define tableSettingOrderBy()
    #if(orderBy=="999999")
    #else
      order by r.create_time desc
    #end
  #end
  #define tableSettingFrom()
   select * from fixed_table_setting r where 1 = 1
      #@tableSettingOrderBy()
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
    from (#@tableSettingFrom())asdfghj
    #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryCount")
  SELECT count(*) from (#@tableSettingFrom())asdfghj
  #if(fuzzy_query)
      where 1=11
      #for(x : tableControlShow)
         or #(x) like binary  concat('%',#para(fuzzy_query),'%')
      #end
    #end
  #end

  #sql("queryById")
  select * from fixed_table_setting where table_setting_id=?
  #end
#sql("setDeleteInfo")
  UPDATE fixed_table_setting set delete_user = ?,delete_time=now() where  table_setting_id = ?
  #end
  #sql("delete")
  delete from fixed_table_setting where table_setting_id = ?
  #end

  #sql("setDeleteModelInfo")
  UPDATE fixed_table_setting set delete_user = ?,delete_time=now() where  ui_id =? and table_setting_model = ?
  #end
  #sql("deleteModel")
  delete from fixed_table_setting where  ui_id =? and table_setting_model = ?
  #end

  #sql("queryByAll")
  select * from fixed_table_setting
  #end

  #sql("queryByUIID")
  select * from fixed_table_setting where ui_id=?
  #end

#end