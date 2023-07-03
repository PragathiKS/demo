import MegaMenuDescription from './MegaMenuDescription';

describe('MegaMenuDescription', function () {
  before(function () {
    this.megaMenuDescription = new MegaMenuDescription({
      el: document.body
    });
    this.initSpy = sinon.spy(this.megaMenuDescription, 'init');
    this.megaMenuDescription.init();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });
});
