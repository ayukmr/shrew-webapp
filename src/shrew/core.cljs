(ns shrew.core
  (:require [shrew.home :as home]
            [shrew.auth :as auth]
            [shrew.scout.core :as scout]
            [shrew.points :as points]
            [shrew.responses :as responses]
            [shrew.settings :as settings]
            [shrew.questions :as questions]
            [secretary.core :refer-macros [defroute] :as secretary]
            [rum.core :as rum]))

(enable-console-print!)

(defn render [view]
  (rum/mount view (.getElementById js/document "root")))

(defroute "/" []
  (render (home/view)))

(defroute "/auth" []
  (render (auth/view)))

(defroute "/scout" []
  (->> #(do
          (scout/fetch)
          (render (scout/view)))
       (auth/require ["scout" "admin"])))

(defroute "/points" []
  (->> #(do
          (points/fetch)
          (render (points/view)))
       (auth/require ["admin"])))

(defroute "/responses" []
  (->> #(do
          (responses/fetch)
          (render (responses/view)))
       (auth/require ["admin"])))

(defroute "/settings" []
  (->> #(do
          (settings/fetch)
          (render (settings/view)))
       (auth/require ["admin"])))

(defroute "/questions" []
  (->> #(do
          (questions/fetch)
          (render (questions/view)))
       (auth/require ["admin"])))

(js/addEventListener "popstate"
                     #(secretary/dispatch! (.-pathname js/location)))

(secretary/dispatch! (.-pathname js/location))
