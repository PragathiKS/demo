"use strict"

const testMTP = require("./pa11y-mtp.cjs");
const testPW = require("./pa11y-pw.cjs");

if (process.argv[2] == 'mtp') {
    const env = process.argv[3];
    const userid =  process.argv[4];
    const passwd = process.argv[5];
    const url_list_file_nm = process.argv[6];
    const out_dir = process.argv[7];
    testMTP(env, userid, passwd, url_list_file_nm, out_dir);
}

if (process.argv[2] == 'pw') {
    const env = process.argv[3];
    const url_list_file_nm = process.argv[4];
    const out_dir = process.argv[5];
    testPW(env, url_list_file_nm, out_dir);
}

