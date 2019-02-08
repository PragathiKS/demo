import $ from 'jquery';

class HelloWorld {
  cache = {};
  init() {
    this.initCache();
    this.bindEvents();
  }
  initCache() {
    this.cache.$btn = $('.js-cuhu__btn');
  }
  bindEvents() {
    this.cache.$btn.on('click', this.changeText);
  }
  changeText() {
    $('.helloworld').find('span').text('Text is changed');
  }
}

export default HelloWorld;
