package com.qualshore.etreasury.configure;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date>{

	@Override
	public Date convertToDatabaseColumn(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	@Override
	public LocalDate convertToEntityAttribute(Date sqlDate) {
		return Instant.ofEpochMilli(sqlDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

}
