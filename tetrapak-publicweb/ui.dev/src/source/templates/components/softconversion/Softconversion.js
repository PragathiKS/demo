import $ from 'jquery';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import {ajaxMethods, REG_EMAIL } from '../../../scripts/utils/constants';

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
    // const firstNamekey = `firstName-${this.cache.$componentName}`;
    // const lastNamekey = `lastName-${this.cache.$componentName}`;
    // const emailkey = `email-${this.cache.$componentName}`;
    // const companykey = `company-${this.cache.$componentName}`;
    // const positionkey = `position-${this.cache.$componentName}`;
    // const siteLanguagekey = `site_language_${this.cache.$componentName}`;
    // const siteCountrykey = `site_country_${this.cache.$componentName}`;
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

    // = {
    //   'typeOfVisitor': '',
    //   firstNamekey: '',
    //   lastNamekey: '',
    //   emailkey: '',
    //   companykey: '',
    //   positionkey: '',
    //   siteLanguagekey:'',
    //   siteCountrykey:'',
    //   'market-consent': '',
    //   'typeOfVisitorTitle': ''
    // };
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
    /* eslint-disable no-console */
    console.log(downloadLink);


    $(`.tab-pane.tab-${this.cache.$componentName}`, this.root).removeClass('active');
    $(`#cf-step-thankyou-${this.cache.$componentName}`, this.root).addClass('active');

    window.open(downloadLink, '_blank');
    // $('.serviceError').removeClass('d-block');
  }

  submitForm = () => {
    const servletPath = this.cache.softconversionapi.data('softconversion-api-url');
    //const siteLanguage = this.root.find(`#site_language_${this.cache.$componentName}`).val();
    //const siteCountry = this.root.find(`#site_country_${this.cache.$componentName}`).val();
    const apiPayload =  {};

    // eslint-disable-next-line no-console
    console.log(this.cache.requestPayload);
    apiPayload.visitorType = this.cache.requestPayload['typeOfVisitorTitle'];
    apiPayload.firstName = this.cache.requestPayload[`firstName-${this.cache.$componentName}`];
    apiPayload.lastName = this.cache.requestPayload[`lastName-${this.cache.$componentName}`];
    apiPayload.email = this.cache.requestPayload[`email-${this.cache.$componentName}`];
    apiPayload.company = this.cache.requestPayload[`company-${this.cache.$componentName}`];
    apiPayload.position = this.cache.requestPayload[`position-${this.cache.$componentName}`];
    apiPayload.language = this.cache.requestPayload[`site_language_${this.cache.$componentName}`];
    apiPayload.site = this.cache.requestPayload[`site_country_${this.cache.$componentName}`];
    apiPayload.pardot_extra_field = this.cache.requestPayload[`pardot_extra_field_${this.cache.$componentName}`];
    apiPayload.marketingConsent = true;

    // eslint-disable-next-line no-console
    console.log('api payload:',apiPayload);
    ajaxWrapper.getXhrObj({
      url: servletPath,
      method: ajaxMethods.POST,
      headers: {
        'Access-Control-Allow-Origin':'*'
      },
      data: apiPayload
    }).done(
      (response) => {
        if (response.statusCode === '200') {
        // eslint-disable-next-line no-console
          console.log('response status code:',response.statusCode);
          // const offsetContact = $('#pw-contactUs').offset();
          $('.pw-softconversion__header__heading', this.root).html('');
          $(`.tab-pane.tab-${this.cache.$componentName}`, this.root).removeClass('active');
          $(`#cf-step-downloadReady-${this.cache.$componentName}`, this.root).addClass('active');
          $('.serviceError').removeClass('d-block');
        // $('html, body').animate({
        //   scrollTop: offsetContact.top - 50
        // });
        } else {
          $('.serviceError').addClass('d-block');
        }
      }
    );
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
      // const textarea = tab.find('textarea');

      // hide fields if type of visitor is not customer
      if(target ===`#cf-step-3-${$componentName}` && requestPayload['typeOfVisitorTitle']!=='Customer'){
        $company.hide();
        $position.hide();
      }

      if(target ===`#cf-step-3-${$componentName}` && requestPayload['typeOfVisitorTitle']==='Customer'){
        $company.show();
        $position.show();
      }


      if (!$(this).hasClass('previousbtn') && (input.length > 0 )) {
        $('input', tab).each(function () {
          const fieldName = $(this).attr('name');
          $('div.' + fieldName).text($(this).val());
          if (fieldName in self.cache.requestPayload) {
            requestPayload[fieldName] = $(this).val();
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


        // $('div.' + fieldName).text($(this).val());
        if (fieldName in self.cache.requestPayload) {
          requestPayload[fieldName] = $(this).val();

       
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
    // location.reload();
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Softconversion;
