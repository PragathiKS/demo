import $ from 'jquery';

class ContactUs {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    /**
     * Use "this.root" to find elements within current component
     * Example:
     * this.cache.$submitBtn = this.root.find('.js-submit-btn');
     */
    this.cache.$submitBtn = $('button[type="submit"]', this.root);
    this.cache.$nextbtn = this.root.find('.tpatom-btn');
  }
  bindEvents() {
    /* Bind jQuery events here */
    // const self = this;
    /**
     * Example:
     * const { $submitBtn } = this.cache;
     * $submitBtn.on('click', () => { ... });
     */
    this.cache.$nextbtn.click(function () {
      const target = $(this).data('target');
      $('.tab-pane').removeClass('active');
      $(target).addClass('active');
      console.log('target>>> ', target); //eslint-disable-line
    });

  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default ContactUs;
