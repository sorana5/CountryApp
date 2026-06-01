package com.sds2.country.export;

import com.sds2.country.dto.CountryExportDTO;
import com.sds2.country.model.Country;
import java.util.List;

public abstract class CountryExporter {

    public final byte[] export(List<Country> countries) throws Exception {
        List<CountryExportDTO> data = transform(countries);
        return write(data);
    }

    protected List<CountryExportDTO> transform(List<Country> countries) {
        return countries.stream()
                .map(c -> new CountryExportDTO(
                        c.getName(),
                        c.getCapital(),
                        c.getPopulation(),
                        c.getContinent()))
                .toList();
    }

    protected abstract byte[] write(List<CountryExportDTO> data) throws Exception;
    public abstract String getContentType();
    public abstract String getFileName();
}