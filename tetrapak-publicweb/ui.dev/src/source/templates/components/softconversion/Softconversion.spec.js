import $ from 'jquery';
import Softconversion from './Softconversion';
import softConversionTemplate from '../../../test-templates-hbs/softConversion.hbs';

describe('Softconversion', function () {
  beforeEach(function () {
    $(document.body).empty().html(softConversionTemplate());
    this.softconversion = new Softconversion({el: document.body});
    this.initSpy = sinon.spy(this.softconversion, 'init');
    this.submitFormSpy = sinon.spy(this.softconversion, 'submitForm');
    this.hidePopUpSpy = sinon.spy(this.softconversion, 'hidePopUp');
    this.showPopupSpy = sinon.spy(this.softconversion, 'showPopup');
    this.downloadHandlerSpy = sinon.spy(this.softconversion, 'downloadHandler');
    this.moreBtnHandlerSpy = sinon.spy(this.softconversion, 'moreBtnHandler');
    this.notMeBtnHandlerSpy = sinon.spy(this.softconversion, 'notMeBtnHandler');
    this.yesMeBtnHandlerSpy = sinon.spy(this.softconversion, 'yesMeBtnHandler');
    this.onRadioChangeHandlerSpy = sinon.spy(this.softconversion, 'onRadioChangeHandler');
    this.getCountryListSpy = sinon.spy(this.softconversion, 'getCountryList');
    this.getPositionListSpy = sinon.spy(this.softconversion, 'getPositionList');
    this.getFunctionListSpy = sinon.spy(this.softconversion, 'getFunctionList');

    this.softconversion.root.modal = ()=>{};
    this.openStub = sinon.stub(window, 'open');
    this.softconversion.init();
  });

  afterEach(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.submitFormSpy.restore();
    this.hidePopUpSpy.restore();
    this.downloadHandlerSpy.restore();
    this.moreBtnHandlerSpy.restore();
    this.notMeBtnHandlerSpy.restore();
    this.yesMeBtnHandlerSpy.restore();
    this.onRadioChangeHandlerSpy.restore();
    this.openStub.restore();
    this.showPopupSpy.restore();
    this.getCountryListSpy.restore();
    this.getPositionListSpy.restore();
    this.getFunctionListSpy.restore();
  });

  it('should initialize', function (done) {
    expect(this.softconversion.init.called).to.be.true;
    done();
  });

  it('should get country list and it should be equal to 2', function () {
    expect(this.softconversion.getCountryList.called).to.be.true;
    expect(this.softconversion.cache.countryList.length).to.equal(2);
    $('.country-dropdown, .country-dropdown-select').keydown();
  });

  it('should get position list and it should be equal to 2', function () {
    expect(this.softconversion.getPositionList.called).to.be.true;
    expect(this.softconversion.cache.positionList.length).to.equal(2);
    $('.position-dropdown, .position-dropdown-select').keydown();
  });

  it('should get function list and it should be equal to 2', function () {
    expect(this.softconversion.getFunctionList.called).to.be.true;
    expect(this.softconversion.cache.functionList.length).to.equal(2);
    $('.function-dropdown, .function-dropdown-select').keydown();
  });

  it('Should update payload with country dropItem changes', function() {
    document.getElementById('ddtest').click();
    expect(this.softconversion.cache.requestPayload['countryTitle']).to.equal('Afghanistan');
    expect(this.softconversion.cache.requestPayload['country']).to.equal('Afghanistan');
  });
  
  it('Should update payload with position dropItem changes', function() {
    document.getElementById('ddtest1').click();
    expect(this.softconversion.cache.requestPayload['position']).to.equal('C-suite');
  });

  it('Should update payload with function dropItem changes', function() {
    document.getElementById('ddtest2').click();
    expect(this.softconversion.cache.requestPayload['function']).to.equal('Administrative');
  });

  it('should not submit Form when required fields are empty', function (done) {
    $('#firstName-textimage').val('mockname');
    this.softconversion.cache.requestPayload.typeOfVisitor = 'customer-textimage';
    this.softconversion.cache.$submitBtn.click();
    expect(this.softconversion.submitForm.called).to.be.false;
    done();
  });

  it('should submit Form when required fields are not empty', function (done) {
    document.getElementById('firstName-textimage').value = 'first';
    document.getElementById('lastName-textimage').value = 'last';
    document.getElementById('email-textimage').value = 'email@em.com';
    document.getElementById('company-textimage').value = 'mockmessage';
    document.getElementById('country').value = 'Afghanistan';
    document.getElementById('position').value = 'VP';
    document.getElementById('function').value = 'Administrative';
    document.getElementById('typeOfVisitor').value = 'Customer';
    this.softconversion.cache.$submitBtn.click();
    expect(this.softconversion.submitForm.called).to.be.true;
    done();
  });

  it('should not update request payload on submit button click', function (done) {
    document.getElementById('firstName-textimage').value = 'first';
    document.getElementById('lastName-textimage').value = 'last';
    document.getElementById('email-textimage').value = 'email';
    document.getElementById('company-textimage').value = 'company';
    document.getElementById('typeOfVisitor').value = 'Customer';
    this.softconversion.cache.$submitBtn.click();
    expect(this.softconversion.cache.requestPayload['firstName-textimage']).to.equal('first');
    expect(this.softconversion.cache.requestPayload['lastName-textimage']).to.equal('last');
    expect(this.softconversion.cache.requestPayload['email-textimage']).to.equal('email');
    expect(this.softconversion.cache.requestPayload['company-textimage']).to.equal('company');
    expect(this.softconversion.cache.requestPayload['typeOfVisitorTitle']).to.not.equal('Customer');
    done();
  });
  
  it('should update request payload for UTM if avaialble in URL params', function (done) {
    const paramsURL = 'http://www.test.com/?utm_campaign=val1&utm_content=val2&utm_medium=val3&utm_source=val4'
    const params = {};
    paramsURL.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(_, key, value) {
      return params[key] = value;
    });
    Object.keys(params).forEach(key => {
      if(key === 'utm_campaign') {
        this.softconversion.cache.requestPayload[key] = params[key];
      } else if(key === 'utm_content') {
        this.softconversion.cache.requestPayload[key] = params[key];
      } else if(key === 'utm_medium') {
        this.softconversion.cache.requestPayload[key] = params[key];
      } else if(key === 'utm_source') {
        this.softconversion.cache.requestPayload[key] = params[key];
      }
    });
    this.softconversion.cache.$submitBtn.click();
    Object.keys(params).forEach(key => {
      if(key === 'utm_campaign') {
        expect(this.softconversion.cache.requestPayload[key]).to.equal(params[key]);
      } else if(key === 'utm_content') {
        expect(this.softconversion.cache.requestPayload[key]).to.equal(params[key]);
      } else if(key === 'utm_medium') {
        expect(this.softconversion.cache.requestPayload[key]).to.equal(params[key]);
      } else if(key === 'utm_source') {
        expect(this.softconversion.cache.requestPayload[key]).to.equal(params[key]);
      }
    });
    done();
  });

  it('should close modal on close icon click', function (done) {
    $('.js-close-btn').click();
    expect(this.softconversion.hidePopUp.called).to.be.true;
    done();
  });

  it('should download on download button click', function (done) {
    $('.thankyouTarget').click();
    expect(this.softconversion.downloadHandler.called).to.be.true;
    done();
  });

  it('should open more white paper on more whitepaper button click', function (done) {
    $('.moreButton-textimage').click();
    expect(this.softconversion.moreBtnHandler.called).to.be.true;
    done();
  });

  it('should start from beginging when not me button click', function (done) {
    $('.notmebtn-textimage[type=button]').click();
    expect(this.softconversion.notMeBtnHandler.called).to.be.true;
    done();
  });

  it('should show download flow yes me button click', function (done) {
    $('.yesmebtn-textimage[type=button]').click();
    expect(this.softconversion.yesMeBtnHandler.called).to.be.true;
    done();
  });

  it('should call RadioHandler on change of radio button', function (done) {
    $('input[type=radio][name="typeOfVisitorOptions"]').change();
    expect(this.softconversion.onRadioChangeHandler.called).to.be.true;
    done();
  });

  it('should call showPopup', function (done) {
    $(document.body).trigger('showsoftconversion-pw');
    expect(this.softconversion.showPopup.called).to.be.true;
    done();
  });
});