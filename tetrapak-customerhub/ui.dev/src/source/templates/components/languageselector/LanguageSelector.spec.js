import LanguageSelector from './LanguageSelector';
import languageSelectorTemplate from '../../../test-templates-hbs/languageSelector.hbs';
import $ from 'jquery';
import { ajaxWrapper } from '../../../scripts/utils/ajax';

describe('LanguageSelector', function () {
  const jqRef = {};
  function ajaxResponse() {
    const pr = $.Deferred();
    pr.resolve({}, 'success', jqRef);
    return pr.promise();
  }
  before(function () {
    $(document.body).empty().html(languageSelectorTemplate());
    this.languageSelector = new LanguageSelector({ el: $('.js-lang-modal') });
    this.reloadStub = sinon.stub(this.languageSelector, 'reloadPage');
    this.initSpy = sinon.spy(this.languageSelector, 'init');
    this.closePopupSpy = sinon.spy(this.languageSelector, 'closeModalHandler');
    this.setCustomerLanguageSpy = sinon.spy(this.languageSelector, 'setCustomerLanguage');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.returns(ajaxResponse());
    this.languageSelector.init();
  });
  after(function () {
    $(document.body).empty();
    this.reloadStub.restore();
    this.initSpy.restore();
    this.closePopupSpy.restore();
    this.setCustomerLanguageSpy.restore();
    this.ajaxStub.restore();
  });

  it('should initialize', function (done) {
    expect(this.languageSelector.init.called).to.be.true;
    done();
  });

  it('should close popup when close button is clicked', function (done) {
    $('.js-close-btn').trigger('click');
    expect(this.languageSelector.closeModalHandler.called).to.be.true;
    done();
  });

  it('should set Customer Language when one` of lang button is clicked', function (done) {
    $('.js-lang-selector__btn').trigger('click');
    expect(this.languageSelector.setCustomerLanguage.called).to.be.true;
    done();
  });
});
