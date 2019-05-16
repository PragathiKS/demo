import $ from 'jquery';
import MaintenanceFiltering from './MaintenanceFiltering';
import maintenanceFilteringTemplate from '../../../test-templates-hbs/maintenanceFiltering.hbs';
import maintenanceFilteringData from './data/maintenanceFiltering.json';
import maintenanceEventsData from './data/maintenanceEvents.json';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';

describe('MaintenanceFiltering', function () {
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
    this.maintenanceFiltering = new MaintenanceFiltering({
      el: document.body
    });
    $(document.body).empty().html(maintenanceFilteringTemplate());
    this.initSpy = sinon.spy(this.maintenanceFiltering, "init");
    this.renderMaintenanceFiltersSpy = sinon.spy(this.maintenanceFiltering, "renderMaintenanceFilters");
    this.processSiteDataSpy = sinon.spy(this.maintenanceFiltering, "processSiteData");
    this.renderMaintenanceContactSpy = sinon.spy(this.maintenanceFiltering, "renderMaintenanceContact");
    this.renderLineFilterSpy = sinon.spy(this.maintenanceFiltering, "renderLineFilter");
    this.renderEquipmentFilterSpy = sinon.spy(this.maintenanceFiltering, "renderEquipmentFilter");
    this.trackAnalyticsSpy = sinon.spy(this.maintenanceFiltering, 'trackAnalytics');
    this.navigateSpy = sinon.spy(this.maintenanceFiltering, 'navigateCalendar');
    this.renderCalendarEventsDotSpy = sinon.spy(this.maintenanceFiltering, 'renderCalendarEventsDot');
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(maintenanceEventsData));
    this.openStub = sinon.stub(window, 'open');
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: "fLW1l1EA38xjklTrTa5MAN7GFmo2",
        expires_in: "43199",
        token_type: "BearerToken"
      }
    });
    this.maintenanceFiltering.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.renderMaintenanceFiltersSpy.restore();
    this.processSiteDataSpy.restore();
    this.renderMaintenanceContactSpy.restore();
    this.renderLineFilterSpy.restore();
    this.renderEquipmentFilterSpy.restore();
    this.renderCalendarEventsDotSpy.restore();
    this.trackAnalyticsSpy.restore();
    this.navigateSpy.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.openStub.restore();
    this.tokenStub.restore();
  });
  it('should initialize', function (done) {
    expect(this.maintenanceFiltering.init.called).to.be.true;
    done();
  });
  it('should render maintenance filters', function (done) {
    this.ajaxStub.restore();
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(maintenanceFilteringData));
    expect(this.maintenanceFiltering.renderMaintenanceFilters.called).to.be.true;
    done();
  });
  it('should process site filter data before rendering maintenance filters', function (done) {
    expect(this.maintenanceFiltering.processSiteData.called).to.be.true;
    done();
  });
  it('should render contacts for site', function (done) {
    expect(this.maintenanceFiltering.renderMaintenanceContact.called).to.be.true;
    done();
  });
  it('should render line filter', function (done) {
    expect(this.maintenanceFiltering.renderLineFilter.called).to.be.true;
    done();
  });
  it('should render Equipment filter', function (done) {
    expect(this.maintenanceFiltering.renderEquipmentFilter.called).to.be.true;
    done();
  });
  it('should render line filter on change of `site` filter', function (done) {
    $('.js-maintenance-filtering__site').trigger('change');
    expect(this.maintenanceFiltering.renderLineFilter.called).to.be.true;
    done();
  });
  it('should render Equipment filter on change of `line` filter', function (done) {
    $('.js-maintenance-filtering__line').trigger('change');
    expect(this.maintenanceFiltering.renderEquipmentFilter.called).to.be.true;
    done();
  });
  it('should call track analytics for maintenance on click of "contact email" link', function () {
    $('.js-maintenance-filtering__contact-mail').trigger('click');
    expect(this.trackAnalyticsSpy.called).to.be.true;
  });
  it('should call track analytics for maintenance on click of "contact phone" link', function () {
    $('.js-maintenance-filtering__contact-phone').trigger('click');
    expect(this.trackAnalyticsSpy.called).to.be.true;
  });
});
