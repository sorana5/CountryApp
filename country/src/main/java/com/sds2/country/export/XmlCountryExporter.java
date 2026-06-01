package com.sds2.country.export;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sds2.country.dto.CountryExportDTO;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class XmlCountryExporter extends CountryExporter {

    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    protected byte[] write(List<CountryExportDTO> data) throws Exception {
        return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(data);
    }

    @Override
    public String getContentType() { return "application/xml"; }

    @Override
    public String getFileName() { return "countries.xml"; }
}