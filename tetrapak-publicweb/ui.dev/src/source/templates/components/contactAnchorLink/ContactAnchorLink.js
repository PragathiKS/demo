import $ from 'jquery';

class ContactAnchorLink {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$contactAnchor = $('.pw-contactAnchorLink', this.root);
  }
  goToContactForm(target) {
    if($(target).length) {
      $('html, body').animate({
        scrollTop: parseInt($(target).offset().top, 10)
      }, 1000);
    }
  }
  bindEvents() {
    const self = this;
    $(window).scroll(function() {
      var windowBottom = $(this).scrollTop() + $(this).innerHeight();
      $('.pw-contactAnchorLink').each(function() {
        /* Check the location of each desired element */
        let target = $(this).attr('href');
        if($(target).length) {
          var objectBottom = $(target).offset().top + $(target).outerHeight();
          /* If the element is completely within bounds of the window, fade it out */
          if (objectBottom < windowBottom) { //object comes into view (scrolling down)
            if ($(this).css('opacity') === '1') {
              $(this).fadeTo(300, 0);
            }
          } else if ($(this).css('opacity') === '0') { //object goes out of view (scrolling up)
            $(this).fadeTo(300, 1);
          }
        }
      });
    }).scroll();
    this.cache.$contactAnchor.click(function(e) {
      e.preventDefault();
      let target = $(this).attr('href');
      self.goToContactForm(target);
    });
  }
  init() {
    this.initCache();
    this.bindEvents();
  }
}
export default ContactAnchorLink;
