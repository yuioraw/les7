package yuioraw;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipFileTest {

    @Test
    void ZipTest() throws Exception {
        ZipFile zipFile = new ZipFile("src/test/resources/files.zip");

        // Проверяем xls файл в архиве
        ZipEntry XlsEntry = zipFile.getEntry("clients.xlsx");
        try (InputStream stream = zipFile.getInputStream(XlsEntry)) {
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0).getRow(3).getCell(0).getStringCellValue())
                    .isEqualTo("Иван");
        }

        // Проверяем csv файл в архиве
        ZipEntry csvEntry = zipFile.getEntry("example.csv");
        try (InputStream stream = zipFile.getInputStream(csvEntry)) {
            CSVReader reader = new CSVReader(new InputStreamReader(stream));
            List<String[]> list = reader.readAll();
            assertThat(list)
                    .hasSize(4)
                    .contains(
                            new String[]{"Name", "LastName"},
                            new String[]{"Egor", "Petrov"},
                            new String[]{"Marina", "Sidiorova"},
                            new String[]{"Oleg", "Minin"}
                    );
        }

        //Проверяем pdf   файл в архиве
        ZipEntry pdfEntry = zipFile.getEntry("track.pdf");
        try (InputStream stream = zipFile.getInputStream(pdfEntry)) {
            PDF parsed = new PDF(stream);
            assertThat(parsed.text).contains("Отслеживание отправлений");
        }
    }
}
