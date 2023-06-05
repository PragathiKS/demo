/* eslint-disable */

import $ from 'jquery';

class MegaMenuDescription {
  constructor({ el, targetClass }) {
    this.root = $(el);

    console.log('constructor', { targetClass });


    this.cache = {
      targetClass: targetClass || '.tp-pw-megamenudescription'
    };
  }

  swapDescriptionStringWithHTML = (targetClass) => {
    console.log('swapDescriptionStringWithHTML', { targetClass: this.cache.targetClass });

    const markupString = $(targetClass).html();
    $(targetClass).html(this.htmlDecode(markupString));
  }

  htmlDecode = (input) => {
    console.log('htmlDecode', { targetClass: this.cache.targetClass });
    var doc = new DOMParser().parseFromString(input, "text/html");
    return doc.documentElement.textContent;
  }

  watchChanges = (targetClass, cb) => {
    console.log('watchChanges', { targetClass: this.cache.targetClass });
    const targetNode = $(targetClass)[0];
    const config = { childList: true, subtree: true };

    const observer = new MutationObserver(cb);

    observer.observe(targetNode, config);
  }

  isEditMode = () => {
    console.log('isEditMode', { targetClass: this.cache.targetClass });
    return window.location.href.includes('/editor.html/content/experience-fragments/publicweb/en/solutions-mega-menu/');
  }

  bindEvents() {
    console.log('bindEvents', { targetClass: this.cache.targetClass });
    const { targetClass } = this.cache;

    if (this.isEditMode()) {
      this.watchChanges(targetClass, () => this.swapDescriptionStringWithHTML(targetClass));
    } else {
      this.swapDescriptionStringWithHTML(targetClass)
    }
  }

  init() {
    console.log('init', { targetClass: this.cache.targetClass });
    this.bindEvents();
  }
}

export default MegaMenuDescription;
