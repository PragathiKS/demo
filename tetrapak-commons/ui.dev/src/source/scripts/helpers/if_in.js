import Handlebars from 'handlebars';
import 'core-js/features/array/includes';

export default function (available, value, opts) {
  let compareValues = [];
  if (typeof available === 'string') {
    compareValues = available.split(',').map(value => Handlebars.escapeExpression(value.trim()));
  } else if (Array.isArray(available)) {
    compareValues = available;
  }
  if (compareValues.includes(value)) {
    return opts.fn(this);
  }
  return opts.inverse(this);
}
