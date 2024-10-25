package com.identity.views;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
//import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import com.identity.dbservice.DbService;
import com.identity.entity.Cell;
import com.identity.entity.Employee;
import com.identity.entity.Office;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;

import elemental.json.Json;

public class EmployeeForm extends FormLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DbService dbservice;
	MainLayout mlayout;
	//MainLayout mainlayout=new MainLayout(DbService);
	Binder<Employee> binder = new BeanValidationBinder<>(Employee.class);
	TextField firstName = new TextField("First Name");
	TextField lastName = new TextField("Last Name");
	TextField designation = new TextField("Designation");
	ComboBox<Office> office = new ComboBox("Offices");
	ComboBox<Cell> cell = new ComboBox("Cells");
	Image image;
	Button save = new Button("Save");
	Button delete = new Button("Delete");
	Button close = new Button("Close");
	//FileBuffer buffer=new FileBuffer();
	MemoryBuffer buffer= new MemoryBuffer();
	Upload upload= new Upload(buffer); 
	private Employee employee;
	//private LocalDate dt;
	
	VerticalLayout imageContainer=new VerticalLayout();
	BufferedImage inputImageoriginal=null;
	boolean admin;
	public EmployeeForm(List<Office> offices, List<Cell> cells, DbService dbservice) {
		this.dbservice=dbservice;
		admin=dbservice.isAdmin();
		//mlayout=new MainLayout(dbservice);
		binder.bindInstanceFields(this);
		office.setItems(dbservice.findOfficesBydistrict());
		cell.setItems(dbservice.findCellsBydistrict());
		office.setItemLabelGenerator(Office::getOfficeName);
		cell.setItemLabelGenerator(Cell::getCellName);
		add(firstName, lastName, designation, office, cell, createUpload(), createButtonsLayout(), imageContainer);
	}
	
	private Component createUpload() {
		upload.setMaxFiles(1);
		upload.setMaxFileSize(100000);
		upload.setUploadButton(new Button ("Upload Photo"));
		upload.setDropLabel(new Div(new Text("Drop Photo")));
		upload.setAcceptedFileTypes("image/tiff", "image/jpeg", "image/jpg");
		upload.addFileRejectedListener(e -> Notification.show("Invalid File: Please select only image files less than 100kb",3000, Position.TOP_END));
		upload.addSucceededListener(event -> showPicture());
		return upload;
	}

	public void showPicture() {
		try {
			if (imageContainer != null) {
				remove(imageContainer);
			}
			StreamResource resource = new StreamResource("inputimage",
					() -> new ByteArrayInputStream(getImageAsByteArray()));
			image = new Image(resource, "No Photo to display");
			imageContainer = new VerticalLayout();
			imageContainer.setWidth("200px");
			imageContainer.setHeight("200px");
			imageContainer.getStyle().set("overflow-x", "auto");
			imageContainer.add(image);
			add(imageContainer);
			//imageContainer.get
		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("Error" + e);
		}
	}
	
	public void clearBuffer() {
		//UI.getCurrent().getPage().reload();/// last option to clear buffer
		buffer=new MemoryBuffer();
		upload.setReceiver(buffer);
		try {
			upload.clearFileList();
			upload.getElement().setPropertyJson("files", Json.createArray());
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private byte[] getImageAsByteArray() {
		try {
			
			inputImageoriginal= ImageIO.read(buffer.getInputStream());
			if (inputImageoriginal == null) {
				return null;
			} else {
				BufferedImage inputImage = resizeImage(inputImageoriginal);
				ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
				ImageIO.write(inputImage, "jpg", pngContent);
				InputStream is = new ByteArrayInputStream(pngContent.toByteArray());
				return IOUtils.toByteArray(is);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	BufferedImage resizeImage(BufferedImage originalImage) throws IOException {
		BufferedImage resizedImage = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, 150, 150, null);
		graphics2D.dispose();
		return resizedImage;
	}

	private Component createButtonsLayout() {
		// TODO Auto-generated method stub
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		save.addClickShortcut(Key.ENTER);
		save.addClickListener(event -> validatandSave());
		delete.addClickListener(event -> fireEvent(new DeleteEvent(this, employee)));
		//delete.setEnabled(admin);
		close.addClickListener(event -> fireEvent(new CloseEvent(this)));
		HorizontalLayout hl1=new HorizontalLayout(save, delete, close);
		hl1.setFlexGrow(1, save);
		hl1.setFlexGrow(1, delete);
		hl1.setFlexGrow(1, close);
		hl1.setWidthFull();
		hl1.setWidthFull();
		return hl1;
	}

	private void validatandSave() {
		if (dbservice.getDistrictMasterByLabel("employee") == null) {
			Notification.show("Please Initialise District-Printing Data Before Entering Any Employees", 3000, Position.TOP_CENTER);
		} else {
			try {
				// LocalDate lt=new LocalDate().now();
				// dt=new DateLocal();
				binder.writeBean(employee);
				// System.out.println("Serial No:"+ employee.getSerialNo());
				if (employee.getSerialNo() == 0) {
					employee.setSerialNo(dbservice.findMaxSerialNo() + 1);
				}
				employee.setDistrict(dbservice.getLoggedDistrict());
				employee.setState(dbservice.getLoggedState());
				employee.setEnteredBy(dbservice.getloggeduser());
				employee.setEnteredOn(LocalDate.now());
				employee.setDistrictmaster(dbservice.getDistrictMasterByLabel("employee"));
				if (getImageAsByteArray() != null) {
					employee.setPicture(getImageAsByteArray());
				}

				// employee.setDistrict(mlayout.districtop.get());
				fireEvent(new SaveEvent(this, employee));
				clearBuffer();

			} catch (ValidationException e) {
				// TODO Auto-generated catch block
				Notification.show("Please Enter All Required Fields", 3000,
						Position.TOP_CENTER);
			}
		}
		//newBuffer();
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
		binder.readBean(employee);
	}

///Operation Save/Delete Events

	public static abstract class EmployeeFormEvent extends ComponentEvent<EmployeeForm> {
		private Employee employee;

		protected EmployeeFormEvent(EmployeeForm source, Employee employee) {
			super(source, false);
			this.employee = employee;
		}

		public Employee getEmployee() {
			return employee;
		}
	}

	public static class SaveEvent extends EmployeeFormEvent {
		SaveEvent(EmployeeForm source, Employee employee) {
			super(source, employee);
		}
	}

	public static class DeleteEvent extends EmployeeFormEvent {
		DeleteEvent(EmployeeForm source, Employee employee) {
			super(source, employee);
		}

	}

	public static class CloseEvent extends EmployeeFormEvent {
		CloseEvent(EmployeeForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}

	public void removeImage() {
		if(imageContainer!=null) {
			imageContainer.removeAll();
		}
	}

	public void addImage(Image image2) {
		// TODO Auto-generated method stub
		// remove(imageContainer);
		if(imageContainer!=null) {
			imageContainer.removeAll();
		}
		imageContainer.add(image2);
		//add(imageContainer);
	
	}
}

