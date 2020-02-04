import $ from 'jquery';
import ArticleItem from './ArticleItem';
import articleItemHbs from '../../../test-templates-hbs/articleItem.hbs';

describe('ArticleItem', function () {
  before(function () {
    $(document.body).empty().html(articleItemHbs());
    this.articleItem = new ArticleItem({
      el: document.body
    });
    this.initSpy = sinon.spy(this.articleItem, 'init');
    this.articleLinkClickSpy = sinon.spy(this.articleItem, 'articleLinkClick');
    window.digitalData = {};
    window._satellite = {
      track() { /* Dummy method */ }
    };
    this.articleItem.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.articleLinkClickSpy.restore();
  });
  it('should initialize', function () {
    expect(this.initSpy.called).to.be.true;
  });
  it('should trigger article link click on click of article link button', function () {
    $('.articleItemLink').trigger('click');
    expect(this.articleLinkClickSpy.called).to.be.true;
  });
});
