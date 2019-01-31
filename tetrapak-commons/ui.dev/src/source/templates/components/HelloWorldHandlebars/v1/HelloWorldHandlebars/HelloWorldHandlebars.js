import $ from 'jquery';
import { $body } from '../../../../../scripts/utils/commonSelectors';

class HelloWorldHandlebars {
  constructor({ templates }) {
    this.templates = templates;
  }
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
    this.cache.$helloWorldForm.html(this.templates.textCtaComponent({
      dateSubTitle: {
        subTitleAbove: 'Subtext Lorem Ipsum',
        date: '2018-07-09'
      },
      title: 'Lorem ipsum header',
      text: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. In laoreet sem et metus varius rutrum. Vivamus et sapien quis leo faucibus accumsan.',
      btnText: 'Lorem Ipsum',
      btnTextIcon: 'icon-Circle_Arrow_Right'
    }));
  }
  changeText = (e) => {
    this.cache.$dynamicText.text($(e.target).val());
  }
}

export default HelloWorldHandlebars;
