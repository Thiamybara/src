package com.qualshore.etreasury.configure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class DateAttributConverter implements AttributeConverter<Date, String>{

	@Override
	public String convertToDatabaseColumn(Date date) {
		// 2017-10-11T16:52:44.606Z exemple
		// "EEE MMM dd yyyy HH:mm:ss zzz ", Locale.ENGLISH)
		//EEE MMM dd yyyy HH:mm:ss 'GMT'Z (z)
		//yyyy-MM-dd'T'HH:mm:ss.SSSXXX
		// "Fri Oct 27 2017 00:00:00 GMT+0000"
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX",Locale.getDefault());
        return formatter.format(date);
	}

	@Override
	public Date convertToEntityAttribute(String str) {
		try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX",Locale.getDefault());
            return  formatter.parse(str) ;
        } catch (ParseException e) {
            return null;
        }
	}

}
