package com.mahadiscom.document_management.service.impl;

import com.mahadiscom.document_management.entity.DocumentMetadata;
import com.mahadiscom.document_management.enums.OcrStatus;
import com.mahadiscom.document_management.repository.DocumentMetadataRepository;
import com.mahadiscom.document_management.service.OcrService;
import com.mahadiscom.document_management.utility.OpenCVUtil;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Service
public class OcrServiceImpl implements OcrService {
    private final ITesseract tesseract;
    private final DocumentMetadataRepository documentMetadataRepository;

    public OcrServiceImpl(@Value("${tesseract.datapath}") String dataPath
            , @Value("${tesseract.language}") String language, DocumentMetadataRepository documentMetadataRepository) {
        this.documentMetadataRepository = documentMetadataRepository;
        tesseract = new Tesseract();
        tesseract.setDatapath(dataPath);
        tesseract.setLanguage(language);
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);
    }

    @Async
    public void extractTextAsync(File file, DocumentMetadata documentMetadata) {

        String text = null;
        try {
            text = getExtractedText(file);
            boolean ocrFailed = isOcrFailed(text);
            if (ocrFailed) {
                /*Here i have to write logic to delete the document from database and metadata also
                 *notify the user to reupload the document
                 **/
            }
            String cleanText = cleanOcrText(text);
            documentMetadata.setExtractedText(text);
            documentMetadata.setCleanExtractedText(cleanText);
            documentMetadata.setOcrStatus(OcrStatus.COMPLETED);
            documentMetadataRepository.save(documentMetadata);
        } catch (IOException | TesseractException e) {
            throw new RuntimeException(e);
        } finally {
            file.delete();
        }
    }

    private String getExtractedText(File file) throws IOException, TesseractException {
        String text = "";
        if (file.getName().endsWith(".pdf")) {
            var pdf = Loader.loadPDF(file);
            var renderer = new PDFRenderer(pdf);

            for (int i = 0; i < pdf.getNumberOfPages(); i++) {
                BufferedImage img = renderer.renderImageWithDPI(i, 300);

                if (img == null) {
                    throw new RuntimeException("Rendered Image is NULL for page" + i);
                }
                Mat mat = OpenCVUtil.bufferedImgToMat(img);

                if (mat.empty()) {
                    throw new RuntimeException("OpenCV mat is empty after conversion");
                }
                Mat processed = preProcess(mat);
                BufferedImage bufferedImage = matToBufferedImg(processed);

                text += tesseract.doOCR(bufferedImage) + "\n";
            }
            pdf.close();
        } else {
            BufferedImage img = ImageIO.read(file);
            text = tesseract.doOCR(img);
        }
        return text;
    }

    private Mat preProcess(Mat src) {
        Mat gray = new Mat();
        Mat threshold = new Mat();

        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);

        Imgproc.adaptiveThreshold(
                gray,
                threshold,
                255,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY,
                11,
                2
        );
        gray.release();
        return threshold;
    }

    private BufferedImage matToBufferedImg(Mat mat) throws IOException {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", mat, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        return ImageIO.read(new ByteArrayInputStream(byteArray));
    }

    private String cleanOcrText(String raw) {
        if (raw == null) {
            return "";
        }

        return raw
                .replaceAll("[^a-zA-Z0-9/\\s@.,-]", "")  // remove noise chars
                .replaceAll("\\s{2,}", " ")                // removing multiple spaces
                .replaceAll("(?m)^\\s*$\\n", "")           // removing blank lines
                .trim();
    }

    private boolean isOcrFailed(String text) {

        if (text == null || text.trim().isEmpty()) {
            return true;
        }

        if (text.length() < 20) {
            return true;
        }

        String cleaned = text.replaceAll("[^a-zA-Z0-9\\s]", "");
        double ratio = (double) cleaned.length() / text.length();
        return ratio < 0.5;
    }

}
