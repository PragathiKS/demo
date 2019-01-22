module.exports.register = function (Handlebars) {
    'use strict';

    /**
     * **Examples:**
     *
     * Data:
     *
     * ```js
     * var accounts = [
     *   {'name': 'John', 'email': 'john@example.com'},
     *   {'name': 'Malcolm', 'email': 'malcolm@example.com'},
     *   {'name': 'David', 'email': 'david@example.com'}
     * ];
     * ```
     *
     * Templates:
     *
     * ```handlebars
     * {{#forEach accounts}}
     *   <a href="mailto:{{ email }}" title="Send an email to {{ name }}">
     *     {{ name }}
     *   </a>{{#unless isLast}}, {{/unless}}
     * {{/forEach}}
     * ```
     * Credit: http://bit.ly/14HLaDR
     */

    Handlebars.registerHelper('forEach', function (array, options) {
        var len = array.length;
        var i = 0;
        var buffer = '';

        while (i < len) {
            var item = array[i];
            item.index = i + 1;
            item._total = len;
            item.isFirst = i === 0;
            item.isLast = i === (len - 1);
            buffer += options.fn(item);
            i++;
        }
        return buffer;
    });

};