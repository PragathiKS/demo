import ManagePreferences from './ManagePreferences';
import $ from 'jquery';
import managePreferencesTemplate from '../../../test-templates-hbs/managepreferences.hbs';

describe('ManagePreferences', function () {
  beforeEach(function () {
    $(document.body).empty().html(managePreferencesTemplate());
    this.managePreferences = new ManagePreferences({
      el: document.body
    });
    this.initSpy = sinon.spy(this.managePreferences, 'init');
    this.unsubscribeHandlerSpy = sinon.spy(this.managePreferences, 'unsubscribeHandler');
    this.getCountryListSpy = sinon.spy(this.managePreferences, 'getCountryList');
    this.getLanguageListSpy = sinon.spy(this.managePreferences, 'getLanguageList');
    this.managePreferences.init();
  });
  afterEach(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.unsubscribeHandlerSpy.restore();
    this.getCountryListSpy.restore();
    this.getLanguageListSpy.restore();
  });
  it('should initialize', function () {
    expect(this.managePreferences.init.called).to.be.true;
  });
  it('Should update country payload with dropItem changes', function() {
    document.getElementById('ddtest').click();
    expect(this.managePreferences.cache.requestPayload['countryTitle']).to.equal('Denmark');
    expect(this.managePreferences.cache.requestPayload['country']).to.equal('denmark');
  });
  it('Should update language payload with dropItem changes', function() {
    document.getElementById('language-select').click();
    expect(this.managePreferences.cache.requestPayload['language']).to.equal('english');
  });
  it('should call the functions', function () {
    $('#unsubscribe-all').trigger('change');
    $('#processing','.pw-form-managePreferences').prop('checked', true);
    $('#event-invitation','.pw-form-managePreferences').prop('checked', true);
    $('#save-preferences').trigger('click');
    $('#unsubscribe-button').trigger('click');
  });
  it('should get country list and it should be equal to 2', function () {
    expect(this.managePreferences.getCountryList.called).to.be.true;
    expect(this.managePreferences.cache.countryList.length).to.equal(2);
    $('.country-field-wrapper .dropdown-menu, .country-field-wrapper .dropdown-toggle').keydown();
  });
  it('should get language list and it should be equal to 2', function () {
    expect(this.managePreferences.getLanguageList.called).to.be.true;
    expect(this.managePreferences.cache.languageList.length).to.equal(2);
    $('.language-field-wrapper .dropdown-menu, .language-field-wrapper .dropdown-toggle').keydown();
  });
});
