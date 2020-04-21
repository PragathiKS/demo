import $ from 'jquery';
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
    this.cache.normalizedData = [];
    this.cache.countryToggle = this.root.find('.js-pw-form__dropdown__country');
    this.cache.cityToggle = this.root.find('.js-pw-form__dropdown__city');
    $(window).click(function() {
      $('.dropdown-menu').removeClass('show');
    });
  }

  bindEvents() {
    loadGoogleMapsApi({
      key: 'AIzaSyC1w2gKCuwiRCsgqBR9RnSbWNuFvI5lryQ',
      libraries: ['places', 'geometry']
    })
      .then(googleMaps => {
        this.cache.googleMaps = googleMaps;
        if ('geolocation' in navigator) {
          this.cache.googleMaps.event.addDomListener(
            window,
            'load',
            navigator.geolocation.getCurrentPosition(this.initMap, () =>
              // eslint-disable-next-line no-console
              console.log('error')
            )
          );
        } else {
          this.cache.googleMaps.event.addDomListener(
            window,
            'load',
            this.initMap()
          );
        }
      })
      .catch(function(error) {
        // eslint-disable-next-line no-console
        console.error(error);
      });

    this.cache.countryToggle.on('click', this.countryDropDownToggle);
    this.cache.cityToggle.on('click', this.cityDropDownToggle);
  }

  init() {
    this.initCache();
    this.bindEvents();
  }

  initMap = position => {
    const currentLat = position.coords.latitude || 55.6998089;
    const currentLng = position.coords.longitude || 13.1676404;
    this.cache.map = new this.cache.googleMaps.Map(
      document.querySelector('.js-pw-find-my-office__map'),
      {
        //eslint-disable-line
        center: { lat: currentLat, lng: currentLng },
        disableDefaultUI: true,
        zoom: 5
      }
    );
    // this.cache.googleMaps.event.addListener(this.cache.map, 'idle', () => {
    //   //eslint-disable-line
    //   this.renderVisibleOffices();
    // });
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
}

export default FindMyOffice;
