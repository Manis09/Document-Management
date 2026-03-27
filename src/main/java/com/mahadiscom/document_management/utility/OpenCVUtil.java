package com.mahadiscom.document_management.utility;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class OpenCVUtil {

    public static Mat bufferedImgToMat(BufferedImage img) {

        BufferedImage converted =
                new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

        converted.getGraphics().drawImage(img, 0, 0, null);

        byte[] pixels =
                ((DataBufferByte) converted.getRaster().getDataBuffer()).getData();

        Mat mat = new Mat(
                converted.getHeight(),
                converted.getWidth(),
                CvType.CV_8UC3
        );

        mat.put(0, 0, pixels);

        return mat;
    }
}