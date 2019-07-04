import ListContentImage from './ListContentImage';
import $ from 'jquery';
import listContentImageTemplate from '../../../test-templates-hbs/listContentImage.hbs';

describe.skip('ListContentImage', function () {
  before(function () {
    $(document.body).empty().html(listContentImageTemplate());
    this.listContentImage = new ListContentImage({ el: document.body });
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
    expect(this.listContentImage.init.called).to.be.true;
  });
  it('should set active tab on click', function () {
    $('.pw-listContentImage__tabMenuListItem__link').first().trigger('click');
    expect(this.listContentImage.setActiveTab.called).to.be.true;
  });
});
