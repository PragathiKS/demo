import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';

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
    this.cache.$document = $(window.document);
  }
  initCacheAfterFormRender() {
    this.cache.$dragAndDrop = this.root.find('.js-tp-add-equipment__drag-and-drop');
    this.cache.$dragAndDropAddButton = this.root.find('.js-tp-add-equipment__drag-and-drop-button');
  }
  initCacheAfterFilesRender() {
    this.cache.$dragAndDropRemoveButton = this.root.find('.js-tp-add-equipment__drag-and-drop-file-remove-container');
  }
  bindEvents() {
    const $this = this;
    $this.cache.$dragAndDrop.on('dragenter', e => {
      $this.dragAndDropPreventDefault(e);
    }).on('dragleave', e => {
      $this.dragAndDropPreventDefault(e);
    }).on('drop', e => {
      $this.dragAndDropPreventDefault(e);
      $this.dropFiles(e, $this, true);
    });

    $this.cache.$document.on('dragenter', e => {
      $this.dragAndDropPreventDefault(e);
    }).on('dragover', e => {
      $this.dragAndDropPreventDefault(e);
    }).on('drop', e => {
      $this.dragAndDropPreventDefault(e);
    });

    $this.cache.$dragAndDropAddButton.click(() => {
      $this.addInputTypeFile();
    });
  }
  bindEventsAfterFilesRender() {
    const $this = this;
    $this.cache.$dragAndDropRemoveButton.click((event) => {
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
    // eslint-disable-next-line no-console
    console.log($this.cache.files, 'dropFiles');
  }
  filterFiles(file) {
    if (file.type === 'application/pdf' || file.type.startsWith('image/')) {
      const maxFileSize = 10 * 1024 * 1024;
      if (file.size < maxFileSize) {
        return true;
      }
    }
    return false;
  }
  dragAndDropPreventDefault(e) {
    e.stopPropagation();
    e.preventDefault();
  }
  renderLayout() {
    const $this = this;
    render.fn({
      template: 'addEquipmentForm',
      target: '.js-tp-add-equipment__form',
      data: $this.cache.i18nKeys
    }, () => {
      $this.cache.$contentWrapper.removeClass('d-none');
      $this.cache.$spinner.addClass('d-none');

      $this.initCacheAfterFormRender();
    });
  }
  renderFiles() {
    const $this = this;
    render.fn({
      template: 'addEquipmentFile',
      target: '.js-tp-add-equipment__drag-and-drop-files-container',
      data: this.cache.files
    }, () => {
      $this.initCacheAfterFilesRender();
      $this.bindEventsAfterFilesRender();
    });
  }
  init() {
    this.initCache();
    this.renderLayout();
    this.bindEvents();
  }
}

export default AddEquipment;
