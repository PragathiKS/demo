import $ from 'jquery';
import 'bootstrap';
import keyDownSearch from '../../../scripts/utils/searchDropDown';

class ManagePreferences {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    // change to submit type
    this.cache.$submitBtn = $('form.pw-form-managePreferences .save-preference-button button[type="button"]', this.root);
    // change to submit type
    this.cache.$unsubscribeBtn = $('form.pw-form-managePreferences .unsubscribe-button button[type="button"]', this.root);
    this.cache.$unsubscribeCheckbox = $('#unsubscribe-all', this.root);
    this.cache.$inputFieldWrapper = $('.input-field-wrapper',this.root);
    this.cache.$dropdownToggle = $('.dropdown-toggle',this.root);
    this.cache.$unsubscribeBox = $('.unsubscribe-box',this.root);
    this.cache.$pressMediaCheckBox = $('#pres-and-media-communication',this.root);
    this.cache.$selectedFormData = $('form.pw-form-managePreferences', this.root);
    this.cache.$savePreferenceButton = $('.save-preference-button',this.root);
    this.cache.$unsubscribeButton = $('.unsubscribe-button',this.root);
    this.cache.communicationTypes = this.root.find('.communication-type');
    this.cache.interestType = this.root.find('.interest-type');
    this.cache.$dropItem = $('.country-field-wrapper .pw-form__dropdown a.dropdown-item', this.root);
    this.cache.$languageDropItem = $('.language-field-wrapper .pw-form__dropdown a.dropdown-item', this.root);
    this.cache.requestPayload = {
      'types-communication':[],
      'area-of-interest':[],
      'country':'',
      'countryTitle':'',
      'language':''
    };
    this.cache.$languageDropdownButton = $('.language-field-wrapper .dropdown-menu, .language-field-wrapper .dropdown-toggle', this.root);
    this.cache.$countryDropdownButton = $('.country-field-wrapper .dropdown-menu, .country-field-wrapper .dropdown-toggle', this.root);
    this.cache.countryList = [];
    this.cache.languageList = [];
  }

  /**
   * Select area of interest
  */
  selectAreaOfInterestHandler = () => {
    const {requestPayload,interestType} = this.cache;
    requestPayload['area-of-interest'] = [];
    interestType.each(function(){
      if($(this).is(':checked')){
        requestPayload['area-of-interest'].push($(this).val());
      }
    });
  }

  /**
   * Select type of communication
  */
  selectCommunicationHandler = () => {
    const {requestPayload,communicationTypes} = this.cache;
    requestPayload['types-communication'] = [];
    communicationTypes.each(function(){
      if($(this).is(':checked')){
        requestPayload['types-communication'].push($(this).val());
      }
    });
  }

  /**
   * check form values
   * @param {domObject} inputGroup input
  */
  checkValues = (inputGroup) => {
    const { requestPayload, $pressMediaCheckBox } = this.cache;
    if(inputGroup.attr('name') === 'communication-type-input' && requestPayload['types-communication'].length > 0){
      inputGroup.data('input-value',true);
    }
    if(!$pressMediaCheckBox.is(':checked') || ($pressMediaCheckBox.is(':checked') && inputGroup.attr('name') === 'area-of-interest' && requestPayload['area-of-interest'].length > 0)){
      inputGroup.data('input-value',true);
    }
  }

  /**
   * set default checkbox values
  */
  setDefaultCheckboxValue = () => {
    const { $selectedFormData, $pressMediaCheckBox } = this.cache;
    let selectedCommunication = $selectedFormData.data('selected-communication');
    selectedCommunication = selectedCommunication && selectedCommunication.split(',').map((val) => val.trim()) || [];
    let selectedInterest = $selectedFormData.data('selected-interest');
    selectedInterest = selectedInterest && selectedInterest.split(',').map((val) => val.trim()) || [];
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
    if(!$pressMediaCheckBox.is(':checked')){
      this.setAreaDisabled();
    }
  }

  /**
   * function to handle unsubscribe button
   * @param {domObject} params params
  */
  unsubscribeHandler = (params) => {
    const { $inputFieldWrapper,$dropdownToggle,$unsubscribeBox,$savePreferenceButton,$unsubscribeButton } = this.cache;
    $('.form-group, .formfield',this.root).removeClass('field-error');
    if(params.prop('checked')){
      $('form.pw-form-managePreferences input[type="checkbox"]', this.root).not('#consentcheckbox,#unsubscribe-all').each(function(){
        const $this = $(this);
        $this.addClass('visible-false');
        $this.prop({checked:false});
        $this.attr('disabled',true);
        $this.closest('label').find('.tpatom-checkbox__icon').addClass('disable-checkbox');
      });
      $inputFieldWrapper.addClass('disable-input');
      $dropdownToggle.addClass('disabled button-disabled');
      $unsubscribeBox.addClass('show');
      $savePreferenceButton.addClass('hide');
      $unsubscribeButton.addClass('show');
    } else {
      $('form.pw-form-managePreferences input[type="checkbox"]', this.root).not('#consentcheckbox,#unsubscribe-all').each(function(){
        const $this = $(this);
        $this.removeAttr('disabled');
        $this.closest('label').find('.tpatom-checkbox__icon').removeClass('disable-checkbox');
      });
      $inputFieldWrapper.removeClass('disable-input');
      $dropdownToggle.removeClass('disabled button-disabled');
      $unsubscribeBox.removeClass('show');
      $savePreferenceButton.removeClass('hide');
      $unsubscribeButton.removeClass('show');
      this.setAreaDisabled();
    }
  }

  /**
   * function to handle press and media checkbox
   * @param {domObject} params params
  */
  pressMediaHandler = (params) => {
    this.root.find('div[name="area-of-interest"].formfield').removeClass('field-error');
    if(params.prop('checked')){
      $('div[name="area-of-interest"] input[type="checkbox"]', this.root).each(function(){
        const $this = $(this);
        $this.removeClass('visible-false');
        $this.prop({checked:false});
        $this.attr('disabled',false);
        $this.closest('label').find('.tpatom-checkbox__icon').removeClass('disable-checkbox');
      });
      this.root.find('div[name="area-of-interest"] .input-field-wrapper').removeClass('disable-input');
    } else {
      this.setAreaDisabled();
    }
  }

  /**
   * function to disable area of interest
  */
  setAreaDisabled = () => {
    this.root.find('div[name="area-of-interest"].formfield').removeClass('field-error');
    $('div[name="area-of-interest"] input[type="checkbox"]', this.root).each(function(){
      const $this = $(this);
      $this.addClass('visible-false');
      $this.prop({checked:false});
      $this.attr('disabled',true);
      $this.closest('label').find('.tpatom-checkbox__icon').addClass('disable-checkbox');
    });
    this.root.find('div[name="area-of-interest"] .input-field-wrapper').addClass('disable-input');
  }

  /**
   * key down search in case of country field
   * @param {object} event event
   * @param {Array} options options
   * @param {domObject} button button
  */
  onKeydown = (event, options, button) => {
    if (button.hasClass('show')) {
      keyDownSearch.call(this, event, options);
    }
  };

  /**
  * function to enable auto suggest for country
  */
  getCountryList() {
    const { $dropItem,$countryDropdownButton } = this.cache;
    const self = this;
    $dropItem.map(function () {
      const datael = $(this)[0];
      self.cache.countryList.push($(datael).data('countrytitle'));
    });
    $countryDropdownButton.keydown(e => this.onKeydown(e, this.cache.countryList,$countryDropdownButton));
  }

  /**
  * function to enable auto suggest for language
  */
  getLanguageList() {
    const { $languageDropItem,$languageDropdownButton } = this.cache;
    const self = this;
    $languageDropItem.map(function () {
      const datael = $(this)[0];
      self.cache.languageList.push($(datael).data('languagetitle'));
    });
    $languageDropdownButton.keydown(e => this.onKeydown(e, this.cache.languageList,$languageDropdownButton));
  }

  bindEvents() {
    const { requestPayload, $submitBtn, $dropItem, $languageDropItem,$unsubscribeBtn, $unsubscribeCheckbox, $pressMediaCheckBox } = this.cache;
    const self = this;
    this.setDefaultCheckboxValue();
    $unsubscribeCheckbox.change(function(e){
      e.preventDefault();
      e.stopPropagation();
      self.unsubscribeHandler($(this));
    });
    $pressMediaCheckBox.change(function(e){
      e.stopPropagation();
      self.pressMediaHandler($(this));
    });
    $submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();
      self.selectCommunicationHandler();
      self.selectAreaOfInterestHandler();
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

      return isvalid;
    });

    $unsubscribeBtn.click(function(e){
      e.preventDefault();
      e.stopPropagation();
      let isvalid = true;
      const tab = $(this).closest('.tab-content-steps');
      $('.consent-checkbox', tab).each(function () {
        if ($('input[name="consent"]:checked').length === 0) {
          isvalid = false;
          e.preventDefault();
          e.stopPropagation();
          $(this).closest('.form-group, .formfield').addClass('field-error');
        } else {
          $(this).closest('.form-group, .formfield').removeClass('field-error');
        }
      });
      return isvalid;
    });

    $dropItem.click(function (e) {
      e.preventDefault();
      const countryTitle = $(this).data('countrytitle');
      const country = $(this).data('country');
      const parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle span', parentDrop).text(countryTitle);
      requestPayload['country'] = country;
      requestPayload['countryTitle'] = countryTitle;
    });

    $languageDropItem.click(function (e) {
      e.preventDefault();
      const languageTitle = $(this).data('languagetitle');
      const languageKey = $(this).data('languagekey');
      const parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle span', parentDrop).text(languageTitle);
      requestPayload['language'] = languageKey;
    });

  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.getCountryList();
    this.getLanguageList();
  }
}

export default ManagePreferences;
