import Header from './Header';
import $ from 'jquery';
import headerTemplate from '../../../test-templates-hbs/header.hbs';

describe('Header', function () {
  before(function () {
    $(document.body).empty().html(headerTemplate());
    this.header = new Header({ el: document.body });
    this.initHeaderSpy = sinon.spy(this.header, 'init');
    this.header.init();

  });
  after(function () {
    $(document.body).empty();
    this.initHeaderSpy.restore();
  });
  it('should initialize header', function (done) {
    expect(this.header.init.called).to.be.true;
    done();
  });
});
