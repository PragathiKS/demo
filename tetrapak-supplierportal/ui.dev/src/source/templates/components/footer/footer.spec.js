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
  it('should initialize', function (done) {
    expect(this.footer.init.called).to.be.true;
    done();
  });
});
