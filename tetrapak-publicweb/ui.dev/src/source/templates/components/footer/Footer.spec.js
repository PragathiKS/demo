import $ from 'jquery';
import Footer from './Footer';

describe('Footer', function () {
  before(function () {
    $(document.body).empty().html(`<div class="tp-pw-footer"><button class="tp_textBtn">Top</button></div>`);
    this.footer = new Footer({
      el: document.body
    });
    this.initSpy = sinon.spy(this.footer, 'init');
    this.goToTopSpy = sinon.spy(this.footer, 'goToTop');
    this.footer.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.goToTopSpy.restore();
  });
  it('should initialize', function () {
    expect(this.initSpy.called).to.be.true;
  });
  it('should go to top on click of "top" button', function () {
    $('.tp_textBtn').trigger('click');
    expect(this.goToTopSpy.called).to.be.true;
  });
});
