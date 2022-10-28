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
    this.getCtiDocumentsSpy = sinon.spy(this.rebuildingkitDetails, 'getCtiDocuments');
    this.renderCtiDocumentsSpy = sinon.spy(this.rebuildingkitDetails, 'renderCtiDocuments');
   // this.renderCtiDocumentsSpy = sinon.spy(this.rebuildingkitDetails, 'renderCtiDocuments');
   // this.renderRebuildingKitDetailsSpy = sinon.spy(this.rebuildingkitDetails, 'renderRebuildingKitDetails');
   // this.getCtiDocumentsSpy = sinon.spy(this.rebuildingkitDetails, 'getCtiDocuments')
   /* this.renderRebuildingKitDetailsSpy = sinon.spy(this.rebuildingkitDetails, 'renderRebuildingKitDetails');

    this.renderCtiDocumentsSpy = sinon.spy(this.rebuildingkitDetails, 'renderCtiDocuments');
    this.renderRebuildingKitDetailsBottomSpy = sinon.spy(this.rebuildingkitDetails, 'renderRebuildingKitDetailsBottom');
    */
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(rebuildingkitDetailsData));
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
   this.getCtiDocumentsSpy.restore();
   this.renderCtiDocumentsSpy.restore();
   // this.renderCtiDocumentsSpy.restore();
   // this.renderRebuildingKitDetailsSpy.restore();
    /*this.renderCtiDocumentsSpy.restore();
    this.renderRebuildingKitDetailsBottomSpy.restore();
    */
    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.openStub.restore();
    this.tokenStub.restore();
  });

  /*beforeEach(function() {
    //this.renderSpy.resetHistory();
   // this.getRebuildingKitDetailsSpy.resetHistory();
    /*this.getCtiDocumentsSpy.resetHistory();
    this.renderRebuildingKitDetailsSpy.resetHistory();
    this.renderCtiDocumentsSpy.resetHistory();
    this.renderRebuildingKitDetailsBottomSpy.resetHistory();
  });*/

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    expect(this.bindEventsSpy.called).to.be.true;
    done();
  });

 it('should call and render rebuilding data', function (done) {
    expect(this.getRebuildingKitDetails.called).to.be.true;
    expect(render.fn.called).to.be.true;
    done();
  });
  
 it('should call and render cti data', function (done) {
    this.ajaxStub.restore();
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(rebuildingkitCtiData));
    expect(this.getCtiDocuments.called).to.be.true;
    expect(this.renderCtiDocuments.called).to.be.true;
    expect(render.fn.called).to.be.true;
    done();
  });
});

