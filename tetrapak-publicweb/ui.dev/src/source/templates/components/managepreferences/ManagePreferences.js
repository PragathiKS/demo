import $ from 'jquery';
import 'bootstrap';

class ManagePreferences {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$submitBtn = $('form.pw-form-managePreferences button[type="submit"]', this.root);
    this.cache.$unsubscribeBtn = $('#unsubscribe-all', this.root);
    this.cache.communicationTypes = this.root.find('.communication-type');
    this.cache.$dropItem = $('.country-field-wrapper .pw-form__dropdown a.dropdown-item', this.root);
    this.cache.$languageDropItem = $('.language-field-wrapper .pw-form__dropdown a.dropdown-item', this.root);
    this.cache.requestPayload = {
      'types-communication':[]
    };
  }

  selectCommunicationHandler = () => {
    const {requestPayload,communicationTypes} = this.cache;
    requestPayload['types-communication'] = [];
    communicationTypes.each(function(){
      if($(this).is(':checked')){
        requestPayload['types-communication'].push($(this).val());
      }
    });
  }

  checkValues = (inputGroup) => {
    const { requestPayload } = this.cache;
    if(inputGroup.attr('name') === 'communication-type' && requestPayload['types-communication'].length > 0){
      inputGroup.data('input-value',true);
    }
    if(inputGroup.attr('name') === 'consent-checkbox' && $('input[name="consent"]:checked').length > 0){
      inputGroup.data('input-value',true);
      requestPayload['marketingConsent'] = $('input[name="consent"]:checked').length > 0;
    }
  }

  setDefaultCheckboxValue = () => {
    const selectedFormData = $('form.pw-form-managePreferences', this.root);
    let selectedCommunication = selectedFormData.data('selected-communication');
    selectedCommunication = selectedCommunication && selectedCommunication.split(',').map((val) => val.trim());
    let selectedInterest = selectedFormData.data('selected-interest');
    selectedInterest = selectedInterest && selectedInterest.split(',').map((val) => val.trim());
    $('.communication-type',this.root).each(function(){
      const $this = $(this);
      if(selectedCommunication.indexOf($this.val()) !== -1) {
        $this.prop('checked', true);
      }
    });

    $('.interest-type',this.root).each(function(){
      const $this = $(this);
      if(selectedInterest.indexOf($this.val()) !== -1) {
        $this.prop('checked', true);
      }
    });


  }

  unsubscribeHandler = (params) => {
    const self = this;
    if(params.prop('checked')){
      $('form.pw-form-managePreferences input[type="checkbox"]', this.root).not('#consentcheckbox,#unsubscribe-all').each(function(){
        const $this = $(this);
        $this.addClass('visible-false');
        $this.prop({checked:false});
        $this.attr('disabled',true);
        $this.closest('label').find('.tpatom-checkbox__icon').addClass('disable-checkbox');
      });
      $('.input-field-wrapper',this.root).addClass('disable-input');
      $('.dropdown-toggle',self.root).addClass('disabled button-disabled');
    } else {
      $('form.pw-form-managePreferences input[type="checkbox"]', this.root).not('#consentcheckbox,#unsubscribe-all').each(function(){
        const $this = $(this);
        $this.removeAttr('disabled');
        $this.closest('label').find('.tpatom-checkbox__icon').removeClass('disable-checkbox');
      });
      $('.input-field-wrapper',this.root).removeClass('disable-input');
      $('.dropdown-toggle',self.root).removeClass('disabled button-disabled');
    }
  }

  bindEvents() {
    const { requestPayload, $submitBtn, $dropItem, $languageDropItem,$unsubscribeBtn } = this.cache;
    const self = this;
    this.setDefaultCheckboxValue();
    $unsubscribeBtn.change(function(e){
      e.preventDefault();
      e.stopPropagation();
      self.unsubscribeHandler($(this));
    });
    $submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();
      self.selectCommunicationHandler();
      let isvalid = true;
      const tab = $(this).closest('.tab-content-steps');

      $('.input-group', tab).each(function () {
        $(this).data('input-value',false);
        self.checkValues($(this));
        if (!$(this).data('input-value')) {
          isvalid = false;
          e.preventDefault();
          e.stopPropagation();
          $(this).closest('.form-group, .formfield').addClass('field-error');
        } else {
          $(this).closest('.form-group, .formfield').removeClass('field-error');
        }
      });

      if (isvalid) {
        // submit form
      }
    });

    $dropItem.click(function (e) {
      e.preventDefault();
      const countryTitle = $(this).data('countrytitle');
      const parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle span', parentDrop).text(countryTitle);
      requestPayload['country'] = countryTitle;
      // self.restObj[self.cache.$countryField.data('country-name-label')] = requestPayload['country'];
      requestPayload['countryTitle'] = countryTitle;
    });

    $languageDropItem.click(function (e) {
      e.preventDefault();
      const languageTitle = $(this).data('languagetitle');
      const languageKey = $(this).data('languagekey');
      const parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle span', parentDrop).text(languageTitle);
      requestPayload['language'] = languageKey;
      // self.restObj2[self.cache.$positionField.data('position-name-label')] = positionTitle;
    });

  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default ManagePreferences;
