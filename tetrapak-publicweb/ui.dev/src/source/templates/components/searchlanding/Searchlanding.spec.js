import Searchlanding from './Searchlanding';

describe('Searchlanding', function () {
  before(function () {
    this.searchlanding = new Searchlanding({
      el: document.body
    });
    this.initSpy = sinon.spy(this.searchlanding, 'init');
    this.searchlanding.init();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });
});
