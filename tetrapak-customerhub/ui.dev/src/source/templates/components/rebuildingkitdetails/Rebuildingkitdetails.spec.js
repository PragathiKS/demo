import $ from 'jquery';
import Rebuildingkitdetails from './Rebuildingkitdetails';
import rebuildingkitDetailsData from './data/rebuildingkitdetails.json';
import rebuildingkitCtiData from './data/rebuildingkitcti.json';
import rebuildingkitDetailsTemplate from '../../../test-templates-hbs/rebuildingkitDetails.hbs';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';

describe('Rebuildingkitdetails', function () {
  const jqRef = {
    setRequestHeader() {
      // Dummy method
    }
  };
  function setDom($this) {
    $(document.body).empty().html(rebuildingkitDetailsTemplate());
    $this.rebuildingkitDetails = new Rebuildingkitdetails({
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
    this.initSpy = sinon.spy(this.rebuildingkitDetails, 'init');
    this.bindEventsSpy = sinon.spy(this.rebuildingkitDetails, 'bindEvents');
    this.getRebuildingKitDetailsSpy = sinon.spy(this.rebuildingkitDetails, 'getRebuildingKitDetails');
    this.renderRebuildingKitDetailsSpy = sinon.spy(this.rebuildingkitDetails, 'renderRebuildingKitDetails');
    this.getCtiDocumentsSpy = sinon.spy(this.rebuildingkitDetails, 'getCtiDocuments');
    this.renderCtiDocumentsSpy = sinon.spy(this.rebuildingkitDetails, 'renderCtiDocuments');
    this.renderRebuildingKitDetailsBottomSpy = sinon.spy(this.rebuildingkitDetails, 'renderRebuildingKitDetailsBottom');
    this.updateRkValidationRowsSpy = sinon.spy(this.rebuildingkitDetails, 'updateRkValidationRows');
    this.renderRebuildingKitReportModalSpy = sinon.spy(this.rebuildingkitDetails, 'renderRebuildingKitReportModal');
    this.requestCtiLanguageSpy = sinon.spy(this.rebuildingkitDetails, 'requestCtiLanguage');
    this.submitCTIemailSpy = sinon.spy(this.rebuildingkitDetails, 'submitCTIemail');
    this.changePreferredLanguageSpy = sinon.spy(this.rebuildingkitDetails, 'changePreferredLanguage');
    this.bindFormChangeEventsSpy = sinon.spy(this.rebuildingkitDetails, 'bindFormChangeEvents');
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    const apiResponse = {data :[{...rebuildingkitDetailsData.data[0],...rebuildingkitCtiData.data[0]}]};
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(apiResponse));
    this.openStub = sinon.stub(window, 'open');
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: 'fLW1l1EA38xjklTrTa5MAN7GFmo2',
        expires_in: '43199',
        token_type: 'BearerToken'
      }
    });
    this.rebuildingkitDetails.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.bindEventsSpy.restore();
    this.getRebuildingKitDetailsSpy.restore();
    this.renderRebuildingKitDetailsSpy.restore();
    this.updateRkValidationRowsSpy.restore();
    this.changePreferredLanguageSpy.restore();
    this.bindFormChangeEventsSpy.restore();
    this.getCtiDocumentsSpy.restore();
    this.renderCtiDocumentsSpy.restore();
    this.renderSpy.restore();
    this.renderRebuildingKitReportModalSpy.restore();
    this.requestCtiLanguageSpy.restore();
    this.submitCTIemailSpy.restore();
    this.ajaxStub.restore();
    this.openStub.restore();
    this.tokenStub.restore();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    expect(this.bindEventsSpy.called).to.be.true;
    done();
  });

  it('should call and render rebuildingkitdetails', function (done) {
    expect(this.getRebuildingKitDetailsSpy.called).to.be.true;
    this.ajaxStub.restore();
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse({ status: 200 }));
    expect(this.renderRebuildingKitDetailsSpy.called).to.be.true;
    expect(render.fn.called).to.be.true;
    expect(this.getCtiDocumentsSpy.called).to.be.true;
    expect(render.fn.called).to.be.true;
    expect(render.fn.called).to.be.true;
    expect(this.renderRebuildingKitDetailsBottomSpy.called).to.be.true;
    expect(render.fn.called).to.be.true;
    expect(this.updateRkValidationRowsSpy.called).to.be.true;
    done();
  });

  it('should call and get CTI data', function (done) {
    expect(this.getRebuildingKitDetailsSpy.called).to.be.true;
    expect(this.getCtiDocumentsSpy.called).to.be.true;
    const rkRelease = 'TT3_2020_01_01';
    if(rkRelease !== '') {
      this.ajaxStub.restore();
      this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
      this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse({ status: 200 }));
      expect(this.renderCtiDocumentsSpy.called).to.be.true;
      expect(render.fn.called).to.be.true;
    } else {
      expect(this.renderCtiDocumentsSpy.called).to.be.true;
      expect(render.fn.called).to.be.true;
    }
    expect(render.fn.called).to.be.true;
    done();
  });

  it('should call and render CTI documents', function (done) {
    expect(this.getCtiDocumentsSpy.called).to.be.true;
    expect(render.fn.called).to.be.true;
    expect(this.renderCtiDocumentsSpy.called).to.be.true;
    const langAvailable = false;
    if(!langAvailable) {
      expect(render.fn.called).to.be.true;
    } else {
      expect(render.fn.called).to.be.true;
    }
    done();
  });

  it('should call and render rebuildingkits bottom data', function (done) {
    expect(this.getRebuildingKitDetailsSpy.called).to.be.true;
    expect(this.renderRebuildingKitDetailsBottomSpy.called).to.be.true;
    expect(render.fn.called).to.be.true;
    done();
  });
  
  it('should change preffered language', function (done) {
    $('.js-apply-language').trigger('click',function(e){
      e.preventDefault();
    });
    expect(this.changePreferredLanguageSpy.called).to.be.true;
    this.ajaxStub.restore();
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse({ status: 200 }));
    expect($('.js-rk-cti-modal').hasClass('show')).to.be.false;
    done();
  });

  it('should open preffered language modal', function (done) {
    $('.js-rk-preferred-language').trigger('click');
    expect($('.js-language-modal').hasClass('show')).to.be.true;
    done();
  });

  it('should close preffered language modal on close icon click', function (done) {
    setDom(this);
    $('.js-close-btn').trigger('click',function(e){
      e.preventDefault();
    });
    expect($('.js-language-modal').hasClass('show')).to.be.false;
    done();
  });

  it('should open more languages modal', function (done) {
    $('.js-langcode').trigger('click',function(e){
      e.preventDefault();
    });
    expect($('.js-rk-cti-modal').hasClass('show')).to.be.false;
    done();
  });

  it('should close more languages modal on close icon click', function (done) {
    setDom(this);
    $('.js-close-btn').trigger('click',function(e){
      e.preventDefault();
    });
    expect($('.js-rk-cti-modal').hasClass('show')).to.be.false;
    done();
  }); 

  it('should request CTI translation', function (done) {
    const $this = this;
    $('.js-request-translation').trigger('click', function(e) {
      e.preventDefault();
      expect($this.requestCtiLanguageSpy.called).to.be.true;
      expect($this.submitCTIemailSpy.called).to.be.true;
      this.ajaxStub.restore();
      this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
      this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse({ status: 200 }));
    });
    done();
  });

  it('should render RK report modal', function (done) {
    const $this = this;
    $('.js-rebuilding-details__update').trigger('click', function(e) {
      e.preventDefault();
      expect($this.renderRebuildingKitReportModalSpy.called).to.be.true;
      this.ajaxStub.restore();
      this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
      this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse({ status: 200 }));
      expect(render.fn.called).to.be.true;
    });
    done();
  });
});