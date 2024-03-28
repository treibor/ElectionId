/*package com.identity.views;


import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.catalina.webresources.FileResource;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import com.identity.dbservice.DbService;
import com.identity.entity.Cell;
import com.identity.entity.Employee;
import com.identity.entity.Office;

import com.identity.repository.EmployeeRepository;
import com.identity.views.MainLayout;
import com.vaadin.componentfactory.pdfviewer.PdfViewer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.frontend.installer.DefaultFileDownloader;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;


@PageTitle("Printing")
@Route(value="Print", layout=MainLayout.class)
public class PrintView extends VerticalLayout{
	ComboBox<Office> office = new ComboBox("Print Office Wise");
	ComboBox<Cell> cell = new ComboBox("Print Cell Wise");
	NumberField fromRange=new NumberField("Print By Serial No ", "From");
	NumberField toRange=new NumberField("", "To");
	DatePicker fromDate=new DatePicker("Print By Dates");
	DatePicker toDate=new DatePicker();
	Notification notify=new Notification();
	private DbService dbservice;
	HorizontalLayout hl1=new HorizontalLayout();
	HorizontalLayout hl2=new HorizontalLayout();
	HorizontalLayout hl3=new HorizontalLayout();
	HorizontalLayout hl4=new HorizontalLayout();
	HorizontalLayout hl5=new HorizontalLayout();
	
	//InputStream pdfStream;
	//OutputStream outputStream;
	//StreamResource resourceoffice;
	//StreamResource resourcecell;
	StreamResource resourcerange;
	PdfViewer pdfViewerrange;
	//PdfViewer pdfVieweroffice;
	//PdfViewer pdfViewercell;
	String user;
	public PrintView(DbService dbservice) {
		this.dbservice=dbservice;
		user=dbservice.getloggeduser();
		List<Office> offices =dbservice.findOfficesBydistrict();
		List<Cell> cells =dbservice.findCellsBydistrict();
		office.setItems(offices);
		cell.setItems(cells);
		office.setItemLabelGenerator(Office::getOfficeName);
		cell.setItemLabelGenerator(Cell::getCellName);
		cell.addValueChangeListener(e->removePdfViewer());
		office.addValueChangeListener(e-> removePdfViewer());
		add(createRangepanel(),createDatepanel(), createOfficepanel(), createCellpanel(),hl4);
		
	}
	
	private void removePdfViewer() {
		
		if(hl4!=null) {
			hl4.removeAll();
		}
		
	}
	public Component createSplit() {
		SplitLayout slayout=new SplitLayout(createDatepanel(), createRangepanel());
		//return slayout.add(createRangepanel(), createDatepanel());
		return slayout;
	}
	public Component createOfficepanel() {
		office.setWidthFull();
		Button printOffice=new Button("Print ID");
		Button printOfficelist=new Button("Print List");
		printOffice.addClickListener(e-> printOfficeReport("id"));
		printOfficelist.addClickListener(e-> printOfficeReport("list"));
		hl3.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		hl3.add(office,  printOffice, printOfficelist);
		//hl1.setWidth("30em");
		return hl3;
		//return new HorizontalLayout(office, printOffice);
	}
	
	public Component createDatepanel() {
		//fromDate.setWidthFull();
		fromDate.setPlaceholder("From");
		//toDate.setWidthFull();
		toDate.setPlaceholder("To");
		Button printDates=new Button("Print");
		Button printDateslist=new Button("Print List");
		printDates.addClickListener(e-> printDatesReport("id"));
		printDateslist.addClickListener(e-> printDatesReport("list"));
		hl2.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		hl2.add(fromDate, toDate, printDates, printDateslist);
		hl2.setWidth("auto");
		//hl2.setMargin(true);
		//hl3.setWidth("30em");
		return hl2;
		//return new HorizontalLayout(office, printOffice);
	}
	public Component createCellpanel() {
		cell.setWidthFull();
		Button printCell=new Button("Print");
		Button printCelllist=new Button("Print List");
		printCell.setWidthFull();
		printCelllist.setWidthFull();
		printCell.addClickListener(e-> printCellReport("id"));
		printCelllist.addClickListener(e-> printCellReport("list"));
		//HorizontalLayout hl2=new HorizontalLayout();
		
		hl5.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		hl5.add(cell, printCell, printCelllist);
		//hl2.setWidth("30em");
		return hl5;
		//return new HorizontalLayout(office, printOffice);
	}
	public Component createRangepanel() {
		//fromRange.setWidthFull();
		//toRange.setWidthFull();
		Button printRange=new Button("Print");
		Button printRangelist=new Button("Print List");
		printRange.setWidthFull();
		printRangelist.setWidthFull();
		printRange.addClickListener(e-> printRangeReport("id"));
		//HorizontalLayout hl2=new HorizontalLayout();
		printRangelist.addClickListener(e-> printRangeReport("list"));
		hl1.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		hl1.setWidth("auto");
		hl1.add(fromRange, toRange, printRange, printRangelist);
		return hl1;
		//return new HorizontalLayout(office, printOffice);
	}
	
	
	
	private void printRangeReport(String reportType) {
		if(fromRange.getValue()==null || toRange.getValue()==null) {
			notify.show("Please Enter Valid Values ", 3000, Position.TOP_CENTER);
		} else {
			double dfrom = fromRange.getValue();
			double dto = toRange.getValue();
			long fromValue = (long) dfrom;
			long toValue = (long) dto;
			try {
				if (fromValue > toValue) {
					notify.show("Please Check  Values: 'From' cannot be Bigger than 'To'", 3000, Position.TOP_CENTER);
				} else {
					System.out.println("Heloooooo");
					List<Employee> employees = dbservice.getEmployeesBetweenSerials(fromValue, toValue);
					System.out.println(employees);
					printReports(employees, reportType);
				}

			} catch (Exception e) {
				notify.show("Unable TO Generate Report", 5000, Position.TOP_CENTER);
				e.printStackTrace();
				// return "Error--> check the console log";
			}
		
		}
	}
	private void printDatesReport(String reportType) {
		if(fromDate.getValue()==null || toDate.getValue()==null) {
			notify.show("Please Enter Valid Dates ", 3000, Position.TOP_CENTER);
		} else {
			LocalDate dfrom = fromDate.getValue();
			LocalDate dto = toDate.getValue();
			try {
				//boolean dfs=dfrom.
				if (dfrom.isAfter(dto)) {
					notify.show("Please Check  Values: 'From' cannot be Bigger than 'To'", 3000, Position.TOP_CENTER);
				} else {
					//ate dt= new LocalDateConverter(dfrom);
					List<Employee> employees = dbservice.getEmployeesBetweenDates(dfrom, dto);
					printReports(employees, reportType);
				}

			} catch (Exception e) {
				notify.show("Unable TO Generate Report", 5000, Position.TOP_CENTER);
				e.printStackTrace();
				// return "Error--> check the console log";
			}
	
		}
	}

	private InputStream createResource(File path) {// get generated pdf file and create Resource
		try {
			return FileUtils.openInputStream(path);
		} catch (Exception ex) {
		}
		return null;
	}

	private void printCellReport(String reportType) {
		if(cell.getValue()==null) {
			notify.show("Please Select a Cell", 3000, Position.TOP_CENTER);
		}else {
				List<Employee> employees= dbservice.getEmployeesByCell(cell.getValue());
				printReports(employees, reportType);

		}
	}

	private void printOfficeReport(String reportType) {
		if(office.getValue()==null) {
			notify.show("Please Select an Office", 3000, Position.TOP_CENTER);
		}else {
				List<Employee> employees= dbservice.getEmployeesByOffice(office.getValue());
				printReports(employees, reportType);
		}
		//return null;
	}
	private void printReports(List <Employee> employees, String reportType) {
		try {
				Resource resource=null;
				long districtid=1;//dbservice.getLoggedDistrict().getDistrictId();//Use this for district specific reports
				removePdfViewer();
				String reportPath = "D:";dbservice.getLoggedDistrict().getDistrictId();
				if(reportType.equals("id")){
					resource= new ClassPathResource("report/"+districtid+"_id.jrxml");
				}else if(reportType.equals("list")){
					resource= new ClassPathResource("report/list.jrxml");
				}
				InputStream employeeReportStream = resource.getInputStream();
				JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
				JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(employees);
				//Map<String, Object> parameters = new HashMap<>();
				//parameters.put("Parameter1", "Sabaton");
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null,jrBeanCollectionDataSource);
				JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath+"//"+user+"report.pdf");
				File a = new File(reportPath+"//"+user+"report.pdf");
				resourcerange = new StreamResource("Report.pdf", () -> createResource(a));
				pdfViewerrange = new PdfViewer();
				pdfViewerrange.setSrc(resourcerange);
				hl4.setSizeFull();
				hl4.add(pdfViewerrange);
			

		} catch (Exception e) {
			notify.show("Unable TO Generate Report. Error:"+e, 5000, Position.TOP_CENTER);
			e.printStackTrace();
			
		}

	}

	
	
}*/
