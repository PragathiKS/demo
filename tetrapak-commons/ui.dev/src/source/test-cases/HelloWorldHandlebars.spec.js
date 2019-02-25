import HelloWorldHandlebars from '../templates/components/HelloWorldHandlebars/HelloWorldHandlebars';

describe('HelloWorldHandlebars', function () {
  before(function () {
    this.helloWorldHandlebars = new HelloWorldHandlebars();
    this.helloWorldHandlebarsSpy = sinon.spy(this.helloWorldHandlebars, 'init');
    this.helloWorldHandlebars.init();
  });
  after(function () {
    this.helloWorldHandlebarsSpy.restore();
  });
  it('should initialize on page load', function () {
    expect(this.helloWorldHandlebars.init.called).to.be.true;
  });
});
