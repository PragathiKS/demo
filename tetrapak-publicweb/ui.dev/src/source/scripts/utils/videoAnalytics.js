import $ from 'jquery';
import { trackAnalytics } from './analytics';
import { logger } from './logger';

let ytRefs = [];

/**
 * Calculates appropriate trackIndex based on percent completion
 * @private
 * @param {number} currentTime Current video time
 * @param {*} totalTime Total video time
 */
function _getIndex(currentTime, totalTime) {
  if (currentTime >= 0) {
    const percentComplete = (currentTime / totalTime) * 100;
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
function _trackVideoParameters(
  videoInteraction,
  videoLength,
  videoTime,
  trackingKey
) {
  const { videoName } = $(this)
    .parents('.js-video-props')
    .data();

  const videomilestone =
    (videoInteraction === '25% milestone' && '25') ||
    (videoInteraction === '50% milestone' && '50') ||
    (videoInteraction === '75% milestone' && '75') ||
    (videoInteraction === 'end' && '100');
  const videoSection =
    (window &&
      window.digitalData &&
      window.digitalData.pageinfo &&
      window.digitalData.pageinfo.siteSection1) ||
    '';
  const trackingObj = {
    videoName,
    videoSection,
    videoLength: new Date(videoLength * 1000).toISOString().substr(11, 8),
    videoTime: new Date(Math.round(videoTime) * 1000)
      .toISOString()
      .substr(11, 8),
    videoInteraction
  };
  if (videomilestone) {
    trackingObj['videomilestone'] = videomilestone;
  }
  if (videoInteraction === 'end') {
    trackingObj['videoend'] = 'true';
  }
  trackAnalytics(trackingObj, 'video', trackingKey, undefined, false, {
    eventType: 'video'
  });
}

/**
 * Calculates progress and log events on completion of 25%, 50%, and 75% completion
 * @private
 * @param {number} currentTime Current video time
 * @param {number} totalTime Total available time
 */
function _calculateProgress(currentTime, totalTime) {
  const percentCompleted = Math.floor((currentTime / totalTime) * 100);
  const $this = $(this);
  let trackIndex = $this.data('trackIndex');
  if (percentCompleted >= 25 && trackIndex === 0) {
    _trackVideoParameters.apply(this, [
      '25% milestone',
      Math.round(totalTime),
      currentTime,
      'videoMilestone'
    ]);
    $this.data('trackIndex', (trackIndex = 1));
  }
  if (percentCompleted >= 50 && trackIndex === 1) {
    _trackVideoParameters.apply(this, [
      '50% milestone',
      Math.round(totalTime),
      currentTime,
      'videoMilestone'
    ]);
    $this.data('trackIndex', (trackIndex = 2));
  }
  if (percentCompleted >= 75 && trackIndex === 2) {
    _trackVideoParameters.apply(this, [
      '75% milestone',
      Math.round(totalTime),
      currentTime,
      'videoMilestone'
    ]);
    $this.data('trackIndex', (trackIndex = 3));
  }
}

/**
 * Pauses play of existing playing videos
 * @public
 * @param {object} el Video elements
 */
export function pauseVideosByReference(el) {
  $(el).each((...args) => {
    const [, ref] = args;
    const $thisRef = $(ref);
    if ($thisRef.hasClass('js-yt-player')) {
      const ytPayerRef = ytRefs.find(ytRef => ytRef.el === ref);
      if (
        ytPayerRef &&
        ytPayerRef.ytPlayer &&
        $thisRef.hasClass('is-playing')
      ) {
        ytPayerRef.ytPlayer.pauseVideo();
      }
    } else if ($thisRef.hasClass('is-playing') && ref.pause) {
      // Assuming HTML5 video
      ref.pause();
    }
  });
}

/**
 * Fires when youtube event state change occurs
 * @private
 * @param {object} thisIns Current instance
 * @param {object} e Event object
 */
function _onStateChange(thisIns, e) {
  const totalTime = thisIns.ytPlayer.getDuration();
  const videoTime = thisIns.ytPlayer.getCurrentTime();
  const $this = $(this);
  $this.data(
    'trackIndex',
    _getIndex.apply(this, [thisIns.ytPlayer.getCurrentTime(), totalTime])
  );
  if (e.data === window.YT.PlayerState.PLAYING) {
    pauseVideosByReference($('.is-playing').not(this));
    $this.removeClass('is-paused is-stopped').addClass('is-playing');
    if (thisIns.intervalRef) {
      clearInterval(thisIns.intervalRef);
    }
    thisIns.intervalRef = window.setInterval(() => {
      _calculateProgress.apply(this, [
        thisIns.ytPlayer.getCurrentTime(),
        totalTime
      ]);
    }, 500);
    const wasStarted = $this.data('started');
    if (['true', true].includes(wasStarted)) {
      _trackVideoParameters.apply(this, [
        'resume',
        Math.round(totalTime),
        videoTime,
        'videoResume'
      ]);
    } else {
      $this.attr('data-started', 'true').data('started', 'true');
      _trackVideoParameters.apply(this, [
        'start',
        Math.round(totalTime),
        videoTime,
        'videoStart'
      ]);
    }
  }
  if (e.data === window.YT.PlayerState.PAUSED) {
    $this.removeClass('is-playing is-stopped').addClass('is-paused');
    if (thisIns.intervalRef) {
      window.clearInterval(thisIns.intervalRef);
    }
    _trackVideoParameters.apply(this, [
      'pause',
      Math.round(totalTime),
      videoTime,
      'videoPause'
    ]);
  }
  if (e.data === window.YT.PlayerState.ENDED) {
    $this.removeClass('is-playing is-paused').addClass('is-stopped');
    if (thisIns.intervalRef) {
      window.clearInterval(thisIns.intervalRef);
    }
    $this.attr('data-started', 'false').data({
      started: 'false',
      trackIndex: 0
    });
    _trackVideoParameters.apply(this, [
      'end',
      Math.round(totalTime),
      totalTime,
      'videoEndButtonClick'
    ]);
  }
}

/**
 * Promise that ensures that youtube API is ready
 * @public
 */
export const ytPromise = new Promise(function(resolve) {
  const scr = document.createElement('script');
  scr.src = 'https://www.youtube.com/iframe_api';
  const firstScript = document.getElementsByTagName('script')[0];
  firstScript.parentNode.insertBefore(scr, firstScript);
  window.onYouTubeIframeAPIReady = function(...args) {
    resolve(args);
  };
});

/**
 * Initializes youtube player API for existing videos
 * @public
 */
export function initializeYoutubePlayer() {
  const $ytVideos = $('.js-yt-player').not('.video-init');
  logger.log(`[Youtube]: ${$ytVideos.length} youtube video(s) initialized`);
  $ytVideos.each(function() {
    const thisIns = {};
    thisIns.el = this;
    if (window.YT && window.YT.Player) {
      thisIns.ytPlayer = new window.YT.Player(this, {
        events: {
          onStateChange: _onStateChange.bind(this, thisIns)
        }
      });
      if (!ytRefs.find(ytRef => ytRef.el === thisIns.el)) {
        ytRefs.push(thisIns);
      }
      $(this).addClass('video-init');
    }
  });
}

/**
 * Clears youtube API instances
 * @public
 * @param {object|string} ref Reference selector
 */
export function removeYTReferences(ref) {
  if (!ref) {
    logger.log(`[Youtube]: ${ytRefs.length} reference(s) removed`);
    ytRefs.length = 0;
    $('.js-yt-player').removeClass('video-init');
    return;
  }
  const initialLength = ytRefs.length;
  $(ref).each(function() {
    const $this = $(this);
    if ($this.hasClass('js-yt-player') && $this.hasClass('video-init')) {
      ytRefs = ytRefs.filter(ytRef => ytRef.el !== this);
      $this.removeClass('video-init');
    }
  });
  const finalLength = ytRefs.length;
  logger.log(`[Youtube]: ${initialLength - finalLength} reference(s) removed`);
}

/**
 * Fires when HTML5 video is played
 * @private
 */
function _onDAMPlay() {
  const $this = $(this);
  pauseVideosByReference($('.is-playing').not(this));
  $this.removeClass('is-paused is-stopped').addClass('is-playing');
  const wasStarted = $this.data('started');
  $this.data(
    'trackIndex',
    _getIndex.apply(this, [this.currentTime, this.duration])
  );
  if (['true', true].includes(wasStarted)) {
    _trackVideoParameters.apply(this, [
      'resume',
      Math.round(this.duration),
      this.currentTime,
      'videoResume'
    ]);
  } else {
    $this.attr('data-started', 'true').data('started', 'true');
    _trackVideoParameters.apply(this, [
      'start',
      Math.round(this.duration),
      this.currentTime,
      'videoStart'
    ]);
  }
}

/**
 * Fires when HTML5 video is paused
 * @private
 */
function _onDAMPause() {
  if (this.currentTime < this.duration) {
    $(this)
      .data(
        'trackIndex',
        _getIndex.apply(this, [this.currentTime, this.duration])
      )
      .removeClass('is-stopped is-playing')
      .addClass('is-paused');
    _trackVideoParameters.apply(this, [
      'pause',
      Math.round(this.duration),
      this.currentTime,
      'videoPause'
    ]);
  }
}

/**
 * Fires when HTML5 video time updates
 * @private
 */
function _onTimeUpdate() {
  const currentDuration = this.duration;
  const currentTime = this.currentTime;
  _calculateProgress.apply(this, [currentTime, currentDuration]);
}

/**
 * Fires when youtube video ends
 */
function _onEnd() {
  $(this)
    .attr('data-started', 'false')
    .data({
      started: 'false',
      trackIndex: 0
    })
    .removeClass('is-playing is-paused')
    .addClass('is-stopped');
  _trackVideoParameters.apply(this, [
    'end',
    Math.round(this.duration),
    this.currentTime,
    'videoEndButtonClick'
  ]);
}

/**
 * Initializes DAM player for existing videos
 * @public
 */
export function initializeDAMPlayer() {
  const $damVideos = $('.js-dam-player').not('.video-init');
  logger.log(`[HTML5 video]: ${$damVideos.length} video(s) initialized`);
  $damVideos
    .addClass('video-init')
    .on('play', _onDAMPlay)
    .on('pause', _onDAMPause)
    .on('timeupdate', _onTimeUpdate)
    .on('ended', _onEnd);
}

/**
 * Default export
 */
export default {
  init() {
    ytPromise.then(() => {
      initializeYoutubePlayer();
    });
    initializeDAMPlayer();
  }
};
