import $ from 'jquery';
import { initializeDAMPlayer,ytPromise,initializeYoutubePlayer } from '../../../scripts/utils/videoAnalytics';

class Video {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    ytPromise.then(() => {
      initializeYoutubePlayer();
    }).catch(err => {
      // eslint-disable-next-line no-console
      console.log('video component err is >>>>>',err);
    });
    initializeDAMPlayer();
  }
  bindEvents() {
    /* Bind jQuery events here */
    /**
     * Example:
     * const { $submitBtn } = this.cache;
     * $submitBtn.on('click', () => { ... });
     */
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Video;
