import $ from 'jquery';
import { REG_EMAIL } from '../../../scripts/utils/constants';

class Softconversion {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$modal = this.root.parent().find('.js-soft-modal');
    this.cache.$nextbtn = this.root.find('.tpatom-btn[type=button]');
    this.cache.$downloadbtn = this.root.find('.thankyouTarget[type=button]');
    this.cache.$radio = this.root.find('input[type=radio][name="typeOfVisitorOptions"]');
    this.cache.$componentName = this.root.find('input[type="hidden"][name="ComponentNameSoft"]').val();
    this.cache.$company = this.root.find(`.company-${this.cache.$componentName}`);
    this.cache.$position = this.root.find(`.position-${this.cache.$componentName}`);
    // this.cache.$submitBtn = $('button[type="submit"]', this.root);
    this.cache.$submitBtn = this.root.find('button[type="submit"]');
    this.cache.requestPayload = {
      'domainURL': window.location.host,
      'typeOfVisitor': '',
      'firstName': '',
      'lastName': '',
      'email': '',
      'company': '',
      'position': '',
      'market-consent': '',
      'typeOfVisitorTitle': ''
    };
  }

  validEmail(email) {
    return REG_EMAIL.test(email);
  }

  onRadioChangeHandler = e => {
    const { requestPayload } = this.cache;
    const value = e.target.value;
    const id = e.target.id;
    $('input[type=hidden][name="typeOfVisitorTitle"]').val(value);
    requestPayload['typeOfVisitor'] = id;
    requestPayload['typeOfVisitorTitle'] = value;
  }

  downloadHandler = () => {
    $('.tab-pane', this.root).removeClass('active');
    $(`#cf-step-thankyou-${this.cache.$componentName}`, this.root).addClass('active');
    // $('.serviceError').removeClass('d-block');
  }

  submitForm = () => {
    // const servletPath = this.cache.contactusapi.data('contactus-api-servlet');
    // ajaxWrapper.getXhrObj({
    //   url: servletPath,
    //   method: ajaxMethods.POST,
    //   data: { 'inputJson': JSON.stringify(this.cache.requestPayload) }
    // }).done(
    //   (response) => {
    //     if (response.statusCode === '200') {
    //       const offsetContact = $('#pw-contactUs').offset();
    $('.pw-softconversion__header__heading', this.root).html('');
    $('.tab-pane', this.root).removeClass('active');
    $(`#cf-step-downloadReady-${this.cache.$componentName}`, this.root).addClass('active');
    $('.serviceError').removeClass('d-block');
    // $('html, body').animate({
    //   scrollTop: offsetContact.top - 50
    // });
    // } else {
    //   $('.serviceError').addClass('d-block');
    // }
  }
  // );
  // }


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
          if (($(this).prop('required') && $(this).val() === '') || (fieldName === 'email') && !self.validEmail($(this).val())) {
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
          $('.tab-pane').removeClass('active');
          $(target).addClass('active');
        }
      }
    });

    $submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();
      let isvalid = true;
      const tab = $(this).closest('.tab-content-steps');
      const honeyPotFieldValue = $('#pardot_extra_field', self.root).val();
      
      $('input', tab).each(function () {
        const fieldName = $(this).attr('name');


        $('div.' + fieldName).text($(this).val());
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
