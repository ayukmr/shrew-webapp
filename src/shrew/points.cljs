(ns shrew.points
  (:require [shrew.request :as request]
            [rum.core :refer [defc] :as rum]
            [reagent.cookies :as cookies]))

(def !points (atom []))

(defn fetch []
  (request/get "/points"
               (cookies/get "admin")
               #(reset! !points %)))

(defc view < rum/reactive []
  [:<> [:header [:h1 "Points"]
                [:p  "Points scored during games. Sorted by team number."]]
       (for [[team points] (->> (rum/react !points)
                                (group-by :team)
                                (seq))]
         [:section [:h2 team]
                   [:table [:thead [:tr [:th "Event"]
                                        [:th "Match"]
                                        [:th "Move"]
                                        [:th "Intake"]
                                        [:th "Outtake"]
                                        [:th "Point"]]]
                           [:tbody (for [point (sort-by :point points)]
                                     [:tr {:key (get point :id)}
                                          (for [field ((juxt :event
                                                             :match
                                                             :move
                                                             :intake
                                                             :outtake
                                                             :point)
                                                       point)]
                                            [:td field])])]]])])
