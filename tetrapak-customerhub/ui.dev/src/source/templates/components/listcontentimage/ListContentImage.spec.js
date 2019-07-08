import ListContentImage from './ListContentImage';
import $ from 'jquery';
import listContentImageTemplate from '../../../test-templates-hbs/listContentImage.hbs';

describe('ListContentImage', function () {
  before(function () {
    $(document.body).empty().html(listContentImageTemplate());
    this.listContentImage = new ListContentImage({ el: $('[data-root]') });
    this.initSpy = sinon.spy(this.listContentImage, 'init');
    this.setActiveTabSpy = sinon.spy(this.listContentImage, 'setActiveTab');
    this.listContentImage.init();

  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.setActiveTabSpy.restore();
  });
  it('should initialize', function () {
    expect(this.initSpy.called).to.be.true;
  });
  it('should set active tab on click', function () {
    $('.js-list-content-image__tab-menu-list-item__link').first().trigger('click');
    expect(this.setActiveTabSpy.called).to.be.true;
  });
});
