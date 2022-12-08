import Logger from 'argon-logger';

export const logger = new Logger({
  allowedQueryStringParameters: ['debug', 'debugClientLibs']
});
