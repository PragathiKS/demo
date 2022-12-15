import $ from 'jquery';
import Accordion from './Accordion';
import accordionTemplate from '../../../test-templates-hbs/accordion.hbs';

describe('Accordion', function () {
  before(function () {
    $(document.body).empty().html(accordionTemplate());
    this.accordion = new Accordion({
      el: document.body
    });
    this.initSpy = sinon.spy(this.accordion, 'init');
    this.accordion.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
  });
  it('should initialize accordion', function (done) {
    expect(this.accordion.init.called).to.be.true;
    done();
  });
});
