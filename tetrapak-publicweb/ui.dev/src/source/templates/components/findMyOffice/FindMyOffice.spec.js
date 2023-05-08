import FindMyOffice from './FindMyOffice';
import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import tpOffices from './data/tp-offices.json';
import findMyOfficeTemplate from '../../../test-templates-hbs/findMyOffice.hbs';

describe('FindMyOffice', function() {
  const jqRef = {
    setRequestHeader() {
      // Dummy Method
    }
  };
  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, 'success', jqRef);
    return pr.promise();
  }

  before(function() {
    $(document.body)
      .empty()
      .html(findMyOfficeTemplate());
    this.findMyOffice = new FindMyOffice({
      el: document.body
    });
    this.initSpy = sinon.spy(this.findMyOffice, 'init');
    this.renderSpy = sinon.spy(render, 'fn');
    this.handleGoogleMapApiSpy = sinon.spy(
      this.findMyOffice,
      'handleGoogleMapApi'
    );
    this.renderCountriesSpy = sinon.spy(this.findMyOffice, 'renderCountries');
    this.getOfficesListSpy = sinon.spy(this.findMyOffice, 'getOfficesList');
    this.goToLocalSiteSpy = sinon.spy(this.findMyOffice, 'goToLocalSite');
    this.onKeydownSpy = sinon.spy(this.findMyOffice, 'onKeydown');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.returns(ajaxResponse(tpOffices));
    this.openStub = sinon.stub(window, 'open');
    this.onClickCountryItemSpy = sinon.spy(
      this.findMyOffice,
      'onClickCountryItem'
    );
    this.onClickCityItemSpy = sinon.spy(this.findMyOffice, 'onClickCityItem');
    this.renderMarkerPositionSpy = sinon.spy(
      this.findMyOffice,
      'renderMarkerPosition'
    );
    this.initBaiduMapSpy = sinon.spy(this.findMyOffice, 'initBaiduMap');

    this.findMyOffice.init();
    const googleMap = {
      event: {
        addDomListener: () => null
      },
      LatLng: () => null,
      Marker: () => ({ setMap: () => null, setVisible: () => null }),
      Map: () => ({ controls: [[]], setZoom: () => null, panTo: () => null }),
      ControlPosition: {
        TOP_RIGHT: 0
      }
    };

    this.findMyOffice.handleGoogleMapApi(googleMap);
  });
  after(function() {
    $(document.body).empty();
    this.initSpy.restore();
    this.handleGoogleMapApiSpy.restore();
    this.renderSpy.restore();
    this.getOfficesListSpy.restore();
    this.goToLocalSiteSpy.restore();
    this.renderCountriesSpy.restore();
    this.ajaxStub.restore();
    this.onClickCountryItemSpy.restore();
    this.onClickCityItemSpy.restore();
    this.renderMarkerPositionSpy.restore();
    this.openStub.restore();
    this.onKeydownSpy.restore();
    this.initBaiduMapSpy.restore();
  });

  it('should initialize', function(done) {
    expect(this.findMyOffice.init.called).to.be.true;
    done();
  });
  it('should call handleGoogleMapApi', function(done) {
    expect(this.findMyOffice.handleGoogleMapApi.called).to.be.true;
    done();
  });
  it('should call init baidu map', function(done) {
    expect(this.findMyOffice.init.called).to.be.true;
    done();
  });
  it('should call getOfficeList', function(done) {
    expect(this.findMyOffice.getOfficesList.called).to.be.true;
    $('.js-pw-form__dropdown__country,.js-pw-form__dropdown__country-select').keydown();
    expect(this.findMyOffice.onKeydown.called).to.be.true;
    done();
  });
  it('should call goToLocalSite', function(done) {
    $('.js-localSiteUrl').trigger('click');
    expect(this.findMyOffice.goToLocalSite.called).to.be.true;
    done();
  });
  it('should render countries', function(done) {
    expect(this.findMyOffice.renderCountries.called).to.be.true;
    done();
  });
  it('should call onClickCountryItem on click', function(done) {
    $('.js-dropdown-item-country').trigger('click');
    expect(this.findMyOffice.onClickCountryItem.called).to.be.true;
    done();
  });
  it('should call onClickCityItem on click', function(done) {
    $('.js-dropdown-item-city').trigger('click');
    expect(this.findMyOffice.onClickCityItem.called).to.be.true;
    done();
  });
  it('should render renderMarkerPosition', function(done) {
    expect(this.findMyOffice.renderMarkerPosition.called).to.be.true;
    done();
  });
});