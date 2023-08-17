package com.frogking.chromedriver;

import org.checkerframework.checker.regex.qual.Regex;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patcher {

    private String _driverExecutablePath;

    public Patcher(String _driverExecutablePath){
        this._driverExecutablePath = _driverExecutablePath;
    }

    public void Auto() throws Exception {
        if (!isBinaryPatched()){
            patchExe();
        }
    }

    private boolean isBinaryPatched() throws Exception{
        if (_driverExecutablePath == null) {
            throw new RuntimeException("driverExecutablePath is required.");
        }
        File file = new File(_driverExecutablePath);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file, StandardCharsets.ISO_8859_1));

            String line = null;

            while ((line = br.readLine()) != null) {
                if (line.contains("undetected chromedriver")) {
                    return true;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(br!=null){
                try{
                    br.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private int patchExe(){

        int linect = 0;
        String replacement = genRandomCdc();
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(_driverExecutablePath, "rw");

            byte[] buffer = new byte[1024];
            StringBuilder stringBuilder = new StringBuilder();
            long read = 0;
            while(true){
                read = file.read(buffer,0,buffer.length);
                if(read == 0 || read == -1){
                    break;
                }
                stringBuilder.append(new String(buffer,0,(int)read,StandardCharsets.ISO_8859_1));
            }
            String content = stringBuilder.toString();
            Pattern pattern = Pattern.compile("\\{window\\.cdc.*?;\\}");
            Matcher matcher = pattern.matcher(content);
            if(matcher.find()){
                String group = matcher.group();
                StringBuilder newTarget = new StringBuilder("{console.log(\"undetected chromedriver 1337!\"}");
                int k = group.length() - newTarget.length();
                for (int i = 0; i < k; i++) {
                    newTarget.append(" ");
                }
                String newContent = content.replace(group, newTarget.toString());

                file.seek(0);
                file.write(newContent.getBytes(StandardCharsets.ISO_8859_1));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(file!=null){
                try {
                    file.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return linect;
    }

    private String genRandomCdc() {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        /*
        char[] cdc = new char[26];

        for(int i=0;i<26;i++){
            cdc[i] = chars.charAt(random.nextInt(chars.length()));
        }
        for (int i = 4; i <= 6; i++) {
            cdc[cdc.length - i] = Character.toUpperCase(cdc[cdc.length - i]);
        }
        cdc[2] = cdc[0];
        cdc[3] = '_';
        return new String(cdc);

         */
        char[] cdc = new char[27];
        for(int i=0;i<27;i++){
            cdc[i] = chars.charAt(random.nextInt(chars.length()));
        }
        return new String(cdc);
    }

}