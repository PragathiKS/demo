export default function (value, available, opts) {
  if (value !== available) {
    return opts.fn(this);
  }
  return opts.inverse(this);
}
