import { resolveCurrency } from '../common/common';

/**
 * Helper function to resolve currency ISO code
 * @param {string} value Value which needs to be prepended with currency symbol
 * @param {object} options Helper options
 */
export default function (value, options) {
  const { isoCode } = options.hash;
  return resolveCurrency(value, isoCode);
}
