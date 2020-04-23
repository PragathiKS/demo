import FindMyOffice from './FindMyOffice';
import $ from 'jquery';
import loadGoogleMapsApi from 'load-google-maps-api';
import { render } from '../../../scripts/utils/render';
import findMyOfficeTemplate from '../../../test-templates-hbs/findMyOffice.hbs';

describe('FindMyOffice', function() {
  before(function() {
    $(document.body)
      .empty()
      .html(findMyOfficeTemplate());
    this.findMyOffice = new FindMyOffice({
      el: document.body
    });
    this.initSpy = sinon.spy(this.findMyOffice, 'init');
    this.renderSpy = sinon.spy(render, 'fn');
    this.initMapSpy = sinon.spy(this.findMyOffice, 'initMap');
    this.renderCountriesSpy = sinon.spy(this.findMyOffice, 'renderCountries');
    this.getOfficesListSpy = sinon.spy(this.findMyOffice, 'getOfficesList');
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
  });
  after(function() {
    $(document.body).empty();
    this.initSpy.restore();
    this.initMapSpy.restore();
    this.renderSpy.restore();
    this.getOfficesListSpy.restore();
    this.renderCountriesSpy.restore();
    this.onClickCountryItemSpy.restore();
    this.onClickCityItemSpy.restore();
    this.countryDropDownToggleSpy.restore();
    this.cityDropDownToggleSpy.restore();
    this.renderMarkerPositionSpy.restore();
  });

  it('should initialize', function() {
    expect(this.findMyOffice.init.called).to.be.true;
  });

  it('should initialize google map', function(done) {
    this.timeout(30000);
    loadGoogleMapsApi({
      key: 'AIzaSyC1w2gKCuwiRCsgqBR9RnSbWNuFvI5lryQ',
      libraries: ['places', 'geometry']
    })
      .then(res => {
        expect(res).to.not.be.null;
        done();
      })
      .catch(done);
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
