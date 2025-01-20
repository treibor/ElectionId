package com.identity.views;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.identity.dbservice.DbService;
import com.identity.dbservice.DbServicePol;
import com.identity.entity.Candidate;
import com.identity.entity.Cell;
import com.identity.entity.Constituency;
import com.identity.entity.Employee;
import com.identity.entity.Office;
import com.identity.entity.Party;
import com.identity.entity.Political;
import com.vaadin.componentfactory.pdfviewer.PdfViewer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import jakarta.annotation.security.RolesAllowed;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


@PageTitle("Reports")
@Route(value="printing", layout=MainLayout.class)
@RolesAllowed({ "USER", "SUPER", "ADMIN" })
public class PrintView2 extends VerticalLayout{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ComboBox<Office> office = new ComboBox("Print Office-Wise");
	ComboBox<Cell> cell = new ComboBox("Print Cell-Wise");
	ComboBox<Party> party = new ComboBox("Print Party-Wise");
	ComboBox<Constituency> consti = new ComboBox("Print Constituency-Wise");
	ComboBox<Candidate> candi = new ComboBox("Print Candidate-Wise");
	NumberField fromRange=new NumberField("Print By Serial No ", "From");
	NumberField toRange=new NumberField("", "To");
	NumberField fromRangep=new NumberField("Print By Serial No ", "From");
	NumberField toRangep=new NumberField("", "To");
	DatePicker fromDate=new DatePicker("Print By Dates");
	DatePicker toDate=new DatePicker();
	//Notification Notification=new Notification();
	private DbService dbservice;
	private DbServicePol dbservice1;
	HorizontalLayout hl4=new HorizontalLayout();
	DatePicker fromDatep=new DatePicker("Print By Dates");
	DatePicker toDatep=new DatePicker();
	StreamResource resourcerange;
	PdfViewer pdfViewerrange;
	String user;
	RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
	RadioButtonGroup<String> radioGroup1 = new RadioButtonGroup<>();
	RadioButtonGroup<String> radioGroup2 = new RadioButtonGroup<>();
	public PrintView2(DbService dbservice, DbServicePol dbservice1) {
		this.dbservice=dbservice;
		this.dbservice1=dbservice1;
		user=dbservice.getloggeduser();
		List<Office> offices =dbservice.findOfficesBydistrict();
		List<Cell> cells =dbservice.findCellsBydistrict();
		List<Party> parties=dbservice1.findPartyBydistrict();
		List<Constituency> constis=dbservice1.findConstBydistrict();
		List<Candidate> candis=dbservice1.findCandidateBydistrict();
		office.setItems(offices);
		cell.setItems(cells);
		party.setItems(parties);
		consti.setItems(constis);
		candi.setItems(candis);
		office.setItemLabelGenerator(Office::getOfficeName);
		cell.setItemLabelGenerator(Cell::getCellName);
		
		party.setItemLabelGenerator(Party :: getPartyName);
		consti.setItemLabelGenerator(Constituency:: getConstituencyName);
		candi.setItemLabelGenerator(Candidate::getCandidateName);
		cell.addValueChangeListener(e->removePdfViewer());
		office.addValueChangeListener(e-> removePdfViewer());
		party.addValueChangeListener(e-> removePdfViewer());
		consti.addValueChangeListener(e-> removePdfViewer());
		candi.addValueChangeListener(e-> removePdfViewer());
		add(createFinalPanel(), hl4);
	}
	
	private void removePdfViewer() {
		
		if(hl4!=null) {
			hl4.removeAll();
		}
		
	}
	public Component createFinalPanel() {
		Accordion accordion=new Accordion();
		accordion.add("Personnel", createGovtpanel());
		accordion.add("Political", createPoliticalpanel());
		//accordion.set
		return accordion;
	}
	
	
	public Component createGovtpanel() {
		HorizontalLayout reportFormat=new HorizontalLayout();
		reportFormat.setDefaultVerticalComponentAlignment(Alignment.CENTER);  
		//reportFormat.setJustifyContentMode(JustifyContentMode.CENTER);
		radioGroup.addClassName("buttons");
		//radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		radioGroup.setLabel("Id Type");
		radioGroup.setItems("Landscape1", "Landscape2", "Landscape2", "Landscape4", "Landscape5","Portrait1", "Portrait2");
		radioGroup.setValue("Landscape1");
		radioGroup.setRenderer(new ComponentRenderer<>(item -> createItemWithImage(item)));
		reportFormat.add(radioGroup);
		FormLayout fl1=new FormLayout();
		Button printRange=new Button("Print Id");
		Button printRangelist=new Button("Print List");
		Button printDates=new Button("Print Id");
		Button printDateslist=new Button("Print List");
		Button printCell=new Button("Print");
		Button printCelllist=new Button("Print List");
		Button printOffice=new Button("Print ID");
		Button printOfficelist=new Button("Print List");
		
		printRange.addClickListener(e-> printRangeReport("id", "employee"));
		printRangelist.addClickListener(e-> printRangeReport("list", "employee"));
		printDates.addClickListener(e-> printDatesReport("id", "employee"));
		printDateslist.addClickListener(e-> printDatesReport("list", "employee"));
		fromDate.setPlaceholder("From");
		toDate.setPlaceholder("To");
		printCell.addClickListener(e-> printCellReport("id", "employee"));
		printCelllist.addClickListener(e-> printCellReport("list", "employee"));
		printOffice.addClickListener(e-> printOfficeReport("id", "employee"));
		printOfficelist.addClickListener(e-> printOfficeReport("list", "employee"));
		fl1.add(reportFormat,8);
		fl1.add(fromRange, 1);
		fl1.add(toRange, 1);
		fl1.add(printRange, printRangelist);
		fl1.add(fromDate, 1);
		fl1.add(toDate, 1);
		fl1.add( printDates, printDateslist);
		fl1.add(cell, 2);
		fl1.add(printCell, printCelllist);
		fl1.add(office, 2);
		fl1.add(printOffice, printOfficelist);
		fl1.setWidth("auto");
		fl1.setResponsiveSteps(
		        new ResponsiveStep("0", 8),
		        // Use two columns, if layout's width exceeds 500px
		        new ResponsiveStep("500px", 8)
		);
		//Details details=new Details("Election Personnel", fl1);
		//details.setOpened(false);
		//return details;
		return fl1;
	}
	private Component createItemWithImage(String item) {
        // Create an image based on the item value
        Image image = new Image();
        image.getStyle().set("width", "7px");
        image.getStyle().set("height", "7px");
        image.getStyle().set("object-fit", "contain");
        // Set the image source based on the report type
        switch (item) {
            case "Landscape1":
            	image = new Image("images/landscape.png", "Landscape Image");
            	//System.out.println("Landscape");
                break;
            case "Landscape2":
            	image = new Image("images/landscape2.jpg", "Landscape Image 2");
            	//System.out.println("Landscape");
                break;
            case "Portrait1":
            	image = new Image("/images/portrait.jpg", "Portrait Image");
                break;
            case "Landscape3":
            	image = new Image("/images/landscape3.png", "DSC Image");
                break;
            case "Landscape4":
            	image = new Image("/images/landscape4.png", "DSC Image 2");
                break;
            case "OTHER":
                image.setSrc("/images/other.png");
                break;
            case "Portrait2":
            	image = new Image("/images/portrait2.jpeg", "Portrait Image 2");
                break;
            case "Landscape5":
            	image = new Image("/images/landscape5.jpeg", "Landscape Image 5");
                break;
            default:
                image.setSrc("/images/default.png");
        }

        //Span label = new Span(item);

        image.setTitle(item);
        HorizontalLayout layout = new HorizontalLayout(image);
        
        layout.setAlignItems(Alignment.CENTER);  // Align image and text vertically in the center

        
        Div wrapper = new Div(layout);
        wrapper.getStyle().set("display", "flex");
        wrapper.getStyle().set("align-items", "center");  // Align items vertically
        wrapper.getStyle().set("justify-content", "center");  // Center items horizontally
        wrapper.getStyle().set("width", "100%");

        return wrapper;
    }
	public Component createPoliticalpanel() {
		HorizontalLayout reportType=new HorizontalLayout();
		HorizontalLayout reportFormat=new HorizontalLayout();
		//radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		radioGroup2.setLabel("Report Type");
		radioGroup2.setItems("Landscape", "Portrait");
		radioGroup2.setValue("Landscape");
		reportFormat.add(radioGroup2);
		radioGroup1.setLabel("Id For");
		radioGroup1.setItems("Agents", "Candidate");
		radioGroup1.setValue("Agents");
		reportType.add(radioGroup1);
		FormLayout fl2=new FormLayout();
		Button printRangep=new Button("Print Id");
		Button printRangelistp=new Button("Print List");
		Button printDatesp=new Button("Print Id");
		Button printDateslistp=new Button("Print List");
		Button printParty=new Button("Print");
		Button printPartylist=new Button("Print List");
		Button printConsti=new Button("Print ID");
		Button printConstilist=new Button("Print List");
		Button printCandi=new Button("Print ID");
		Button printCandilist=new Button("Print List");
		printRangep.addClickListener(e-> printRangeReportPolitical("id_p", "political"));
		printRangelistp.addClickListener(e-> printRangeReportPolitical("list_p", "political"));
		printDatesp.addClickListener(e-> printDatesReportPolitical("id_p", "political"));
		printDateslistp.addClickListener(e-> printDatesReportPolitical("list_p", "political"));
		fromDatep.setPlaceholder("From");
		toDatep.setPlaceholder("To");
		printParty.addClickListener(e-> printPartyReport("id_p", "political"));
		printPartylist.addClickListener(e-> printPartyReport("list_p", "political"));
		printConsti.addClickListener(e-> printConstituencyReport("id_p", "political"));
		printConstilist.addClickListener(e-> printConstituencyReport("list_p", "political"));
		printCandi.addClickListener(e-> printCandidateReport("id_p", "political"));
		printCandilist.addClickListener(e-> printCandidateReport("list_p", "political"));
		fl2.add(reportType, 8);
		fl2.add(reportFormat, 8);
		fl2.add(fromRangep, 1);
		fl2.add(toRangep, 1);
		fl2.add(printRangep, printRangelistp);
		fl2.add(fromDatep, 1);
		fl2.add(toDatep, 1);
		fl2.add( printDatesp, printDateslistp);
		fl2.add(party, 2);
		fl2.add(printParty, printPartylist);
		fl2.add(consti, 2);
		fl2.add(printConsti, printConstilist);
		fl2.add(candi, 2);
		fl2.add(printCandi, printCandilist);
		fl2.setResponsiveSteps(
		        new ResponsiveStep("0", 8),
		        // Use two columns, if layout's width exceeds 500px
		        new ResponsiveStep("500px", 8)
		);
		//Details detailsp=new Details("Political Agents", fl2);
		//detailsp.setOpened(false);
		//return detailsp;
		return fl2;
	}
	
	
	
	private void printRangeReport(String reportType, String type) {
		String format=radioGroup.getValue().trim();
		if(fromRange.getValue()==null || toRange.getValue()==null) {
			Notification.show("Please Enter Valid Values ", 3000, Position.TOP_CENTER);
		} else {
			double dfrom = fromRange.getValue();
			double dto = toRange.getValue();
			long fromValue = (long) dfrom;
			long toValue = (long) dto;
			try {
				if (fromValue > toValue) {
					Notification.show("Please Check  Values: 'From' cannot be Bigger than 'To'", 3000, Position.TOP_CENTER);
				} else {
					List<Employee> employees = dbservice.getEmployeesBetweenSerials(fromValue, toValue, type);
					//if(format=="Portrait") {
						printReports(employees, reportType, type, format);
					//}
				}

			} catch (Exception e) {
				Notification.show("Unable TO Generate Report", 5000, Position.TOP_CENTER);
				e.printStackTrace();
				// return "Error--> check the console log";
			}
		
		}
	}

	private void printRangeReportPolitical(String reportType, String type) {

		if (fromRangep.getValue() == null || toRangep.getValue() == null) {
			Notification.show("Please Enter Valid Values ", 3000, Position.TOP_CENTER);
		} else {
			double dfrom = fromRangep.getValue();
			double dto = toRangep.getValue();
			long fromValue = (long) dfrom;
			long toValue = (long) dto;
			try {
				if (fromValue > toValue) {
					Notification.show("Please Check  Values: 'From' cannot be Bigger than 'To'", 3000, Position.TOP_CENTER);
				} else {
					// System.out.println("Hello");
					List<Political> political = dbservice1.findPoliticalByRange(fromValue, toValue, type);
					//System.out.println("Hello:" + political);
					printPoliticalReports(political, reportType, type);
				}

			} catch (Exception e) {
				Notification.show("Unable TO Generate Report", 5000, Position.TOP_CENTER);
				e.printStackTrace();
				// return "Error--> check the console log";
			}

		}
	}

	private void printDatesReport(String reportType, String type) {
		String format=radioGroup.getValue().trim();
		if (fromDate.getValue() == null || toDate.getValue() == null) {
			Notification.show("Please Enter Valid Dates ", 3000, Position.TOP_CENTER);
		} else {
			LocalDate dfrom = fromDate.getValue();
			LocalDate dto = toDate.getValue();
			try {
				// boolean dfs=dfrom.
				if (dfrom.isAfter(dto)) {
					Notification.show("Please Check  Values: 'From' cannot be Bigger than 'To'", 3000, Position.TOP_CENTER);
				} else {
					// ate dt= new LocalDateConverter(dfrom);
					List<Employee> employees = dbservice.getEmployeesBetweenDates(dfrom, dto, type);
					printReports(employees, reportType, type, format);
				}

			} catch (Exception e) {
				Notification.show("Unable TO Generate Report", 5000, Position.TOP_CENTER);
				e.printStackTrace();
				// return "Error--> check the console log";
			}

		}
	}
	
	private void printDatesReportPolitical(String reportType, String type) {
		if (fromDatep.getValue() == null || toDatep.getValue() == null) {
			Notification.show("Please Enter Valid Dates ", 3000, Position.TOP_CENTER);
		} else {
			LocalDate dfrom = fromDatep.getValue();
			LocalDate dto = toDatep.getValue();
			try {
				// boolean dfs=dfrom.
				if (dfrom.isAfter(dto)) {
					Notification.show("Please Check  Values: 'From' cannot be Bigger than 'To'", 3000, Position.TOP_CENTER);
				} else {
					// ate dt= new LocalDateConverter(dfrom);
					List<Political> political = dbservice1.findPoliticalByDate(dfrom, dto, type);
					printPoliticalReports(political, reportType, type);
				}

			} catch (Exception e) {
				Notification.show("Unable TO Generate Report", 5000, Position.TOP_CENTER);
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

	private void printCellReport(String reportType, String type) {
		String format=radioGroup.getValue().trim();
		if(cell.getValue()==null) {
			Notification.show("Please Select a Cell", 3000, Position.TOP_CENTER);
		}else {
				List<Employee> employees= dbservice.getEmployeesByCell(cell.getValue(), type);
				printReports(employees, reportType, type, format);

		}
	}

	private void printOfficeReport(String reportType, String type) {
		String format=radioGroup.getValue().trim();
		if(office.getValue()==null) {
			Notification.show("Please Select an Office", 3000, Position.TOP_CENTER);
		}else {
				List<Employee> employees= dbservice.getEmployeesByOffice(office.getValue(), type);
				printReports(employees, reportType, type, format);
		}
		//return null;
	}
	
	private void printPartyReport(String reportType, String type) {
		if(party.getValue()==null) {
			Notification.show("Please Select a Cell", 3000, Position.TOP_CENTER);
		}else {
				List<Political> political= dbservice1.findPoliticalByParty(party.getValue(), type);
				printPoliticalReports(political, reportType, type);

		}
	}
	private void printCandidateReport(String reportType, String type) {
		if(candi.getValue()==null) {
			Notification.show("Please Select a Cell", 3000, Position.TOP_CENTER);
		}else {
				List<Political> political= dbservice1.findPoliticalByCandidate(candi.getValue(), type);
				printPoliticalReports(political, reportType, type);

		}
	}

	private void printConstituencyReport(String reportType, String type) {
		if(consti.getValue()==null) {
			Notification.show("Please Select a Cell", 3000, Position.TOP_CENTER);
		}else {
				List<Political> political= dbservice1.findPoliticalByConstituency(consti.getValue(), type);
				printPoliticalReports(political, reportType, type);

		}
	}

	
	private void printReports(List<Employee> employees, String reportType, String type, String format) {
		//System.out.println("Format:"+format);
		
		try {
			long districtid = dbservice.getLoggedDistrict().getDistrictId();
			//This is to get The path of reports folder.
			URL res = getClass().getClassLoader().getResource("report/1_id.jrxml");
			File file = Paths.get(res.toURI()).toFile();
			String absolutePath = file.getAbsolutePath();
			String reportPath = absolutePath.substring(0, absolutePath.length() - 11);
			//String reportPath = "D:";
			Resource resource = null;
			Resource resourceduplicate = null;
			InputStream employeeReportStream=null;
			removePdfViewer();
			
			
			if (reportType.equals("id")) {
				if (format == "Landscape1") {
					resource = new ClassPathResource("report/" + districtid + "_id.jrxml");
					resourceduplicate = new ClassPathResource("report/1_id.jrxml");
				}else if (format == "Landscape2") {
					resource = new ClassPathResource("report/" + districtid + "_id2.jrxml");
					resourceduplicate = new ClassPathResource("report/1_id2.jrxml");
				}else if (format == "Landscape3") {
					resource = new ClassPathResource("report/" + districtid + "_id3.jrxml");
					resourceduplicate = new ClassPathResource("report/1_id3.jrxml");
				}else if (format == "Landscape4") {
					resource = new ClassPathResource("report/" + districtid + "_id4.jrxml");
					resourceduplicate = new ClassPathResource("report/1_id4.jrxml");
				}else if (format == "Landscape5") {
					resource = new ClassPathResource("report/" + districtid + "_id5.jrxml");
					resourceduplicate = new ClassPathResource("report/1_id5.jrxml");
				}else if (format == "Portrait1") {
					resource = new ClassPathResource("report/" + districtid + "_idp.jrxml");
					resourceduplicate = new ClassPathResource("report/1_idp.jrxml");
				}else if (format == "Portrait2") {
					resource = new ClassPathResource("report/" + districtid + "_idpl.jrxml");
					resourceduplicate = new ClassPathResource("report/1_idpl.jrxml");
				}else {
					resource = new ClassPathResource("report/" + districtid + "_id.jrxml");
					resourceduplicate = new ClassPathResource("report/1_id.jrxml");
				}
			} else if (reportType.equals("list")) {
				resource = new ClassPathResource("report/list.jrxml");
			}
			try {
				employeeReportStream = resource.getInputStream();
			}catch (FileNotFoundException e) {
				// TODO: handle exception
				employeeReportStream = resourceduplicate.getInputStream();
				//System.out.println("Duplicate ");
			}
			JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
			JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(employees);
			// Map<String, Object> parameters = new HashMap<>();
			// parameters.put("Parameter1", "Sabaton");
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, jrBeanCollectionDataSource);
			JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "//" + user + "report.pdf");
			File a = new File(reportPath + "//" + user + "report.pdf");
			resourcerange = new StreamResource("Report.pdf", () -> createResource(a));
			pdfViewerrange = new PdfViewer();
			pdfViewerrange.setSrc(resourcerange);
			hl4.setSizeFull();
			hl4.add(pdfViewerrange);

		} catch (Exception e) {
			Notification.show("Unable TO Generate Report. Error:" + e, 5000, Position.TOP_CENTER);
			e.printStackTrace();

		}

	}
	private void printPoliticalReports(List<Political> politicals, String reportType, String type) {
		String reporttype=radioGroup1.getValue().trim();
		String reportformat=radioGroup2.getValue().trim();
		//System.out.println("Type"+reporttype);
		//System.out.println("Format"+reportformat);
		try {
			Resource resource = null;
			Resource resourceduplicate = null;
			InputStream employeeReportStream =null;
			removePdfViewer();
			
			long districtid = dbservice.getLoggedDistrict().getDistrictId();
			URL res = getClass().getClassLoader().getResource("report/1_idp.jrxml");
			File file = Paths.get(res.toURI()).toFile();
			String absolutePath = file.getAbsolutePath();
			String reportPath = absolutePath.substring(0, absolutePath.length() - 11);
			//String reportPath = "D:";
			if (reportType.equals("id_p")) {
				if (reporttype == "Candidate") {
					resource = new ClassPathResource("report/" + districtid + "_id_pc.jrxml");
					resourceduplicate = new ClassPathResource("report/1_id_pc.jrxml");
				}else {
					if(reportformat=="Portrait") {
						resource = new ClassPathResource("report/" + districtid + "_id_p.jrxml");
						resourceduplicate = new ClassPathResource("report/1_id_p.jrxml");
					}else {
						resource = new ClassPathResource("report/" + districtid + "_id_pl.jrxml");
						resourceduplicate = new ClassPathResource("report/1_id_pl.jrxml");
					}
				}
			} else if (reportType.equals("list_p")) {
				resource = new ClassPathResource("report/list_p.jrxml");

			}
			try {
				employeeReportStream = resource.getInputStream();
			}catch (FileNotFoundException e) {
				// TODO: handle exception
				employeeReportStream = resourceduplicate.getInputStream();
				//System.out.println("Duplicate ");
			}
			//InputStream employeeReportStream = resource.getInputStream();
			JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
			JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(politicals);
			// Map<String, Object> parameters = new HashMap<>();
			// parameters.put("Parameter1", "Sabaton");
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, jrBeanCollectionDataSource);
			JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "//" + user + "report.pdf");
			File a = new File(reportPath + "//" + user + "report.pdf");
			resourcerange = new StreamResource("Report.pdf", () -> createResource(a));
			pdfViewerrange = new PdfViewer();
			pdfViewerrange.setSrc(resourcerange);
			hl4.setSizeFull();
			hl4.add(pdfViewerrange);

		} catch (Exception e) {
			Notification.show("Unable TO Generate Report. Error:" + e, 5000, Position.TOP_CENTER);
			e.printStackTrace();

		}

	}
}