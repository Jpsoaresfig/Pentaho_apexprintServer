package com.Pentaho_apexprintServer.controllers;

import com.Pentaho_apexprintServer.services.PentahoService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pdf")
public class PentahoController {

    private final PentahoService pentahoService;

    public PentahoController(PentahoService pentahoService) {
        this.pentahoService = pentahoService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generatePdf(@RequestBody String base64Xml) {
        try {
            String pdfBase64 = pentahoService.generatePdfFromXml(base64Xml);
            return ResponseEntity.ok(pdfBase64);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar PDF: " + e.getMessage());
        }
    }
}
