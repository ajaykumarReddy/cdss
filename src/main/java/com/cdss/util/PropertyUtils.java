package com.cdss.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class PropertyUtils {

	@Produces
	public Properties configProps() throws IOException {

		Properties properties = new Properties();
		InputStream stream = null;
		try {
			stream = this.getClass().getClassLoader()
					.getResourceAsStream("application.properties");
			if (stream != null) {
				properties.load(stream);
			}
		} catch (Exception ex) {
			System.out
					.println("application.propertioes files not found please check!!");
		}

		finally {
			if (stream != null) {
				stream.close();
			}
		}
		return properties;
	}

}
