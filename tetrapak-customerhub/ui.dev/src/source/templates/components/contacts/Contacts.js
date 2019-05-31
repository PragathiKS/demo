import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { trackAnalytics } from '../../../scripts/utils/analytics';

/**
 * Fire analytics on click of
 * site filter or contact's mail, phone
 */
function _trackAnalytics(name, title) {
  const analyticsData = {
    linkType: 'internal',
    linkSection: 'contact'
  };

  if (['email', 'phone'].includes(name)) {
    analyticsData.linkParentTitle = `contact-${title}`;
    analyticsData.linkName = name;
  } else {
    analyticsData.linkParentTitle = name;
    analyticsData.linkSelection = 'site name';
    analyticsData.linkName = 'site name selection';
  }

  trackAnalytics(analyticsData, 'linkClick', 'linkClicked', undefined, false);
}

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
  initPostCache() {
    this.cache.$site = this.root.find('.js-contacts-filtering__site');
  }
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
    this.root
      .on('change', '.js-contacts-filtering__site', function () {
        let { siteFilterHeading } = self.cache.i18nKeys;

        self.initPostCache();
        self.renderFilteredContacts();

        siteFilterHeading = siteFilterHeading
          ? siteFilterHeading.toLowerCase()
          : 'find contacts on your site';
        self.trackAnalytics(siteFilterHeading);
      })
      .on('click', '.js-contacts__cell-mail', function () {
        self.trackAnalytics('email', $(this).data('title').toLowerCase());
      })
      .on('click', '.js-contacts__cell-phone', function () {
        self.trackAnalytics('phone', $(this).data('title').toLowerCase());
      });
  }
  renderFilteredContacts = (...arg) => _renderFilteredContacts.apply(this, arg);
  processSites = (...arg) => _processSites.apply(this, arg);
  renderSites = () => _renderSites.call(this);
  trackAnalytics = (name, type) => _trackAnalytics.call(this, name, type);
  init() {
    this.initCache();
    this.bindEvents();
    this.renderSites();
  }
}

export default Contacts;
