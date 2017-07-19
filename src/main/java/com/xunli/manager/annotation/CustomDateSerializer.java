package com.xunli.manager.annotation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CustomDateSerializer extends com.fasterxml.jackson.databind.JsonSerializer<Date>
{

	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider arg2) throws IOException, JsonProcessingException
	{
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     String formattedDate = formatter.format(value);  
	     jgen.writeString(formattedDate);  
	}
}
