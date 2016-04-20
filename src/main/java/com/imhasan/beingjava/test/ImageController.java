/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imhasan.beingjava.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Uzzal
 */
public class ImageController {

    private Map<String, List<ImgEntry>> imageMap;
    private Pattern p;
    private Matcher m;

    public ImageController() {
        imageMap = new HashMap<>();
    }

    public Map<String, List<ImgEntry>> createHwAddressToImgEntriesMap(String directory) throws ParseException {
        File filePath = new File(directory);
        String regex = "[\\d]+_[xX]?[0-9a-fA-F]{12,16}_(19|20)[\\d][\\d]-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])_[\\d]{2}[\\d]{2}[\\d]{2}";

        for (File f : search(filePath, regex)) {
            final ImgEntry imgEntry = getObjFromFilePath(f);
            final List<ImgEntry> entrys;
            if (imageMap.containsKey(imgEntry.getHwAddress())) {
                entrys = imageMap.get(imgEntry.getHwAddress());
            } else {
                entrys = new ArrayList<>();
                entrys.add(imgEntry);
                imageMap.put(imgEntry.getHwAddress(), entrys);
            }
        }
        return Collections.unmodifiableMap(imageMap);
    }

    private List<File> search(File target, String regex) {
        List<File> validFileList = new ArrayList<>();
        if (target != null && target.isDirectory()) {
            File[] files = target.listFiles();

            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) {
                        p = Pattern.compile(regex);
                        m = p.matcher(f.getName());
                        if (m.find()) {
//                            System.out.println(f.getAbsolutePath());
                            validFileList.add(f);
                        }
                    } else {
                        search(f, regex);
                    }
                }
            }
        }
        return Collections.unmodifiableList(validFileList);
    }

    private ImgEntry getObjFromFilePath(File f) throws ParseException {
        String path = f.getName();
        String ext = path.substring(path.lastIndexOf("."));
        path = path.substring(0, path.lastIndexOf("."));
        String[] params = path.split("_");
        ImgEntry imgEntry = new ImgEntry();
        imgEntry.setEventCode(Integer.parseInt(params[0]));
        imgEntry.setHwAddress(params[1]);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
        cal.setTime(sdf.parse(params[2] + "_" + params[3]));
        imgEntry.setTime(cal);
        try {
            imgEntry.setMd5Hex(getFileContentMD5Checksum(f));
        } catch (IOException ex) {
            Logger.getLogger(ImageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return imgEntry;
    }

    private String getFileContentMD5Checksum(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        String md5 = DigestUtils.md5Hex(fis);
        fis.close();
        return md5;
    }

    public String printMap(Map<String, List<ImgEntry>> images) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : images.keySet()) {
            stringBuilder = stringBuilder.append(images.get(key).toString());
        }
        System.out.println("ImageController{" + "imageMap=" + stringBuilder + '}');
        return "ImageController{" + "imageMap=" + stringBuilder + '}';
    }
}
