/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import objects.Activity;
import objects.ActivityList;
import utils.JsonUtils;

/**
 *
 * @author alumno
 */
public class ActivityHandler {
    
    public static ActivityHandler INSTANCE = new ActivityHandler();
    
    public boolean addActivity(Activity activity){
        try {
            String destinationPath = JsonUtils.PATH + "/" + JsonUtils.FILE_TYPE.AGENDA.rawValue() + ".json";
            if(new File(destinationPath).exists()){
                ActivityList existingList = JsonUtils.INSTANCIA.readJSON(JsonUtils.FILE_TYPE.AGENDA, ActivityList.class);
                activity.setId(existingList.getCurrentId()+ 1);
                existingList.setCurrentId(existingList.getCurrentId()+ 1);

                existingList.getList().add(activity);
               
                return JsonUtils.INSTANCIA.writeJSON(existingList, JsonUtils.FILE_TYPE.AGENDA);           
            }else{
                ActivityList newList = new ActivityList();
                ArrayList<Activity> actList = new ArrayList<Activity>();
               
                activity.setId(1);
                actList.add(activity);
                
                newList.setCurrentId(1);
                newList.setList(actList);
                
                return JsonUtils.INSTANCIA.writeJSON(newList, JsonUtils.FILE_TYPE.AGENDA);  
            }
        } catch (IOException ex) {
            return false;
        }
    }
    
    public boolean deleteActivity(Activity activity){
        try {
            ActivityList existingList = JsonUtils.INSTANCIA.readJSON(JsonUtils.FILE_TYPE.AGENDA, ActivityList.class);
            
            existingList.getList().removeIf((Activity a) -> a.getId() == activity.getId());

            return JsonUtils.INSTANCIA.writeJSON(existingList, JsonUtils.FILE_TYPE.AGENDA);           
        } catch (IOException ex) {
            return false;
        }     
    }
    
    public boolean editActivity(Activity activity){
        try {
            ActivityList existingList = JsonUtils.INSTANCIA.readJSON(JsonUtils.FILE_TYPE.AGENDA, ActivityList.class);
            
            existingList.getList().removeIf((Activity a) -> a.getId() == activity.getId());
            
            existingList.getList().add(activity);

            return JsonUtils.INSTANCIA.writeJSON(existingList, JsonUtils.FILE_TYPE.AGENDA);           
        } catch (IOException ex) {
            return false;
        }
    }
}
