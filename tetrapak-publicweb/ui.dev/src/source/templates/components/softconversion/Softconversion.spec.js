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
    this.softconversion.root.modal = ()=>{};
    this.softconversion.init();
  });

  afterEach(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.submitFormSpy.restore();
    this.hidePopUpSpy.restore();
  });

  it('should initialize', function () {
    expect(this.softconversion.init.called).to.be.true;
  });

  it('should not submit Form when required fields are empty', function (done) {
    $('#firstName-textimage').val('mockname');
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
    document.getElementById('market-consent').checked = true;
    
    this.softconversion.cache.$submitBtn.click();
    expect(this.softconversion.submitForm.called).to.be.true;
    done();
  });

  it('Should not update request payload on step-1 next button click', function () {
    document.getElementById('typeOfVisitor').value = 'Customer';
    document.getElementById('step1btn').click();
    expect(this.softconversion.cache.requestPayload['typeOfVisitorTitle']).to.not.equal('Customer');
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


});
