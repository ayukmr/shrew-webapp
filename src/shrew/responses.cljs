(ns shrew.responses
  (:require [shrew.request :as request]
            [rum.core :refer [defc] :as rum]
            [reagent.cookies :as cookies]
            [clojure.string :as string]))

(def !responses (atom []))
(def !type (atom :pre))

(defn fetch []
  (request/get "/responses"
               (cookies/get "admin")
               #(reset! !responses %)))

(defc view < rum/reactive []
  [:<> [:header [:h1 "Responses"]
                [:p  "Written responses during games. Sorted by team number."]]
       [:section [:label {:for "type"} "Question Type"]
                 [:select {:id "type"
                           :on-change #(reset! !type (keyword (.. % -target -value)))}
                          (for [type #{:pre :post}
                                :let [str-type (subs (str type) 1)]]
                            [:option {:key str-type :value str-type}
                                     (string/capitalize str-type)])]]
       (for [[team responses]
             (->> (get (rum/react !responses) (rum/react !type))
                  (group-by :team)
                  (seq))]
         [:section [:h2 team]
                   [:table [:thead [:tr [:th "Event"]
                                        [:th "Match"]
                                        [:th "Question"]
                                        [:th "Response"]]]
                           [:tbody (for [response (sort-by :question responses)]
                                     [:tr {:key (get response :id)}
                                          (for [field ((juxt :event
                                                             :match
                                                             :question
                                                             :response)
                                                       response)]
                                            [:td field])])]]])])
