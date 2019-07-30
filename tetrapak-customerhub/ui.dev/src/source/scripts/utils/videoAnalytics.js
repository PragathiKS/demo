import $ from 'jquery';
import 'core-js/features/promise';
import 'core-js/features/array/includes';
import 'core-js/features/array/find';
import { trackAnalytics } from './analytics';

let YT = null;

const ytRefs = [];

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
  const $this = $(this);
  let trackIndex = $this.data('trackIndex');
  if (percentCompleted >= 25 && trackIndex === 0) {
    _trackVideoParameters.apply(this, ['25%', Math.round(totalTime)]);
    $this.data('trackIndex', (trackIndex = 1));
  }
  if (percentCompleted >= 50 && trackIndex === 1) {
    _trackVideoParameters.apply(this, ['50%', Math.round(totalTime)]);
    $this.data('trackIndex', (trackIndex = 2));
  }
  if (percentCompleted >= 75 && trackIndex === 2) {
    _trackVideoParameters.apply(this, ['75%', Math.round(totalTime)]);
    $this.data('trackIndex', (trackIndex = 3));
  }
}

/**
 * Pauses play of existing playing videos
 * @public
 * @param {object} el Video elements
 */
export const pauseVideosByReference = (el) => {
  $(el).each((...args) => {
    const [, ref] = args;
    const $thisRef = $(ref);
    if ($thisRef.hasClass('js-yt-player')) {
      const ytPayerRef = ytRefs.find(ytRef => ytRef.el === ref);
      if (ytPayerRef && ytPayerRef.ytPlayer && $thisRef.hasClass('is-playing')) {
        ytPayerRef.ytPlayer.pauseVideo();
      }
    } else if ($thisRef.hasClass('is-playing') && ref.pause) {
      // Assuming HTML5 video
      ref.pause();
    }
  });
};

/**
 * Fires when youtube event state change occurs
 * @private
 * @param {object} thisIns Current instance
 * @param {object} e Event object
 */
function _onStateChange(thisIns, e) {
  const totalTime = thisIns.ytPlayer.getDuration();
  const $this = $(this);
  $this.data('trackIndex', _getIndex.apply(this, [thisIns.ytPlayer.getCurrentTime(), totalTime]));
  if (e.data === YT.PlayerState.PLAYING) {
    pauseVideosByReference($('.is-playing').not(this));
    $this.removeClass('is-paused is-stopped').addClass('is-playing');
    if (thisIns.intervalRef) {
      clearInterval(thisIns.intervalRef);
    }
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
    $this.removeClass('is-playing is-stopped').addClass('is-paused');
    if (thisIns.intervalRef) {
      window.clearInterval(thisIns.intervalRef);
    }
    _trackVideoParameters.apply(this, ['pause', Math.round(totalTime)]);
  }
  if (e.data === YT.PlayerState.ENDED) {
    $this.removeClass('is-playing is-paused').addClass('is-stopped');
    if (thisIns.intervalRef) {
      window.clearInterval(thisIns.intervalRef);
    }
    $this.attr('data-started', 'false').data({
      started: 'false',
      trackIndex: 0
    });
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
        thisIns.el = this;
        thisIns.ytPlayer = new YT.Player(this, {
          events: {
            onStateChange: _onStateChange.bind(this, thisIns)
          }
        });
        ytRefs.push(thisIns);
      });
    });
    $('.js-dam-player').on('play', function () {
      const $this = $(this);
      pauseVideosByReference($('.is-playing').not(this));
      $this.removeClass('is-paused is-stopped').addClass('is-playing');
      const wasStarted = $this.data('started');
      $this.data('trackIndex', _getIndex.apply(this, [this.currentTime, this.duration]));
      if (['true', true].includes(wasStarted)) {
        _trackVideoParameters.apply(this, ['resume', Math.round(this.duration)]);
      } else {
        $this.attr('data-started', 'true').data('started', 'true');
        _trackVideoParameters.apply(this, ['start', Math.round(this.duration)]);
      }
    }).on('pause', function () {
      if (this.currentTime < this.duration) {
        $(this).data('trackIndex', _getIndex.apply(this, [this.currentTime, this.duration])).removeClass('is-stopped is-playing').addClass('is-paused');
        _trackVideoParameters.apply(this, ['pause', Math.round(this.duration)]);
      }
    }).on('timeupdate', function () {
      const currentDuration = this.duration;
      const currentTime = this.currentTime;
      _calculateProgress.apply(this, [currentTime, currentDuration]);
    }).on('ended', function () {
      $(this).attr('data-started', 'false').data({
        started: 'false',
        trackIndex: 0
      }).removeClass('is-playing is-paused').addClass('is-stopped');
      _trackVideoParameters.apply(this, ['end', Math.round(this.duration)]);
    });
  }
};
