import $ from 'jquery';
import CotsSupport from './CotsSupport';
import cotsSupportTemplate from '../../../test-templates-hbs/cotsSupport.hbs';

const helpers = {
  fillEmptyInputs() {
    $('input:text')
        .filter((idx, el) => !$(el).val())
        .val('Test data');
  }
}

describe('CotsSupport', function () {
  before(function () {
    $(document.body).empty().html(cotsSupportTemplate());
    this.cotsSupport = new CotsSupport({
      el: document.body,
    });
    this.initSpy = sinon.spy(this.cotsSupport, 'init');
    this.renderCotsSupportFormSpy = sinon.spy(this.cotsSupport,'renderCotsSupportForm');
    this.renderFilesSpy = sinon.spy(this.cotsSupport,'renderFiles');
    this.addInputTypeFileSpy = sinon.spy(this.cotsSupport, 'addInputTypeFile');
    this.dragAndDropPreventDefaultSpy = sinon.spy(this.cotsSupport, 'dragAndDropPreventDefault');
    this.dropFilesSpy = sinon.spy(this.cotsSupport, 'dropFiles');
    this.setFilterFilesSpy = sinon.spy(this.cotsSupport, 'filterFiles');
    this.renderSuccessMessageSpy = sinon.spy(this.cotsSupport,'renderSuccessMessage');
    this.addErrorMsgSpy = sinon.spy(this.cotsSupport, 'addErrorMsg');
    this.submitFormSpy = sinon.spy(this.cotsSupport, 'submitForm');
    this.cotsSupport.init();
  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.renderCotsSupportFormSpy.restore();
    this.renderFilesSpy.restore();
    this.addInputTypeFileSpy.restore();
    this.dragAndDropPreventDefaultSpy.restore();
    this.dropFilesSpy.restore();
    this.setFilterFilesSpy.restore();
    this.renderSuccessMessageSpy.restore();
    this.submitFormSpy.restore();
    this.addErrorMsgSpy.restore();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });

  it('should render type of query form', function (done) {
    expect(this.renderCotsSupportFormSpy.called).to.be.true;
    done();
  });

  it('should render form validation errors', function (done) {
    $('.js-tp-cots-support__submit').trigger('click');
    expect(this.renderSuccessMessageSpy.called).to.be.false;
    expect(this.addErrorMsgSpy.called).to.be.true;
    done();
  });

  it('should open file picker', function (done) {
    $('.js-tp-cots-support__drag-and-drop-button').trigger('click');
    expect(this.addInputTypeFileSpy.called).to.be.true;
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
    this.cotsSupport.dropFiles(e, this.cotsSupport);
    expect(this.setFilterFilesSpy.called).to.be.true;

    done();
  });


  

  it('should remove file', function (done) {
    $('.js-tp-cots-support__drag-and-drop-file-remove-container').trigger('click');
    expect(this.renderFilesSpy.called).to.be.true;
    done();
  });

  it('should prevent default on drag leave', function (done) {
    $('.js-tp-cots-support__drag-and-drop').trigger('dragleave');
    expect(this.dragAndDropPreventDefaultSpy.called).to.be.true;
    done();
  });

  it('should prevent default on window drag enter', function (done) {
    $(window.document).trigger('dragenter');
    expect(this.dragAndDropPreventDefaultSpy.called).to.be.true;
    done();
  });

  it('should render Success Message page after form submit', function (done) {
    $('#technicalIssues[value=technicalIssues]').prop('checked', true);
    $('#company').val('The Milk Company');
    $('#customerSite').val('Test Data');
    $('#affectedSystems option:eq(1)').prop('selected', true);
    $('#softwareVersion').val('1.0.0');
    $('#licensenumber').val('123456');
    $('#description').val('Test description');
    $('#questions').val('Test content');
    $('#name').val('Test user');
    $('#emailAddress').val('tetrapak@tetrapak.com');
    $('#telephone').val('1234567');
    $('.js-tp-cots-support__submit').trigger('click');
    expect(this.submitFormSpy.called).to.be.true;
    done();
  });

  it('should handle config read error', function () {
    $(document.body).empty().html(cotsSupportTemplate());
    $('.js-tp-cots-support__config').remove();
    this.cotsSupport = new CotsSupport({
      el: document.body,
    });
    this.cotsSupport.init();
    expect(this.cotsSupport.cache.i18nKeys).to.be.deep.equal({});
  });
});
