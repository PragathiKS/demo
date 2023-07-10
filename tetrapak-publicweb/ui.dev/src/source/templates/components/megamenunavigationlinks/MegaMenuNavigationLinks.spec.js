import MegaMenuNavigationLinks from './MegaMenuNavigationLinks';

describe('MegaMenuNavigationLinks', function () {
  before(function () {
    this.megaMenuNavigationLinks = new MegaMenuNavigationLinks({
      el: document.body
    });
    this.initSpy = sinon.spy(this.megaMenuNavigationLinks, 'init');
    this.megaMenuNavigationLinks.init();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });
});
