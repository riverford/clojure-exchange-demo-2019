(ns clojure-exchange-demo-2019.entry-point
  (:require
   [shadow.expo :as shadow-expo]
   [hx.react :as hx :refer [defnc]]
   [clojure-exchange-demo-2019.style :refer [s]]
   [clojure-exchange-demo-2019.revealer :as r :refer [Revealer]]
   ["react" :as react]
   ["react-native" :as rn]))

(defnc Field
  []
  [rn/Text {:style (s [:ui1 :f4])} "hey"])

(defnc Button
  []
  (let [reveal (react/useContext r/context)
        ref (react/useRef)]
    [rn/TouchableOpacity {:ref ref
                          :onPress #(reveal ref)}
     [rn/Text "press me"]]))

(defnc TextInput
  []
  [r/RevealingTextInput
   {:underlineColorAndroid "transparent"
    :autoCorrect false
    :autoCapitalize "none"
    :style (s [:br1 :ui0 :bg-ui1 :pv3 :ph3 :mb2 :f4 :h3 :w100])}])

(defnc App
  []
  (let [ref (react/useRef)]
    [rn/View {:style (s [:bg-brand0 :fg1 :pt5 :ph2])}
     [rn/Text {:style (s [:ui1 :f4])}
      "Hello Clojure Exchange 2020!"]
     [Revealer {:style (s [:fg1])}
      (for [i (range 20)]
        [TextInput])
      [Field]
      [Button]]]))

(defn start
  "Entry point for ui, called every hot reload"
  {:dev/after-load true}
  []
  (shadow-expo/render-root (hx/f [App])))

(defn init
  "Initialization function, called once at app start up"
  []
  (start))
