package com.identity.konva;

import java.util.function.Consumer;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.shared.Registration;

import elemental.json.JsonValue;

/**
 * Vaadin wrapper for the <konva-canvas> web component.
 */
@Tag("konva-canvas") 
@JavaScript("./konva.min.js")
@JsModule("./konva-canvas.js")
@NpmPackage(value = "konva", version = "^8.3.13")
public class KonvaCanvas extends Component implements HasSize{

    /** Adds a text node with default styling. */
    public void addText(String text) {
        getElement().callJsFunction("addText", text);
    }

    /** Adds a rectangle placeholder (for images). */
    public void addImage(int width, int height) {
        getElement().callJsFunction("addImage", width, height);
    }

    /** Adds a line from (x1,y1) to (x2,y2). */
    public void addLine(int x1, int y1, int x2, int y2) {
        getElement().callJsFunction("addLine", x1, y1, x2, y2);
    }

    /** Adds a rectangle shape. */
    public void addRect(int width, int height) {
        getElement().callJsFunction("addRect", width, height);
    }

    /** Exports the current layout as JSON via callback. */
        private Consumer<String> exportCallback;

    /** Exports the current layout as JSON via callback. */
    public void exportJson(Consumer<String> callback) {
        this.exportCallback = callback;
        // Calls client-side exportJson and then invokes receiveExportJson on server
        getElement().executeJs("this.exportJson().then(json => $0.$server.receiveExportJson(json));", getElement());
    }

    /** ClientCallable to receive exported JSON from client */
    @ClientCallable
    private void receiveExportJson(String json) {
        if (exportCallback != null) {
            exportCallback.accept(json);
        }
    }

    /** Loads a saved JSON layout. */
    public void loadJson(String json) {
        getElement().callJsFunction("loadJson", json);
    }

    // Styling APIs
    public void setFontFamily(String family)   { getElement().callJsFunction("setFontFamily", family); }
    public void setFontSize(int size)          { getElement().callJsFunction("setFontSize", size); }
    public void setFontStyle(String style)     { getElement().callJsFunction("setFontStyle", style); }
    public void setUnderline(boolean u)        { getElement().callJsFunction("setUnderline", u); }
    public void setAlign(String align)         { getElement().callJsFunction("setAlign", align); }
    public void setLetterSpacing(double val)   { getElement().callJsFunction("setLetterSpacing", val); }
    public void setLineHeight(double val)      { getElement().callJsFunction("setLineHeight", val); }
    public void setFillColor(String color)     { getElement().callJsFunction("setFillColor", color); }
    public void setStrokeColor(String color)   { getElement().callJsFunction("setStrokeColor", color); }
    public void setStrokeWidth(double width)   { getElement().callJsFunction("setStrokeWidth", width); }
    public void setOpacity(double opacity)     { getElement().callJsFunction("setOpacity", opacity); }

    /**
     * Adds a listener for when shapes are selected.
     * Usage: canvas.addShapeSelectListener(event -> {
     *   JsonObject detail = event.getDetail();
     *   // ...
     * });
     */
        /**
     * Adds a listener for when shapes are selected.
     * Usage: canvas.addShapeSelectListener(event -> {
     *   String type = event.getType();
     *   JsonValue attrs = event.getAttrs();
     *   // ...
     * });
     */
    public Registration addShapeSelectListener(ComponentEventListener<ShapeSelectedEvent> listener) {
        // Use Vaadin component-level event registration
        return addListener(ShapeSelectedEvent.class, listener);
    }

    /** Event fired from the client when a shape is selected. */
    @DomEvent("shape-selected")
public static class ShapeSelectedEvent extends ComponentEvent<KonvaCanvas> {
    private final String type;
    private final JsonValue attrs;

    public ShapeSelectedEvent(KonvaCanvas source, boolean fromClient,
                              @EventData("event.detail.type") String type,
                              @EventData("event.detail.attrs") JsonValue attrs) {
        super(source, fromClient);
        this.type = type;
        this.attrs = attrs;
    }

    public String getType() { return type; }
    public JsonValue getAttrs() { return attrs; }
}
}
