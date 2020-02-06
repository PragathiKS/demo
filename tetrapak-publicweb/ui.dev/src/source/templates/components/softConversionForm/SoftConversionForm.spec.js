import SoftConversionForm from './SoftConversionForm';
import $ from 'jquery';
import softConversionTemplate from '../../../test-templates-hbs/softConversionForm.hbs';
import { storageUtil } from '../../../scripts/common/common';
import { ajaxWrapper } from '../../../scripts/utils/ajax';

describe('SoftConversionForm', function () {
  const jqRef = {};
  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, 'success', jqRef);
    return pr.promise();
  }
  before(function () {
    window.digitalData = {
      formInfo: {}
    };
    window._satellite = {
      track() { /* Dummy method */ }
    };
    $(document.body).empty().html(softConversionTemplate());
    this.softConversion = new SoftConversionForm({ el: document.body });
    this.initSpy = sinon.spy(this.softConversion, 'init');
    this.checkAndSubmitSpy = sinon.spy(this.softConversion, 'checkAndSubmit');
    this.checkStepAndContinueSpy = sinon.spy(this.softConversion, 'checkStepAndContinue');
    this.setFieldsSpy = sinon.spy(this.softConversion, 'setFields');
    this.prevSpy = sinon.spy(this.softConversion, 'prevStep');
    this.storageUtilStub = sinon.stub(storageUtil, 'get');
    this.storageUtilStub.returns([
      {
        name: 'group',
        value: 'group'
      },
      {
        name: 'non-group',
        value: 'non-group'
      }
    ]);
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.returns(ajaxResponse({}));
    this.softConversion.init();
    $('#softConversionModal')
      .trigger('show.bs.modal')
      .trigger('hidden.bs.modal');
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.checkAndSubmitSpy.restore();
    this.checkStepAndContinueSpy.restore();
    this.setFieldsSpy.restore();
    this.prevSpy.restore();
    this.storageUtilStub.restore();
    this.ajaxStub.restore();
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
  it('should set to previous tab on click of previous button', function () {
    $('#softConversionModal .pw-form__prevbtn__scf[data-toggle="tab"]').trigger('click');
    expect(this.prevSpy.called).to.be.true;
  });
});
