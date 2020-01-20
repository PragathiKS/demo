import $ from 'jquery';
import { $win, $global } from '../../../scripts/utils/commonSelectors';

class ContactAnchorLink {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$contactAnchor = this.root.find('.js-pw-contactAnchorLink');
  }
  goToContactForm(e) {
    e.preventDefault();
    const $target = $($(this).attr('href'));
    if ($target.length) {
      $global.animate({
        scrollTop: parseInt($target.offset().top, 10)
      }, 1000);
    }
  }
  bindEvents() {
    const { $contactAnchor } = this.cache;
    $win.on('scroll', function () {
      const $this = $(this);
      var windowBottom = $this.scrollTop() + $this.innerHeight();
      $contactAnchor.each(function () {
        const $this = $(this);
        const $target = $($this.attr('href')); // Hash link used as ID selector
        if ($target.length) {
          var objectBottom = $target.offset().top + $target.outerHeight();
          $this[(objectBottom < windowBottom) ? 'addClass' : 'removeClass']('fade-out');
        }
      });
    }).scroll();
    $contactAnchor.on('click', this.goToContactForm);
  }
  init() {
    this.initCache();
    this.bindEvents();
  }
}
export default ContactAnchorLink;
