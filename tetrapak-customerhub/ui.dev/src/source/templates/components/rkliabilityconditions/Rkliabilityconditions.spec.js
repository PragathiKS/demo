import $ from 'jquery';
import Rkliabilityconditions from './Rkliabilityconditions';
import rkLiabilityConditonsData from './data/rkLiabilityConditions.json';
import rkLiabilityConditonsTemplate from '../../../test-templates-hbs/rebuildingkitDetails.hbs';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';

describe('Rkliabilityconditions', function () {
  const jqRef = {
    setRequestHeader() {
      // Dummy method
    }
  };
  function setDom($this) {
    $(document.body).empty().html(rkLiabilityConditonsTemplate());
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
    this.getPDFButtonsSpy = sinon.spy(this.rkliabilityConditions, 'getPDFButtons');
    this.renderButtonsSpy = sinon.spy(this.rkliabilityConditions, 'renderButtons');
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    const apiResponse = {data :[{...rkLiabilityConditonsData }]};
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(apiResponse));
    this.openStub = sinon.stub(window, 'open');
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: 'eyJleHAiOjE2NzkzODkyMzAsImlhdCI6MTY3OTM4ODYzMH0.fD4ORA2N9_lh9tRWiRDBB69X05YbPUCpQalTuPAdGOc',
        expires_in: '43199',
        token_type: 'BearerToken'
      }
    });
    this.rkliabilityConditions.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.bindEventsSpy.restore();
    this.getPDFButtonsSpy.restore();
    this.renderButtonsSpy.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.openStub.restore();
    this.tokenStub.restore();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    expect(this.bindEventsSpy.called).to.be.true;
    done();
  });

  it('should call and render buttons', function (done) {
    expect(this.getPDFButtonsSpy.called).to.be.true;
    expect(this.renderButtonsSpy.called).to.be.true;
    expect(render.fn.called).to.be.true;
    done();
  });

});
