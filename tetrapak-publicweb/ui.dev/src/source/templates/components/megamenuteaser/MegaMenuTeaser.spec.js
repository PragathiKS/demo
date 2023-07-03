import MegaMenuTeaser from './MegaMenuTeaser';

describe('MegaMenuTeaser', function () {
  before(function () {
    this.megaMenuTeaser = new MegaMenuTeaser({
      el: document.body
    });
    this.initSpy = sinon.spy(this.megaMenuTeaser, 'init');
    this.megaMenuTeaser.init();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });
});
