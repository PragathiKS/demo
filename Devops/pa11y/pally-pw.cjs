"use strict"

const pa11y = require('pa11y');
const puppeteer = require('puppeteer');
const fs = require('fs');
const readline = require('readline');
const htmlReporter = require("pa11y/lib/reporters/html");

async function testPW(env, url_list_file_nm) {
    const browser = await puppeteer.launch({
        headless: 'new',
        ignoreHTTPSErrors: true
    });
    const page = await browser.newPage();
    
    try {
        await page.setViewport({width: 1366, height: 768});
        await page.setCacheEnabled(false);
        // accept cookie banner
        await page.goto(`https://www-${env}.tetrapak.com`, { waitUntil: 'networkidle2' });
        await page.waitForSelector('#onetrust-accept-btn-handler', { visible: true });
        await page.waitForTimeout(7000)
        await page.click('#onetrust-accept-btn-handler');
        
        // Pa11y Options
        const mtp_opt =  {
            browser,
            page: page,
            viewport: {
                width: 1366,
                height: 768
            },
            ignoreUrl: false,
            wait: 10000,
            runners: ['htmlcs', 'axe'],
            standard: 'WCAG2AAA'
        };
        fs.mkdirSync(`pw/${env}`, { recursive: true })
        // Read urls from the file
        const fileStream = fs.createReadStream(url_list_file_nm);
        const rl = readline.createInterface({
            input: fileStream,
            crlfDelay: Infinity
        });
        // iterate over all urls
        for await (const line of rl) {
            const result = await pa11y(line, mtp_opt);
            const report = await htmlReporter.results(result);
            const path = line.substring(line.lastIndexOf('/') + 1);
            const file_path = `pw/${env}/${path}`;
            fs.writeFile(file_path, report, (err) => {
                if (err) throw err;
            });
        }
    } catch (error) {
        // Handle the error
        throw error;
    }
    finally {
       await page.close();
       await browser.close();
    }
};
 
module.exports = testPW;