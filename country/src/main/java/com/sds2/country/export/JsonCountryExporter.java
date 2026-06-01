package com.sds2.country.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sds2.country.dto.CountryExportDTO;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class JsonCountryExporter extends CountryExporter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected byte[] write(List<CountryExportDTO> data) throws Exception {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(data);
    }

    @Override
    public String getContentType() { return "application/json"; }

    @Override
    public String getFileName() { return "countries.json"; }
}