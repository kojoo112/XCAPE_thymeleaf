package com.samsan.xcape.util;

public class RandomKeyValue {
    /*
     *  create randomKeyValue
     */

    public static String getRandomKey(){
        String randomString = "";
        for(int i=0; i<3; i++){
            char randomChar = (char) ((Math.random() * 26) + 65);
            randomString += randomChar;
        }
        for(int i=0; i<2; i++){
            char randomNumber = (char) ((Math.random() * 10) + 48);
            randomString += randomNumber;
        }

        return randomString;
    }
}
