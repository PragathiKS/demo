import $ from 'jquery';

class ArticleItem {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$articleItemBtnLink = this.root.find('.articleItemLink');
  }
  bindEvents() {
    this.cache.$articleItemBtnLink.on('click', this.articleLinkClick);
  }
  articleLinkClick = (e) => {
    const $target = $(e.target);
    const article = $target.closest('.pw-article');
    const articleContainer = $target.closest('.article-container');
    if (window.digitalData) {
      $.extend(window.digitalData, {
        linkClick: {
          linkType: article.attr('data-articleItem-linkType'),
          linkSection: 'articleItem',
          linkParentTitle: $.trim($target.closest('.pw-article').find('.pw-article__title').text()),
          linkName: $.trim($target.text())
        }
      });
      if (articleContainer.length) {
        const linkListPos = articleContainer.find('.pw-article').index(article) + 1;
        $.extend(window.digitalData.linkClick, {
          linkListPos,
          contentName: $.trim($target.closest('.article-container').find('.article-container__title').text())
        });
      }
      if (window._satellite) {
        window._satellite.track('linkClicked');
      }
    }
  }
  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default ArticleItem;
