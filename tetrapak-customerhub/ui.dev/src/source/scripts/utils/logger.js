import Logger from 'argon-logger';
import { isHeadlessChrome } from './browserDetect';

export const logger = new Logger({
  disable: isHeadlessChrome(),
  allowedQueryStringParameters: ['debug', 'debugClientLibs']
});
