import $ from 'jquery';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { REG_EMAIL,ajaxMethods } from '../../../scripts/utils/constants';
import { isExternal } from '../../../scripts/utils/updateLink';
import { validateFieldsForTags, isMobileMode, storageUtil } from '../../../scripts/common/common';
import { makeLoad, changeStepNext, loadDownloadReady, downloadLinkTrack, changeStepPrev, changeStepError } from './softconversion.analytics.js';

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
    this.cache.$moreBtn = this.root.find(`.moreButton-${this.cache.$componentName}`);
    this.cache.$company = this.root.find(`.company-${this.cache.$componentName}`);
    this.cache.$position = this.root.find(`.position-${this.cache.$componentName}`);
    this.cache.$notmebtn = this.root.find(`.notmebtn-${this.cache.$componentName}[type=button]`);
    this.cache.$yesmebtn = this.root.find(`.yesmebtn-${this.cache.$componentName}[type=button]`);
    
    this.cache.softconversionapi = this.root.find(`form.pw-form-softconversion-${this.cache.$componentName}`);
    this.cache.$submitBtn = this.root.find('button[type="submit"]');
   
    this.cache.requestPayload = {};
    this.cache.requestPayload['typeOfVisitor']=''; 
    this.cache.requestPayload[`firstName-${this.cache.$componentName}`]='';
    this.cache.requestPayload[`lastName-${this.cache.$componentName}`]='';
    this.cache.requestPayload[`email-${this.cache.$componentName}`]='';
    this.cache.requestPayload[`company-${this.cache.$componentName}`]='';
    this.cache.requestPayload[`position-${this.cache.$componentName}`]='';
    this.cache.requestPayload[`site_language_${this.cache.$componentName}`]='';
    this.cache.requestPayload[`site_country_${this.cache.$componentName}`]='';
    this.cache.requestPayload[`pardot_extra_field_${this.cache.$componentName}`]='';
    this.cache.requestPayload['market-consent']='';
    this.cache.requestPayload['typeOfVisitorTitle']='';

  }

  validEmail(email) {
    return REG_EMAIL.test(email);
  }

  onRadioChangeHandler = e => {
    const { requestPayload } = this.cache;
    const value = e.target.value;
    const id = e.target.id;
    const radioName = `typeOfVisitorTitle-${this.cache.$componentName}`;
    $(`input[type=hidden][name=${radioName}]`).val(value);
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

    downloadLinkTrack(downloadObj, 'downloadClick', this.mainHeading, 'download');

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

    downloadLinkTrack(downloadObj, 'linkClick');
    window.open(downloadLink, $this.attr('target'));
  }

  notMeBtnHandler = () => {
    isMobileMode() &&  $(`.pw-sf_body_${this.cache.$componentName}`).css('align-items', 'normal');
    $(`.tab-pane.tab-${this.cache.$componentName}`, this.root).removeClass('active');
    $(`.heading_${this.cache.$componentName}`, this.root).text($(`#heading_${this.cache.$componentName}`).val());
    $(`#cf-step-1-${this.cache.$componentName}`, this.root).addClass('active');
    // do the analytics call for not me
    changeStepNext(this.mainHeading, 'Step 1', 'welcome back', { customerType: $(`.notmebtn-${this.cache.$componentName}[type=button]`).text().trim()});
  }

  yesMeBtnHandler = () => {
    $(`.tab-pane.tab-${this.cache.$componentName}`, this.root).removeClass('active');
    $(`#cf-step-downloadReady-${this.cache.$componentName}`, this.root).addClass('active');

    const servletPath = this.cache.softconversionapi.data('softconversion-api-url');
    const pardotUrl = this.cache.softconversionapi.data('softconversion-pardot-url');
    const apiPayload =  {};
    apiPayload.email = storageUtil.getCookie('visitor-mail');
    apiPayload.language = this.root.find(`#site_language_${this.cache.$componentName}`).val();
    apiPayload.site = this.root.find(`#site_country_${this.cache.$componentName}`).val();
    apiPayload.pardot_extra_field = '';
    apiPayload.pardotUrl = pardotUrl;
    apiPayload.marketingConsent = true;

    ajaxWrapper.getXhrObj({
      url: servletPath,
      method: ajaxMethods.POST,
      data: apiPayload
    }).done(
      () => {
        $('.serviceError').removeClass('d-block');
      }
    );

    // do the analytics call for yes its me
    changeStepNext(this.mainHeading, 'Step 1', 'welcome back', { customerType: $(`.yesmebtn-${this.cache.$componentName}[type=button]`).text().trim()});
  }

  submitForm = () => {
    const servletPath = this.cache.softconversionapi.data('softconversion-api-url');
    const pardotUrl = this.cache.softconversionapi.data('softconversion-pardot-url');
    
    const apiPayload =  {};

    apiPayload.visitorType = this.cache.requestPayload['typeOfVisitorTitle'];
    apiPayload.firstName = this.cache.requestPayload[`firstName-${this.cache.$componentName}`];
    apiPayload.lastName = this.cache.requestPayload[`lastName-${this.cache.$componentName}`];
    apiPayload.email = this.cache.requestPayload[`email-${this.cache.$componentName}`];
    apiPayload.company = this.cache.requestPayload[`company-${this.cache.$componentName}`];
    apiPayload.position = this.cache.requestPayload[`position-${this.cache.$componentName}`];
    apiPayload.language = this.cache.requestPayload[`site_language_${this.cache.$componentName}`];
    apiPayload.site = this.cache.requestPayload[`site_country_${this.cache.$componentName}`];
    apiPayload.pardot_extra_field = this.cache.requestPayload[`pardot_extra_field_${this.cache.$componentName}`];
    apiPayload.pardotUrl = pardotUrl;
    apiPayload.marketingConsent = this.root.find(`#market-consent-${this.cache.$componentName}`).is(':checked'); 
    loadDownloadReady(this.mainHeading, { ...this.restObj2, 'Marketing Consent': apiPayload.marketingConsent ? 'Checked':'Unchecked' });
    ajaxWrapper.getXhrObj({
      url: servletPath,
      method: ajaxMethods.POST,
      data: apiPayload
    }).done(
      () => {
        $('.serviceError').removeClass('d-block');
      }
    );
    //drop cookies of email id 
    storageUtil.setCookie('visitor-mail', apiPayload.email, 365);

    $(`.heading_${this.cache.$componentName}`, this.root).text('');
    $(`.tab-pane.tab-${this.cache.$componentName}`, this.root).removeClass('active');
    $(`#cf-step-downloadReady-${this.cache.$componentName}`, this.root).addClass('active');
    isMobileMode() &&  $(`.pw-sf_body_${this.cache.$componentName}`).css('align-items', 'center');
  }


  bindEvents() {
    const {requestPayload, $radio, $nextbtn, $submitBtn, $componentName, $company, $position, $downloadbtn, $notmebtn, $yesmebtn, $moreBtn } = this.cache;
    const self = this;
    this.root.on('click', '.js-close-btn', this.hidePopUp)
      .on('click', function () {
        if ($(this).hasClass('js-soft-modal')) {
          this.hidePopUp;

        }
      })
      .on('showsoftconversion-pw', this.showPopup);

    $radio.on('change', this.onRadioChangeHandler);

    $downloadbtn.on('click', this.downloadHandler);
    $moreBtn.on('click', this.moreBtnHandler);
    $notmebtn.on('click', this.notMeBtnHandler);
    $yesmebtn.on('click', this.yesMeBtnHandler);

    $nextbtn.click(function (e) {
      let isvalid = true;
      const target = $(this).data('target');
      const tab = $(this).closest('.tab-content-steps');
      const input = tab.find('input');
      const errObj = [];

      // hide fields if type of visitor is not customer
      if(target ===`#cf-step-3-${$componentName}` && requestPayload['typeOfVisitor']!==`customer-${$componentName}`){
        $company.hide();
        $position.hide();
        $('.marketing-consent').addClass('no-form-group');
      }

      if(target ===`#cf-step-3-${$componentName}` && requestPayload['typeOfVisitor']===`customer-${$componentName}`){
        $company.show();
        $position.show();
      }

      if ($(this).hasClass('previousbtn')) {
        switch (target) {
        case `#cf-step-1-${$componentName}`:
          changeStepPrev(self.mainHeading, 'Step 2', self.step2heading);
          break;
        case `#cf-step-2-${$componentName}`:
          changeStepPrev(self.mainHeading, 'Step 3', self.step3heading);
          break;
        default:
          break;
        }
      }

      if (!$(this).hasClass('previousbtn') && (input.length > 0 )) {
        $('input', tab).each(function () {
          const fieldName = $(this).attr('name');

          const newSafeValues = $(this).attr('type') !== 'hidden' ? validateFieldsForTags($(this).val()) : $(this).val();
          $('div.' + fieldName).text(newSafeValues);
          if (fieldName in self.cache.requestPayload) {
            requestPayload[fieldName] = newSafeValues;
            
          }
          if (($(this).prop('required') && $(this).val() === '') || (fieldName === `email-${$componentName}`) && !self.validEmail($(this).val())) {
            isvalid = false;
            e.preventDefault();
            e.stopPropagation();
            const errmsg = $(this).closest('.form-group, .formfield').find('.errorMsg').text().trim();
            let erLbl = '';


            switch (fieldName) {
            case `typeOfVisitorTitle-${$componentName}`:
              erLbl = self.step1heading;
              break;
            case `firstName-${$componentName}`:
              erLbl = $(`#cf-step-2-${$componentName} label`)[0].textContent;
              break;
            case `lastName-${$componentName}`:
              erLbl = $(`#cf-step-2-${$componentName} label`)[1].textContent;
              break;
            case `email-${$componentName}`:
              erLbl = $(`#cf-step-2-${$componentName} label`)[2].textContent;
              break;
            default:
              erLbl = fieldName;
              break;
            }

            errObj.push({
              formErrorMessage: errmsg,
              formErrorField: erLbl
            });

            $(this).closest('.form-group, .formfield').addClass('field-error');
          } else {
            $(this).closest('.form-group, .formfield').removeClass('field-error');
          }
        });
      }
      if (isvalid) {
        tab.find('.form-group, .formfield').removeClass('field-error');
        if (target) {
          $(`.tab-pane.tab-${$componentName}`).removeClass('active');
          $(target).addClass('active');
          if (!$(this).hasClass('previousbtn')) {
            switch (target) {
            case `#cf-step-2-${$componentName}`:
              changeStepNext(self.mainHeading, 'Step 1', self.step1heading, { [self.step1heading]: self.cache.requestPayload['typeOfVisitorTitle'] });
              break;
            case `#cf-step-3-${$componentName}`:
              changeStepNext(self.mainHeading, 'Step 2', self.step2heading, { ...self.restObj });
              break;
            default:
              break;
            }
          } 



        }
      }else{
        switch (target) {
        case `#cf-step-2-${$componentName}`:
          changeStepError(self.mainHeading, 'Step 1', self.step1heading, {}, errObj);
          break;
        case `#cf-step-3-${$componentName}`:
          changeStepError(self.mainHeading, 'Step 2', self.step2heading, {}, errObj);
          break;
        default:
          break;
        }
      }
    });

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
        if ($(this).prop('required') && $(this).val() === '' && requestPayload['typeOfVisitor']===`customer-${$componentName}`) {
          isvalid = false;
          const errmsg = $(this).closest('.form-group, .formfield').find('.errorMsg').text().trim();
          let erLbl = '';
          switch (fieldName) {
          case `company-${$componentName}`:
            erLbl = $(`#cf-step-3-${$componentName} label`)[0].textContent;
            break;
          case `position-${$componentName}`:
            erLbl = $(`#cf-step-3-${$componentName} label`)[1].textContent;
            break;
          default:
            erLbl = fieldName;
            break;
          }
          errObj.push({
            formErrorMessage: errmsg,
            formErrorField: erLbl
          });
          $(this).closest('.form-group, .formfield').addClass('field-error');
        }else {
          $(this).closest('.form-group, .formfield').removeClass('field-error');
        }
      });
      if (isvalid) {
        self.submitForm();
      }else if(!isvalid && target ===`#cf-step-downloadReady-${$componentName}`){
        changeStepError(self.mainHeading, 'Step 3', self.step3heading, {}, errObj);
        // case `#cf-step-downloadReady-${$componentName}`:
        //changeStepError(self.mainHeading, 'Step 3', self.step3heading, {}, errObj);
        //break;
      }
    });
  }

  showPopup = () => {
    const $this = this;
    const visitorMail = storageUtil.getCookie('visitor-mail');
    if(visitorMail) {
      makeLoad('welcome back', $this.mainHeading, 'welcome back:formstart');
      $(`#visitor-email-${this.cache.$componentName}`).text(visitorMail).css('font-weight', 900);
      $(`.heading_${this.cache.$componentName}`, this.root).text('');
      $(`.tab-pane.tab-${this.cache.$componentName}`, this.root).removeClass('active');
      $(`#cf-step-welcomeback-${this.cache.$componentName}`, this.root).addClass('active');
      isMobileMode() &&  $(`.pw-sf_body_${this.cache.$componentName}`).css('align-items', 'center');
    }else{
      makeLoad($this.step1heading, $this.mainHeading);
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
    this.step2heading = $(`#cf-step-2-${this.cache.$componentName} .tab-content-steps`).find('h4').text();
    this.step3heading = 'Company information';
    this.restObj = {};
    this.restObj2 = {};
    $(`#cf-step-2-${this.cache.$componentName} label`).each((i, v) => this.restObj[$(v).text()] = 'NA');
    $(`#cf-step-3-${this.cache.$componentName} label`).slice(0, 2).each((i, v) => this.restObj2[$(v).text()] = 'NA');
  }
}

export default Softconversion;
