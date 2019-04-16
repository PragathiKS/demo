import $ from 'jquery';
import {dynamic} from '../../../scripts/utils/dynamicMedia';

class DynamicMediaImage {
  constructor({el}) {
    this.root = $(el);
  }

  bindEvents() {
    dynamic._processImageAttributes(this.root);
  }


  init() {
    /* Mandatory method */
    this.bindEvents();
  }
}

export default DynamicMediaImage;
