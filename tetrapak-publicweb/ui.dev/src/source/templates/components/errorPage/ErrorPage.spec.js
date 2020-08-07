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
  it('should initialize', function (done) {
    expect(this.errorPage.init.called).to.be.true;
    done();
  });
});
