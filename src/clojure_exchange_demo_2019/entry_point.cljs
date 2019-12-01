(ns clojure-exchange-demo-2019.entry-point
  (:require
   [shadow.expo :as shadow-expo]
   [hx.react :as hx :refer [defnc]]
   [hx.hooks :as hooks]
   [clojure-exchange-demo-2019.style :refer [s]]
   [clojure-exchange-demo-2019.revealer :as r :refer [Revealer]]
   [clojure-exchange-demo-2019.buttons :as b]
   [clojure-exchange-demo-2019.async-storage :as async-storage]
   [clojure-exchange-demo-2019.dream-questions :as q]
   [cljs-bean.core :refer [->clj]]
   ["date-fns" :as d]
   ["@expo/vector-icons" :refer [Entypo Ionicons]]
   ["react" :as react]
   ["react-native" :as rn]
   ["@react-navigation/core" :refer [useNavigation]]
   ["@react-navigation/native" :refer [NavigationNativeContainer]]
   ["@react-navigation/stack" :refer [createStackNavigator]]
   ["react-native-safe-area-context" :refer [useSafeArea]]))

(def diary-context
  (react/createContext))

(defn useDiary
  "Not ideal storing this all in one lot, but just a demo"
  []
  (let [[diary setDiary] (async-storage/useAsyncStorage {:k "diary2"})]
    {:diary diary
     :record-dream (fn [dream]
                     (setDiary (conj (or diary []) dream)))}))

(defnc HelpButtonOverlay
  []
  (let [navigation (useNavigation)]
    [rn/View {:style (s [:absolute-fill :aife :jcfe])
              :pointerEvents "box-none"}
     [rn/View {:style (s [:pa3])}
      [b/HelpButton {:onPress (fn []
                                (.navigate navigation "dream-questions"))}]]]))

(defnc TopRightCloseButtonLight
  []
  (let [insets (useSafeArea)
        navigation (useNavigation)]
    [rn/View {:style (merge
                       (s [:absolute-fill :aife :jcfs :pa2])
                       {:paddingTop (.-top insets)})
              :pointerEvents "box-none"}
     [b/CloseButtonLight {:onPress (fn []
                                     (.goBack navigation))}]]))

(defnc TopRightCloseButtonDark
  []
  (let [insets (useSafeArea)
        navigation (useNavigation)]
    [rn/View {:style (merge
                       (s [:absolute-fill :aife :jcfs :pa2])
                       {:paddingTop (.-top insets)})
              :pointerEvents "box-none"}
     [b/CloseButtonDark {:onPress (fn []
                                    (.goBack navigation))}]]))

(def Stack (createStackNavigator))

(defnc AddDreamCta
  []
  (let [navigation (useNavigation)]
    [rn/View {:style (s [:pa4 :aic])}
     [b/TextButton
      {:onPress (fn []
                  (.navigate navigation "add-dream"))
       :text "Record Dream"}]]))

(defnc DreamHeader
  []
  [rn/View {:style [:aic :fg1]}
   [rn/Text {:style (s [:ui1 :f3 :tc :pb3])}
    "Dream Diary"]
   ])

(defn format-date
  "Formats a date & time using date-fns"
  [d]
  (d/format d "do MMMM yyyy HH':'mm"))

(defnc AddDream
  []
  (let [insets (useSafeArea)
        [state setState] (react/useState {:recorded-at (js/Date.)
                                          :content ""
                                          :analysis ""})
        {:keys [diary record-dream]} (react/useContext diary-context)
        navigation (useNavigation)]
    (let [{:keys [recorded-at dream notes]} state]
      [rn/View {:style (merge {:paddingTop (.-top insets)}
                              (s [:absolute-fill]))}
       [rn/Text {:style (s [:ui0 :f3 :tc :pb3])}
        "Add Dream"]
       [r/Revealer {:style (s [:ph2 :pv3])}
        [rn/Text {:style (s [:ui0 :f4 :pb2])}
         "Date recorded"]
        [rn/Text {:style (s [:ui0 :fwb :f5 :pb3])}
         (format-date recorded-at)]
        [rn/Text {:style (s [:ui0 :f4 :pb2])}
         "Dream content"]
        [rn/View {:style (s [:pb5])}
         [r/MultilineTextInput
          {:numberOfLines 10
           :multiline true
           :onChangeText (fn [text]
                           (setState (fn [state]
                                       (assoc state :content text))))}]]
        [rn/Text {:style (s [:ui0 :f4 :pb2])}
         "Dream analysis"]
        [rn/View {:style (s [:pb5])}
         [r/MultilineTextInput
          {:numberOfLines 10
           :multiline true
           :onChangeText (fn [text]
                           (setState (fn [state]
                                       (assoc state :analysis text))))}]]
        [rn/View {:style (s [:aic :pb6 :jcc])}
         [b/TextButton2
          {:onPress (fn []
                      (record-dream state)
                      (.goBack navigation))
           :text "Save Dream"}]]]
       [TopRightCloseButtonDark]
       [HelpButtonOverlay]])))

(defnc EmptyState
  []
  [rn/View {:style (s [:jcc :aic])}
   [rn/View {:style (s [:pb2 :aic :jcc])}
    [Ionicons {:name "ios-cloudy-night"
               :size 150
               :style (s [:ui1])}]]
   [rn/Text {:style (s [:ui1 :f4 :tc :pb1])}
    "No dreams yet?"]
   [rn/Text {:style (s [:ui1 :f5 :tc])}
    "Crack out the cocoa, \n it's time for a good old snooze..."]])

(defnc Dream
  [props]
  (let [{:keys [recorded-at content analysis]} props]
    [rn/View
     [rn/Text {:style (s [:ui1 :f5 :pb2 :fwb])}
      (format-date recorded-at)]
     [rn/View {:style (s [:pl2 :pb4])}
      [rn/Text {:style (s [:ui1 :f5 :pb2])}
       content]
      [rn/Text  {:style (s [:ui1 :f5])}
       analysis]]]))

(defnc DreamsList
  []
  (let [{:keys [diary]} (react/useContext diary-context)]
    [rn/FlatList
     {:showsVerticalScrollIndicator false
      :keyExtractor (fn [item]
                      (let [{:keys [recorded-at]} (->clj item)]
                        (str recorded-at)))
      :data (some-> diary rseq to-array)
      :renderItem (fn [^js obj]
                    (let [{:keys [item]} (->clj obj)]
                      (hx/f [Dream item])))}]))

(defnc Diary
  [props]
  (let [insets (useSafeArea)
        {:keys [diary]} (react/useContext diary-context)]
    [rn/View {:style (merge (s [:absolute-fill :bg-brand0 :ph2])
                            {:paddingTop (.-top insets)})}
     [DreamHeader]
     (if (seq diary)
       [DreamsList]
       [EmptyState])
     [AddDreamCta]
     [HelpButtonOverlay]]))

(defnc DreamQuestion
  [props]
  (let [{:keys [question notes]} props]
    [rn/View
     [rn/Text {:style (s [:ui1 :f5 :pb2])}
      question]
     [rn/Text {:style (s [:ui1 :f6 :pb4])}
      notes]]))

(defnc DreamQuestions
  [props]
  (let [insets (useSafeArea)]
    [rn/View {:style (merge
                       (s [:bg-ui0 :absolute-fill])
                       {:paddingTop (.-top insets)})}
     [rn/Text {:style (s [:ui1 :f3 :tc])}
      "Dream Questions"]
     [rn/ScrollView {:style (s [:fg1 :pa3])}
      (for [question q/dream-questions]
        [DreamQuestion question])]
     [TopRightCloseButtonLight]]))

(defnc App
  []
  (let [ref (react/useRef)]
    [NavigationNativeContainer
     [:provider {:context diary-context
                 :value (useDiary)}
      [Stack.Navigator {:initialRouteName "diary"
                        :headerMode "none"
                        :mode "modal"}
       [Stack.Screen {:name "diary"
                      :component Diary}]
       [Stack.Screen {:name "dream-questions"
                      :component DreamQuestions}]
       [Stack.Screen {:name "add-dream"
                      :component AddDream}]]]]))

(defn start
  "Entry point for ui, called every hot reload"
  {:dev/after-load true}
  []
  (shadow-expo/render-root (hx/f [App])))

(defn init
  "Initialization function, called once at app start up"
  []
  (start))
