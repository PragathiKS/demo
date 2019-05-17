import EventsListing from './EventsListing';
import $ from 'jquery';
import eventsListingTemplate from '../../../test-templates-hbs/eventsListing.hbs';
import maintenanceEventsData from './data/maintenanceEvents.json';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';

describe('Maintenanceevents', function () {
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
    $(document.body).empty().html(eventsListingTemplate());
    this.maintenanceevents = new EventsListing({ el: $('.js-maintenance__events') });
    this.initSpy = sinon.spy(this.maintenanceevents, "init");
    this.renderMaintenanceEventsSpy = sinon.spy(this.maintenanceevents, "renderMaintenanceEvents");
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(maintenanceEventsData));
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: "fLW1l1EA38xjklTrTa5MAN7GFmo2",
        expires_in: "43199",
        token_type: "BearerToken"
      }
    });
    this.maintenanceevents.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.renderMaintenanceEventsSpy.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
    this.tokenStub.restore();
  });
  it('should initialize', function (done) {
    expect(this.maintenanceevents.init.called).to.be.true;
    done();
  });
  it('should render events on trigger of renderMaintenanceEvents event', function (done) {
    $('.js-maintenance').trigger('renderMaintenance', [{
      $site: $('.js-maintenance-filtering__site'),
      $line: $('.js-maintenance-filtering__line'),
      $equipment: $('.js-maintenance-filtering__equipment')
    }]);
    expect(this.renderMaintenanceEventsSpy.called).to.be.true;
    done();
  });
});
