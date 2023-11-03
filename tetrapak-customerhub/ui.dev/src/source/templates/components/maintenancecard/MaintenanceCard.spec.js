import $ from 'jquery';
import MaintenanceCard from './MaintenanceCard';
import maintenanceCardTemplate from '../../../test-templates-hbs/maintenanceCard.hbs';
import maintenanceCardData from './data/maintenanceCard.json';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';

describe('MaintenanceCard', function () {
  const jqRef = {
    setRequestHeader() {
      // Dummy method
    }
  };
  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, 'success', jqRef);
    return pr.promise();
  };
  before(function () {
    this.maintenanceCard = new MaintenanceCard({
      el: document.body
    });
    $(document.body).empty().html(maintenanceCardTemplate());
    this.initSpy = sinon.spy(this.maintenanceCard, "init");
    this.renderMaintenanceEventsSpy = sinon.spy(this.maintenanceCard, "renderMaintenanceEvents");
    this.processEventsDataSpy = sinon.spy(this.maintenanceCard, "processEventsData");
    this.trackAnalyticsSpy = sinon.spy(this.maintenanceCard, 'trackAnalytics');
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(maintenanceCardData));
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: "fLW1l1EA38xjklTrTa5MAN7GFmo2",
        expires_in: "43199",
        token_type: "BearerToken"
      }
    });
    this.maintenanceCard.init();
  });
  after(function () {
    this.initSpy.restore();
    this.renderMaintenanceEventsSpy.restore();
    this.processEventsDataSpy.restore();
    this.trackAnalyticsSpy.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.tokenStub.restore();
  });
  it('should initialize', function () {
    expect(this.maintenanceCard.init.called).to.be.true;
  });
  it('should process maintenance events data before rendering', function () {
    expect(this.maintenanceCard.processEventsData.called).to.be.true;
  });
  it('should render maintenance events', function () {
    expect(this.maintenanceCard.renderMaintenanceEvents.called).to.be.true;
  });
  it('should call track analytics on click of maintenace event', function () {
    $('.js-maintenance-card__event').trigger('click');
    expect(this.trackAnalyticsSpy.called).to.be.true;
  });
  it('should call track analytics on click of "contact email" link', function () {
    $('.js-maintenance-card__contact-mail').trigger('click');
    expect(this.trackAnalyticsSpy.called).to.be.true;
  });
  it('should call track analytics on click of "contact phone" link', function () {
    $('.js-maintenance-card__contact-phone').trigger('click');
    expect(this.trackAnalyticsSpy.called).to.be.true;
  });
});