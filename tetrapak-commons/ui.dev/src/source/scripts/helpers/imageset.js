/**
 * Renders an image with adaptive rendition attributes
 *
 * Usage:
 *
 * {{imageset src="..." alt="..." config="..." }}
 *
 * Attributes:
 *
 * src: Image source attribute
 * alt: Image alt attribute
 * config: Configuration object
 *
 * Configuration example:
 *
 * {
 *   baseUrl: 'https://...'
 *   renditions: {
 *     desktop: "<width>,<height>,<cropStartX>,<cropStartY>,<cropEndX>,<cropEndY>",
 *     tabletl: "<width>,<height>,<cropStartX>,<cropStartY>,<cropEndX>,<cropEndY>",
 *     tabletp: "<width>,<height>,<cropStartX>,<cropStartY>,<cropEndX>,<cropEndY>",
 *     mobilel: "<width>,<height>,<cropStartX>,<cropStartY>,<cropEndX>,<cropEndY>",
 *     mobiley: "<width>,<height>,<cropStartX>,<cropStartY>,<cropEndX>,<cropEndY>"
 *   }
 * }
 */

import Handlebars from 'handlebars';

const placeholderImage = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAABmJLR0QAAAAAAAD5Q7t/AAAACXBIWXMAAAsSAAALEgHS3X78AAAADUlEQVQI12P4////fwAJ+wP90YOM8AAAAABJRU5ErkJggg==';

function queryString(variation) {
  const queryParams = [];
  Object.keys(variation).forEach(prop => {
    queryParams.push(`${prop}=${variation[prop]}`);
  });
  return queryParams.join('&');
}

export default function (options) {
  let src = Handlebars.escapeExpression(options.hash.src);
  let alt = Handlebars.escapeExpression(options.hash.alt);
  const classes = Handlebars.escapeExpression(options.hash.classes);
  const config = options.hash.config;
  let appliedClasses = ['tp-image'];
  if (typeof classes === 'string') {
    appliedClasses = [...appliedClasses, ...classes.split(' ').map(className => Handlebars.escapeExpression(className.trim()))];
  } else if (Array.isArray(classes)) {
    appliedClasses = [...appliedClasses, ...classes];
  }
  if (
    config
    && typeof config === 'object'
    && typeof config.baseUrl === 'string'
  ) {
    src = config.baseUrl + src;
  }
  const srcAttr = src ? ` data-src="${src}"` : '';
  const altAttr = alt ? ` alt="${alt}"` : '';
  if (
    !config
    || typeof config !== 'object'
    || (
      config
      && typeof config === 'object'
      && typeof config.renditions === 'undefined'
    )
  ) {
    return new Handlebars.SafeString(`<img src="${placeholderImage}" class="${appliedClasses.join(' ').trim()}"${srcAttr}${altAttr} />`);
  }
  const attributes = [];
  Object.keys(config.renditions).forEach(rendition => {
    const renditionObj = {};
    const renditionValues = config[rendition].split(',').map(value => Handlebars.escapeExpression(value.trim()));
    renditionObj.attr = `data-src_${rendition}`;
    renditionObj.variation = {
      wid: renditionValues[0],
      hei: renditionValues[1]
    };
    const cropN = renditionValues[2] ? renditionValues.slice(2).join(',') : '';
    if (cropN) {
      renditionObj.variation.cropN = cropN;
    }
    attributes.push(renditionObj);
  });
  let renditionAttr = '';
  attributes.forEach(attr => {
    renditionAttr += ` ${attr.rendition}="${src}?${queryString(attr.variation)}"`;
  });
  return new Handlebars.SafeString(`<img src="${placeholderImage}" class="${appliedClasses.join(' ').trim()}"${srcAttr}${altAttr}${renditionAttr} />`);
}
