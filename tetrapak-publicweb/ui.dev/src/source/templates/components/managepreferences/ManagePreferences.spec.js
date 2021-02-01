import ManagePreferences from './ManagePreferences';
import $ from 'jquery';
import managePreferencesTemplate from '../../../test-templates-hbs/managePreferences.hbs';

describe('ManagePreferences', function () {
  beforeEach(function () {
    $(document.body).empty().html(managePreferencesTemplate());
    this.managePreferences = new ManagePreferences({
      el: document.body
    });
    this.initSpy = sinon.spy(this.managePreferences, 'init');
    this.unsubscribeHandlerSpy = sinon.spy(this.managePreferences, 'unsubscribeHandler');
    this.managePreferences.init();
  });
  afterEach(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.unsubscribeHandlerSpy.restore();
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
  it('Should call unsubscribe button change', function () {
    $('#unsubscribe-all').trigger('change');
  });
});
