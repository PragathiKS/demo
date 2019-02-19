import $ from 'jquery';
import 'bootstrap';

class modalWindow {
  cache = {};
  initCache() {
    /* Initialize cache here */
  }
  bindEvents() {
    /* Bind jQuery events here */
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    $('#myModal').modal();
  }
}

export default modalWindow;