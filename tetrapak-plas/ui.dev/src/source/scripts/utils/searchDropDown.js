import $ from 'jquery';

function searchOptionInRange(start, end, options) {
  for (let i = start; i < end; i++) {
    const opt = options[i].toUpperCase();
    if (opt.startsWith(this.searchValue)) {
      return { value: opt, index: i };
    }
  }

  return null;
}

function searchOption(index, options) {
  let option;

  if (this.searchValue) {
    option = searchOptionInRange.call(this, index, options.length, options);

    if (!option) {
      option = searchOptionInRange.call(this, 0, index, options);
    }
  }

  return option;
}

export default function(event, options) {
  if (this.searchTimeout) {
    clearTimeout(this.searchTimeout);
  }

  const char = String.fromCharCode(event.keyCode);
  this.previousSearchChar = this.currentSearchChar;
  this.currentSearchChar = char;

  if (this.previousSearchChar === this.currentSearchChar) {
    this.searchValue = this.currentSearchChar;
  } else {
    this.searchValue = this.searchValue ? this.searchValue + char : char;
  }

  const searchIndex = 0;
  const newOption = searchOption.call(this, searchIndex, options);

  if (newOption) {
    $('.dropdown-menu.show .dropdown-item')
      .get(newOption.index)
      .focus();
  }

  this.searchTimeout = setTimeout(() => {
    this.searchValue = null;
  }, 500);
}
