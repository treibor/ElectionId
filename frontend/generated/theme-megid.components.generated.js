import { unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin/register-styles';

import vaadinRadioButtonCss from 'themes/megid/components/vaadin-radio-button.css?inline';


if (!document['_vaadintheme_megid_componentCss']) {
  registerStyles(
        'vaadin-radio-button',
        unsafeCSS(vaadinRadioButtonCss.toString())
      );
      
  document['_vaadintheme_megid_componentCss'] = true;
}

if (import.meta.hot) {
  import.meta.hot.accept((module) => {
    window.location.reload();
  });
}

