import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';

function _renderLayout() {
  const $this = this;
  render.fn({
    template: 'addEquipmentForm',
    target: '.js-tp-add-equipment__content-wrapper',
    data: { i18nKeys: $this.cache.i18nKeys, country: $this.cache.country, line: $this.cache.line , site: $this.cache.site, equipmentStatus: $this.cache.equipmentStatus }
  }, () => {
    $this.cache.$contentWrapper.removeClass('d-none');
    $this.cache.$spinner.addClass('d-none');
  });
}
function _renderFiles() {
  const $this = this;
  const obj = $this.cache.files.map(obj => ({ name: obj.name, size: (obj.size / (1024 * 1024)).toFixed(2) + ' MB', removeFileLabel: $this.cache.i18nKeys.removeFileLabel }));
  render.fn({
    template: 'addEquipmentFiles',
    target: '.js-tp-add-equipment__drag-and-drop-files-container',
    data: obj
  });
}

class AddEquipment {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.files = [];
    this.cache.$contentWrapper = this.root.find('.js-tp-add-equipment__content-wrapper');
    this.cache.$spinner = this.root.find('.js-tp-spinner');
    this.cache.configJson = this.root.find('.js-tp-add-equipment__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
    const dummyArray  = [ { key: 'A', desc: 'A' }, { key: 'B', desc: 'B'}, { key: 'C', desc: 'C'} ];
    this.cache.country = dummyArray;
    this.cache.site = dummyArray;
    this.cache.line = dummyArray;
    this.cache.equipmentStatus = dummyArray;
  }
  bindEvents() {
    const $this = this;
    $(window.document).on('dragenter dragover drop', e => {
      $this.dragAndDropPreventDefault(e);
    });

    $this.root.on('dragenter dragleave drop', '.js-tp-add-equipment__drag-and-drop', e => {
      $this.dragAndDropPreventDefault(e);
      if (e.type === 'drop') {
        $this.dropFiles(e, $this, true);
      }
    });

    $this.root.on('click', '.js-tp-add-equipment__drag-and-drop-button', () => {
      $this.addInputTypeFile();
    });

    $this.root.on('click', '.js-tp-add-equipment__drag-and-drop-file-remove-container', event => {
      $this.removeFile(event);
    });
  }
  removeFile(e) {
    const index = $(e.currentTarget).data('index');
    this.cache.files.splice(index, 1);
    this.renderFiles();
  }
  addInputTypeFile() {
    const input = document.createElement('input');
    input.style.cssText = 'display: none;';
    input.type = 'file';
    input.multiple = true;
    document.body.appendChild(input);
    input.addEventListener('change', (event) => this.dropFiles(event, this), false);
    input.click();
    document.body.removeChild(input);
  }
  dropFiles(e, $this, dropEvent) {
    let files;
    if (dropEvent) {
      files = e.originalEvent.dataTransfer.files;
    } else {
      files = e.target.files;
    }
    for (let i = 0; i < files.length; i++) {
      if ($this.filterFiles(files[i])) {
        $this.cache.files.push(files[i]);
      }
    }
    $this.renderFiles();
  }
  filterFiles(file) {
    const maxFileSize = 10 * 1024 * 1024;
    if (file.size < maxFileSize) {
      return true;
    }
    return false;
  }
  dragAndDropPreventDefault(e) {
    e.stopPropagation();
    e.preventDefault();
  }
  renderLayout() {
    return _renderLayout.apply(this, arguments);
  }

  renderFiles() {
    return _renderFiles.apply(this, arguments);
  }
  init() {
    this.initCache();
    this.renderLayout();
    this.bindEvents();
  }
}

export default AddEquipment;
