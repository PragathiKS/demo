import MegaMenuConfig from './MegaMenuConfig';

describe('MegaMenuConfig', function () {
  before(function () {
    this.megaMenuConfig = new MegaMenuConfig({
      el: document.body
    });
    this.initSpy = sinon.spy(this.megaMenuConfig, 'init');
    this.megaMenuConfig.init();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });
});
