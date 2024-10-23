package com.identity.views;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.vaadin.lineawesome.LineAwesomeIcon;

import com.identity.dbservice.DbService;
import com.identity.entity.District;
import com.identity.entity.State;
import com.identity.entity.Users;
import com.identity.security.SecurityService;
//import com.identity.views.PrintView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

public class MainLayout extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DbService dbservice;
	private final SecurityService securityService;
	public District district_global;
	public Users logged_user;

	PasswordField oldpwd;
	PasswordField newpwd;
	PasswordField confirmpwd;
	Button cancelButton = new Button("Cancel");
	Button saveButton = new Button("Save");
	private Users user;
	Dialog dialog;
	Dialog userdialog;
	TextField userName = new TextField("User Name");
	String userType;
	ComboBox<State> state = new ComboBox("State");
	ComboBox<District> district = new ComboBox("District");
	final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	ComboBox usertype = new ComboBox("Role");

	public MainLayout(DbService dbservice, SecurityService securityService) {
		this.dbservice = dbservice;
		this.securityService = securityService;
		createHeader();
		createDrawer();
		userType = dbservice.getUser().getRole();
		usertype.setItems("ADMIN", "USER");
		// setPrimarySection(Section.DRAWER);
	}

	private void createDrawer() {
		// Create a vertical layout for the drawer content
		VerticalLayout drawerContent = new VerticalLayout();

		// Create navigation items with helper text and appropriate icons
		SideNavItemWithHelperText personnel = new SideNavItemWithHelperText("", "Personnel", EmployeeView.class,LineAwesomeIcon.USER_TIE_SOLID.create());
		SideNavItemWithHelperText political = new SideNavItemWithHelperText("", "Political", PoliticalView.class,LineAwesomeIcon.GHOST_SOLID.create());
		SideNavItemWithHelperText reports = new SideNavItemWithHelperText("", "Reports", PrintView2.class,LineAwesomeIcon.PRINT_SOLID.create());
		SideNavItemWithHelperText master = new SideNavItemWithHelperText("", "Offices & Cells", OfficeView.class,LineAwesomeIcon.BUILDING_SOLID.create());
		SideNavItemWithHelperText omaster = new SideNavItemWithHelperText("", "Political Master", PoliticalMasterView.class,LineAwesomeIcon.SNOWFLAKE.create());
		SideNavItemWithHelperText distMaster = new SideNavItemWithHelperText("", "Printing Master", MasterDistrictView.class,LineAwesomeIcon.KEYBOARD.create());
		
		// Set visibility based on roles
		
		// Add all the navigation items to the drawer content
		drawerContent.add(personnel,political,  master,omaster, distMaster, reports);

		// Add the content to the drawer
		addToDrawer(drawerContent);
	}

	private void createHeader() {
		Avatar avatarImage = new Avatar(dbservice.getloggeduser());
		avatarImage.setColorIndex(2);
		// avatarImage.addThemeVariants(AvatarVariant.LUMO_LARGE);
		MenuBar menuBar = new MenuBar();
		menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
		MenuItem item = menuBar.addItem(avatarImage);
		SubMenu subMenu = item.getSubMenu();
		subMenu.addItem("Change Password", e -> openPasswordDialog());
		subMenu.addItem("Create User", e -> createUser());
		subMenu.addItem("Logout", e -> securityService.logout());
		H2 logo = new H2("Identity Card Management : "

				+ dbservice.getLoggedDistrict().getDistrictName() + " DISTRICT" + ", "
				+ dbservice.getLoggedState().getStateName());
		logo.addClassNames("text-s", "m-m");
		// Image img = new Image("images/logo.png", "placeholder plant");
		HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, menuBar);
		header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		header.expand(logo);
		header.setWidthFull();
		header.addClassNames("py-0", "px-m");
		addToNavbar(header);
	}

	private void createUser() {
		// TODO Auto-generated method stub
		if (dbservice.getUser().getRole().equals("ADMIN") || dbservice.getUser().getRole().equals("SUPER")) {
			userdialog = new Dialog();
			VerticalLayout dialogLayout1 = createUserDialog(userdialog);
			userdialog.add(dialogLayout1);
			userdialog.open();

		} else {
			Notification.show("Please Contact Your Administrator", 3000, Position.TOP_CENTER);
		}
	}

	private VerticalLayout createUserDialog(Dialog userdialog) {
		H2 headline = new H2("Create User");
		headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0").set("font-size", "1.5em").set("font-weight",
				"bold");
		if (userType.equals("SUPER")) {
			state.setItems(dbservice.findAllStates());
			district.setItems(dbservice.findAllDistricts());
			usertype.setEnabled(true);
		} else if (userType.equals("ADMIN")) {
			state.setItems(dbservice.getLoggedState());
			district.setItems(dbservice.getLoggedDistrict());
			usertype.setValue("USER");
			usertype.setEnabled(false);
		}
		state.setItemLabelGenerator(State::getStateName);
		district.setItemLabelGenerator(District::getDistrictName);
		cancelButton.addClickListener(e -> userdialog.close());
		saveButton.addClickListener(e -> saveNewUser());
		newpwd = new PasswordField("Password");
		confirmpwd = new PasswordField("Confirm Password");
		VerticalLayout fieldLayout1 = new VerticalLayout(state, district, userName, newpwd, confirmpwd, usertype);
		fieldLayout1.setSpacing(false);
		fieldLayout1.setPadding(false);
		fieldLayout1.setAlignItems(FlexComponent.Alignment.STRETCH);

		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		HorizontalLayout buttonLayout1 = new HorizontalLayout(cancelButton, saveButton);
		buttonLayout1.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		VerticalLayout dialogLayout1 = new VerticalLayout(headline, fieldLayout1, buttonLayout1);
		dialogLayout1.setPadding(false);
		dialogLayout1.setAlignItems(FlexComponent.Alignment.STRETCH);
		dialogLayout1.getStyle().set("width", "300px").set("max-width", "100%");
		// clearDialog();
		return dialogLayout1;
	}

	private void saveNewUser() {
		// TODO Auto-generated method stub
		if (district.getValue() == null || state.getValue() == null || usertype.getValue() == null
				|| userName.getValue().equals("") || newpwd.getValue() == "" || confirmpwd.getValue() == "") {
			Notification.show("Wat Leh Jaituh Ho Lok. Enter All Values, Please", 3000, Position.TOP_CENTER);

		} else {
			if (!newpwd.getValue().equals(confirmpwd.getValue())) {
				Notification.show("Check Your Passwords, Please", 3000, Position.TOP_CENTER);
			} else {
				try {
					if (dbservice.getUserByName(userName.getValue()) == null) {
						Users users = new Users();
						System.out.println("UserID:" + dbservice.getMaxUserId());
						users.setUserId(dbservice.getMaxUserId() + 1);
						users.setDistrict(district.getValue());
						users.setState(state.getValue());
						users.setRole(usertype.getValue().toString());
						users.setUserName(userName.getValue());
						users.setPassword(passwordEncoder.encode(newpwd.getValue()));
						dbservice.saveUser(users);
						clearUserFields();
						Notification.show("User Created Successfully", 3000, Position.TOP_CENTER);
					} else {
						Notification.show("Username Already Taken. Enter Another Username", 3000, Position.TOP_CENTER);
						userName.setValue("");
						userName.focus();
					}
					// Notification.show("Wat Leh Kamkai. Enter All Values, Please", 3000,
					// Position.TOP_CENTER);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void clearUserFields() {
		district.setValue(null);
		state.setValue(null);
		userName.setValue("");
		newpwd.setValue("");
		confirmpwd.setValue("");
	}

	private void openPasswordDialog() {
		if (dialog != null) {
			dialog = null;
		}
		dialog = new Dialog();
		VerticalLayout dialogLayout = createDialogLayout(dialog);
		dialog.add(dialogLayout);
		dialog.open();
	}

	private VerticalLayout createDialogLayout(Dialog dialog) {
		H2 headline = new H2("Change Password");
		headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0").set("font-size", "1.5em").set("font-weight",
				"bold");
		oldpwd = new PasswordField("Old Password");
		newpwd = new PasswordField("New Password");
		confirmpwd = new PasswordField("Confirm New Password");
		cancelButton.addClickListener(e -> dialog.close());
		saveButton.addClickListener(e -> changePassword());
		VerticalLayout fieldLayout = new VerticalLayout(oldpwd, newpwd, confirmpwd);
		fieldLayout.setSpacing(false);
		fieldLayout.setPadding(false);
		fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, saveButton);
		buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		VerticalLayout dialogLayout = new VerticalLayout(headline, fieldLayout, buttonLayout);
		dialogLayout.setPadding(false);
		dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
		dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");
		clearDialog();
		return dialogLayout;
	}

	public void setUsers(Users user) {
		this.user = user;
	}

	private void changePassword() {
		// Notification.show("Under Development", 3000, Position.TOP_CENTER);
		if (oldpwd.getValue() == "" || newpwd.getValue() == "" || confirmpwd.getValue() == "") {
			Notification.show("Wat Leh Kamkai. Enter All Values, Please", 3000, Position.TOP_CENTER);
		} else {
			if (newpwd.getValue().equals(confirmpwd.getValue())) {
				String pwd = oldpwd.getValue();

				if (passwordEncoder.matches(pwd, dbservice.getUser().getPassword())) {
					user = dbservice.getUser();
					user.setPassword(passwordEncoder.encode(newpwd.getValue()));
					dbservice.saveUser(user);
					Notification.show("Password Changed Successfully for User:" + user.getUserName(), 3000,
							Position.TOP_CENTER);
					// dialog.close();
					clearDialog();

				} else {

					Notification.show("Unauthorised User", 3000, Position.TOP_CENTER);
				}
			} else {
				Notification.show("Please check and confirm your passwords", 3000, Position.TOP_CENTER);
			}
		}
	}

	public void clearDialog() {
		oldpwd.setValue("");
		confirmpwd.setValue("");
		newpwd.setValue("");
	}
}
