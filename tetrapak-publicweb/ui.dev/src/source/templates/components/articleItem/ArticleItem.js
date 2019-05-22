import $ from 'jquery';

class ArticleItem {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize cache here */
    this.cache.$articleItemBtnLink = $('.articleItemLink', this.root);
    this.cache.digitalData = digitalData; //eslint-disable-line
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.cache.$articleItemBtnLink.click((e) => {
      let article = e.target.closest('.pw-article');
      let articleContainer = e.target.closest('.article-container');
      if (this.cache.digitalData) {
        this.cache.digitalData.linkClick = {};
        this.cache.digitalData.linkClick.linkType = article.getAttribute('data-articleItem-linkType');
        this.cache.digitalData.linkClick.linkSection = 'articleItem';
        this.cache.digitalData.linkClick.linkParentTitle = article.getAttribute('data-articleItem-parentTitle');
        if (articleContainer && articleContainer.getAttribute('data-container-title') !== null) {
          this.cache.digitalData.linkClick.contentName = articleContainer.getAttribute('data-container-title');
          let listPosition =  [].slice.call(articleContainer.querySelectorAll('.pw-article')).indexOf(article) + 1;
          this.cache.digitalData.linkClick.linkListPos = listPosition;
        }
        this.cache.digitalData.linkClick.linkName = article.getAttribute('data-articleItem-linkName');
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
