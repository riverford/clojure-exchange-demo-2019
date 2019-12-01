(ns clojure-exchange-demo-2019.keyboard
  (:require [hx.react :refer [defnc]]
            [hx.hooks :as hooks]
            ["react" :as react]
            ["react-native" :as rn :refer [Keyboard]]))

(def show-event "keyboardWillShow")
(def hide-event "keyboardWillHide")
(def keyboard-height 320)

(defn useKeyboard
  "Returns a pair of [visible dismiss], where state
  reflects the current visibility and height of the keyboard.
  "
  []
  (let [[visible setVisible] (react/useState false)
        dismiss (react/useCallback
                  (fn []
                    (.dismiss Keyboard)
                    (setVisible false)))
        onShow (react/useCallback (fn [^js e]
                                    (println "showing")
                                    (setVisible true)))
        onHide (react/useCallback (fn [_]
                                    (println "hiding")
                                    (setVisible false)))]
    (hooks/useEffect
      (fn []
        (.addListener Keyboard show-event onShow)
        (.addListener Keyboard hide-event onHide)
        (fn []
          (.removeListener Keyboard show-event onShow)
          (.removeListener Keyboard hide-event onHide))))
    [visible dismiss]))
