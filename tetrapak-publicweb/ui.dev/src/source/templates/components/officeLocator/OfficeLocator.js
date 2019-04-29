import {render} from '../../../scripts/utils/render';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {ajaxWrapper} from '../../../scripts/utils/ajax';

class OfficeLocator {
  cache = {};

  initCache() {
    this.cache.map = {};
    this.cache.service = '';
    this.cache.officesList = [];
    this.cache.normalizedData = [];
    this.cache.mapMarkers = [];
  }

  bindEvents() {

    if ('geolocation' in navigator) {
      //this.cache.location = navigator.geolocation.getCurrentPosition(this.initMap, () => console.log('error'));//eslint-disable-line
    } else {
      //this.initMap();
    }
  }

  init() {
    this.initCache();
    this.bindEvents();
    this.getOfficesList();
    if ('geolocation' in navigator) {
      google.maps.event.addDomListener(window, 'load', navigator.geolocation.getCurrentPosition(this.initMap, () => console.log('error')));//eslint-disable-line
    } else {
      google.maps.event.addDomListener(window, 'load', this.initMap());//eslint-disable-line
    }

  }

  getOfficesList = () => {
    ajaxWrapper.getXhrObj({
      url: `../../apps/settings/wcm/designs/publicweb/jsonData/tp-offices.json`,
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

  renderOfficesList = (office) => {
    render.fn({
      template: 'officesList',
      data: office,
      target: '.js-pw-office-locator__offices-list'
    });
  };

  initMap = (position) => {
    let currentLat = position.coords.latitude || 55.6998089;
    let currentLng = position.coords.longitude || 13.1676404;
    this.cache.map = new google.maps.Map(document.getElementById('map'), {//eslint-disable-line
      center: {lat: currentLat, lng: currentLng},
      disableDefaultUI: true,
      zoom: 15
    });

    google.maps.event.addListener(this.cache.map, 'idle', () => {//eslint-disable-line
      this.renderVisibleOffices();
    });
  };

  renderVisibleOffices = () => {
    let bounds = this.cache.map.getBounds(),//eslint-disable-line
      officesToRender = [];

    for (let i = 0; i < this.cache.mapMarkers.length; i++) {
      let mapMarker = this.cache.mapMarkers[i];

      if(bounds.contains(mapMarker.getPosition()) === true) {//eslint-disable-line
        officesToRender.push(this.cache.normalizedData.find(el => el.id === mapMarker.officeData.id));
      }
    }
    this.renderOfficesList(officesToRender);
  };

  renderMarkers = () => {
    //console.log(this.cache.normalizedData);//eslint-disable-line
    for (let i = 0; i < this.cache.normalizedData.length; i++) {
      let office = this.cache.normalizedData[i];
      let marker = new google.maps.Marker({//eslint-disable-line
        position: {lat: office.lat, lng: office.lng},
        map: this.cache.map,
        officeData: office,
        title: office.Name
      });

      google.maps.event.addListener(marker, 'click', ((marker, i) => {//eslint-disable-line
        return () => {
          this.renderOfficesList([{
            'Title': 'Representative Office',
            'Name': 'Tetra Pak d.o.o',
            'Local Name': '',
            'Address': 'Note: Managed by Croatia office --- TETRA PAK d.o.o., Bani 110, 10010 Zagreb',
            'Local Address': '',
            'Cluster': '',
            'Country': 'Albania',
            'E-mail': 'Maria.Sigala@tetrapak.com',
            'Sort Order': null,
            'Google Maps Url': 'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d9364.047671391842!2d15.990230483928658!3d45.75218783787174!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x4765d5893b03fd31%3A0x2b38dd652522c0c8!2sTetra+Pak+D.O.O.!5e0!3m2!1sen!2sse!4v1485417600059',
            'Phone': '+385 1 661 0000',
            'Fax': '+385 1 661 0050',
            'Local site url': '',
            'Local site label': '',
            'Modified': '11/22/17 12:40',
            'Item Type': 'Item',
            'Path': 'Lists/Offices'
          }]);
        };
      })(marker, i));
      this.cache.mapMarkers.push(marker);
    }
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
      obj.id = i;
      return obj;
    });
  };
}

export default OfficeLocator;
