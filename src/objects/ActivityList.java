/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.ArrayList;

/**
 *
 * @author alumno
 */
public class ActivityList extends BaseObject{
    private int currentId;
    private ArrayList<Activity> list;
    
    public ActivityList(){}
    
    public ActivityList(ArrayList<Activity> list, int currentId){
        this.list = list;
        this.currentId = currentId;
    }
    
    
    
    /**
     * @return the list
     */
    public ArrayList<Activity> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(ArrayList<Activity> list) {
        this.list = list;
    }

    /**
     * @return the currentId
     */
    public int getCurrentId() {
        return currentId;
    }

    /**
     * @param currentId the currentId to set
     */
    public void setCurrentId(int currentId) {
        this.currentId = currentId;
    }
    
    
}
