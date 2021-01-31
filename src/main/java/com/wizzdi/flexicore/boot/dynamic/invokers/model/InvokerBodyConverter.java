package com.wizzdi.flexicore.boot.dynamic.invokers.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;


@Component
@Converter
public class InvokerBodyConverter implements ApplicationContextAware , AttributeConverter<Object,byte[]>{
	private static final Logger logger= LoggerFactory.getLogger(InvokerBodyConverter.class);
	private static ObjectMapper objectMapper;
	@Override
	public byte[] convertToDatabaseColumn(Object invokerBody) {
		try {
			return invokerBody!=null?objectMapper.writeValueAsBytes(new InvokerBody().setObject(invokerBody)):null;
		} catch (JsonProcessingException e) {
			logger.error("failed writing value as string",e);
		}
		return null;
	}

	@Override
	public Object convertToEntityAttribute(byte[] s) {
		try {
			return s!=null?objectMapper.readValue(s, InvokerBody.class).getObject():null;
		} catch (IOException e) {
			logger.error("failed reading value from string",e);
		}
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		objectMapper=applicationContext.getBean(ObjectMapper.class);
	}
}
