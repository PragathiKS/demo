import $ from 'jquery';
import AddEquipment from './AddEquipment';
import addEquipmentTemplate from '../../../test-templates-hbs/addEquipment.hbs';
import { ajaxWrapper } from "../../../scripts/utils/ajax";
import auth from "../../../scripts/utils/auth";

describe('AddEquipment', function () {
  const jqRef = {
    setRequestHeader() {
      // Dummy method
    }
  };
  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, 'success', jqRef);
    return pr.promise();
  }
  function setDom($this) {
    $(document.body).empty().html($this.domHtml);
    $this.addEquipment = new AddEquipment({
      el: document.body
    });
  }
  before(function () {
    this.domHtml = addEquipmentTemplate();
    setDom(this);
    this.initSpy = sinon.spy(this.addEquipment, 'init');
    this.bindEventsSpy = sinon.spy(this.addEquipment, 'bindEvents');
    this.renderFormSpy = sinon.spy(this.addEquipment, 'renderForm');
    this.addInputTypeFileSpy = sinon.spy(this.addEquipment, 'addInputTypeFile');
    this.dragAndDropPreventDefaultSpy = sinon.spy(this.addEquipment, 'dragAndDropPreventDefault');
    this.dropFilesSpy = sinon.spy(this.addEquipment, 'dropFiles');
    this.submitFormSpy = sinon.spy(this.addEquipment, 'submitForm');
    this.renderSubmitSpy = sinon.spy(this.addEquipment, 'renderSubmit');
    this.renderFilesSpy = sinon.spy(this.addEquipment, 'renderFiles');
    this.setRemoveErrorMsgSpy = sinon.spy(this.addEquipment, 'removeErrorMsg');
    this.setFilterFilesSpy = sinon.spy(this.addEquipment, 'filterFiles');

    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse({statusCode:'200'}));
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: "fLW1l1EA38xjklTrTa5MAN7GFmo2",
        expires_in: "43199",
        token_type: "BearerToken"
      }
    });

    this.addEquipment.init();
  });
  after(function () {
    $(document.body).empty();
    this.domHtml = null;
    this.initSpy.restore();
    this.bindEventsSpy.restore();
    this.renderFormSpy.restore();
    this.addInputTypeFileSpy.restore();
    this.dragAndDropPreventDefaultSpy.restore();
    this.dropFilesSpy.restore();
    this.submitFormSpy.restore();
    this.renderSubmitSpy.restore();
    this.renderFilesSpy.restore();
    this.ajaxStub.restore();
    this.tokenStub.restore();
    this.setRemoveErrorMsgSpy.restore();
    this.setFilterFilesSpy.restore();
  });


  it('should remove error messages', function (done) {
    // setDom(this);
    // if (!this.addEquipment.cache || !this.addEquipment.cache.files) {
    //   this.addEquipment.cache = {
    //     files: []
    //   }
    // }
    this.addEquipment.cache.files.push('file');
    this.addEquipment.setFieldsMandatory();
    expect(this.setRemoveErrorMsgSpy.called).to.be.true;
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
    this.addEquipment.dropFiles(e, this.addEquipment);
    expect(this.setFilterFilesSpy.called).to.be.true;

    done();
  });


  it('should prevent default on drag leave', function (done) {
    setDom(this);
    $('.js-tp-add-equipment__drag-and-drop').trigger('dragleave');
    expect(this.dragAndDropPreventDefaultSpy.called).to.be.true;
    done();
  });


  it('should render add equipment form after submit', function (done) {
    setDom(this);
    $('.js-tp-add-equipment__add-another-equipment').trigger('click');
    expect(this.renderFormSpy.called).to.be.true;
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
    const $el = $('#addEquipmentComments');
    $el.val('<script>test</script>');
    $el.trigger('blur');
    expect($el.val()).to.equal('test');
    done();
  });

  it('should remove file', function (done) {
    setDom(this);
    $('.js-tp-add-equipment__drag-and-drop-file-remove-container').trigger('click');
    expect(this.renderFilesSpy.called).to.be.true;
    done();
  });

  it('should initialize', function (done) {
    setDom(this);
    expect(this.initSpy.called).to.be.true;
    done();
  });

  it('should render add equipment form', function (done) {
    setDom(this);
    expect(this.renderFormSpy.called).to.be.true;
    done();
  });

  it('should open file picker', function (done) {
    setDom(this);
    $('.js-tp-add-equipment__drag-and-drop-button').trigger('click');
    expect(this.addInputTypeFileSpy.called).to.be.true;
    done();
  });

  it('should submit form', function (done) {
    setDom(this);
    $("#addEquipmentSerialNumber").val('12345');
    $("#addEquipmentCountry").val('PL');
    $("#addEquipmentSite").val('DAIRY PLU_NSW');
    $("#addEquipmentLine").val('MXP-NESTLELGS JAL-LIN0A');
    $("#addEquipmentPosition").val('position');
    $("#addEquipmentEquipmentStatus").val('EXPO');
    $("#addEquipmentEquipmentDescription").val('desc');
    $("#addEquipmentManufactureOfAsset").val('asset');
    $("#addEquipmentCountryOfManufacture").val('PL');
    $("#addEquipmentConstructionYear").val('2021');
    $('.js-tp-add-equipment__submit').trigger('click');

    expect(this.submitFormSpy.called).to.be.true;
    done();
  });
});
