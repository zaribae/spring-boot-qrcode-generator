package qrcodeapi.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

@Service
public class ImageService {
    QRCodeWriter writer = new QRCodeWriter();
    final static int SIZE = 250;


    public BufferedImage createImage(String content, int size) {
        BufferedImage image = null;
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size);
            image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            e.getMessage();
        }
        return image;

    }

    public String checkParams(String contents, int size, String type, String correction) {
        String sizeErrorMessage = "Image size must be between 150 and 350 pixels";
        String typeErrorMessage = "Only png, jpeg and gif image types are supported";
        String contentErrorMessage = "Contents cannot be null or blank";
        String correctionErrorMessage = "Permitted error correction levels are L, M, Q, H";
        boolean hasSizeError = false;
        boolean hasTypeError = true;
        boolean hasContentError = false;
        boolean hasCorrectionError = true;

        if (size < 150 || size > 350) {
            hasSizeError = true;
        }
        if (type == null) {
            hasTypeError = false;
        } else {
            if (type.equalsIgnoreCase("png") ||
                    type.equalsIgnoreCase("jpeg") ||
                    type.equalsIgnoreCase("gif")) {
                hasTypeError = false;
            }
        }
        if (contents.isEmpty() || contents.isBlank()) {
            hasContentError = true;
        }

        if (correction.equals("L") ||
                correction.equals("M") ||
                correction.equals("Q") ||
                correction.equals("H")) {
            hasCorrectionError = false;
        }

        return hasContentError ? contentErrorMessage :
                hasSizeError ? sizeErrorMessage :
                hasCorrectionError ? correctionErrorMessage :
                hasTypeError ? typeErrorMessage :  "ok";
    }

    public MediaType getMediaType(String type) {
        MediaType mediaType = switch(type) {
            case "png", "PNG" -> MediaType.IMAGE_PNG;
            case"jpeg", "JPEG" -> MediaType.IMAGE_JPEG;
            case "gif", "GIF" -> MediaType.IMAGE_GIF;
            default -> MediaType.IMAGE_PNG;
        };
        return mediaType;
    }
}
