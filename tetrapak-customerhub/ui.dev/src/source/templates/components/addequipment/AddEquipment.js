import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';

function _renderLayout() {
  const $this = this;
  render.fn({
    template: 'addEquipmentForm',
    target: '.js-tp-add-equipment__form',
    data: $this.cache.i18nKeys
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
  }, () => {
    $this.bindEventsAfterFilesRender();
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
  }
  bindEvents() {
    const $this = this;
    $this.root.find('.js-tp-add-equipment__drag-and-drop').on('dragenter', e => {
      $this.dragAndDropPreventDefault(e);
    }).on('dragleave', e => {
      $this.dragAndDropPreventDefault(e);
    }).on('drop', e => {
      $this.dragAndDropPreventDefault(e);
      $this.dropFiles(e, $this, true);
    });

    $(window.document).on('dragenter', e => {
      $this.dragAndDropPreventDefault(e);
    }).on('dragover', e => {
      $this.dragAndDropPreventDefault(e);
    }).on('drop', e => {
      $this.dragAndDropPreventDefault(e);
    });

    $this.root.find('.js-tp-add-equipment__drag-and-drop-button').click(() => {
      $this.addInputTypeFile();
    });

  }
  bindEventsAfterFilesRender() {
    const $this = this;
    $this.root.find('.js-tp-add-equipment__drag-and-drop-file-remove-container').click((event) => {
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
