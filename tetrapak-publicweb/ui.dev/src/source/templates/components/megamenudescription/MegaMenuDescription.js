import $ from 'jquery';

class MegaMenuDescription {
  constructor({ el }) {
    this.root = $(el);
  }

  swapDescriptionStringWithHTML = () => {
    const markupString = this.root.html();
    this.root.html(this.htmlDecode(markupString));
  }

  htmlDecode = (input) => {
    var doc = new DOMParser().parseFromString(input, 'text/html');
    return doc.documentElement.textContent;
  }

  watchChanges = (cb) => {
    const config = { childList: true, subtree: true };

    const observer = new MutationObserver(cb);

    observer.observe(this.root, config);
  }

  isEditMode = () => window.location.href.includes(
    '/editor.html/content/experience-fragments/publicweb/en/solutions-mega-menu/'
  );

  bindEvents() {
    if (this.isEditMode()) {
      this.watchChanges(this.root, () => this.swapDescriptionStringWithHTML());
    } else {
      this.swapDescriptionStringWithHTML();
    }
  }

  init() {
    this.bindEvents();
  }
}

export default MegaMenuDescription;
