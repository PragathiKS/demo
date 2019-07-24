import 'core-js/features/promise';
import 'core-js/features/array/includes';
import { trackAnalytics } from './analytics';

let YT = null;

let trackIndex = 0;

/**
 * Calculates appropriate trackIndex based on percent completion
 * @param {number} currentTime Current video time
 * @param {*} totalTime Total video time
 */
function _getIndex(currentTime, totalTime) {
  if (currentTime >= 0) {
    const percentComplete = currentTime / totalTime * 100;
    if (percentComplete < 25) {
      return 0;
    }
    if (percentComplete >= 25 && percentComplete < 50) {
      return 1;
    }
    if (percentComplete >= 50 && percentComplete < 75) {
      return 2;
    }
    return 3;
  }
  return -1;
}

/**
 * Triggers video analytics event
 * @private
 * @param {string} videoInteraction Video event
 * @param {number} videoLength Video length in seconds
 */
function _trackVideoParameters(videoInteraction, videoLength) {
  const { linkParentTitle, videoName } = $(this).parents('.js-video-props').data();
  const linkType = 'internal';
  const linkSection = 'video';
  const trackingObj = {
    linkType,
    linkSection,
    linkParentTitle,
    videoName,
    videoInteraction,
    videoLength
  };
  trackAnalytics(trackingObj, 'linkClick', 'linkClicked', undefined, false);
}

/**
 * Calculates progress and log events on completion of 25%, 50%, and 75% completion
 * @private
 * @param {number} currentTime Current video time
 * @param {number} totalTime Total available time
 */
function _calculateProgress(currentTime, totalTime) {
  const percentCompleted = Math.floor(currentTime / totalTime * 100);
  if (percentCompleted >= 25 && trackIndex === 0) {
    _trackVideoParameters.apply(this, ['25%', Math.round(totalTime)]);
    trackIndex = 1;
  }
  if (percentCompleted >= 50 && trackIndex === 1) {
    _trackVideoParameters.apply(this, ['50%', Math.round(totalTime)]);
    trackIndex = 2;
  }
  if (percentCompleted >= 75 && trackIndex === 2) {
    _trackVideoParameters.apply(this, ['75%', Math.round(totalTime)]);
    trackIndex = 3;
  }
}

/**
 * Fires when youtube event state change occurs
 * @private
 * @param {object} thisIns Current instance
 * @param {object} e Event object
 */
function _onStateChange(thisIns, e) {
  const totalTime = thisIns.ytPlayer.getDuration();
  trackIndex = _getIndex(thisIns.ytPlayer.getCurrentTime(), totalTime);
  const $this = $(this);
  if (e.data === YT.PlayerState.PLAYING) {
    thisIns.intervalRef = window.setInterval(() => {
      _calculateProgress.apply(this, [thisIns.ytPlayer.getCurrentTime(), totalTime]);
    }, 500);
    const wasStarted = $this.data('started');
    if (['true', true].includes(wasStarted)) {
      _trackVideoParameters.apply(this, ['resume', Math.round(totalTime)]);
    } else {
      $this.attr('data-started', 'true').data('started', 'true');
      _trackVideoParameters.apply(this, ['start', Math.round(totalTime)]);
    }
  }
  if (e.data === YT.PlayerState.PAUSED) {
    if (thisIns.intervalRef) {
      window.clearInterval(thisIns.intervalRef);
    }
    _trackVideoParameters.apply(this, ['pause', Math.round(totalTime)]);
  }
  if (e.data === YT.PlayerState.ENDED) {
    if (thisIns.intervalRef) {
      window.clearInterval(thisIns.intervalRef);
    }
    _trackVideoParameters.apply(this, ['end', Math.round(totalTime)]);
  }
}

/**
 * Promise that ensures that youtube API is ready
 */
export const ytPromise = new Promise(function (resolve) {
  const scr = document.createElement('script');
  scr.src = 'https://www.youtube.com/iframe_api';
  const firstScript = document.getElementsByTagName('script')[0];
  firstScript.parentNode.insertBefore(scr, firstScript);
  window.onYouTubeIframeAPIReady = function (...args) {
    YT = window.YT;
    resolve(args);
  };
});

/**
 * Default export
 */
export default {
  init() {
    ytPromise.then(() => {
      $('.js-yt-player').each(function () {
        const thisIns = {};
        thisIns.ytPlayer = new YT.Player(this, {
          events: {
            onStateChange: _onStateChange.bind(this, thisIns)
          }
        });
      });
    });
    $('.js-dam-player').on('play', function () {
      const $this = $(this);
      const wasStarted = $this.data('started');
      if (['true', true].includes(wasStarted)) {
        _trackVideoParameters.apply(this, ['resume', Math.round(this.duration)]);
      } else {
        $this.attr('data-started', 'true').data('started', 'true');
        _trackVideoParameters.apply(this, ['start', Math.round(this.duration)]);
      }
    }).on('pause', function () {
      if (this.currentTime < this.duration) {
        trackIndex = _getIndex(this.currentTime, this.duration);
        _trackVideoParameters.apply(this, ['pause', Math.round(this.duration)]);
      }
    }).on('timeupdate', function () {
      const currentDuration = this.duration;
      const currentTime = this.currentTime;
      _calculateProgress.apply(this, [currentTime, currentDuration]);
    }).on('ended', function () {
      trackIndex = _getIndex(this.currentTime, this.duration);
      _trackVideoParameters.apply(this, ['end', Math.round(this.duration)]);
    });
  }
};
