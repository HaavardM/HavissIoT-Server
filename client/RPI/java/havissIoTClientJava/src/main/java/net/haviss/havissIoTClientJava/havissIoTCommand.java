package net.haviss.havissIoTClientJava;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Håvard on 4/3/2015.
 * Class gets commands and arguments from string and processes it.
 */
public class havissIoTCommand {
    public String[] getCommands(String fullString) {
        List<String> tempCmd = new ArrayList<String>();
        String temp = "";
        for(int i = 0; i < fullString.length(); i++) {
            if((fullString.charAt(i) == '-' || fullString.charAt(i) == '$') && i == 0) {
                tempCmd.add(temp);
                temp = "";
                temp += fullString.charAt(i);
            }
            temp += fullString.charAt(i);
        }
        if(temp.length() != 0) {
            tempCmd.add(temp);
        }
        return  tempCmd.toArray(new String[tempCmd.size()]);
    }
}

