
# DatePicker

## About
This component is based on [jQuery](https://www.npmjs.com/package/jquery) (v. 3.4.1), [jQuery UI](https://www.npmjs.com/package/jquery-ui) (v. 1.13.2) and  [inputmask](https://www.npmjs.com/package/inputmask/) (v. 5.0.7) and needs them included in the project to function properly.

## Usage
DatePicker is rendered by executing JS script, which targets `<div class="js-dp" />` element in the HTML markup.
```html
<div  class="js-dp"  />
```

```javascript
/* 1. Import DatePicker class or renderDatePicker function from 'src/source/templates/components/datepicker' */

import { renderDatePicker, DatePicker } from  '../datepicker';

/* 2. When the parent element is ready, call the render function either using provided method */
	renderDatePicker({
		el:  $('.js-dp'),
		type:  'range'
	});
/* or by directly calling init function on DatePicker instance */
	const  dp = new  DatePicker({
		el:  $('.js-dp'),
		type:  'range'
	});
	dp.init();
```



It also needs to have `_datepicker.scss` stylesheet imported somewhere in the project:
```sass
@import '../../../../scripts/utils/datePicker/datepicker';
```

## Options

**All of the options are optional**. Most of them you have to pass inside the object passed as the `renderDatePicker()` function argument, like so:
```javascript
renderDatePicker({
	el:  $('.js-dp'),
	type: 'range',
	dateFormat:  "dd-mm-yy",
	inputMask:  "99-99-9999",
	inputFormat:  "DD-MM-YYYY",
	startDate: '2000-01-01',
	endDate: '2000-02-01',
	minDate:  new  Date(),
	maxDate:  moment(new  Date()).add(50, 'years').toDate(),
	monthNames: ["Sty","Lut","Mar","Kwi","Maj","Cze","Lip","Sie","Wrz","Paz","Lis","Gru"],
	dayNames: ["Nd", "Po", "Wt", "Śr", "Cz", "Pt", "Sb"],
	inputFromLabel:  "Od",
	inputToLabel:  "Do",
	inputErrorLabel:  "To nie jest poprawna data",
	onCorrectValue: (dateFrom, dateTo) => console.log('onCorrectValue', { dateFrom, dateTo }),
	onIncorrectValue: (dateFrom, dateTo) => console.log('onIncorrectValue', { dateFrom, dateTo }),
	onEmptyValue: (dateFrom, dateTo) => console.log('onEmptyValue', { dateFrom, dateTo }),
	widgetConfig: {
		onClose: () =>  console.log("close")
	}
});
```
However, to make it easier for you to include translations, it is also possible to specify labels inside the HTML markup:

```html
<div
	class="js-dp"
	data-from-label="Od"
	data-to-label="Do"
	data-error-label="To nie jest poprawna data"
/>
```

If you specify labels in both the script and the markup, the function parameters take precedence.



Option | Type | Default | Usage
- | - | - | -
el | `jQuery object` | The target element. Note that default wrapper class "js-dp" is looked up for styling purposes, so if you want to make changes to class attribute make sure to include it as well.
type | `"default" | "range"` | `"default"` | Either `"default"` or `"range"`.
dateFormat | `String` | `"yy-mm-dd"` | Has to match `inputMask` and `inputFormat`, see above code for an example. See <https://api.jqueryui.com/datepicker/#option-dateFormat/> for possible values.
inputMask | `String` | `"9999-99-99"` | See <https://robinherbots.github.io/Inputmask/#/documentation/> for possible values.
inputFormat | `String` | `"YYYY-MM-DD"` | Has to match `inputMask`, is also used as input's placeholder.
startDate | `Date object | string` | `""` | Initital date to be selected.
endDate | `Date object | string` | `""` | Initital end date to be selected in the `range` type component.
minDate | `Date object` | Date 100 years before today | Will restrict possible dates for both input elements and dropdowns.
maxDate | `Date object` | Date 100 years after today | Will restrict possible dates for both input elements and dropdowns.
monthNames | `String[]` | `["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"]` | This option is used for displaying month names on the dropdown.
dayNames | `String[]` | `["Su","Mo","Tu","We","Th","Fr","Sa"]` | This option is used for displaying day names in the table header.
inputFromLabel | `String` | `"From"` | This option is used for displaying input's label. In a default DatePicker it's the only visible input.
inputToLabel | `String` | `"To"` | This option is used for displaying input's label. It only affects a `range` type component.
inputErrorLabel | `String` | `"To"` | This option is used for displaying error label in case of an invalid date.
onCorrectValue | `(dateFrom: Date | string, dateTo: Date | string | null): void` | `() => {}` | Function that will trigger if correct date is selected. Date type parameter means that the value is correct, string that it is wrong or empty.
onIncorrectValue | `(dateFrom: Date | string, dateTo: Date | string | null): void` | `() => {}` | Function that will trigger if wrong date is selected. Date type parameter means that the value is correct, string that it is wrong or empty.
onEmptyValue | `(dateFrom: Date | string, dateTo: Date | string | null): void` | `() => {}` | Function that will trigger if any value is cleared. Date type parameter means that the value is correct, string that it is wrong or empty.
widgetConfig | `object` | This option will override DatePicker's widget initial settings. See <https://api.jqueryui.com/datepicker/> for reference.

## Author
> Signed off by Filip Sas-Kulczycki

Contact me on Tetrapak or Publicis Teams or by e-mail (filsasku@publicisgroupe.net) if you need any support.
