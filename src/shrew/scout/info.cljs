(ns shrew.scout.info
  (:require [rum.core :refer [defc] :as rum]))

(def !team  (atom nil))
(def !match (atom nil))

(defc view [!settings]
  [:<> [:h1 "Match Info"]
       [:form {:id "form"}
              [:label {:for "team"} "Team Number"]
              [:input {:id   "team"
                       :name "team"
                       :type "number"
                       :defaultValue @!team
                       :on-change #(reset! !team (.. % -target -value))}]
              [:label {:for "match"} "Match Number"]
              [:input {:id   "match"
                       :name "match"
                       :type "number"
                       :defaultValue @!match
                       :on-change #(reset! !match (.. % -target -value))}]]])
