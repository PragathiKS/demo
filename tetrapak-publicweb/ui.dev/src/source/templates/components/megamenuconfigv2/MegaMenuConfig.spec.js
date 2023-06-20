import MegaMenuConfigV2 from './MegaMenuConfigV2';

describe('MegaMenuConfigV2', function () {
  before(function () {
    this.megaMenuConfig = new MegaMenuConfigV2({
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
