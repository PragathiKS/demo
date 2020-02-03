import SoftConversionForm from './SoftConversionForm';
import $ from 'jquery';
import softConversionTemplate from '../../../test-templates-hbs/softConversionForm.hbs';
import { storageUtil } from '../../../scripts/common/common';

describe('SoftConversionForm', function () {
  before(function () {
    window.onbeforeunload = () => 'Oh no!';
    $(document.body).empty().html(softConversionTemplate());
    this.softConversion = new SoftConversionForm({ el: document.body });
    this.initSpy = sinon.spy(this.softConversion, 'init');
    this.checkAndSubmitSpy = sinon.spy(this.softConversion, 'checkAndSubmit');
    this.checkStepAndContinueSpy = sinon.spy(this.softConversion, 'checkStepAndContinue');
    this.setFieldsSpy = sinon.spy(this.softConversion, 'setFields');
    this.storageUtilStub = sinon.stub(storageUtil, 'get');
    this.storageUtilStub.returns({});
    this.softConversion.init();

  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.checkAndSubmitSpy.restore();
    this.checkStepAndContinueSpy.restore();
    this.setFieldsSpy.restore();
    this.storageUtilStub.restore();
  });
  it('should initialize', function () {
    expect(this.softConversion.init.called).to.be.true;
  });
  it('should call check and submit on click', function () {
    $('#softConversionModal .form-submit').first().trigger('click');
    expect(this.softConversion.checkAndSubmit.called).to.be.true;
  });
  it('should call check and continue on click', function () {
    $('#softConversionModal .pw-form__nextbtn[data-toggle="tab"]').first().trigger('click');
    expect(this.softConversion.checkStepAndContinue.called).to.be.true;
  });
  it('should set form fields on click', function () {
    $('#softConversionModal input:radio').first().trigger('click');
    expect(this.softConversion.setFields.called).to.be.true;
  });
});
