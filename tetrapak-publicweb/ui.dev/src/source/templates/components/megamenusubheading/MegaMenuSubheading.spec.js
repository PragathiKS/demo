import MegaMenuSubheading from './MegaMenuSubheading';

describe('MegaMenuSubheading', function () {
  before(function () {
    this.megaMenuSubheading = new MegaMenuSubheading({
      el: document.body
    });
    this.initSpy = sinon.spy(this.megaMenuSubheading, 'init');
    this.megaMenuSubheading.init();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });
});
