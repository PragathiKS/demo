import HelloWorld from '../templates/components/HelloWorld/HelloWorld';

describe('HelloWorld', function () {
  before(function () {
    this.helloWorld = new HelloWorld();
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
