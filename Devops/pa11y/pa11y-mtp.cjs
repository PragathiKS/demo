"use strict"

const pa11y = require('pa11y');
const puppeteer = require('puppeteer');
const fs = require('fs');
const readline = require('readline');
const htmlReporter = require("pa11y/lib/reporters/html");


async function testMTP(env, userid, passwd, url_list_file_nm) {
    var appEnv = "MyTetraPakDev";
    appEnv = (env == 'qa') ? "MyTetraPakTest" : appEnv;
    appEnv = (env == 'stage') ? "MyTetraPakStage" : appEnv;
    const browser = await puppeteer.launch({
        headless: false,
        ignoreHTTPSErrors: true
    });
    const page = await browser.newPage();
    
    try {
        
        // Log in and accept cookie banner
        await page.setViewport({width: 1366, height: 768});
        await page.goto(`https://ssodev.tetrapak.com/idp/startSSO.ping?PartnerSpId=${appEnv}`, { waitUntil: 'networkidle2' });
        await page.waitForSelector('#signInButton');
        await page.$eval('#username', (el, value) => el.value = value, userid);
        await page.$eval('#password', (el, value) => el.value = value, passwd);
        await page.click('#signInButton');
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
        fs.mkdirSync(`mtp/${env}`, { recursive: true })
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
            const file_path = `mtp/${env}/${path}`;
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

module.exports = testMTP;