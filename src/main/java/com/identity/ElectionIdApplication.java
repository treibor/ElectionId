package com.identity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.identity.dbservice.DbService;
import com.identity.entity.Cell;
import com.identity.entity.District;
import com.identity.entity.Employee;
import com.identity.entity.Office;
import com.identity.entity.State;
import com.identity.entity.Users;
import com.identity.repository.CellRepository;
import com.identity.repository.DistrictRepository;
import com.identity.repository.EmployeeRepository;
import com.identity.repository.OfficeRepository;
import com.identity.repository.StateRepository;
import com.identity.repository.UsersRepository;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class ElectionIdApplication extends SpringBootServletInitializer  {
	
	
	@Override
	protected SpringApplicationBuilder configure (SpringApplicationBuilder builder) {
		return builder.sources(ElectionIdApplication.class);
		
	}
	
	//private static final Logger log = LoggerFactory.getLogger(ElectionIdApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(ElectionIdApplication.class, args);
	}
	/*
	@Bean
	public CommandLineRunner demo(EmployeeRepository erepo, OfficeRepository orepo, CellRepository crepo, StateRepository srepo, DistrictRepository drepo, UsersRepository urepo) {
		return (args) -> {
			/*srepo.save(new State("Meghalaya"));
			srepo.save(new State("Assam"));
			State state=srepo.getById((long) 1);
			drepo.save(new District("Ri Bhoi", state));
			drepo.save(new District("East Jaintia Hills", state));
			District district1=drepo.getById((long) 1);
			District district2=drepo.getById((long) 2);
			urepo.save(new Users("RBH", "$2a$12$FvBpuH/vfoteqxVBua5n..I2ggk/cnOkwlqDnnbSHdOCWgCAhZaL2","USER", district1, state));
			urepo.save(new Users("EJH", "$2a$12$FvBpuH/vfoteqxVBua5n..I2ggk/cnOkwlqDnnbSHdOCWgCAhZaL2","USER", district2, state));
			*/
			//System.out.println(db.findMaxSerialNo());
			//orepo.save(new Office(""));
			//save a few customers
			/*
			orepo.save(new Office( "Public Works Department"));
			orepo.save(new Office( "Public Health Engineering Department"));
			orepo.save(new Office( "District School and Education Officer"));
			orepo.save(new Office( "Deputy Commissioner's Office"));
			Office office1=orepo.getById((long)1);
			Office office2=orepo.getById((long)2);
			Office office3=orepo.getById((long)3);
			Office office4=orepo.getById((long)3);
			crepo.save(new Cell("Personnel", "#0000FF"));
			crepo.save(new Cell("Training", "#808080"));
			crepo.save(new Cell("Computer", "##008000"));
			crepo.save(new Cell("Catering", "#FF0000"));
			Cell cell1=crepo.getById((long)1);
			Cell cell2=crepo.getById((long)2);
			Cell cell3=crepo.getById((long)3);
			erepo.save(new Employee("Manbha","Maloi","DTO", office1, cell1, null));
			erepo.save(new Employee("Nuno","Longhair","Driver", office2, cell2, null));
			erepo.save(new Employee("Dolf","Stoned","Doctor", office3, cell3, null));
			erepo.save(new Employee("Pahara","Hehhhh","Peon", office3, cell1, null));
			erepo.save(new Employee("Damian","Lalot","Tractor Driver", office1, cell3, null));
			//erepo.save(new Employee("Paloma","Nongkynrih","DIA", office2, cell2, null));
			//erepo.save(new Employee("Jayoma","Nongkhlaw","ADC", office2, cell1, null));
			//orepo.save(new Office("Personnel"));
			
			log.info("Customers found with findAll():");
		      log.info("-------------------------------");
		      for (Employee employee : erepo.findAll()) {
		        log.info(employee.toString());
		      }
		      log.info("");
		      log.info("Employee found with findByLastName('Marwein'):");
		      log.info("--------------------------------------------");
		      erepo.findBylastName("Marwein").forEach(marwein -> {
		        log.info(marwein.toString());
		      });
		
		};
	}*/
	
}
