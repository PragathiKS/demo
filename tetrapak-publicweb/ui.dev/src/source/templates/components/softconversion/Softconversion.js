import $ from 'jquery';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { REG_EMAIL,ajaxMethods } from '../../../scripts/utils/constants';
import { validateFieldsForTags } from '../../../scripts/common/common';

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
    this.cache.$company = this.root.find(`.company-${this.cache.$componentName}`);
    this.cache.$position = this.root.find(`.position-${this.cache.$componentName}`);
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

    window.open(downloadLink, '_blank');
  }

  submitForm = () => {
    const servletPath = this.cache.softconversionapi.data('softconversion-api-url');
    const pardotUrl = this.cache.softconversionapi.data('softconversion-padrot-url');
    
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
    apiPayload.padrotUrl = pardotUrl;
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
    $('.pw-softconversion__header__heading', this.root).html('');
    $(`.tab-pane.tab-${this.cache.$componentName}`, this.root).removeClass('active');
    $(`#cf-step-downloadReady-${this.cache.$componentName}`, this.root).addClass('active');
  }


  bindEvents() {
    const {requestPayload, $radio, $nextbtn, $submitBtn, $componentName, $company, $position, $downloadbtn } = this.cache;
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

    $nextbtn.click(function (e) {
      let isvalid = true;
      const target = $(this).data('target');
      const tab = $(this).closest('.tab-content-steps');
      const input = tab.find('input');

      // hide fields if type of visitor is not customer
      if(target ===`#cf-step-3-${$componentName}` && requestPayload['typeOfVisitor']!==`customer-${$componentName}`){
        $company.hide();
        $position.hide();
      }

      if(target ===`#cf-step-3-${$componentName}` && requestPayload['typeOfVisitor']===`customer-${$componentName}`){
        $company.show();
        $position.show();
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
        }
      }
    });

    $submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();
      let isvalid = true;
      const tab = $(this).closest('.tab-content-steps');
      const honeyPotFieldValue = $(`#pardot_extra_field_${$componentName}`, self.root).val();
      
      $('input', tab).each(function () {
        const fieldName = $(this).attr('name');

        const newSafeValues = $(this).attr('type') !== 'hidden' ? validateFieldsForTags($(this).val()) : $(this).val();

        if (fieldName in self.cache.requestPayload) {
          requestPayload[fieldName] = newSafeValues;

       
        }
        if ($(this).prop('required') && $(this).val() === '' && requestPayload['typeOfVisitorTitle']==='Customer') {
          isvalid = false;
          $(this).closest('.form-group, .formfield').addClass('field-error');
        }else if(fieldName ==='market-consent' && !$(this).is(':checked')){
          isvalid = false;
          $(this).closest('.form-group, .formfield').addClass('field-error');
        }else {
          $(this).closest('.form-group, .formfield').removeClass('field-error');
        }
      });
      if (isvalid && !honeyPotFieldValue) {
        self.submitForm();
      }
    });
  }

  showPopup = () => {
    const $this = this;
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
  }
}

export default Softconversion;