(ns shrew-webapp.responses
  (:require [shrew-webapp.request :as request]
            [rum.core :refer [defc] :as rum]
            [reagent.cookies :as cookies]))

(def !responses (atom []))

(defc view < rum/reactive []
  (request/get "/responses"
               (cookies/get "admin")
               #(reset! !responses %))
  [:<>
       [:header [:h1 "Responses"]
                [:p  "Written responses after games. Sorted by team number."]]
       (for [[team responses]
             (->> (rum/react !responses)
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
