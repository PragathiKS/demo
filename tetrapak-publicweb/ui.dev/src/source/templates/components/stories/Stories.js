import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { dynMedia } from '../../../scripts/utils/dynamicMedia';
import { addLinkAttr,getLinkClickAnalytics } from '../../../scripts/common/common';

class Stories {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.renderData = [];
    this.cache.counterShowMore = 0;
    this.cache.noOfSetOfTeaser = 0;
    this.cache.tabsDataArray = [
    ];
    this.cache.storiesData = this.root.find('.js-stories-data');
    this.cache.storiesNode = this.root.find('.pw-stories__story-grid');
  }
  bindEvents() {
    this.getStoriesList();
  }
  /**
   * function to get stories list from api
  */
  getStoriesList = () => {
    const servletPath = this.cache.storiesData.data('stories-api');
    ajaxWrapper
      .getXhrObj({
        url: `${servletPath}.model.json`,
        method: ajaxMethods.GET,
        cache: true,
        dataType: 'json',
        contentType: 'application/json'
      })
      .done(data => {
        if (data) {
          this.cache.tabsDataArray = data.storyList;
          this.renderTeaserData(this.cache.tabsDataArray);
          this.renderButton();
          dynMedia.processImages();
          this.cache.storiesLink = this.root.find('.js-stories-analytics');
          addLinkAttr('.js-stories-analytics');
          
          $(this.cache.storiesLink).each(function() {
            const linkText = $(this).find('h3.story-text');
            const maxSize = 60;
            let sizeString = $.trim(linkText.text());
            
            if(sizeString.length > maxSize) {
              sizeString = sizeString.substring(0, maxSize)+'...';
              linkText.text(sizeString);
            }
          });
          this.cache.storiesLink.on('click', this.trackImageAnalytics);
        }
      });
  };

  /**
   * function to show render load more button
  */
  renderButton = () => {
    const { tabsDataArray } = this.cache;
    if(tabsDataArray.length > 6 ){
      this.root.find('.show-more').removeClass('d-none');
      this.root.on('click','.pw-stories__load-more',this.handleShowMore);
      // set margin bottom zero for hidden element
      this.root.find(`.set`).each(function(){
        $(this).closest('.margin-bottom').addClass('margin-bottom-zero');
      });
    }
  }

  /**
   * function to show more stories on click of load more button
  */
  handleShowMore = (e) => {
    const { counterShowMore } = this.cache;
    this.cache.noOfSetOfTeaser += 1;
    if(this.cache.noOfSetOfTeaser <= counterShowMore){
      this.root.find(`.set-${this.cache.noOfSetOfTeaser}`).each(function(){
        $(this).addClass('show');
        $(this).closest('.margin-bottom').removeClass('margin-bottom-zero');
      });
    }
    if(this.cache.noOfSetOfTeaser === counterShowMore){
      this.root.find('.show-more').addClass('d-none');
    }
    this.trackShowMoreAnalytics(e);
  }

  /**
   * set classes and property to story component
   * @param {Array} arr list of stories
  */
  setClassToTeaser = (arr) => {
    this.cache.storiesData = this.root.find('.js-stories-data');
    arr.forEach((set,i) => {
      set.forEach((item,index) => {
        item['isWcmmode'] = this.cache.storiesData.data('wcm-mode');
        if(i !== 0){
          item['class-hide'] = `set set-${i}`;
        }
        if(arr.length === i+1 && set.length === index+1){
          item['padding-zero'] = true;
        }

      });
    });
    return arr;
  }

  /**
   * structure stories list data
   * @param {Array} tabsDataArray list of stories
  */
  structuredData = (tabsDataArray) => {
    const teaserCount = tabsDataArray.length;
    const teaserData = {
      restSet : []
    };
    if(teaserCount < 6){
      teaserData['firstSet'] = [];
      teaserData['restSet'] = [];
      teaserData['iteratorValue'] = [...tabsDataArray];
    } else if(teaserCount >= 6){
      const numberOfSet = Math.floor(teaserCount/6);
      teaserData['firstSet'] = tabsDataArray.slice(0,6);
      if(numberOfSet === 1){
        teaserData['restSet'] = [];
        teaserData['iteratorValue'] = tabsDataArray.slice(6,tabsDataArray.length);
      }
      else if(numberOfSet > 1){
        let indexStart = 6;
        for(let i=2; i<=numberOfSet; i++){
          teaserData['restSet'].push(tabsDataArray.slice(indexStart,indexStart + 6));
          indexStart = indexStart + 6;
        }
        teaserData['iteratorValue'] = tabsDataArray.slice(indexStart,tabsDataArray.length);
      }
    }
    return { firstSet: teaserData['firstSet'],restSet :teaserData['restSet'],iteratorValue: teaserData['iteratorValue'] };
  }


  /**
   * set stories count value and structure stories list data
   * @param {Array} tabsDataArray list of stories
  */
  renderTeaserData = (tabsDataArray) => {
    const { firstSet,restSet,iteratorValue } = this.structuredData(tabsDataArray);
    const completeArray = [];
    if(firstSet.length) {
      completeArray.push(firstSet);
    }
    if(restSet.length){
      if(Array.isArray(restSet[0])){
        restSet.forEach((item) => {
          completeArray.push(item);
        });
      }
      else {
        completeArray.push(restSet);
      }
    }
    if(iteratorValue.length){
      completeArray.push(iteratorValue);
    }

    this.cache.renderData = this.setClassToTeaser(completeArray);

    this.cache.counterShowMore = restSet.length + (iteratorValue.length ? 1 : 0);
    this.renderTeaser(this.cache.renderData);
  }

  /**
   * render stories data
   * @param {Array} completeArray list of stories
  */
  renderTeaser = (completeArray) => {
    const { firstSet,restSet,iteratorValue } = this.structuredData(completeArray.flat());
    render.fn({
      template: 'storyGrid',
      data: {firstSet,restSet,iteratorValue},
      target: this.cache.storiesNode,
      hidden: false
    });
  }

  /**
   * track stories image data
  */
  trackImageAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.find('.story-text');
    const link = $target.closest('.js-stories-analytics');
    if(!link.attr('href') || link.attr('href') === '#') {
      return;
    }
    const dataObj = {
      'linkSection' : 'Stories_image click',
      'linkParentTitle': '',
      'linkName': $this.data('heading')
    };
    getLinkClickAnalytics(e,null,'Stories','.js-stories-analytics',true,dataObj);
  }

  /**
   * track show more button
  */
  trackShowMoreAnalytics = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.pw-stories__load-more');
    const dataObj = {
      'linkSection' : 'Stories_CTA click',
      'linkParentTitle': '',
      'linkName': $this.data('link-name')
    };
    getLinkClickAnalytics(e,null,'Stories','.js-stories-analytics',false,dataObj);
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Stories;
