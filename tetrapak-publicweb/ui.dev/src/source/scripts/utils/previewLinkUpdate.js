import $ from 'jquery';
import { parseQueryString } from '../common/common';

export default () => {
  const hasPreviewLink = parseQueryString().preview;
  if (hasPreviewLink) {
    $('.previewLinkBtn').prop('disabled', true);
    $('.preview-search-icon').css('display','none');
  }
};
