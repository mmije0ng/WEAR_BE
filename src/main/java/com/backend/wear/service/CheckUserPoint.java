package com.backend.wear.service;

import com.backend.wear.entity.EnvironmentLevel;
import com.backend.wear.entity.User;

public class CheckUserPoint {
    public static void checkUserPointLevel(User user, Integer point){
        if(point%100!=0) return;

        switch(point){
            case 100:
                user.setLevel( EnvironmentLevel.SAPLING);
                break;
            case 200:
                user.setLevel(EnvironmentLevel.COTTON);
                break;
            case 300:
                user.setLevel(EnvironmentLevel.FLOWER);
                break;
            case 400:
                user.setLevel(EnvironmentLevel.CLOTHES);
                break;
        }
    }
}
