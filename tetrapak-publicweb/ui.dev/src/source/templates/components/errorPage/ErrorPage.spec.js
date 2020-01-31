import ErrorPage from './ErrorPage';

describe('Error Page', function () {
  before(function () {
    this.errorPage = new ErrorPage({
      el: document.body
    });
    this.initSpy = sinon.spy(this.errorPage, 'init');
    this.errorPage.init();
  });
  after(function () {
    this.initSpy.restore();
  });
  it('should initialize', function () {
    expect(this.initSpy.called).to.be.true;
  });
});
