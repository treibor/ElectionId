package com.identity.views;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import com.identity.dbservice.DbService;
import com.identity.dbservice.DbServicePol;
import com.identity.entity.Candidate;
import com.identity.entity.Cell;
import com.identity.entity.Constituency;
import com.identity.entity.District;
import com.identity.entity.Political;
import com.identity.entity.Office;
import com.identity.entity.Party;
import com.identity.entity.Political;
import com.identity.views.PoliticalForm;
import com.identity.views.PoliticalForm.CloseEvent;
import com.identity.views.PoliticalForm.PoliticalFormEvent;
import com.identity.views.PoliticalForm.DeleteEvent;
import com.identity.views.PoliticalForm.SaveEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;

import elemental.json.Json;

public class PoliticalForm extends FormLayout {
	DbServicePol dbservice;
	DbService dbservic;
	//MainLayout mlayout;
	Binder<Political> binder = new BeanValidationBinder<>(Political.class);
	TextField firstName = new TextField("First Name");
	TextField lastName = new TextField("Last Name");
	LocalDate ld;
	//TextField designation = new TextField("Designation");
	ComboBox<Party> party = new ComboBox("Party");
	ComboBox<Constituency> constituency = new ComboBox("Assembly Constituency");
	ComboBox<Candidate> candidate = new ComboBox("Candidate");
	ComboBox<Cell> cell=new ComboBox("Cell");
	Image image;
	Button save = new Button("Save");
	Button delete = new Button("Delete");
	Button close = new Button("Close");
	MemoryBuffer buffer= new MemoryBuffer();
	Upload upload= new Upload(buffer); 
	private Political political;
	Notification notify = new Notification();
	VerticalLayout imageContainer=new VerticalLayout();
	BufferedImage inputImageoriginal=null;
	public PoliticalForm(List<Party> parties, List<Constituency> constis, List <Candidate> candis, List<Cell> cells, DbServicePol dbservice, DbService dbservic) {
		this.dbservice=dbservice;
		this.dbservic=dbservic;
		candidate.addValueChangeListener(e-> getCandiDetails());
		party.setVisible(false);
		constituency.setVisible(false);
		binder.bindInstanceFields(this);
		party.setItems(dbservice.findPartyBydistrict());
		constituency.setItems(dbservice.findConstBydistrict());
		candidate.setItems(dbservice.findCandidateBydistrict());
		cell.setItems(dbservic.findCellsBydistrict());
		party.setItemLabelGenerator(Party::getPartyName);
		constituency.setItemLabelGenerator(Constituency::getConstituencyName);
		candidate.setItemLabelGenerator(Candidate :: getCandidateName);
		cell.setItemLabelGenerator(Cell::getCellName);
		add(firstName, lastName, candidate, party, constituency,cell, createUpload(), createButtonsLayout(), imageContainer);
	}
	
	private void getCandiDetails() {
		if (candidate.getValue() != null) {
			String candiName=candidate.getValue().getCandidateName().toString();
			party.setValue(dbservice.getCandidateDetails(candiName).getParty());
			constituency.setValue(dbservice.getCandidateDetails(candiName).getConstituency());
		}
	}

	
	private void getCandidate() {
		// TODO Auto-generated method stub
		if (party.getValue() != null && constituency.getValue() != null) {
			candidate.setValue(dbservice.getCandidateComboBox(party.getValue(), constituency.getValue()));
		}
	}


	private Component createUpload() {
		upload.setMaxFiles(1);
		upload.setMaxFileSize(100000);
		//upload.setDropLabelIcon(new Icon(VaadinIcon.AIRPLANE));
		upload.setUploadButton(new Button ("Upload Photo"));
		upload.setDropLabel(new Label("Drop Photo"));
		upload.setAcceptedFileTypes("image/tiff", "image/jpeg", "image/jpg");
		upload.addFileRejectedListener(e -> notify.show("Invalid File: Please select only image files less than 100kb",3000, Position.TOP_END));
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
			notify.show("Error" + e);
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
		save.setWidthFull();
		delete.setWidthFull();
		close.setWidthFull();
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		save.addClickListener(event -> validatandSave());
		delete.addClickListener(event -> fireEvent(new DeleteEvent(this, political)));
		close.addClickListener(event -> fireEvent(new CloseEvent(this)));
		return new HorizontalLayout(save, delete, close);
	}

	private void validatandSave() {
		if (dbservice.getDistrictMasterByLabel("political") == null) {
			notify.show("Please Initialise District-Printing Data Before Entering Any Political Agents", 5000, Position.TOP_CENTER);
		} else {
			try {
				binder.writeBean(political);
				if (political.getSerialNo() == 0) {
					political.setSerialNo(dbservice.findMaxSerialNo() + 1);
				}
				political.setDistrictmaster(dbservice.getDistrictMasterByLabel("political"));
				political.setDistrict(dbservice.getLoggedDistrict());
				political.setState(dbservice.getLoggedState());
				political.setEnteredBy(dbservice.getloggeduser());
				political.setEnteredOn(ld.now());
				if (getImageAsByteArray() != null) {
					political.setPicture(getImageAsByteArray());
				}
				fireEvent(new SaveEvent(this, political));
				clearBuffer();

			} catch (ValidationException e) {
				Notification notification = Notification.show("Please Enter All Required Fields", 3000,
						Position.TOP_CENTER);
			}
		}
	}

	public void setPolitical(Political political) {
		this.political = political;
		binder.readBean(political);
	}

///Operation Save/Delete Events

	public static abstract class PoliticalFormEvent extends ComponentEvent<PoliticalForm> {
		private Political political;

		protected PoliticalFormEvent(PoliticalForm source, Political political) {
			super(source, false);
			this.political = political;
		}

		public Political getPolitical() {
			return political;
		}
	}

	public static class SaveEvent extends PoliticalFormEvent {
		SaveEvent(PoliticalForm source, Political political) {
			super(source, political);
		}
	}

	public static class DeleteEvent extends PoliticalFormEvent {
		DeleteEvent(PoliticalForm source, Political political) {
			super(source, political);
		}

	}

	public static class CloseEvent extends PoliticalFormEvent {
		CloseEvent(PoliticalForm source) {
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

