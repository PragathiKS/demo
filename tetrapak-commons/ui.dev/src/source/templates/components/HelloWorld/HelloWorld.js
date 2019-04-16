import $ from 'jquery';

class HelloWorld {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  init() {
    this.initCache();
    this.bindEvents();
  }
  initCache() {
    this.cache.$btn = this.root.find('.js-cuhu__btn');
  }
  bindEvents() {
    this.cache.$btn.on('click', this.changeText);
  }
  changeText() {
    this.root.find('.helloworld').find('span').text('Text is changed');
  }
}

export default HelloWorld;
