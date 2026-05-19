package com.example.volt.commnad;

import android.util.Log;

import com.example.volt.model.Destination;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.navigation.model.SpeedLevel;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;


public class GoToCommand implements Command<String> {

    private final Set<Destination> destinations;

    public GoToCommand(Set<Destination> destinations) {
        this.destinations = destinations;
    }

    @Override
    public boolean execute(String destination) {
        if (destination != null) {
            Log.d("noy", "in command: " +destination);
            destination = wordsToNumbers(destination);
            Log.d("noy", "in command: " +destination);
            for (Destination i : this.destinations) {
                if (i.getDisplayName().equalsIgnoreCase(destination) || i.getInternalName().equalsIgnoreCase(destination)) {
                    Log.d("noy", "found: " +destination);
                    Robot.getInstance().goTo(destination, true, false, SpeedLevel.HIGH);
                    return true;
                }
            }
        }
        return false;
    }

    public static String wordsToNumbers(String input) {
        String[] words = input.toLowerCase().split(" ");
        StringBuilder result = new StringBuilder();
        int number = 0;
        int temp = 0;

        Map<String, Integer> map = new HashMap<>();
        map.put("zero", 0); map.put("one", 1); map.put("two", 2);
        map.put("three", 3); map.put("four", 4); map.put("five", 5);
        map.put("six", 6); map.put("seven", 7); map.put("eight", 8);
        map.put("nine", 9); map.put("ten", 10); map.put("eleven", 11);
        map.put("twelve", 12); map.put("thirteen", 13); map.put("fourteen", 14);
        map.put("fifteen", 15); map.put("sixteen", 16); map.put("seventeen", 17);
        map.put("eighteen", 18); map.put("nineteen", 19);
        map.put("twenty", 20); map.put("thirty", 30); map.put("forty", 40);
        map.put("fifty", 50); map.put("sixty", 60); map.put("seventy", 70);
        map.put("eighty", 80); map.put("ninety", 90);
        map.put("hundred", 100);

        for (String w : words) {
            if (map.containsKey(w)) {
                int val = map.get(w);
                if (val == 100) temp *= 100;
                else temp += val;
            } else {
                if (temp != 0) {
                    number += temp;
                    result.append(" ").append(number);
                    temp = 0;
                    number = 0;
                }
                result.append(" ").append(w);
            }
        }
        if (temp != 0) result.append(" ").append(number + temp);

        return result.toString().trim().replaceAll("\\s+", " ");
    }


}