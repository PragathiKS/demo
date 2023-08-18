import $ from 'jquery';
import 'bootstrap';
import keyDownSearch from '../../../scripts/utils/searchDropDown';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { REG_EMAIL,ajaxMethods } from '../../../scripts/utils/constants';
import { isExternal } from '../../../scripts/utils/updateLink';
import { validateFieldsForTags, isMobileMode, storageUtil, capitalizeFirstLetter, removeParams } from '../../../scripts/common/common';
import { makeLoad, changeStepNext, loadDownloadReady, downloadLinkTrack, changeStepError } from './softconversion.analytics.js';

class Softconversion {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {

    this.cache.$modal = this.root.parent().find('.js-soft-modal');
    this.cache.$nextbtn = this.root.find('.tpatom-btn[type=button]');
    this.cache.$downloadbtn = this.root.find('.thankyouTarget');
    this.cache.$radio = this.root.find('input[type=radio][name="typeOfVisitorOptions"]');
    this.cache.$componentName = this.root.find('input[type="hidden"][name="ComponentNameSoft"]').val();
    this.cache.$parentComponent = this.root.find('input[type="hidden"][name="parentComponent"]').val();
    this.cache.$moreBtn = this.root.find(`.moreButton-${this.cache.$componentName}`);
    this.cache.$userUnknown = this.root.find('.pw-form__userUnknown');
    this.cache.$partiallyUser = this.root.find('.pw-form__userPartially');
    this.cache.$partiallyUserClone = $(this.cache.$partiallyUser).html();
    this.cache.$company = this.root.find(`.company-${this.cache.$componentName}`);
    this.cache.$position = this.root.find(`.position-${this.cache.$componentName}`);
    this.cache.$function = this.root.find(`.function-${this.cache.$componentName}`);
    this.cache.$notmebtn = this.root.find(`.notmebtn-${this.cache.$componentName}[type=button]`);
    this.cache.$yesmebtn = this.root.find(`.yesmebtn-${this.cache.$componentName}[type=button]`);
    this.cache.$dropItem = $('.pw-form__dropdown a.dropdown-item', this.root);

    this.cache.softconversionapi = this.root.find(`form.pw-form-softconversion-${this.cache.$componentName}`);
    this.cache.$submitBtn = this.root.find('button[type="submit"]');
    this.cache.$countryField = this.root.find('.country-field');
    this.cache.$positionField = this.root.find('.position-field');
    this.cache.$functionField = this.root.find('.function-field');
    this.cache.$userType = 1;
    this.cache.$preFix = 'Download | ';

    this.cache.requestPayload = {};
    this.cache.requestPayload['typeOfVisitor']='';
    this.cache.requestPayload[`firstName-${this.cache.$componentName}`]='';
    this.cache.requestPayload[`lastName-${this.cache.$componentName}`]='';
    this.cache.requestPayload[`email-${this.cache.$componentName}`]='';
    this.cache.requestPayload[`company-${this.cache.$componentName}`]='';
    this.cache.requestPayload[`site_language_${this.cache.$componentName}`]='';
    this.cache.requestPayload[`site_country_${this.cache.$componentName}`]='';
    this.cache.requestPayload[`pardot_extra_field_${this.cache.$componentName}`]='';
    this.cache.requestPayload['market-consent']='';
    this.cache.requestPayload['typeOfVisitorTitle']='';
    this.cache.requestPayload['countryTitle']='';
    this.cache.requestPayload['country']='';
    this.cache.requestPayload['position']='';
    this.cache.requestPayload['function']='';
    this.cache.countryList = [];
    this.cache.positionList = [];
    this.cache.functionList = [];
    this.cache.inputFields = this.root.find('.tab-content .formfield input');
    this.cache.requestPayload['pageurl'] = window.location.href;
  }

  validEmail(email) {
    return REG_EMAIL.test(email);
  }

  onKeydown = (event, options) => {
    if ($('.dropdown-menu').hasClass('show')) {
      keyDownSearch.call(this, event, options);
    }
  };

  getCountryList = () => {
    const self = this;
    $('.country-dropdown-select > a').map(function () {
      const datael = $(this)[0];
      self.cache.countryList.push($(datael).data('title'));
    });
    $('.country-dropdown, .country-dropdown-select').keydown(e => this.onKeydown(e, this.cache.countryList));
  }

  getPositionList = () => {
    const self = this;
    $('.position-dropdown-select > a').map(function () {
      const datael = $(this)[0];
      self.cache.positionList.push($(datael).data('title'));
    });
    $('.position-dropdown, .position-dropdown-select').keydown(e => this.onKeydown(e, this.cache.positionList));
  }

  getFunctionList = () => {
    const self = this;
    $('.function-dropdown-select > a').map(function () {
      const datael = $(this)[0];
      self.cache.functionList.push($(datael).data('title'));
    });
    $('.function-dropdown, .function-dropdown-select').keydown(e => this.onKeydown(e, this.cache.functionList));
  }

  onRadioChangeHandler = e => {
    const { requestPayload } = this.cache;
    const value = e.target.value || $(e.target).data('persist-val');
    const id = e.target.id;
    const radioName = `typeOfVisitorTitle-${this.cache.$componentName}`;
    $(e.target).val(value);
    const labelValue = $('label[for="'+id+'"]').text().trim();
    $(`input[type=hidden][name=${radioName}]`).val(labelValue);
    requestPayload['typeOfVisitor'] = id;
    requestPayload['typeOfVisitorTitle'] = value;
  }

  downloadHandler = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.thankyouTarget');
    const downloadLink = $this.data('downloadlink');

    $(`.tab-pane.tab-${this.cache.$componentName}`, this.root).removeClass('active');
    $(`#cf-step-thankyou-${this.cache.$componentName}`, this.root).addClass('active');

    // do the download analytics call
    const downloadObj = {
      linkType: isExternal(downloadLink) ? 'external': 'internal',
      linkSection: $this.data('link-section'),
      linkParentTitle:$this.data('parent-title'),
      linkName: $this.data('link-name'),
      dwnDocName:$this.data('asset-name'),
      dwnDocUrl:downloadLink,
      dwnType:'gated',
      dwnSource:''
    };

    downloadLinkTrack(downloadObj, 'downloadClick', this.cache.$parentComponent);

    window.open(downloadLink, '_blank');
  }

  moreBtnHandler = (e) => {
    e.preventDefault();
    e.stopPropagation();
    const $target = $(e.target);
    const $this = $target.closest(`.moreButton-${this.cache.$componentName}`);
    const downloadLink = $this.attr('href');

    // do the link click analytics call
    const downloadObj = {
      linkType: isExternal(downloadLink) ? 'external': 'internal',
      linkSection: $this.data('link-section'),
      linkParentTitle:$this.data('parent-title'),
      linkName: $this.data('link-name')
    };

    downloadLinkTrack(downloadObj, 'linkClick', this.cache.$parentComponent);
    window.open(downloadLink, $this.attr('target'));
  }

  notMeBtnHandler = () => {
    const $this = this;
    const { $userUnknown, $partiallyUser } = this.cache;
    isMobileMode() &&  $(`.pw-sf_body_${this.cache.$componentName}`).css('align-items', 'normal');
    $(`.tab-pane.tab-${this.cache.$componentName}`, this.root).removeClass('active');
    $(`.heading_${this.cache.$componentName}`, this.root).text($(`#heading_${this.cache.$componentName}`).val());
    $partiallyUser.hide();
    $userUnknown.show();
    $userUnknown.find('input').each(function() {
      if($(this).attr('type') !== 'radio') {
        $(this).attr('required', 'required');
      }
    });
    $(`#cf-step-1-${this.cache.$componentName}`, this.root).addClass('active');

    // do the analytics call for not me
    const $formName = $this.cache.$preFix + 'Welcome back';
    changeStepNext($formName, { 'return Type': $(`.notmebtn-${this.cache.$componentName}[type=button]`).text().trim()}, this.cache.$parentComponent);

    // reset the input values for all fields
    this.cache.inputFields.each(function(){
      $(this).val('');
      $(this).prop('checked', false);
    });

    $partiallyUser.find('input').each(function() {
      $(this).removeAttr('required');
    });

    // Reset Dropdown
    $('.dropdown-toggle span').each(function() {
      $(this).text($(this).data('placeholder'));
    });

    // Reset Cookie
    this.cache.$userType = 1;
    storageUtil.setCookie('userType', '', -1);
    storageUtil.setCookie('visitor-mail', '', -1);
    storageUtil.setCookie('countryValue', '', -1);
  }

  yesMeBtnHandler = () => {
    const $this = this;
    const { $userUnknown, $partiallyUser } = this.cache;
    const userType = parseInt(storageUtil.getCookie('userType'), 10);
    if(userType === 1) {
      $userUnknown.hide();
      $userUnknown.find('input').each(function() {
        $(this).removeAttr('required');
      });

      // Uncheck Marketing Consent
      const marketingConsent = $('.marketing-consent').find('input');
      $(marketingConsent).prop('checked', false);

      // Open Form 2
      $(`.tab-pane.tab-${this.cache.$componentName}`, this.root).addClass('active');
      $(`#cf-step-thankyou-${this.cache.$componentName}`, this.root).removeClass('active');
      $(`#cf-step-welcomeback-${this.cache.$componentName}`, this.root).removeClass('active');
      $(`#cf-step-downloadReady-${this.cache.$componentName}`, this.root).removeClass('active');
      $(`.heading_${this.cache.$componentName}`, this.root).text($(`#heading_${this.cache.$componentName}`).val());

      $partiallyUser.find('input').each(function () {
        $(this).attr('required', 'required');
      });

      $partiallyUser.show();
    }
    if(userType > 1) {
      $userUnknown.hide();
      $partiallyUser.hide();
      $(`.tab-pane.tab-${this.cache.$componentName}`, this.root).removeClass('active');
      $(`#cf-step-downloadReady-${this.cache.$componentName}`, this.root).addClass('active');

      const servletPath = this.cache.softconversionapi.data('softconversion-api-url');
      const pardotUrl = this.cache.softconversionapi.data('softconversion-pardot-url');
      const chinapardotUrl = this.cache.softconversionapi.data('softconversion-china-pardot-url');
      const countryCookie= storageUtil.getCookie('countryValue');
      const apiPayload =  {};
      apiPayload.email = storageUtil.getCookie('visitor-mail');
      apiPayload.country = countryCookie;
      apiPayload.language = this.root.find(`#site_language_${this.cache.$componentName}`).val();
      apiPayload.site = this.root.find(`#site_country_${this.cache.$componentName}`).val();
      apiPayload.pardot_extra_field = '';
      if(apiPayload.country === 'China' || apiPayload.site ==='cn' || countryCookie ==='China' ) {
        apiPayload.pardotUrl = chinapardotUrl;
        apiPayload.route_country = 'China';
      }
      else {
        apiPayload.pardotUrl = pardotUrl;
      }
      apiPayload.pageurl = this.getPageURL();
      Object.keys(this.cache.requestPayload).forEach(key => {
        if(key === 'utm_campaign') {
          apiPayload['utm_campaign'] = this.cache.requestPayload[key];
        } else if(key === 'utm_content') {
          apiPayload['utm_content'] = this.cache.requestPayload[key];
        } else if(key === 'utm_medium') {
          apiPayload['utm_medium'] = this.cache.requestPayload[key];
        } else if(key === 'utm_source') {
          apiPayload['utm_source'] = this.cache.requestPayload[key];
        }
      });

      ajaxWrapper.getXhrObj({
        url: servletPath,
        method: ajaxMethods.POST,
        data: apiPayload
      }).done(
        () => {
          $('.serviceError').removeClass('d-block');
        }
      );
    }

    // do the analytics call for yes its me
    const $formName = $this.cache.$preFix + 'Welcome back';
    changeStepNext($formName, { 'return Type': $(`.yesmebtn-${this.cache.$componentName}[type=button]`).text().trim()}, this.cache.$parentComponent);
  }

  submitForm = () => {
    const servletPath = this.cache.softconversionapi.data('softconversion-api-url');
    const pardotUrl = this.cache.softconversionapi.data('softconversion-pardot-url');
    const chinapardotUrl = this.cache.softconversionapi.data('softconversion-china-pardot-url');
    const apiPayload =  {};

    const userType = parseInt(storageUtil.getCookie('userType'), 10);
    const visitorEmail = storageUtil.getCookie('visitor-mail');
    const countryCookie= storageUtil.getCookie('countryValue');

    let dataObj = {};

    if(this.root.find(`#market-consent-${this.cache.$componentName}`).is(':checked')){
      apiPayload.marketingConsent = capitalizeFirstLetter(String(this.root.find(`#market-consent-${this.cache.$componentName}`).is(':checked')));
    }

    if(visitorEmail && userType === 1) {
      apiPayload.email = storageUtil.getCookie('visitor-mail');
      apiPayload.company = this.cache.requestPayload[`company-${this.cache.$componentName}`];
      apiPayload.position = this.cache.requestPayload['position'];
      apiPayload.function = this.cache.requestPayload['function'];
      apiPayload.country = countryCookie;

      dataObj = {
        'Company': 'NA',
        'Position': apiPayload.position,
        'Function': apiPayload.function,
        'Marketing Consent': this.root.find(`#market-consent-${this.cache.$componentName}`).is(':checked') ? 'Checked':'Unchecked'
      };
    } else {
      apiPayload.visitorType = this.cache.requestPayload['typeOfVisitorTitle'];
      apiPayload.countryTitle = this.cache.requestPayload['countryTitle'];
      apiPayload.country = this.cache.requestPayload['country'];
      apiPayload.firstName = this.cache.requestPayload[`firstName-${this.cache.$componentName}`];
      apiPayload.lastName = this.cache.requestPayload[`lastName-${this.cache.$componentName}`];
      apiPayload.email = this.cache.requestPayload[`email-${this.cache.$componentName}`];

      dataObj = {
        'E-mail': 'NA',
        'First name': 'NA',
        'Last name': 'NA',
        'Country/Region': apiPayload.country,
        'Purpose of visit': apiPayload.visitorType,
        'Marketing Consent': this.root.find(`#market-consent-${this.cache.$componentName}`).is(':checked') ? 'Checked':'Unchecked'
      };
    }

    apiPayload.language = this.cache.requestPayload[`site_language_${this.cache.$componentName}`];
    apiPayload.site = this.cache.requestPayload[`site_country_${this.cache.$componentName}`];
    apiPayload.pardot_extra_field = this.cache.requestPayload[`pardot_extra_field_${this.cache.$componentName}`];
    if(apiPayload.country === 'China' || apiPayload.site ==='cn' || countryCookie ==='China' ) {
      apiPayload.pardotUrl = chinapardotUrl;
      apiPayload.route_country = 'China';
    }
    else {
      apiPayload.pardotUrl = pardotUrl;
    }

    apiPayload.pageurl = this.getPageURL();
    Object.keys(this.cache.requestPayload).forEach(key => {
      if(key === 'utm_campaign') {
        apiPayload['utm_campaign'] = this.cache.requestPayload[key];
      } else if(key === 'utm_content') {
        apiPayload['utm_content'] = this.cache.requestPayload[key];
      } else if(key === 'utm_medium') {
        apiPayload['utm_medium'] = this.cache.requestPayload[key];
      } else if(key === 'utm_source') {
        apiPayload['utm_source'] = this.cache.requestPayload[key];
      }
    });

    // Send Visitor Params
    const visitorId = storageUtil.getCookie('visitor_id857883');
    if(visitorId) {
      apiPayload['pardot_cookie_id'] = visitorId;
    }

    ajaxWrapper.getXhrObj({
      url: servletPath,
      method: ajaxMethods.POST,
      data: apiPayload
    }).done(
      () => {
        $('.serviceError').removeClass('d-block');
      }
    );

    //drop cookies
    if(userType === 1) {
      this.cache.$userType = userType+1;
    } else {
      storageUtil.setCookie('visitor-mail', apiPayload.email, 365);
    }
    storageUtil.setCookie('userType', this.cache.$userType, 365);
    if(countryCookie){
      storageUtil.setCookie('countryValue', countryCookie, 365);
    }
    else {
      storageUtil.setCookie('countryValue', this.cache.requestPayload['country'], 365);
    }
    $(`.heading_${this.cache.$componentName}`, this.root).text('');
    $(`.tab-pane.tab-${this.cache.$componentName}`, this.root).removeClass('active');
    $(`#cf-step-downloadReady-${this.cache.$componentName}`, this.root).addClass('active');
    isMobileMode() &&  $(`.pw-sf_body_${this.cache.$componentName}`).css('align-items', 'center');

    // Analytics Tracking
    loadDownloadReady(this.cache.$preFix+this.mainHeading, dataObj, this.cache.$parentComponent);
  }

  getPageURL() {
    const { requestPayload } = this.cache;
    const params = {};
    let pageURL = this.cache.requestPayload['pageurl'];

    window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(_, key, value) {
      return params[key] = value;
    });

    Object.keys(params).forEach(key => {
      if(key === 'utm_campaign') {
        requestPayload['utm_campaign'] = params[key];
        pageURL = removeParams('utm_campaign', pageURL);
      } else if(key === 'utm_content') {
        requestPayload['utm_content'] = params[key];
        pageURL = removeParams('utm_content', pageURL);
      } else if(key === 'utm_medium') {
        requestPayload['utm_medium'] = params[key];
        pageURL = removeParams('utm_medium', pageURL);
      } else if(key === 'utm_source') {
        requestPayload['utm_source'] = params[key];
        pageURL = removeParams('utm_source', pageURL);
      }
    });

    pageURL = pageURL.split('?');
    return pageURL[0];
  }

  bindEvents() {
    const {requestPayload, $radio, $submitBtn, $componentName, $parentComponent, $downloadbtn, $notmebtn, $yesmebtn, $moreBtn, $dropItem, $partiallyUser } = this.cache;

    const self = this;
    $($partiallyUser).hide();
    $(window).ready(function() {
      $('#pw-form-soft-conversion').on('keypress', function (event) {
        var keyPressed = event.keyCode || event.which;
        if (keyPressed === 13) {
          event.preventDefault();
          return false;
        }
      });
    });

    this.root.on('click', '.js-close-btn', this.hidePopUp).on('showsoftconversion-pw', this.showPopup);
    $radio.on('change', this.onRadioChangeHandler);
    $downloadbtn.on('click', this.downloadHandler);
    $moreBtn.on('click', this.moreBtnHandler);
    $notmebtn.on('click', this.notMeBtnHandler);
    $yesmebtn.on('click', this.yesMeBtnHandler);

    $submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();
      let isvalid = true;
      const errObj = [];
      const tab = $(this).closest('.tab-content-steps');
      const target = $(this).data('target');
      $('input', tab).each(function () {
        const fieldName = $(this).attr('name');

        const newSafeValues = $(this).attr('type') !== 'hidden' ? validateFieldsForTags($(this).val()) : $(this).val();

        if (fieldName in self.cache.requestPayload) {
          requestPayload[fieldName] = newSafeValues;
        }
        if($(this).attr('type') === 'checkbox' && $(this).attr('name') === 'market-consent'){
          requestPayload[fieldName] = $('input[name="market-consent"]:checked').length > 0;
        }
        if (($(this).prop('required') && $(this).val() === '')) {

          const errmsg = $(this).closest('.form-group, .formfield').find('.errorMsg').text().trim();
          let erLbl = '';

          const userType = parseInt(storageUtil.getCookie('userType'), 10);
          const visitorEmail = storageUtil.getCookie('visitor-mail');

          if(visitorEmail && userType === 1) {
            isvalid = false;
            switch (fieldName) {
            case `company-${$componentName}`:
              erLbl = $(this).closest('.formfield').find('label').text();
              break;
            case `positionTitle`:
              erLbl = $(this).closest('.formfield').find('label').text();
              break;
            case `functionTitle`:
              erLbl = $(this).closest('.formfield').find('label').text();
              break;
            default:
              erLbl = fieldName;
              break;
            }
            $(this).closest('.form-group, .formfield').addClass('field-error');
          } else {
            isvalid = false;
            switch (fieldName) {
            case `typeOfVisitorTitle-${$componentName}`:
              erLbl = self.step1heading;
              break;
            case `email-${$componentName}`:
              if((fieldName === `email-${$componentName}`) && !self.validEmail($(this).val())) {
                erLbl = $(`#cf-step-1-${$componentName} label`)[0].textContent;
              } else {
                erLbl = $(`#cf-step-1-${$componentName} label`)[0].textContent;
              }
              break;
            case `firstName-${$componentName}`:
              erLbl = $(`#cf-step-1-${$componentName} label`)[1].textContent;
              break;
            case `lastName-${$componentName}`:
              erLbl = $(`#cf-step-1-${$componentName} label`)[2].textContent;
              break;
            case `countryTitle`:
              erLbl = $(this).closest('.formfield').find('label').text();
              break;
            default:
              erLbl = fieldName;
              break;
            }
            $(this).closest('.form-group, .formfield').addClass('field-error');
          }

          errObj.push({
            formErrorMessage: errmsg,
            formErrorField: erLbl
          });
        }else {
          $(this).closest('.form-group, .formfield').removeClass('field-error');
        }
      });
      if (isvalid) {
        self.submitForm();
      } else if(!isvalid && target ===`#cf-step-downloadReady-${$componentName}`) {
        changeStepError(self.cache.$preFix+self.mainHeading, 'Step 1', self.step1heading, {}, $parentComponent, errObj);
      }
    });

    $dropItem.click(function (e) {
      e.preventDefault();
      const title = $(this).data('title');
      const value = $(this).data('key');
      const field = $(this).data('field-name');
      const fieldtitle = $(this).data('field-title');
      const parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle span', parentDrop).text(title);
      $('input', parentDrop).val(title);
      requestPayload[field] = value || title;
      if(field === 'country') {
        requestPayload[fieldtitle] = title;
      }
      $dropItem.removeClass('active');
      $(this).addClass('active');
    });
  }

  showPopup = () => {
    const $this = this;
    const $formName = $this.cache.$preFix+$this.mainHeading;
    const visitorMail = storageUtil.getCookie('visitor-mail');
    if(visitorMail) {
      makeLoad('welcome back', $formName, this.cache.$parentComponent, 'welcome back:formstart');
      $(`#visitor-email-${this.cache.$componentName}`).text(visitorMail).css('font-weight', 900);
      $(`.heading_${this.cache.$componentName}`, this.root).text('');
      $(`.tab-pane.tab-${this.cache.$componentName}`, this.root).removeClass('active');
      $(`#cf-step-welcomeback-${this.cache.$componentName}`, this.root).addClass('active');
      isMobileMode() &&  $(`.pw-sf_body_${this.cache.$componentName}`).css('align-items', 'center');
    }else{
      makeLoad($this.step1heading, $formName, this.cache.$parentComponent, 'formstart');
    }

    const { $modal } = $this.cache;
    $modal.modal();
  }

  hidePopUp = () => {
    const $this = this;
    $this.root.modal('hide');
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.mainHeading = $(`#heading_${this.cache.$componentName}`).val();
    this.step1heading = $(`#cf-step-1-${this.cache.$componentName} .radioHeading`).text().trim();
    this.getCountryList();
    this.getPositionList();
    this.getFunctionList();
  }
}

export default Softconversion;
