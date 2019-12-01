(ns clojure-exchange-demo-2019.dimensions
  (:require
   ["react-native" :refer [Dimensions]]
   [cljs-bean.core :refer [->clj]]
   [hx.hooks :as hooks]))

(defn useDimensions
  "A hook that extracts the current window dimenions,
   returns state that will update whenever the dimensions change"
  []
  (let [[dimensions setDimensions] (hooks/useState (->clj (.get Dimensions "window")))
        onChange (fn [^js obj]
                   (setDimensions (:window (->clj obj))))]
    (hooks/useEffect
      (fn []
        (.addEventListener Dimensions "change" onChange)
        (fn []
          (.removeEventListener Dimensions "change" onChange)))
      [])
    dimensions))
