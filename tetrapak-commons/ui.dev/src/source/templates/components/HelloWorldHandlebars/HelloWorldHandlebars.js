import $ from 'jquery';
import { $body } from '../../../scripts/utils/commonSelectors';
import { render } from '../../../scripts/utils/render';

class HelloWorldHandlebars {
  cache = {};
  init() {
    this.initCache();
    this.bindEvents();
  }
  initCache() {
    this.cache.$dynamicText = $('.js-dynamic-text');
    this.cache.$btn = $('.js-cuhu__btn');
    this.cache.$helloWorldForm = $('.js-helloworld-form');
  }
  bindEvents() {
    this.cache.$btn.on('click', this.loadForm);
    $body.on('input', '.js-textfield', this.changeText);
  }
  loadForm = () => {
    render.fn({
      template: 'changeTextForm',
      target: this.cache.$helloWorldForm
    }, () => {
      this.cache.$helloWorldForm.find('.js-textfield').val(this.cache.$dynamicText.text());
    });
  }
  changeText = (e) => {
    this.cache.$dynamicText.text($(e.target).val());
  }
}

export default HelloWorldHandlebars;
