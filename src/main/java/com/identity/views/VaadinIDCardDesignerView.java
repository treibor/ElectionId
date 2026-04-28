package com.identity.views;

import org.vaadin.addons.tatu.ColorPicker;

import com.identity.konva.KonvaCanvas;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;


@Route(value = "designer", layout = MainLayout.class)
@RolesAllowed({ "USER", "SUPER", "ADMIN" })
@JsModule("./konva.min.js")
public class VaadinIDCardDesignerView extends VerticalLayout {

    

    public VaadinIDCardDesignerView() {
       setSizeFull();
       Button abc=new Button("Button");
       KonvaCanvas kc = new KonvaCanvas();
       kc.setWidth("100%");
       kc.setHeight("600px");

       // Defer shape creation until after the component is attached
       kc.addAttachListener(event -> {
         kc.addText("Hello!");
         kc.addRect(120, 80);
         kc.addLine(0, 0, 200, 0);
       });

       add(kc);
  
       add(kc,abc);
    }

}