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
    this.downloadHandlerSpy = sinon.spy(this.softconversion, 'downloadHandler');
    this.moreBtnHandlerSpy = sinon.spy(this.softconversion, 'moreBtnHandler');
    this.notMeBtnHandlerSpy = sinon.spy(this.softconversion, 'notMeBtnHandler');
    this.yesMeBtnHandlerSpy = sinon.spy(this.softconversion, 'yesMeBtnHandler');
    this.onRadioChangeHandlerSpy = sinon.spy(this.softconversion, 'onRadioChangeHandler');

    this.softconversion.root.modal = ()=>{};
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
  });

  it('should initialize', function (done) {
    expect(this.softconversion.init.called).to.be.true;
    done();
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
    document.getElementById('position-textimage').value = 'mockmessage';
    document.getElementById('typeOfVisitor').value = 'Customer';

    this.softconversion.cache.$submitBtn.click();
    expect(this.softconversion.submitForm.called).to.be.true;
    done();
  });

  it('Should not update request payload on step-1 next button click', function (done) {
    document.getElementById('typeOfVisitor').value = 'Customer';
    document.getElementById('step1btn').click();
    expect(this.softconversion.cache.requestPayload['typeOfVisitorTitle']).to.not.equal('Customer');
    done();
  });

  it('should update request payload when step-2 next button is clicked', function (done) {
    document.getElementById('firstName-textimage').value = 'first';
    document.getElementById('lastName-textimage').value = 'last';
    document.getElementById('email-textimage').value = 'email';
    document.getElementById('step2btn').click();
    expect(this.softconversion.cache.requestPayload['firstName-textimage']).to.equal('first');
    expect(this.softconversion.cache.requestPayload['lastName-textimage']).to.equal('last');
    expect(this.softconversion.cache.requestPayload['email-textimage']).to.equal('email');
    done();
  });

  it('should close modal on close icon click', function (done) {
    $('.js-close-btn').click();
    expect(this.softconversion.hidePopUp.called).to.be.true;
    done();
  });

  it('should download on download button click', function (done) {
    $('.thankyouTarget').click();
    expect(this.softconversion.downloadHandler.called).to.be.false;
    done();
  });

  it('should open more white paper on more whitepaper button click', function (done) {
    $('.moreButton-textimage').click();
    expect(this.softconversion.moreBtnHandler.called).to.be.false;
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



});
