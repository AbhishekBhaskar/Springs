package com.CodeGenAPI.Configuration;

import java.util.Collection;

import org.apache.catalina.Context;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public TomcatServletWebServerFactory containerFactory() {
		return new TomcatServletWebServerFactory() {
			
			@Override
			public Collection<TomcatContextCustomizer> getTomcatContextCustomizers() {
				
				TomcatContextCustomizer ctxCustomizer = new TomcatContextCustomizer() {
					@Override
					public void customize(Context context) {

						String uploadFolder = "C:\\AutoCodeGeneration\\ivu.apie.database\\ivu.apie.database\\";
						context.setPath(getContextPath());
						context.setDocBase(uploadFolder);
					}

				};
				
				super.getTomcatContextCustomizers().add(ctxCustomizer);
				
				return super.getTomcatContextCustomizers();
			}
			

		};
	}

}
