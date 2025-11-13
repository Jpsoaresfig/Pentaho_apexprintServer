package com.Pentaho_apexprintServer.services;

import org.springframework.stereotype.Service;
import java.io.*;
import java.net.URL;
import java.util.Base64;

import org.pentaho.reporting.engine.classic.core.*;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfReportUtil;
import org.pentaho.reporting.engine.classic.core.modules.misc.tablemodel.*;
import org.xml.sax.InputSource;

@Service
public class PentahoService {

    public String generatePdfFromXml(String base64Xml) throws Exception {
        
        byte[] xmlBytes = Base64.getDecoder().decode(base64Xml);
        String xmlContent = new String(xmlBytes);

    // 2️⃣ Cria o arquivo XML temporário
        File xmlFile = File.createTempFile("input-", ".xml");
        try (FileOutputStream fos = new FileOutputStream(xmlFile)) {
            fos.write(xmlContent.getBytes());
        }

        
        ResourceManager manager = new ResourceManager();
        manager.registerDefaults();

        URL reportURL = getClass().getResource("/relatorio.prpt");
        if (reportURL == null) {
            throw new FileNotFoundException("Arquivo relatorio.prpt não encontrado em resources.");
        }

        MasterReport report = (MasterReport) manager.createDirectly(reportURL, MasterReport.class).getResource();

        
        DataFactory dataFactory = new TableDataFactory("default",
                new XMLTableModel(new InputSource(new FileReader(xmlFile))));
        report.setDataFactory(dataFactory);

       
        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        PdfReportUtil.createPDF(report, pdfStream);

       
        return Base64.getEncoder().encodeToString(pdfStream.toByteArray());
    }
}
