import $ from 'jquery';

export default {
  bindEvents() {
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
    $('.pw-contactAnchorLink').click(function(e) {
      e.preventDefault();
      let target = $(this).attr('href');
      if($(target).length) {
        $('html, body').animate({
          scrollTop: parseInt($(target).offset().top, 10)
        }, 1000);
      }
    });
  },
  init() {
    this.bindEvents();
  }
};
