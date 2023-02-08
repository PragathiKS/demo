import $ from 'jquery';
import 'bootstrap';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { render } from '../../../scripts/utils/render';
import keyDownSearch from '../../../scripts/utils/searchDropDown';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import loadGoogleMapsApi from 'load-google-maps-api';
import { isMobile } from '../../../scripts/common/common';


function getTimeoutBasedOnNetwork() {
  if(!window.navigator?.connection) {
    return 3000;
  }
  if(window.navigator?.connection?.effectiveType === '2g') {
    return 5000;
  } else if(window.navigator?.connection?.effectiveType === '3g') {
    return 2000;
  }
  return 1000;
}
class FindMyOffice {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  initCache() {
    this.cache.map = {};
    this.cache.bMap = null;
    this.cache.bMapMarker = null;
    this.cache.isChinaLocale = window.location.href.indexOf('cn') === -1 ? false:true;
    this.cache.googleMaps = '';
    this.cache.officesList = [];
    this.cache.officesNameList = [];
    this.cache.normalizedData = {};
    this.cache.selectedCountryValue = '';
    this.cache.cities = [];
    this.cache.selectedCityValue = '';
    this.cache.selectedOffice = {};
    this.cache.marker = null;
    this.cache.defaultLatitude = 55.6998089;
    this.cache.defaultLongitude = 13.1676404;
    this.cache.goToLocalSiteElement = this.root.find('.js-i18-value');
    this.cache.goToLocalSiteI18nValue = $(
      this.cache.goToLocalSiteElement
    ).text();
    this.cache.cityFieldTextValue = $(
      '.js-pw-form__dropdown__city .js-pw-form__dropdown__city-text'
    ).text();
    this.cache.googleApi = this.root.find('.js-google-api');
    this.cache.baiduMapApi = this.root.find('.js-baidu-map-api');
    this.cache.hiddenElement = this.root.find('.js-hidden-element');
    this.cache.linkSectionElement = this.root.find(
      '.js-pw-find-my-office__wrapper'
    );
    this.setCityInitialState();
  }

  onKeydown = (event, options) => {
    if ($('.dropdown-menu').hasClass('show')) {
      keyDownSearch.call(this, event, options);
    }
  };

  loadBaiduMapScript() {
    const baiduMapKey = this.cache.baiduMapApi.data('baidu-map-key');
    window.BMAP_PROTOCOL = 'https'; 
    const date = new Date();
    window.BMap_loadScriptTime = (date).getTime(); 
    const script = document.createElement('script');
    const t = `${date.getFullYear()}${(date.getMonth() + 1).toString().padStart(2,0)}${date.getDate().toString().padStart(2,0)}${date.getHours().toString().padStart(2,0)}${date.getMinutes().toString().padStart(2,0)}${date.getSeconds().toString().padStart(2,0)}`;
    script.src = `https://api.map.baidu.com/getscript?v=3.0&ak=${baiduMapKey}&services=&t=${t}`;
    script.onload = this.initBaiduMap;
    document.head.appendChild(script);
  }

  bindEvents() {
    const googleApiKey = this.cache.googleApi.data('google-api-key');
    if (!this.cache.isChinaLocale) {
      loadGoogleMapsApi({
        key: googleApiKey,
        libraries: ['places', 'geometry']
      }).then(this.handleGoogleMapApi);
    } else {
      this.getOfficesList();
      setTimeout(() => {
        const { country } = window.OneTrust ? window.OneTrust.getGeolocationData() : { country: '' };
        if(country === 'CN' || window.OptanonActiveGroups.includes('4')) {
          this.loadBaiduMapScript();
        }
      },getTimeoutBasedOnNetwork());
    }
    this.root.on('click', '.js-localSiteUrl', this.goToLocalSite);
  }

  initBaiduMap = () => {
    const $this = this;
    this.cache.bMap = new window.BMap.Map('pw-find-my-office__map');
    const point = new window.BMap.Point(this.cache.defaultLongitude, this.cache.defaultLatitude);
    this.cache.bMap.centerAndZoom(point, 1);
    this.cache.bMap.enableDragging();
    this.cache.bMap.enableDoubleClickZoom();
    this.cache.bMap.enablePinchToZoom();
    this.cache.bMap.enableKeyboard();

    const zoomCtrl = new window.BMap.NavigationControl({
      anchor: 'BMAP_ANCHOR_TOP_RIGHT',
      type: 'BMAP_NAVIGATION_CONTROL_ZOOM'
    });
    this.cache.bMap.addControl(zoomCtrl);
    setTimeout(function() {
      $this.customizeBaiduMap();
    }, 1500);
  }

  customizeBaiduMap = () => {
    const mapCtrl = $('.BMap_stdMpCtrl');
    const mapZoomOut = $('.BMap_stdMpZoomOut');
    
    $(mapCtrl).css({
      'height': '55px',
      'inset': 'auto -10px 20px auto',
      'top': 'auto',
      'right': '-10px',
      'bottom': '20px',
      'left': 'auto',
      'width': '50px'
    });
    $(mapZoomOut).css({
      'top':'25px'
    });
    
    // Generate View Full Map button
    const mapURI = 'https://map.baidu.com/?latlng='+ this.cache.defaultLatitude +','+ this.cache.defaultLongitude +'&autoOpen=true&l';
    const newlink = document.createElement('a');
    newlink.classList.add('BMap_stdMpViewMap');
    newlink.textContent = $(this.cache.hiddenElement).text();
    newlink.setAttribute('href', mapURI);
    newlink.setAttribute('target', '_blank');
    newlink.setAttribute('auto_locator', 'viewLargeMapCTA');
    $('.pw-find-my-office__map').prepend(newlink);
  }

  baiduMapMarker = (map, lng, lat) => {
    if(this.cache.bMapMarker) {
      map.removeOverlay(this.cache.bMapMarker);
    }
    const markerArr = [{
      icon: { w: 40, h: 55, l: 0, t: 0, x: 6, lb: 5 }
    }];

    for (let i = 0; i < markerArr.length; i++) {
      const json = markerArr[i];
      const p0 = lng;
      const p1 = lat;
      const point = new window.BMap.Point(p0, p1);
      const iconImg = this.createBaiduMapMarkerIcon(json.icon);
      const marker = new window.BMap.Marker(point, { icon: iconImg });
      this.cache.bMapMarker = marker;
      map.addOverlay(marker);
    }
  }

  createBaiduMapMarkerIcon = (json) => {
    const icon = new window.BMap.Icon('/content/dam/tetrapak/publicweb/Pin.png', new window.BMap.Size(json.w, json.h), { imageOffset: new window.BMap.Size(-json.l, -json.t), infoWindowOffset: new window.BMap.Size(json.lb + 5, 1), offset: new window.BMap.Size(json.x, json.h) });
    return icon;
  }

  goToLocalSite = e => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-localSiteUrl');
    const OfficeName = $this.data('office-name');
    const targetLink = $this.attr('target');
    const url = $this.attr('href');

    const trackingObj = {
      locateCountry: this.cache.selectedCountryValue,
      locateOffice: OfficeName,
      officeName: OfficeName
    };
    const eventObj = {
      eventType: 'gotolocalsite',
      event: 'findmyoffice'
    };

    this.trackAnalyticsCall(trackingObj, eventObj);

    if (url && targetLink) {
      window.open(url, targetLink);
    }
  };

  handleGoogleMapApi = googleMaps => {
    this.cache.googleMaps = googleMaps;
    this.cache.googleMaps.event.addDomListener(window, 'load', this.initMap());
    this.getOfficesList();
  };

  onClickCountryItem = e => {
    this.cache.selectedCountryValue = e.target.innerText;
    this.cache.selectedCountryValue = String(
      this.cache.selectedCountryValue
    ).trim();
    $('.js-pw-find-my-office__form-group__city-field').removeClass('hide');
    $('.js-pw-form__dropdown__country .js-pw-form__dropdown__country-text').text(e.target.innerText);
    $('.js-pw-form__dropdown__country').attr('title', e.target.innerText);
    this.renderCities();
    this.clearSelectedCities();
    $('.js-pw-form__dropdown__city,.js-pw-form__dropdown__city-select').keydown(e => this.onKeydown(e, this.cache.officesNameList));
    this.cache.selectedCity = this.root.find('.js-dropdown-item-city');
    this.cache.selectedCity.on('click', this.onClickCityItem);
    this.resetOfficeDetails();
    this.cache.selectedOffice = this.cache.normalizedData[
      this.cache.selectedCountryValue
    ];
    let mapZoomLevel = 5;
    let markerVisibility = false;
    if (
      Object.keys(this.cache.normalizedData).length !== 0 &&
      this.cache.normalizedData[this.cache.selectedCountryValue].offices
        .length === 1
    ) {
      this.cache.selectedOffice = this.cache.normalizedData[
        this.cache.selectedCountryValue
      ].offices[0];
      $('.js-pw-find-my-office__form-group__address').removeClass('hide');
      $('.js-pw-find-my-office__form-group__city-field').addClass('hide');
      this.renderOfficeDetailsPanel(this.cache.selectedOffice);
      !isMobile() && this.checkForMapHeight();
      mapZoomLevel = 10;
      markerVisibility = true;
    }
    this.cache.marker && this.cache.marker.setMap(null);
    this.renderMarkerPosition(this.cache.selectedOffice, {
      mapZoomLevel,
      markerVisibility
    });
  };

  checkForMapHeight = () => {
    const minimumMarginAroundForm = 96;
    const formHeight =
      $('.pw-find-my-office__form-group').outerHeight() +
      minimumMarginAroundForm;
    const existingMapHeight = $('.js-pw-find-my-office__map').outerHeight();
    if (formHeight > existingMapHeight) {
      $('.js-pw-find-my-office__map').height(formHeight);
    }
  };
  
  renderMarkerPosition = (office, options) => {
    if (this.cache.isChinaLocale) {
      if (office.offices) {
        const point = new window.BMap.Point(office.longitude, office.latitude);
        this.cache.bMap.centerAndZoom(point, 6);
      } else {
        const point = new window.BMap.Point(office.longitude, office.latitude);
        this.cache.bMap.centerAndZoom(point, 11);
        this.baiduMapMarker(this.cache.bMap, office.longitude, office.latitude);
      }
      const mapURL = 'https://map.baidu.com/?latlng='+ office.latitude + ',' + office.longitude +'&autoOpen=true&l';
      document.querySelector('.BMap_stdMpViewMap').setAttribute('href', mapURL);
    } else {
      const latLng = new this.cache.googleMaps.LatLng(
        office.latitude,
        office.longitude
      );

      this.cache.marker = new this.cache.googleMaps.Marker({
        position: latLng,
        title: office.name,
        icon: '/content/dam/tetrapak/publicweb/Pin.png'
      });

      // To add the marker to the map, call setMap();
      this.cache.marker.setMap(this.cache.map);
      this.cache.marker.setVisible(options.markerVisibility);
      this.cache.map.setZoom(options.mapZoomLevel || 5);
      this.cache.map.panTo(this.cache.marker.position);
    }
  };

  resetOfficeDetails = () => {
    this.cache.selectedOffice = {};
    this.renderOfficeDetailsPanel(this.cache.selectedOffice);
    $('.js-pw-find-my-office__form-group__address').addClass('hide');
  };

  onClickCityItem = e => {
    this.cache.selectedCityValue = e.target.innerText;
    this.cache.selectedCityValue = String(this.cache.selectedCityValue).trim();
    $('.js-pw-find-my-office__form-group__address').removeClass('hide');
    $('.js-pw-form__dropdown__city .js-pw-form__dropdown__city-text').text(
      e.target.innerText
    );
    $('.js-pw-form__dropdown__city').attr('title', e.target.innerText);
    this.renderOfficeDetails(this.cache.selectedCityValue);
  };

  clearSelectedCities = () => {
    $('.js-pw-form__dropdown__city .js-pw-form__dropdown__city-text').text(
      this.cache.cityFieldTextValue
    );
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
    !isMobile() && this.checkForMapHeight();
    this.cache.marker && this.cache.marker.setMap(null);
    this.renderMarkerPosition(this.cache.selectedOffice, {
      mapZoomLevel: 10,
      markerVisibility: true
    });
  };

  renderOfficeDetailsPanel = office => {
    if (office.name) {
      const trackingObj = {
        locateCountry: this.cache.selectedCountryValue,
        locateOffice: office.name,
        officeName: office.name
      };

      const eventObj = {
        eventType: 'office address result',
        event: 'findmyoffice'
      };

      this.trackAnalyticsCall(trackingObj, eventObj);
    }
    const linkName =
      (office.name && `${office.name} - ${this.cache.selectedCountryValue}`) ||
      this.cache.selectedCountryValue;
    this.cache.linkSectionElement.attr('data-link-name', linkName);
    render.fn({
      template: 'officeDetails',
      data: { ...office, goToLocalSiteText: this.cache.goToLocalSiteI18nValue },
      target: '.js-pw-find-my-office-office-details',
      hidden: false
    });
  };

  trackAnalyticsCall = (trackingObj, eventObj) => {
    trackAnalytics(
      trackingObj,
      'locateUs',
      'locateUsClick',
      undefined,
      false,
      eventObj
    );
  };

  renderCountryOfficesList = countries => {
    render.fn({
      template: 'countryOfficesList',
      data: countries,
      target: '.js-pw-form__dropdown__country-select'
    });
  };

  renderCitiesOfficesList = () => {
    render.fn({
      template: 'citiesOfficesList',
      data: this.cache.cities,
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
          this.cache.selectedCountry = this.root.find('.js-dropdown-item-country');
          $('.js-pw-form__dropdown__country,.js-pw-form__dropdown__country-select').keydown(e =>
            this.onKeydown(e, Object.keys(this.cache.normalizedData))
          );
          this.cache.selectedCountry.on('click', this.onClickCountryItem);
        }
      });
  };

  setCityInitialState = () => {
    $('.js-pw-form__dropdown__city').attr('disabled', true);
    $('.js-pw-find-my-office__form-group__label').addClass('opacity');
  };

  renderCountries = () => {
    const countries = Object.keys(this.cache.normalizedData);
    this.renderCountryOfficesList(countries);
  };

  renderCities = () => {
    const selectedCountry = $(
      '.js-pw-form__dropdown__country .js-pw-form__dropdown__country-text'
    )
      .text()
      .trim();
    this.cache.cities = [];
    this.cache.normalizedData[selectedCountry] &&
      this.cache.normalizedData[selectedCountry].offices.map(city => {
        this.cache.cities.push(city);
      });
    if (this.cache.cities.length > 0) {
      $('.js-pw-form__dropdown__city').removeAttr('disabled');
      $('.js-pw-find-my-office__form-group__label').removeClass('opacity');
      this.getOfficesName();
      this.renderCitiesOfficesList();
    }
  };

  getOfficesName = () => {
    this.cache.officesNameList = this.cache.cities.map(office => office.name);
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
    const gotoMapButton = document.createElement('div');
    $(gotoMapButton).addClass('view-larger-map');
    $(gotoMapButton).attr('auto_locator', 'viewLargeMapCTA');
    gotoMapButton.innerHTML = $(this.cache.hiddenElement).text();
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
    const url = `https://www.google.com/maps?q=${encodeURIComponent(
      uri.toUrlValue()
    )}`;
    // you can also hard code the URL
    window.open(url);
  };

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default FindMyOffice;
