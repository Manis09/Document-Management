package com.mahadiscom.document_management.config;

import nu.pattern.OpenCV;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenCVConfig {


    /**
     * OpenCV will automatically loaded at the time of the staring of an application
     * It is used to process the image
     * It works only with Mat type file
     */
    static{
        OpenCV.loadLocally();
        System.out.println("OpenCV load successfully");
    }
}
