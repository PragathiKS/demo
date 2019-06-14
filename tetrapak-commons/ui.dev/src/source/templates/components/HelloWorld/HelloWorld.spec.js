import HelloWorld from './HelloWorld';

describe('HelloWorld', function () {
  before(function () {
    this.helloWorld = new HelloWorld({ el: document.body });
    this.helloWorldSpy = sinon.spy(this.helloWorld, 'init');
    this.helloWorld.init();
  });
  after(function () {
    this.helloWorldSpy.restore();
  });
  it('should initialize on page load', function () {
    expect(this.helloWorld.init.called).to.be.true;
  });
});
