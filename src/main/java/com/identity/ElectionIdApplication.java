package com.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
//@Theme(value = "megid", variant = Lumo.DARK)
@Theme(value = "megid")
@PWA(name = "megid", shortName = "Meg ID", iconPath = "/icons/icon.png")
public class ElectionIdApplication extends SpringBootServletInitializer implements AppShellConfigurator{
	
	
	
	
	private static final long serialVersionUID = 1L;

	//private static final Logger log = LoggerFactory.getLogger(ElectionIdApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(ElectionIdApplication.class, args);
	}
	
	@Override
	public void configurePage(AppShellSettings settings) {
		settings.addFavIcon("icon", "/icons/icon.png", "192x192");
		settings.addLink("shortcut icon", "/icons/icon.png");
		settings.setPageTitle("MegId");
	}
	
}
