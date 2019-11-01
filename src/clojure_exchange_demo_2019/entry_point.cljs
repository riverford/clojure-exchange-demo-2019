(ns clojure-exchange-demo-2019.entry-point
  (:require
   [shadow.expo :as shadow-expo]
   [hx.react :as hx :refer [defnc]]
   ["react-native" :as rn]))

(defnc App
  []
  [rn/View {:style {:backgroundColor "purple"
                    :alignItems "center"
                    :flexGrow 1
                    :justifyContent "center"}}
   [rn/Text {:style {:color "white"
                     :fontSize 20}}
    "Hello Clojure Exchange 2019!"]])

(defn start
  "Entry point for ui, called every hot reload"
  {:dev/after-load true}
  []
  (shadow-expo/render-root (hx/f [App])))

(defn init
  "Initialization function, called once at app start up"
  []
  (start))
