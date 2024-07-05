(ns shrew.scout.core
  (:require [shrew.scout.info :as info]
            [shrew.scout.points :as points]
            [shrew.scout.responses :as responses]
            [shrew.link :as link]
            [shrew.request :as request]
            [rum.core :refer [defc] :as rum]
            [reagent.cookies :as cookies]))

(def !settings  (atom {}))
(def !questions (atom {}))

(def !page (atom 0))
(def pages [(info/view !settings)
            (responses/view !questions :pre)
            (points/view    !settings info/!team info/!match)
            (responses/view !questions :post)])

(defn fetch []
  (request/get "/settings"
               (or (cookies/get "scout") (cookies/get "admin"))
               #(reset! !settings %))
  (request/get "/questions"
               (or (cookies/get "scout") (cookies/get "admin"))
               #(reset! !questions %)))

(defc continue-button < rum/reactive []
  [:button#continue {:on-click #(case @!page
                                  3 (do
                                      (points/submit @info/!team @info/!match)
                                      (responses/submit @info/!team @info/!match)
                                      (js/alert "Submitted scout!")
                                      (reset! !settings {})
                                      (reset! !questions {})
                                      (reset! !page 0)
                                      (reset! info/!team nil)
                                      (reset! info/!match nil)
                                      (link/navigate "/"))
                                  0 (if (and (seq @info/!team) (seq @info/!match))
                                      (swap! !page inc)
                                      (js/alert "Input team number and match number!"))
                                  (swap! !page inc))}
                    (if (= (rum/react !page) 3)
                      "Submit"
                      "Continue")])

(defc back-button < rum/reactive []
  (when-not (= (rum/react !page) 0)
    [:button#continue {:on-click #(swap! !page dec)} "Back"]))

(defc view < rum/reactive []
  [:<> [:div#scout (get pages (rum/react !page))
       [:section (continue-button) (back-button)]]])
