import $ from 'jquery';
import Searchbar from './Searchbar';
import searchbarTemplate from '../../../test-templates-hbs/searchbar.hbs';


describe('Searchbar', function () {
  before(function () {
    $(document.body).empty().html(searchbarTemplate());
    this.searchbar = new Searchbar({
      el: document.body
    });
    this.initSpy = sinon.spy(this.searchbar, 'init');
    this.searchbarCloseClickSpy = sinon.spy(this.searchbar, 'searchbarCloseClick');
    this.searchIconClickSpy = sinon.spy(this.searchbar, 'searchIconClick');
    this.openStub = sinon.stub(window, 'open');
    this.searchbar.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.searchbarCloseClickSpy.restore();
    this.searchIconClickSpy.restore();
    this.openStub.restore();
  });
  it('should call searchbarCloseClick on click', function () {
    $('.search-bar-close').trigger('click');
    expect(this.searchbar.searchbarCloseClick.called).to.be.true;
  });
  it('should call searchIconClick with value on click', function () {
    $('.search-icon').trigger('click');
    expect(this.searchbar.searchIconClick.called).to.be.true;
  });
  it('should call searchIconClick without value on click', function () {
    $('.js-search-bar-input').val('');
    $('.search-icon').trigger('click');
    expect(this.searchbar.searchIconClick.called).to.be.true;
  });
});
