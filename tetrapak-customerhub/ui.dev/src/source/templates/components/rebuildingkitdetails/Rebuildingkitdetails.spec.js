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
    this.changePreferredLanguageSpy = sinon.spy(this.rebuildingkitDetails, 'changePreferredLanguage');
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
    this.changePreferredLanguageSpy.restore();
    this.getCtiDocumentsSpy.restore();
    this.renderCtiDocumentsSpy.restore();
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

  it('should call and render rebuilding data', function (done) {
    expect(this.getRebuildingKitDetailsSpy.called).to.be.true;
    expect(this.renderRebuildingKitDetailsSpy.called).to.be.true;
    expect(render.fn.called).to.be.true;
    expect(this.getCtiDocumentsSpy.called).to.be.true;
    expect(render.fn.called).to.be.true;
    expect(render.fn.called).to.be.true;
    expect(this.renderRebuildingKitDetailsBottomSpy.called).to.be.true;
    expect(render.fn.called).to.be.true;
    done();
  });
  
  it('should change preffered language', function (done) {
    $('.js-apply-language').trigger('click',function(e){
      e.preventDefault();
    });
    expect(this.changePreferredLanguageSpy.called).to.be.true;
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
});