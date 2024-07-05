(ns shrew.settings
  (:require [shrew.request :as request]
            [shrew.link :as link]
            [rum.core :refer [defc] :as rum]
            [reagent.cookies :as cookies]
            [clojure.string :as string]))

(def !settings (atom {}))

(defn fetch []
  (request/get "/settings"
               (cookies/get "admin")
               #(reset! !settings %)))

(defn submit [event]
  (.preventDefault event)
  (let [data (->> (.getElementById js/document "form")
                  (js/FormData.)
                  (.fromEntries js/Object))
        event  (.-event  data)
        points (.-points data)]
    (request/post "/settings"
                  {:event  event
                   :points (string/split points ",")}
                  (cookies/get "admin"))
    (link/navigate "/")))

(defc view < rum/reactive []
  [:<> [:header [[:h1 "Settings"]
                 [:p  "Global settings for Shrew.
                       Point types is a list separated by commas."]]]
       [:section [:form {:id "form"}
                        [:label {:for "event"} "Event Name"]
                        [:input {:id   "event"
                                 :name "event"
                                 :type "text"
                                 :defaultValue (-> (rum/react !settings)
                                                   (get :event))}]
                        [:label {:for "points"} "Point Types"]
                        [:input {:id   "points"
                                 :name "points"
                                 :type "text"
                                 :defaultValue (->> (rum/react !settings)
                                                    (:points)
                                                    (string/join ","))}]
                        [:input {:value "Save" :type "submit" :on-click submit}]]]])
