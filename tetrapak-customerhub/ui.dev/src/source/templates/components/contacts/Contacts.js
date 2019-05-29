import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { ajaxMethods } from '../../../scripts/utils/constants';

/**
 * Renders contacts based on selected site as filter
 * @param {object} data JSON data object for selected site
 */
function _renderFilteredContacts(data = this.cache.data) {
  const siteVal = this.cache.$site.val();
  const filteredContacts = data.results[siteVal].contacts;

  render.fn({
    template: 'contactListing',
    data: filteredContacts,
    target: '.js-contacts__listing'
  });
}
/**
 * Process Sites Data
 * @param {object} data JSON data object
 */
function _processSites(data) {
  if (Array.isArray(data.results)) {
    data.allSites = {};
    data.allSites = data.results.map((siteResult, index) => ({
      key: index,
      desc: siteResult.site
    }));
  } else {
    data.noData = true;
  }
}
/**
 * Renders sites
 * @param {object} data JSON data object for selected site
 */
function _renderSites() {
  const $this = this;
  auth.getToken(({ data: authData }) => {
    render.fn({
      template: 'contactsFiltering',
      url: {
        path: '/apps/settings/wcm/designs/customerhub/jsonData/contacts.json'
      },
      target: '.js-contacts__filtering',
      ajaxConfig: {
        method: ajaxMethods.GET,
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        cache: true,
        showLoader: true,
        cancellable: true
      },
      beforeRender(data) {
        const { i18nKeys } = $this.cache;

        if (!data) {
          this.data = data = {
            isError: true,
            i18nKeys
          };
        } else {
          data.i18nKeys = i18nKeys;
          $this.processSites(data);
          $this.cache.data = data;
        }
      }
    }, (data) => {
      if (!data.isError && !data.noData) {
        $this.initPostCache();

        // creating two-dimensional array of respective site's contacts
        // to create multiple rows of 3-columns
        data.results.map(site => {
          const filteredContacts = [];
          while (site.contacts.length > 0) {
            filteredContacts.push(site.contacts.splice(0, 3));
          }
          site.contacts = filteredContacts;
        });
        $this.renderFilteredContacts(data);
      }
    });
  });
}

class Contacts {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  /**
  * Initialize selector cache after filters rendering
  */
  initPostCache() {
    this.cache.$site = this.root.find('.js-contacts-filtering__site');
  }
  /**
  * Initialize selector cache on component load
  */
  initCache() {
    /* Initialize selector cache here */
    this.cache.configJson = this.root.find('.js-contacts__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }
  bindEvents() {
    const self = this;
    /* Bind jQuery events here */
    this.root.on('change', '.js-contacts-filtering__site', function () {
      self.initPostCache();
      self.renderFilteredContacts();
    });
  }
  renderFilteredContacts = (...arg) => _renderFilteredContacts.apply(this, arg);
  processSites = (...arg) => _processSites.apply(this, arg);
  renderSites = () => _renderSites.call(this);
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderSites();
  }
}

export default Contacts;
