import $ from 'jquery';
import 'bootstrap';
import keyDownSearch from '../../../scripts/utils/searchDropDown';
import { subscriptionAnalytics } from './subscriptionform.analytics.js';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods, REG_EMAIL } from '../../../scripts/utils/constants';
import { getLinkClickAnalytics, validateFieldsForTags, capitalizeFirstLetter, removeParams, storageUtil } from '../../../scripts/common/common';
class Subscriptionform {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    this.cache.$modal = this.root.parent().find('.js-subscription-modal');
    this.cache.$componentName = this.root.find('input[type="hidden"][name="ComponentNameSubscribe"]').val();
    this.cache.$parentComponent = this.root.find('input[type="hidden"][name="parentComponentSubscribe"]').val();
    this.cache.businessformapi = this.root.find(`form.pw-form-subscriptionForm-${this.cache.$componentName}`);
    this.cache.$submitBtn = this.root.find('button[type="submit"]');
    this.cache.communicationTypes = this.root.find('.communication-type');
    this.cache.communicationError = this.root.find('.communication-error');
    this.cache.$dropItem = $('.pw-form__dropdown a.dropdown-item', this.root);
    this.cache.$dropdownButton = $('.dropdown-menu, .dropdown-toggle', this.root);
    this.cache.countryList = [];
    this.cache.requestPayload = {
      'consent' :'',
      'country':'',
      'pageurl': window.location.href
    };
    this.cache.requestPayload[`pardot_extra_field_${this.cache.$componentName}`]='';
    this.cache.requestPayload[`firstName-${this.cache.$componentName}`]='';
    this.cache.requestPayload[`lastName-${this.cache.$componentName}`]='';
    this.cache.requestPayload[`email-${this.cache.$componentName}`]='';
    this.cache.mainHead = $($('.'+this.cache.$componentName).find('.pw-subscription__modalTitle')).html();
    this.cache.$thankYouCTA = this.root.find('.thankyouTarget');
  }

  validEmail(email) {
    return REG_EMAIL.test(email);
  }
  
  /**
   * key down search in case of country field
   * @param {object} event event
   * @param {Array} options options
  */
  onKeydown = (event, options) => {
    const { $dropdownButton } = this.cache;
    if ($dropdownButton.hasClass('show')) {
      keyDownSearch.call(this, event, options);
    }
  };

  /**
   * function to enable auto suggest
  */
  getCountryList() {
    const { $dropItem,$dropdownButton } = this.cache;
    const self = this;
    $dropItem.map(function () {
      const datael = $(this)[0];
      self.cache.countryList.push($(datael).data('countrytitle'));
    });
    $dropdownButton.keydown(e => this.onKeydown(e, this.cache.countryList));
  }

  submitForm = () => {
    const servletPath = this.cache.businessformapi.data('sf-api-servlet');
    const pardotURL = this.cache.businessformapi.data('sf-pardot-url');
    const chinapardotURL = this.cache.businessformapi.data('sf-china-pardot-url');
    const countryCode = this.cache.businessformapi.data('sf-countrycode');
    const marketSiteSubscribed = this.cache.businessformapi.data('sf-marketsitesubscribed');
    const langCode = this.cache.businessformapi.data('sf-langcode');

    const dataObj = {};
    if($('input[name="consent"]').is(':checked')) {
      dataObj['marketingConsent'] = capitalizeFirstLetter(String($('input[name="consent"]').is(':checked')));
    }
    dataObj['email'] = this.cache.requestPayload[`email-${this.cache.$componentName}`];
    dataObj['firstName'] = this.cache.requestPayload[`firstName-${this.cache.$componentName}`];
    dataObj['lastName'] = this.cache.requestPayload[`lastName-${this.cache.$componentName}`];
    dataObj['pardot_extra_field'] = this.cache.requestPayload[`pardot_extra_field_${this.cache.$componentName}`];
    dataObj['language'] = langCode;
    dataObj['site'] = countryCode;
    dataObj['marketSiteSubscribed'] = marketSiteSubscribed;
    dataObj['country'] = this.cache.requestPayload['country'];
    // dataObj['pageurl'] = this.cache.requestPayload['pageurl'];
    if(this.cache.requestPayload['country'] === 'China' || countryCode === 'cn') {
      dataObj['pardotUrl'] = chinapardotURL;
      dataObj['route_country'] = 'China';
    }
    else {
      dataObj['pardotUrl'] = pardotURL;
    }
    subscriptionAnalytics(
      this.cache.mainHead, {
        'E-mail': 'NA',
        'First Name': 'NA',
        'Last Name': 'NA',
        'Country/Location':dataObj.country,
        'Marketing Consent': dataObj.marketingConsent ? 'Checked':'Unchecked'
      }, 'formcomplete', 'formload', 
      []
    );

    // IF UTM fields in URL
    const params = {};
    window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(_, key, value) {
      return params[key] = value;
    });

    let pageURL = this.cache.requestPayload['pageurl'];
    Object.keys(params).forEach(key => {
      if(key === 'utm_campaign') {
        dataObj['utm_campaign'] = params[key];
        pageURL = removeParams('utm_campaign', pageURL);
      } else if(key === 'utm_content') {
        dataObj['utm_content'] = params[key];
        pageURL = removeParams('utm_content', pageURL);
      } else if(key === 'utm_medium') {
        dataObj['utm_medium'] = params[key];
        pageURL = removeParams('utm_medium', pageURL);
      } else if(key === 'utm_source') {
        dataObj['utm_source'] = params[key];
        pageURL = removeParams('utm_source', pageURL);
      }
    });

    pageURL = pageURL.split('?');
    dataObj['pageurl'] = pageURL[0];

    // Send Visitor Params
    const visitorId = storageUtil.getCookie('visitor_id857883');
    if(visitorId) {
      dataObj['pardot_cookie_id'] = visitorId;
    }
    
    ajaxWrapper.getXhrObj({
      url: servletPath,
      method: ajaxMethods.POST,
      data: $.param(dataObj,true),
      dataType: 'html'
    }).done(
      () => {
        $('.pw-subscription__modalTitle').hide();
        $('.pw-subscription__thankYouTitle').show();
        $('.sf-tab-pane', this.root).removeClass('active');
        $('#sf-step-final', this.root).addClass('active');
        $('.serviceError').hide();
      }
    ).fail(() => {
      $('.serviceError').show();
    });
  }


  bindEvents() {
    const { requestPayload, $submitBtn, $dropItem, $thankYouCTA, $componentName } = this.cache;
    this.root.on('click', '.js-close-btn', this.hidePopUp)
      .on('showSubscription-pw', this.showPopup);
    
    const self = this;
    $submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();

      let isvalid = true;
      const errObj = [];
      const tab = $(this).closest('.tab-content-steps');
      const input = tab.find('input');
      
      if (!$(this).hasClass('previousbtn') && (input.length > 0)) {
        $('input:not(.communication-type), textarea', tab).each(function () {
          const fieldName = $(this).attr('name');
          const newSafeValues = $(this).attr('type') !== 'hidden' ? validateFieldsForTags($(this).val()) : $(this).val();
          $('div.'+fieldName).text($(this).val());
          if (fieldName in self.cache.requestPayload) {
            requestPayload[fieldName] = newSafeValues;
          }
          if($(this).attr('type') === 'checkbox' && $(this).attr('name') === 'consent'){
            requestPayload[fieldName] = $('input[name="consent"]:checked').length > 0;
          }
          if (($(this).prop('required') && $(this).val() === '') || (fieldName === `email-${self.cache.$componentName}` && !self.validEmail($(this).val())) || ((fieldName === 'consent') && !$(this).prop('checked'))) {
            isvalid = false;
            const errmsg = $(this).closest('.form-group, .formfield').find('.errorMsg').text().trim();
            let erLbl = $(this).closest('.formfield').find('label').html();
            if(fieldName === 'consent') {
              erLbl = 'Marketing Consent';
            }
            errObj.push({
              formErrorMessage: errmsg,
              formErrorField: erLbl
            });
            e.preventDefault();
            e.stopPropagation();
            $(this).closest('.form-group, .formfield').addClass('field-error');
          } else {
            $(this).closest('.form-group, .formfield').removeClass('field-error');
          }
        });
      }
      if (isvalid) {
        tab.find('.form-group, .formfield').removeClass('field-error');
        self.submitForm();
      }else {
        subscriptionAnalytics(self.cache.mainHead, {}, 'formerror', 'formclick', errObj);
      }
    });

    $dropItem.click(function (e) {
      e.preventDefault();
      const countryTitle = $(this).data('countrytitle');
      const parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle span', parentDrop).text(countryTitle);
      $('input', parentDrop).val(countryTitle);
      requestPayload['country'] = countryTitle;
    });

    $thankYouCTA.click(function(e) {
      const $target = e.currentTarget;
      e.preventDefault();
      const $parentTitle = $('.'+$componentName).find('.pw-subscription__thankYouTitle').html();
      $(this).attr('data-parent-title', $parentTitle);
      getLinkClickAnalytics(e, 'parent-title', 'newsletter subscription form', '.thankyouTarget', false);
      self.hidePopUp();
      window.open($($target).attr('href'), $($target).attr('target'));
    });
  }

  showPopup = () => {
    const $this = this;
    const { $modal } = $this.cache;
    this.resetModal();
    $modal.modal();
    subscriptionAnalytics(this.cache.mainHead, {}, 'formstart', 'formload', []);
  }

  hidePopUp = () => {
    const $this = this;
    $this.root.modal('hide');
    this.resetModal();
  }

  resetModal = () => {
    /* Reset modal */
    $('.pw-subscription__modalTitle').show();
    $('.pw-subscription__thankYouTitle').hide();
    $('.sf-tab-pane', this.root).addClass('active');
    $('#sf-step-final', this.root).removeClass('active');
    $('.serviceError').hide();
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.getCountryList();
  }
}

export default Subscriptionform;