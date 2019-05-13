import $ from 'jquery';

class ArticleItem {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize cache here */
    this.cache.$articleItemBtnLink = $('.articleItemLink', this.root);
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.cache.$articleItemBtnLink.click((e) => {
      let article = e.target.closest('.pw-article');
      let articleContainer = e.target.closest('.article-container');
      if (digitalData) { // eslint-disable-line
        digitalData.linkClick = {}; //eslint-disable-line
        digitalData.linkClick.linkType = article.getAttribute('data-articleItem-linkType'); //eslint-disable-line
        digitalData.linkClick.linkSection = 'articleItem'; //eslint-disable-line
        if (articleContainer) {
          digitalData.linkClick.linkParentTitle = articleContainer.getAttribute('data-container-title'); //eslint-disable-line
        }
        digitalData.linkClick.linkName = article.getAttribute('data-articleItem-linkName'); //eslint-disable-line
        digitalData.linkClick.contentName = article.getAttribute('data-articleItem-contentName'); //eslint-disable-line
        if (typeof _satellite !== 'undefined') { //eslint-disable-line
            _satellite.track('linkClicked'); //eslint-disable-line
        }
      }
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default ArticleItem;
