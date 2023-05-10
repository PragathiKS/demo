import Accordion from './Accordion';

describe('Accordion', function () {
  before(function () {
    this.accordion = new Accordion({
      el: document.body
    });
    
    this.initSpy = sinon.spy(this.accordion, 'init');
    this.accordion.init();
  });
  after(function () {
    this.initSpy.restore();
  });
  it('should initialize', function (done) {
    expect(this.accordion.init.called).to.be.true;
    done();
  });
});
