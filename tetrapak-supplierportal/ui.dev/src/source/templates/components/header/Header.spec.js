import Header from './Header';
import $ from 'jquery';
import headerTemplate from '../../../test-templates-hbs/header.hbs';

describe('Header', function () {
  before(function () {
    $(document.body).empty().html(headerTemplate());
    this.header = new Header({ el: document.body });
    this.initSpy = sinon.spy(this.header, 'init');
    this.header.init();

  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
  });

  it('should initialize', function (done) {
    expect(this.header.init.called).to.be.true;
    done();
  });
});
