module.exports.register = function (Handlebars) {
    'use strict';

    Handlebars.registerHelper('logJSON', function (data, options) {

        // function pbcopy(data) { var proc = require('child_process').spawn('pbcopy'); proc.stdin.write(data); proc.stdin.end(); }
        // pbcopy(json);
        var fs = require('fs');
        var outputFilename = 'logs/output.json';

        fs.writeFile(outputFilename, JSON.stringify(data, null, 2), function (err) {
            if (err) {
                console.log(err);
            } else {
                console.log("JSON saved to " + outputFilename);
            }
        });

        return require('util').inspect(arguments, {depth: null});

    });

};