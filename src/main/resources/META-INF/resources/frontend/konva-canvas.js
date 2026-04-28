import Konva from 'konva';
import { LitElement, html, css } from 'lit';

export class KonvaCanvas extends LitElement {
  static styles = css`
    :host {
      display: block;
      width: 100%;
      height: 100%;
    }
    #container {
      width: 100%;
      height: 100%;
      position: relative;
    }
  `;

  render() {
    return html`<div id="container"></div>`;
  }

  firstUpdated() {
    const container = this.renderRoot.getElementById('container');
    // Initialize stage with current container size
    this.stage = new Konva.Stage({
      container,
      width: container.clientWidth,
      height: container.clientHeight
    });
    this.layer = new Konva.Layer();
    this.stage.add(this.layer);

    // Observe resizing to keep stage in sync
    const resizeObserver = new ResizeObserver(entries => {
      for (let entry of entries) {
        const { width, height } = entry.contentRect;
        this.stage.width(width);
        this.stage.height(height);
        this.stage.batchDraw();
      }
    });
    resizeObserver.observe(container);
  }

  // Shape creation methods...
  addText(text = 'Text') {
    const txt = new Konva.Text({ text, x: 50, y: 50, fontSize: 20, fill: '#000', draggable: true });
    this._addShape(txt);
  }

  addRect(width = 150, height = 100) {
    const rect = new Konva.Rect({ x: 150, y: 150, width, height, fill: '#add8e6', stroke: '#000', strokeWidth: 2, draggable: true });
    this._addShape(rect);
  }

  addLine(x1 = 0, y1 = 0, x2 = 200, y2 = 0) {
    const line = new Konva.Line({ points: [x1, y1, x2, y2], stroke: '#000', strokeWidth: 2, draggable: true });
    this._addShape(line);
  }

  addImage(width = 100, height = 100) {
    const imgBox = new Konva.Rect({ x: 100, y: 100, width, height, fill: '#ccc', stroke: '#000', strokeWidth: 1, draggable: true });
    this._addShape(imgBox);
  }

  _addShape(shape) {
    this.layer.add(shape);
    this._attachTransformer(shape);
    this._selectShape(shape);
    this.layer.draw();
  }

  _attachTransformer(shape) {
    if (!this.tr) {
      this.tr = new Konva.Transformer({ rotateEnabled: true });
      this.layer.add(this.tr);
    }
    shape.on('click', () => this._selectShape(shape));
  }

  _selectShape(shape) {
    this.tr.nodes([shape]);
    this.selected = shape;
    this.layer.draw();
    this.dispatchEvent(new CustomEvent('shape-selected', { detail: { type: shape.getClassName(), attrs: shape.getAttrs() } }));
  }

  // Styling APIs omitted for brevity...

  exportJson() {
    return this.stage.toJSON();
  }

  loadJson(json) {
    Konva.Node.create(json, this.stage.container());
    this.layer = this.stage.getLayers()[0];
    this.layer.draw();
  }
}

customElements.define('konva-canvas', KonvaCanvas);
