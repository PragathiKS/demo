import $ from 'jquery';
import Rkliabilityconditions from './Rkliabilityconditions';
import rkLiabilityPDFLinksData from './data/rkLiabilityPDFLinks.json';
import rkLiabilityPDFLinksTemplate from '../../../test-templates-hbs/rkLiabilityPDFLinks.hbs';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
// import auth from '../../../scripts/utils/auth';

describe('Rkliabilityconditions', function () {
  const jqRef = {
    setRequestHeader() {
      // Dummy method
    }
  };
  function setDom($this) {
    $(document.body).empty().html(rkLiabilityPDFLinksTemplate());
    $this.rkliabilityConditions = new Rkliabilityconditions({
      el: document.body
    });
  }
  function ajaxResponse(response, operation = 'success') {
    const pr = $.Deferred();
    if(operation === 'success') {
      pr.resolve(response, operation, jqRef);
    } else {
      pr.reject(response);
    }
    
    return pr.promise();
  };
  before(function () {
    setDom(this);
    this.initSpy = sinon.spy(this.rkliabilityConditions, 'init');
    this.bindEventsSpy = sinon.spy(this.rkliabilityConditions, 'bindEvents');
    this.getPDFDocumentLinksSpy = sinon.spy(this.rkliabilityConditions, 'getPDFDocumentLinks');
    this.renderPDFLinksSpy = sinon.spy(this.rkliabilityConditions, 'renderPDFLinks');
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    const apiResponse = {data :[{...rkLiabilityPDFLinksData }]};
    this.ajaxStub.returns(ajaxResponse(apiResponse));
    this.openStub = sinon.stub(window, 'open');
    // this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
    //   data: {
    //     access_token: 'eyJleHAiOjE2NzkzODkyMzAsImlhdCI6MTY3OTM4ODYzMH0.fD4ORA2N9_lh9tRWiRDBB69X05YbPUCpQalTuPAdGOc',
    //     expires_in: '43199',
    //     token_type: 'BearerToken'
    //   }
    // });
    this.rkliabilityConditions.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.bindEventsSpy.restore();
    this.getPDFDocumentLinksSpy.restore();
    this.renderPDFLinksSpy.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.openStub.restore();
    // this.tokenStub.restore();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    expect(this.bindEventsSpy.called).to.be.true;
    done();
  });

  it('should call and render pdf links', function (done) {
    expect(this.getPDFDocumentLinksSpy.called).to.be.true;
    expect(this.renderPDFLinksSpy.called).to.be.true;
    expect(render.fn.called).to.be.true;
    done();
  });

});
