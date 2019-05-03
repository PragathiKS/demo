import $ from 'jquery';
import {render} from '../../../scripts/utils/render';
import {ajaxMethods, API_SHAREPOINT_OFFICES} from '../../../scripts/utils/constants';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import loadGoogleMapsApi from 'load-google-maps-api';

class OfficeLocator {
  cache = {};

  initCache() {
    this.cache.map = {};
    this.cache.googleMaps = '';
    this.cache.officesList = [];
    this.cache.normalizedData = [];
  }

  bindEvents() {
    loadGoogleMapsApi({key: 'AIzaSyAC6bZHia8GCcaxfWVYSoq6HFnNB17PbxQ', libraries: ['places', 'geometry']}).then((googleMaps) => {
      this.cache.googleMaps = googleMaps;
      if ('geolocation' in navigator) {
        this.cache.googleMaps.event.addDomListener(window, 'load', navigator.geolocation.getCurrentPosition(this.initMap, () => console.log('error')));//eslint-disable-line
      } else {
        this.cache.googleMaps.event.addDomListener(window, 'load', this.initMap());//eslint-disable-line
      }
      this.getOfficesList();
    }).catch(function (error) {
      console.error(error);//eslint-disable-line
    });
  }

  init() {
    this.initCache();
    this.bindEvents();
  }

  initMap = (position) => {
    let currentLat = position.coords.latitude || 55.6998089;
    let currentLng = position.coords.longitude || 13.1676404;
    this.cache.map = new this.cache.googleMaps.Map(document.getElementById('map'), {//eslint-disable-line
      center: {lat: currentLat, lng: currentLng},
      disableDefaultUI: true,
      zoom: 5
    });
    this.cache.googleMaps.event.addListener(this.cache.map, 'idle', () => {//eslint-disable-line
      this.renderVisibleOffices();
    });
  };

  normalizeData = (offices) => {
    let latRegex = /!3d(.*?)!/;
    let lngRegex = /!2d(.*?)!/;
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

  getOfficesList = () => {
    ajaxWrapper.getXhrObj({
      url: API_SHAREPOINT_OFFICES,
      method: ajaxMethods.GET,
      cache: true,
      dataType: 'json',
      contentType: 'application/json'
    }).done((data) => {
      if (data) {
        this.cache.normalizedData = this.normalizeData(data);
        this.renderMarkers();
      }
    });
  };

  renderOfficesList = (offices) => {
    render.fn({
      template: 'officesList',
      data: offices,
      target: '.js-pw-office-locator__offices-list'
    });
  };

  renderOfficeDetailsPanel = (office) => {
    console.log(office);//eslint-disable-line
    render.fn({
      template: 'officeDetailsPanel',
      data: office,
      target: '.js-pw-office-locator__office-details-panel',
      hidden: false
    }, function () {
      $(this).fadeIn();
    });
  };

  renderVisibleOffices = () => {
    let bounds = this.cache.map.getBounds(),//eslint-disable-line
      officesToRender = [];

    for (let i = 0; i < this.cache.normalizedData.length; i++) {
      let mapMarker = this.cache.normalizedData[i].mapMarker;
      let center = this.cache.map.getCenter();//eslint-disable-line

      if(mapMarker.position && bounds.contains(mapMarker.getPosition()) === true) {//eslint-disable-line
        let markerLatLng = mapMarker.getPosition();//eslint-disable-line
        this.cache.normalizedData[i].distanceFromCenter = this.cache.googleMaps.geometry.spherical.computeDistanceBetween(center, markerLatLng);//eslint-disable-line
        officesToRender.push(this.cache.normalizedData[i]);
      }
    }

    if (officesToRender.length > 0) {
      officesToRender.sort(function(a, b) {return a.distanceFromCenter - b.distanceFromCenter;});
      this.renderOfficesList(officesToRender);
    }
  };

  renderMarkers = () => {
    for (let i = 0; i < this.cache.normalizedData.length; i++) {
      let office = this.cache.normalizedData[i];
      let marker = new this.cache.googleMaps.Marker({//eslint-disable-line
        position: {lat: office.lat, lng: office.lng},
        map: this.cache.map,
        title: office.Name
      });

      this.cache.googleMaps.event.addListener(marker, 'click', ((marker, i) => {//eslint-disable-line
        return () => {
          this.renderOfficeDetailsPanel(office);
        };
      })(marker, i));
      office.mapMarker = marker;
    }
  };

}

export default OfficeLocator;
