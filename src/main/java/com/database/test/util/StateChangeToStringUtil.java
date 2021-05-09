package com.database.test.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateChangeToStringUtil {

    public void changeRecordStateToString(List<Map<String,Object>> oldList,List<Map<String,Object>> newList){

        if (oldList.size()>0){
            for(int i=0;i<oldList.size();i++)
            {
                Map<String,Object> one  = oldList.get(i);
                int flag = (int) one.get("record_state");
                Map<String,Object> newOne = new HashMap<>();
                newOne.putAll(one);
                newOne.remove("record_state");
                if (flag==1){
                    newOne.put("record_state","已归还");
                }
                if (flag==2){
                    newOne.put("record_state","已挂失");
                }
                newList.add(newOne);
            }
        }else {
            newList=oldList;
        }


    }



    public void changePenaltyStateAndCalDays(List<Map<String,Object>> oldList,List<Map<String,Object>> newList){

        for(int i=0;i<oldList.size();i++)
        {
            Map<String,Object> one = oldList.get(i);
            Date oldTime = (Date) one.get("borrow_time");
            Date newTime = (Date) one.get("return_time");

            int flag= (int) one.get("penalty_state");
            int delayDays = new TimeUtil().calLateDays(oldTime.toString(),newTime.toString());
            String penaltyState;
            if(flag==1)
                penaltyState="已缴纳";
            else
                penaltyState="未缴纳";
            Map<String,Object> newOne = new HashMap<>();
            newOne.putAll(one);
            newOne.remove("borrow_time");
            newOne.remove("return_time");
            newOne.remove("fine_state");
            newOne.put("borrow_time",oldTime.toString());
            newOne.put("return_time",newTime.toString());
            newOne.put("delay_days",delayDays);
            newOne.put("penalty_state",penaltyState);
            newList.add(newOne);
        }
    }

}
