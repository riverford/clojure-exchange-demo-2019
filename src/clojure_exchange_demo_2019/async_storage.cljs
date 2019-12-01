(ns clojure-exchange-demo-2019.async-storage
  (:require ["react-native" :refer [AsyncStorage]]
            ["react" :as react]
            [hx.hooks :as hooks]
            [cljs.reader :as edn]))

(defn useAsyncStorage
  "Given a key, returns a pair of value, setValue that will
  maintain the value in local storage"
  [opts]
  (let [{:keys [serialize deserialize default k]
         :or {serialize pr-str
              deserialize edn/read-string}} opts
        [value setValue] (hooks/useState default)]
    (hooks/useEffect
      (fn []
        (-> (.getItem AsyncStorage k)
            (.then (fn [val]
                     (setValue (deserialize val))))))
      [k])
    [value (fn [f-or-v]
             (let [val (if (fn? f-or-v)
                         (f-or-v value)
                         f-or-v)]
               (-> (.setItem AsyncStorage k (serialize val))
                   (.then (fn [_]
                            (setValue val))))))]))

(defn removeKey
  "Remove a key"
  [k]
  (.removeItem AsyncStorage k))
