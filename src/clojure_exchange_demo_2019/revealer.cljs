(ns clojure-exchange-demo-2019.revealer
  (:require [hx.react :refer [defnc]]
            [hx.hooks :as hooks]
            [clojure-exchange-demo-2019.keyboard :as keyboard]
            [clojure-exchange-demo-2019.style :refer [s]]
            [clojure-exchange-demo-2019.dimensions :as dimensions]
            [cljs-bean.core :refer [->clj]]
            ["react" :as react]
            ["react-native-reanimated" :default Animated :refer
             [Easing.inOut Easing.ease]]
            ["react-native" :as rn]
            [goog.math :as math]))


(def AnimatedValue (.-Value Animated))
(def AnimatedView (.-View Animated))
(def AnimatedScrollView (.-ScrollView Animated))
(def animated-timing (.-timing Animated))

;; A revealer is a scroll view that can reveal an element.
;; It creates a context with a single 'reveal' function
;; It optionally takes a reveal-offset so that the
;; revealed element isn't right at the top.

(def context (react/createContext))

(defnc Revealer
  [props]
  (let [{:keys [children reveal-offset]
         :or {reveal-offset 50}} props
        inner (react/useRef)
        outer (react/useRef)
        [visible _] (keyboard/useKeyboard)
        reveal (react/useCallback
                 (fn [^js ref]
                   (js/setTimeout
                     (fn []
                       (when-let [r (.-current ref)]
                         (.measureLayout
                           r
                           (rn/findNodeHandle (.-current inner))
                           (fn [x y]
                             (.scrollTo (.getNode (.-current outer)) #js {:y (max (- y reveal-offset) 0)
                                                                          :animated true})))))
                     200)))
        keyboard-padding (react/useRef (AnimatedValue. 0))]
    (hooks/useEffect
      (fn []
        (let [anim (animated-timing
                     (.-current keyboard-padding)
                     #js {:toValue (if visible keyboard/keyboard-height 0)
                          :duration 200
                          :easing (Easing.inOut Easing.ease)})]
          (.start anim)))
      [visible])
    [:provider {:context context
                :value reveal}
     [AnimatedScrollView
      (merge
        (dissoc props :children :reveal-offset)
        {:ref outer
         :keyboardShouldPersistTaps "handled"
         :showsVerticalScrollIndicator false
         :scrollEventThrottle 1})
      [rn/View {:ref inner}
       children]
      [AnimatedView
       {:style {:height (.-current keyboard-padding)}}]]]))

;; This text input will reveal itself
(defnc TextInput
  [props]
  (let [{:keys [onFocus]} props
        reveal (react/useContext context)
        ref (react/useRef)]
    [rn/TextInput
     (merge
       {:underlineColorAndroid "transparent"
        :autoCorrect false
        :autoCapitalize "none"
        :style (s [:br1 :ui1 :bg-ui1 :pa2 :f4 :w100])}
       props
       {:ref ref
        :onFocus (fn [e]
                   (reveal ref)
                   (when onFocus
                     (onFocus e)))})]))

(defnc MultilineTextInput
  [props]
  (let [{:keys [onFocus]} props
        reveal (react/useContext context)
        ref (react/useRef)]
    [rn/TextInput
     (merge
       {:underlineColorAndroid "transparent"
        :autoCorrect false
        :multiline true
        :numberOfLines 20
        :autoCapitalize "none"
        :style (s [:br1 :h10 :ui1 :bg-brand1 :pa2 :f4 :w100])}
       props
       {:ref ref
        :onFocus (fn [e]
                   (reveal ref)
                   (when onFocus
                     (onFocus e)))})]))
