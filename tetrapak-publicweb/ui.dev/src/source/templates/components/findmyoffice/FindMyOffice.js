import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import loadGoogleMapsApi from 'load-google-maps-api';

class FindMyOffice {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  initCache() {
    this.cache.map = {};
    this.cache.googleMaps = '';
    this.cache.officesList = [];
    this.cache.normalizedData = {};
    this.cache.selectedCountryValue = '';
    this.cache.selectedCityValue = '';
    this.cache.selectedOffice = {};
    this.cache.marker = null;
    this.cache.defaultLatitude = 55.6998089;
    this.cache.defaultLongitude = 13.1676404;
    this.cache.countryToggle = this.root.find('.js-pw-form__dropdown__country');
    this.cache.cityToggle = this.root.find('.js-pw-form__dropdown__city');
    this.cache.cityFieldTextValue = $(this.cache.cityToggle).text();
    this.cache.googleApi = this.root.find('.js-google-api');
    this.cache.linkSectionElement = this.root.find(
      '.js-pw-find-my-office__wrapper'
    );
    this.setCityInitialState();
  }

  bindEvents() {
    const googleApiKey = this.cache.googleApi.data('google-api-key');
    loadGoogleMapsApi({
      key: googleApiKey,
      libraries: ['places', 'geometry']
    })
      .then(this.handleGoogleMapApi)
      .catch(function(error) {
        // eslint-disable-next-line no-console
        console.error(error);
      });

    this.cache.countryToggle.on('click', this.countryDropDownToggle);
    this.cache.cityToggle.on('click', this.cityDropDownToggle);
  }

  handleGoogleMapApi = googleMaps => {
    this.cache.googleMaps = googleMaps;
    this.cache.googleMaps.event.addDomListener(window, 'load', this.initMap());
    this.getOfficesList();
  };

  onClickCountryItem = e => {
    this.cache.selectedCountryValue = e.target.innerText;
    $('.js-pw-find-my-office__form-group__country-field').css(
      'display',
      'block'
    );
    $('.js-pw-form__dropdown__country').html(e.target.innerText);
    this.renderCities();
    this.clearSelectedCities();
    this.cache.selectedCity = this.root.find('.js-dropdown-item-city');
    this.cache.selectedCity.on('click', this.onClickCityItem);
    this.resetOfficeDetails();
    this.cache.selectedOffice = {};
    let mapZoomLevel = 5;
    if (
      Object.keys(this.cache.normalizedData).length !== 0 &&
      this.cache.normalizedData[this.cache.selectedCountryValue].offices
        .length === 1
    ) {
      this.cache.selectedOffice = this.cache.normalizedData[
        this.cache.selectedCountryValue
      ].offices[0];
      $('.js-pw-find-my-office__form-group__address').css('display', 'block');
      $('.js-pw-find-my-office__form-group__country-field').css(
        'display',
        'none'
      );
      this.renderOfficeDetailsPanel(this.cache.selectedOffice);
      mapZoomLevel = 10;
    }
    this.cache.marker && this.cache.marker.setMap(null);
    this.renderMarkerPosition(
      this.cache.normalizedData[this.cache.selectedCountryValue],
      { mapZoomLevel }
    );
  };

  renderMarkerPosition = (office, mapZoomLevel) => {
    var latLng = new this.cache.googleMaps.LatLng(
      office.latitude,
      office.longitude
    );
    this.cache.marker = new this.cache.googleMaps.Marker({
      position: latLng,
      title: office.name,
      icon: '/content/dam/publicweb/Pin.png'
    });

    // To add the marker to the map, call setMap();
    this.cache.marker.setMap(this.cache.map);
    this.cache.map.setZoom((mapZoomLevel && mapZoomLevel.mapZoomLevel) || 5);
    this.cache.map.panTo(this.cache.marker.position);
  };

  resetOfficeDetails = () => {
    this.cache.selectedOffice = {};
    this.renderOfficeDetailsPanel(this.cache.selectedOffice);
    $('.js-pw-find-my-office__form-group__address').css('display', 'none');
  };

  onClickCityItem = e => {
    this.cache.selectedCityValue = e.target.innerText;
    $('.js-pw-find-my-office__form-group__address').css('display', 'block');
    $('.js-pw-form__dropdown__city').html(e.target.innerText);
    this.renderOfficeDetails(this.cache.selectedCityValue);
  };

  clearSelectedCities = () => {
    $('.js-pw-form__dropdown__city').text(this.cache.cityFieldTextValue);
  };

  renderOfficeDetails = officeName => {
    const selectedOfficeDetails =
      Object.keys(this.cache.normalizedData).length !== 0 &&
      this.cache.normalizedData[this.cache.selectedCountryValue].offices.filter(
        office => office.name === officeName
      );
    this.cache.selectedOffice =
      selectedOfficeDetails.length > 0 && selectedOfficeDetails[0];
    this.renderOfficeDetailsPanel(this.cache.selectedOffice);
    this.cache.marker && this.cache.marker.setMap(null);
    this.renderMarkerPosition(this.cache.selectedOffice, { mapZoomLevel: 10 });
  };

  renderOfficeDetailsPanel = office => {
    this.cache.linkSectionElement.attr('data-link-name', office.name);
    render.fn({
      template: 'officeDetails',
      data: office,
      target: '.js-pw-find-my-office-office-details',
      hidden: false
    });
  };

  renderCountryOfficesList = countries => {
    render.fn({
      template: 'countryOfficesList',
      data: countries,
      target: '.js-pw-form__dropdown__country-select'
    });
  };

  renderCitiesOfficesList = cities => {
    render.fn({
      template: 'citiesOfficesList',
      data: cities,
      target: '.js-pw-form__dropdown__city-select'
    });
  };

  getOfficesList = () => {
    const servletPath = this.cache.googleApi.data('google-api-servlet');
    ajaxWrapper
      .getXhrObj({
        url: servletPath,
        method: ajaxMethods.GET,
        cache: true,
        dataType: 'json',
        contentType: 'application/json'
      })
      .done(data => {
        if (data) {
          this.cache.normalizedData = data;
          this.renderCountries();
          this.cache.selectedCountry = this.root.find(
            '.js-dropdown-item-country'
          );

          this.cache.selectedCountry.on('click', this.onClickCountryItem);
        }
      });
  };

  setCityInitialState = () => {
    $('.js-pw-form__dropdown__city').attr('disabled', true);
    $('.js-pw-find-my-office__form-group__label').addClass('opacity');
    $(window).click(function() {
      $('.dropdown-menu,.dropdown-toggle').removeClass('show');
    });
  };

  renderCountries = () => {
    const countries = Object.keys(this.cache.normalizedData);
    this.renderCountryOfficesList(countries);
  };

  renderCities = () => {
    const selectedCountry = $('.js-pw-form__dropdown__country').text();
    const cities = [];
    this.cache.normalizedData[selectedCountry] &&
      this.cache.normalizedData[selectedCountry].offices.map(city => {
        cities.push(city);
      });
    if (cities.length > 0) {
      $('.js-pw-form__dropdown__city').attr('disabled', false);
      $('.js-pw-find-my-office__form-group__label').removeClass('opacity');
      this.renderCitiesOfficesList(cities);
    }
  };

  initMap = () => {
    this.cache.map = new this.cache.googleMaps.Map(
      document.querySelector('.js-pw-find-my-office__map'),
      {
        //eslint-disable-line
        center: {
          lat: this.cache.defaultLatitude,
          lng: this.cache.defaultLongitude
        },
        disableDefaultUI: true,
        zoom: 3
      }
    );
    var gotoMapButton = document.createElement('div');
    gotoMapButton.setAttribute(
      'style',
      'margin: 5px; border: 1px solid; padding: 1px 12px; font: bold 11px Roboto, Arial, sans-serif; color: #000000; background-color: #FFFFFF; cursor: pointer;'
    );
    gotoMapButton.innerHTML = 'View larger map';
    this.cache.map.controls[
      this.cache.googleMaps.ControlPosition.TOP_RIGHT
    ].push(gotoMapButton);
    this.cache.googleMaps.event.addDomListener(
      gotoMapButton,
      'click',
      this.viewLargeMap
    );
  };

  viewLargeMap = () => {
    let uri = '';
    if (this.cache.marker && this.cache.marker.getPosition()) {
      uri = this.cache.marker && this.cache.marker.getPosition();
    } else {
      uri = new this.cache.googleMaps.LatLng({
        lat: this.cache.defaultLatitude,
        lng: this.cache.defaultLongitude
      });
    }
    var url = `https://www.google.com/maps?q=${encodeURIComponent(
      uri.toUrlValue()
    )}`;
    // you can also hard code the URL
    window.open(url);
  };

  countryDropDownToggle = e => {
    e.stopPropagation();
    if ($('.js-pw-form__dropdown__city-select').hasClass('show')) {
      $(
        '.js-pw-form__dropdown__city-select,.js-pw-form__dropdown__city'
      ).removeClass('show');
    }
    $(
      '.js-pw-form__dropdown__country-select,.js-pw-form__dropdown__country'
    ).toggleClass('show');
  };

  cityDropDownToggle = e => {
    e.stopPropagation();
    if (e.target.disabled) {
      return;
    }
    if ($('.js-pw-form__dropdown__country-select').hasClass('show')) {
      $(
        '.js-pw-form__dropdown__country-select,.js-pw-form__dropdown__country'
      ).removeClass('show');
    }
    $(
      '.js-pw-form__dropdown__city-select,.js-pw-form__dropdown__city'
    ).toggleClass('show');
  };

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default FindMyOffice;
