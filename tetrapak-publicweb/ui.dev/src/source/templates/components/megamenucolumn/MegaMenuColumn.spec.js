import MegaMenuColumn from './MegaMenuColumn';

describe('MegaMenuColumn', function () {
  before(function () {
    this.megaMenuColumn = new MegaMenuColumn({
      el: document.body
    });
    this.initSpy = sinon.spy(this.megaMenuColumn, 'init');
    this.megaMenuColumn.init();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });
});
