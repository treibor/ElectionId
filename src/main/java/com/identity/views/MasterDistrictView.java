package com.identity.views;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import com.identity.dbservice.DbService;
import com.identity.entity.District;
import com.identity.entity.Districtmaster;
import com.identity.entity.Employee;
import com.identity.views.EmployeeForm.EmployeeFormEvent;
import com.identity.views.EmployeeForm.SaveEvent;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.menubar.MenuBar;
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
import com.vaadin.flow.data.converter.LocalDateToDateConverter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;

import jakarta.annotation.security.RolesAllowed;

@PageTitle("Printing Master")
@Route(value="masterprinting", layout=MainLayout.class)
@RolesAllowed({"SUPER", "ADMIN" })
public class MasterDistrictView extends VerticalLayout{
	MenuBar menu=new MenuBar();
	ComboBox select=new ComboBox("Select");
	Binder<Districtmaster> binder=new BeanValidationBinder<>(Districtmaster.class); 
	Notification notify=new Notification();
	MemoryBuffer buffer1=new MemoryBuffer();
	MemoryBuffer buffer2=new MemoryBuffer();
	MemoryBuffer buffer3=new MemoryBuffer();
	Image image=new Image();
	HorizontalLayout h1=new HorizontalLayout();
	HorizontalLayout h2=new HorizontalLayout();
	HorizontalLayout h3=new HorizontalLayout();
	HorizontalLayout imageContainer1=new HorizontalLayout();
	HorizontalLayout imageContainer2=new HorizontalLayout();
	HorizontalLayout imageContainer3=new HorizontalLayout();
	TextField footerFirstLine= new TextField("Signatory Line 1");
	TextField footerSecondLine= new TextField("Signatory Line 2");
	TextField footerThirdLine= new TextField("Signatory Line 3");
	TextField headerFirstLine= new TextField("Header Line 1");
	TextField headerSecondLine= new TextField("Header Line 2");
	TextField headerThirdLine= new TextField("Header Line 3");
	Upload leftupload=new Upload(buffer1);
	Upload rightupload=new Upload(buffer2);
	Upload signupload=new Upload(buffer3);
	DatePicker validity=new DatePicker("Validity of Id Card");
	Button updateButton=new Button("Update");
	private Districtmaster dist;
	DbService dbService;
	boolean isadmin;
	public MasterDistrictView(DbService dbService) {
		select.addValueChangeListener(e->populateDetails((String) e.getValue()));
		headerFirstLine.setRequired(true);
		binder.bind(headerFirstLine, "headerFirstLine");
		binder.bind(headerSecondLine, "headerSecondLine");
		binder.bind(headerThirdLine, "headerThirdLine");
		binder.bind(footerFirstLine, "footerFirstLine");
		binder.bind(footerSecondLine, "footerSecondLine");
		binder.bind(footerThirdLine, "footerThirdLine");
		binder.bind(validity, "validity");
		this.dbService=dbService;
		updateButton.setWidth("10em");
		updateButton.addClickListener(event-> updateDetails());
		add ( createTextFields());
		//populateDetails();
		this.setJustifyContentMode(JustifyContentMode.CENTER);
		this.setAlignItems(Alignment.CENTER);
		setSizeFull();
		isadmin=dbService.isAdmin();
		updateButton.setEnabled(isadmin);
	}
	public void populateDetails(String selected) {
		Districtmaster dist=null;
		if(selected=="Personnel") {
			//System.out.println("Selected:"+selected);
			dist=dbService.getDistrictMasterByLabel("employee");
		}else {
			dist=dbService.getDistrictMasterByLabel("political");
			//System.out.println("Selected:"+selected);
		}
		if (dist != null) {
			removeImageContainers();
			setDistrictMaster(dist);
			byte[] leftpicture = dist.getLeftImage();
			byte[] rightpicture = dist.getRightImage();
			byte[] signpicture = dist.getSignatureImage();
			if (leftpicture != null) {
				StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(leftpicture));
				Image image1 = new Image(resource, "No Image");
				if (imageContainer1 != null) {
					imageContainer1.removeAll();
				}
				imageContainer1.add(image1);
				h1.add(imageContainer1);
			} else {
				imageContainer1.removeAll();
			}
			if (rightpicture != null) {
				StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(rightpicture));
				Image image2 = new Image(resource, "No Image");
				if (imageContainer2 != null) {
					imageContainer2.removeAll();
				}
				imageContainer2.add(image2);
				h2.add(imageContainer2);
			} else {
				imageContainer2.removeAll();
			}
			if (signpicture != null) {
				StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(signpicture));
				Image image3 = new Image(resource, "No Image");
				if (imageContainer3 != null) {
					imageContainer3.removeAll();
				}
				imageContainer3.setWidth("100px");
				imageContainer3.setHeight("50px");
				imageContainer3.add(image3);
				h3.add(imageContainer3);
			} else {
				imageContainer3.removeAll();
			}
		}else {
			footerFirstLine.setValue("");
			footerSecondLine.setValue("");
			footerThirdLine.setValue("");
			headerFirstLine.setValue("");
			headerSecondLine.setValue("");
			headerThirdLine.setValue("");
			validity.setValue(null);
			removeImageContainers();
		}
	}
	
	public void removeImageContainers(){
		if(imageContainer1!=null) {
			imageContainer1.removeAll();
		}if(imageContainer2!=null) {
			imageContainer2.removeAll();
		}if(imageContainer3!=null) {
			imageContainer3.removeAll();
		}
	}
	
	private void updateDetails() {
		//System.out.println("Bean:"+dist);
		if(select.getValue()==null) {
			notify.show("Please Select A Valid Value", 3000, Position.TOP_CENTER);
			select.setAutofocus(true);
		} else {
			if (dist == null) {
				dist = new Districtmaster();
				//dist.setDistrict(dbService.getLoggedDistrict());
				if(select.getValue().equals("Personnel")) {
					dist.setMasterLabel("employee");
				}else {
					dist.setMasterLabel("political");
				}
			}
			try {
				// dist.setMasterLabel("employee");
				dist.setDistrict(dbService.getLoggedDistrict());
				binder.writeBean(dist);
				//System.out.println("Bean2:" + dist);
				if (getImageAsByteArray(buffer1) != null) {
					dist.setLeftImage(getImageAsByteArray(buffer1));
				}
				if (getImageAsByteArray(buffer2) != null) {
					dist.setRightImage(getImageAsByteArray(buffer2));
				}
				if (getImageAsByteArray(buffer3) != null) {
					dist.setSignatureImage(getImageAsByteArray(buffer3));
				}

				dbService.saveDistrictMaster(dist);
				clearBuffer();
				notify.show("Updated Successfully", 3000, Position.TOP_CENTER);
			} catch (ValidationException e) {
				notify.show("Please Enter All Fields");
				e.printStackTrace();
			}
		}
	}

	private void clearBuffer() {
		// TODO Auto-generated method stub
		
		buffer1=new MemoryBuffer();
		buffer2=new MemoryBuffer();
		buffer3=new MemoryBuffer();
		leftupload.setReceiver(buffer1);
		rightupload.setReceiver(buffer2);
		signupload.setReceiver(buffer3);
		try {
		leftupload.clearFileList();
		rightupload.clearFileList();
		}
		catch(Exception e) {
			notify.show("Unexpected Error While Processing Images");
		}
		signupload.clearFileList();
	}



	private Component createTextFields() {
		FormLayout fl1=new FormLayout();
		
		select.setItems("Personnel","Political");
		select.setRequired(true);
		fl1.add(select, 3);
		fl1.add(new Label(), 3);;
		fl1.setMaxWidth("800px");
		////vl1.add(headerFirstLine, headerSecondLine);
		fl1.add(headerFirstLine, 3 );
		fl1.add(headerSecondLine, 3);
		fl1.add(headerThirdLine, 3);
		fl1.add(footerFirstLine, 3);
		fl1.add(footerSecondLine, 3);
		fl1.add(footerThirdLine, 3);
		fl1.add(validity,3);
		fl1.add(createleftUploads(), 6);
		fl1.add(createrightUploads(),6);
		fl1.add(createSignUploads(),6);
		fl1.add(new Label(), new Label());
		fl1.add(updateButton, 2);
		fl1.setResponsiveSteps(
		        new ResponsiveStep("0", 6),
		        // Use two columns, if layout's width exceeds 500px
		        new ResponsiveStep("500px", 6)
		);
		//Details detail=new Details("Govt. Employees", fl1);
		//detail.setOpened(false);
		
		var vert=new VerticalLayout(fl1);
		vert.setSizeFull();
		vert.setAlignItems(Alignment.CENTER);
		vert.setJustifyContentMode(JustifyContentMode.CENTER);
		return vert;
	}
	
	private Component createleftUploads() {
		leftupload.setMaxFiles(1);
		//leftupload.setL
		//leftupload.setWidth("30em");
		leftupload.setWidthFull();
		leftupload.setMaxFileSize(1000000);
		leftupload.setUploadButton(new Button("Upload Left Image"));
		leftupload.setDropLabel(new Label("Drop Left Image"));
		leftupload.setAcceptedFileTypes("image/tiff", "image/jpeg", "image/jpg","image/jpeg", "image/png");
		leftupload.addFileRejectedListener(e -> notify.show("Invalid File: Please select only image files less than 1Mb",	3000, Position.TOP_END));
		leftupload.addSucceededListener(event -> showPicture1());
		h1.add(leftupload);
		h1.setPadding(isVisible());
		return h1;
	}
	
	
	private Component createrightUploads() {
		rightupload.setMaxFiles(1);
		rightupload.setWidthFull();
		rightupload.setMaxFileSize(1000000);
		rightupload.setUploadButton(new Button("Upload Right Image"));
		rightupload.setDropLabel(new Label("Drop right Image"));
		rightupload.setAcceptedFileTypes("image/tiff", "image/jpeg", "image/jpg", "image/jpeg","image/png");
		rightupload.addFileRejectedListener(e -> notify.show("Invalid File: Please select only image files less than 1Mb",	3000, Position.TOP_END));
		rightupload.addSucceededListener(event -> showPicture2());
		h2.add(rightupload);
		h2.setPadding(isVisible());
		return h2;
	}
	private Component createSignUploads() {
		signupload.setMaxFiles(1);
		signupload.setWidthFull();
		signupload.setMaxFileSize(1000000);
		signupload.setUploadButton(new Button("Upload Signature Image"));
		signupload.setDropLabel(new Label("Drop Signature Image"));
		signupload.setAcceptedFileTypes("image/tiff", "image/jpeg", "image/jpg","image/jpeg","image/png");
		signupload.addFileRejectedListener(e -> notify.show("Invalid File: Please select only image files less than 1Mb",	3000, Position.TOP_END));
		signupload.addSucceededListener(event -> showPicture3());
		h3.add(signupload);
		h3.setPadding(true);
		return h3;
	}
	private void showPicture2() {
		try {
			if (imageContainer2 != null) {
				h2.remove(imageContainer2);
			}
			StreamResource resource = new StreamResource("inputimage",
					() -> new ByteArrayInputStream(getImageAsByteArray(buffer2)));
			image = new Image(resource, "No Photo to display");
			imageContainer2 = new HorizontalLayout();
			imageContainer2.setWidth("100px");
			imageContainer2.setHeight("100px");
			imageContainer2.getStyle().set("overflow-x", "auto");
			imageContainer2.add(image);
			h2.add(imageContainer2);
		} catch (Exception e) {
			e.printStackTrace();
			notify.show("Error" + e);
		}
	}
	private void showPicture3() {
		try {
			if (imageContainer3 != null) {
				h3.remove(imageContainer3);
			}
			StreamResource resource = new StreamResource("inputimage",
					() -> new ByteArrayInputStream(getSignImageAsByteArray(buffer3)));
			image = new Image(resource, "No Photo to display");
			imageContainer3 = new HorizontalLayout();
			imageContainer3.setWidth("100px");
			imageContainer3.setHeight("50px");
			imageContainer3.getStyle().set("overflow-x", "auto");
			imageContainer3.add(image);
			h3.add(imageContainer3);
		} catch (Exception e) {
			e.printStackTrace();
			notify.show("Error" + e);
		}
	}
	
	private void showPicture1() {
		// TODO Auto-generated method stub
		try {
			if (imageContainer1 != null) {
				h1.remove(imageContainer1);
				//remove(imageContainer1);
			}
			StreamResource resource = new StreamResource("inputimage",
					() -> new ByteArrayInputStream(getImageAsByteArray(buffer1)));
			image = new Image(resource, "No Photo to display");
			imageContainer1 = new HorizontalLayout();
			imageContainer1.setWidth("100px");
			imageContainer1.setHeight("100px");
			imageContainer1.getStyle().set("overflow-x", "auto");
			imageContainer1.add(image);
			h1.add(imageContainer1);
		} catch (Exception e) {
			e.printStackTrace();
			notify.show("Error" + e);
		}
		
	}
	private byte[] getSignImageAsByteArray(MemoryBuffer buffer) {
		try {
			
			BufferedImage inputImageoriginal= ImageIO.read(buffer.getInputStream());
			if (inputImageoriginal == null) {
				return null;
			} else {
				BufferedImage inputImage = resizeSignImage(inputImageoriginal);
				ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
				ImageIO.write(inputImage, "png", pngContent);
				InputStream is = new ByteArrayInputStream(pngContent.toByteArray());
				return IOUtils.toByteArray(is);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private byte[] getImageAsByteArray(MemoryBuffer buffer) {
		try {
			
			BufferedImage inputImageoriginal= ImageIO.read(buffer.getInputStream());
			if (inputImageoriginal == null) {
				return null;
			} else {
				BufferedImage inputImage = resizeImage(inputImageoriginal);
				ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
				ImageIO.write(inputImage, "png", pngContent);
				InputStream is = new ByteArrayInputStream(pngContent.toByteArray());
				return IOUtils.toByteArray(is);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	BufferedImage resizeImage(BufferedImage originalImage) throws IOException {
		BufferedImage resizedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, 100, 100, null);
		graphics2D.dispose();
		return resizedImage;
	}
	BufferedImage resizeSignImage(BufferedImage originalImage) throws IOException {
		BufferedImage resizedImage = new BufferedImage(100, 50, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, 100, 50, null);
		graphics2D.dispose();
		return resizedImage;
	}
	
	public void setDistrictMaster(Districtmaster dist) {
		this.dist = dist;
		binder.readBean(dist);
	}

	public static abstract class MasterDistrictViewEvent extends ComponentEvent<MasterDistrictView> {
		private Districtmaster dist;

		protected MasterDistrictViewEvent(MasterDistrictView source, Districtmaster dist) {
			super(source, false);
			this.dist = dist;
		}

		public Districtmaster getDistrict() {
			return dist;
		}
	}

	public static class SaveEvent extends MasterDistrictViewEvent {

		SaveEvent(MasterDistrictView source, Districtmaster dist) {
			super(source, dist);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}

}
