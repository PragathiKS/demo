import $ from 'jquery';
import Footer from './Footer';
import footerTemplate from '../../../test-templates-hbs/footer.hbs';

describe('Footer', function () {
  before(function () {
    $(document.body).empty().html(footerTemplate());
    this.footer = new Footer({
      el: document.body
    });
    this.initSpy = sinon.spy(this.footer, 'init');
    this.footer.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
  });
  it('should trigger "showlanuagepreferencepopup" event on click of change language', function (done) {
    $('.js-footer__change-lang').on('click', () => {
      done();
    });
    $('.js-footer__change-lang').trigger('click');
  });
});
