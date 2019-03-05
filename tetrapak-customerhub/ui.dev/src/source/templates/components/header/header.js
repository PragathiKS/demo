import $ from 'jquery';

class header {
  constructor({ el }) {
    this.root = $(el);
  }

  init() {
    this.root.find('.tp-header__burger-menu').on('click', () => {
      $(document).trigger('showLeftNav');
    });
  }
}

export default header;

