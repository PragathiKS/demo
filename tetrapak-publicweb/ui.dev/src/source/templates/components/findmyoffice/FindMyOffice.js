import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
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
    this.cache.linkSectionElement = this.root.find(
      '.js-pw-find-my-office__wrapper'
    );
    this.setCityInitialState();
  }

  bindEvents() {
    loadGoogleMapsApi({
      key: 'AIzaSyC1w2gKCuwiRCsgqBR9RnSbWNuFvI5lryQ',
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
    this.renderCountries();
    this.cache.selectedCountry = this.root.find('.js-dropdown-item-country');

    this.cache.selectedCountry.on('click', this.onClickCountryItem);
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
    }
    this.cache.marker && this.cache.marker.setMap(null);
    this.renderMarkerPosition(
      this.cache.normalizedData[this.cache.selectedCountryValue]
    );
  };

  renderMarkerPosition = office => {
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
    this.cache.map.setZoom(10);
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
    $('.js-pw-form__dropdown__city').html('Select City');
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
    this.renderMarkerPosition(this.cache.selectedOffice);
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
    this.cache.normalizedData = {
      Australia: {
        latitude: -25.274398,
        longitude: 133.775136,
        offices: [
          {
            name: 'Tetra Pak Marketing Pty Ltd',
            address:
              'Level 2, 5 Burwood Road,\r\nHawthorn VIC 3122 Australia\r\n',
            phoneNumber: '+61 3 8561 3800',
            fax: '+61 3 9818 4250',
            localSiteUrl: 'http://tetrapak.com/au',
            latitude: -37.8213467,
            longitude: 145.0219618
          }
        ]
      },
      Sweden: {
        latitude: 60.128161,
        longitude: 18.643501,
        offices: [
          {
            name: 'Tetra Pak Sverige AB',
            address: 'Ruben Rausings gata SE-221 86 Lund SWEDEN',
            phoneNumber: '+46 46 36 10 00',
            localSiteUrl: 'http://tetrapak.com/se',
            latitude: 55.68719225009939,
            longitude: 13.190959829407474
          },
          {
            name: 'AB Tetra Pak',
            address: 'Ruben Rausings gata SE-221 86 Lund SWEDEN',
            phoneNumber: '+46 46 36 10 00',
            localSiteUrl: 'http://tetrapak.com/se',
            latitude: 58.68719225009939,
            longitude: 145.190959829407474
          }
        ]
      }
    };
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
        zoom: 2
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
