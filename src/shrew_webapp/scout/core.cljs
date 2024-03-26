(ns shrew-webapp.scout.core
  (:require [shrew-webapp.scout.info :as info]
            [shrew-webapp.scout.points :as points]
            [shrew-webapp.scout.responses :as responses]
            [shrew-webapp.request :as request]
            [rum.core :refer [defc] :as rum]
            [reagent.cookies :as cookies]))

(def !settings (atom {}))

(def !page (atom 0))
(def pages [(info/view !settings)
            (points/view    !settings info/!team info/!match)
            (responses/view !settings info/!team info/!match)])

(defc continue-button < rum/reactive []
  [:button#continue {:on-click #(case @!page
                                  2 (do
                                      (points/submit info/!team info/!match)
                                      (responses/submit info/!team info/!match)
                                      (js/alert "Submitted scout!"))
                                  0 (if (and (seq @info/!team) (seq @info/!match))
                                      (swap! !page inc)
                                      (js/alert "Input team number and match number!"))
                                  (swap! !page inc))}
                    (if (= (rum/react !page) 2)
                      "Submit"
                      "Continue")])

(defc back-button < rum/reactive []
  (when (not (= (rum/react !page) 0))
             [:button#continue {:on-click #(swap! !page dec)} "Back"]))

(defc view < rum/reactive []
  (request/get "/settings"
               (cookies/get "scout")
               #(reset! !settings %))
  [:section#scout (get pages (rum/react !page))
                  (continue-button)
                  (back-button)])
