package com.frogking.chromedriver;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

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
                if (line.contains("cdc_")) {
                    return false;
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
        return true;
    }

    private int patchExe(){

        int linect = 0;
        String replacement = genRandomCdc();
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(_driverExecutablePath, "rw");

            byte[] buffer = new byte[1];
            StringBuilder check = new StringBuilder("....");
            int read = 0;

            while (true) {
                read = file.read(buffer, 0, buffer.length);

                if (read == 0) {
                    break;
                }

                check.delete(0, 1);

                check.append((char) buffer[0]);

                if (check.toString().equals("cdc_")) {
                    file.seek(file.getFilePointer() - 4);

                    byte[] bytes = replacement.getBytes(StandardCharsets.ISO_8859_1);

                    file.write(bytes, 0, bytes.length);
                    linect++;
                }
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
    }

}