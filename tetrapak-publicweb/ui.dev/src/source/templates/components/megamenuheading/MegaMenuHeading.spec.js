import MegaMenuHeading from './MegaMenuHeading';

describe('MegaMenuHeading', function () {
  before(function () {
    this.megaMenuHeading = new MegaMenuHeading({
      el: document.body
    });
    this.initSpy = sinon.spy(this.megaMenuHeading, 'init');
    this.megaMenuHeading.init();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });
});
