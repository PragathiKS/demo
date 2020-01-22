import Header from './Header';
import $ from 'jquery';
import headerTemplate from '../../../test-templates-hbs/header.hbs';
import { loc } from '../../../scripts/common/common';

describe('Header', function () {
  before(function () {
    $(document.body).empty().html(headerTemplate());
    this.header = new Header({ el: document.body });
    this.initSpy = sinon.spy(this.header, 'init');
    this.openSearchBoxSpy = sinon.spy(this.header, 'openSearchBox');
    this.closeSearchBoxSpy = sinon.spy(this.header, 'closeSearchBox');
    this.searchSpy = sinon.spy(this.header, 'search');
    this.replaceStub = sinon.stub(loc, 'replace');
    this.replaceStub.returns(true);
    this.header.init();

  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.openSearchBoxSpy.restore();
    this.closeSearchBoxSpy.restore();
    this.searchSpy.restore();
    this.replaceStub.restore();
  });
  it('should initialize', function () {
    expect(this.header.init.called).to.be.true;
  });
  it('should open search box on click', function () {
    $('.js-tp-pw-header__search-box-toggle').trigger('click');
    expect(this.header.openSearchBox.called).to.be.true;
  });
  it('should close search box on click', function () {
    $('.js-tp-pw-search-box__close-search-box').trigger('click');
    expect(this.header.closeSearchBox.called).to.be.true;
  });
  it('should submit search term on enter key press', function () {
    $('.js-tp-pw-search-box__input').trigger('key.return');
    expect(this.searchSpy.called).to.be.true;
  });
});
