import $ from 'jquery';
import Embedpowerbireport from './Embedpowerbireport';
import embedpowerbireportTemplate from '../../../test-templates-hbs/embedpowerbireport.hbs';

describe('Embedpowerbireport', function () {
  function setDom($this) {
    $(document.body).empty().html(embedpowerbireportTemplate());
    $this.embedpowerbireport = new Embedpowerbireport({
      el: document.body
    });
  }
  before(function () {    
    setDom(this);
    this.initSpy = sinon.spy(this.embedpowerbireport, 'init');
    this.bindEventsSpy = sinon.spy(this.embedpowerbireport, 'bindEvents');
    this.renderPowerBIReportSpy = sinon.spy(this.embedpowerbireport, 'renderPowerBIReport');  
    this.embedpowerbireport.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.bindEventsSpy.restore();
    this.renderPowerBIReportSpy.restore();

  });
  it('should initialize component', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });  
  it('should call renderPowerBIReport function', function (done) {
    expect(this.embedpowerbireport.renderPowerBIReport.called).to.be.true;
    done();
  });
  it('should get iframe and it should be equal to 2', function (done) {
    expect($('#reportContainer').children).to.have.length(2);
    done();
  });
});
