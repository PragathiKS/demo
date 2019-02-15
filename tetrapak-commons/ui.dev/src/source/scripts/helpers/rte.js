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
  const allowedAttr = sanitizeHtml.defaults.allowedAttributes;
  allowedAttr.img = ['src', 'alt', 'data-*'];
  allowedAttr.sup = ['data-*'];
  allowedAttr.sub = ['data-*'];
  const cleanHtml = sanitizeHtml(htmlText, {
    allowedTags: sanitizeHtml.defaults.allowedTags.concat(['img', 'sup', 'sub']),
    allowedAttributes: allowedAttr
  });
  return new Handlebars.SafeString(cleanHtml);
}
