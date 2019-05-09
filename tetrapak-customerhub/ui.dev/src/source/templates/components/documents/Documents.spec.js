import $ from'jquery';
import Documents from './Documents';
import documentsTemplate from '../../../test-templates-hbs/documents.hbs';
import { maintenanceFilteringData as documentsFilteringData } from '../maintenance/data/maintenanceFiltering.json';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';

describe('Documents', function () {
  const jqRef ={
    setRequestHeader() {
      // Dummy Method
    }
  };
  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, 'success', jqRef);
    return pr.promise();
  }
  before(function () {
    this.documents = new Documents({
      el: document.body
    });
    $(document.body).empty().html(documentsTemplate());
    this.initSpy = sinon.spy(this.documents, "init");
    this.renderSiteFiltersSpy = sinon.spy(this.documents, "renderSiteFilters");
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(documentsFilteringData));
    this.openStub = sinon.stub(window, 'open');
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: "fLW1l1EA38xjklTrTa5MAN7GFmo2",
        expires_in: "43199",
        token_type: "BearerToken"
      }
    });
    this.documents.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.renderSiteFiltersSpy.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.openStub.restore();
    this.tokenStub.restore();
  });
  it('should initialize', function (done) {
    expect(this.documents.init.called).to.be.true;
    done();
  });
  it('should render maintenance filters', function (done) {
    expect(this.documents.renderSiteFilters.called).to.be.true;
    done();
  });
});
