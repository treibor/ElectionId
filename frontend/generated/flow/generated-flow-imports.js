import { injectGlobalCss } from 'Frontend/generated/jar-resources/theme-util.js';

import { css, unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin';
import $cssFromFile_0 from 'Frontend/generated/jar-resources/styles/wizard.css?inline';

injectGlobalCss($cssFromFile_0.toString(), 'CSSImport end', document);
import $cssFromFile_1 from 'Frontend/generated/jar-resources/styles/toolbar-button.css?inline';
const $css_1 = typeof $cssFromFile_1  === 'string' ? unsafeCSS($cssFromFile_1) : $cssFromFile_1;
registerStyles('vaadin-button', $css_1, {moduleId: 'flow_css_mod_1'});
import $cssFromFile_2 from 'print-js/dist/print.css?inline';

injectGlobalCss($cssFromFile_2.toString(), 'CSSImport end', document);
import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/combo-box/theme/lumo/vaadin-combo-box.js';
import 'Frontend/generated/jar-resources/comboBoxConnector.js';
import 'Frontend/generated/jar-resources/vaadin-grid-flow-selection-column.js';
import '@vaadin/grid/theme/lumo/vaadin-grid-column.js';
import '@vaadin/integer-field/theme/lumo/vaadin-integer-field.js';
import '@vaadin/password-field/theme/lumo/vaadin-password-field.js';
import '@vaadin/app-layout/theme/lumo/vaadin-app-layout.js';
import '@vaadin/tooltip/theme/lumo/vaadin-tooltip.js';
import '@vaadin/tabs/theme/lumo/vaadin-tab.js';
import '@vaadin/icon/theme/lumo/vaadin-icon.js';
import '@vaadin/upload/theme/lumo/vaadin-upload.js';
import '@vaadin/side-nav/theme/lumo/vaadin-side-nav-item.js';
import 'Frontend/generated/jar-resources/dndConnector.js';
import '@vaadin/progress-bar/theme/lumo/vaadin-progress-bar.js';
import '@vaadin/context-menu/theme/lumo/vaadin-context-menu.js';
import 'Frontend/generated/jar-resources/contextMenuConnector.js';
import 'Frontend/generated/jar-resources/contextMenuTargetConnector.js';
import '@vaadin/form-layout/theme/lumo/vaadin-form-item.js';
import '@vaadin/grid/theme/lumo/vaadin-grid.js';
import '@vaadin/grid/theme/lumo/vaadin-grid-sorter.js';
import '@vaadin/checkbox/theme/lumo/vaadin-checkbox.js';
import 'Frontend/generated/jar-resources/gridConnector.js';
import '@vaadin/button/theme/lumo/vaadin-button.js';
import 'Frontend/generated/jar-resources/buttonFunctions.js';
import '@vaadin/details/theme/lumo/vaadin-details.js';
import 'Frontend/generated/jar-resources/menubarConnector.js';
import '@vaadin/menu-bar/theme/lumo/vaadin-menu-bar.js';
import '@vaadin/text-field/theme/lumo/vaadin-text-field.js';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/form-layout/theme/lumo/vaadin-form-layout.js';
import '@vaadin/dialog/theme/lumo/vaadin-dialog.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/app-layout/theme/lumo/vaadin-drawer-toggle.js';
import '@vaadin/horizontal-layout/theme/lumo/vaadin-horizontal-layout.js';
import '@vaadin/tabs/theme/lumo/vaadin-tabs.js';
import '@vaadin/avatar/theme/lumo/vaadin-avatar.js';
import '@vaadin/grid/theme/lumo/vaadin-grid-column-group.js';
import 'Frontend/generated/jar-resources/lit-renderer.ts';
import '@vaadin/notification/theme/lumo/vaadin-notification.js';
import '@vaadin/email-field/theme/lumo/vaadin-email-field.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '757f545d171d495d53f2b3f0307c94ff89b5d57d687d10a8fab59479d43c3908') {
    pending.push(import('./chunks/chunk-f455ff1b325819e0e3bec407d28c7e82625ec4c71994f2ba68106a7ae147339a.js'));
  }
  if (key === '5903027ff5a563a6f690e96bd1ccca7cf36cb40e31c24df6fc9bad6df1e71864') {
    pending.push(import('./chunks/chunk-6f4dccacbe1f2ebd2fbaee9438f8a201697c6aae23144dd275c042d9d87c8282.js'));
  }
  if (key === '660d9a872ddf3b2176ccc58608c9fcf7356e359c3e3ad0ba62a420e6ef6bbb28') {
    pending.push(import('./chunks/chunk-6f4dccacbe1f2ebd2fbaee9438f8a201697c6aae23144dd275c042d9d87c8282.js'));
  }
  if (key === '3c6599b9e7dedb629e783659ba9686c0b8273d0230cf9c306cf9224f2e1822c1') {
    pending.push(import('./chunks/chunk-f455ff1b325819e0e3bec407d28c7e82625ec4c71994f2ba68106a7ae147339a.js'));
  }
  if (key === '4c4161a446dfa4b327de63775a1637e65ffe1c305847be5881e4586216a13752') {
    pending.push(import('./chunks/chunk-f455ff1b325819e0e3bec407d28c7e82625ec4c71994f2ba68106a7ae147339a.js'));
  }
  if (key === '73aa86a14f574f5038b42a6e1eac2f1fdd981e17874ce4c110f44a06e5ce33c8') {
    pending.push(import('./chunks/chunk-289b6b1fa1433b60be5b6ea4b8483e49478071cf719fd014e97bf38cc151ca60.js'));
  }
  if (key === '6b8e18566ab3756401bff2586e82824b027bd92f5f2148b792736baa8414a9f2') {
    pending.push(import('./chunks/chunk-3015b79b3f16ff5c3a27fe5276ed94715b7e13c4ca56adbd9b3954463b1c2aad.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;