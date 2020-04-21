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
    this.cache.countryToggle = this.root.find('.js-pw-form__dropdown__country');
    this.cache.cityToggle = this.root.find('.js-pw-form__dropdown__city');
    this.setCityInitialState();
  }

  bindEvents() {
    loadGoogleMapsApi({
      key: 'AIzaSyC1w2gKCuwiRCsgqBR9RnSbWNuFvI5lryQ',
      libraries: ['places', 'geometry']
    })
      .then(googleMaps => {
        this.cache.googleMaps = googleMaps;
        this.cache.googleMaps.event.addDomListener(
          window,
          'load',
          this.initMap()
        );
        this.getOfficesList();
        this.renderCountries();
        this.cache.selectedCountry = this.root.find(
          '.js-dropdown-item-country'
        );

        this.cache.selectedCountry.on('click', this.onClickCountryItem);
      })
      .catch(function(error) {
        // eslint-disable-next-line no-console
        console.error(error);
      });

    this.cache.countryToggle.on('click', this.countryDropDownToggle);
    this.cache.cityToggle.on('click', this.cityDropDownToggle);
  }

  onClickCountryItem = e => {
    $('.js-pw-form__dropdown__country').html(e.target.innerText);
    this.renderCities();
    this.clearSelectedCities();
    this.cache.selectedCity = this.root.find('.js-dropdown-item-city');
    this.cache.selectedCity.on('click', this.onClickCityItem);
  };
  onClickCityItem = e => {
    $('.js-pw-form__dropdown__city').html(e.target.innerText);
  };

  clearSelectedCities = () => {
    $('.js-pw-form__dropdown__city').html('Select City');
  };

  renderOfficeDetailsPanel = office => {
    render.fn(
      {
        template: 'officeDetails',
        data: office,
        target: '.js-pw-find-my-office-office-details',
        hidden: false
      },
      function() {
        $(this).fadeIn();
      }
    );
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
        lat: -25.274398,
        lng: 133.775136,
        offices: [
          {
            Title: 'Office - Melbourne',
            name: 'Tetra Pak Marketing Pty Ltd',
            Address:
              'Level 2, 5 Burwood Road,\r\nHawthorn VIC 3122 Australia\r\n',
            Country: 'Australia',
            'E-mail': 'info.au@tetrapak.com',
            'Google Maps Url':
              'https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d12606.65458755712!2d145.0219618!3d-37.8213467!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0xc7dfbb7fe63da174!2sTetra+Pak!5e0!3m2!1ssv!2snz!4v1499411324448',
            Phone: '+61 3 8561 3800',
            Fax: '+61 3 9818 4250',
            'Local site url': '/au',
            Modified: '5/28/19 7:45',
            'Item Type': 'Item',
            Path: 'Lists/Offices',
            lat: -37.8213467,
            lng: 145.0219618
          }
        ]
      },
      Sweden: {
        lat: '60.128161',
        lng: '18.643501',
        offices: [
          {
            Title: 'Marketing & Sales',
            name: 'Tetra Pak Sverige AB',
            'Local Name': 'Tetra Pak Nordics',
            Address: 'Ruben Rausings gata SE-221 86 Lund SWEDEN',
            Cluster: 'Europe & Central Asia',
            Country: 'Sweden',
            'E-mail': 'info.se@tetrapak.com',
            'Google Maps Url':
              'https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d4498.206546301627!2d13.190959829407474!3d55.68719225009939!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x465397e9489281e1%3A0x75046ec0553d3a1e!2sAB+Tetra+Pak!5e0!3m2!1sen!2s!4v1423494195747',
            Phone: '+46 46 36 10 00',
            'Local site url': '/se',
            Modified: '4/27/18 8:02',
            'Item Type': 'Item',
            Path: 'Lists/Offices',
            lat: 55.68719225009939,
            lng: 13.190959829407474
          },
          {
            Title: 'Head functions',
            name: 'AB Tetra Pak',
            Address: 'Ruben Rausings gata SE-221 86 Lund SWEDEN',
            Cluster: 'Europe & Central Asia',
            Country: 'Sweden',
            'E-mail': 'info.se@tetrapak.com',
            'Google Maps Url':
              'https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d4498.206546301627!2d13.190959829407474!3d55.68719225009939!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x465397e9489281e1%3A0x75046ec0553d3a1e!2sAB+Tetra+Pak!5e0!3m2!1sen!2s!4v1423494195747',
            Phone: '+46 46 36 10 00',
            'Local site url': '/se',
            Modified: '4/27/18 8:02',
            'Item Type': 'Item',
            Path: 'Lists/Offices',
            lat: 55.68719225009939,
            lng: 13.190959829407474
          }
        ]
      }
    };
  };

  setCityInitialState = () => {
    $('.js-pw-form__dropdown__city').attr('disabled', true);
    $('.js-pw-find-my-office__form-group__label').addClass('opacity');
    $(window).click(function() {
      $('.dropdown-menu').removeClass('show');
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
    const currentLat = 55.6998089;
    const currentLng = 13.1676404;
    this.cache.map = new this.cache.googleMaps.Map(
      document.querySelector('.js-pw-find-my-office__map'),
      {
        //eslint-disable-line
        center: { lat: currentLat, lng: currentLng },
        disableDefaultUI: true,
        zoom: 5
      }
    );
  };

  countryDropDownToggle = e => {
    e.stopPropagation();
    if ($('.js-pw-form__dropdown__city-select').hasClass('show')) {
      $('.js-pw-form__dropdown__city-select').removeClass('show');
    }
    $('.js-pw-form__dropdown__country-select').toggleClass('show');
  };

  cityDropDownToggle = e => {
    e.stopPropagation();
    if (e.target.disabled) {
      // or this.disabled
      return;
    }
    if ($('.js-pw-form__dropdown__country-select').hasClass('show')) {
      $('.js-pw-form__dropdown__country-select').removeClass('show');
    }
    $('.js-pw-form__dropdown__city-select').toggleClass('show');
  };

  normalizeData = () => {
    const offices = [
      'https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d3005.8900171346613!2d29.02389!3d41.115093!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x14cab595eb4c4ee1%3A0x8efd6045e1e8eb3d!2sNurol+Plaza!5e0!3m2!1sen!2s!4v1423470732620'
    ];
    const latRegex = /!3d(.*?)!/;
    const lngRegex = /!2d(.*?)!/;
    return offices.map((obj, i) => {
      if (obj['Google Maps Url'].length > 0) {
        obj.lat = parseFloat(obj['Google Maps Url'].match(latRegex)[1] || 0);
        obj.lng = parseFloat(obj['Google Maps Url'].match(lngRegex)[1] || 0);
      } else {
        obj.lat = 55.6998089;
        obj.lng = 13.1676404;
      }
      obj.mapMarker = {};
      obj.id = i;
      return obj;
    });
  };

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default FindMyOffice;
