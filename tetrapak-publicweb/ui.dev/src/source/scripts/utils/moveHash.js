import $ from 'jquery';
import { scrollToElement } from '../common/common';

export default () => {
  const hash = window.location.hash;
  if (hash) {
    $(window).on('load', function() {
      scrollToElement(null, hash);
    });
  }
};
