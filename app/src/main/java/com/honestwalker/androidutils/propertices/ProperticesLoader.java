package com.honestwalker.androidutils.propertices;

import com.honestwalker.androidutils.propertices.exception.ProperticeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by honestwalker on 16-1-22.
 */
public final class ProperticesLoader {

    private ArrayList<String> configLines = new ArrayList<>();

    private HashMap<String , String> configProperticeMapping = new HashMap<>();
    private ArrayList<String> groups = new ArrayList<>();
    private HashMap<String , HashMap<String , String>> properticeInGroup = new HashMap<>();

    public void loadConfig(InputStreamReader inputStreamReader) throws ProperticeException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                tempString.trim();
                if(tempString.length() > 0) {
                    configLines.add(tempString);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        String lastGroupName = null;

        for(String line : configLines) {        // 逐行读取配置

            if(isGroupStart(line)) {    // 读取分组
                String groupName = line.replace("[" , "").replace("]", "");
                lastGroupName = groupName;
                groups.add(groupName);
//                ArrayList<NameValuePair> nameValuePairsTmp = new ArrayList<>();
                HashMap<String , String> properticesInGroup = new HashMap<>();
                properticeInGroup.put(groupName , properticesInGroup);
                continue;
            }

            if(lastGroupName != null) {     // 如果在某个分组下，读取属性内容
                HashMap<String , String> properticesInGroup = properticeInGroup.get(lastGroupName); // 取出当前组的属性列表 ， 准备添加新属性
                ProperticeNameValuePair nameValuePair = getNameValuePair(line);
                properticesInGroup.put(nameValuePair.getName(), nameValuePair.getValue());
            } else {                         // 读取分组外层属性 ， 外层属性写在最顶 没有group
                ProperticeNameValuePair nameValuePair = getNameValuePair(line);

                if(nameValuePair != null) {
                    configProperticeMapping.put(nameValuePair.getName() , nameValuePair.getValue());
                }
            }

        }
    }

    public void loadConfig(File file) throws ProperticeException {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                tempString.trim();
                if(tempString.length() > 0) {
                    configLines.add(tempString);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        String lastGroupName = null;

        for(String line : configLines) {        // 逐行读取配置

            if(isGroupStart(line)) {    // 读取分组
                String groupName = line.replace("[" , "").replace("]", "");
                lastGroupName = groupName;
                groups.add(groupName);
//                ArrayList<NameValuePair> nameValuePairsTmp = new ArrayList<>();
                HashMap<String , String> properticesInGroup = new HashMap<>();
                properticeInGroup.put(groupName , properticesInGroup);
                continue;
            }

            if(lastGroupName != null) {     // 如果在某个分组下，读取属性内容
                HashMap<String , String> properticesInGroup = properticeInGroup.get(lastGroupName); // 取出当前组的属性列表 ， 准备添加新属性
                ProperticeNameValuePair nameValuePair = getNameValuePair(line);
                properticesInGroup.put(nameValuePair.getName(), nameValuePair.getValue());
            } else {                         // 读取分组外层属性 ， 外层属性写在最顶 没有group
                ProperticeNameValuePair nameValuePair = getNameValuePair(line);

                if(nameValuePair != null) {
                    configProperticeMapping.put(nameValuePair.getName() , nameValuePair.getValue());
                }
            }

        }

    }

    /**
     * 配置是否包含属性
     * @param name
     * @return
     */
    public boolean containsPropertice(String name) {
        return configProperticeMapping.containsKey(name);
    }

    /**
     * 读取配置指定属性
     * @param name
     * @return
     */
    public String getPropertice(String name) {
        return configProperticeMapping.get(name);
    }

    /**
     * 读取指定分组下的指定属性
     * @param group
     * @param name
     * @return
     */
    public String getProperticeByGroup(String group, String name) {

        if(properticeInGroup.containsKey(group)) {
            return properticeInGroup.get(group).get(name);
        } else {
            return null;
        }

    }

    /**
     * 判断行是否是分组开头 ， 如 [group]
     * @param line
     * @return
     */
    private boolean isGroupStart(String line) {
        return (line.startsWith("[") && line.endsWith("]"));
    }

    private ProperticeNameValuePair getNameValuePair(String line) {
        ProperticeNameValuePair nameValuePair = new ProperticeNameValuePair();
        if(line.indexOf("=") > -1) {
            String[] kv = line.split("=");
            if(kv.length == 2) {
                nameValuePair.setName(kv[0]);
                nameValuePair.setValue(kv[1]);
            } else if(kv.length == 1) {
                nameValuePair.setName(kv[0]);
                nameValuePair.setValue("");
            }
            return nameValuePair;
        } else {
            return null;
        }
    }

    public static String getEnvironment() {
        return "";
    }

//    private Environment loadEnvironment(String name) {
//        Environment environment = new Environment();
//        return environment;
//    }

    public ArrayList<String> groups() {
        return groups;
    }

}
