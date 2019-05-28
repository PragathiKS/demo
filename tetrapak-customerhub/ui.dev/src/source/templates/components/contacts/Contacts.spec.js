import $ from 'jquery';
import Contacts from './Contacts';
import contactsTemplate from '../../../test-templates-hbs/contacts.hbs';
import contactsFilteringData from './data/contacts.json';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';

describe('Contacts', function () {
  const jqRef = {
    setRequestHeader() {
      // Dummy Method
    }
  };
  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, 'success', jqRef);
    return pr.promise();
  };
  before(function () {
    this.contacts = new Contacts({
      el: document.body
    });

    $(document.body).empty().html(contactsTemplate());

    this.initSpy = sinon.spy(this.contacts, "init");
    this.renderSitesSpy = sinon.spy(this.contacts, "renderSites");
    this.processSitesSpy = sinon.spy(this.contacts, "processSites");
    this.renderFilteredContactsSpy = sinon.spy(this.contacts, "renderFilteredContacts");
    this.renderSpy = sinon.spy(render, 'fn');

    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(contactsFilteringData));
    this.openStub = sinon.stub(window, 'open');
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: "fLW1l1EA38xjklTrTa5MAN7GFmo2",
        expires_in: "43199",
        token_type: "BearerToken"
      }
    });
    this.contacts.init();
  });
  after(function () {
    $(document.body).empty();

    this.initSpy.restore();

    this.renderSitesSpy.restore();
    this.processSitesSpy.restore();
    this.renderFilteredContactsSpy.restore();

    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.openStub.restore();
    this.tokenStub.restore();
  });
  it('should initialize', function (done) {
    expect(this.contacts.init.called).to.be.true;
    done();
  });
  it('should render sites', function (done) {
    expect(this.contacts.renderSites.called).to.be.true;
    done();
  });
  it('should process sites data before rendering contacts', function (done) {
    expect(this.contacts.processSites.called).to.be.true;
    done();
  });
  it('should render contacts', function (done) {
    expect(this.contacts.renderFilteredContacts.called).to.be.true;
    done();
  });
  it('should render contacts based on site selection', function (done) {
    $('.js-contacts-filtering__site').trigger('change');
    expect(this.contacts.renderFilteredContacts.called).to.be.true;
    done();
  });
});
