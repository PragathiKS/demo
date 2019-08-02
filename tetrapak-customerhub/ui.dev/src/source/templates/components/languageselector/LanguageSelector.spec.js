import LanguageSelector from './LanguageSelector';
import languageSelectorTemplate from '../../../test-templates-hbs/languageSelector.hbs';
import { PageReloader } from '../../../scripts/utils/pageReloader';
import $ from 'jquery';

describe('LanguageSelector', function () {
  before(function () {
    $(document.body).empty().html(languageSelectorTemplate());
    this.languageSelector = new LanguageSelector({ el: $('.js-lang-modal') });
    this.initSpy = sinon.spy(this.languageSelector, 'init');
    this.closePopupSpy = sinon.spy(this.languageSelector, 'closeModalHandler');
    this.setCustomerLanguageSpy = sinon.spy(this.languageSelector, 'setCustomerLanguage');
    this.pageReloadSpy = sinon.stub(PageReloader, 'reload');
    this.languageSelector.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.closePopupSpy.restore();
    this.setCustomerLanguageSpy.restore();
    this.pageReloadSpy.restore();
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
