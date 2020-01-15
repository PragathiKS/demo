import HelloEarth from './HelloEarth';

describe('HelloEarth', function () {
  before(function () {
    this.helloEarth = new HelloEarth({ el: document.body });
    this.helloEarthSpy = sinon.spy(this.helloEarth, 'init');
    this.helloEarth.init();
  });
  after(function () {
    this.helloEarthSpy.restore();
  });
  it('should initialize on page load', function () {
    expect(this.helloEarth.init.called).to.be.true;
  });
});
