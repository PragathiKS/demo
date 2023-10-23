class AllPaymentsTag {
  constructor (cache, root) {
    this.cache = cache;
    this.root = root;
    this.cache.tag = this.root.querySelector('.tagContainer');
  }

  updateFilter = (key, value) => {
    let {activeTagFilter} = this.cache;
    const {activeFilter} = this.cache;

    if (key === 'invoiceDates') {
      activeFilter[key] = [];
    }
    else {
      activeFilter[key] = activeFilter[key].filter(obj => obj !== value);
    }
    activeTagFilter = activeTagFilter.filter(obj =>  !obj[key] || (obj[key] && obj[key].val !== value));
    this.cache.activeFilter[key] = activeFilter[key];
    this.cache.activeTagFilter = activeTagFilter;

    const customEvent = new CustomEvent('FilterChanged', {
      detail: {
        type: 'removed'
      }
    });
    this.root.dispatchEvent(customEvent);
  }

  bindEvents() {
    const self = this;

    if(this.cache.tag) {
      this.cache.tag.addEventListener('click', function(e) {
        const target = e.target.closest('.js-tag');
        if(target) {
          const item = target.querySelector('.js-field');
          self.updateFilter(item.getAttribute('data-key'), item.getAttribute('data-value'));
          self.cache.tag.removeChild(target);
        }
      });
    }
  }
}

export default AllPaymentsTag;
