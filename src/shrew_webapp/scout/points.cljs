(ns shrew-webapp.scout.points
  (:require [shrew-webapp.request :as request]
            [rum.core :refer [defc] :as rum]
            [reagent.cookies :as cookies]
            [reagent.format :refer [format]]
            [clojure.string :as string]))

(def !times  (atom {:move 0 :intake 0 :outtake 0}))
(def !points (atom []))

(def !interval-id (atom nil))

(js/addEventListener "pointerup"
                     #(do (js/clearInterval @!interval-id)
                          (reset! !interval-id nil)))

(defn hold-interval [id]
  (when (some? @!interval-id)
    (js/clearInterval @!interval-id)
    (reset! !interval-id nil))
  (reset! !interval-id
          (js/setInterval #(swap! !times update id + 0.1) 100)))

(defn score-point [id]
  (swap! !points conj (merge {:point id} @!times))
  (reset! !times {:move 0 :intake 0 :outtake 0}))

(defn submit [!team !match]
  (request/post "/points"
                {:scouting @!team
                 :match    @!match
                 :points   @!points}
                (cookies/get "scout")))

(defc hold-button-label < rum/reactive [id]
  [:span (format "%.1f"
                 (get (rum/react !times) id))])

(defc hold-button [title id]
  [:.group (hold-button-label id)
           [:br]
           [:button {:on-pointer-down #(hold-interval id)} title]])

(defc point-button [point]
  [:button {:on-click #(score-point point)} point])

(defc view < rum/reactive [!settings !team !match]
  [:#points [:.buttons (hold-button "In"   :intake)
                       (hold-button "Move" :move)
                       (hold-button "Out"  :outtake)]
            [:h1#team (str (rum/react !match) " • " (rum/react !team))]
            [:.buttons (for [point (get (rum/react !settings) :points)]
                         [:<> {:key point}
                              (point-button point)])]])
