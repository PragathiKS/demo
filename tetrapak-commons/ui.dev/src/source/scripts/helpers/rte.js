/**
 * Helper to render rich text html in safe context
 *
 * Usage:
 *
 * {{rte <variable or string>}}
 */

import sanitizeHtml from 'sanitize-html';
import Handlebars from 'handlebars';

export default function (htmlText) {
  const allowedTags = sanitizeHtml.defaults.allowedTags.concat(['img', 'sup', 'sub']);
  const allowedAttr = sanitizeHtml.defaults.allowedAttributes;
  allowedAttr.img = ['src', 'alt'];
  allowedTags.forEach(tag => {
    allowedAttr[tag] = Array.isArray(allowedAttr[tag]) ? allowedAttr[tag] : [];
    allowedAttr[tag].push('class', 'id', 'data-*');
  });
  const cleanHtml = sanitizeHtml(htmlText, {
    allowedTags,
    allowedAttributes: allowedAttr
  });
  return new Handlebars.SafeString(cleanHtml);
}
