package com.sds2.country.export;

import com.sds2.country.dto.CountryExportDTO;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

@Component
public class CsvCountryExporter extends CountryExporter {

    @Override
    protected byte[] write(List<CountryExportDTO> data) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        writer.println("name,capital,population,continent");
        for (CountryExportDTO c : data) {
            writer.printf("%s,%s,%d,%s%n",
                    escape(c.getName()),
                    escape(c.getCapital()),
                    c.getPopulation(),
                    escape(c.getContinent()));
        }
        writer.flush();
        return out.toByteArray();
    }

    private String escape(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    @Override
    public String getContentType() { return "text/csv"; }

    @Override
    public String getFileName() { return "countries.csv"; }
}