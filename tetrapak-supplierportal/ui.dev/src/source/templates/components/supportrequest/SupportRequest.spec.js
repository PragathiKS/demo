import $ from 'jquery';
import SupportRequest from './SupportRequest';
import supportRequestTemplate from '../../../test-templates-hbs/supportRequest.hbs';

describe('SupportRequest', function () {
  const jqRef = {
    setRequestHeader() {
      // Dummy method
    }
  };
  function setDom($this) {
    $(document.body).empty().html($this.domHtml);
    $this.supportRequest = new SupportRequest({
      el: document.body
    });
  }
  before(function () {
    this.domHtml = supportRequestTemplate();
    setDom(this);
    this.initSpy = sinon.spy(this.supportRequest, 'init');
    this.renderLayoutSpy = sinon.spy(this.supportRequest, 'renderLayout');
    this.addInputTypeFileSpy = sinon.spy(this.supportRequest, 'addInputTypeFile');
    this.dragAndDropPreventDefaultSpy = sinon.spy(this.supportRequest, 'dragAndDropPreventDefault');
    this.dropFilesSpy = sinon.spy(this.supportRequest, 'dropFiles');
    this.submitFormSpy = sinon.spy(this.supportRequest, 'submitForm');
    this.renderSubmitSpy = sinon.spy(this.supportRequest, 'renderSubmit');
    this.renderFilesSpy = sinon.spy(this.supportRequest, 'renderFiles');
    this.removeErrorMsgSpy = sinon.spy(this.supportRequest, 'removeErrorMsg');
    this.removeAllErrorMessagesSpy = sinon.spy(this.supportRequest, 'removeAllErrorMessages');
    this.setFilterFilesSpy = sinon.spy(this.supportRequest, 'filterFiles');

    this.supportRequest.init();
  });
  after(function () {
    $(document.body).empty();
    this.domHtml = null;
    this.initSpy.restore();
    this.renderLayoutSpy.restore();
    this.addInputTypeFileSpy.restore();
    this.dragAndDropPreventDefaultSpy.restore();
    this.dropFilesSpy.restore();
    this.submitFormSpy.restore();
    this.renderSubmitSpy.restore();
    this.renderFilesSpy.restore();
    this.removeErrorMsgSpy.restore();
    this.removeAllErrorMessagesSpy.restore();
    this.setFilterFilesSpy.restore();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });

  it('should filter files', function (done) {
    const e = {
      target: {
        files: [
          {
            name: 'file 1',
            size: 1024
          },
          {
            name: 'file 2',
            size: 20971520
          },
        ]
      }
    }
    this.supportRequest.dropFiles(e, this.supportRequest);
    expect(this.setFilterFilesSpy.called).to.be.true;

    done();
  });

  it('should prevent default on drag leave', function (done) {
    setDom(this);
    $('.js-tp-support-request__drag-and-drop').trigger('dragleave');
    expect(this.dragAndDropPreventDefaultSpy.called).to.be.true;
    done();
  });

  it('should render support request form after submit', function (done) {
    setDom(this);
    $('.js-tp-support-request__send-another-support-request').trigger('click');
    expect(this.renderLayoutSpy.called).to.be.true;
    done();
  });

  it('should prevent default on window drag enter', function (done) {
    setDom(this);
    $(window.document).trigger('dragenter');
    expect(this.dragAndDropPreventDefaultSpy.called).to.be.true;
    done();
  });

  it('should sanitize textarea on blur', function (done) {
    setDom(this);
    const $el = $('#howHelp');
    $el.val('<script>test</script>');
    $el.trigger('blur');
    expect($el.val()).to.equal('test');
    done();
  });

  it('should remove file', function (done) {
    setDom(this);
    $('.js-tp-support-request__drag-and-drop-file-remove-container').trigger('click');
    expect(this.renderFilesSpy.called).to.be.true;
    done();
  });

  it('should render support request form', function (done) {
    setDom(this);
    expect(this.renderLayoutSpy.called).to.be.true;
    done();
  });

  it('should open file picker', function (done) {
    setDom(this);
    $('.js-tp-support-request__drag-and-drop-button').trigger('click');
    expect(this.addInputTypeFileSpy.called).to.be.true;
    done();
  });

  it('should clean all error messages on fields when submited form', function (done) {
    setDom(this);
    $('.js-tp-support-request__submit').trigger('click');
    expect(this.removeAllErrorMessagesSpy.called).to.be.true;
    expect($('error-msg--active').length).to.equal(0);
    done();
  });

  it('should show error message on phone field when entered incorrect value and submit form', function(done) {
    setDom(this);
    const $phone =  $('#ownPhoneNumber');
    $phone.val('hjjagsdjhagdj');
    $('.js-tp-support-request__submit').trigger('click');
    expect($phone.closest('.js-tp-support-request__form-element').find('.error-msg--active').length).to.equal(1);
    done();
  });

  it('should show error message all required fields and optional if incorrect value entered and submit form', function(done) {
    setDom(this);
    $('#ownPhoneNumber').val('hjjagsdjhagdj');
    $('.js-tp-support-request__submit').trigger('click');
    expect($('.error-msg--active').length).to.equal(6);
    done();
  });

  it('should not submit form and show error messages on required fields', function(done) {
    setDom(this);
    $('.js-tp-support-request__submit').trigger('click');
    expect($('.error-msg--active').length).to.equal(5);
    done();
  });

  it('should submit form', function (done) {
    setDom(this);
    $("#onboardingMaintanance").trigger('click');
    $("#howHelp").val('Hello, I need to know how to send my form');
    $("#name").val('Gustavo Common');
    $("#email").val('Gustavo.Common@supplier.com');
    $("#companyLegalName").val('Test');
    $("#country").val('Indie');
    $("#city").val('Delphi');
    $('.js-tp-support-request__submit').trigger('click');

    expect(this.submitFormSpy.called).to.be.true;
    expect(this.renderSubmitSpy.called).to.be.true;
    done();
  });

});
