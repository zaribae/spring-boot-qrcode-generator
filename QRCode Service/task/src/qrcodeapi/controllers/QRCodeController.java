package qrcodeapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import qrcodeapi.services.ImageService;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@RestController
public class QRCodeController {
    private final ImageService service;

    @Autowired
    public QRCodeController(ImageService service) {
        this.service = service;
    }

    @GetMapping("/api/health")
    public ResponseEntity checkHealth() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/api/qrcode")
    public ResponseEntity<?> getQR(@RequestParam String contents,
                                   @RequestParam(required = false) String size,
                                   @RequestParam(required = false) String type,
                                   @RequestParam(required = false) String correction) {
        if (size == null) {
            size = "250";
        }
        if (correction == null) {
            correction = "L";
        }
        if (type == null) {
            type = "png";
        }

        String errorMessage = service.checkParams(contents, Integer.parseInt(size), type, correction);
        if (!"ok".equals(errorMessage)) {
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", errorMessage));
        }

        BufferedImage QRImage = service.createImage(contents, Integer.parseInt(size));
        return ResponseEntity
                .ok()
                .contentType(service.getMediaType(type))
                .body(QRImage);
    }

}
