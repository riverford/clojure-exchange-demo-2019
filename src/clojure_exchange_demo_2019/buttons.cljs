(ns clojure-exchange-demo-2019.buttons
  (:require ["react-native" :as rn]
            [hx.react :as hx :refer [defnc]]
            [clojure-exchange-demo-2019.style :refer [s]]
            ["@expo/vector-icons" :refer [Entypo]]))

(defnc HelpButton
  [props]
  [rn/TouchableOpacity
   (merge props {:style (merge (s [:sh1 :h3 :w3 :br50 :bg-ui1 :aic :jcc]))})
   [rn/Text {:style (s [:f3])} "?"]])

(defnc TextButton
  [props]
  (let [{:keys [text]} props]
    [rn/TouchableOpacity
     (-> props
         (dissoc :text)
         (merge {:style (s [:sh1 :pv2 :ph3 :br2 :bg-ui1 :aic :jcc])}))
     [rn/Text {:style (s [:f4 :tc])} text]]))

(defnc TextButton2
  [props]
  (let [{:keys [text]} props]
    [rn/TouchableOpacity
     (-> props
         (dissoc :text)
         (merge {:style (s [:sh1 :pv2 :ph3 :br2 :bg-brand0 :aic :jcc])}))
     [rn/Text {:style (s [:f4 :ui1 :tc])} text]]))

(defnc CloseButtonDark
  [props]
  [rn/TouchableOpacity props
   [Entypo {:name "cross"
            :style (s [:ui0])
            :size 30}]])

(defnc CloseButtonLight
  [props]
  [rn/TouchableOpacity props
   [Entypo {:name "cross"
            :style (s [:ui1])
            :size 30}]])
