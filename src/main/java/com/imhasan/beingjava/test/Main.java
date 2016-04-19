/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imhasan.beingjava.test;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Uzzal
 */
public class Main {

    public static void main(String args[]) throws ParseException {
        ImageController imageController = new ImageController();
        Map<String, List<ImgEntry>> images = imageController.createHwAddressToImgEntriesMap("C:\\Users\\Uzzal\\Desktop\\New folder\\questions_to_Robiul_Hasan\\data");
        imageController.printMap(images);
    }
}
