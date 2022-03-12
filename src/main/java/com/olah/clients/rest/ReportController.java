package com.olah.clients.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import com.olah.clients.model.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@RestController
@RequestMapping("/api/relatorios")
public class ReportController {

    @Autowired
    UsuarioController usuarioController;

    private final String NOME_RELATORIO_MEMBROS = "relatorioDeMembros";
    private final String relatorioDir = File.separator + "main" + File.separator + "java" + File.separator + "com" + File.separator
            + "olah" + File.separator  + "clients" + File.separator + "jasperReport" + File.separator;
    private final String extensaoXls = ".xlsx";

    @GetMapping("/membros={idIgreja}")
    public void exportarMembrosEmXls(@PathVariable Integer idIgreja,
                                     HttpServletResponse response) throws IOException, JRException {
        String nomeDoRelatorio = NOME_RELATORIO_MEMBROS + retornarSufixoDoRelatorio() + extensaoXls;
        String sourceFileName = ResourceUtils.getFile(getServletContext() + relatorioDir + "SampleJasperReport.jasper")
                .getAbsolutePath();
        List<Usuario> dataList = usuarioController.obterTodosDaIgrejaPorPerfil(1, idIgreja);
        Map<String, Object> parameters = new HashMap<>();
        if (dataList.size() > 0) parameters.put("P_NOME_IGREJA", dataList.get(0).getIgrejas().get(0).getNome());
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        exportarXls(sourceFileName, beanColDataSource, response, nomeDoRelatorio, "Rel Membros", parameters);
    }

    private void exportarXls(String sourceFileName, JRBeanCollectionDataSource beanColDataSource, HttpServletResponse response,
                               String nomeDoRelatorio, String sheetName, Map<String, Object> parameters) throws JRException, IOException {
        JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
        JRXlsxExporter exporter = new JRXlsxExporter();
        SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
        reportConfigXLS.setSheetNames(new String[] { sheetName });
        exporter.setConfiguration(reportConfigXLS);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        response.setHeader("Content-Disposition", "attachment;filename=" + nomeDoRelatorio);
        response.setContentType("application/octet-stream");
        exporter.exportReport();
    }

    private static String retornarSufixoDoRelatorio() {
        SimpleDateFormat df = null;
        df = new SimpleDateFormat("dd-MM-yyyy");
        Integer numRandom = new Random().nextInt();
        numRandom = numRandom < 0 ? (numRandom * -1) : numRandom;
        return "_" + df.format(new Date()) + "_" + numRandom;
    }

    private static String getServletContext() {
        return FileSystems.getDefault().getPath("", "src").toString();
    }

}
