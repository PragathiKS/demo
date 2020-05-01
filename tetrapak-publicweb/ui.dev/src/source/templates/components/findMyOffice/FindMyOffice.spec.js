import FindMyOffice from './FindMyOffice';
import $ from 'jquery';
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
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.returns(ajaxResponse(tpOffices));
    this.onClickCountryItemSpy = sinon.spy(
      this.findMyOffice,
      'onClickCountryItem'
    );
    this.onClickCityItemSpy = sinon.spy(this.findMyOffice, 'onClickCityItem');
    this.countryDropDownToggleSpy = sinon.spy(
      this.findMyOffice,
      'countryDropDownToggle'
    );
    this.cityDropDownToggleSpy = sinon.spy(
      this.findMyOffice,
      'cityDropDownToggle'
    );
    this.renderMarkerPositionSpy = sinon.spy(
      this.findMyOffice,
      'renderMarkerPosition'
    );

    this.findMyOffice.init();
    const googleMap = {
      event: {
        addDomListener: () => null
      },
      LatLng: () => null,
      Marker: () => ({ setMap: () => null }),
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
    this.renderCountriesSpy.restore();
    this.ajaxStub.restore();
    this.onClickCountryItemSpy.restore();
    this.onClickCityItemSpy.restore();
    this.countryDropDownToggleSpy.restore();
    this.cityDropDownToggleSpy.restore();
    this.renderMarkerPositionSpy.restore();
  });

  it('should initialize', function() {
    expect(this.findMyOffice.init.called).to.be.true;
  });
  it('should call handleGoogleMapApi', function(done) {
    expect(this.findMyOffice.handleGoogleMapApi.called).to.be.true;
    done();
  });
  it('should call getOfficeList', function(done) {
    expect(this.findMyOffice.getOfficesList.called).to.be.true;
    done();
  });
  it('should render countries', function(done) {
    expect(this.findMyOffice.renderCountries.called).to.be.true;
    done();
  });
  it('should call onClickCountryItem on click', function() {
    $('.js-dropdown-item-country').trigger('click');
    expect(this.findMyOffice.onClickCountryItem.called).to.be.true;
  });
  it('should call onClickCityItem on click', function() {
    $('.js-dropdown-item-city').trigger('click');
    expect(this.findMyOffice.onClickCityItem.called).to.be.true;
  });
  it('should call countryDropDownToggle on click', function() {
    $('.js-pw-form__dropdown__country').trigger('click');
    expect(this.findMyOffice.countryDropDownToggle.called).to.be.true;
  });
  it('should call cityDropDownToggle on click', function() {
    $('.js-pw-form__dropdown__city').trigger('click');
    expect(this.findMyOffice.cityDropDownToggle.called).to.be.true;
  });
  it('should render renderMarkerPosition', function(done) {
    expect(this.findMyOffice.renderMarkerPosition.called).to.be.true;
    done();
  });
});