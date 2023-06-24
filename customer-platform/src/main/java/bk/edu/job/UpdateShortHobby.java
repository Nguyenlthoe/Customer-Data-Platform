package bk.edu.job;

import bk.edu.utils.MySqlUtils;

import java.util.Set;

public class UpdateShortHobby {
    public void process(){
        MySqlUtils mySqlUtils = new MySqlUtils();
        Set<Integer> userIds = mySqlUtils.getListUserUpdate();
        mySqlUtils.deleteOldHobby();
        userIds.forEach(mySqlUtils::updateShortHobbies);
    }
    public static void main(String args[]){
        UpdateShortHobby updateShortHobby = new UpdateShortHobby();
        updateShortHobby.process();
    }
}
